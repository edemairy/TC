/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the BloomFilterSerializeException class.
 * </p>
 *
 * @author cucu
 * @version 1.0
 */
public class BloomFilterSerializeExceptionTests extends TestCase {

    /**
     * Cause used for instantiating other exceptions.
     */
    private static IllegalArgumentException cause = new IllegalArgumentException("Problem");


    /**
     * Check that BloomFilterException extends BloomFilterException.
     */
    public void testBloomFilterSerializeExceptionExtends() {
        assertTrue("bad definition for BloomFilterSerializeException",
                new BloomFilterSerializeException("x") instanceof BloomFilterException);
    }

    /**
     * Test for BloomFilterException, constructor BloomFilterException(String message).
     * It tests that the message is correctly set.
     */
    public void testBloomFilterSerializeExceptionConstructor1() {
        BloomFilterSerializeException bfe = new BloomFilterSerializeException("error");
        assertEquals("bad message in BloomFilterSerializeException",
                "error", bfe.getMessage());
    }

    /**
     * Test for BloomFilterSerializeException.
     * constructor BloomFilterSerializeException(String message, String cause).
     * It tests that the message and cause are correctly set.
     */
    public void testBloomFilterSerializeExceptionConstructor2() {
        BloomFilterSerializeException bfe = new BloomFilterSerializeException("error", cause);
        assertEquals("bad message in BloomFilterSerializeException",
                "error", bfe.getMessage());
        assertEquals("bad cause in BloomFilterSerializeException",
                cause, bfe.getCause());

    }

}
