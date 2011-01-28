package com.omega.distributedsupport.coordinator;

import java.util.*;

/**
 * Elector that determines the new master by comparing the hashes of
 * all UUIDs, and returning the UUID that has the highest numerical hash.
 * 
 * @author Ross Bayer
 *
 */
public class HighestHashElector implements Elector {

	/**
	 * Implement master determination algorithm.
	 * 
	 * @see Elector#determineNewMaster(UUID, Set)
	 */
	@Override
	public UUID determineNewMaster(UUID coordinatorUUID, Set<UUID> peers)
	{
		HashMap<Integer, UUID> map = this.getHashes(peers);
		int coordHash = coordinatorUUID.hashCode();
		map.put(coordHash, coordinatorUUID);
		
		TreeSet<Integer> sortedKeys = new TreeSet<Integer>(map.keySet());
		
		Integer topHash = sortedKeys.last();
		
		return map.get(topHash);
	}

	
	
	/**
	 * Implement rank determination algorithm.
	 * 
	 * @see Elector#getRank(UUID, Set)
	 */
	@Override
	public int getRank(UUID coordinatorUUID, Set<UUID> peers)
	{
		HashMap<Integer, UUID> map = this.getHashes(peers);
		int coordHash = coordinatorUUID.hashCode();
		map.put(coordHash, coordinatorUUID);
		
		TreeSet<Integer> sortedKeys = new TreeSet<Integer>(map.keySet());
		
		int count = sortedKeys.size();
		// a, b, c, d, e, f
		for(Integer key : sortedKeys)
		{
			if(key == coordHash)
			{
				return count;
			}
			count--;
		}
		
		return -1;
	}

	/**
	 * Generates map of peer hashes to their UUIDs.
	 * 
	 * @param peers The set of known peers.
	 * 
	 * @return The map of hashes to UUIDs for peers.
	 */
	protected HashMap<Integer, UUID> getHashes(Set<UUID> peers)
	{
		HashMap<Integer, UUID> set = new HashMap<Integer, UUID>();
		for(UUID u : peers)
		{
			set.put(u.hashCode(), u);
		}
		
		return set;
	}
}
