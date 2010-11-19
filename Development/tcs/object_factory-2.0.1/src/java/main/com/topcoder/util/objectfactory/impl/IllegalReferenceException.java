/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

import com.topcoder.util.objectfactory.SpecificationFactoryException;


/**
 * <p>Thrown by the ConfigManagerSpecificationFactory constructor if cannot properly match specifications
 * to each other, or the properties are malformed.</p>
 *
 * @author codedoc, mgmg
 * @version 2.0
 */
public class IllegalReferenceException extends SpecificationFactoryException {
    /**
     * <p>Creates an instance with a descriptive error message.</p>
     *
     * @param message a descriptive error message
     */
    public IllegalReferenceException(String message) {
        super(message);
    }

    /**
     * <p>Creates an instance with a descriptive error message and the cause of the error.</p>
     *
     * @param message a descriptive error message
     * @param cause the cause of the error
     */
    public IllegalReferenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
