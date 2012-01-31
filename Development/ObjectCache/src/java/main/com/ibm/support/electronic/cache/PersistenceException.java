package com.ibm.support.electronic.cache;


/**
 * This is the thrown if any error occurs with the persistence storage. It's thrown by CachedObjectDAO(and its implementations), CacheManager(and its implementations).
 * 
 * Thread Safety:
 * This class is not thread-safe because the base class is not thread-safe.
 */
public class PersistenceException extends Exception {
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
    public PersistenceException(String message) {
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
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

