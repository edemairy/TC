/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate.accuracytests;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.TestHelper;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;
import com.topcoder.search.builder.hibernate.AndFragmentBuilder;
import com.topcoder.search.builder.hibernate.RangeFragmentBuilder;

import com.topcoder.util.classassociations.ClassAssociator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


/**
 * The accuracy test for the class {@link RangeFragmentBuilder}.
 *
 * @author KLW
 * @version 1.4
 */
public class RangeFragmentBuilderAccTests extends TestCase {
    /**
     * the RangeFragmentBuilder instance for accuracy test.
     */
    private RangeFragmentBuilder builder;

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
        builder = new RangeFragmentBuilder();
        aliasMap = new HashMap();
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
    public void test_buildSearch_1() throws Exception {
        Filter filter = new GreaterThanFilter("The age", new Integer(23));
        builder.buildSearch(filter, searchContext);
        assertEquals("the generated String should be same.", "age > ?", searchContext.getSearchString().toString());
        assertEquals("one parameters are added.", 1, searchContext.getBindableParameters().size());
    }

    /**
     * the accuracy test for the method
     * {@link AndFragmentBuilder#buildSearch(Filter, SearchContext)}.
     *
     * @throws Exception
     *             all exception throw to JUnit.
     */
    public void test_buildSearch_2() throws Exception {
        Filter filter = new GreaterThanOrEqualToFilter("The age", new Integer(23));
        builder.buildSearch(filter, searchContext);
        assertEquals("the generated String should be same.", "age >= ?", searchContext.getSearchString().toString());
        assertEquals("one parameters are added.", 1, searchContext.getBindableParameters().size());
    }

    /**
     * the accuracy test for the method
     * {@link AndFragmentBuilder#buildSearch(Filter, SearchContext)}.
     *
     * @throws Exception
     *             all exception throw to JUnit.
     */
    public void test_buildSearch_3() throws Exception {
        Filter filter = new LessThanFilter("The age", new Integer(23));
        builder.buildSearch(filter, searchContext);
        assertEquals("the generated String should be same.", "age < ?", searchContext.getSearchString().toString());
        assertEquals("one parameters are added.", 1, searchContext.getBindableParameters().size());
    }

    /**
     * the accuracy test for the method
     * {@link AndFragmentBuilder#buildSearch(Filter, SearchContext)}.
     *
     * @throws Exception
     *             all exception throw to JUnit.
     */
    public void test_buildSearch_4() throws Exception {
        Filter filter = new LessThanOrEqualToFilter("The age", new Integer(23));
        builder.buildSearch(filter, searchContext);
        assertEquals("the generated String should be same.", "age <= ?", searchContext.getSearchString().toString());
        assertEquals("one parameters are added.", 1, searchContext.getBindableParameters().size());
    }

    /**
     * the accuracy test for the method
     * {@link AndFragmentBuilder#buildSearch(Filter, SearchContext)}.
     *
     * @throws Exception
     *             all exception throw to JUnit.
     */
    public void test_buildSearch_5() throws Exception {
        Filter filter = new BetweenFilter("The age", new Integer(23), new Integer(32));
        builder.buildSearch(filter, searchContext);
        assertEquals("the generated String should be same.", "age BETWEEN ? AND ?",
            searchContext.getSearchString().toString());
        assertEquals("two parameter are added.", 2, searchContext.getBindableParameters().size());
    }
}
