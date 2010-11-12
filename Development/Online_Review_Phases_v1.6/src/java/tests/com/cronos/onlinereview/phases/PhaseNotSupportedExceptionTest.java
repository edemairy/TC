/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import junit.framework.TestCase;

/**
 * Accuracy tests for PhaseNotSupportedException class.
 *
 * @author bose_java
 * @version 1.0
 */
public class PhaseNotSupportedExceptionTest extends TestCase {
    /** constant to hold error message for testing. */
    private static final String ERROR_MSG = "error msg";

    /**
     * Test that PhaseNotSupportedException(String) constructor propagates the
     * message argument.
     */
    public void testPhaseNotSupportedExceptionString() {
        PhaseNotSupportedException exc = new PhaseNotSupportedException(
                        ERROR_MSG);
        assertEquals("error msg not propagated to super.", ERROR_MSG, exc
                        .getMessage());
    }
}
