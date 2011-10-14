/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ibm.support.electronic.cache.model.CacheStatistics;


/**
 * <p>
 * Unit test case of {@link CacheStatisticsPrinterImpl}.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CacheStatisticsPrinterImplTest {
    /**
     * <p>
     * The CacheStatisticsPrinterImpl instance to test against.
     * </p>
     */
    private CacheStatisticsPrinterImpl printer;

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CacheStatisticsPrinterImplTest.class);
    }

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception to jUnit.
     */
    @Before
    public void setUp() throws Exception {
        printer = new CacheStatisticsPrinterImpl();
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception to jUnit.
     */
    @After
    public void tearDown() throws Exception {
        printer = null;
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatisticsPrinterImpl#CacheStatisticsPrinterImpl()}.
     * </p>
     */
    @Test
    public void testCtor() {
        Assert.assertNotNull("Unable to instantiate CacheStatisticsPrinterImpl", printer);
    }

    /**
     * <p>
     * Accuracy test method for <code>CacheStatisticsPrinterImpl#printStatistics(
     * com.ibm.support.electronic.cache.model.CacheStatistics, java.io.PrintWriter)</code> when there is access
     * count for key . It verifies the cache statistics is printed correctly.
     * </p>
     */
    @Test
    public void testPrintStatistics1() {
        // create the writer
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        // create the cache statistics
        CacheStatistics stats = new CacheStatistics();

        Map<String, Integer> accessCountsById = new HashMap<String, Integer>();
        accessCountsById.put("key", 2);
        stats.setAccessCountsById(accessCountsById);
        stats.setInMemoryItemCount(1);
        stats.setMissCount(1);
        stats.setPersistedItemCount(1);

        // print the statistics
        printer.printStatistics(stats, pw);

        // verify the result
        BufferedReader reader = new BufferedReader(new StringReader(sw.toString()));
        try {
            Assert.assertEquals("Incorrect total item line", "Number of items in the cache: 2", reader.readLine());
            Assert.assertEquals("Incorrect keys line", "Keys in the cache:", reader.readLine());
            Assert.assertEquals("Incorrect key access line", "Key key is accessed 2 times.", reader.readLine());
            Assert.assertEquals("Incorrect memory count line", "Number of items in memory: 1", reader.readLine());
            Assert.assertEquals("Incorrect persisted count line", "Number of items in persistent storage: 1",
                reader.readLine());
            Assert.assertEquals("Incorrect miss count line", "Number of misses: 1", reader.readLine());
            Assert.assertNull("Should be the end of printed stats", reader.readLine());
        } catch (IOException e) {
            // never happen since we read from string
        }
    }

    /**
     * <p>
     * Accuracy test method for <code>CacheStatisticsPrinterImpl#printStatistics(
     * com.ibm.support.electronic.cache.model.CacheStatistics, java.io.PrintWriter)</code> when there is no access
     * count for key . It verifies the cache statistics is printed correctly.
     * </p>
     */
    @Test
    public void testPrintStatistics2() {
        // create the writer
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        // create the cache statistics
        CacheStatistics stats = new CacheStatistics();

        Map<String, Integer> accessCountsById = new HashMap<String, Integer>();
        stats.setAccessCountsById(accessCountsById);
        stats.setInMemoryItemCount(1);
        stats.setMissCount(1);
        stats.setPersistedItemCount(1);

        // print the statistics
        printer.printStatistics(stats, pw);

        // verify the result
        BufferedReader reader = new BufferedReader(new StringReader(sw.toString()));
        try {
            Assert.assertEquals("Incorrect total item line", "Number of items in the cache: 2", reader.readLine());
            Assert.assertEquals("Incorrect memory count line", "Number of items in memory: 1", reader.readLine());
            Assert.assertEquals("Incorrect persisted count line", "Number of items in persistent storage: 1",
                reader.readLine());
            Assert.assertEquals("Incorrect miss count line", "Number of misses: 1", reader.readLine());
            Assert.assertNull("Should be the end of printed stats", reader.readLine());
        } catch (IOException e) {
            // never happen since we read from string
        }
    }

    /**
     * <p>
     * Failure test method for <code>CacheStatisticsPrinterImpl#printStatistics(
     * com.ibm.support.electronic.cache.model.CacheStatistics, java.io.PrintWriter)</code> when cacheStatistics is
     * null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPrintStatistics_NullCacheStatistics() {
        printer.printStatistics(null, new PrintWriter(new StringWriter()));
    }

    /**
     * <p>
     * Failure test method for <code>CacheStatisticsPrinterImpl#printStatistics(
     * com.ibm.support.electronic.cache.model.CacheStatistics, java.io.PrintWriter)</code> when printWriter is
     * null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPrintStatistics_NullPrintWriter() {
        CacheStatistics stats = new CacheStatistics();
        Map<String, Integer> accessCountsById = new HashMap<String, Integer>();
        stats.setAccessCountsById(accessCountsById);
        printer.printStatistics(stats, null);
    }

    /**
     * <p>
     * Failure test method for <code>CacheStatisticsPrinterImpl#printStatistics(
     * com.ibm.support.electronic.cache.model.CacheStatistics, java.io.PrintWriter)</code> when accessCountsById of
     * cacheStatistics is null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPrintStatistics_NullAccessCounts() {
        printer.printStatistics(new CacheStatistics(), new PrintWriter(new StringWriter()));
    }

    /**
     * <p>
     * Failure test method for <code>CacheStatisticsPrinterImpl#printStatistics(
     * com.ibm.support.electronic.cache.model.CacheStatistics, java.io.PrintWriter)</code> when accessCountsById
     * contains null key.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPrintStatistics_NullKey() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put(null, 1);
        CacheStatistics stats = new CacheStatistics();
        stats.setAccessCountsById(map);
        printer.printStatistics(stats, new PrintWriter(new StringWriter()));
    }

    /**
     * <p>
     * Failure test method for <code>CacheStatisticsPrinterImpl#printStatistics(
     * com.ibm.support.electronic.cache.model.CacheStatistics, java.io.PrintWriter)</code> when accessCountsById
     * contains empty key.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPrintStatistics_EmptyKey() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("  ", 1);
        CacheStatistics stats = new CacheStatistics();
        stats.setAccessCountsById(map);
        printer.printStatistics(stats, new PrintWriter(new StringWriter()));
    }

    /**
     * <p>
     * Failure test method for <code>CacheStatisticsPrinterImpl#printStatistics(
     * com.ibm.support.electronic.cache.model.CacheStatistics, java.io.PrintWriter)</code> when accessCountsById
     * contains null value.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPrintStatistics_NullValue() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("key", null);
        CacheStatistics stats = new CacheStatistics();
        stats.setAccessCountsById(map);
        printer.printStatistics(stats, new PrintWriter(new StringWriter()));
    }
}
