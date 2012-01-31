package com.ibm.support.electronic.cache;

import java.lang.*;

/**
 * This is the thrown if any error occurs during printing cache statistics. It's thrown by CacheStatisticsPrinter(and its implementations), CacheManager(and its implementations).
 * 
 * Thread Safety:
 * This class is not thread-safe because the base class is not thread-safe.
 */
public class StatisticsPrintingException extends Exception {
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
    public StatisticsPrintingException(String message) {
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
    public StatisticsPrintingException(String message, Throwable cause) {
        super(message, cause);
    }
}

