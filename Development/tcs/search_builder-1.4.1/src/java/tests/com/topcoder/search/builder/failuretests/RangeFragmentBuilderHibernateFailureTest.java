/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.failuretests;

import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.hibernate.RangeFragmentBuilder;

/**
 * <p>
 * Failure test cases for <code>RangeFragmentBuilder</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.4
 */
public class RangeFragmentBuilderHibernateFailureTest extends BaseFragmentBuilderHibernateTest {
    /**
     * <p>
     * Failure test for the method <code>buildSearch(Filter filter, SearchContext searchContext)</code> with the
     * filter is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchWithFilterNull() throws Exception {
        try {
            new RangeFragmentBuilder().buildSearch(null, searchContext);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>buildSearch(Filter filter, SearchContext searchContext)</code> with the
     * searchContext is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchWithSearchContextNull() throws Exception {
        try {
            new RangeFragmentBuilder().buildSearch(new BetweenFilter("name", "100", "1"), null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>buildSearch(Filter filter, SearchContext searchContext)</code> with the
     * filter is not one of GreaterThanFilter, LessThanFilter, GreaterThanOrEqualToFilter, LessThanOrEqualToFilter or
     * BetweenFilter, UnrecognizedFilterException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchWithFilterTypeIncorrect() throws Exception {
        try {
            new RangeFragmentBuilder().buildSearch(new EqualToFilter("sb", "2"), searchContext);
            fail("UnrecognizedFilterException should be thrown.");
        } catch (UnrecognizedFilterException e) {
            // pass
        }
    }
}
