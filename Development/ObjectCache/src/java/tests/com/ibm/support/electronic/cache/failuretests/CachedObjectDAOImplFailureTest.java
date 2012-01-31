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
import com.ibm.support.electronic.cache.dao.CachedObjectDAOImpl;
import com.ibm.support.electronic.cache.model.CachedObject;


/**
 * <p>
 * Failure tests for the class <code>CachedObjectDAOImpl</code>.
 * </p>
 *
 * @author chuchao333
 * @version 1.0
 */
public class CachedObjectDAOImplFailureTest extends TestCase {
    /**
     * <p>
     * The <code>CachedObjectDAOImpl</code> instance used for test.
     * </p>
     */
    private CachedObjectDAOImpl instance;

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
        return new TestSuite(CachedObjectDAOImplFailureTest.class);
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

        instance = new CachedObjectDAOImpl(props);

        cachedObject = new CachedObject();
        cachedObject.setCacheSetName("failuretests");
        cachedObject.setContent(new byte[12]);
        cachedObject.setId("id");
    }

    /**
     * <p>
     * Failure test for the constructor <code>CachedObjectDAOImpl(Properties)</code>.
     * </p>
     */
    public void testCtorProps_Failure1() {
        try {
            new CachedObjectDAOImpl(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>CachedObjectDAOImpl(Properties)</code>.
     * </p>
     */
    public void testCtorProps_Failure2() {
        props.remove("url");
        try {
            new CachedObjectDAOImpl(props);
            fail("ObjectCacheConfigurationException is expected.");
        } catch (ObjectCacheConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>CachedObjectDAOImpl(Properties)</code>.
     * </p>
     */
    public void testCtorProps_Failure3() {
        props.put("driver", "no_such_driver");
        try {
            new CachedObjectDAOImpl(props);
            fail("ObjectCacheConfigurationException is expected.");
        } catch (ObjectCacheConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>deleteByCacheSet(String)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testDeleteByCacheSet_Failure1() throws Exception {
        try {
            instance.deleteByCacheSet(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>deleteByCacheSet(String)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testDeleteByCacheSet_Failure2() throws Exception {
        try {
            instance.deleteByCacheSet("\n");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>get(String, String)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testGet_Failure1() throws Exception {
        try {
            instance.get(null, "null");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>get(String, String)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testGet_Failure2() throws Exception {
        try {
            instance.get("  ", "empty");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>get(String, String)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testGet_Failure3() throws Exception {
        try {
            instance.get("empty", "  \t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>get(String, String)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testGet_Failure4() throws Exception {
        try {
            instance.get("null", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>save(CachedObject)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testSave_Failure1() throws Exception {
        try {
            instance.save(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>save(CachedObject)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testSave_Failure2() throws Exception {
        cachedObject.setContent(null);
        try {
            instance.save(cachedObject);
            fail("PersistenceException is expected.");
        } catch (PersistenceException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>save(CachedObject)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testSave_Failure3() throws Exception {
        props.remove("user");
        props.remove("password");
        instance = new CachedObjectDAOImpl(props);
        try {
            instance.save(cachedObject);
            fail("PersistenceException is expected.");
        } catch (PersistenceException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>delete(String, String)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testDelete_Failure1() throws Exception {
        try {
            instance.delete(null, "null");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>delete(String, String)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testDelete_Failure2() throws Exception {
        try {
            instance.delete("   \t \n", "empty");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>delete(String, String)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testDelete_Failure3() throws Exception {
        try {
            instance.delete("empty", "   \t \n");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for the method <code>delete(String, String)</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testDelete_Failure4() throws Exception {
        try {
            instance.delete("null", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
}
