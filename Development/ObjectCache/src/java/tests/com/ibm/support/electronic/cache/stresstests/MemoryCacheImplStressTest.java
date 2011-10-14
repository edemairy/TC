/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.stresstests;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.ibm.support.electronic.cache.memorycache.MemoryCacheImpl;
import com.ibm.support.electronic.cache.model.CacheStatistics;
import com.ibm.support.electronic.cache.model.CachedObject;


/**
 * <p>
 * Stress test case of {@link MemoryCacheImpl}.
 * </p>
 *
 * @author mumujava
 * @version 1.0
 */
public class MemoryCacheImplStressTest extends BaseStressTest {
    /**
     * <p>
     * The MemoryCacheImpl instance to test.
     * </p>
     */
    private MemoryCacheImpl instance;

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
        Properties p = new Properties();
        p.setProperty("initialCapacity", "100");
        p.setProperty("loadFactor", "0.5");
        instance = new MemoryCacheImpl(p);
        testCount = 1000;

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
        TestSuite suite = new TestSuite(MemoryCacheImplStressTest.class);
        return suite;
    }

    /**
     * <p>
     * Stress test for method MemoryCacheImpl#put().
     * </p>
     *
     * @throws Throwable to junit
     */
    public void test_put() throws Throwable {
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
            CachedObject cachedObject = new CachedObject();
            cachedObject.setCacheSetName("set" + i);
            cachedObject.setContent(("cotent" + i).getBytes());
            instance.put("" + i, cachedObject);
        }
        for (int i = 0; i < testCount; i++) {
            CachedObject cachedObject = instance.get("" + i);
            assertEquals("set" + i, cachedObject.getCacheSetName());
        }

        for (int i = 0; i < testCount; i++) {
            CachedObject cachedObject = instance.remove("" + i);
            assertEquals("set" + i, cachedObject.getCacheSetName());
        }
        for (int i = 0; i < testCount; i++) {
            CachedObject cachedObject = instance.get("" + i);
            assertEquals(null, cachedObject);
        }

        System.out.println("Run put()/get()/remove() for  " + testCount + " times takes "
            + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for method MemoryCacheImpl#clear().
     * </p>
     *
     * @throws Throwable to junit
     */
    public void test_clear() throws Throwable {
        final CacheStatistics cacheStatistics = new CacheStatistics();
        cacheStatistics.setInMemoryItemCount(10);
        cacheStatistics.setMissCount(20);
        cacheStatistics.setPersistedItemCount(5);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        cacheStatistics.setAccessCountsById(map);

        for (int j = 0; j < testCount; j++) {
            for (int i = 0; i < testCount; i++) {
                CachedObject cachedObject = new CachedObject();
                cachedObject.setCacheSetName("set" + i);
                cachedObject.setContent(("cotent" + i).getBytes());
                instance.put("" + i, cachedObject);
            }
            instance.clear();
            for (int i = 0; i < testCount; i++) {
                CachedObject cachedObject = instance.get("" + i);
                assertEquals(null, cachedObject);
            }
        }

        System.out.println("Run put()/get()/clear() for  " + testCount + " times takes "
            + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for method MemoryCacheImpl#getReplaceableCachedObject().
     * </p>
     *
     * @throws Throwable to junit
     */
    public void test_getReplaceableCachedObject() throws Throwable {
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
            CachedObject cachedObject = new CachedObject();
            cachedObject.setCacheSetName("set" + i);
            cachedObject.setContent(("cotent" + i).getBytes());
            instance.put("" + i, cachedObject);
        }

        for (int j = 0; j < testCount; j++) {
            for (int i = 0; i < testCount; i++) {
                CachedObject cachedObject = instance.getReplaceableCachedObject();
                assertEquals("set" + 0, cachedObject.getCacheSetName());
            }
        }
        instance.clear();
        for (int i = 0; i < testCount; i++) {
            CachedObject cachedObject = instance.getReplaceableCachedObject();
            assertEquals(null, cachedObject);
        }

        System.out.println("Run put()/getReplaceableCachedObject() for  " + testCount + " times takes "
            + (new Date().getTime() - start) + "ms");
    }
}
