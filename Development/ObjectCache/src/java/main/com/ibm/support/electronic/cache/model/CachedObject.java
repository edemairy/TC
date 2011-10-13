package com.ibm.support.electronic.cache.model;

import java.lang.*;
import java.sql.Timestamp;

/**
 * This is a simple POJO that stores a serialized object in a cache set. The id and cacheSetName together identify an instance of this class. This component stores all cached objects in serialized form, and each cached object is serialized and wrapped in one instance of this class.
 * 
 * Thread Safety:
 * This class is not thread-safe because it's mutable
 * 
 * Changed in version1.1:
 * expiration timestamp is added to associate with a cached object.
 */
public class CachedObject {
    /**
     * The id of the cached object. It has both getter and setter.
     * LegalValue:
     * It can be any value.
     * Initialization and Mutability:
     * It does not need to be initialized when the instance is created. 
     * Usage:
     * It is used in getId(), setId().
     */
    private String id;

    /**
     * The name of the cache set this object belongs. It has both getter and setter.
     * LegalValue:
     * It can be any value.
     * Initialization and Mutability:
     * It does not need to be initialized when the instance is created. 
     * Usage:
     * It is used in setCacheSetName(), getCacheSetName().
     */
    private String cacheSetName;

    /**
     * The serialized content of the object. It has both getter and setter.
     * LegalValue:
     * It can be any value. 
     * Initialization and Mutability:
     * It does not need to be initialized when the instance is created. 
     * Usage:
     * It is used in getContent(), setContent().
     */
    private byte[] content;

    /**
     * The expiration timestamp for the cached object. It has both getter and setter. !!It's type is java.sql.Timestamp instead of java.util.Timestamp.!!
     * LegalValue:
     * It can be any value. 
     * Initialization and Mutability:
     * It does not need to be initialized when the instance is created. 
     * Usage:
     * It is used in getter and setter.
     * 
     * It's added in version1.1.
     */
    private Timestamp expirationTimestamp;

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
    public CachedObject() {
    }

    /**
     * Getter for the namesake instance variable. 
     * Simply return the namesake instance variable.
     * @return 
     */
    public String getCacheSetName() {
        return cacheSetName;
    }

    /**
     * Getter for the namesake instance variable. 
     * Simply return the namesake instance variable.
     * @return 
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for the namesake instance variable. 
     * Simply return the namesake instance variable.
     * @return 
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Getter for the namesake instance variable. 
     * Simply return the namesake instance variable.
     * 
     * It's added in version1.1.
     * @return 
     */
    public Timestamp getExpirationTimestamp() {
        return expirationTimestamp;
    }

    /**
     * Setter for the namesake instance variable. 
     * Simply assign the value to the instance variable.
     * 
     * Parameters:
     * id - The id of the cached object. 
     * @param id 
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Setter for the namesake instance variable. 
     * Simply assign the value to the instance variable.
     * 
     * Parameters:
     * cacheSetName - The name of the cache set this object belongs. 
     * @param cacheSetName 
     */
    public void setCacheSetName(String cacheSetName) {
        this.cacheSetName = cacheSetName;
    }

    /**
     * Setter for the namesake instance variable. 
     * Simply assign the value to the instance variable.
     * 
     * Parameters:
     * content - The serialized content of the object. 
     * @param content 
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * Setter for the namesake instance variable. 
     * Simply assign the value to the instance variable.
     * 
     * Parameters:
     * expirationTimestamp - The expiration timestamp for the cached object.
     * 
     * It's added in version1.1.
     * @param expirationTimestamp 
     */
    public void setExpirationTimestamp(Timestamp expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }
}

