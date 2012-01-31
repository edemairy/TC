/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.accuracytests;

import java.util.Properties;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.ibm.support.electronic.cache.MemoryCache;
import com.ibm.support.electronic.cache.memorycache.MemoryCacheImpl;
import com.ibm.support.electronic.cache.model.CachedObject;

/**
 * The accuracy test for class {@link MemoryCacheImpl}.
 *
 * @author KLW
 * @version 1.0
 */
public class MemoryCacheImplAccuracyTest extends TestCase {
    /**
     * <p>
     * the instance of MemoryCacheImpl for test.
     * </p>
     */
    private MemoryCacheImpl instance;

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
     * </p>
     *
     * @throws Exception
     *             if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        prop = new Properties();
        instance = new MemoryCacheImpl(prop);

        // create instance of CachedObject
        cachedObject = new CachedObject();
        cachedObject.setCacheSetName("name");
        cachedObject.setContent(new byte[10]);
        cachedObject.setId("id");
    }

    /**
     * <p>
     * The accuracy test method for {@link MemoryCacheImpl#MemoryCacheImpl(java.util.Properties)}.
     * </p>
     */
    @Test
    public void testMemoryCacheImpl() {
        assertNotNull("The instance should not be null!.", instance);
        assertTrue("The instance type is incorrect.", instance instanceof MemoryCache);
    }

    /**
     * <p>
     * The accuracy test method for {@link MemoryCacheImpl#getReplaceableCachedObject()}.
     * </p>
     */
    @Test
    public void testGetReplaceableCachedObject() {
        assertNull("no cache object should be return.", instance.getReplaceableCachedObject());

        instance.put("id1", cachedObject);
        AccuracyTestHelper.checkCacheObject(instance.getReplaceableCachedObject(), cachedObject);

        // put another cached object
        CachedObject cachedObject2 = new CachedObject();
        cachedObject.setId("2");
        instance.put("id2", cachedObject2);
        // the result also should be the first one.
        AccuracyTestHelper.checkCacheObject(instance.getReplaceableCachedObject(), cachedObject);

        // update the map
        instance.remove("id1");
        AccuracyTestHelper.checkCacheObject(instance.getReplaceableCachedObject(), cachedObject2);
    }

    /**
     * <p>
     * The accuracy test method for {@link MemoryCacheImpl#get(String)}.
     * </p>
     */
    @Test
    public void testGet() {
        assertNull("no cache object should be return.", instance.get("id"));

        instance.put("idd", cachedObject);
        AccuracyTestHelper.checkCacheObject(instance.get("idd"), cachedObject);
    }

    /**
     * <p>
     * The accuracy test method for {@link MemoryCacheImpl#put(String, CachedObject)}. No exception should be thrown.
     * </p>
     */
    @Test
    public void testPut() {
        instance.put("id", cachedObject);
        AccuracyTestHelper.checkCacheObject(instance.get("id"), cachedObject);

        cachedObject.setId("test");
        instance.put("id2", cachedObject);
        AccuracyTestHelper.checkCacheObject(instance.get("id2"), cachedObject);
    }

    /**
     * <p>
     * The accuracy test method for {@link MemoryCacheImpl#clear()}.
     * </p>
     */
    @Test
    public void testClear() {
        instance.put("id", cachedObject);
        instance.put("id2", cachedObject);
        instance.clear();

        assertNull("no cache object should be return.", instance.getReplaceableCachedObject());
        assertNull("no cache object should be return.", instance.get("id"));
        assertNull("no cache object should be return.", instance.get("id2"));
    }

    /**
     * <p>
     * The accuracy test method for {@link MemoryCacheImpl#remove(String)}.
     * </p>
     */
    @Test
    public void testRemove() {
        instance.put("id", cachedObject);
        instance.put("id2", cachedObject);
        instance.remove("id");

        assertNull("no cache object should be return.", instance.get("id"));
        assertNotNull("cache object should be return.", instance.get("id2"));
    }
}
