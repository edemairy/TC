/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import junit.framework.TestCase;


/**
 * Unit tests for InvalidClassSpecificationException.
 *
 * @author mgmg
 * @version 2.0
 */
public class InvalidClassSpecificationExceptionUnitTest extends TestCase {
    /**
     * Test the constructor with one message parameter.
     * The object should be created correctly.
     */
    public void testConstructor1() {
        InvalidClassSpecificationException e = new InvalidClassSpecificationException("message");

        assertNotNull("The object should not be null", e);
    }

    /**
     * Test the constructor with two parameters.
     * The object should be created correctly.
     */
    public void testConstructor2() {
        InvalidClassSpecificationException e =
            new InvalidClassSpecificationException("message", new Exception());

        assertNotNull("The object should not be null", e);
    }
}
