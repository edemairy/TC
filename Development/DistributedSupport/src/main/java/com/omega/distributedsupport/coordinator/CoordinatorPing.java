package com.omega.distributedsupport.coordinator;

/**
 * Object containing data received in a ping message
 * received from a Coordinator that is the master.
 * 
 * @author Ross Bayer
 *
 */
public class CoordinatorPing extends CoordinatorMsg {
	
	/**
	 * Serialization Id number used by java io.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Indicates whether this is the first ping sent by the new master.
	 */
	protected boolean firstPing;
	/**
	 * The IP address of the master.
	 */
	protected String masterIP;
	/**
	 * The hostname of the master.
	 */
	protected String masterHostname;
	/**
	 * Incremental account increased each time a new master is created.  This is
	 * for tracking for potential multiple masters.
	 */
	protected long masterCount;
	
	/**
	 * Gets boolean indicating whether this is the first ping sent
	 * by the master instance.
	 * 
	 * @return the firstPing
	 */
	public boolean isFirstPing()
	{
		return firstPing;
	}
	
	/**
	 * Sets boolean indicating whether this is the first ping setn
	 * by the master instance.
	 * 
	 * @param firstPing the firstPing to set
	 */
	public void setFirstPing(boolean firstPing)
	{
		this.firstPing = firstPing;
	}
	
	/**
	 * Gets the master's IP address.
	 * 
	 * @return the masterIP
	 */
	public String getMasterIP()
	{
		return masterIP;
	}
	
	/**
	 * Sets the master's IP address.
	 * 
	 * @param masterIP the masterIP to set
	 */
	public void setMasterIP(String masterIP)
	{
		this.masterIP = masterIP;
	}
	
	/**
	 * Gets the hostname of the master.
	 * 
	 * @return the masterHostname
	 */
	public String getMasterHostname()
	{
		return masterHostname;
	}
	
	/**
	 * Sets the master's hostname.
	 * 
	 * @param masterHostname the masterHostname to set
	 */
	public void setMasterHostname(String masterHostname)
	{
		this.masterHostname = masterHostname;
	}

	/**
	 * Gets the current master count.
	 * 
	 * @return the masterCount
	 */
	public long getMasterCount()
	{
		return masterCount;
	}

	/**
	 * Sets the current master count.
	 * 
	 * @param masterCount the masterCount to set
	 */
	public void setMasterCount(long masterCount)
	{
		this.masterCount = masterCount;
	}
}
