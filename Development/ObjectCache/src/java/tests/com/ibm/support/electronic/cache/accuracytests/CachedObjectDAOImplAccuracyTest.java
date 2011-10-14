/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.accuracytests;

import java.util.Properties;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.ibm.support.electronic.cache.CachedObjectDAO;
import com.ibm.support.electronic.cache.dao.CachedObjectDAOImpl;
import com.ibm.support.electronic.cache.model.CachedObject;

/**
 * The accuracy test for class {@link CachedObjectDAOImpl}.
 *
 * @author KLW
 * @version 1.0
 */
public class CachedObjectDAOImplAccuracyTest extends TestCase {
    /**
     * <p>
     * the instance for test.
     * </p>
     */
    private CachedObjectDAOImpl instance;

    /**
     * <p>
     * the properties for test.
     * </p>
     */
    private Properties prop;
    /**
     * <p>
     * the cachedObject for test.
     * </p>
     */
    private CachedObject cachedObject;

    /**
     * <p>
     * set up the test environment.
     *
     * @throws Exception
     *             if any error occurs.
     * </p>
     */
    @Before
    public void setUp() throws Exception {

        prop = AccuracyTestHelper.getProperties();
        instance = new CachedObjectDAOImpl(prop);

        cachedObject = new CachedObject();
        cachedObject.setCacheSetName("cacheSetName");
        cachedObject.setContent(new byte[12]);
        cachedObject.setId("id");
    }

    /**
     * <p>
     * The accuracy test method for {@link CachedObjectDAOImpl#CachedObjectDAOImpl(java.util.Properties)}.
     * </p>
     */
    @Test
    public void testCachedObjectDAOImpl() {
        assertNotNull("The instance should not be null!.", instance);
        assertTrue("The instance type is incorrect.", instance instanceof CachedObjectDAO);
    }

    /**
     * <p>
     * The accuracy test method for {@link CachedObjectDAOImpl#deleteByCacheSet(java.lang.String)}. No exception should
     * be thrown.
     *
     * @throws Exception
     *             if any error occurs.
     * </p>
     */
    @Test
    public void testDeleteByCacheSet() throws Exception {
        instance.save(cachedObject);
        instance.deleteByCacheSet(cachedObject.getCacheSetName());
        assertNull("cachedObject should be deleted.", instance.get("cacheSetName", "id"));
    }

    /**
     * <p>
     * The accuracy test method for {@link CachedObjectDAOImpl#get(java.lang.String, java.lang.String)}. No Exception
     * should be thrown.
     *
     * @throws Exception
     *             if any error occurs.
     * </p>
     */
    @Test
    public void testGet() throws Exception {
        // get not exist object
        assertNull("cachedObject should be deleted.", instance.get("cacheSetName", "invalid"));

        instance.save(cachedObject);
        CachedObject result = instance.get("cacheSetName", "id");
        AccuracyTestHelper.checkCacheObject(result, cachedObject);

        instance.delete("cachedObject", "id");
    }

    /**
     * <p>
     * The accuracy test method for
     * {@link CachedObjectDAOImpl#save(com.ibm.support.electronic.cache.model.CachedObject)}. insert new row to table,
     * No exception should be thrown.
     *
     * @throws Exception
     *             if any error occurs.
     * </p>
     */
    @Test
    public void testSave_oneTime() throws Exception {
        instance.save(cachedObject);
        CachedObject result = instance.get("cacheSetName", "id");
        AccuracyTestHelper.checkCacheObject(result, cachedObject);

        instance.delete("cachedObject", "id");
    }

    /**
     * <p>
     * The accuracy test method for
     * {@link CachedObjectDAOImpl#save(com.ibm.support.electronic.cache.model.CachedObject)}. Update a row in table, no
     * exception should be thrown.
     *
     * @throws Exception
     *             if any error occurs.
     * </p>
     */
    @Test
    public void testSave_again() throws Exception {
        instance.save(cachedObject);

        // update cachedObject and save again
        cachedObject.setContent(new byte[10]);
        instance.save(cachedObject);

        CachedObject result = instance.get("cacheSetName", "id");
        // check the result
        AccuracyTestHelper.checkCacheObject(result, cachedObject);

        instance.delete("cachedObject", "id");
    }

    /**
     * <p>
     * The accuracy test method for {@link CachedObjectDAOImpl#delete(java.lang.String, java.lang.String)}. No exception
     * should be thrown.
     *
     * @throws Exception
     *             if any error occurs.
     * </p>
     */
    @Test
    public void testDelete() throws Exception {
        instance.save(cachedObject);
        instance.delete("cacheSetName", "id");
        assertNull("cachedObject should be deleted.", instance.get("cacheSetName", "id"));
    }
}
