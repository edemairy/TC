/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

import com.topcoder.util.objectfactory.SpecificationFactoryException;


/**
 * <p>Throwns by the ConfigManagerSpecificationFactory constructor if cannot properly use
 * the ConfigManager, i.e. the namespace is not recognized.</p>
 *
 * @author codedoc, mgmg
 * @version 2.0
 */
public class SpecificationConfigurationException extends SpecificationFactoryException {
    /**
     * <p>Creates an instance with a descriptive error message.</p>
     *
     * @param message a descriptive error message
     */
    public SpecificationConfigurationException(String message) {
        super(message);
    }

    /**
     * <p>Creates an instance with a descriptive error message and the cause of the error.</p>
     *
     * @param message a descriptive error message
     * @param cause the cause of the error
     */
    public SpecificationConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
