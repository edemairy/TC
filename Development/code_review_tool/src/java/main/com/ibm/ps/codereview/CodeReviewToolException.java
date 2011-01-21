package com.ibm.ps.codereview;

import java.lang.*;

/**
 * This is a base exception of the component. It indicates general issue with code review tool operationing.
 * This class inherits from not thread-safe class, so it's not thread-safe.
 */
public class CodeReviewToolException extends Exception {
    /**
     * Creates instance with specified error message. Simply delegate to base class constructor with same signature.
     * 
     * ---Paramaters---
     * message - Error message.
     * @param message 
     */
    public CodeReviewToolException(String message) {
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
    public CodeReviewToolException(String message, Throwable cause) {
    }
}

