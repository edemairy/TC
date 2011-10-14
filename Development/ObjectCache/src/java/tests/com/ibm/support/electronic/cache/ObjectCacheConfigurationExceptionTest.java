/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import junit.framework.JUnit4TestAdapter;

import org.junit.Assert;
import org.junit.Test;


/**
 * <p>
 * Unit test case of {@link ObjectCacheConfigurationException}.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ObjectCacheConfigurationExceptionTest {
    /**
     * <p>
     * Represents the error message used for testing.
     * </p>
     */
    private static final String ERROR_MESSAGE = "error message";

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ObjectCacheConfigurationExceptionTest.class);
    }

    /**
     * <p>
     * Accuracy test method for
     * {@link ObjectCacheConfigurationException#ObjectCacheConfigurationException(java.lang.String)}.
     * </p>
     */
    @Test
    public void testCtor1() {
        ObjectCacheConfigurationException e = new ObjectCacheConfigurationException(ERROR_MESSAGE);
        Assert.assertNotNull("Unable to instantiate ObjectCacheConfigurationException", e);
        Assert.assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE,
            e.getMessage());
        Assert.assertNull("Error cause should be null", e.getCause());
    }

    /**
     * <p>
     * Accuracy test method for <code>ObjectCacheConfigurationException#
     * ObjectCacheConfigurationException(java.lang.String, java.lang.Throwable)</code>.
     * </p>
     */
    @Test
    public void testCtor2() {
        Throwable cause = new NullPointerException();
        ObjectCacheConfigurationException e = new ObjectCacheConfigurationException(ERROR_MESSAGE, cause);
        Assert.assertNotNull("Unable to instantiate ObjectCacheConfigurationException", e);
        Assert.assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE,
            e.getMessage());
        Assert.assertEquals("Error cause is not properly propagated to super class", cause, e.getCause());
    }
}
