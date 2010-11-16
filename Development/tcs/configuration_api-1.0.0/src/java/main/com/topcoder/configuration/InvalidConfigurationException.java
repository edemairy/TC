/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;


/**
 * <p>
 * This exception indicates given property key, property value, child name or
 * child instance is not acceptable by specific ConfigurationObject
 * implementation, like a cycle occurs after adding some child. For example, the
 * default implementation requires all the property values are instances of Serialiable.
 * </p>
 *
 * <p>
 * It can be thrown from methods updating properties and adding children. In the
 * default implementation, this exception is thrown for non-Serializable property values.
 * </p>
 *
 * <p>Thread safe: This class is immutable and thus thread safe.</p>
 *
 * @author maone, TCSDEVELOPER
 * @version 1.0
 */
public class InvalidConfigurationException extends ConfigurationException {
    /**
     * Constructs a new InvalidConfigurationException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidConfigurationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
