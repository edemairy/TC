/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.failuretests;

import java.util.HashMap;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.TestHelper;

import junit.framework.TestCase;

/**
 * <p>
 * Base test cases for fragment builder.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.4
 */
public class BaseFragmentBuilderHibernateTest extends TestCase {

    /**
     * <p>
     * Represents the <code>SearchContext</code> instance for test.
     * </p>
     */
    protected SearchContext searchContext;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    protected void setUp() throws Exception {
        TestHelper.clearConfig();
        TestHelper.addConfig();
        searchContext = new SearchContext(SearchBuilderHelper.loadClassAssociator("HibernateSearchStrategy"),
                new HashMap());
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        TestHelper.clearConfig();
    }
}
