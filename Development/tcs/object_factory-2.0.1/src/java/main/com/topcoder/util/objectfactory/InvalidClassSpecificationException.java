/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;


/**
 * <p>Thrown by the ObjectFactory createObject methods if the spceification is not valid and can't be used to
 * create an object.</p>
 *
 * @author codedoc, mgmg
 * @version 2.0
 */
public class InvalidClassSpecificationException extends ObjectCreationException {
    /**
     * <p>Creates an instance with a descriptive error message.</p>
     *
     * @param message a descriptive error message
     */
    public InvalidClassSpecificationException(String message) {
        super(message);
    }

    /**
     * <p>Creates an instance with a descriptive error message and the cause of the error.</p>
     *
     * @param message a descriptive error message
     * @param cause the cause of the error
     */
    public InvalidClassSpecificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
