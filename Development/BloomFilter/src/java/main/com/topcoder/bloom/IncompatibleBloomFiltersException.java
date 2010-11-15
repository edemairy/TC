/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom;

/**
 * <p>
 * This exception is thrown by the BloomFilter class when
 * union or intersection operation is performed on
 * two Bloom filters that are incompatible.
 * Two Bloom filters are meant to be incompatible if they do not have
 * equal bit set lengths and hash function families.
 * This exception derives from BloomFilterException
 * </p>
 *
 * @author real_vg, cucu
 * @version 1.0
 */
public class IncompatibleBloomFiltersException extends BloomFilterException {

    /**
     * <p>
     * Constructs a new exception with the specified detail message.
     * </p>
     *
     * @param message the detail message
     */
    public IncompatibleBloomFiltersException(String message) {
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
    public IncompatibleBloomFiltersException(String message, Throwable cause) {
        super(message, cause);
    }
}
