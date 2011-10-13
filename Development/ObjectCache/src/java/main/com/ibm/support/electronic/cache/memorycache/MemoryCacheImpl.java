package com.ibm.support.electronic.cache.memorycache;

import com.ibm.support.electronic.cache.Helper;
import com.ibm.support.electronic.cache.ObjectCacheConfigurationException;
import com.ibm.support.electronic.cache.model.*;
import java.util.*;
import java.lang.*;

/**
 * This is the MemoryCache implementation that uses Least Recently Used strategy to get the element to remove from memory cache. It maintains a LinkedHashMap to store the cached objects so that it can get the least recently used element from the map, which will be the element to remove. The LinkedHashMap can achieve this function because both its put and get methods result in a structural modification (see http://download.oracle.com/javase/6/docs/api/java/util/LinkedHashMap.html for details)
 * 
 * Thread Safety:
 * This class is not thread-safe because it has mutable state. The caller should use it with proper synchronization.
 */
public class MemoryCacheImpl {

    /**
     * <p>
     * The default load factor of LinkedHashMap.
     * </p>
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    /**
     * <p>
     * The default initial capacity of LinkedHashMap.
     * </p>
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 1000;
    /**
     * The memory cache whose key is the id of the cached object, and value is the cached object itself. 
     * LegalValue:
     * It cannot be null but can be empty. The key can't be null. The contained values can't be null. 
     * Initialization and Mutability:
     * It's initialized within constructor, won't change afterwards. 
     * Usage:
     * It is used in clear(), put(), remove(), get(), getReplaceableCachedObject(), MemoryCacheImpl() (for initialization).
     */
    private final LinkedHashMap<String, CachedObject> cache;

    /**
     * Create an instance of the class
     * 
     * 
     * Parameters:
     * prop - configuration parameters
     * 
     * Exception:
     * ObjectCacheConfigurationException if any error occursIllegalArgumentException if
     *  prop is null;
     * 
     * Implementation Notes:
     *   initialCapacity,loadFactor = extract the namesake properties from 
     * prop(if null or empty, use default value in CS3.1)
     *   cache = new LinkedHashMap<String, CachedObject>(initialCapacity, loadFactor, true);
     * @param prop 
     */
    public MemoryCacheImpl(Properties prop) {
        Helper.checkNull(prop, "prop should not be null");
        int initialCapacity = Helper.getIntProperty(prop, "initialCapacity", DEFAULT_INITIAL_CAPACITY);
        float loadFactor = getFloatProperty(prop, "loadFactor", DEFAULT_LOAD_FACTOR);
        cache = new LinkedHashMap<String, CachedObject>(initialCapacity, loadFactor, true);
    }

    /**
     * Get the next cached object that can be moved from memory into persistence storage, to make space for new cached objects. This method simply returns the least recently used object.
     * 
     * 
     * Parameters:
     * None
     * 
     * Return:
     * the next replaceable object
     * Exception:
     * None
     * 
     * Implementation Notes:
     * 1 If cache.values().iterator().hasNext() then
     *         return cache.values().iterator().next();
     * 2 Else
     *         return null;
     * @return 
     */
    public CachedObject getReplaceableCachedObject() {
        Collection<CachedObject> values = cache.values();
        if (!values.isEmpty()) {
            return values.iterator().next();
        }
        return null;
    }

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
     * 
     * Implementation Notes:
     *         return cache.get(id);
     * @param id 
     * @return 
     */
    public CachedObject get(String id) {
        Helper.checkNullOrEmpty(id, "id should not be null or empty");
        return cache.get(id);
    }

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
     * 
     * Implementation Notes:
     *         cache.put(id, cachedObject);
     * @param id 
     * @param cachedObject 
     */
    public void put(String id, CachedObject cachedObject) {
        Helper.checkNullOrEmpty(id, "id should not be null or empty");
        Helper.checkNull(cachedObject, "cachedObject should not be null");
        cache.put(id, cachedObject);
    }

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
     * 
     * Implementation Notes:
     *         cache.clear();
     */
    public void clear() {
        cache.clear();
    }

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
     * 
     * Implementation Notes:
     *         return cache.remove(id);
     * @param id 
     * @return 
     */
    public CachedObject remove(String id) {
        Helper.checkNullOrEmpty(id, "id should not be null or empty");
        return cache.remove(id);
    }

    /**
     * <p>
     * Gets the value of optional property as a float value.
     * </p>
     *
     * @param prop the Properties to get the value from.
     * @param key the key of the property to get its value.
     * @param defaultValue the default value to return if optional property is not specified.
     * @return the property value.
     * @throws ObjectCacheConfigurationException if the value is not String, or if value is an empty String, or if
     *         value cannot be parsed as float, or if value is not positive.
     */
    private static float getFloatProperty(Properties prop, String key, float defaultValue) {
        String value = Helper.getProperty(prop, key, false);
        if (value == null) {
            return defaultValue;
        }
        try {
            float floatValue = Float.parseFloat(value);
            if (floatValue <= 0) {
                throw new ObjectCacheConfigurationException(Helper.concat("The value of property '", key,
                        "' should be positive"));
            }
            return floatValue;
        } catch (NumberFormatException e) {
            throw new ObjectCacheConfigurationException(Helper.concat("The value of property '", key,
                    "' cannot be parsed as float"));
        }
    }
}
