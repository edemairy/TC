/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate.accuracytests;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.TestHelper;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.hibernate.AndFragmentBuilder;

import com.topcoder.util.classassociations.ClassAssociator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


/**
 * The accuracy test for the class {@link AndFragmentBuilder}.
 *
 * @author KLW
 * @version 1.4
 */
public class AndFragmentBuilderAccTests extends TestCase {
    /**
     * the AndFragmentBuilder instance for accuracy test.
     */
    private AndFragmentBuilder builder;

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
        builder = new AndFragmentBuilder();
        aliasMap = new HashMap();
        aliasMap.put("The name", "peoplename");
        aliasMap.put("The age", "age");
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
        Filter filter1 = new EqualToFilter("The name", "p1");
        Filter filter2 = new EqualToFilter("The age", new Integer(3));
        Filter andFilter = new AndFilter(filter1, filter2);
        builder.buildSearch(andFilter, searchContext);
        assertEquals("the generated String should be same.", "(peoplename = ? AND age = ?)",
            searchContext.getSearchString().toString());
        assertEquals("two parameters are added.", 2, searchContext.getBindableParameters().size());
    }
}