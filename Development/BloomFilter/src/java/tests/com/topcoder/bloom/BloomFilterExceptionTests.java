/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom;

import junit.framework.TestCase;

/**
 * <p>
 * Test the BloomFilterException class.
 * </p>
 *
 * @author cucu
 * @version 1.0
 */
public class BloomFilterExceptionTests extends TestCase {

    /**
     * Cause used for instantiating other exceptions.
     */
    private static IllegalArgumentException cause = new IllegalArgumentException("Problem");

    /**
     * Check that BloomFilterException extends RuntimeException.
     */
    public void testBloomFilterExceptionExtends() {
        assertTrue("bad definition for BloomFilterException",
                new BloomFilterException("x") instanceof RuntimeException);
    }

    /**
     * Test for BloomFilterException, constructor BloomFilterException(String message).
     * It tests that the message is correctly set.
     */
    public void testBloomFilterExceptionConstructor1() {
        BloomFilterException bfe = new BloomFilterException("error");
        assertEquals("bad message in BloomFilterException",
                "error", bfe.getMessage());
    }

    /**
     * Test for BloomFilterException, constructor BloomFilterException(String message, String cause).
     * It tests that the message and cause are correctly set.
     */
    public void testBloomFilterExceptionConstructor2() {
        BloomFilterException bfe = new BloomFilterException("error", cause);
        assertEquals("bad message in BloomFilterException",
                "error", bfe.getMessage());
        assertEquals("bad cause in BloomFilterException",
                cause, bfe.getCause());

    }

}
