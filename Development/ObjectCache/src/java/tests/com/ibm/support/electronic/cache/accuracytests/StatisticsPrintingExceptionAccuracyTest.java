/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.accuracytests;

import junit.framework.TestCase;

import org.junit.Test;

import com.ibm.support.electronic.cache.ObjectCacheException;
import com.ibm.support.electronic.cache.StatisticsPrintingException;

/**
 * Unit test for {@link StatisticsPrintingException}.
 *
 * @author KLW
 * @version 1.0
 */
public class StatisticsPrintingExceptionAccuracyTest extends TestCase {
    /** the exception to test. */
    private StatisticsPrintingException e;

    /**
     * <p>
     * The accuracy test for method {@link StatisticsPrintingException#StatisticsPrintingException(String)}.
     * </p>
     */
    @Test
    public void testStatisticsPrintingExceptionString() {
        e = new StatisticsPrintingException("test");
        assertNull("The cause should be null.", e.getCause());
        assertEquals("The message is incorrect.", "test", e.getMessage());
        assertTrue("the class type is incorrect.", ObjectCacheException.class.isInstance(e));
    }

    /**
     * <p>
     * The accuracy test for method {@link StatisticsPrintingException#StatisticsPrintingException(String, Throwable)} .
     * </p>
     */
    @Test
    public void testStatisticsPrintingExceptionStringThrowable() {
        Exception cause = new Exception();
        e = new StatisticsPrintingException("test", cause);
        assertNull("The cause is incorrect.", cause.getCause());
        assertEquals("The message is incorrect.", "test", e.getMessage());
    }
}

