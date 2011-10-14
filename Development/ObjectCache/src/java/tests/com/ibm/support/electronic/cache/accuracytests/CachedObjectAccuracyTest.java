/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.accuracytests;


import static org.junit.Assert.assertArrayEquals;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.ibm.support.electronic.cache.model.CachedObject;


/**
 * The accuracy test for class {@link CachedObject}.
 *
 * @author KLW
 * @version 1.0
 */
public class CachedObjectAccuracyTest extends TestCase {
    /**
     * <p>
     * the instance for test.
     * </p>
     */
    private CachedObject instance;

    /**
     * <p>
     * set up the test environment.
     * </p>
     */
    @Before
    public void setUp() {
        instance = new CachedObject();
    }

    /**
     * <p>
     * The accuracy test method for {@link CachedObject#CachedObject()}.
     * </p>
     */
    @Test
    public void testCachedObject() {
        assertNotNull("The instance should not be null!.", instance);
    }

    /**
     * <p>
     * The accuracy test method for {@link CachedObject#getCacheSetName()}.
     * </p>
     */
    @Test
    public void testGetCacheSetName() {
        instance.setCacheSetName("cacheSetName");
        assertEquals("The cacheSetName is incorrect.", "cacheSetName", instance.getCacheSetName());
    }

    /**
     * <p>
     * The accuracy test method for {@link CachedObject#getId()}.
     * </p>
     */
    @Test
    public void testGetId() {
        instance.setId("id");
        assertEquals("The id is incorrect.", "id", instance.getId());
    }

    /**
     * <p>
     * The accuracy test method for {@link CachedObject#getContent()}.
     * </p>
     */
    @Test
    public void testGetContent() {
        byte[] content = new byte[12];
        instance.setContent(content);
        assertEquals("The content is incorrect.", 12, instance.getContent().length);
        assertArrayEquals("the content is incorrect.", content, instance.getContent());
    }

    /**
     * <p>
     * The accuracy test method for {@link CachedObject#setId(String)}.
     * </p>
     */
    @Test
    public void testSetId() {
        instance.setId("id");
        assertEquals("The id is incorrect.", "id", instance.getId());
    }

    /**
     * <p>
     * The accuracy test method for {@link CachedObject#setCacheSetName(java.lang.String)}.
     * </p>
     */
    @Test
    public void testSetCacheSetName() {
        instance.setCacheSetName("cacheSetName");
        assertEquals("The cacheSetName is incorrect.", "cacheSetName", instance.getCacheSetName());
    }

    /**
     * <p>
     * The accuracy test method for {@link CachedObject#setContent(byte[])}.
     * </p>
     */
    @Test
    public void testSetContent() {
        byte[] content = new byte[12];
        content[1] = 1;
        instance.setContent(content);
        assertArrayEquals("the content is incorrect.", content, instance.getContent());
    }

}
