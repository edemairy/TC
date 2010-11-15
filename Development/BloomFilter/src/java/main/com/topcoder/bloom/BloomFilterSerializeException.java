/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom;

/**
 * <p>
 * This exception is thrown by BloomFilter and DefaultHashFunctionFamily classes when they can't
 * build the object from the serialization string because it doesn't have the right format or
 * appropriate values.
 * This exception derives from BloomFilterException.
 * </p>
 *
 * @author real_vg, cucu
 * @version 1.0
 */
public class BloomFilterSerializeException extends BloomFilterException {

    /**
     * <p>
     * Constructs a new exception with the specified detail message.
     * </p>
     *
     * @param message the detail message
     */
    public BloomFilterSerializeException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new exception with the specified detail message and cause.
     * </p>
     *
     * @param message the detail message
     * @param cause the cause
     */
    public BloomFilterSerializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
