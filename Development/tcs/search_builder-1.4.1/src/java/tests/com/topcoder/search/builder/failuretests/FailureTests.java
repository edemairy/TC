/*
 * Copyright (c) 2008, TopCoder, Inc. All rights reserved
 */
package com.topcoder.search.builder.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class FailureTests extends TestCase {

    /**
     * <p>
     * Aggregates all Failure test cases.
     * </p>
     *
     * @return the aggregated test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(HibernateSearchStrategyFailureTest.class);

        suite.addTestSuite(AndFragmentBuilderHibernateFailureTest.class);
        suite.addTestSuite(EqualsFragmentBuilderHibernateFailureTest.class);
        suite.addTestSuite(InFragmentBuilderHibernateFailureTest.class);
        suite.addTestSuite(LikeFragmentBuilderHibernateFailureTest.class);
        suite.addTestSuite(NotFragmentBuilderHibernateFailureTest.class);
        suite.addTestSuite(NullFragmentBuilderHibernateFailureTest.class);
        suite.addTestSuite(OrFragmentBuilderHibernateFailureTest.class);
        suite.addTestSuite(RangeFragmentBuilderHibernateFailureTest.class);

        return suite;
    }
}
