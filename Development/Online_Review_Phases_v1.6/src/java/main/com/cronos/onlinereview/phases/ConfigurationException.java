/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * Represents an exception related to loading configuration settings. Inner
 * exception should be provided to give more details about the error. It is used
 * in PhaseHandler implementation classes.
 * </p>
 * <p>
 * Thread safety: This class is immutable and thread safe.
 * </p>
 *
 * @author tuenm, bose_java
 * @version 1.0
 */
public class ConfigurationException extends BaseException {
    /**
     * The generated serial version UID.
     */
    private static final long serialVersionUID = 5786137119616593442L;

    /**
     * Create a new ConfigurationException instance with the specified error
     * message.
     *
     * @param message the error message of the exception
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Create a new ConfigurationException instance with the specified error
     * message and inner exception.
     *
     * @param message the error message of the exception
     * @param cause the inner exception
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
