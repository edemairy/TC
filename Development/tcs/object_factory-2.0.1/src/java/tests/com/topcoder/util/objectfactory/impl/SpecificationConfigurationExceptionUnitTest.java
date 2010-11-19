/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

import junit.framework.TestCase;


/**
 * Unit tests for SpecificationConfigurationException.
 *
 * @author mgmg
 * @version 2.0
 */
public class SpecificationConfigurationExceptionUnitTest extends TestCase {
    /**
     * Test the constructor with one message parameter.
     * The object should be created correctly.
     */
    public void testConstructor1() {
        SpecificationConfigurationException e = new SpecificationConfigurationException("message");

        assertNotNull("The object should not be null", e);
    }

    /**
     * Test the constructor with two parameters.
     * The object should be created correctly.
     */
    public void testConstructor2() {
        SpecificationConfigurationException e =
            new SpecificationConfigurationException("message", new Exception());

        assertNotNull("The object should not be null", e);
    }
}
