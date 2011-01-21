package com.ibm.ps.codereview;

import java.lang.*;

/**
 * This runtime exception indicates fatal issue with component configuration.
 * This class inherits from not thread-safe class, so it's not thread-safe.
 */
public class CodeReviewToolConfigurationException extends RuntimeException {
    /**
     * Creates instance with specified error message. Simply delegate to base class constructor with same signature.
     * 
     * ---Paramaters---
     * message - Error message.
     * @param message 
     */
    public CodeReviewToolConfigurationException(String message) {
    }

    /**
     * Creates instance with specified error message and inner cause. Simply delegate to base class constructor with same signature.
     * 
     * ---Paramaters---
     * message - Error message.
     * cause - Inner cause.
     * @param message 
     * @param cause 
     */
    public CodeReviewToolConfigurationException(String message, Throwable cause) {
    }
}

