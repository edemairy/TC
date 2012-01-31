/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.cachemanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.ibm.support.electronic.cache.CacheStatisticsPrinter;
import com.ibm.support.electronic.cache.CachedObjectDAO;
import com.ibm.support.electronic.cache.Foo;
import com.ibm.support.electronic.cache.PersistenceException;
import com.ibm.support.electronic.cache.StatisticsPrintingException;
import com.ibm.support.electronic.cache.TestHelper;
import com.ibm.support.electronic.cache.model.CacheStatistics;


/**
 * <p>
 * Unit test case of methods of {@link CacheManagerImpl}.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CacheManagerImplTest {
    /**
     * <p>
     * The Properties instance used for testing.
     * </p>
     */
    private Properties prop;

    /**
     * <p>
     * The CacheManagerImpl instance to test against.
     * </p>
     */
    private CacheManagerImpl manager;

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CacheManagerImplTest.class);
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
        prop = TestHelper.loadProperties(TestHelper.CONFIG_PROPERTIES);
        prop.setProperty("maxMemorySize", "1");
        manager = new CacheManagerImpl(prop);
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
        manager = null;
        prop = null;
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#printStatistics(java.io.PrintWriter)}.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testPrintStatistics() throws Exception {
        // put some objects
        manager.put("1", "test");
        manager.put("2", TestHelper.createFoo("foo", 10));

        // access an object
        manager.get("1");
        manager.get("3");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        manager.printStatistics(pw);

        // verify the result
        BufferedReader reader = new BufferedReader(new StringReader(sw.toString()));
        try {
            Assert.assertEquals("Incorrect total item line", "Number of items in the cache: 2", reader.readLine());
            Assert.assertEquals("Incorrect keys line", "Keys in the cache:", reader.readLine());
            Assert.assertEquals("Incorrect key access line", "Key 2 is accessed 0 times.", reader.readLine());
            Assert.assertEquals("Incorrect key access line", "Key 1 is accessed 1 times.", reader.readLine());
            Assert.assertEquals("Incorrect memory count line", "Number of items in memory: 1", reader.readLine());
            Assert.assertEquals("Incorrect persisted count line", "Number of items in persistent storage: 1",
                reader.readLine());
            Assert.assertEquals("Incorrect miss count line", "Number of misses: 1", reader.readLine());
            Assert.assertNull("Should be the end of printed stats", reader.readLine());
        } catch (IOException e) {
            // never happen since we read from string
        }

        TestHelper.clearDB();
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#printStatistics(java.io.PrintWriter)} when fails to print
     * the statistics.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = StatisticsPrintingException.class)
    public void testPrintStatistics_PrintingFail1() throws Exception {
        CacheStatisticsPrinter mockPrinter = Mockito.mock(CacheStatisticsPrinter.class);
        Mockito.doThrow(new StatisticsPrintingException("For test only")).when(mockPrinter)
            .printStatistics(Mockito.any(CacheStatistics.class), Mockito.any(PrintWriter.class));

        Field printerField = manager.getClass().getDeclaredField("cacheStatisticsPrinter");
        printerField.setAccessible(true);
        printerField.set(manager, mockPrinter);

        manager.printStatistics(new PrintWriter(new StringWriter()));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#printStatistics(java.io.PrintWriter)} when printWriter is
     * null.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPrintStatistics_NullPrintWriter() throws Exception {
        manager.printStatistics(null);
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#get(java.lang.String)} when id is null.
     * </p>
     */
    @Test
    public void testGet1() {
        Assert.assertNull("Should not exist if id is null", manager.get(null));
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#get(java.lang.String)} when id is empty.
     * </p>
     */
    @Test
    public void testGet2() {
        Assert.assertNull("Should not exist if id is empty", manager.get(" "));
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#get(java.lang.String)} when the object is not cached
     * neither in memory nor database.
     * </p>
     */
    @Test
    public void testGet3() {
        Assert.assertNull("Should not exist both in memory and database", manager.get("unknown"));
        CacheStatistics stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect miss count", 1, stats.getMissCount());
        Assert.assertEquals("Incorrect in memory count", 0, stats.getInMemoryItemCount());
        Assert.assertEquals("Incorrect persisted count", 0, stats.getPersistedItemCount());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#get(java.lang.String)} when the object is not found in
     * memory but exists in database.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testGet4() throws Exception {
        String id = "get4";
        Foo object = TestHelper.createFoo("foo", 10);

        // put object that too large to put into memory so it will be persisted to database
        manager.put(id, object);

        CacheStatistics stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect persisted count", 1, stats.getPersistedItemCount());
        Assert.assertEquals("Incorrect access counts", 0, stats.getAccessCountsById().get(id).intValue());

        // get the object from the cache
        Serializable cached = manager.get(id);
        Assert.assertTrue("Incorrect type of cached object", cached instanceof Foo);
        TestHelper.assertFooEquals(object, (Foo) cached);

        stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect miss count", 0, stats.getMissCount());
        Assert.assertEquals("Incorrect in-memory count", 0, stats.getInMemoryItemCount());
        Assert.assertEquals("Incorrect access counts", 1, stats.getAccessCountsById().get(id).intValue());

        TestHelper.clearDB();
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#get(java.lang.String)} when the object is found in memory.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testGet5() throws Exception {
        String id = "get5";
        String object = "test";
        manager.put(id, object);

        CacheStatistics stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect access counts", 0, stats.getAccessCountsById().get(id).intValue());

        // get the object from the cache
        Serializable cached = manager.get(id);
        Assert.assertTrue("Incorrect type of cached object", cached instanceof String);
        Assert.assertEquals("Incorrect object", object, cached);

        stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect miss count", 0, stats.getMissCount());
        Assert.assertEquals("Incorrect in-memory count", 1, stats.getInMemoryItemCount());
        Assert.assertEquals("Incorrect persisted count", 0, stats.getPersistedItemCount());
        Assert.assertEquals("Incorrect access counts", 1, stats.getAccessCountsById().get(id).intValue());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#get(java.lang.String)} when the object is not found in
     * memory and exists in database. Accessing the object will force the least recently used object in memory
     * cache is placed in database.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testGet6() throws Exception {
        byte[] object1 = new byte[8];
        Arrays.fill(object1, (byte) 0);
        manager.put("obj1", object1);
        CacheStatistics stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect in-memory count", 1, stats.getInMemoryItemCount());

        byte[] object2 = new byte[990];
        Arrays.fill(object2, (byte) 1);
        manager.put("obj2", object2);

        stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect persisted count", 1, stats.getPersistedItemCount());

        // get the object from the cache
        Serializable cached = manager.get("obj2");
        Assert.assertTrue("Incorrect type of cached object", cached instanceof byte[]);
        Assert.assertTrue("Incorrect object2", Arrays.equals(object2, (byte[]) cached));

        stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect access counts", 1, stats.getAccessCountsById().get("obj2").intValue());
        Assert.assertEquals("Incorrect miss count", 0, stats.getMissCount());

        Assert.assertEquals("Incorrect in-memory count", 1, stats.getInMemoryItemCount());

        // the object1 is moved out to the database
        Assert.assertEquals("Incorrect persisted count", 1, stats.getPersistedItemCount());

        TestHelper.clearDB();
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#get(java.lang.String)} when there is persistence error
     * while getting the object from database.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testGet7() throws Exception {
        CachedObjectDAO mockDao = Mockito.mock(CachedObjectDAO.class);
        Mockito.when(mockDao.get(Mockito.anyString(), Mockito.anyString())).thenThrow(
            new PersistenceException("For test only"));

        String id = "get4";
        Foo object = TestHelper.createFoo("foo", 10);

        // put object that too large to put into memory so it will be persisted to database
        manager.put(id, object);

        CacheStatistics stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect persisted count", 1, stats.getPersistedItemCount());
        Assert.assertEquals("Incorrect access counts", 0, stats.getAccessCountsById().get(id).intValue());

        Field daoField = manager.getClass().getDeclaredField("cachedObjectDAO");
        daoField.setAccessible(true);
        daoField.set(manager, mockDao);

        Assert.assertNull("Should not exist since there is persistence error", manager.get(id));
        stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect in memory count", 0, stats.getInMemoryItemCount());
        Assert.assertEquals("Incorrect persisted count", 1, stats.getPersistedItemCount());
        Assert.assertEquals("Incorrect access counts", 1, stats.getAccessCountsById().get(id).intValue());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#put(java.lang.String, java.io.Serializable)} when the
     * object to cache is to big to put into memory. The object will go directly to database.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testPut1() throws Exception {
        String id = "put1";
        Foo object = TestHelper.createFoo("foo", 10);
        manager.put(id, object);

        CacheStatistics stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect persisted count", 1, stats.getPersistedItemCount());
        Assert.assertEquals("Incorrect access counts", 0, stats.getAccessCountsById().get(id).intValue());

        // get the object from the cache
        Serializable cached = manager.get(id);
        Assert.assertTrue("Incorrect type of cached object", cached instanceof Foo);
        TestHelper.assertFooEquals(object, (Foo) cached);

        stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect miss count", 0, stats.getMissCount());
        Assert.assertEquals("Incorrect in-memory count", 0, stats.getInMemoryItemCount());
        Assert.assertEquals("Incorrect access counts", 1, stats.getAccessCountsById().get(id).intValue());

        TestHelper.clearDB();
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#put(java.lang.String, java.io.Serializable)} when the
     * object to put is fit in memory.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testPut2() throws Exception {
        String id = "put1";
        String object = "test";
        manager.put(id, object);

        // get the object from the cache
        Serializable cached = manager.get(id);
        Assert.assertTrue("Incorrect type of cached object", cached instanceof String);
        Assert.assertEquals("Incorrect object", object, cached);

        CacheStatistics stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect miss count", 0, stats.getMissCount());
        Assert.assertEquals("Incorrect in-memory count", 1, stats.getInMemoryItemCount());
        Assert.assertEquals("Incorrect persisted count", 0, stats.getPersistedItemCount());
        Assert.assertEquals("Incorrect access counts", 1, stats.getAccessCountsById().get(id).intValue());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#put(java.lang.String, java.io.Serializable)} when the
     * object to put is already existed in the cache.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testPut3() throws Exception {
        String id = "put3";
        String object = "test";
        manager.put(id, object);

        // get the object from the cache
        Serializable cached = manager.get(id);
        Assert.assertTrue("Incorrect type of cached object", cached instanceof String);
        Assert.assertEquals("Incorrect object", object, cached);

        // put another object with same id
        String newObject = "updated";
        manager.put(id, newObject);
        cached = manager.get(id);
        Assert.assertTrue("Incorrect type of new cached object", cached instanceof String);
        Assert.assertEquals("Incorrect new object", newObject, cached);

        CacheStatistics stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect miss count", 0, stats.getMissCount());
        Assert.assertEquals("Incorrect in-memory count", 1, stats.getInMemoryItemCount());
        Assert.assertEquals("Incorrect persisted count", 0, stats.getPersistedItemCount());
        Assert.assertEquals("Incorrect access counts", 1, stats.getAccessCountsById().get(id).intValue());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#put(java.lang.String, java.io.Serializable)} when the
     * object in memory cache is going to be removed.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testPut4() throws Exception {
        String id = "put4";
        String object = "test";
        manager.put(id, object);

        CacheStatistics stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect in-memory count", 1, stats.getInMemoryItemCount());

        // get the object from the cache
        Serializable cached = manager.get(id);
        Assert.assertTrue("Incorrect type of cached object", cached instanceof String);
        Assert.assertEquals("Incorrect object", object, cached);

        // remove the object
        manager.put(id, null);
        Assert.assertNull("Should be removed", manager.get(id));

        stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect miss count", 1, stats.getMissCount());
        Assert.assertEquals("Incorrect in-memory count", 0, stats.getInMemoryItemCount());
        Assert.assertEquals("Incorrect persisted count", 0, stats.getPersistedItemCount());
        Assert.assertNull("Incorrect access counts", stats.getAccessCountsById().get(id));
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#put(java.lang.String, java.io.Serializable)} when the
     * object in database is going to be removed.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testPut5() throws Exception {
        String id = "put5";
        Foo object = TestHelper.createFoo("foo", 10);
        manager.put(id, object);
        CacheStatistics stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect persisted count", 1, stats.getPersistedItemCount());

        // get the object from the cache
        Serializable cached = manager.get(id);
        Assert.assertTrue("Incorrect type of cached object", cached instanceof Foo);
        TestHelper.assertFooEquals(object, (Foo) cached);

        // remove the object
        manager.put(id, null);
        Assert.assertNull("Should be removed", manager.get(id));

        stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect miss count", 1, stats.getMissCount());
        Assert.assertEquals("Incorrect in-memory count", 0, stats.getInMemoryItemCount());
        Assert.assertEquals("Incorrect persisted count", 0, stats.getPersistedItemCount());
        Assert.assertNull("Incorrect access counts", stats.getAccessCountsById().get(id));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#put(java.lang.String, java.io.Serializable)} when id is
     * null.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPut_NullId() throws Exception {
        manager.put(null, "test");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#put(java.lang.String, java.io.Serializable)} when id is
     * empty.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPut_EmptyId() throws Exception {
        manager.put("  ", "test");
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#clear()}.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testClear() throws Exception {
        // put some objects
        manager.put("1", "test");
        manager.put("2", TestHelper.createFoo("foo", 10));

        CacheStatistics stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect in-memory count", 1, stats.getInMemoryItemCount());
        Assert.assertEquals("Incorrect persisted count", 1, stats.getPersistedItemCount());

        // clear
        manager.clear();
        stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect in-memory count after clear", 0, stats.getInMemoryItemCount());
        Assert.assertEquals("Incorrect persisted count after clear", 0, stats.getPersistedItemCount());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#getCacheStatistics()} when there has been no access to the
     * cache yet.
     * </p>
     */
    @Test
    public void testGetCacheStatistics1() {
        CacheStatistics stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect miss count", 0, stats.getMissCount());
        Assert.assertEquals("Incorrect in-memory count", 0, stats.getInMemoryItemCount());
        Assert.assertEquals("Incorrect persisted count", 0, stats.getPersistedItemCount());
        Assert.assertTrue("Incorrect access counts", stats.getAccessCountsById().isEmpty());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#getCacheStatistics()} when cache is already used.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testGetCacheStatistics2() throws Exception {
        manager.put("1", "test");
        manager.put("2", TestHelper.createFoo("foo", 10));

        // access an object
        manager.get("1");
        manager.get("3");

        CacheStatistics stats = manager.getCacheStatistics();
        Assert.assertEquals("Incorrect miss count", 1, stats.getMissCount());
        Assert.assertEquals("Incorrect in-memory count", 1, stats.getInMemoryItemCount());
        Assert.assertEquals("Incorrect persisted count", 1, stats.getPersistedItemCount());
        Map<String, Integer> accessCountsById = stats.getAccessCountsById();
        Assert.assertEquals("Incorrect number of access counts", 2, accessCountsById.size());
        Assert.assertEquals("Incorrect access count-1", 1, accessCountsById.get("1").intValue());
        Assert.assertEquals("Incorrect access count-2", 0, accessCountsById.get("2").intValue());

        manager.clear();
    }
}
