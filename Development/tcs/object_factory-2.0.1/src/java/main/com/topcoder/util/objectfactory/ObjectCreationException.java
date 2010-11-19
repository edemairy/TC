/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;


/**
 * <p>Common exception for exceptions that deal with the life cycle of creating an object.</p>
 *
 * @author codedoc, mgmg
 * @version 2.0
 */
public class ObjectCreationException extends ObjectFactoryException {
    /**
     * <p>Creates an instance with a descriptive error message.</p>
     *
     * @param message a descriptive error message
     */
    public ObjectCreationException(String message) {
        super(message);
    }

    /**
     * <p>Creates an instance with a descriptive error message and the cause of the error.</p>
     *
     * @param message a descriptive error message
     * @param cause the cause of the error
     */
    public ObjectCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
