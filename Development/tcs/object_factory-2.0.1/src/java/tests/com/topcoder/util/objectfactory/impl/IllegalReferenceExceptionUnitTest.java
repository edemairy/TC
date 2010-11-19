/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

import junit.framework.TestCase;


/**
 * Unit tests for IllegalReferenceException.
 *
 * @author mgmg
 * @version 2.0
 */
public class IllegalReferenceExceptionUnitTest extends TestCase {
    /**
     * Test the constructor with one message parameter.
     * The object should be created correctly.
     */
    public void testConstructor1() {
        IllegalReferenceException e = new IllegalReferenceException("message");

        assertNotNull("The object should not be null", e);
    }

    /**
     * Test the constructor with two parameters.
     * The object should be created correctly.
     */
    public void testConstructor2() {
        IllegalReferenceException e = new IllegalReferenceException("message", new Exception());

        assertNotNull("The object should not be null", e);
    }
}
