package com.ibm.support.electronic.cache.cachemanager;

import com.ibm.support.electronic.cache.model.*;
import org.apache.log4j.*;
import java.util.*;
import java.io.*;
import com.ibm.support.electronic.cache.*;
import java.lang.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;

/**
 * This is the default implementation of CacheManager. The object to put into the cache must implement Serializable, and must be responsible for its own serialization (writing its own readObject() and writeObject()) if the serialization of its own involves custom behaviors. It uses MemoryCache to manage the in-memory cache and CachedObjectDAO to manage the database table. MemoryCache also provides the replacement strategy for picking the element to remove from memory cache and put to database when the memory cache is out of configured memory.
 * 
 * Thread Safety:
 * This class is thread-safe because it's properly synchronized. The synchronization guarantees that the mutable state of the class is always modified by one single thread at a time.
 * 
 * Changed in version1.1:
 * The added API is implemented in v1.1 to support expiration timestamp on an object. And -getCachedObject(id:String):CachedObject from v1.0 is modified:when the current time is after the expiration timestamp, the object is not returned and it is purged from the cache.
 */
public class CacheManagerImpl implements CacheManager {

    /**
     * <p>
     * The default class name to use for CacheObjectDAO implementation if not specified in configuration.
     * </p>
     */
    private static final String DEFAULT_DAO_CLASS = "com.ibm.support.electronic.cache.dao.CachedObjectDAOImpl";
    /**
     * <p>
     * The default class name to use for MemoryCache implementation if not specified in configuration.
     * </p>
     */
    private static final String DEFAULT_CACHE_CLASS = "com.ibm.support.electronic.cache.memorycache.MemoryCacheImpl";
    /**
     * <p>
     * The default class name to use for CacheStatisticsPrinter implementation if not specified in configuration.
     * </p>
     */
    private static final String DEFAULT_PRINTER_CLASS = "com.ibm.support.electronic.cache.printer."
            + "CacheStatisticsPrinterImpl";
    /**
     * <p>
     * The number of bytes of 1 kilobytes.
     * </p>
     */
    private static final int KILOBYTES = 1024;
    /**
     * <p>
     * n The default cache set' name to use if not specified in configuration.
     * </p>
     */
    private static final String DEFAULT_CACHE_SET_NAME = "default";
    /**
     * The logger used for logging. 
     * LegalValue:
     * It's a constant. So it can only be that constant value
     * Initialization and Mutability:
     * It is final and won't change once it is initialized as part of variable declaration to: Logger.getLogger(CacheManagerImpl.class). 
     * Usage:
     * It is used throughout the class wherever logging is needed.
     */
    private static final Logger LOGGER = Logger.getLogger(CacheManagerImpl.class);
    /**
     * The file path of the default configuration file. 
     * LegalValue:
     * It's a constant. So it can only be that constant value
     * Initialization and Mutability:
     * It is final and won't change once it is initialized as part of variable declaration to: "config.properties". 
     * Usage:
     * It is used in CacheManagerImpl() (for initialization).
     */
    public static final String DEFAULT_CONFIG_PATH = "config.properties";
    /**
     * The name of the cache set this cache manager operates on. 
     * LegalValue:
     * It cannot be null but can be empty.
     * Initialization and Mutability:
     * It's initialized within constructor, won't change afterwards. 
     * Usage:
     * It is used in clear(), removeCachedObject(), put(), CacheManagerImpl() (for initialization), getCachedObject().
     */
    private final String cacheSetName;
    /**
     * The max memory size in bytes. 
     * LegalValue:
     * It must be non-negative.
     * Initialization and Mutability:
     * It's initialized within constructor, won't change afterwards. 
     * Usage:
     * It is used in CacheManagerImpl() (for initialization), putCachedObject().
     */
    private final long maxMemorySize;
    /**
     * The CachedObjectDAO instance used to perform persistence operation on CachedObject. 
     * LegalValue:
     * It cannot be null.
     * Initialization and Mutability:
     * It's initialized within constructor, won't change afterwards. 
     * Usage:
     * It is used in clear(), removeCachedObject(), CacheManagerImpl() (for initialization), getCachedObject(), putCachedObject().
     */
    private final CachedObjectDAO cachedObjectDAO;
    /**
     * The cache statistics printer. 
     * LegalValue:
     * It cannot be null.
     * Initialization and Mutability:
     * It's initialized within constructor, won't change afterwards. 
     * Usage:
     * It is used in CacheManagerImpl() (for initialization), printStatistics().
     */
    private final CacheStatisticsPrinter cacheStatisticsPrinter;
    /**
     * The memory cache to use, which not only stores cached objects in memory but also provides the replacement strategy when memory shortage occurs. 
     * LegalValue:
     * It cannot be null.
     * Initialization and Mutability:
     * It's initialized within constructor, won't change afterwards. 
     * Usage:
     * It is used in clear(), removeCachedObject(), CacheManagerImpl() (for initialization), getCachedObject(), putIntoMemoryCache(), putCachedObject().
     */
    private final MemoryCache memoryCache;
    /**
     * The current in-memory size in bytes (the size of all cached objects that are in the memory cache). 
     * LegalValue:
     * It must be non-negative.
     * Initialization and Mutability:
     * It can be changed after it is initialized as part of variable declaration to: 0. 
     * Usage:
     * It is used in clear(), removeCachedObject(), putIntoMemoryCache(), putCachedObject().
     */
    private long currentInMemorySize = 0;
    /**
     * The cache statistics. It is accessed through getter and doesn't have a setter.
     * LegalValue:
     * It's a constant. So it can only be that constant value
     * Initialization and Mutability:
     * It is final and won't change once it is initialized as part of variable declaration to: new CacheStatistics(). 
     * Usage:
     * It is used in clear(), removeCachedObject(), CacheManagerImpl() (for initialization), getCacheStatistics(), getCachedObject(), printStatistics(), putIntoMemoryCache(), putCachedObject().
     */
    private final CacheStatistics cacheStatistics = new CacheStatistics();

    /**
     * Create an instance of this class with the default configuration file.
     * 
     * 
     * Parameters:
     * None
     * 
     * Exception:
     * ObjectCacheConfigurationException if any error occurs during configuring the instance
     * 
     * Implementation Notes:
     *   this(DEFAULT_CONFIG_PATH);
     */
    public CacheManagerImpl() {
        this(DEFAULT_CONFIG_PATH);
    }

    /**
     * Create an instance of this class with a configuration file.
     * 
     * 
     * Parameters:
     * configFilePath - the file path of a configuration file. 
     * 
     * Exception:
     * ObjectCacheConfigurationException if any error occurs during configuring the instance
     * IllegalArgumentException if
     *  configFilePath doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     * 
     * Implementation Notes:
     *   this(loadProperties(configFilePath));
     * @param configFilePath 
     */
    public CacheManagerImpl(String configFilePath) {
        this(loadProperties(configFilePath));
    }

    /**
     * Create an instance of this class with a Properties object and a given cache set name. The given cache set name will override the cache set name in the properties object.
     * 
     * 
     * Parameters:
     * prop - the configuration parameters
     * cacheSetName - the name of the cache set this cache manager uses. 
     * 
     * Exception:
     * ObjectCacheConfigurationException if any error occurs during configuring the instance
     * IllegalArgumentException if
     *  prop is null;
     *  cacheSetName doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     * 
     * Implementation Notes:
     *   Modify prop (this step should be put into a static helper method): prop.setProperty("cacheSetName", cacheSetName);
     *   this(prop);
     * @param prop 
     * @param cacheSetName 
     */
    public CacheManagerImpl(Properties prop, String cacheSetName) {
        this(overrideCacheSetName(prop, cacheSetName));
    }

    /**
     * Create an instance of this class with a Properties object.
     * 
     * 
     * Parameters:
     * prop - the configuration parameters
     * 
     * Exception:
     * ObjectCacheConfigurationException if any error occurs during configuring the instance
     * IllegalArgumentException if
     *  prop is null;
     * 
     * @param prop 
     */
    public CacheManagerImpl(Properties prop) {
        Helper.checkNull(prop, "prop should not be null");

        // get the class name for cachedObjectDAO
        String cachedObjectDAOClass = Helper.getProperty(prop, "cachedObjectDAOClass", DEFAULT_DAO_CLASS);

        // get the class name for memoryCache
        String memoryCacheClass = Helper.getProperty(prop, "memoryCacheClass", DEFAULT_CACHE_CLASS);

        // get the class name for cacheStatisticsPrinter
        String cacheStatisticsPrinterClass = Helper.getProperty(prop, "cacheStatisticsPrinterClass", DEFAULT_PRINTER_CLASS);

        Class<?>[] constructorTypes = {Properties.class};
        // create the dao instance
        cachedObjectDAO = (CachedObjectDAO) createReflectively(cachedObjectDAOClass, new Class[]{prop.getClass()}, new Object[]{prop});

        // create the printer instance
        cacheStatisticsPrinter = (CacheStatisticsPrinter) createReflectively(cacheStatisticsPrinterClass, new Class[]{}, new Object[]{});

        // create the memory cache instance
        memoryCache = (MemoryCache) createReflectively(memoryCacheClass, new Class[]{}, new Object[]{});

        // get the max memory size
        maxMemorySize = Helper.getIntProperty(prop, "maxMemorySize", 0) * KILOBYTES;

        // get the cache set name
        cacheSetName = Helper.getProperty(prop, "cacheSetName", DEFAULT_CACHE_SET_NAME);

        // initialize the accessCountsById map for the cacheStatistics
        cacheStatistics.setAccessCountsById(new HashMap<String, Integer>());
    }

    /**
     * This is a helper method that creates an object reflectively by calling its constructor with signature given by constructorTypes and params given by params
     * 
     * 
     * Parameters:
     * className - the full class name
     * constructorTypes - the constructor signature
     * params - the parameters to be passed into the constructor
     * 
     * Return:
     * the created object
     * Exception:
     * ObjectCacheConfigurationException if any error occurs during creating the object
     * 
     * Implementation Notes:
     *   Get the Class object:
     *   Class clazz = Class.forName(className);
     *   Instantiate the class reflectively:
     *   return clazz.getDeclaredConstructor(constructorTypes).newInstance(params);
     * @param constructorTypes 
     * @param className 
     * @param params 
     * @return 
     */
    private static Object createReflectively(String className, Class[] constructorTypes, Object[] params) {
        try {
            Class<?> clazz = Class.forName(className);
            return clazz.getDeclaredConstructor(constructorTypes).newInstance(params);
        } catch (ClassNotFoundException e) {
            throw new ObjectCacheConfigurationException(Helper.concat("The '", className, "' is not found"), e);
        } catch (IllegalArgumentException e) {
            throw new ObjectCacheConfigurationException("Illegal argument passed when creating instance", e);
        } catch (SecurityException e) {
            throw new ObjectCacheConfigurationException("Security error occurs when instantiating the class", e);
        } catch (InstantiationException e) {
            throw new ObjectCacheConfigurationException(
                    Helper.concat("The '", className, "' is an abstract class"), e);
        } catch (IllegalAccessException e) {
            throw new ObjectCacheConfigurationException("The constructor is inaccessible", e);
        } catch (InvocationTargetException e) {
            throw new ObjectCacheConfigurationException("An exception was thrown by the invoked constructor", e);
        } catch (NoSuchMethodException e) {
            throw new ObjectCacheConfigurationException(Helper.concat("No matching constructor is found in '",
                    className, "'"), e);
        }
    }

    /**
     * Changed in version 1.1:
     * the expiration timestamp should also be used to check whether the current time is after the expiration timestamp or not, if so the object is not returned and it is purged from the cache. Implementation should be modified as below:
     * 
     * A step should be inserted between step4 and step5:
     * get the current timestamp, name it currentTimestamp;
     * if(cachedObject.getExpirationTimestamp() != null && currentTimestamp.after(cachedObject.getExpirationTimestamp()))
     * {
     * removeCachedObject(id);
     * cachedObject=null;
     * }
     * ------------------------------------------------------------------------------------------------------------------
     * Get a cached object from either memory or database by its id
     * 
     * 
     * Parameters:
     * id - the id of the cached object to get
     * 
     * Return:
     * the cached object of the given id
     * Exception:
     * None
     *
     * @param id 
     * @return 
     */
    private synchronized CachedObject getCachedObject(String id) throws PersistenceException {
        if (cacheStatistics.getAccessCountsById().containsKey(id)) {
            cacheStatistics.getAccessCountsById().put(id, cacheStatistics.getAccessCountsById().get(id) + 1);
        } else {
            cacheStatistics.setMissCount(cacheStatistics.getMissCount() + 1);
            return null;
        }
        CachedObject cachedObject = memoryCache.get(id);
        if (cachedObject == null) {
            cachedObject = cachedObjectDAO.get(cacheSetName, id);
            cachedObjectDAO.delete(cacheSetName, id);
            cacheStatistics.setPersistedItemCount(cacheStatistics.getPersistedItemCount() - 1);
            putCachedObject(id, cachedObject, true);
        }
        Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
        if (cachedObject.getExpirationTimestamp() != null && currentTimestamp.after(cachedObject.getExpirationTimestamp())) {
            removeCachedObject(id);
            cachedObject = null;
        }
        return cachedObject;
    }

    /**
     * Put an object into memory cache
     * 
     * 
     * Parameters:
     * id - the id of the object to put
     * cachedObject - the object to put into memory cache
     * 
     * Return:
     * None
     * 
     * Exception:
     * None
     * 
     * @param id 
     * @param cachedObject 
     */
    private synchronized void putIntoMemoryCache(String id, CachedObject cachedObject) {
        // Delegate to memoryCache to store it
        memoryCache.put(id, cachedObject);

        // Update in-memory size
        currentInMemorySize += cachedObject.getContent().length;

        // Update in-memory item count
        cacheStatistics.setInMemoryItemCount(cacheStatistics.getInMemoryItemCount() + 1);
    }

    /**
     * Remove a cached object by id from either memory or database
     * 
     * 
     * Parameters:
     * id - the id of the cached object to remove
     * 
     * Return:
     * None
     * 
     * Exception:
     * PersistenceException if any error occurs
     * 
     * @param id 
     */
    private synchronized void removeCachedObject(String id) throws PersistenceException {
        Map<String, Integer> accessCountsById = cacheStatistics.getAccessCountsById();
        if (!accessCountsById.containsKey(id)) {
            return;
        }

        // Remove it from memory cache
        CachedObject objectToRemove = memoryCache.remove(id);

        if (objectToRemove == null) {
            // the object is not in memory
            // remove it from database
            cachedObjectDAO.delete(cacheSetName, id);

            // update the persisted item count
            cacheStatistics.setPersistedItemCount(cacheStatistics.getPersistedItemCount() - 1);
        } else {
            // Update in-memory size
            currentInMemorySize -= objectToRemove.getContent().length;

            // update the in-memory item count
            cacheStatistics.setInMemoryItemCount(cacheStatistics.getInMemoryItemCount() - 1);
        }

        // remove this id
        accessCountsById.remove(id);
    }

    /**
     * Put a CachedObject into cache. When isPersistedCachedObject is false, it will first try to put it into memory cache if there is still free memory, otherwise it goes to database. When isPersistedCachedObject is true, it tries to put it into memory cache as long as it can remove other elements from the memory cache; however, if it can't fit into the memory cache at all, it goes to database.
     * 
     * 
     * Parameters:
     * id - the id of the object to put
     * cachedObject - the cached object
     * isPersistedCachedObject - the flag indicating whether the object should always go to in-memory cache (if possible). This flag also indicates if the cached object to put is an existing one from persistence storage or is a new cached object.
     * 
     * Return:
     * None
     * 
     * Exception:
     * PersistenceException if any error occurs
     * 
     * @param id 
     * @param isPersistedCachedObject 
     * @param cachedObject 
     */
    private synchronized void putCachedObject(String id, CachedObject cachedObject, boolean isPersistedCachedObject) throws PersistenceException {
        if (!isPersistedCachedObject) {
            // remove whatever element with the same id in the cache (even if it doesn't exist)
            removeCachedObject(id);
        }
        if (cachedObject != null) {
            if (!isPersistedCachedObject) {
                // Set the access count to 0 for this new object
                cacheStatistics.getAccessCountsById().put(id, 0);
            }

            int contentLength = cachedObject.getContent().length;
            if (currentInMemorySize + contentLength <= maxMemorySize) {
                // put it into memory cache directly
                putIntoMemoryCache(id, cachedObject);
            } else {
                if (isPersistedCachedObject && contentLength <= maxMemorySize) {
                    // try to put it into memory by replacing others from the memory cache
                    while (currentInMemorySize + contentLength > maxMemorySize) {
                        // Get the object to replace from memoryCache:
                        CachedObject replaceable = memoryCache.getReplaceableCachedObject();

                        // Save it to database
                        cachedObjectDAO.save(replaceable);

                        // Set the persisted item count
                        cacheStatistics.setPersistedItemCount(cacheStatistics.getPersistedItemCount() + 1);

                        // Remove it from memory
                        memoryCache.remove(replaceable.getId());

                        // Update the in-memory size
                        currentInMemorySize -= replaceable.getContent().length;

                        // Update the in-memory item count
                        cacheStatistics.setInMemoryItemCount(cacheStatistics.getInMemoryItemCount() - 1);
                    }

                    // Put the cached object into memory
                    putIntoMemoryCache(id, cachedObject);
                } else {
                    // Put the cached object into into database because the memory cache is too small to hold it:
                    cachedObjectDAO.save(cachedObject);

                    // Update the persisted item count:
                    cacheStatistics.setPersistedItemCount(cacheStatistics.getPersistedItemCount() + 1);
                }
            }
        }
    }

    /**
     * Print the cache statistics to the given print writer. This method will not close the print writer.
     * 
     * Parameters:
     * printWriter - the print writer to use
     * 
     * Return:
     * None
     * 
     * Exception:
     * StatisticsPrintingException if any error occurs
     * IllegalArgumentException if
     *  printWriter is null;
     * 
     * Implementation Notes:
     *         cacheStatisticsPrinter.printStatistics(this.cacheStatistics, printWriter);
     * @param printWriter 
     */
    @Override
    public void printStatistics(PrintWriter printWriter) throws StatisticsPrintingException {
        if (printWriter == null) {
            throw new IllegalArgumentException("printWriter is null");
        }
        String method = "CacheManagerImpl#printStatistics(PrintWriter)";
        try {
            // print the statistics
            cacheStatisticsPrinter.printStatistics(cacheStatistics, printWriter);
        } catch (IllegalArgumentException e) {
            // printWriter is null
            throw new StatisticsPrintingException("", e);
        }
    }

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
     * 
     * Implementation Notes:
     * 1 Get the serialized cached object with id:
     *   CachedObject cachedObject = getCachedObject(id);
     * 2 If cachedObject == null then
     *         return null;
     * 3 Create an ObjectInputStream for reading the serialized object:
     *   ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(cachedObject.getContent()));
     * 4 Read the object:
     *   Object object = ois.readObject();
     * 5 Close the stream:
     *   ois.close();
     * 6 return (Serializable) object;
     * @param id 
     * @return 
     */
    public Serializable get(String id) {
        long startTime = System.currentTimeMillis();
        String method = "CacheManagerImpl#get(String)";
        logEntrance(LOGGER, method, new String[]{"id"}, id);
        if (id == null || id.trim().length() == 0) {
            return logExit(LOGGER, method, startTime, false, null);
        }

        // Get the serialized cached object with id
        CachedObject cachedObject;
        try {
            // get cached object from either memory or database
            cachedObject = getCachedObject(id);

            // if not in the cache then return null
            if (cachedObject == null) {
                return logExit(LOGGER, method, startTime, false, null);
            }
        } catch (PersistenceException e) {
            return logExit(LOGGER, method, startTime, false, null);
        }

        // Create a stream for reading the serialized object
        Serializable object;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(cachedObject.getContent()));

            // Read the object
            object = (Serializable) ois.readObject();
        } catch (IOException e) {
            logError(LOGGER, method, e);
            return logExit(LOGGER, method, startTime, false, null);
        } catch (ClassNotFoundException e) {
            logError(LOGGER, method, e);
            return logExit(LOGGER, method, startTime, false, null);
        } finally {
            closeIO(method, ois);
        }

        return logExit(LOGGER, method, startTime, false, object);
    }

    /**
     * Put an object into cache.
     * 
     * 
     * Parameters:
     * id - the id of the object to put. 
     * object - the object to put into cache. If it's null, it means the item 
     * with given id in the cache should be removed from the cache. If an item 
     * already exists in the cache with the same id, that item is replaced. 
     * Note that in this case, the statistics of this id is reset.
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
     * 
     * Implementation Notes:
     * 1 If object == null then
     *         cachedObject = null;
     * 2 Else
     *     2.1 ByteArrayOutputStream bos = new ByteArrayOutputStream();
     *     2.2 Create an ObjectOutputStream for serializing the object:
     *         ObjectOutputStream oos = new ObjectOutputStream(bos);
     *     2.3 Serialize the object:
     *         oos.writeObject(object);
     *     2.4 Close the stream:
     *         oos.close();
     *     2.5 Create a CachedObject for the object:
     *         cachedObject = new CachedObject();
     *     2.6 Set the id:
     *         cachedObject.setId(id);
     *     2.7 Set the cache set name:
     *         cachedObject.setCacheSetName(cacheSetName);
     *     2.8 Set the content:     cachedObject.setContent(bos.toByteArray());
     * 
     * 3 Put the cached object into cache:
     *   putCachedObject(id, cachedObject, false);
     * @param id 
     * @param object 
     */
    @Override
    public void put(String id, Serializable object) throws ObjectCacheException {
        String method = "CacheManagerImpl#put(String, Serializable)";

        CachedObject cachedObject;
        if (object == null) {
            cachedObject = null;
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(bos);
                oos.writeObject(object);
            } catch (IOException e) {
                throw logError(LOGGER, method, new ObjectCacheException("Fails to serialize object", e));
            } finally {
                closeIO(method, oos);
            }
            cachedObject = new CachedObject();
            cachedObject.setId(id);
            cachedObject.setCacheSetName(cacheSetName);
            cachedObject.setContent(bos.toByteArray());
        }
    }

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
     * 
     * @param id 
     * @param object 
     * @param milliseconds 
     */
    public void put(String id, Serializable object, long milliseconds) throws ObjectCacheException, PersistenceException {
        if ((id == null) || (id.isEmpty())) {
            throw new IllegalArgumentException("((id == null) || (id.isEmpty() ))");
        }
        if (milliseconds <= 0) {
            throw new IllegalArgumentException("milliseconds <=0");
        }
        Timestamp expirationTimestamp = new Timestamp(System.currentTimeMillis() + milliseconds);
        put(id, object, expirationTimestamp);
    }

    /**
     * Put an object into cache with expiration timestamp which indicates the object will be purged after the expiration timestamp. This method is added in version1.1.
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
     * 
     * Implementation Notes:
     * 1 If object == null then
     *         cachedObject = null;
     * 2 Else
     *     2.1 ByteArrayOutputStream bos = new ByteArrayOutputStream();
     *     2.2 Create an ObjectOutputStream for serializing the object:
     *         ObjectOutputStream oos = new ObjectOutputStream(bos);
     *     2.3 Serialize the object:
     *         oos.writeObject(object);
     *     2.4 Close the stream:
     *         oos.close();
     *     2.5 Create a CachedObject for the object:
     *         cachedObject = new CachedObject();
     *     2.6 Set the id:
     *         cachedObject.setId(id);
     *     2.7 Set the cache set name:
     *         cachedObject.setCacheSetName(cacheSetName);
     *     2.8 Set the content:     cachedObject.setContent(bos.toByteArray());
     *     2.9 Set the expiration timestamp:     cachedObject.setExpirationTimestamp(expirationTimestamp);
     * 
     * 3 Put the cached object into cache:
     *   putCachedObject(id, cachedObject, false);
     * @param expirationTimestamp 
     * @param id 
     * @param object 
     */
    public void put(String id, Serializable object, Timestamp expirationTimestamp) throws ObjectCacheException, PersistenceException {
        CachedObject cachedObject;
        String method = "CacheManagerImpl#put(String, Serializable, Timestamp)";
        if (object == null) {
            cachedObject = null;
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(bos);
                oos.writeObject(object);
            } catch (IOException e) {
                throw logError(LOGGER, method, new ObjectCacheException("Fails to serialize object", e));
            } finally {
                closeIO(method, oos);
            }

            cachedObject = new CachedObject();
            cachedObject.setId(id);
            cachedObject.setCacheSetName(cacheSetName);
            cachedObject.setContent(bos.toByteArray());
            cachedObject.setExpirationTimestamp(expirationTimestamp);
        }
        putCachedObject(id, cachedObject, false);
    }

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
     * 
     * Implementation Notes:
     * 1 Delete the cache set's database rows:
     *   cachedObjectDAO.deleteByCacheSet(cacheSetName);
     * 2 currentInMemorySize = 0;
     * 3 Clear the memory cache:
     *   memoryCache.clear();
     * 4 Set the access counts by id:
     *   cacheStatistics.setAccessCountsById(new HashMap<String, Integer>());
     * 5 Call cacheStatistics.setPersistedItemCount(), cacheStatistics.setInMemoryItemCount(), cacheStatistics.setMissCount() to set all to 0
     */
    public synchronized void clear() {
        cachedObjectDAO.deleteByCacheSet(cacheSetName);
        currentInMemorySize = 0;
        memoryCache.clear();
        cacheStatistics.setAccessCountsById(new HashMap<String, Integer>());
        cacheStatistics.setPersistedItemCount(0);
        cacheStatistics.setInMemoryItemCount(0);
        cacheStatistics.setMissCount(0);
    }

    /**
     * Load the configuration file into a Properties object
     * 
     * 
     * Parameters:
     * configFilePath - the file path of the configuration file
     * 
     * Return:
     * the loaded Properties object
     * Exception:
     * ObjectCacheConfigurationException if any error occurs during loading the properties
     * 
     * Implementation Notes:
     * 1 Create an empty Properties:
     *   Properties prop = new Properties();
     * 2 Create a FileInputStream:
     *   in = new FileInputStream(configFilePath);
     * 3 Load the properties:
     *   prop.load(in);
     * 4 Close the input stream:
     *   in.close();
     * 5 return prop;
     * @param configFilePath 
     * @return 
     */
    private static Properties loadProperties(String configFilePath) {
        Properties prop = new Properties();
        try {
            FileInputStream in = new FileInputStream(configFilePath);
            prop.load(in);
            in.close();
        } catch (IOException e) {
            throw new ObjectCacheConfigurationException("Error in CacheManagerImpl#loadProperties", e);
        }
        return prop;
    }

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
     * 
     * Implementation Notes:
     * 1 CacheStatistics copied = new CacheStatistics();
     * 2 Set the value of this.cacheStatistics to copied (for the map in this.cacheStatistics, make a deep copy)
     * 3 return copied;
     * @return 
     */
    public CacheStatistics getCacheStatistics() {
        CacheStatistics copy = new CacheStatistics();
        // copy all the counts
        copy.setInMemoryItemCount(cacheStatistics.getInMemoryItemCount());
        copy.setPersistedItemCount(cacheStatistics.getPersistedItemCount());
        copy.setMissCount(cacheStatistics.getMissCount());        
        // get the access counts
        Map<String, Integer> accessCountsById = cacheStatistics.getAccessCountsById();

        // create a new map for deep copying
        Map<String, Integer> copyMap = new HashMap<String, Integer>();
        for (Map.Entry<String, Integer> entries : accessCountsById.entrySet()) {
            // deep copy each key/value pair
            copyMap.put(new String(entries.getKey()), new Integer(entries.getValue()));
        }
        copy.setAccessCountsById(copyMap);
        return copy;
    }

    /**
     * <p>
     * Overrides cacheSetName property value in given properties object.
     * </p>
     *
     * @param prop the properties object to override its cacheSetName property value.
     * @param cacheSetName the cacheSetName property value to use.
     * @return the properties object.
     * @throws IllegalArgumentException if any argument is <code>null</code>, or if <code>cacheSetName</code> is
     *         empty.
     */
    private static Properties overrideCacheSetName(Properties prop, String cacheSetName) {
        Helper.checkNull(prop, "prop should not be null");
        Helper.checkNullOrEmpty(cacheSetName, "cacheSetName should not be null or empty");
        prop.setProperty("cacheSetName", cacheSetName);
        return prop;
    }

    /**
     * <p>
     * This is a helper method that creates an object reflectively by calling its constructor with signature given
     * by constructorTypes and params given by params.
     * </p>
     *
     * @param <T> the generic type of the expected instance.
     * @param clazz the type of the expected instance.
     * @param className the full class name
     * @param constructorTypes the constructor signature
     * @param params the parameters to be passed into the constructor
     * @return the created object.
     * @throws ObjectCacheConfigurationException if any error occurs during creating the object.
     */
    @SuppressWarnings("unchecked")
    private static <T> T createReflectively(Class<T> clazz, String className, Class<?>[] constructorTypes,
            Object... params) {
        try {
            Class<?> cls = Class.forName(className);

            if (!clazz.isAssignableFrom(cls)) {
                throw new ObjectCacheConfigurationException(Helper.concat("Instance of the '", className,
                        "' must can be assigned to '", clazz.getName() + "'"));
            }

            return (T) cls.getDeclaredConstructor(constructorTypes).newInstance(params);
        } catch (ClassNotFoundException e) {
            throw new ObjectCacheConfigurationException(Helper.concat("The '", className, "' is not found"), e);
        } catch (IllegalArgumentException e) {
            throw new ObjectCacheConfigurationException("Illegal argument passed when creating instance", e);
        } catch (SecurityException e) {
            throw new ObjectCacheConfigurationException("Security error occurs when instantiating the class", e);
        } catch (InstantiationException e) {
            throw new ObjectCacheConfigurationException(
                    Helper.concat("The '", className, "' is an abstract class"), e);
        } catch (IllegalAccessException e) {
            throw new ObjectCacheConfigurationException("The constructor is inaccessible", e);
        } catch (ExceptionInInitializerError e) {
            throw new ObjectCacheConfigurationException("Fails to initialize the class", e);
        } catch (InvocationTargetException e) {
            throw new ObjectCacheConfigurationException("An exception was thrown by the invoked constructor", e);
        } catch (NoSuchMethodException e) {
            throw new ObjectCacheConfigurationException(Helper.concat("No matching constructor is found in '",
                    className, "'"), e);
        }
    }

    /**
     * <p>
     * Logs the method entrance and the method parameters (if any) at DEBUG level.
     * </p>
     *
     * @param log the logger.
     * @param method the name of the method.
     * @param paramNames the parameter names.
     * @param paramValues the parameter values.
     */
    private static void logEntrance(Logger log, String method, String[] paramNames, Object... paramValues) {
        log.debug(Helper.concat("[Entering method ", method, "]"));
        if (paramNames != null) {
            StringBuilder sb = new StringBuilder("[Input parameters[");
            for (int i = 0; i < paramNames.length; i++) {
                sb.append(paramNames[i]).append(":").append(paramValues[i]).append(", ");
            }
            // remove last comma
            int length = sb.length();
            sb.delete(length - 2, length);
            sb.append("]]");
            log.debug(sb.toString());
        }
    }

    /**
     * <p>
     * Logs given error at ERROR level before it is returned back.
     * </p>
     *
     * @param <T> the parameterized type of the exception
     * @param log the logger
     * @param method the name of the method which the error has occurred
     * @param e the exception to log
     * @return the exception
     */
    private static <T extends Throwable> T logError(Logger log, String method, T e) {
        log.error(Helper.concat("[Error in method ", method, ": Details ", e), e);
        return e;
    }

    /**
     * <p>
     * Logs the method exit, the method execution time and the return value (if not void) at DEBUG level.
     * </p>
     *
     * @param <T> the parameterized type of the return value.
     * @param log the logger.
     * @param method the name of the method.
     * @param startTime the method execution start time.
     * @param isVoid whether the method is void.
     * @param value the return value to log.
     * @return the return value.
     */
    private static <T> T logExit(Logger log, String method, long startTime, boolean isVoid, T value) {
        log.debug(Helper.concat("[Exiting method ", method, "][Call duration ",
                (System.currentTimeMillis() - startTime), " ms]"));
        if (!isVoid) {
            log.debug(Helper.concat("[Output parameter ", value, "]"));
        }
        return value;
    }

    /**
     * <p>
     * Closes the closeable resource.
     * </p>
     *
     * @param method the name of the method that invokes this method.
     * @param closeable the resource to close.
     */
    private static void closeIO(String method, Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // ignore
                if (method != null) {
                    logError(LOGGER, method, e);
                }
            }
        }
    }
}
