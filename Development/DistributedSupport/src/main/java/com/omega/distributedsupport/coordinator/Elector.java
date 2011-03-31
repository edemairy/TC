
package com.omega.distributedsupport.coordinator;

import java.util.*;

/**
 * Object that provides mechanism/algorithm for selection new master Coordinator
 * object when one goes down.
 * 
 * @author Ross Bayer
 *
 */
public interface Elector {

	/**
	 * Determines the new master Coordinator and returns its UUID.
	 * 
	 * @param coordinatorUUID The UUID of the Coordinator that invoked
	 * the method.
	 * @param peers The set of known peers.
	 * 
	 * @return The UUID of the new master.
	 */
	public UUID determineNewMaster(UUID coordinatorUUID, Set<UUID> peers);
	
	/**
	 * Determines the rank of the UUID.  The higher the number, the sooner
	 * the Coordinator with the provided UUID will become the master.
	 * Ranks start at size of peers + 1, and descend by 1 each.
	 * 
	 * @param coordinatorUUID The UUID of the Coordinator that invoked
	 * the method.
	 * @param peers The set of known peers.
	 * 
	 * @return The numerical rank of the coordinator.
	 */	
	public int getRank(UUID coordinatorUUID, Set<UUID> peers);
}
