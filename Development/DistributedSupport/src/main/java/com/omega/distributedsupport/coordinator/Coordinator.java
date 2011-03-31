package com.omega.distributedsupport.coordinator;

import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.commons.logging.*;

/**
 * <p>
 * Class that provides support for failover/redundancy in clustered environments
 * as well as central task coordination.
 * </p>
 * <p>
 * Objects deriving from this class support failover/redundancy management in the
 * following way:  Each Coordinator object subscribes to a single multicast
 * group at a configurable multicast address and port number.  A single Coordinator
 * object is considered the master, or primary instance.  All others exist as
 * failovers in case the master goes down, stop responding, etc (for instance,
 * if the process crashes or the server goes down).  
 * </p>
 * <p>
 * The master will periodically send out a ping to the multicast address to indicate
 * that it is alive and operational. This ping is received by all other instances
 * of this class.  After a configurable amount of time, if no ping is received
 * from the master instance, it is assumed it is down, and one of the failovers
 * should take over its duties.
 * </p>
 * <p>
 * Depending on the Elector used for master selection, one of the
 * failovers will become the new master, and start broadcasting pings to the 
 * multicast address.
 * </p>
 * <p>
 * Objects interested in the status of failovers can listen for events generated
 * by this class via the CoordinatorEventListener interface, which allows an object
 * to be notified of events, such as when a Coordinator has become the master.
 * The design of this class is such that it can be used in a very flexible way,
 * depending on if redundancy is desired at the web application level, process level,
 * or thread level.
 * </p>
 * 
 * @author Ross Bayer
 * 
 * @see CoordinatorEventListener
 * @see Elector
 *
 */
public class Coordinator implements Runnable {

	/**
	 * Used for logging.
	 */
	protected Log logger = LogFactory.getLog(this.getClass()); 
	
	/**
	 * Name to identify Coordinator.
	 */
	protected String name = "Coordinator";
	/**
	 * Number of milliseconds between pings sent out by master.
	 */
	protected int millisBetweenPings = 5000;
	/**
	 * Number of milliseconds to wait for ping from master before it is considered
	 * down.
	 */
	protected int millisTillMasterDown = this.millisBetweenPings * 2;
	/**
	 * Number of milliseconds between announcements that this Coordinator is alive.
	 */
	protected int millisBetweenAnnouncements = this.millisBetweenPings * 3;
	/**
	 * Indicates whether this object is the master in the failover group or
	 * not.
	 */
	protected volatile boolean master;
	/**
	 * List of listener objects to notify of events.
	 */
	protected ArrayList<CoordinatorEventListener> listeners = new ArrayList<CoordinatorEventListener>();
	/**
	 * The UUID uniquely identifying this instance.
	 */
	protected UUID uuid;
	/**
	 * Multicast socket used to receive pings from master.
	 */
	protected MulticastSocket multicastSocket;
	/**
	 * Thread used to receive pings on.
	 */
	protected Thread pingReceiverThread;
	/**
	 * Timer used to initiate master pings sent out if this is the master.
	 */
	protected Timer timer;
	/**
	 * Indicates to stop receiver thread.
	 */
	protected volatile boolean stopThreads = false;
	/**
	 * Cached copy of machine's hostname
	 */
	protected String hostname;
	/**
	 * Cahced copy of machine's ip address
	 */
	protected String ipAddress;
	/**
	 * Flag to store state of whether the ping being sent by this object
	 * is the first ping.
	 */
	protected boolean firstPing = true;
	/**
	 * The multicast address.
	 */
	protected InetAddress groupAddress;
	/**
	 * The port to send/receive pings on.
	 */
	protected short port;
	/**
	 * The time at which the last ping was received.
	 */
	protected volatile Date lastPingReceived;
	/**
	 * set containing UUIDs of all Coordinator peers in multicast group.
	 */
	protected HashSet<UUID> peerIds = new HashSet<UUID>();
	/**
	 * Object used to determine master if the current one goes down.
	 */
	protected Elector elector;
	/**
	 * The UUID of the current master.
	 */
	protected UUID masterUUID;
	/**
	 * The count of masters that have been elected.
	 */
	protected long masterCount;
	/**
	 * TimerTask used by master to send pings
	 */
	protected TimerTask pingTask;
	/**
	 * TimerTask used by non-master instances to send out announcement msgs
	 * that they are part of the group.
	 */
	protected TimerTask announcementTask;
	/**
	 * TimerTask used to check for pings from the master.
	 */
	protected TimerTask checkPingTask;	
	
	/**
	 * Initializes a new Coordinator 
	 * 
	 * @throws CoordinatorException Thrown if the hostname or IP address
	 * cannot be retrieved for the localhost.
	 */
	public Coordinator() throws CoordinatorException
	{
		this.uuid = UUID.randomUUID();
		try
		{
			InetAddress address = InetAddress.getLocalHost();
			this.hostname = address.getHostName();
			this.ipAddress = address.getHostAddress();
		}
		catch(UnknownHostException uEx)
		{
			throw new CoordinatorException("Could not retrieve hostname and IP address of localhost.", uEx);
		}
	}
	
	/**
	 * Gets the name of the Coordinator
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name of the Coordinator
	 * 
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Sets the number of seconds between pings sent by the master instance.
	 * 
	 * @param secondsBetweenPings the secondsBetweenPings to set
	 */
	public void setSecondsBetweenPings(int secondsBetweenPings)
	{
		this.millisBetweenPings = secondsBetweenPings * 1000;
		this.millisTillMasterDown = this.millisBetweenPings * 2;
		this.millisBetweenAnnouncements = this.millisBetweenPings * 3;
	}

	/**
	 * Gets boolean indicating whether this provider is the master.
	 * 
	 * @return the master
	 */
	public boolean isMaster()
	{
		return master;
	}

	/**
	 * Sets the Coordinator instance as the master.
	 * 
	 * @param master the master to set
	 */
	public void setMaster(boolean master)
	{
		this.master = master;
	}
	
	/**
	 * Sets the multicast address that pings from the master will be sent
	 * out to.
	 * 
	 * @param groupAddress The multicast group address.
	 * 
	 * @throws Exception Thrown if the provided address is invalid.
	 */
	public void setGroupAddress(String groupAddress) throws Exception
	{
		this.groupAddress = InetAddress.getByName(groupAddress);
	}
	
	/**
	 * Sets the port number used to receive pings on.
	 * 
	 * @param port The port used to receive pings on from the master.
	 */
	public void setPort(short port)
	{
		this.port = port;
	}
	
	/**
	 * Sets the object used to determine the new master if the current one
	 * goes down.
	 * 
	 * @param elector the elector to set
	 */
	public void setElector(Elector elector)
	{
		this.elector = elector;
	}
	
	/**
	 * Sets events listeners for this Coordinator.
	 * 
	 * @param listeners The listeners for this Coordinator.
	 */
	public void setCoordinatorEventListeners(List<CoordinatorEventListener> listeners)
	{
		this.listeners.clear();
		this.listeners.addAll(listeners);
	}

	/**
	 * Adds a CoordinatorEventListener object to be notified when failover
	 * events occur.
	 * 
	 * @param listener The listener to add.
	 */
	public void addCoordinatorEventListener(CoordinatorEventListener listener)
	{
		synchronized(this.listeners)
		{
			this.listeners.add(listener);
		}
	}
	
	/**
	 * Returns the number of known peers in the Coordinator group.
	 * 
	 * @return The number of peers in the Coordinator group.
	 */
	public int getPeerCount()
	{
		return this.peerIds.size() + 1;
	}
	
	/**
	 * Returns set of IDs of all known Coordinators in the Coordinator
	 * group.
	 *  
	 * @return Set of IDs of all known Coordinators in the Coordinator group.
	 */
	public HashSet<UUID> getPeerIds()
	{
		HashSet<UUID> p = new HashSet<UUID>(this.peerIds);
		p.add(this.uuid);
		return p;
	}
	
	/**
	 * Users internal Elector to determine this Coordinator's rank in the
	 * group.  The rank determines the next master Coordinator, with the Coordinator
	 * with the highest rank becoming the master.
	 * 
	 * @return The rank of the Coordinator.  Ranks begin at {@link #getPeerCount()}
	 * and descend by 1 to the lowest-ranked Coordinator.
	 */
	public int getRank()
	{
		return this.elector.getRank(this.uuid, peerIds);
	}
	
	/**
	 * Fires the masterDown event to listeners.
	 */
	protected void onMasterDown()
	{
		synchronized(this.listeners)
		{
			for(CoordinatorEventListener listener : this.listeners)
			{
				listener.masterDown();
			}
		}
		
		//Determine new master
		this.peerIds.remove(this.masterUUID);
		this.masterUUID = this.elector.determineNewMaster(this.uuid, this.peerIds);
		
		if(this.masterUUID.equals(this.uuid))
		{
			this.onBecameMaster();
		}
	}
	
	/**
	 * Fires the becameMaster event to listeners.
	 */
	protected void onBecameMaster()
	{
		this.master = true;
		this.masterCount++;
		this.announcementTask.cancel();
		this.checkPingTask.cancel();
		this.pingTask = new PingTask();
		this.timer.schedule(pingTask, this.millisBetweenPings, 
				this.millisBetweenPings);
		
		synchronized(this.listeners)
		{
			for(CoordinatorEventListener listener : this.listeners)
			{
				listener.becameMaster();
			}
		}
	}
	
	/**
	 * Fires the masterPingReceived event to listeners.
	 * 
	 * @param ping The data from the ping received from the master.
	 */
	protected void onMasterPingReceived(CoordinatorPing ping)
	{
		synchronized(this.listeners)
		{
			for(CoordinatorEventListener listener : this.listeners)
			{
				listener.masterPingReceived(ping);
			}
		}
	}
	
	/**
	 * Starts the Coordinator, including creating sockets and beginning
	 * to listen for pings.
	 *
	 * @throws IOException Thrown if an error occurs trying to create the 
	 * multicast socket.
	 */
	public synchronized void start() throws IOException
	{
		if(this.pingReceiverThread != null)
		{
			return;
		}
		
		this.multicastSocket = new MulticastSocket(this.port);
		this.multicastSocket.setSoTimeout(50);
		this.multicastSocket.joinGroup(this.groupAddress);
		this.multicastSocket.setLoopbackMode(false);
		
		//start listening for pings
		this.pingReceiverThread = new Thread(this);
		this.pingReceiverThread.setDaemon(true);
		this.pingReceiverThread.setName("CoordinatorPing:" + this.name + "->" + this.uuid.toString());
		this.pingReceiverThread.start();
		
		//Start checking for pings
		this.lastPingReceived = new Date();
		this.timer = new Timer("CoordinatorTimer:" + this.name + "->" + this.uuid.toString(), true);
		
		if(this.master)
		{
			this.pingTask = new PingTask();
			this.timer.schedule(this.pingTask, this.millisBetweenPings, 
					this.millisBetweenPings);
		}
		else
		{
			this.announcementTask = new AnnouncementTask();
			this.timer.schedule(this.announcementTask, 
					this.millisBetweenAnnouncements,
					this.millisBetweenAnnouncements);
			this.checkPingTask = new PingCheckTask();
			this.timer.schedule(this.checkPingTask, 
					this.millisTillMasterDown, this.millisTillMasterDown);
		}
	}
	
	/**
	 * Stops the Coordinator.  This will end all receiving and transmitting.
	 */
	public void stop()
	{
		this.stopThreads = true;
		
		this.timer.cancel();
		
		try
		{
			this.pingReceiverThread.join();
		}
		catch(InterruptedException iEx) { }
		
		this.pingReceiverThread = null;
	}

	/**
	 * Implementation of receiving thread that listens for messages received
	 * via the multicast socket.
	 */
	@Override
	public void run()
	{
		boolean dataReceived = false;
		while(!this.stopThreads)
		{
			byte[] buffer = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			try
			{
				this.multicastSocket.receive(packet);
				dataReceived = true;
			}
			catch(SocketTimeoutException sEx)
			{
				dataReceived = false;
			}
			catch(IOException iEx)
			{
				dataReceived = false;
				this.logger.error("An I/O error occured while trying to receive.", iEx);
				return;
			}
			
			if(dataReceived)
			{				
				byte[] msg = packet.getData();
				
				if(msg.length <= CoordinatorPing.HEADER_SIZE)
				{
					this.logger.warn("Received less bytes that header size, dropping data.");
				}
				else
				{
					ByteArrayInputStream bis = new ByteArrayInputStream(msg);
					DataInputStream dis = new DataInputStream(bis);
					
					short payloadSize = 0;
					long checksum = 0L;
					try
					{
						payloadSize = dis.readShort();
						checksum = dis.readLong();
					}
					catch(IOException iEx)
					{
						this.logger.error("Received I/O exception while reading header info from received packets.", iEx);	
					}
					
					byte[] payload = new byte[payloadSize];
					int readLength = 0;
					try
					{
						readLength = dis.read(payload);
					}
					catch(IOException iEx)
					{
						this.logger.error("Received I/O exception while reading payload from received packet.", iEx);
					}
					
					if(readLength != payloadSize)
					{
						this.logger.error(String.format("Received less bytes in payload than specified in message header. Expected: %d Received: %d. Dropping data.",
								payloadSize, readLength));
					}
					else
					{
						this.processReceivedMessage(payload, checksum);
					}
				}
			}
			
			try
			{
				Thread.sleep(50);
			}
			catch(InterruptedException iEx)
			{
				return;
			}
		}
	}
	
	/**
	 * Handles message received from multicast group.
	 * 
	 * @param messageBytes The bytes received 
	 * @param checksum The checksum in message header.
	 */
	protected void processReceivedMessage(byte[] messageBytes, long checksum)
	{
		try
		{
			CoordinatorMsg recvMsg = CoordinatorMsg.fromBytes(messageBytes, checksum);
			if(recvMsg instanceof CoordinatorPing)
			{
				CoordinatorPing ping = (CoordinatorPing)recvMsg; 
				if(!ping.uuid.equals(this.uuid))
				{
					this.lastPingReceived = new Date();
					this.onMasterPingReceived(ping);
				}
			}
			else
			{
				//A new coordinator has joined the group, add to list of known
				//coordinators
				this.peerIds.add(recvMsg.uuid);
			}
		}
		catch(IOException iEx)
		{
			this.logger.error("Received invalid CoordinatorMsg object.", iEx);
		}
	}
	
	/**
	 * Sends out a single ping to the multicast group.
	 * 
	 * @return The timestamp of when the ping was sent.
	 * 
	 * @throws IOException Thrown if the ping cannot be transmitted.
	 */
	protected Date ping() throws IOException
	{
		CoordinatorPing p = new CoordinatorPing();
		p.firstPing = this.firstPing;
		p.masterHostname = this.hostname;
		p.masterIP = this.ipAddress;
		p.uuid = this.uuid;
		p.masterCount = this.masterCount;
		
		this.firstPing = false;
	
		return this.sendMsg(p);
	}
	
	/**
	 * Sends out a single announcement msg to the multicast group.
	 * 
	 * @return The timestamp of when the msg was sent.
	 * 
	 * @throws IOException Thrown if the msg cannot be sent.
	 */
	protected Date announce() throws IOException
	{
		CoordinatorMsg msg = new CoordinatorMsg();
		msg.uuid = this.uuid;
		
		return this.sendMsg(msg);
	}
	
	/**
	 * Sends out message to multicast group
	 * 
	 * @param msg The message to send
	 * 
	 * @return The timestamp of when the message was sent.
	 * 
	 * @throws IOException Thrown if an I/O error occurs.
	 */
	protected Date sendMsg(CoordinatorMsg msg) throws IOException
	{
		byte[] pingBytes = msg.toBytes();
		
		DatagramPacket packet = new DatagramPacket(pingBytes, pingBytes.length,
				this.groupAddress, this.port);
		this.multicastSocket.send(packet);
		
		return msg.timestamp;
	}
	
	/**
	 * TimerTask used to check to see if the master is down or up
	 * based on the last time a ping was received from it.
	 * 
	 * @author Ross Bayer
	 */
	protected class PingCheckTask extends TimerTask
	{
		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run()
		{
			Date now = new Date();
			
			if(now.getTime() - lastPingReceived.getTime() > millisTillMasterDown)
			{
				onMasterDown();
			}
		}
	}
	
	/**
	 * TimerTask used to send out master pings.
	 * 
	 * @author Ross Bayer
	 *
	 */
	protected class PingTask extends TimerTask
	{
		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run()
		{
			try
			{
				ping();
			}
			catch(IOException iEx)
			{
				logger.error("Could not send ping.", iEx);
			}
		}		
	}
	
	/**
	 * TimerTask used to send out Coordinator announcements.
	 * 
	 * @author Ross Bayer
	 */
	protected class AnnouncementTask extends TimerTask
	{
		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run()
		{
			try
			{
				announce();
			}
			catch(IOException iEx)
			{
				logger.error("Could not send announcement.", iEx);
			}
		}	
	}
}
