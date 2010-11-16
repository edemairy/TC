/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.failuretests;

import com.topcoder.configuration.SynchronizedConfigurationObject;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Failure test cases for SynchronizedConfigurationObject.
 * </p>
 *
 * @author biotrail
 * @version 1.0
 */
public class SynchronizedConfigurationObjectFailureTests extends TestCase {
    /**
     * <p>
     * Return all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(SynchronizedConfigurationObjectFailureTests.class);
    }

    /**
     * <p>
     * Tests ctor SynchronizedConfigurationObject#SynchronizedConfigurationObject(ConfigurationObject) for failure.
     * It tests the case that when configObj is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullConfigObj() {
        try {
            new SynchronizedConfigurationObject(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }
}