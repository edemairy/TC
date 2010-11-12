/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.sql.databaseabstraction.accuracytests.AccuracyTests;
import com.topcoder.util.sql.databaseabstraction.failuretests.FailureTests;
import com.topcoder.util.sql.databaseabstraction.stresstests.StressTests;

/**
 * <p>
 * This test case aggregates all test cases for this package.
 * </p>
 *
 * @author justforplay
 * @version 1.1
 * @since 1.0
 */
public class AllTests extends TestCase {

    /**
     * all test suite.
     *
     * @return test.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // unit tests
        suite.addTest(UnitTests.suite());

        // accuracy tests
        suite.addTest(AccuracyTests.suite());

        // failure tests
        suite.addTest(FailureTests.suite());

        // stress tests
        suite.addTest(StressTests.suite());

        return suite;
    }

}
