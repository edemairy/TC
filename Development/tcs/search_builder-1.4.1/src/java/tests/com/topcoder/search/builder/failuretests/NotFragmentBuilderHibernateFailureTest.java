/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.failuretests;

import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.hibernate.NotFragmentBuilder;

/**
 * <p>
 * Failure test cases for <code>NotFragmentBuilder</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.4
 */
public class NotFragmentBuilderHibernateFailureTest extends BaseFragmentBuilderHibernateTest {

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
            new NotFragmentBuilder().buildSearch(null, searchContext);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
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
            new NotFragmentBuilder().buildSearch(new NotFilter(new EqualToFilter("sn", "2")), null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for the method <code>buildSearch(Filter filter, SearchContext searchContext)</code> with the
     * filter is not NotFilter, UnrecognizedFilterException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchWithFilterIncorrectType() throws Exception {
        try {
            new NotFragmentBuilder().buildSearch(new EqualToFilter("sb", "2"), searchContext);
            fail("UnrecognizedFilterException should be thrown.");
        } catch (UnrecognizedFilterException e) {
            // expected
        }
    }
}
