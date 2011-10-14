/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.model;

import java.util.HashMap;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * <p>
 * Unit test case of {@link CacheStatistics}.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CacheStatisticsTest {
    /**
     * <p>
     * The CacheStatistics instance to test against.
     * </p>
     */
    private CacheStatistics cacheStatistics;

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CacheStatisticsTest.class);
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
        cacheStatistics = new CacheStatistics();
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
        cacheStatistics = null;
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#CacheStatistics()}.
     * </p>
     */
    @Test
    public void testCtor() {
        Assert.assertNotNull("Unable to instantiate CacheStatistics", cacheStatistics);
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#getAccessCountsById()}. It verifies the returned value is
     * correct.
     * </p>
     */
    @Test
    public void testGetAccessCountsById() {
        Assert.assertNull("Incorrect accessCountsById", cacheStatistics.getAccessCountsById());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#setAccessCountsById(java.util.Map)}. It verifies the
     * assigned value is correct.
     * </p>
     */
    @Test
    public void testSetAccessCountsById() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("key", 1);
        cacheStatistics.setAccessCountsById(map);
        Assert.assertTrue("Incorrect accessCountsById", cacheStatistics.getAccessCountsById().equals(map));
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#getMissCount()}. It verifies the returned value is correct.
     * </p>
     */
    @Test
    public void testGetMissCount() {
        Assert.assertEquals("Incorrect missCount", 0, cacheStatistics.getMissCount());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#setMissCount(int)}. It verifies the assigned value is
     * correct.
     * </p>
     */
    @Test
    public void testSetMissCount() {
        cacheStatistics.setMissCount(1);
        Assert.assertEquals("Incorrect missCount", 1, cacheStatistics.getMissCount());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#getPersistedItemCount()}. It verifies the returned value is
     * correct.
     * </p>
     */
    @Test
    public void testGetPersistedItemCount() {
        Assert.assertEquals("Incorrect persistedItemCount", 0, cacheStatistics.getPersistedItemCount());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#setPersistedItemCount(int)}. It verifies the assigned value
     * is correct.
     * </p>
     */
    @Test
    public void testSetPersistedItemCount() {
        cacheStatistics.setPersistedItemCount(1);
        Assert.assertEquals("Incorrect persistedItemCount", 1, cacheStatistics.getPersistedItemCount());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#getInMemoryItemCount()}. It verifies the returned value is
     * correct.
     * </p>
     */
    @Test
    public void testGetInMemoryItemCount() {
        Assert.assertEquals("Incorrect inMemoryItemCount", 0, cacheStatistics.getInMemoryItemCount());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#setInMemoryItemCount(int)}. It verifies the assigned value
     * is correct.
     * </p>
     */
    @Test
    public void testSetInMemoryItemCount() {
        cacheStatistics.setInMemoryItemCount(1);
        Assert.assertEquals("Incorrect inMemoryItemCount", 1, cacheStatistics.getInMemoryItemCount());
    }
}
