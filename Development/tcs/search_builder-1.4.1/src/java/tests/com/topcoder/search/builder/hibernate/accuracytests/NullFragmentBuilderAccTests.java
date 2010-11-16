/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate.accuracytests;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.TestHelper;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.NullFilter;
import com.topcoder.search.builder.hibernate.NullFragmentBuilder;

import com.topcoder.util.classassociations.ClassAssociator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


/**
 * The accuracy test for the class {@link NullFragmentBuilder}.
 *
 * @author KLW
 * @version 1.4
 */
public class NullFragmentBuilderAccTests extends TestCase {
    /**
     * the NullFragmentBuilder instance for accuracy test.
     */
    private NullFragmentBuilder builder;

    /**
     * The map of alias name and real name.
     */
    private Map aliasMap;

    /**
     * The class ClassAssociator.
     */
    private ClassAssociator classAssociator;

    /**
     * The SearchContext.
     */
    private SearchContext searchContext;

    /**
     * sets up the test environment.
     *
     * @throws Exception
     *             all exception throw to JUnit.
     */
    protected void setUp() throws Exception {
        TestHelper.clearConfig();
        TestHelper.addConfig();
        builder = new NullFragmentBuilder();
        aliasMap = new HashMap();
        aliasMap.put("The name", "peoplename");
        classAssociator = SearchBuilderHelper.loadClassAssociator("HibernateSearchStrategy");
        searchContext = new SearchContext(classAssociator, aliasMap);
    }

    /**
     * tears down the test environment.
     *
     * @throws Exception
     *             all exception throw to JUnit.
     */
    protected void tearDown() throws Exception {
        TestHelper.clearConfig();
        aliasMap.clear();
        aliasMap = null;
        builder = null;
        searchContext = null;
        classAssociator = null;
    }

    /**
     * the accuracy test for the constructor {@link AndFragmentBuilder#AndFragmentBuilder()}.
     */
    public void test_ctor() {
        assertNotNull("The instance should not be null.", builder);
    }

    /**
     * the accuracy test for the method
     * {@link AndFragmentBuilder#buildSearch(Filter, SearchContext)}.
     *
     * @throws Exception
     *             all exception throw to JUnit.
     */
    public void test_buildSearch() throws Exception {
        Filter filter = new NullFilter("The name");
        builder.buildSearch(filter, searchContext);
        assertEquals("the generated String should be same.", "peoplename IS NULL",
            searchContext.getSearchString().toString());
        assertEquals("no parameter are added.", 0, searchContext.getBindableParameters().size());
    }
}
