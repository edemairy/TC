/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.stresstests;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.ibm.support.electronic.cache.model.CacheStatistics;
import com.ibm.support.electronic.cache.printer.CacheStatisticsPrinterImpl;


/**
 * <p>
 * Stress test case of {@link CacheStatisticsPrinterImpl}.
 * </p>
 *
 * @author mumujava
 * @version 1.0
 */
public class CacheStatisticsPrinterImplStressTest extends BaseStressTest {
    /**
     * <p>
     * The CacheStatisticsPrinterImpl instance to test.
     * </p>
     */
    private CacheStatisticsPrinterImpl instance;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception to jUnit.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        instance = new CacheStatisticsPrinterImpl();

    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception to jUnit.
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        instance = null;
    }

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(CacheStatisticsPrinterImplStressTest.class);
        return suite;
    }

    /**
     * <p>
     * Stress test for method CacheStatisticsPrinterImpl#printStatistics().
     * </p>
     *
     * @throws Throwable to junit
     */
    public void test_printStatistics() throws Throwable {
        Thread thread[] = new Thread[testCount];
        final CacheStatistics cacheStatistics = new CacheStatistics();
        cacheStatistics.setInMemoryItemCount(10);
        cacheStatistics.setMissCount(20);
        cacheStatistics.setPersistedItemCount(5);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        cacheStatistics.setAccessCountsById(map);
        for (int i = 0; i < testCount; i++) {
            thread[i] = new Thread() {
                public void run() {
                    StringWriter sw = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(sw);
                    instance.printStatistics(cacheStatistics, printWriter);
                    String res = sw.toString();
                    assertTrue(res.contains("Number of items in the cache: 15"));
                    assertTrue(res.contains("Keys in the cache:"));
                    assertTrue(res.contains("Key A is accessed 1 times."));
                    assertTrue(res.contains("Key C is accessed 3 times."));
                    assertTrue(res.contains("Key B is accessed 2 times."));
                    assertTrue(res.contains("Number of items in memory: 10"));
                    assertTrue(res.contains("Number of items in persistent storage: 5"));
                }
            };
            thread[i].start();
        }
        for (int i = 0; i < testCount; i++) {
            // wait to end
            thread[i].join();
        }
        if (lastError != null) {
            throw lastError;
        }

        System.out.println("Run printStatistics() for  " + testCount + " times takes "
            + (new Date().getTime() - start) + "ms");
    }
}
