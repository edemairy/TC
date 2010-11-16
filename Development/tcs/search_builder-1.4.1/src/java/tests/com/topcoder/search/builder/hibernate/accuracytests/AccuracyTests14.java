/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all accuracy test cases.
 * </p>
 *
 * @author KLW
 * @version 1.4
 */
public class AccuracyTests14 extends TestCase {
    /**
     * aggregates all accuracy test cases.
     *
     * @return a test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(AndFragmentBuilderAccTests.class);
        suite.addTestSuite(EqualsFragmentBuilderAccTests.class);
        suite.addTestSuite(HibernateSearchStrategyAccTests.class);
        suite.addTestSuite(InFragmentBuilderAccTests.class);
        suite.addTestSuite(LikeFragmentBuilderAccTests.class);
        suite.addTestSuite(NotFragmentBuilderAccTests.class);
        suite.addTestSuite(NullFragmentBuilderAccTests.class);
        suite.addTestSuite(OrFragmentBuilderAccTests.class);
        suite.addTestSuite(RangeFragmentBuilderAccTests.class);

        return suite;
    }
}
