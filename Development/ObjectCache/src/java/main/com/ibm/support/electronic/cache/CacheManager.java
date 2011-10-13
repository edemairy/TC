package com.ibm.support.electronic.cache;

import com.ibm.support.electronic.cache.model.*;
import java.io.*;
import java.lang.*;
import java.sql.Timestamp;

/**
 * This interface defines the contract for managing an object cache. It provides method to get objects from cache, put objects into cache, and clear the cache. In addition, it provides a way to get and print out cache statistics. Before using the cache, the clear() method should first be called to remove any old data in the persistence storage.
 * 
 * Thread Safety:
 * The implementation of this interface must be thread-safe
 * 
 * Changed in version1.1:
 * Extra API is added to support expiration on a cached object, one API adds an extra milliseconds since current time as parameter, the other uses expiration timestamp to indicate the expiration timestamp.
 */
public interface CacheManager {
    /**
     * Get the statistics of the cache
     * 
     * Parameters:
     * None
     * 
     * Return:
     * the statistics of the cache
     * Exception:
     * None
     * @return 
     */
    public CacheStatistics getCacheStatistics();

    /**
     * Print the cache statistics to the given print writer. This method will not close the print writer.
     * 
     * Parameters:
     * printWriter - the print writer to use
     * 
     * Return:None
     * 
     * Exception:
     * StatisticsPrintingException if any error occurs
     * IllegalArgumentException if
     *  printWriter is null;
     * @param printWriter 
     */
    public void printStatistics(PrintWriter printWriter) throws StatisticsPrintingException;

    /**
     * Get a cached object by id.
     * 
     * 
     * Parameters:
     * id - the id of the object to get. If it's null or empty, simply return null. 
     * 
     * Return:
     * the object with given id from the cache. It could be null if the object with given id doesn't exist or if any error occurs within the component.
     * Exception:
     * None
     * @param id 
     * @return 
     */
    public Serializable get(String id);

    /**
     * Put an object into cache.
     * 
     * 
     * Parameters:
     * id - the id of the object to put. 
     * object - the object to put into cache. If it's null, it means the item with given id in the cache should be removed from the cache. If an item already exists in the cache with the same id, that item is replaced. Note that in this case, the statistics of this id is reset.
     * 
     * Return:
     * None
     * 
     * Exception:
     * PersistenceException if any persistence-related error occurs
     * ObjectCacheException if any other error occurs
     * IllegalArgumentException if
     *  id doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     * @param id 
     * @param object 
     */
    public void put(String id, Serializable object) throws PersistenceException, ObjectCacheException;

    /**
     * Clear the cache. The memory cache will be cleared and all database rows corresponding to the cache set managed by the instance of this class will be deleted. Cache statistics will be cleared too.
     * 
     * 
     * Parameters:
     * None
     * 
     * Return:
     * None
     * 
     * Exception:
     * PersistenceException if any persistence-related error occurs
     */
    public void clear() throws PersistenceException;

    /**
     * Put an object into cache with expiration millisenconds which indicates the object will be purged after the current time plus milliseconds. This method is added in version1.1.
     * 
     * 
     * Parameters:
     * id - the id of the object to put. 
     * object - the object to put into cache. If it's null, it means the item with given id in the cache should be removed from the cache. If an item already exists in the cache with the same id, that item is replaced. Note that in this case, the statistics of this id is reset.
     * milliseconds - the milliseconds, after current time plus milliseconds, the object will be purged, MUST be positive.
     * 
     * Return:
     * None
     * 
     * Exception:
     * PersistenceException if any persistence-related error occurs
     * ObjectCacheException if any other error occurs
     * IllegalArgumentException if
     *  id doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     *   if milliseconds is not positive.
     * @param id 
     * @param object 
     * @param milliseconds 
     */
    public void put(String id, Serializable object, long milliseconds) throws PersistenceException, ObjectCacheException;

    /**
     * Put an object into cache with expiration timestamp which indicates the object will be purged after the expiration timestamp.This method is added in version1.1.
     * 
     * 
     * Parameters:
     * id - the id of the object to put. 
     * object - the object to put into cache. If it's null, it means the item with given id in the cache should be removed from the cache. If an item already exists in the cache with the same id, that item is replaced. Note that in this case, the statistics of this id is reset.
     * expirationTimstamp - the expiration timestamp, after that the object will be purged, can not be null
     * 
     * Return:
     * None
     * 
     * Exception:
     * PersistenceException if any persistence-related error occurs
     * ObjectCacheException if any other error occurs
     * IllegalArgumentException if
     *  id doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     *   if expirationTimestamp is null or is not after current timestamp.
     * @param expirationTimestamp 
     * @param id 
     * @param object 
     */
    public void put(String id, Serializable object, Timestamp expirationTimestamp) throws PersistenceException, ObjectCacheException;
}

