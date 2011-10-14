/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.failuretests;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.ibm.support.electronic.cache.ObjectCacheConfigurationException;
import com.ibm.support.electronic.cache.PersistenceException;
import com.ibm.support.electronic.cache.cachemanager.CacheManagerImpl;


/**
 * <code>
 * Failure tests for the class <code>CacheManagerImpl</code>. </code>
 *
 * @author chuchao333
 * @version 1.0
 */
public class CacheManagerImplFailureTest extends TestCase {
    /**
     * <p>
     * The <code>CacheManagerImpl</code> instance used for test.
     * </p>
     */
    private CacheManagerImpl instance;

    /**
     * <p>
     * The <code>Properties</code> instance used for test.
     * </p>
     */
    private Properties props;

    /**
     * <p>
     * The <code>Dummy</code> instance, which is a Serializable object for test.
     * </p>
     */
    private Dummy dummy;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(CacheManagerImplFailureTest.class);
    }

    /**
     * <p>
     * set up the test environment.
     *
     * @throws Exception if any error occurs.
     *         </p>
     */
    protected void setUp() throws Exception {
        props = new Properties();
        InputStream inputStream = new FileInputStream("test_files/failuretests/config.properties");
        props.load(inputStream);

        instance = new CacheManagerImpl(props);

        dummy = new Dummy();
        dummy.setAge(100);
        dummy.setName("failure test");
    }

    /**
     * <p>
     * Failure test for the constructor <code>CacheManagerImpl(String)</code>.
     * </p>
     */
    public void testCtorStr_Failure1() {
        try {
            new CacheManagerImpl((String) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>CacheManagerImpl(String)</code>.
     * </p>
     */
    public void testCtorStr_Failure2() {
        try {
            new CacheManagerImpl("  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>CacheManagerImpl(String)</code>.
     * </p>
     */
    public void testCtorStr_Failure3() {
        try {
            new CacheManagerImpl("no_such_file");
            fail("ObjectCacheConfigurationException is expected.");
        } catch (ObjectCacheConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>CacheManagerImpl(Properties, String)</code>.
     * </p>
     */
    public void testCtorPropsStr_Failure1() {
        try {
            new CacheManagerImpl(null, "null");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>CacheManagerImpl(Properties, String)</code>.
     * </p>
     */
    public void testCtorPropsStr_Failure2() {
        try {
            new CacheManagerImpl(props, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>CacheManagerImpl(Properties, String)</code>.
     * </p>
     */
    public void testCtorPropsStr_Failure3() {
        try {
            new CacheManagerImpl(props, "\t   ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>CacheManagerImpl(Properties)</code>.
     * </p>
     */
    public void testCtorProps_Failure1() {
        try {
            new CacheManagerImpl((Properties) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>CacheManagerImpl(Properties)</code>.
     * </p>
     */
    public void testCtorProps_Failure2() {
        props.put("cacheSetName", " \n");
        try {
            new CacheManagerImpl(props);
            fail("ObjectCacheConfigurationException is expected.");
        } catch (ObjectCacheConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>CacheManagerImpl(Properties)</code>.
     * </p>
     */
    public void testCtorProps_Failure3() {
        props.put("cacheStatisticsPrinterClass", "failure");
        try {
            new CacheManagerImpl(props);
            fail("ObjectCacheConfigurationException is expected.");
        } catch (ObjectCacheConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>printStatistics(PrintWriter)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testPrintStatistics_Failure1() throws Exception {
        try {
            instance.printStatistics(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>put(String, java.io.Serializable)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testPut_Failure1() throws Exception {
        try {
            instance.put(null, dummy);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>put(String, java.io.Serializable)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testPut_Failure2() throws Exception {
        try {
            instance.put("  ", dummy);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>put(String, java.io.Serializable)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testPut_Failure3() throws Exception {
        props.put("maxMemorySize", "1");
        props.put("url", "failure");
        instance = new CacheManagerImpl(props);

        try {
            instance.put("first object", dummy);
            instance.put("second object", new Dummy());
            fail("PersistenceException is expected.");
        } catch (PersistenceException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>clear()</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testClear_Failure1() throws Exception {
        props.put("url", "failure");
        instance = new CacheManagerImpl(props);

        try {
            instance.clear();
            fail("PersistenceException is expected.");
        } catch (PersistenceException e) {
            // success
        }
    }
}
