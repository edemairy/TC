/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.accuracytests;

import com.topcoder.configuration.ConfigurationException;
import com.topcoder.configuration.ProcessException;

import junit.framework.TestCase;


/**
 * The unit test of ProcessException.
 *
 * @author KKD
 * @version 1.0
 */
public class ProcessExceptionAccuracyTests extends TestCase {
    /** Represents the error message used for testing. */
    private static final String ERROR_MESSAGE = "test error message";

    /**
     * <p>
     * Inheritance test for class <code>ProcessException</code>.
     * </p>
     *
     * <p>
     * Verifies <code>ProcessException</code>. subclasses
     * <code>ConfigurationException</code>.
     * </p>
     */
    public void testProcessExceptionInheritance() {
        assertTrue("ProcessException does not subclass ConfigurationException.",
            new ProcessException(ERROR_MESSAGE) instanceof ConfigurationException);
    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>ProcessException()</code>.
     * </p>
     *
     * <p>
     * Verifies the instance could be successfully created.
     * </p>
     */
    public void testProcessException() {
        ProcessException spe = new ProcessException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate ProcessException.",
            spe);
    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>ProcessException(String)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testProcessExceptionString() {
        ProcessException spe = new ProcessException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate ProcessException.",
            spe);
        assertEquals("Error message is not properly propagated to super class.",
            ERROR_MESSAGE, spe.getMessage());
    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>ProcessException(String)</code>.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testProcessExceptionStringThrowable() {
        Throwable cause = new Exception();
        ProcessException spe = new ProcessException(ERROR_MESSAGE,
                cause);
        assertNotNull("Unable to instantiate ProcessException.",
            spe);
    }
}
