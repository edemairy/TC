package com.ibm.support.electronic.cache;

import java.lang.*;

/**
 * This is thrown if any configuration error occurs in this component. It's thrown by MemoryCacheImpl, CachedObjectDAOImpl, CacheManagerImpl.
 * 
 * Thread Safety:
 * This class is not thread-safe because the base class is not thread-safe.
 */
public class ObjectCacheConfigurationException extends RuntimeException {
    /**
     * Create a new exception with error message.
     * Parameters:
     * message - the error message
     * 
     * Exception:
     * None
     * 
     * Implementation Notes:
     *         super(message);
     * @param message 
     */
    public ObjectCacheConfigurationException(String message) {
        super(message);
    }

    /**
     * Create a new exception with error message and inner cause.
     * Parameters:
     * message - the error message
     * cause - inner cause
     * 
     * Exception:
     * None
     * 
     * Implementation Notes:
     *         super(message, cause);
     * @param message 
     * @param cause 
     */
    public ObjectCacheConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}

