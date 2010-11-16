/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.stresstests;

import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.TestHelper;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;
import com.topcoder.search.builder.filter.LikeFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.search.builder.hibernate.HibernateSearchStrategy;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Stress tests for <code>HibernateSearchStrategy</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.4
 */
public class HibernateSearchStrategyStressTests extends TestCase {
    /**
     * <p>
     * Represents the stress test count.
     * </p>
     */
    private static long COUNT = 500;

    /**
     * <p>
     * Represents the start time milliseconds.
     * </p>
     */
    private long start;

    /**
     * <p>
     * Represents the SimpleHibernateSearchStrategy.
     * </p>
     */
    private SimpleHibernateSearchStrategy strategy;

    /**
     * <p>
     * Represents the alias map.
     * </p>
     */
    private Map aliasMap;

    /**
     * <p>
     * Sets up.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    protected void setUp() throws Exception {
        TestHelper.clearConfig();
        TestHelper.addConfig();

        aliasMap = new HashMap();
        aliasMap.put("The age", "p.age");
        aliasMap.put("The company name", "c.name");

        strategy = new SimpleHibernateSearchStrategy("HibernateSearchStrategy");
    }

    /**
     * <p>
     * Tears down.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        TestHelper.clearConfig();
    }

    /**
     * <p>
     * Prints the test result.
     * </p>
     *
     * @param method
     *            the method to test
     */
    private void printTestResult(String method) {
        System.out.println("Stress test for the " + method + " " + COUNT + " times, cost "
                + (System.currentTimeMillis() - start) + " milliseconds");
    }

    /**
     * <p>
     * Stress test for the <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code>
     * method.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchStress1() throws Exception {
        String context = "FROM Person as p join p.company as c WHERE";

        EqualToFilter equalToFilter1 = new EqualToFilter("The age", new Integer(23));
        EqualToFilter equalToFilter2 = new EqualToFilter("p.sex", "Female");
        AndFilter andFilter = new AndFilter(equalToFilter1, equalToFilter2);
        equalToFilter2 = new EqualToFilter("p.sex", "Male");
        OrFilter orFilter = new OrFilter(equalToFilter1, equalToFilter2);

        List values = new ArrayList();
        values.add("Petr");
        values.add("CodeDoc");
        List list = new ArrayList();
        list.add("sex");

        start = System.currentTimeMillis();

        for (int i = 0; i < COUNT; ++i) {
            // test EqualToFilter
            List result = (List) strategy.search(context, equalToFilter1, new ArrayList(), aliasMap);
            assertEquals(2, result.size());

            // test AndFilter
            result = (List) strategy.search(context, andFilter, new ArrayList(), aliasMap);
            assertEquals(1, result.size());

            // test OrFilter
            result = (List) strategy.search(context, orFilter, new ArrayList(), aliasMap);
            assertEquals(3, result.size());

            // test NotFilter
            result = (List) strategy.search(context, new NotFilter(orFilter), new ArrayList(), aliasMap);
            assertEquals(0, result.size());

            // test InFilter
            result = (List) strategy.search(context, new InFilter("p.name", values), new ArrayList(), aliasMap);
            assertEquals(2, result.size());

            // test BetweenFilter
            result = (List) strategy.search(context, new BetweenFilter("p.age", new Integer(60), new Integer(50)),
                    list, aliasMap);
            assertEquals(1, result.size());

            // test LessThanOrEqualToFilter
            list = (List) strategy.search(context, new LessThanOrEqualToFilter("The age", new Integer(53)),
                    new ArrayList(), aliasMap);
            assertEquals(3, list.size());

            // test GreaterThanOrEqualToFilter
            list = (List) strategy.search(context, new GreaterThanOrEqualToFilter("The age", new Integer(53)),
                    new ArrayList(), aliasMap);
            assertEquals(1, list.size());

            // test GreaterThanFilter
            list = (List) strategy.search(context, new GreaterThanFilter("The age", new Integer(53)),
                    new ArrayList(), aliasMap);
            assertEquals(0, list.size());
        }

        this.printTestResult("search");
    }

    /**
     * <p>
     * Stress test for the <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code>
     * method.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchStress2() throws Exception {
        String context = "SELECT p.name, c.name FROM Person as p join p.company as c WHERE";
        EqualToFilter equalToFilter1 = new EqualToFilter("The age", new Integer(23));
        EqualToFilter equalToFilter2 = new EqualToFilter("p.sex", "Female");
        List values = new ArrayList();
        values.add("Petr");
        values.add("CodeDoc");

        start = System.currentTimeMillis();

        for (int i = 0; i < COUNT; ++i) {
            // test EqualToFilter
            List result = (List) strategy.search(context, equalToFilter1, new ArrayList(), aliasMap);
            assertEquals(2, result.size());

            // test AndFilter
            AndFilter andFilter = new AndFilter(equalToFilter1, equalToFilter2);
            result = (List) strategy.search(context, andFilter, new ArrayList(), aliasMap);
            assertEquals(1, result.size());

            // test OrFilter
            OrFilter orFilter = new OrFilter(equalToFilter1, equalToFilter2);
            result = (List) strategy.search(context, orFilter, new ArrayList(), aliasMap);
            assertEquals(2, result.size());

            // test NotFilter
            result = (List) strategy.search(context, new NotFilter(orFilter), new ArrayList(), aliasMap);
            assertEquals(1, result.size());

            // test LikeFilter
            List list = (List) strategy.search(context, new LikeFilter("p.name", "SW:P", '!'), new ArrayList(),
                    aliasMap);
            assertEquals(1, list.size());

            // test InFilter
            list = (List) strategy.search(context, new InFilter("p.name", values), new ArrayList(), aliasMap);
            assertEquals(2, list.size());

            // test BetweenFilter
            result = (List) strategy.search(context, new BetweenFilter("p.age", new Integer(60), new Integer(50)),
                    new ArrayList(), aliasMap);
            assertEquals(1, result.size());

            list = (List) strategy.search(context, new GreaterThanFilter("The age", new Integer(53)),
                    new ArrayList(), aliasMap);
            assertEquals(0, list.size());
        }

        this.printTestResult("search");
    }

    /**
     * <p>
     * Stress test for the
     * <code>buildSearchContext(String context, Filter filter, List returnFields, Map aliasMap)</code> method.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchContextStress() throws Exception {
        String context = "FROM Person as p join p.company as c WHERE";
        EqualToFilter equalToFilter1 = new EqualToFilter("The age", new Integer(23));
        EqualToFilter equalToFilter2 = new EqualToFilter("p.sex", "Female");
        AndFilter andFilter = new AndFilter(equalToFilter1, equalToFilter2);

        start = System.currentTimeMillis();

        for (int i = 0; i < COUNT; ++i) {
            SearchContext searchContext = strategy.buildSearchContext(context, new EqualToFilter("age", new Integer(
                    10)), new ArrayList(), aliasMap);
            assertEquals("FROM Person as p join p.company as c WHERE age = ?", searchContext.getSearchString()
                    .toString());

            searchContext = strategy.buildSearchContext(context, andFilter, new ArrayList(), aliasMap);
            assertEquals("FROM Person as p join p.company as c WHERE (p.age = ? AND p.sex = ?)", searchContext
                    .getSearchString().toString());

            searchContext = strategy.buildSearchContext(context, new BetweenFilter("The age", new Integer(50),
                    new Integer(60)), new ArrayList(), aliasMap);

            assertEquals("FROM Person as p join p.company as c WHERE p.age BETWEEN ? AND ?", searchContext
                    .getSearchString().toString());
        }

        this.printTestResult("buildSearchContext");
    }

    private class SimpleHibernateSearchStrategy extends HibernateSearchStrategy {

        public SimpleHibernateSearchStrategy() throws SearchBuilderConfigurationException {
            super();
        }

        public SimpleHibernateSearchStrategy(String namespace) throws SearchBuilderConfigurationException {
            super(namespace);
        }

        protected SearchContext buildSearchContext(String context, Filter filter, List returnFields, Map aliasMap)
                throws UnrecognizedFilterException {
            return super.buildSearchContext(context, filter, returnFields, aliasMap);
        }
    }
}
