/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.NullFilter;
import com.topcoder.search.builder.filter.OrFilter;

import com.topcoder.util.sql.databaseabstraction.CustomResultSet;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <p>
 * The demo usage of the component.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.4
 */
public class Demo extends TestCase {
    /**
     * The namespace to construct the SearchBundleManager to test.
     */
    private static final String NAMESPACE = "com.topcoder.search.builder";

    /**
     * SearchBundleManager instance for demo.
     */
    private SearchBundleManager manager = null;

    /**
     * setUp.
     *
     * @throws Exception
     *             to junit
     */
    protected void setUp() throws Exception {
        TestHelper.clearConfig();
        TestHelper.addConfig();
        manager = new SearchBundleManager(NAMESPACE);
        TestHelper.addEntries();
    }

    /**
     * tearDown.
     *
     * @throws Exception
     *             to junit
     */
    protected void tearDown() throws Exception {
        TestHelper.clearConfig();

        TestHelper.delEntries();
    }

    /**
     * Demo test with database access with search build.
     *
     * @throws Exception
     *             to junit
     */
    public void testDemoDatabase() throws Exception {
        // get the dbsearchBundle
        SearchBundle dbsearchBundle = manager.getSearchBundle(
                "FirstSearchBundle");

        // get the people who's age = 10
        EqualToFilter equalToFilter = new EqualToFilter("The age",
                new Integer(10));

        // search depend the filter
        CustomResultSet result = (CustomResultSet) dbsearchBundle.search(equalToFilter);
        List list = new ArrayList();

        // get the result
        while (result.next()) {
            list.add(result.getString("peopleName"));
        }

        // makeup values
        List values = new ArrayList();
        values.add(new Integer(3));
        values.add(new Integer(5));

        // construct infilter
        InFilter infilter = new InFilter("The age", values);
        result = (CustomResultSet) dbsearchBundle.search(infilter);

        int siz = 0;

        while (result.next()) {
            siz++;
        }

        NotFilter notFilter = new NotFilter(infilter);
        AndFilter andFilter = new AndFilter(infilter, notFilter);
        result = (CustomResultSet) dbsearchBundle.search(andFilter);
        siz = 0;

        while (result.next()) {
            siz++;
        }

        list = new ArrayList();
        list.add(new Integer(60));
        list.add(new Integer(60));

        list.clear();

        while (result.next()) {
            list.add(result.getString("peoplename"));
        }

        // return with fields
        // make up fields list
        list = new ArrayList();
        list.add("peoplename");

        result = (CustomResultSet) dbsearchBundle.search(new EqualToFilter(
                    "The age", new Integer(10)), list);
        result.next();
        assertNotNull("The field 'peoplename' should be return",
            result.getString("peoplename"));
        result = (CustomResultSet) dbsearchBundle.search(new NullFilter(
                    "The age"), list);
    }

    /**
     * Demo test with ldap access with search build. Note:This test is on the search scope =
     * 2.
     *
     * @throws Exception
     *             to junit
     */
    public void testDemoLDAP() throws Exception {
        // get the dbsearchBundle
        SearchBundle ldapsearchBundle = manager.getSearchBundle(
                "SecondSearchBundle");

        EqualToFilter equalToFilter1 = new EqualToFilter("sn", "5");
        EqualToFilter equalToFilter2 = new EqualToFilter("sn", "3");
        EqualToFilter equalToFilter3 = new EqualToFilter("sn", "0");

        // get the prople who sn = "5"
        Iterator it = (Iterator) ldapsearchBundle.search(equalToFilter1);

        AndFilter andFilter = new AndFilter(equalToFilter1, equalToFilter2);
        it = (Iterator) ldapsearchBundle.search(andFilter);

        // no peoples with name = "1" & "3" & "5"
        assertEquals("There are 0 people under the condition setted", 0,
            TestHelper.getItemNumber(it));

        OrFilter orFilter = new OrFilter(equalToFilter1, equalToFilter2);
        orFilter.addFilter(equalToFilter3);
        it = (Iterator) ldapsearchBundle.search(orFilter);

        // 2 peoples with name = "0" | "3" | "5"
        NotFilter notFilter = new NotFilter(equalToFilter1);
        it = (Iterator) ldapsearchBundle.search(notFilter);

        // 8 items with name != "5"
        assertEquals("There are 8 people under the condition setted", 8,
            TestHelper.getItemNumber(it));

        List values = new ArrayList();
        values.add("3");
        values.add("4");

        BetweenFilter bwtweenFilter = new BetweenFilter("sn", "6", "7");
        it = (Iterator) ldapsearchBundle.search(bwtweenFilter);

        InFilter inFilter = new InFilter("sn", values);
        it = (Iterator) ldapsearchBundle.search(inFilter);

        // 2 peoples with name in ("2", "3")
        assertEquals("There are 2 people under the condition setted", 2,
            TestHelper.getItemNumber(it));

        GreaterThanFilter greaterThanFilter = new GreaterThanFilter("sb", "sb1");
        it = (Iterator) ldapsearchBundle.search(greaterThanFilter);

        // 3 roles with the name greater and "sb1"
        LessThanFilter lessThanFilter = new LessThanFilter("sb", "sb1");
        it = (Iterator) ldapsearchBundle.search(lessThanFilter);

        // no role with the name less and "sb1"
    }

    /**
     * Demo For version 1.4 (When using a 'From' HQL query).
     *
     * @throws Exception
     *             to JUnit
     */
    public void testDemo14_FROMHQL() throws Exception {
        // get the dbsearchBundle
        SearchBundle dbsearchBundle = manager.getSearchBundle(
                "HibernateSearchBundleFromHQL");

        // Look for people whose age = 23
        EqualToFilter equalToFilter = new EqualToFilter("The age",
                new Integer(23));

        // search using equals the filter. Result will have 2 results: Petr and Anita
        List result = (List) dbsearchBundle.search(equalToFilter);
        // Look for people working at Google and Microsoft
        assertEquals(result.size(), 2);

        List values = new ArrayList();
        values.add("Google");
        values.add("Microsoft");

        // construct infilter
        InFilter infilter = new InFilter("The company name", values);
        // Again 2 results will be returned. Petr and Codedoc
        result = (List) dbsearchBundle.search(infilter);

        // Look for people not working at Yahoo
        EqualToFilter equalToFilter2 = new EqualToFilter("The company name",
                "Yahoo");
        NotFilter notFilter = new NotFilter(equalToFilter2);
        // Will return 2 people: Petr and Codedoc
        result = (List) dbsearchBundle.search(notFilter);
        assertEquals(result.size(), 2);

        // Similarly a lot of other filters can be applied.
    }

    /**
     * Demo For version 1.4 (When using a 'Select' HQL query).
     *
     * @throws Exception
     *             to JUnit
     */
    public void testDemo14_SELECTHQL() throws Exception {
        // get the dbsearchBundle
        SearchBundle dbsearchBundle = manager.getSearchBundle(
                "HibernateSearchBundleSELECTHQL");

        // Look for people whose age = 23
        EqualToFilter equalToFilter = new EqualToFilter("The age",
                new Integer(23));

        // search using equals the filter.
        List result = (List) dbsearchBundle.search(equalToFilter);
        Object[] objs = (Object[]) result.get(0);
        String name = (String) objs[0];
        String companyName = (String) objs[1];
        assertEquals("The name is incorrect.", "Petr", name);
        assertEquals("The companyName is incorrect.", "Google", companyName);

        objs = (Object[]) result.get(1);
        name = (String) objs[0];
        companyName = (String) objs[1];
        assertEquals("The name is incorrect.", "Anita", name);
        assertEquals("The companyName is incorrect.", "Yahoo", companyName);
    }
}
