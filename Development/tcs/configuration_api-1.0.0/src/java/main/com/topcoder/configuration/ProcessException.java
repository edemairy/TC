/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;


/**
 * <p>
 * This exception indicates an error occurs while processing ConfigurationObject intances.
 * It can be thrown from Processor implementations, and ConfigurationObject#processDescendants method.
 * </p>
 *
 * <p>Thread safe: This class is immutable and thus thread safe.</p>
 *
 * @author maone, TCSDEVELOPER
 * @version 1.0
 */
public class ProcessException extends ConfigurationException {
    /**
     * Constructs a new InvalidConfigurationException with the specified detail message.
     *
     * @param message the detail message
     */
    public ProcessException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidConfigurationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public ProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
