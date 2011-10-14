/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.accuracytests;

import junit.framework.TestCase;

import org.junit.Test;

import com.ibm.support.electronic.cache.ObjectCacheConfigurationException;

/**
 * Unit test for {@link ObjectCacheConfigurationException}.
 *
 * @author KLW
 * @version 1.0
 */
public class ObjectCacheConfigurationExceptionAccuracyTest extends TestCase {
    /**
     * <p>
     *  the exception to test.
     * </p>
     */
    private ObjectCacheConfigurationException e;

    /**
     * <p>
     * The accuracy test for method {@link ObjectCacheConfigurationException#ObjectCacheConfigurationException(String)}.
     * </p>
     */
    @Test
    public void testObjectCacheConfigurationExceptionString() {
        e = new ObjectCacheConfigurationException("test");
        assertNull("The cause should be null.", e.getCause());
        assertEquals("The message is incorrect.", "test", e.getMessage());
        assertTrue("the class type is incorrect.", RuntimeException.class.isInstance(e));
    }

    /**
     * <p>
     * The accuracy test for method
     * {@link ObjectCacheConfigurationException#ObjectCacheConfigurationException(String, Throwable)} .
     * </p>
     */
    @Test
    public void testObjectCacheConfigurationExceptionStringThrowable() {
        Exception cause = new Exception();
        e = new ObjectCacheConfigurationException("test", cause);
        assertNull("The cause is incorrect.", cause.getCause());
        assertEquals("The message is incorrect.", "test", e.getMessage());
    }
}

