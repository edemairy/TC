/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;


/**
 * <p>The reference passed by the factory, which referes to the type and/or identifier, refers to a mapping
 * that does not exist in the specification factory, or the speficication tree for this reference contains
 * a mapping that does not exist. For example, the type and idnetifier might map to a valid specification,
 * but one of its parameters might not. Thrown by the getObjectSpecification method in the SpecificationFactory.</p>
 *
 * @author codedoc, mgmg
 * @version 2.0
 */
public class UnknownReferenceException extends SpecificationFactoryException {
    /**
     * <p>Creates an instance with a descriptive error message.</p>
     *
     * @param message a descriptive error message
     */
    public UnknownReferenceException(String message) {
        super(message);
    }

    /**
     * <p>Creates an instance with a descriptive error message and the cause of the error.</p>
     *
     * @param message a descriptive error message
     * @param cause the cause of the error
     */
    public UnknownReferenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
