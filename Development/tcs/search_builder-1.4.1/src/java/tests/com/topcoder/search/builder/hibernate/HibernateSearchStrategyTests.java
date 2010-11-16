/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate;

import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.TestHelper;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;
import com.topcoder.search.builder.filter.LikeFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.OrFilter;

import com.topcoder.util.classassociations.ClassAssociator;

import junit.framework.TestCase;

import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Unit test cases for <code>HibernateSearchStrategy</code>.
 * </p>
 *
 * @author myxgyy
 * @version 1.4
 */
public class HibernateSearchStrategyTests extends TestCase {
    /**
     * The hibernate configuration file..
     */
    private static final String FILE = "sampleHibernateConfig.cfg.xml";

    /**
     * The context.
     */
    private static final String CONTEXT_ONE = "FROM Person as p join p.company as c WHERE";

    /**
     * The context.
     */
    private static final String CONTEXT_TWO = "SELECT p.name, c.name FROM Person as p join p.company as c WHERE";

    /**
     * The namespace for DB.
     */
    private static final String NS = "HibernateSearchStrategy";
    
    /**
     * The jndi name for session factory.
     */
    private static final String SESSION_FACTORY_JNDI_NAME = "sessionFactoryJndiName";
    /**
     * The HibernateSearchStrategy instance to test.
     */
    private HibernateSearchStrategy strategy = null;

    /**
     * The returnFields.
     */
    private List returnFields = null;

    /**
     * The map of alias name and real name.
     */
    private Map aliasMap = null;

    /**
     * The ClassAssociator instance used to test.
     */
    private ClassAssociator fragmentBuilders;

    /**
     * The SessionFactory instance used to test.
     */
    private SessionFactory sessionFactory;

    /**
     * The setUp of the unit test.
     *
     * @throws Exception
     *             to Junit
     */
    protected void setUp() throws Exception {
        // add the configuration
        TestHelper.clearConfig();
        TestHelper.addConfig();

        aliasMap = new HashMap();
        aliasMap.put("The age", "p.age");
        aliasMap.put("The company name", "c.name");

        returnFields = new ArrayList();
        returnFields.add("p.age");

        strategy = new HibernateSearchStrategy(NS);

        fragmentBuilders = SearchBuilderHelper.loadClassAssociator(
                "HibernateSearchStrategy");
        sessionFactory = new Configuration().configure(FILE)
                                            .buildSessionFactory();
    }

    /**
     * The tearDown of the unit test.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        // remove the configuration in tearDown
        TestHelper.clearConfig();
        sessionFactory.close();
    }

    /**
     * The accuracy test of the constructor <code>HibernateSearchStrategy()</code>.
     */
    public void testConstructorAccuracy1() throws Exception {
        assertNotNull("can not construct the HibernateSearchStrategy.",
            new HibernateSearchStrategy());
    }

    /**
     * The accuracy test of the constructor <code>HibernateSearchStrategy(String)</code>.
     */
    public void testconstructor_accuracy2() {
        assertNotNull("can not construct the HibernateSearchStrategy.", strategy);
    }

    /**
     * The accuracy test of the constructor
     * <code>HibernateSearchStrategy(SessionFactory, ClassAssociator)</code>.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testconstructor_accuracy3() throws Exception {
        assertNotNull("can not construct the HibernateSearchStrategy.",
            new HibernateSearchStrategy(sessionFactory, fragmentBuilders));
    }
    
    /**
     * The accuracy test of the constructor
     * <code>HibernateSearchStrategy(String, ClassAssociator)</code>.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testconstructor_accuracy4() throws Exception {
        assertNotNull("can not construct the HibernateSearchStrategy.",
            new HibernateSearchStrategy(SESSION_FACTORY_JNDI_NAME, fragmentBuilders));
    }

    /**
     * The failure test of the constructor <code>HibernateSearchStrategy()</code>.
     * SearchBuilderConfigurationException should be thrown for the namespace has
     * been deleted.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testconstructor_failure1() throws Exception {
        // first we reomve the namespace
        TestHelper.clearConfig();

        try {
            new HibernateSearchStrategy();
            fail("SearchBuilderConfigurationException should be thrown");
        } catch (SearchBuilderConfigurationException e) {
            // pass
        }
    }

    /**
     * The failure test of the constructor <code>HibernateSearchStrategy(String)</code>.
     * IllegalArgumentException should be thrown for the null String.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testconstructor_failure2() throws Exception {
        try {
            new HibernateSearchStrategy(null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of the constructor <code>HibernateSearchStrategy(String)</code>.
     * IllegalArgumentException should be thrown for the empty argument.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testconstructor_failure3() throws Exception {
        try {
            new HibernateSearchStrategy("");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of the constructor <code>HibernateSearchStrategy(String)</code>.
     * IllegalArgumentException should be thrown for the empty(trimmed) argument.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testconstructor_failure4() throws Exception {
        try {
            new HibernateSearchStrategy(" \n\t  ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the constructor <code>HibernateSearchStrategy(String)</code>.
     * </p>
     * <p>
     * namespace unknown, SearchBuilderConfigurationException should be thrown.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testconstructor_failure5() throws Exception {
        try {
            new HibernateSearchStrategy("not exists");
            fail("SearchBuilderConfigurationException should be thrown");
        } catch (SearchBuilderConfigurationException e) {
            // pass
        }
    }

    /**
     * The failure test of the constructor
     * <code>HibernateSearchStrategy(SessionFactory, ClassAssociator)</code>. Null
     * sessionFactory, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testconstructor_failure6() throws Exception {
        try {
            new HibernateSearchStrategy((SessionFactory)null, fragmentBuilders);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of the constructor
     * <code>HibernateSearchStrategy(SessionFactory, ClassAssociator)</code>. Null
     * fragmentBuilders, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testconstructor_failure7() throws Exception {
        try {
            new HibernateSearchStrategy(sessionFactory, null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    /**
     * The failure test of the constructor
     * <code>HibernateSearchStrategy(String, ClassAssociator)</code>. Null
     * sessionFactoryJndiName, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testconstructor_failure8() throws Exception {
        try {
            new HibernateSearchStrategy((String)null, fragmentBuilders);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of the constructor
     * <code>HibernateSearchStrategy(String, ClassAssociator)</code>. Null
     * fragmentBuilders, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testconstructor_failure9() throws Exception {
        try {
            new HibernateSearchStrategy(SESSION_FACTORY_JNDI_NAME, null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    /**
     * The failure test of the constructor
     * <code>HibernateSearchStrategy(String, ClassAssociator)</code>. No sessionFactory could be found.
     * SearchBuilderConfigurationException should be thrown.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testconstructor_failure10() throws Exception {
        try {
            new HibernateSearchStrategy("null", fragmentBuilders);
            fail("SearchBuilderConfigurationException should be thrown");
        } catch (SearchBuilderConfigurationException e) {
            // pass
        }
    }
    
    /**
     * The failure test of the constructor
     * <code>HibernateSearchStrategy(String, ClassAssociator)</code>. The object under context is not type of SessionFactory.
     * SearchBuilderConfigurationException should be thrown.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testconstructor_failure11() throws Exception {
        try {
            new HibernateSearchStrategy("object", fragmentBuilders);
            fail("SearchBuilderConfigurationException should be thrown");
        } catch (SearchBuilderConfigurationException e) {
            // pass
        }
    }
    
    /**
     * The failure test of the constructor
     * <code>HibernateSearchStrategy(String, ClassAssociator)</code>. A NamingException occurred.
     * SearchBuilderConfigurationException should be thrown.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testconstructor_failure12() throws Exception {
        try {
            new HibernateSearchStrategy("throwNamingException", fragmentBuilders);
            fail("SearchBuilderConfigurationException should be thrown");
        } catch (SearchBuilderConfigurationException e) {
            // pass
        }
    }

    /**
     * Search from the table person who's age = 23 with context one.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy1() throws Exception {
        EqualToFilter equalToFilter = new EqualToFilter("The age",
                new Integer(23));

        // search depend the filter
        List result = (List) strategy.search(CONTEXT_ONE, equalToFilter,
                new ArrayList(), aliasMap);

        assertEquals("There are 2 people with age = 23", 2, result.size());
    }

    /**
     * Search from the table who's age = 23 with context two.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy2() throws Exception {
        EqualToFilter equalToFilter = new EqualToFilter("The age",
                new Integer(23));

        // search depend the filter
        List result = (List) strategy.search(CONTEXT_TWO, equalToFilter,
                new ArrayList(), aliasMap);

        assertEquals("There are 2 people with age = 23", 2, result.size());
    }

    /**
     * Search from the table who's age = 23 and sex = Female with context one.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy3() throws Exception {
        EqualToFilter equalToFilter1 = new EqualToFilter("The age",
                new Integer(23));
        EqualToFilter equalToFilter2 = new EqualToFilter("p.sex", "Female");
        AndFilter andFilter = new AndFilter(equalToFilter1, equalToFilter2);

        // search depend the filter
        List result = (List) strategy.search(CONTEXT_ONE, andFilter,
                new ArrayList(), aliasMap);

        assertEquals("There are 1 people with age = 23 and sex = female", 1,
            result.size());
    }

    /**
     * Search from the table who's age = 23 and sex = Female with context two.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy4() throws Exception {
        EqualToFilter equalToFilter1 = new EqualToFilter("The age",
                new Integer(23));
        EqualToFilter equalToFilter2 = new EqualToFilter("p.sex", "Female");
        AndFilter andFilter = new AndFilter(equalToFilter1, equalToFilter2);

        // search depend the filter
        List result = (List) strategy.search(CONTEXT_TWO, andFilter,
                new ArrayList(), aliasMap);

        assertEquals("There are 1 people with age = 23 and sex = female", 1,
            result.size());
    }

    /**
     * Search from the table who's age = 23 or sex = Male with context one.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy5() throws Exception {
        EqualToFilter equalToFilter1 = new EqualToFilter("The age",
                new Integer(23));
        EqualToFilter equalToFilter2 = new EqualToFilter("p.sex", "Male");
        OrFilter orFilter = new OrFilter(equalToFilter1, equalToFilter2);

        // search depend the filter
        List result = (List) strategy.search(CONTEXT_ONE, orFilter,
                new ArrayList(), aliasMap);

        assertEquals("There are 3 people with age = 23 or sex = Male", 3,
            result.size());
    }

    /**
     * Search from the table who's age = 23 or sex = Male with context two.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy6() throws Exception {
        EqualToFilter equalToFilter1 = new EqualToFilter("The age",
                new Integer(23));
        EqualToFilter equalToFilter2 = new EqualToFilter("p.sex", "Male");
        OrFilter orFilter = new OrFilter(equalToFilter1, equalToFilter2);

        // search depend the filter
        List result = (List) strategy.search(CONTEXT_TWO, orFilter,
                new ArrayList(), aliasMap);

        assertEquals("There are 3 people with age = 23 or sex = Male", 3,
            result.size());
    }

    /**
     * Search from the table who's !(age = 23 or sex = Male) with context one.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy7() throws Exception {
        EqualToFilter equalToFilter1 = new EqualToFilter("The age",
                new Integer(23));
        EqualToFilter equalToFilter2 = new EqualToFilter("p.sex", "Male");
        OrFilter orFilter = new OrFilter(equalToFilter1, equalToFilter2);
        NotFilter filter = new NotFilter(orFilter);

        // search depend the filter
        List result = (List) strategy.search(CONTEXT_ONE, filter,
                new ArrayList(), aliasMap);

        assertEquals("There are 0 people with !(age = 23 or sex = Male)", 0,
            result.size());
    }

    /**
     * Search from the table who's !(age = 23 or sex = Male) with context two.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy8() throws Exception {
        EqualToFilter equalToFilter1 = new EqualToFilter("The age",
                new Integer(23));
        EqualToFilter equalToFilter2 = new EqualToFilter("p.sex", "Male");
        OrFilter orFilter = new OrFilter(equalToFilter1, equalToFilter2);
        NotFilter filter = new NotFilter(orFilter);

        // search depend the filter
        List result = (List) strategy.search(CONTEXT_TWO, filter,
                new ArrayList(), aliasMap);

        assertEquals("There are 0 people with !(age = 23 or sex = Male)", 0,
            result.size());
    }

    /**
     * search from the table person who's name like P%.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy9() throws Exception {
        LikeFilter filter = new LikeFilter("p.name", "SW:P", '!');

        List list = (List) strategy.search(CONTEXT_TWO, filter,
                new ArrayList(), aliasMap);

        assertEquals("There are 1 person with name like P%", 1, list.size());
    }

    /**
     * search from the table person who's age between 50 and 60 with context one.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy10() throws Exception {
        BetweenFilter filter = new BetweenFilter("p.age", new Integer(60),
                new Integer(50));

        List list = new ArrayList();
        list.add("sex");

        // search depend the filter
        List result = (List) strategy.search(CONTEXT_ONE, filter, list, aliasMap);

        assertEquals("There are 1 person who's age between 50 and 60", 1,
            result.size());
    }

    /**
     * search from the table person who's age between 50 and 60 with context two.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy11() throws Exception {
        BetweenFilter filter = new BetweenFilter("The age", new Integer(60),
                new Integer(50));

        // search depend the filter
        List result = (List) strategy.search(CONTEXT_TWO, filter,
                new ArrayList(), aliasMap);

        assertEquals("There are 1 person who's age between 50 and 60", 1,
            result.size());
    }

    /**
     * search from the table person who's name in 'Petr' or 'CodeDoc' with context one.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy12() throws Exception {
        List values = new ArrayList();
        values.add("Petr");
        values.add("CodeDoc");

        InFilter filter = new InFilter("p.name", values);

        List list = (List) strategy.search(CONTEXT_ONE, filter,
                new ArrayList(), aliasMap);

        assertEquals("There are 2 persons who's name in 'Petr' or 'CodeDoc", 2,
            list.size());
    }

    /**
     * search from the table person who's name in 'Petr' or 'CodeDoc' with context two.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy13() throws Exception {
        List values = new ArrayList();
        values.add("Petr");
        values.add("CodeDoc");

        InFilter filter = new InFilter("p.name", values);

        List list = (List) strategy.search(CONTEXT_TWO, filter,
                new ArrayList(), aliasMap);

        assertEquals("There are 2 persons who's name in 'Petr' or 'CodeDoc", 2,
            list.size());
    }

    /**
     * search from the table person who's age <= 53.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy14() throws Exception {
        LessThanOrEqualToFilter lessThanFilter = new LessThanOrEqualToFilter("The age",
                new Integer(53));

        // search depend the filter
        List list = (List) strategy.search(CONTEXT_ONE, lessThanFilter,
                new ArrayList(), aliasMap);

        assertEquals("There are 3 persons who's age <= 53", 3, list.size());
    }

    /**
     * search from the table person who's age < 53.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy15() throws Exception {
        LessThanFilter lessThanFilter = new LessThanFilter("The age",
                new Integer(53));

        // search depend the filter
        List list = (List) strategy.search(CONTEXT_ONE, lessThanFilter,
                new ArrayList(), aliasMap);

        assertEquals("There are 2 persons who's age < 53", 2, list.size());
    }

    /**
     * search from the table person who's age >= 53.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy16() throws Exception {
        GreaterThanOrEqualToFilter filter = new GreaterThanOrEqualToFilter("The age",
                new Integer(53));

        // search depend the filter
        List list = (List) strategy.search(CONTEXT_ONE, filter,
                new ArrayList(), aliasMap);

        assertEquals("There are 1 person who's age >= 53", 1, list.size());
    }

    /**
     * search from the table person who's age > 53.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy17() throws Exception {
        GreaterThanFilter filter = new GreaterThanFilter("The age",
                new Integer(53));

        // search depend the filter
        List list = (List) strategy.search(CONTEXT_ONE, filter,
                new ArrayList(), aliasMap);

        assertEquals("There are 0 person who's age > 53", 0, list.size());
    }

    /**
     * search all from the table person who's age > 53.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_accuracy18() throws Exception {
        GreaterThanFilter filter = new GreaterThanFilter("The age",
                new Integer(53));

        List fields = new ArrayList();
        fields.add("p.sex");
        fields.add("p.id");

        // search depend the filter
        List list = (List) strategy.search(CONTEXT_TWO, filter, fields, aliasMap);

        assertEquals("There are 0 person who's age > 53", 0, list.size());
    }

    /**
     * The failure test of search for the null context,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_failure1() throws Exception {
        EqualToFilter filter = new EqualToFilter("The age", "23");

        try {
            strategy.search(null, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of search for the empty context,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_failure2() throws Exception {
        EqualToFilter filter = new EqualToFilter("The age", "2");

        try {
            strategy.search("", filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of search for the null filter,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_failure3() throws Exception {
        try {
            strategy.search(CONTEXT_ONE, null, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of search for the null returnFields,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_failure4() throws Exception {
        EqualToFilter filter = new EqualToFilter("The age", "2");

        try {
            strategy.search(CONTEXT_ONE, filter, null, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of search for returnFields contains null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_failure5() throws Exception {
        EqualToFilter filter = new EqualToFilter("The age", "2");
        returnFields.add(null);

        try {
            strategy.search(CONTEXT_ONE, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of search for returnFields contains empty String,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_failure6() throws Exception {
        EqualToFilter filter = new EqualToFilter("The age", "2");
        returnFields.add("  ");

        try {
            strategy.search(CONTEXT_ONE, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of search for null aliasMap,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_failure7() throws Exception {
        EqualToFilter filter = new EqualToFilter("The age", "2");

        try {
            strategy.search(CONTEXT_ONE, filter, returnFields, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of search for aliasMap contains null key,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_failure8() throws Exception {
        EqualToFilter filter = new EqualToFilter("The age", "2");
        aliasMap.put(null, "a");

        try {
            strategy.search(CONTEXT_ONE, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of search for aliasMap contains empty key,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_failure9() throws Exception {
        EqualToFilter filter = new EqualToFilter("The age", "2");
        aliasMap.put("  ", "a");

        try {
            strategy.search(CONTEXT_ONE, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of search for aliasMap contains invalid key,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_failure10() throws Exception {
        EqualToFilter filter = new EqualToFilter("The age", "2");
        aliasMap.put(new Object(), "a");

        try {
            strategy.search(CONTEXT_ONE, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of search for aliasMap contains null value,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_failure11() throws Exception {
        EqualToFilter filter = new EqualToFilter("The age", "2");
        aliasMap.put("a", null);

        try {
            strategy.search(CONTEXT_ONE, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of search for aliasMap contains empty value,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_failure12() throws Exception {
        EqualToFilter filter = new EqualToFilter("The age", "2");
        aliasMap.put("a", "  ");

        try {
            strategy.search(CONTEXT_ONE, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * The failure test of search for aliasMap contains invalid value,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to Junit
     */
    public void testSearch_failure13() throws Exception {
        EqualToFilter filter = new EqualToFilter("The age", "2");
        aliasMap.put("a", new Object());

        try {
            strategy.search(CONTEXT_ONE, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Accuracy test case for the method {@link HibernateSearchStrategy#buildSearchContext}.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchContextAccuracy1() throws Exception {
        SearchContext searchContext = strategy.buildSearchContext(CONTEXT_ONE,
                new EqualToFilter("age", new Integer(10)), new ArrayList(),
                aliasMap);

        assertEquals("The searchString should be same.",
            "FROM Person as p join p.company as c WHERE age = ?",
            searchContext.getSearchString().toString());
    }

    /**
     * Accuracy test case for the method {@link HibernateSearchStrategy#buildSearchContext}.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchContextAccuracy2() throws Exception {
        EqualToFilter equalToFilter1 = new EqualToFilter("The age",
                new Integer(23));
        EqualToFilter equalToFilter2 = new EqualToFilter("p.sex", "Female");
        AndFilter andFilter = new AndFilter(equalToFilter1, equalToFilter2);

        SearchContext searchContext = strategy.buildSearchContext(CONTEXT_ONE,
                andFilter, new ArrayList(), aliasMap);

        assertEquals("The searchString should be same.",
            "FROM Person as p join p.company as c WHERE (p.age = ? AND p.sex = ?)",
            searchContext.getSearchString().toString());
    }

    /**
     * Accuracy test case for the method {@link HibernateSearchStrategy#buildSearchContext}.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchContextAccuracy3() throws Exception {
        BetweenFilter filter = new BetweenFilter("The age", new Integer(50),
                new Integer(60));

        SearchContext searchContext = strategy.buildSearchContext(CONTEXT_ONE,
                filter, new ArrayList(), aliasMap);

        assertEquals("The searchString should be same.",
            "FROM Person as p join p.company as c WHERE p.age BETWEEN ? AND ?",
            searchContext.getSearchString().toString());
    }

    /**
     * Test failure test of the method {@link HibernateSearchStrategy#buildSearchContext},
     * UnrecognizedFilterException will be thrown.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchContext_failure1() throws Exception {
        HibernateSearchStrategy hss = new HibernateSearchStrategy(
                "HibernateSearchStrategyFail");

        try {
            hss.buildSearchContext(CONTEXT_ONE,
                new EqualToFilter("The age", new Integer(50)), new ArrayList(),
                aliasMap);
            fail("Should have thrown UnrecognizedFilterException");
        } catch (UnrecognizedFilterException e) {
            // pass
        }
    }

    /**
     * Test failure test of the method {@link HibernateSearchStrategy#buildSearchContext},
     * Context does not start with 'SELECT' or 'FROM'. IllegalArgumentException will be
     * thrown.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchContext_failure2() throws Exception {
        try {
            this.strategy.buildSearchContext("not start with select or from",
                new BetweenFilter("The age", new Integer(50), new Integer(60)),
                new ArrayList(), aliasMap);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test failure test of the method {@link HibernateSearchStrategy#buildSearchContext},
     * Context starts with 'SELECT', but it does not contain a 'FROM'.
     * IllegalArgumentException will be thrown.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testBuildSearchContext_failure3() throws Exception {
        try {
            this.strategy.buildSearchContext("select * where",
                new BetweenFilter("The age", new Integer(50), new Integer(60)),
                new ArrayList(), aliasMap);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Accuracy test cases for <code>HibernateSearchStrategy#getSessionFactory</code>.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetSessionFactoryAccuracy() throws Exception {
        assertNotNull("session factory should be same",
            strategy.getSessionFactory());
    }

    /**
     * Accuracy test cases for <code>HibernateSearchStrategy#finalize</code>.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testFinalizeAccuracy() throws Exception {
        this.strategy.finalize();
    }
}
