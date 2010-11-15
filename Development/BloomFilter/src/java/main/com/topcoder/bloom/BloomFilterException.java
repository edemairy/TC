/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom;

/**
 * <p>
 * This exception is the base exception for all exceptions in the component.
 * </p>
 *
 * @author real_vg, cucu
 * @version 1.0
 */
public class BloomFilterException extends RuntimeException {

    /**
     * <p>
     * Constructs a new exception with the specified detail message.
     * </p>
     * @param message the detail message.
     */
    public BloomFilterException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new exception with the specified detail message and cause.
     * </p>
     *
     * @param message the details message
     * @param cause the cause
     */
    public BloomFilterException(String message, Throwable cause) {
        super(message, cause);
    }
}
