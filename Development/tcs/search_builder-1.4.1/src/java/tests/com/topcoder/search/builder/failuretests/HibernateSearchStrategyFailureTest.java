/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.failuretests;

import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.TestHelper;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.hibernate.HibernateSearchStrategy;

import junit.framework.TestCase;

import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Failure test cases for <code>HibernateSearchStrategy</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.4
 */
public class HibernateSearchStrategyFailureTest extends TestCase {
    /**
     * <p>
     * Represents the hibernate configuration file for test.
     * </p>
     */
    private static final String FILE = "failuretests/HibernateConfig.cfg.xml";

    /**
     * <p>
     * Represents the context for test.
     * </p>
     */
    private static final String CONTEXT = "FROM Person as p join p.company as c WHERE";

    /**
     * <p>
     * Represents the <code>DatabaseSearchStrategy</code> instance for test.
     * </p>
     */
    private HibernateSearchStrategy strategy;

    /**
     * <p>
     * Represents the map of alias name and real name for test.
     * </p>
     */
    private Map aliasMap;

    /**
     * <p>
     * Represents the <code>SessionFactory</code> instance for test.
     * </p>
     */
    private SessionFactory sessionFactory;

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

        aliasMap = new HashMap();
        aliasMap.put("The age", "p.age");
        aliasMap.put("The company name", "c.name");

        strategy = new HibernateSearchStrategy("HibernateSearchStrategy");

        sessionFactory = new Configuration().configure(FILE).buildSessionFactory();
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
        sessionFactory.close();
    }

    /**
     * <p>
     * Failure tests for the constructor <code>HibernateSearchStrategy(String namespace)</code> with the namespace
     * is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithNamespaceNull() throws Exception {
        try {
            new HibernateSearchStrategy(null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the constructor <code>HibernateSearchStrategy(String namespace)</code> with the namespace
     * is empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithNamespaceEmpty() throws Exception {
        try {
            new HibernateSearchStrategy("");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the constructor <code>HibernateSearchStrategy(String namespace)</code> with the namespace
     * is trimmed empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithNamespaceTrimmedEmpty() throws Exception {
        try {
            new HibernateSearchStrategy(" \n\t  ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the constructor <code>HibernateSearchStrategy(String namespace)</code> with the namespace
     * is not exist, SearchBuilderConfigurationException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor2WithNamespaceNotExist() throws Exception {
        try {
            new HibernateSearchStrategy("not exists");
            fail("SearchBuilderConfigurationException should be thrown");
        } catch (SearchBuilderConfigurationException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the constructor
     * <code>HibernateSearchStrategy(SessionFactory sessionFactory, ClassAssociator fragmentBuilders)</code> with
     * the sessionFactory is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor3WithSessionFactoryNull() throws Exception {
        try {
            new HibernateSearchStrategy((SessionFactory)null, SearchBuilderHelper.loadClassAssociator("HibernateSearchStrategy"));
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the constructor
     * <code>HibernateSearchStrategy(SessionFactory sessionFactory, ClassAssociator fragmentBuilders)</code> with
     * the fragmentBuilders is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor3WithClassAssociatorNull() throws Exception {
        try {
            new HibernateSearchStrategy(sessionFactory, null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code> with the context is null,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchWithContextNull() throws Exception {
        try {
            strategy.search(null, new EqualToFilter("The age", "23"), new ArrayList(), aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code> with the context is
     * empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchWithContextEmpty() throws Exception {
        try {
            strategy.search("", new EqualToFilter("The age", "2"), new ArrayList(), aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code> with the filter is null,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchWithFilterNull() throws Exception {
        try {
            strategy.search(CONTEXT, null, new ArrayList(), aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code> with the returnFields is
     * null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchWithReturnFieldsNull() throws Exception {
        try {
            strategy.search(CONTEXT, new EqualToFilter("The age", "2"), null, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code> with the returnFields
     * contains null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchWithReturnFieldsContainsNull() throws Exception {
        EqualToFilter filter = new EqualToFilter("The age", "2");
        List returnFields = new ArrayList();
        returnFields.add(null);

        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code> with the returnFields
     * contains empty string, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchWithReturnFieldsContainsEmpty() throws Exception {
        EqualToFilter filter = new EqualToFilter("The age", "2");
        List returnFields = new ArrayList();
        returnFields.add("  ");

        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code> with the aliasMap is
     * null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchWithAliasMapNull() throws Exception {
        try {
            strategy.search(CONTEXT, new EqualToFilter("The age", "2"), new ArrayList(), null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code> with the aliasMap
     * contains null key, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchWithAliasMapContainsNullKey() throws Exception {
        aliasMap.put(null, "a");

        try {
            strategy.search(CONTEXT, new EqualToFilter("The age", "2"), new ArrayList(), aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code> with the aliasMap
     * contains empty key, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchWithAliasMapContainsEmptyKey() throws Exception {
        aliasMap.put("  ", "a");

        try {
            strategy.search(CONTEXT, new EqualToFilter("The age", "2"), new ArrayList(), aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code> with the aliasMap
     * contains the key not a String instance, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchWithAliasMapContainsNotStringKey() throws Exception {
        aliasMap.put(new Object(), "a");

        try {
            strategy.search(CONTEXT, new EqualToFilter("The age", "2"), new ArrayList(), aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code> with the aliasMap
     * contains null value, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchWithAliasMapContainsNullValue() throws Exception {
        aliasMap.put("a", null);

        try {
            strategy.search(CONTEXT, new EqualToFilter("The age", "2"), new ArrayList(), aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code> with the aliasMap
     * contains empty value, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchWithAliasMapContainsEmptyValue() throws Exception {
        aliasMap.put("a", "  ");

        try {
            strategy.search(CONTEXT, new EqualToFilter("The age", "2"), new ArrayList(), aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>search(String context, Filter filter, List returnFields, Map aliasMap)</code> with the aliasMap
     * contains the value not a String instance, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearchWithAliasMapContainsNotStringValue() throws Exception {
        aliasMap.put("a", new Object());

        try {
            strategy.search(CONTEXT, new EqualToFilter("The age", "2"), new ArrayList(), aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>buildSearchContext(String context, Filter filter, List returnFields, Map aliasMap)</code> with the
     * context does not start with "SELECT" or "FROM", IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchContextWithContectIncorrect() throws Exception {
        MockHibernateSearchStrategy hss = new MockHibernateSearchStrategy("HibernateSearchStrategyFail");
        try {
            hss.buildSearchContext("not start with select or from", new BetweenFilter("The age", new Integer(50),
                    new Integer(60)), new ArrayList(), aliasMap);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test failure test of the method {@link HibernateSearchStrategy#buildSearchContext}, Context starts with
     * 'SELECT', but it does not contain a 'FROM'. IllegalArgumentException will be thrown.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchContext_failure3() throws Exception {
        MockHibernateSearchStrategy hss = new MockHibernateSearchStrategy("HibernateSearchStrategyFail");

        try {
            hss.buildSearchContext("select * where", new BetweenFilter("The age", new Integer(50), new Integer(60)),
                    new ArrayList(), aliasMap);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure tests for the method
     * <code>buildSearchContext(String context, Filter filter, List returnFields, Map aliasMap)</code> with the
     * filter is unrecognized, UnrecognizedFilterException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchContextWithUnrecognizedFilter() throws Exception {
        MockHibernateSearchStrategy hss = new MockHibernateSearchStrategy("HibernateSearchStrategyFail");

        try {
            hss.buildSearchContext(CONTEXT, new EqualToFilter("age", new Integer(50)), new ArrayList(), aliasMap);
            fail("UnrecognizedFilterException should be thrown.");
        } catch (UnrecognizedFilterException e) {
            // expected
        }
    }

    /**
     * <p>
     * This mock class extends <code>HibernateSearchStrategy</code> for test.
     * </p>
     *
     * @author TCSDEVELOPER
     * @version 1.4
     */
    private class MockHibernateSearchStrategy extends HibernateSearchStrategy {

        /**
         * Creates a new <code>HibernateSearchStrategy</code> using the default namespace.
         *
         * @throws SearchBuilderConfigurationException
         *             if a configuration property that is required is not found, or if a configuration property does
         *             not make sense.
         */
        public MockHibernateSearchStrategy() throws SearchBuilderConfigurationException {
            super();
        }

        /**
         * Creates the <code>HibernateSearchStrategy</code> from the given namespace.
         *
         * @param namespace
         *            The namespace in configuration from which to create the HibernateSearchStrategy.
         * @throws IllegalArgumentException
         *             If nameSpace is null or empty.
         * @throws SearchBuilderConfigurationException
         *             if a configuration property that is required is not found, or if a configuration property does
         *             not make sense.
         */
        public MockHibernateSearchStrategy(String namespace) throws SearchBuilderConfigurationException {
            super(namespace);
        }

        /**
         * This is a protected method that can be used to build the SearchContext for the search. It may be overridden
         * by subclasses to provide additional or different methods of building up the search context. In this
         * Strategy, the SearchContext is used to hold the state of processing the Filters. The search String is built
         * separately from the binding parameters.
         *
         * @return A SearchContext object that is used to hold the state of building the search.
         * @param context
         *            The search context. This would be an HQL statement, for ex. "from Cat" or "select name, age from
         *            Cat" Note that Cat here is actually a name of OR mapped class. Hibernate is responsible for
         *            finding the underlying persistence table. Similarly name, age are properties of the Cat class.
         *            Hibernate is responsible for finding the underlying persistence columns.
         * @param filter
         *            The filter to use. This is typically used for providing the WHERE clause of the resultant query.
         *            An example could be an Equals filter with name = 'age' and value = 10. Again, age is property of
         *            class Cat, not a database column. Another example could be an Equals filter with name =
         *            'cat.mother.name' and value = 'Momma'. Here mother is a property of Cat class with type as Cat
         *            and we are trying to find all cats such that their mother's name is Momma.
         * @param returnFields
         *            The set of properties to return. This will be used only if it is a SELECT query. In case of a
         *            FROM query, this list is ignored.
         * @param aliasMap
         *            a map of strings, holding the alternate names of properties as keys and their actual property
         *            names as the respective values. Exceptions:
         * @throws UnrecognizedFilterException
         *             Propagated from SearchFragmentBuilder.
         * @throws IllegalArgumentException
         *             If context is invalid HQL string. An HQL is considered invalid if it does not start with
         *             "SELECT" or "FROM". If it starts with "SELECT" it must also contain a "FROM".
         */
        protected SearchContext buildSearchContext(String context, Filter filter, List returnFields, Map aliasMap)
                throws UnrecognizedFilterException {
            return super.buildSearchContext(context, filter, returnFields, aliasMap);
        }
    }
}
