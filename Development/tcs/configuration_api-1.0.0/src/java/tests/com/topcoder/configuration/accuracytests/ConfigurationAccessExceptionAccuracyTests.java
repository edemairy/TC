/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.accuracytests;


import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationException;

import junit.framework.TestCase;
/**
 * The unit test of ConfigurationAccessException.
 *
 * @author KKD
 * @version 1.0
 */
public class ConfigurationAccessExceptionAccuracyTests extends TestCase {
    /** Represents the error message used for testing. */
    private static final String ERROR_MESSAGE = "test error message";

    /**
     * <p>
     * Inheritance test for class <code>ConfigurationAccessException</code>.
     * </p>
     *
     * <p>
     * Verifies <code>ConfigurationAccessException</code>. subclasses
     * <code>ConfigurationException</code>.
     * </p>
     */
    public void testConfigurationAccessExceptionInheritance() {
        assertTrue("ConfigurationAccessException does not subclass ConfigurationException.",
            new ConfigurationAccessException(ERROR_MESSAGE) instanceof ConfigurationException);
    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>ConfigurationAccessException()</code>.
     * </p>
     *
     * <p>
     * Verifies the instance could be successfully created.
     * </p>
     */
    public void testConfigurationAccessException() {
        ConfigurationAccessException spe = new ConfigurationAccessException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate ConfigurationAccessException.",
            spe);
    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>ConfigurationAccessException(String)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testConfigurationAccessExceptionString() {
        ConfigurationAccessException spe = new ConfigurationAccessException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate ConfigurationAccessException.",
            spe);
        assertEquals("Error message is not properly propagated to super class.",
            ERROR_MESSAGE, spe.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>ConfigurationAccessException(String)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testConfigurationAccessExceptionStringThrowable() {
        Throwable cause = new Exception();
        ConfigurationAccessException spe = new ConfigurationAccessException(ERROR_MESSAGE,
                cause);
        assertNotNull("Unable to instantiate ConfigurationAccessException.",
            spe);
    }
}
