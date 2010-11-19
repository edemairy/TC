/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import com.topcoder.util.objectfactory.accuracytests.AccuracyTests;
import com.topcoder.util.objectfactory.failuretests.FailureTests;
import com.topcoder.util.objectfactory.stresstests.StressTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>This test case aggregates all test cases for this package.</p>
 *
 * @author mgmg
 * @version 2.0
 */
public class AllTests extends TestCase {
    /**
     * Aggregate all test cases.
     *
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // unit tests
        suite.addTest(UnitTests.suite());

        // failure tests.
        suite.addTest(FailureTests.suite());

        // accuracy tests.
        suite.addTest(AccuracyTests.suite());

        // stress tests.
        suite.addTest(StressTests.suite());

        return suite;
    }
}
