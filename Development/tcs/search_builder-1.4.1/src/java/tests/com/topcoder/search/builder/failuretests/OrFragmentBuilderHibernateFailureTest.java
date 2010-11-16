/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.failuretests;

import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.hibernate.OrFragmentBuilder;

/**
 * <p>
 * Failure test cases for <code>OrFragmentBuilder</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.4
 */
public class OrFragmentBuilderHibernateFailureTest extends BaseFragmentBuilderHibernateTest {
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
            new OrFragmentBuilder().buildSearch(null, searchContext);
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
        Filter filter = new OrFilter(new EqualToFilter("sb", "2"), new EqualToFilter("The age", "1"));

        try {
            new OrFragmentBuilder().buildSearch(filter, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test for the method <code>buildSearch(Filter filter, SearchContext searchContext)</code> with the
     * filter is not OrFilter, UnrecognizedFilterException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchWithFilterNotOrFilter() throws Exception {
        try {
            new OrFragmentBuilder().buildSearch(new EqualToFilter("sb", "2"), searchContext);
            fail("UnrecognizedFilterException should be thrown.");
        } catch (UnrecognizedFilterException e) {
            // pass
        }
    }
}
