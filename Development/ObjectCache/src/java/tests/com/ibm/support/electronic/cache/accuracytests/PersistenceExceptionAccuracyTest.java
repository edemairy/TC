/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.accuracytests;

import junit.framework.TestCase;

import org.junit.Test;

import com.ibm.support.electronic.cache.ObjectCacheException;
import com.ibm.support.electronic.cache.PersistenceException;

/**
 * Unit test for {@link PersistenceException}.
 *
 * @author KLW
 * @version 1.0
 */
public class PersistenceExceptionAccuracyTest extends TestCase {
    /**
     * <p>
     *  the exception to test.
     * </p>
     */
    private PersistenceException e;

    /**
     * <p>
     * The accuracy test for method {@link PersistenceException#PersistenceException(String)}.
     * </p>
     */
    @Test
    public void testPersistenceExceptionString() {
        e = new PersistenceException("test");
        assertNull("The cause should be null.", e.getCause());
        assertEquals("The message is incorrect.", "test", e.getMessage());
        assertTrue("the class type is incorrect.", ObjectCacheException.class.isInstance(e));
    }

    /**
     * <p>
     * The accuracy test for method {@link PersistenceException#PersistenceException(String, Throwable)} .
     * </p>
     */
    @Test
    public void testPersistenceExceptionStringThrowable() {
        Exception cause = new Exception();
        e = new PersistenceException("test", cause);
        assertNull("The cause is incorrect.", cause.getCause());
        assertEquals("The message is incorrect.", "test", e.getMessage());
    }
}

