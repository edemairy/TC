/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 *
 * @author chuchao333
 * @version 1.0
 */
public class FailureTests extends TestCase {
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(CachedObjectDAOImplFailureTest.suite());
        suite.addTest(CacheManagerImplFailureTest.suite());
        suite.addTest(MemoryCacheImplFailureTest.suite());
        return suite;
    }
}
