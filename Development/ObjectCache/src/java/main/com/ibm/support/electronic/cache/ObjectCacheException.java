package com.ibm.support.electronic.cache;

/**
 * This is the base exception of all checked exception encountered by this component. It's thrown by CacheManager(and its implementations).
 * 
 * Thread Safety:
 * This class is not thread-safe because the base class is not thread-safe.
 */
public class ObjectCacheException extends Exception {
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
    public ObjectCacheException(String message) {
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
    public ObjectCacheException(String message, Throwable cause) {
        super(message, cause);
    }
}

