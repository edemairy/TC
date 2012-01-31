/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.stresstests;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.ibm.support.electronic.cache.ObjectCacheException;
import com.ibm.support.electronic.cache.PersistenceException;
import com.ibm.support.electronic.cache.cachemanager.CacheManagerImpl;
import com.ibm.support.electronic.cache.model.CacheStatistics;
import com.ibm.support.electronic.cache.model.CachedObject;


/**
 * <p>
 * Stress test case of {@link CacheManagerImpl}.
 * </p>
 *
 * @author mumujava
 * @version 1.0
 */
public class CacheManagerImplStressTest extends BaseStressTest {
    /**
     * <p>
     * The CacheManagerImpl instance to test.
     * </p>
     */
    private CacheManagerImpl instance;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception to jUnit.
     */
    @Override
    public void setUp() throws Exception {
        TestHelper.clear();
        super.setUp();
        Properties config = new Properties();
        FileInputStream fio = new FileInputStream("test_files/stresstests/config.properties");
        try {
            config.load(fio);
        } finally {
            fio.close();
        }
        instance = new CacheManagerImpl(config);

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
        TestHelper.clear();
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
        TestSuite suite = new TestSuite(CacheManagerImplStressTest.class);
        return suite;
    }

    /**
     * <p>
     * Stress test for method CacheManagerImpl#printStatistics().
     * </p>
     *
     * @throws Throwable to junit
     */
    public void test_save() throws Throwable {
        testCount = 200;
        Thread thread[] = new Thread[testCount];
        for (int i = 0; i < testCount; i++) {
            thread[i] = new TestThread(i) {
                public void run() {
                    try {
                        CachedObject cachedObject = new CachedObject();
                        cachedObject.setId(getIndex() + "");
                        cachedObject.setCacheSetName("set" + getIndex());
                        cachedObject.setContent(("cotent" + getIndex()).getBytes());
                        instance.put(getIndex() + "", "This is a String Object " + getIndex());

                        Serializable got = instance.get("wrong index" + getIndex() + "");
                        assertEquals(null, got);
                        got = instance.get(getIndex() + "");
                        assertEquals("This is a String Object " + getIndex(), got);

                        // make sure all objects are cached.
                        Thread.sleep(1500);
                        CacheStatistics stat = instance.getCacheStatistics();
                        assertEquals(30, stat.getInMemoryItemCount());
                        assertEquals(testCount - 30, stat.getPersistedItemCount());
                        assertEquals(1, stat.getAccessCountsById().get(getIndex() + "").intValue());

                        assertEquals(testCount, stat.getMissCount());

                        // make sure all objects are cached.
                        Thread.sleep(3000);
                        instance.clear();
                        got = instance.get(getIndex() + "");
                        assertEquals(null, got);
                    } catch (PersistenceException e) {
                        lastError = e;
                    } catch (ObjectCacheException e) {
                        lastError = e;
                    } catch (InterruptedException e) {
                        lastError = e;
                    }

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

        System.out.println("Run put()/get()/getCacheStatistics()/clear() for  " + testCount + " times takes "
            + (new Date().getTime() - start) + "ms");
    }
}
