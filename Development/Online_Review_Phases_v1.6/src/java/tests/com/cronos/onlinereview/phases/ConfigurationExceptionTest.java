/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import junit.framework.TestCase;

/**
 * Accuracy tests for ConfigurationException class.
 *
 * @author bose_java
 * @version 1.0
 */
public class ConfigurationExceptionTest extends TestCase {
    /** constant to hold error message for testing. */
    private static final String ERROR_MSG = "error msg";

    /** constant to hold an exception for testing. */
    private static final Exception EXCEPTION = new Exception();

    /**
     * Test that ConfigurationException(String) constructor propagates the
     * message argument.
     */
    public void testConfigurationExceptionString() {
        ConfigurationException exc = new ConfigurationException(ERROR_MSG);
        assertEquals("error msg not propagated to super.", ERROR_MSG, exc
                        .getMessage());
    }

    /**
     * Test that ConfigurationException(String, Throwable) constructor
     * propagates the arguments.
     */
    public void testConfigurationExceptionStringThrowable() {
        ConfigurationException exc = new ConfigurationException(ERROR_MSG,
                        EXCEPTION);
        assertEquals("Throwable not propagated to super.", EXCEPTION, exc
                        .getCause());
    }
}
