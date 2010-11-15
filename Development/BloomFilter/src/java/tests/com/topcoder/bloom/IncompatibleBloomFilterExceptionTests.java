/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the IncompatibleBloomFilterException class.
 * </p>
 *
 * @author cucu
 * @version 1.0
 */
public class IncompatibleBloomFilterExceptionTests extends TestCase {

    /**
     * Cause used for instantiating other exceptions.
     */
    private static IllegalArgumentException cause = new IllegalArgumentException("Problem");

    /**
     * Check that IncompatibleBloomFiltersException extends BloomFilterException.
     */
    public void testIncompatibleBloomFiltersExceptionExtends() {
        assertTrue("bad definition for IncompatibleBloomFiltersException",
                new IncompatibleBloomFiltersException("x") instanceof BloomFilterException);
    }

    /**
     * Test for IncompatibleBloomFiltersException, constructor IncompatibleBloomFiltersException(String message).
     * It tests that the message is correctly set.
     */
    public void testIncompatibleBloomFiltersExceptionConstructor1() {
        IncompatibleBloomFiltersException bfe = new IncompatibleBloomFiltersException("error");
        assertEquals("bad message in IncompatibleBloomFiltersException",
                "error", bfe.getMessage());
    }

    /**
     * Test for IncompatibleBloomFiltersException.
     * constructor IncompatibleBloomFiltersException(String message, String cause).
     * It tests that the message and cause are correctly set.
     */
    public void testIncompatibleBloomFiltersExceptionConstructor2() {
        IncompatibleBloomFiltersException bfe = new IncompatibleBloomFiltersException("error", cause);
        assertEquals("bad message in IncompatibleBloomFiltersException",
                "error", bfe.getMessage());
        assertEquals("bad cause in IncompatibleBloomFiltersException",
                cause, bfe.getCause());

    }
}
