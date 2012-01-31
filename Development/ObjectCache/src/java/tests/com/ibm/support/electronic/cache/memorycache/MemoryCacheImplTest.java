/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.memorycache;

import java.util.LinkedHashMap;
import java.util.Properties;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ibm.support.electronic.cache.ObjectCacheConfigurationException;
import com.ibm.support.electronic.cache.TestHelper;
import com.ibm.support.electronic.cache.model.CachedObject;


/**
 * <p>
 * Unit test case of {@link MemoryCacheImpl}.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MemoryCacheImplTest {
    /**
     * <p>
     * The MemoryCacheImpl instance to test against.
     * </p>
     */
    private MemoryCacheImpl memoryCache;

    /**
     * <p>
     * The Properties instance used for testing.
     * </p>
     */
    private Properties prop;

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MemoryCacheImplTest.class);
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
        prop = new Properties();
        prop.setProperty("initialCapacity", "3");
        prop.setProperty("loadFactor", "0.75");
        memoryCache = new MemoryCacheImpl(prop);
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
        memoryCache = null;
        prop = null;
    }

    /**
     * <p>
     * Accuracy test method for {@link MemoryCacheImpl#MemoryCacheImpl(java.util.Properties)} when all optional
     * properties are defined.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testCtor1() throws Exception {
        Assert.assertNotNull("Unable to instantiate MemoryCacheImpl", memoryCache);
        Assert.assertNotNull("Incorrect internal cache", TestHelper.getField(memoryCache, "cache"));
    }

    /**
     * <p>
     * Accuracy test method for {@link MemoryCacheImpl#MemoryCacheImpl(java.util.Properties)} when optional
     * properties are not defined.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testCtor2() throws Exception {
        memoryCache = new MemoryCacheImpl(new Properties());
        Assert.assertNotNull("Unable to instantiate MemoryCacheImpl", memoryCache);
        Assert.assertNotNull("Incorrect internal cache", TestHelper.getField(memoryCache, "cache"));
    }

    /**
     * <p>
     * Failure test method for {@link MemoryCacheImpl#MemoryCacheImpl(java.util.Properties)} when prop is null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_NullProp() {
        new MemoryCacheImpl(null);
    }

    /**
     * <p>
     * Failure test method for {@link MemoryCacheImpl#MemoryCacheImpl(java.util.Properties)} when the value of
     * property initialCapacity is not a string.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_InitialCapacityNotString() {
        prop.put("initialCapacity", 1L);
        new MemoryCacheImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link MemoryCacheImpl#MemoryCacheImpl(java.util.Properties)} when the value of
     * property initialCapacity is an empty string.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_InitialCapacityEmptyString() {
        prop.put("initialCapacity", "  ");
        new MemoryCacheImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link MemoryCacheImpl#MemoryCacheImpl(java.util.Properties)} when the value of
     * property initialCapacity cannot be parsed as integer.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_InvalidInitialCapacity() {
        prop.setProperty("initialCapacity", "one");
        new MemoryCacheImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link MemoryCacheImpl#MemoryCacheImpl(java.util.Properties)} when the value of
     * property initialCapacity is negative.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_IllegalInitialCapacity() {
        prop.setProperty("initialCapacity", "-2");
        new MemoryCacheImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link MemoryCacheImpl#MemoryCacheImpl(java.util.Properties)} when the value of
     * property loadFactor is not a string.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_LoadFactorNotString() {
        prop.put("loadFactor", 1L);
        new MemoryCacheImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link MemoryCacheImpl#MemoryCacheImpl(java.util.Properties)} when the value of
     * property loadFactor is an empty string.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_LoadFactorEmptyString() {
        prop.put("loadFactor", "  ");
        new MemoryCacheImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link MemoryCacheImpl#MemoryCacheImpl(java.util.Properties)} when the value of
     * property loadFactor cannot be parsed as float.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_InvalidLoadFactor() {
        prop.setProperty("loadFactor", ".76x");
        new MemoryCacheImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link MemoryCacheImpl#MemoryCacheImpl(java.util.Properties)} when the value of
     * property loadFactor is nonpositive.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_IllegalLoadFactor() {
        prop.setProperty("loadFactor", "0");
        new MemoryCacheImpl(prop);
    }

    /**
     * <p>
     * Accuracy test method for {@link MemoryCacheImpl#getReplaceableCachedObject()} when the cache is empty.
     * </p>
     */
    @Test
    public void testGetReplaceableCachedObject1() {
        Assert.assertNull("Should be no replaceable cached object", memoryCache.getReplaceableCachedObject());
    }

    /**
     * <p>
     * Accuracy test method for {@link MemoryCacheImpl#getReplaceableCachedObject()} when the cache is not empty.
     * </p>
     */
    @Test
    public void testGetReplaceableCachedObject2() {
        // put some objects to the cache
        CachedObject obj1 = new CachedObject();
        CachedObject obj2 = new CachedObject();
        CachedObject obj3 = new CachedObject();
        memoryCache.put("1", obj1);
        memoryCache.put("2", obj2);
        memoryCache.put("3", obj3);

        Assert.assertSame("Incorrect replaceable cached object", obj1, memoryCache.getReplaceableCachedObject());
    }

    /**
     * <p>
     * Accuracy test method for {@link MemoryCacheImpl#get(java.lang.String)}.
     * </p>
     */
    @Test
    public void testGet() {
        // get object that doesn't exist
        String id = "1";
        Assert.assertNull("Should not exist", memoryCache.get(id));

        // put an object
        CachedObject obj1 = new CachedObject();
        memoryCache.put(id, obj1);

        Assert.assertSame("Incorrect object", obj1, memoryCache.get(id));
    }

    /**
     * <p>
     * Failure test method for {@link MemoryCacheImpl#get(java.lang.String)} when id is null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGet_NullId() {
        memoryCache.get(null);
    }

    /**
     * <p>
     * Failure test method for {@link MemoryCacheImpl#get(java.lang.String)} when id is empty.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGet_EmptyId() {
        memoryCache.get(" ");
    }

    /**
     * <p>
     * Accuracy test method for
     * {@link MemoryCacheImpl#put(java.lang.String, com.ibm.support.electronic.cache.model.CachedObject)}.
     * </p>
     *
     * @throws Exception to junit.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPut() throws Exception {
        // put some objects to the cache
        CachedObject obj1 = new CachedObject();
        CachedObject obj2 = new CachedObject();
        memoryCache.put("1", obj1);
        memoryCache.put("2", obj2);

        LinkedHashMap<String, CachedObject> cache = (LinkedHashMap<String, CachedObject>) TestHelper.getField(
            memoryCache, "cache");

        Assert.assertTrue("Incorrect number of cached objects", cache.size() == 2);
        Assert.assertSame("Incorrect object1", obj1, memoryCache.get("1"));
        Assert.assertSame("Incorrect object2", obj2, memoryCache.get("2"));
    }

    /**
     * <p>
     * Failure test method for
     * {@link MemoryCacheImpl#put(java.lang.String, com.ibm.support.electronic.cache.model.CachedObject)} when id
     * is null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPut_NullId() {
        memoryCache.put(null, new CachedObject());
    }

    /**
     * <p>
     * Failure test method for
     * {@link MemoryCacheImpl#put(java.lang.String, com.ibm.support.electronic.cache.model.CachedObject)} when id
     * is empty.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPut_EmptyId() {
        memoryCache.put("  ", new CachedObject());
    }

    /**
     * <p>
     * Failure test method for
     * {@link MemoryCacheImpl#put(java.lang.String, com.ibm.support.electronic.cache.model.CachedObject)} when
     * cachedObject is null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPut_NullCachedObject() {
        memoryCache.put("1", null);
    }

    /**
     * <p>
     * Accuracy test method for {@link MemoryCacheImpl#clear()}.
     * </p>
     *
     * @throws Exception to junit.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testClear() throws Exception {
        // put some objects to the cache
        CachedObject obj1 = new CachedObject();
        CachedObject obj2 = new CachedObject();
        memoryCache.put("1", obj1);
        memoryCache.put("2", obj2);

        LinkedHashMap<String, CachedObject> cache = (LinkedHashMap<String, CachedObject>) TestHelper.getField(
            memoryCache, "cache");

        Assert.assertTrue("Incorrect number of cached objects", cache.size() == 2);

        // clear the cache
        memoryCache.clear();
        Assert.assertTrue("Should be empty after being cleared", cache.isEmpty());
    }

    /**
     * <p>
     * Accuracy test method for {@link MemoryCacheImpl#remove(java.lang.String)} when cached object to remove does
     * not exist.
     * </p>
     */
    @Test
    public void testRemove1() {
        Assert.assertNull("Should not exist", memoryCache.remove("1"));
    }

    /**
     * <p>
     * Accuracy test method for {@link MemoryCacheImpl#remove(java.lang.String)} when cached object to remove
     * exists.
     * </p>
     */
    @Test
    public void testRemove2() {
        // put object to cache
        String id = "1";
        CachedObject obj1 = new CachedObject();
        memoryCache.put(id, obj1);
        Assert.assertSame("Incorrect cachedObject", obj1, memoryCache.remove(id));
    }

    /**
     * <p>
     * Failure test method for {@link MemoryCacheImpl#remove(java.lang.String)} when id is null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullId() {
        memoryCache.remove(null);
    }

    /**
     * <p>
     * Failure test method for {@link MemoryCacheImpl#remove(java.lang.String)} when id is empty.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_EmptyId() {
        memoryCache.remove(" ");
    }
}
