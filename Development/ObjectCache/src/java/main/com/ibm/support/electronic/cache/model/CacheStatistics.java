package com.ibm.support.electronic.cache.model;

import java.util.*;

/**
 * This is a simple class that stores statistics about the cache. Its setter/getter doesn't perform validation.
 * Thread Safety:
 * This class is not thread-safe because it's mutable
 */
public class CacheStatistics {
    /**
     * The access counts of each id (or key) in the cache, including in-memory cache and persistence storage. The key is the id, and value is the access count. It has both getter and setter.
     * LegalValue:
     * It can be any value. 
     * Initialization and Mutability:
     * It does not need to be initialized when the instance is created. 
     * Usage:
     * It is used in getAccessCountsById(), setAccessCountsById().
     */
    private Map<String, Integer> accessCountsById;

    /**
     * The number of items in memory cache. It has both getter and setter.
     * LegalValue:
     * It can be any value.
     * Initialization and Mutability:
     * It does not need to be initialized when the instance is created. 
     * Usage:
     * It is used in getInMemoryItemCount(), setInMemoryItemCount().
     */
    private int inMemoryItemCount;

    /**
     * The number of items in persistence storage. It has both getter and setter.
     * LegalValue:
     * It can be any value.
     * Initialization and Mutability:
     * It does not need to be initialized when the instance is created. 
     * Usage:
     * It is used in getPersistedItemCount(), setPersistedItemCount().
     */
    private int persistedItemCount;

    /**
     * Number of misses, where an object is requested that is not in the cache. It has both getter and setter.
     * LegalValue:
     * It can be any value.
     * Initialization and Mutability:
     * It does not need to be initialized when the instance is created. 
     * Usage:
     * It is used in getMissCount(), setMissCount().
     */
    private int missCount;

    /**
     * This is the default constructor for the class.
     * 
     * Parameters:
     * None
     * 
     * Exception:
     * None
     * 
     * Implementation Notes:
     * Do nothing
     */
    public CacheStatistics() {
    }

    /**
     * Getter for the namesake instance variable. 
     * Simply return the namesake instance variable.
     * @return 
     */
    public Map<String, Integer> getAccessCountsById() {
        return this.accessCountsById;
    }

    /**
     * Getter for the namesake instance variable. 
     * Simply return the namesake instance variable.
     * @return 
     */
    public int getMissCount() {
        return missCount;
    }

    /**
     * Getter for the namesake instance variable. 
     * Simply return the namesake instance variable.
     * @return 
     */
    public int getPersistedItemCount() {
        return persistedItemCount;
    }

    /**
     * Getter for the namesake instance variable. 
     * Simply return the namesake instance variable.
     * @return 
     */
    public int getInMemoryItemCount() {
        return inMemoryItemCount;
    }

    /**
     * Setter for the namesake instance variable. 
     * Simply assign the value to the instance variable.
     * 
     * Parameters:
     * accessCountsById - The access counts of each id (or key) in the cache, including in-memory cache and persistence storage. The key is the id, and value is the access count. 
     * @param accessCountsById 
     */
    public void setAccessCountsById(Map<String, Integer> accessCountsById) {
        this.accessCountsById = accessCountsById;
    }

    /**
     * Setter for the namesake instance variable. 
     * Simply assign the value to the instance variable.
     * 
     * Parameters:
     * missCount - Number of misses, where an object is requested that is not in the cache. 
     * @param missCount 
     */
    public void setMissCount(int missCount) {
        this.missCount = missCount;
    }

    /**
     * Setter for the namesake instance variable. 
     * Simply assign the value to the instance variable.
     * 
     * Parameters:
     * persistedItemCount - The number of items in persistence storage. 
     * @param persistedItemCount 
     */
    public void setPersistedItemCount(int persistedItemCount) {
        this.persistedItemCount = persistedItemCount;
    }

    /**
     * Setter for the namesake instance variable. 
     * Simply assign the value to the instance variable.
     * 
     * Parameters:
     * inMemoryItemCount - The number of items in memory cache. 
     * @param inMemoryItemCount 
     */
    public void setInMemoryItemCount(int inMemoryItemCount) {
        this.inMemoryItemCount = inMemoryItemCount;                
    }
}

