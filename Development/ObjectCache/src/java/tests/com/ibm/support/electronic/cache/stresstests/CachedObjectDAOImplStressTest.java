/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.stresstests;

import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.ibm.support.electronic.cache.PersistenceException;
import com.ibm.support.electronic.cache.dao.CachedObjectDAOImpl;
import com.ibm.support.electronic.cache.model.CachedObject;


/**
 * <p>
 * Stress test case of {@link CachedObjectDAOImpl}.
 * </p>
 *
 * @author mumujava
 * @version 1.0
 */
public class CachedObjectDAOImplStressTest extends BaseStressTest {
    /**
     * <p>
     * The CachedObjectDAOImpl instance to test.
     * </p>
     */
    private CachedObjectDAOImpl instance;

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
        Properties config = new Properties();
        FileInputStream fio = new FileInputStream("test_files/stresstests/config.properties");
        try {
            config.load(fio);
        } finally {
            fio.close();
        }
        instance = new CachedObjectDAOImpl(config);

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
        TestSuite suite = new TestSuite(CachedObjectDAOImplStressTest.class);
        return suite;
    }

    /**
     * <p>
     * Stress test for method CachedObjectDAOImpl#printStatistics().
     * </p>
     *
     * @throws Throwable to junit
     */
    public void test_save() throws Throwable {
        Thread thread[] = new Thread[testCount];
        for (int i = 0; i < testCount; i++) {
            thread[i] = new TestThread(i) {
                public void run() {
                    try {
                        CachedObject cachedObject = new CachedObject();
                        cachedObject.setId(getIndex() + "");
                        cachedObject.setCacheSetName("set" + getIndex());
                        cachedObject.setContent(("content" + getIndex()).getBytes());
                        instance.save(cachedObject);

                        CachedObject got = instance.get("set" + getIndex(), getIndex() + "");
                        assertEquals("content" + getIndex(), new String(got.getContent()));

                        instance.delete("set" + getIndex(), getIndex() + "");
                        assertEquals(null, instance.get("set" + getIndex(), getIndex() + ""));
                    } catch (PersistenceException e) {
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

        System.out.println("Run save()/get()/delete() for  " + testCount + " times takes "
            + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for method CachedObjectDAOImpl#printStatistics().
     * </p>
     *
     * @throws Throwable to junit
     */
    public void test_deleteByCacheSet() throws Throwable {
        Thread thread[] = new Thread[testCount];
        for (int i = 0; i < testCount; i++) {
            thread[i] = new TestThread(i) {
                public void run() {
                    try {
                        CachedObject cachedObject = new CachedObject();
                        cachedObject.setId(getIndex() + "");
                        cachedObject.setCacheSetName("set" + getIndex());
                        cachedObject.setContent(("content" + getIndex()).getBytes());
                        instance.save(cachedObject);

                        instance.deleteByCacheSet("set" + getIndex());
                        CachedObject got = instance.get("set" + getIndex(), getIndex() + "");
                        assertEquals(null, got);
                    } catch (PersistenceException e) {
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

        System.out.println("Run save()/get()/deleteByCacheSet() for  " + testCount + " times takes "
            + (new Date().getTime() - start) + "ms");
    }
}
