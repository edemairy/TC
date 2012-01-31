/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.accuracytests;

import junit.framework.TestCase;

import org.junit.Test;

import com.ibm.support.electronic.cache.ObjectCacheException;

/**
 * Unit test for {@link ObjectCacheException}.
 *
 * @author KLW
 * @version 1.0
 */
public class ObjectCacheExceptionAccuracyTest extends TestCase {
    /**
     * <p>
     *  the exception to test.
     * </p>
     */
    private ObjectCacheException e;

    /**
     * <p>
     * The accuracy test for method {@link ObjectCacheException#ObjectCacheException(String)}.
     * </p>
     */
    @Test
    public void testObjectCacheExceptionString() {
        e = new ObjectCacheException("test");
        assertNull("The cause should be null.", e.getCause());
        assertEquals("The message is incorrect.", "test", e.getMessage());
        assertTrue("the class type is incorrect.", Exception.class.isInstance(e));
    }

    /**
     * <p>
     * The accuracy test for method {@link ObjectCacheException#ObjectCacheException(String, Throwable)} .
     * </p>
     */
    @Test
    public void testObjectCacheExceptionStringThrowable() {
        Exception cause = new Exception();
        e = new ObjectCacheException("test", cause);
        assertNull("The cause is incorrect.", cause.getCause());
        assertEquals("The message is incorrect.", "test", e.getMessage());
    }
}

