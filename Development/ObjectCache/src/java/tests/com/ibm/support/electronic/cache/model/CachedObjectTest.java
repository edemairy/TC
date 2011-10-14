/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.model;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * <p>
 * Unit test case of {@link CachedObject}.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CachedObjectTest {
    /**
     * <p>
     * The CachedObject instance to test against.
     * </p>
     */
    private CachedObject cachedObject;

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CachedObjectTest.class);
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
        cachedObject = new CachedObject();
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
        cachedObject = null;
    }

    /**
     * <p>
     * Accuracy test method for {@link CachedObject#CachedObject()}.
     * </p>
     */
    @Test
    public void testCtor() {
        Assert.assertNotNull("Unable to instantiate CachedObject", cachedObject);
    }

    /**
     * <p>
     * Accuracy test method for {@link CachedObject#getCacheSetName()}. It verifies the returned value is correct.
     * </p>
     */
    @Test
    public void testGetCacheSetName() {
        Assert.assertNull("Incorrect cacheSetName", cachedObject.getCacheSetName());
    }

    /**
     * <p>
     * Accuracy test method for {@link CachedObject#setCacheSetName(java.lang.String)}. It verifies the assigned
     * value is correct.
     * </p>
     */
    @Test
    public void testSetCacheSetName() {
        String name = "default";
        cachedObject.setCacheSetName(name);
        Assert.assertEquals("Incorrect cacheSetName", name, cachedObject.getCacheSetName());
    }

    /**
     * <p>
     * Accuracy test method for {@link CachedObject#getId()}. It verifies the returned value is correct.
     * </p>
     */
    @Test
    public void testGetId() {
        Assert.assertNull("Incorrect id", cachedObject.getId());
    }

    /**
     * <p>
     * Accuracy test method for {@link CachedObject#setId(java.lang.String)}. It verifies the assigned value is
     * correct.
     * </p>
     */
    @Test
    public void testSetId() {
        String id = "default";
        cachedObject.setId(id);
        Assert.assertEquals("Incorrect id", id, cachedObject.getId());
    }

    /**
     * <p>
     * Accuracy test method for {@link CachedObject#getContent()}. It verifies the returned value is correct.
     * </p>
     */
    @Test
    public void testGetContent() {
        Assert.assertNull("Incorrect content", cachedObject.getContent());
    }

    /**
     * <p>
     * Accuracy test method for {@link CachedObject#setContent(byte[])}. It verifies the assigned value is correct.
     * </p>
     */
    @Test
    public void testSetContent() {
        byte[] content = {1};
        cachedObject.setContent(content);
        Assert.assertSame("Incorrect content", content, cachedObject.getContent());
    }
}
