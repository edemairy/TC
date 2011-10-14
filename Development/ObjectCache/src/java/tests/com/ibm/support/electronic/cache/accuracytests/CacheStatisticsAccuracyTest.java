/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.accuracytests;



import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.ibm.support.electronic.cache.model.CacheStatistics;


/**
 * The accuracy test for class {@link CacheStatistics}.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CacheStatisticsAccuracyTest extends TestCase {

    /**
     * <p>
     * the instance to test.
     * </p>
     */
    private CacheStatistics instance;

    /**
     * <p>
     * set up the test environment.
     *
     * </p>
     */
    @Before
    public void setUp() {
        instance = new CacheStatistics();
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#CacheStatistics()}.
     * </p>
     */
    @Test
    public void testCacheStatistics() {
        assertNotNull("The instance should not be null!.", instance);
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#getAccessCountsById()}.
     * </p>
     */
    @Test
    public void testGetAccessCountsById() {
        Map<String, Integer> accessCountsById = new HashMap<String, Integer>();
        instance.setAccessCountsById(accessCountsById);
        assertTrue("The accessCountsById is incorrect.", instance.getAccessCountsById().isEmpty());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#getMissCount()}.
     * </p>
     */
    @Test
    public void testGetMissCount() {
        instance.setMissCount(12);
        assertEquals("The missCount is incorrect.", 12, instance.getMissCount());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#getPersistedItemCount()}.
     * </p>
     */
    @Test
    public void testGetPersistedItemCount() {
        instance.setPersistedItemCount(123);
        assertEquals("The persistedItemCount is incorrect.", 123, instance.getPersistedItemCount());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#getInMemoryItemCount()}.
     * </p>
     */
    @Test
    public void testGetInMemoryItemCount() {
        instance.setInMemoryItemCount(123);
        assertEquals("The memoryItemCount is incorrect.", 123, instance.getInMemoryItemCount());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#setAccessCountsById(java.util.Map)}.
     * </p>
     */
    @Test
    public void testSetAccessCountsById() {
        Map<String, Integer> accessCountsById = new HashMap<String, Integer>();
        instance.setAccessCountsById(accessCountsById);
        assertTrue("The accessCountsById is incorrect.", instance.getAccessCountsById().isEmpty());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#setMissCount(int)}.
     * </p>
     */
    @Test
    public void testSetMissCount() {
        instance.setMissCount(12);
        assertEquals("The miss count is incorrect", 12, instance.getMissCount());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#setPersistedItemCount(int)}.
     * </p>
     */
    @Test
    public void testSetPersistedItemCount() {
        instance.setPersistedItemCount(123);
        assertEquals("The persistedItemCount is incorrect.", 123, instance.getPersistedItemCount());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatistics#setInMemoryItemCount(int)}.
     * </p>
     */
    @Test
    public void testSetInMemoryItemCount() {
        instance.setInMemoryItemCount(123);
        assertEquals("The memoryItemCount is incorrect.", 123, instance.getInMemoryItemCount());
    }

}
