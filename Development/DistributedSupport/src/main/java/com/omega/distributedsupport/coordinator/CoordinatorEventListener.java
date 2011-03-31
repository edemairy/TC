package com.omega.distributedsupport.coordinator;

/**
 * Interface specifying event handling methods for an object listening for
 * events from a Coordinator.
 * 
 * @author Ross Bayer
 *
 * @see Coordinator
 *
 */
public interface CoordinatorEventListener {

	/**
	 * Invoked when a ping is received from the Coordinator that is
	 * the master to indicate it is alive.
	 * 
	 * @param ping Contains information on the master.
	 * 
	 */
	public void masterPingReceived(CoordinatorPing ping);
	
	/**
	 * Invoked when the master stops pinging, which means it is considered down.
	 */
	public void masterDown();
	
	/**
	 * Invoked when the Coordinator that fired the event becomes the master.
	 */
	public void becameMaster();
	
}
