/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import junit.framework.TestCase;


/**
 * <p>
 * The unit test of InvalidConfigurationException.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class InvalidConfigurationExceptionTests extends TestCase {
    /** Represents the error message used for testing. */
    private static final String ERROR_MESSAGE = "test error message";

    /**
     * <p>
     * Inheritance test for class <code>InvalidConfigurationException</code>.
     * </p>
     *
     * <p>
     * Verifies <code>InvalidConfigurationException</code>. subclasses
     * <code>ConfigurationException</code>.
     * </p>
     */
    public void testInvalidConfigurationExceptionInheritance() {
        assertTrue("InvalidConfigurationException does not subclass ConfigurationException.",
            new InvalidConfigurationException(ERROR_MESSAGE) instanceof ConfigurationException);
    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>InvalidConfigurationException()</code>.
     * </p>
     *
     * <p>
     * Verifies the instance could be successfully created.
     * </p>
     */
    public void testInvalidConfigurationException() {
        InvalidConfigurationException spe = new InvalidConfigurationException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate InvalidConfigurationException.",
            spe);
    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>InvalidConfigurationException(String)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testInvalidConfigurationExceptionString() {
        InvalidConfigurationException spe = new InvalidConfigurationException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate InvalidConfigurationException.",
            spe);
        assertEquals("Error message is not properly propagated to super class.",
            ERROR_MESSAGE, spe.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>InvalidConfigurationException(String)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testInvalidConfigurationExceptionStringThrowable() {
        Throwable cause = new Exception();
        InvalidConfigurationException spe = new InvalidConfigurationException(ERROR_MESSAGE,
                cause);
        assertNotNull("Unable to instantiate InvalidConfigurationException.",
            spe);
    }
}
