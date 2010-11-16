/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import com.topcoder.util.errorhandling.BaseException;


/**
 * <p>
 * This class is the base exception of this component.
 * It provides user the supports for dealing with all the exceptions from this component as a whole.
 * </p>
 *
 * <p>
 * Currently, there are three extensions of this exception,
 * ProcessException, ConfigurationAccessException and InvlaidConfigurationException.
 * </p>
 *
 * <p>Thread safe: This class is thread safe since it is immutable.</p>
 *
 * @author maone, TCSDEVELOPER
 * @version 1.0
 */
public class ConfigurationException extends BaseException {
    /**
     * Constructs a new ConfigurationException with the specified detail message.
     *
     * @param message the detail message
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConfigurationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
