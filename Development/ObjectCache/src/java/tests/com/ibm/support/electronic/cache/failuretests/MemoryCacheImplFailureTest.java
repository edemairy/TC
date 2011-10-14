/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.failuretests;

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.ibm.support.electronic.cache.ObjectCacheConfigurationException;
import com.ibm.support.electronic.cache.memorycache.MemoryCacheImpl;
import com.ibm.support.electronic.cache.model.CachedObject;


/**
 * <p>
 * Failure tests for the class <code>MemoryCacheImpl</code>.
 * </p>
 *
 * @author chuchao333
 * @version 1.0
 */
public class MemoryCacheImplFailureTest extends TestCase {
    /**
     * <p>
     * The <code>MemoryCacheImpl</code> instance used for test.
     * </p>
     */
    private MemoryCacheImpl instance;

    /**
     * <p>
     * The <code>Properties</code> instance used for test.
     * </p>
     */
    private Properties props;

    /**
     * <p>
     * The <code>CachedObject</code> instance used for test.
     * </p>
     */
    private CachedObject cachedObject;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(MemoryCacheImplFailureTest.class);
    }

    /**
     * <p>
     * set up the test environment.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        props = new Properties();
        instance = new MemoryCacheImpl(props);

        // create instance of CachedObject
        cachedObject = new CachedObject();
        cachedObject.setCacheSetName("failure test");
        cachedObject.setContent(new byte[100]);
        cachedObject.setId("id");
    }

    /**
     * <p>
     * Tear down the test environment.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        props = null;
        cachedObject = null;
        instance = null;
    }

    /**
     * <p>
     * Failure test for the constructor <code>MemoryCacheImpl(Properties)</code>.
     * </p>
     */
    public void testCtor_Failure1() {
        try {
            new MemoryCacheImpl(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>MemoryCacheImpl(Properties)</code>.
     * </p>
     */
    public void testCtor_Failure2() {
        props.put("initialCapacity", "not a number");
        try {
            new MemoryCacheImpl(props);
            fail("ObjectCacheConfigurationException is expected.");
        } catch (ObjectCacheConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>MemoryCacheImpl(Properties)</code>.
     * </p>
     */
    public void testCtor_Failure3() {
        props.put("initialCapacity", "-10");
        try {
            new MemoryCacheImpl(props);
            fail("ObjectCacheConfigurationException is expected.");
        } catch (ObjectCacheConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>MemoryCacheImpl(Properties)</code>.
     * </p>
     */
    public void testCtor_Failure4() {
        props.put("loadFactor", "-10");
        try {
            new MemoryCacheImpl(props);
            fail("ObjectCacheConfigurationException is expected.");
        } catch (ObjectCacheConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>MemoryCacheImpl(Properties)</code>.
     * </p>
     */
    public void testCtor_Failure5() {
        props.put("loadFactor", "0");
        try {
            new MemoryCacheImpl(props);
            fail("ObjectCacheConfigurationException is expected.");
        } catch (ObjectCacheConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>MemoryCacheImpl(Properties)</code>.
     * </p>
     */
    public void testCtor_Failure6() {
        props.put("loadFactor", "not a number");
        try {
            new MemoryCacheImpl(props);
            fail("ObjectCacheConfigurationException is expected.");
        } catch (ObjectCacheConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>get(String id)</code>.
     * </p>
     */
    public void testGet_Failure1() {
        try {
            instance.get(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>get(String id)</code>.
     * </p>
     */
    public void testGet_Failure2() {
        try {
            instance.get("  \t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>put(String id, CachedObject obj)</code>.
     * </p>
     */
    public void testPut_Failure1() {
        try {
            instance.put("  \t", cachedObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>put(String id, CachedObject obj)</code>.
     * </p>
     */
    public void testPut_Failure2() {
        try {
            instance.put(null, cachedObject);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>put(String id, CachedObject obj)</code>.
     * </p>
     */
    public void testPut_Failure3() {
        try {
            instance.put("null", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>remove(String id)</code>.
     * </p>
     */
    public void testRemove_Failure1() {
        try {
            instance.remove("  \t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>remove(String id)</code>.
     * </p>
     */
    public void testRemove_Failure2() {
        try {
            instance.remove(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
}
