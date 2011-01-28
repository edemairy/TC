
package com.omega.distributedsupport.coordinator;

import java.io.*;
import java.util.*;
import java.util.zip.CRC32;

/**
 * Object constituting message sent between coordinators via multicast.
 * <p>
 * CoordinatorMsg objects transmitted over the wire will be comprised into the
 * following byte message structure:
 * </p>
 * <ul>
 * <li>Payload Size -> 2 byte short containing size of message payload.</li>
 * <li>Checksum -> 8 byte CRC-32 checksum of the payload.</li>
 * <li>Payload -> N bytes comprising a CoordinatorMsg object serialized via
 * java.io.ObjectOutputStream.
 * </ul>
 * 
 * @author Ross Bayer
 *
 */
public class CoordinatorMsg implements Serializable {
	
	/**
	 * Serialization Id number used by java io.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The size of the header portion of a CoordinatorPing msg transmitted
	 * via a socket.
	 */
	public static final int HEADER_SIZE = 10;
	
	/**
	 * The time at which the message was created.
	 */
	protected Date timestamp = new Date();
	/**
	 * UUID uniquely identifying the Coordinator object that generated the
	 * message
	 */
	protected UUID uuid;
	
	/**
	 * Gets Date at which object was created.
	 * 
	 * @return the timestamp
	 */
	public Date getTimestamp()
	{
		return timestamp;
	}

	/**
	 * Gets the UUID of the Coordinator that generated the message.
	 * 
	 * @return the uuid
	 */
	public UUID getUuid()
	{
		return uuid;
	}

	/**
	 * Sets the UUID of the Coordinator that generated the message.
	 * 
	 * @param uuid the uuid to set
	 */
	public void setUuid(UUID uuid)
	{
		this.uuid = uuid;
	}

	/**
	 * Converts the CoordinatorMsg object to a byte message that can be sent
	 * over a socket or other stream.
	 * 
	 * @return The CoordinatorPing object in byte array form.
	 * 
	 * @throws IOException Thrown if an error occurs converting the CoordinatorPing
	 * object to a byte array.
	 */
	public byte[] toBytes() throws IOException
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutputStream oStream = new ObjectOutputStream(stream);
	
		oStream.writeObject(this);
		oStream.close();
		
		ByteArrayOutputStream finalStream = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(finalStream);
		
		int payloadSize = stream.size();
		byte[] payload = stream.toByteArray();
		long checksum = getChecksum(payload);
		
		dos.writeShort(payloadSize);
		dos.writeLong(checksum);
		dos.write(payload);
		dos.close();
		
		return finalStream.toByteArray();
	}

	/**
	 * Converts a Coordinator object in byte message form into a CoordinatorPing
	 * object.  In addition to deserializing the byte array, this method
	 * also checks the data integrity via the checksum.
	 *  
	 * @param bytes The byte array to convert.
	 * @param checksum The checksum receiving with the byte array.
	 * 
	 * @return The deserialized CoordinatorPing object.
	 * 
	 * @throws IOException Thrown if the checksum does not match the provided
	 * array.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromBytes(byte[] bytes, long checksum) throws IOException
	{
		if(checksum != getChecksum(bytes))
		{
			throw new IOException("The checksum does not match the provided byte array.");
		}
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bis);
		
		try
		{
			return (T)ois.readObject();
		}
		catch(ClassNotFoundException cEx)
		{
			throw new IOException("Received invalid ping data that cannot be converted.", cEx);
		}	
	}
	
	/**
	 * Gets CRC-32 checksum for CoordinatorPing object converted to byte[] array.
	 * 
	 * @param pingBytes The byte array containing the CoordinatorPing object in
	 * serialized form.
	 * 
	 * @return The CRC-32 checksum.
	 */
	protected static long getChecksum(byte[] pingBytes)
	{
		CRC32 crc = new CRC32();
		crc.update(pingBytes);
		return crc.getValue();
	}
}
