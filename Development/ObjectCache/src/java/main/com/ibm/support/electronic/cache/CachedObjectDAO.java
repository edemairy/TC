package com.ibm.support.electronic.cache;

import com.ibm.support.electronic.cache.model.*;
import java.lang.*;

/**
 * This interface defines the methods to get, save (including insert/update depending on if the row already exists), delete a CachedObject from persistence storage, as well as to delete all CachedObject instances of one particular cache set from persistence storage. Implementations must have a constructor that takes a Properties object as the sole parameter.
 * 
 * Thread Safety:
 * The implementation of this interface does not have to be thread-safe
 */
public interface CachedObjectDAO {
    /**
     * Delete all cached objects of a given cache set
     * 
     * 
     * Parameters:
     * cacheSetName - the name of the cache set to delete. 
     * 
     * Return:
     * None
     * 
     * Exception:
     * PersistenceException if any error occurs
     * IllegalArgumentException if
     *  cacheSetName doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     * @param cacheSetName 
     */
    public void deleteByCacheSet(String cacheSetName);

    /**
     * Get a CachedObject by cache set name and ID
     * 
     * 
     * Parameters:
     * cacheSetName - the cache set name of the object. 
     * id - the id of the object. 
     * 
     * Return:
     * the CachedObject matching cache set name and id
     * Exception:
     * PersistenceException if any error occurs
     * IllegalArgumentException if
     *  cacheSetName doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     *  id doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     * @param id 
     * @param cacheSetName 
     * @return 
     */
    public CachedObject get(String cacheSetName, String id);

    /**
     * Insert or update a CachedObject to database
     * 
     * 
     * Parameters:
     * cachedObject - the cached object to save
     * 
     * Return:
     * None
     * 
     * Exception:
     * PersistenceException if any error occurs
     * IllegalArgumentException if
     *  cachedObject is null;
     * @param cachedObject 
     */
    public void save(CachedObject cachedObject);

    /**
     * Delete a particular cached object by cache set name and id
     * 
     * 
     * Parameters:
     * cacheSetName - the cache set name of the object to delete. 
     * id - the id of the object to delete. 
     * 
     * Return:
     * None
     * 
     * Exception:
     * PersistenceException if any error occurs
     * IllegalArgumentException if
     *  cacheSetName doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     *  id doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     * @param id 
     * @param cacheSetName 
     */
    public void delete(String cacheSetName, String id);
}

