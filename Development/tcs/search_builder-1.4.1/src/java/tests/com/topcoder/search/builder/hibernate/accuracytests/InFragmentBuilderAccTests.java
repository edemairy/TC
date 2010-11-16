/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate.accuracytests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.TestHelper;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.hibernate.AndFragmentBuilder;
import com.topcoder.search.builder.hibernate.InFragmentBuilder;
import com.topcoder.util.classassociations.ClassAssociator;

import junit.framework.TestCase;


/**
 * The accuracy test for the class {@link InFragmentBuilder}.
 *
 * @author KLW
 * @version 1.4
  */
public class InFragmentBuilderAccTests extends TestCase {
    /**
     * the InFragmentBuilder instance for accuracy test.
     */
    private InFragmentBuilder builder;

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
        builder = new InFragmentBuilder();
        aliasMap = new HashMap();
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
        List values = new ArrayList();
        values.add("p1");
        values.add("p2");
        values.add("p3");
        Filter filter = new InFilter("peoplename",values);
        builder.buildSearch(filter, searchContext);
        assertEquals("the generated String should be same.", "peoplename IN (?, ?, ?)",
            searchContext.getSearchString().toString());
        assertEquals("three parameters are added.", 3, searchContext.getBindableParameters().size());
    }
}
