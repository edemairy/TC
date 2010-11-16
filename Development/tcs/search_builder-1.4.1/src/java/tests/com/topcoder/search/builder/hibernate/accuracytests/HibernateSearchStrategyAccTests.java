/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate.accuracytests;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.TestHelper;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;
import com.topcoder.search.builder.filter.LikeFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.search.builder.hibernate.HibernateSearchStrategy;

import com.topcoder.util.classassociations.ClassAssociator;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The accuracy test for the class {@link HibernateSearchStrategy}.
 * 
 * @author KLW
 * @version 1.4
 */
public class HibernateSearchStrategyAccTests extends TestCase {
    /**
     * the HibernateSearchStrategy for test.
     */
    private HibernateSearchStrategy strategy;

    /**
     * the map for test.
     */
    private Map aliasMap;

    /**
     * sets up the test environment.
     * 
     * @throws Exception
     *             all exception throw to JUnit.
     */
    protected void setUp() throws Exception {
        TestHelper.clearConfig();
        TestHelper.addConfig();
        ConfigManager.getInstance().removeNamespace("HibernateSearchStrategy");
        ConfigManager.getInstance().removeNamespace("com.topcoder.search.builder.hibernate.HibernateSearchStrategy");
        ConfigManager.getInstance().removeNamespace(
                "com.topcoder.search.builder.hibernate.factory");
        ConfigManager.getInstance().add("hibernateSearchStrategyConfigAcc.xml");
        strategy = new HibernateSearchStrategy();
        aliasMap = new HashMap();
        aliasMap.put("The name", "p.name");
        aliasMap.put("The age", "p.age");
        aliasMap.put("The sex", "p.sex");
        aliasMap.put("The company name", "c.name");
    }

    /**
     * tears down the test environment.
     * 
     * @throws Exception
     *             all exception throw to JUnit.
     */
    protected void tearDown() throws Exception {
        TestHelper.clearConfig();
        strategy = null;
        aliasMap.clear();
        aliasMap = null;
    }

    /**
     * the accuracy test for the constructor
     * {@link HibernateSearchStrategy#HibernateSearchStrategy()}.
     */
    public void test_ctor_1() {
        assertNotNull("The instance should not be null.", strategy);
    }

    /**
     * the accuracy test for the constructor
     * {@link HibernateSearchStrategy#HibernateSearchStrategy(String)}.
     * 
     * @throws Exception
     *             all exception throw to JUnit.
     */
    public void test_ctor_2() throws Exception {
        String namespace = HibernateSearchStrategy.class.getName();
        strategy = new HibernateSearchStrategy(namespace);
        assertNotNull("The instance should not be null.", strategy);
    }

    /**
     * the accuracy test for the constructor
     * {@link HibernateSearchStrategy#HibernateSearchStrategy(SessionFactory, com.topcoder.util.classassociations.ClassAssociator)}.
     * 
     * @throws Exception
     *             all exception throw to JUnit.
     */
    public void test_ctor_3() throws Exception {
        SessionFactory factory = new Configuration().configure(
                "sampleHibernateConfig.cfg.xml").buildSessionFactory();
        ClassAssociator classAssociator = SearchBuilderHelper
                .loadClassAssociator(HibernateSearchStrategy.class.getName());

        strategy = new HibernateSearchStrategy(factory, classAssociator);
        assertNotNull("The instance should not be null.", strategy);
    }

    /**
     * the accuracy test for the method
     * {@link HibernateSearchStrategy#search(String, com.topcoder.search.builder.filter.Filter, java.util.List, java.util.Map)}.
     */
    public void test_search_1() throws Exception {
        String context = "FROM Person as p join p.company as c WHERE";
        Filter filter = new EqualToFilter("The age", new Integer(23));
        List result = (List) strategy.search(context, filter, new ArrayList(),
                aliasMap);
        // it should contains 2 elements.
        assertEquals("it should contains 2 elements", 2, result.size());
    }

    /**
     * the accuracy test for the method
     * {@link HibernateSearchStrategy#search(String, com.topcoder.search.builder.filter.Filter, java.util.List, java.util.Map)}.
     */
    public void test_search_2() throws Exception {
        String context = "FROM Person as p join p.company as c WHERE";
        Filter filter1 = new EqualToFilter("The age", new Integer(23));
        Filter filter2 = new EqualToFilter("The sex", "Female");
        Filter filter = new AndFilter(filter1, filter2);
        List result = (List) strategy.search(context, filter, new ArrayList(),
                aliasMap);
        // it should contains 1 element.
        assertEquals("it should contains 1 element", 1, result.size());
    }

    /**
     * the accuracy test for the method
     * {@link HibernateSearchStrategy#search(String, com.topcoder.search.builder.filter.Filter, java.util.List, java.util.Map)}.
     */
    public void test_search_3() throws Exception {
        String context = "FROM Person as p join p.company as c WHERE";
        Filter filter1 = new EqualToFilter("The age", new Integer(23));
        Filter filter2 = new EqualToFilter("The name", "CodeDoc");
        Filter filter = new OrFilter(filter1, filter2);
        List result = (List) strategy.search(context, filter, new ArrayList(),
                aliasMap);
        // it should contains 3 elements.
        assertEquals("it should contains 3 element", 3, result.size());
    }

    /**
     * the accuracy test for the method
     * {@link HibernateSearchStrategy#search(String, com.topcoder.search.builder.filter.Filter, java.util.List, java.util.Map)}.
     */
    public void test_search_4() throws Exception {
        String context = "FROM Person as p join p.company as c WHERE";
        Filter filter1 = new EqualToFilter("The age", new Integer(23));
        Filter filter = new NotFilter(filter1);
        List result = (List) strategy.search(context, filter, new ArrayList(),
                aliasMap);
        // it should contains 1 elements.
        assertEquals("it should contains 1 elements", 1, result.size());
    }

    /**
     * the accuracy test for the method
     * {@link HibernateSearchStrategy#search(String, com.topcoder.search.builder.filter.Filter, java.util.List, java.util.Map)}.
     */
    public void test_search_5() throws Exception {
        String context = "FROM Person as p join p.company as c WHERE";
        Filter filter = new LikeFilter("The name", "EW:a");
        List result = (List) strategy.search(context, filter, new ArrayList(),
                aliasMap);
        assertEquals("it should contains 1 elements", 1, result.size());
    }

    /**
     * the accuracy test for the method
     * {@link HibernateSearchStrategy#search(String, com.topcoder.search.builder.filter.Filter, java.util.List, java.util.Map)}.
     */
    public void test_search_6() throws Exception {
        String context = "FROM Person as p join p.company as c WHERE";
        Filter filter = new BetweenFilter("The age", new Integer(24), new Integer(23));
        List result = (List) strategy.search(context, filter, new ArrayList(),
                aliasMap);
        assertEquals("it should contains 2 elements", 2, result.size());
    }

    /**
     * the accuracy test for the method
     * {@link HibernateSearchStrategy#search(String, com.topcoder.search.builder.filter.Filter, java.util.List, java.util.Map)}.
     */
    public void test_search_7() throws Exception {
        String context = "FROM Person as p join p.company as c WHERE";
        List values = new ArrayList();
        values.add(new Integer(23));
        values.add(new Integer(53));

        Filter filter = new InFilter("The age", values);
        List result = (List) strategy.search(context, filter, new ArrayList(),
                aliasMap);
        assertEquals("it should contains 3 elements", 3, result.size());
    }

    /**
     * the accuracy test for the method
     * {@link HibernateSearchStrategy#search(String, com.topcoder.search.builder.filter.Filter, java.util.List, java.util.Map)}.
     */
    public void test_search_8() throws Exception {
        String context = "FROM Person as p join p.company as c WHERE";
        Filter filter = new LessThanOrEqualToFilter("The age", new Integer(23));
        List result = (List) strategy.search(context, filter, new ArrayList(),
                aliasMap);
        assertEquals("it should contains 2 element", 2, result.size());
    }

    /**
     * the accuracy test for the method
     * {@link HibernateSearchStrategy#search(String, com.topcoder.search.builder.filter.Filter, java.util.List, java.util.Map)}.
     */
    public void test_search_9() throws Exception {
        String context = "FROM Person as p join p.company as c WHERE";
        Filter filter = new LessThanFilter("The age", new Integer(23));
        List result = (List) strategy.search(context, filter, new ArrayList(),
                aliasMap);
        assertEquals("it should contains 0 element", 0, result.size());
    }

    /**
     * the accuracy test for the method
     * {@link HibernateSearchStrategy#search(String, com.topcoder.search.builder.filter.Filter, java.util.List, java.util.Map)}.
     */
    public void test_search_10() throws Exception {
        String context = "FROM Person as p join p.company as c WHERE";
        Filter filter = new GreaterThanOrEqualToFilter("The age", new Integer(53));
        List result = (List) strategy.search(context, filter, new ArrayList(),
                aliasMap);
        assertEquals("it should contains 1 element", 1, result.size());
    }

    /**
     * the accuracy test for the method
     * {@link HibernateSearchStrategy#search(String, com.topcoder.search.builder.filter.Filter, java.util.List, java.util.Map)}.
     */
    public void test_search_11() throws Exception {
        String context = "FROM Person as p join p.company as c WHERE";
        Filter filter = new GreaterThanFilter("The age", new Integer(53));
        List result = (List) strategy.search(context, filter, new ArrayList(),
                aliasMap);
        assertEquals("it should contains 0 element", 0, result.size());
    }

    /**
     * the accuracy test for the method
     * {@link HibernateSearchStrategy#search(String, com.topcoder.search.builder.filter.Filter, java.util.List, java.util.Map)}.
     */
    public void test_search_12() throws Exception {
        String context = "SELECT FROM Person as p join p.company as c WHERE";
        Filter filter = new EqualToFilter("The age", new Integer(53));
        List resultFields = new ArrayList();
        resultFields.add("The name");
        resultFields.add("The age");
        List result = (List) strategy.search(context, filter, resultFields,
                aliasMap);
        assertEquals("it should contains 1 element", 1, result.size());
        Object[] objs = (Object[]) result.get(0);
        String name = (String) objs[0];
        Integer age = (Integer) objs[1];
        assertEquals("The name is incorrect.", "CodeDoc", name);
        assertEquals("The age is incorrect.", 53, age.intValue());
    }
}
