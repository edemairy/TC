/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;


/**
 * <p>
 * This exception indicates an error occurs while accessing (reading or writing)
 * the configuration. It may cause by IO problem, database connection problem, and so on.
 * </p>
 *
 * <p>
 * It can be thrown from almost all of methods of ConfigurationObject interface.
 * But in the default implementation, this exception is never thrown. Because
 * default implementation is in memory.
 * </p>
 *
 * <p>
 * Thread safe: This class is immutable and thus thread safe.
 * </p>
 *
 * @author maone, TCSDEVELOPER
 * @version 1.0
 */
public class ConfigurationAccessException extends ConfigurationException {
    /**
     * Constructs a new ConfigurationAccessException with the specified detail message.
     *
     * @param message
     *            the detail message
     */
    public ConfigurationAccessException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConfigurationAccessException with the specified detail
     * message and cause.
     *
     * @param message
     *            the detail message
     * @param cause
     *            the cause of this exception
     */
    public ConfigurationAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
