package com.omega.distributedsupport.coordinator;

/**
 * Event listener adapter that provides empty implementations for
 * methods of CoordinatorEventListener.
 * 
 * @author Ross Bayer
 *
 */
public class CoordinatorEventAdapter implements CoordinatorEventListener {

	/**
	 * Implement master transition handling.
	 * 
	 * @see CoordinatorEventListener#becameMaster()
	 */
	@Override
	public void becameMaster()
	{
		return;
	}

	/**
	 * Implement master transition handling.
	 * 
	 * @see CoordinatorEventListener#masterDown()
	 */
	@Override
	public void masterDown()
	{
		return;
	}

	/**
	 * Implement master interaction handling.
	 * 
	 * @see CoordinatorEventListener#masterPingReceived(CoordinatorPing)
	 */
	@Override
	public void masterPingReceived(CoordinatorPing ping)
	{
		return;
	}

}
