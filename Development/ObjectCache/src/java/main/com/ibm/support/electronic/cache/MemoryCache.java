package com.ibm.support.electronic.cache;

import com.ibm.support.electronic.cache.model.*;
import java.lang.*;

/**
 * This interface defines the contract for managing the in-memory cache of the component. It defines the method to put, get, and remove CachedObject to/from an in-memory storage it maintains. It provides the method to clear the memory cache. In addition and most importantly, it provides the getReplaceableCachedObject() method that acts as the replacement strategy that picks the element to remove from the memory cache during memory shortage. Note that this interface doesn't throw any exception. This is intended because the memory cache should be as simple as possible and the strategy for determining the next replaceable object should be based on some kind of calculation, which should not throw exception. Implementations must have a constructor that takes a Properties object as the sole parameter.
 * 
 * Thread Safety:
 * The implementation of this interface does not have to be thread-safe
 */
public interface MemoryCache {
    /**
     * Get the next cached object that can be moved from memory into persistence storage, to make space for new cached objects.
     * 
     * 
     * Parameters:
     * None
     * 
     * Return:
     * the next replaceable object
     * Exception:
     * None
     * @return 
     */
    public CachedObject getReplaceableCachedObject();

    /**
     * Get a cached object from memory cache
     * 
     * 
     * Parameters:
     * id - the id of the cached object to get. 
     * 
     * Return:
     * the cached object of the given id
     * Exception:
     * IllegalArgumentException if
     *  id doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     * @param id 
     * @return 
     */
    public CachedObject get(String id);

    /**
     * Put a cached object into memory cache
     * 
     * 
     * Parameters:
     * id - the id of the cached object to put. 
     * cachedObject - the cached object to put
     * 
     * Return:
     * None
     * 
     * Exception:
     * IllegalArgumentException if
     *  id doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     *  cachedObject is null;
     * @param id 
     * @param cachedObject 
     */
    public void put(String id, CachedObject cachedObject);

    /**
     * Clear the memory cache
     * 
     * Parameters:
     * None
     * 
     * Return:
     * None
     * 
     * Exception:
     * None
     */
    public void clear();

    /**
     * Remove a cached object by id
     * 
     * 
     * Parameters:
     * id - the id of the cached object to remove. 
     * 
     * Return:
     * the removed cached object
     * Exception:
     * IllegalArgumentException if
     *  id doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     * @param id 
     * @return 
     */
    public CachedObject remove(String id);
}

