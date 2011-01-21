package com.ibm.ps.codereview;

import java.lang.*;

/**
 * This exception indicates error with CVS log (reading, parsing, etc).
 * This class inherits from not thread-safe class, so it's not thread-safe.
 */
public class CvsLogException extends CodeReviewToolException {
    /**
     * Creates instance with specified error message. Simply delegate to base class constructor with same signature.
     * 
     * ---Paramaters---
     * message - Error message.
     * @param message 
     */
    public CvsLogException(String message) {
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
    public CvsLogException(String message, Throwable cause) {
    }
}

