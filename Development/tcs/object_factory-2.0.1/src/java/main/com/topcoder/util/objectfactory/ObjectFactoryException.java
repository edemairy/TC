/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import com.topcoder.util.errorhandling.BaseException;


/**
 * <p>Common exception for the ObjectFactory.</p>
 *
 * @author codedoc, mgmg
 * @version 2.0
 */
public class ObjectFactoryException extends BaseException {
    /**
     * <p>Creates an instance with a descriptive error message.</p>
     *
     * @param message a descriptive error message
     */
    public ObjectFactoryException(String message) {
        super(message);
    }

    /**
     * <p>Creates an instance with a descriptive error message and the cause of the error.</p>
     *
     * @param message a descriptive error message
     * @param cause the cause of the error
     */
    public ObjectFactoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
