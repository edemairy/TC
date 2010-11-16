/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.accuracytests;

import com.topcoder.configuration.ConfigurationException;
import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;


/**
 * The unit test of ConfigurationException.
 *
 * @author KKD
 * @version 1.0
 */
public class ConfigurationExceptionAccuracyTests extends TestCase {
    /** Represents the error message used for testing. */
    private static final String ERROR_MESSAGE = "test error message";

    /**
     * <p>
     * Inheritance test for class <code>ConfigurationException</code>.
     * </p>
     *
     * <p>
     * Verifies <code>ConfigurationException</code>. subclasses
     * <code>ConfigurationException</code>.
     * </p>
     */
    public void testConfigurationExceptionInheritance() {
        assertTrue("ConfigurationException does not subclass BaseException.",
            new ConfigurationException(ERROR_MESSAGE) instanceof BaseException);
    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>ConfigurationException()</code>.
     * </p>
     *
     * <p>
     * Verifies the instance could be successfully created.
     * </p>
     */
    public void testConfigurationException() {
        ConfigurationException spe = new ConfigurationException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate ConfigurationException.",
            spe);
    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>ConfigurationException(String)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testConfigurationExceptionString() {
        ConfigurationException spe = new ConfigurationException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate ConfigurationException.",
            spe);
        assertEquals("Error message is not properly propagated to super class.",
            ERROR_MESSAGE, spe.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>ConfigurationException(String)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testConfigurationExceptionStringThrowable() {
        Throwable cause = new Exception();
        ConfigurationException spe = new ConfigurationException(ERROR_MESSAGE,
                cause);
        assertNotNull("Unable to instantiate ConfigurationException.",
            spe);
    }
}
