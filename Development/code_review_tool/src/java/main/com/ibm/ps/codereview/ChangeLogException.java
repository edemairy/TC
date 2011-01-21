package com.ibm.ps.codereview;

import java.lang.*;

/**
 * This exception indicates error with change log (writing).
 * This class inherits from not thread-safe class, so it's not thread-safe.
 */
public class ChangeLogException extends CodeReviewToolException {
    /**
     * Creates instance with specified error message. Simply delegate to base class constructor with same signature.
     * 
     * ---parameters---
     * message - Error message.
     * @param message 
     */
    public ChangeLogException(String message) {
        super(message);
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
    public ChangeLogException(String message, Throwable cause) {
        super(message,cause);
    }
}

