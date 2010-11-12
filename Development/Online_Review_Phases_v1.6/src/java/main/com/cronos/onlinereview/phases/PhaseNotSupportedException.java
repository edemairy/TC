/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.phase.PhaseHandlingException;

/**
 * <p>
 * This exception is thrown by a phase handler when the input phase is not the
 * type the handler can handle. For example, ScreeningPhaseHandler can only
 * handle Screening phases.
 * </p>
 * <p>
 * It is used in phase handler classes.
 * </p>
 * <p>
 * Thread safety: This class is immutable and thread safe.
 * </p>
 *
 * @author tuenm, bose_java
 * @version 1.0
 */
public class PhaseNotSupportedException extends PhaseHandlingException {
    /**
     * The generated serial version UID.
     */
    private static final long serialVersionUID = -2534347268584751918L;

    /**
     * Create a new PhaseNotSupportedException instance with the specified error
     * message.
     *
     * @param message the error message of the exception
     */
    public PhaseNotSupportedException(String message) {
        super(message);
    }
}
