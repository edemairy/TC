/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Stress test cases.
 * </p>
 *
 * @author mumujava
 * @version 1.0
 */
public class StressTests extends TestCase {
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(CacheStatisticsPrinterImplStressTest.suite());
        suite.addTest(CachedObjectDAOImplStressTest.suite());
        suite.addTest(MemoryCacheImplStressTest.suite());
        suite.addTest(CacheManagerImplStressTest.suite());
        return suite;
    }
}
