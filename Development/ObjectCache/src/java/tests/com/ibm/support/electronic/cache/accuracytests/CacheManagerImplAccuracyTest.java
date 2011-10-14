/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.accuracytests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.support.electronic.cache.CacheManager;
import com.ibm.support.electronic.cache.cachemanager.CacheManagerImpl;


/**
 * The accuracy test for class {@link CacheManagerImpl}.
 *
 * @author KLW
 * @version 1.0
 */
public class CacheManagerImplAccuracyTest extends TestCase {

    /**
     * <p>
     * the instance for test.
     * </p>
     */
    private CacheManagerImpl manager;

    /**
     * <p>
     * the properties for test.
     * </p>
     */
    private Properties prop;
    /**
     * <p>
     * the Serializable object for test.
     * </p>
     */
    private Foo foo;

    /**
     * <p>
     * set up the test environment.
     * </p>
     *
     * @throws Exception if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        prop = AccuracyTestHelper.getProperties();
        manager = new CacheManagerImpl(prop);

        foo = new Foo();
        foo.setAge(123);
        foo.setName("foo");
    }

    /**
     * <p>
     * tear down the test environment.
     * </p>
     *
     * @throws Exception if any error occurs.
     */
    @After
    public void tearDown() throws Exception {
        manager.clear();
    }

    /**
     * <p>
     * The accuracy test method for {@link CacheManagerImpl#CacheManagerImpl()}.
     * </p>
     */
    @Test
    public void testCacheManagerImpl() {
        assertNotNull("The manager should not be null!.", new CacheManagerImpl());
    }

    /**
     * <p>
     * The accuracy test method for {@link CacheManagerImpl#CacheManagerImpl(String)}.
     * </p>
     */
    @Test
    public void testCacheManagerImplString() {
        manager = new CacheManagerImpl("test_files/accuracytests/config.properties");
        assertNotNull("The manager should not be null!.", manager);
    }

    /**
     * <p>
     * The accuracy test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, String)}.
     * </p>
     *
     * @throws Exception if any exception occurs.
     */
    @Test
    public void testCacheManagerImplPropertiesString() throws Exception {
        manager = new CacheManagerImpl(prop, "cacheSetName2");
        assertNotNull("The manager should not be null!.", manager);
        AccuracyTestHelper.assertFieldValue("cacheSetName", manager, "cacheSetName2");
    }

    /**
     * <p>
     * The accuracy test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)}.
     * </p>
     */
    @Test
    public void testCacheManagerImplProperties() {
        assertNotNull("The manager should not be null!.", manager);
        assertTrue("The manager type is incorrect.", manager instanceof CacheManager);
    }

    /**
     * <p>
     * The accuracy test method for {@link CacheManagerImpl#printStatistics(java.io.PrintWriter)}.
     * </p>
     *
     * @throws Exception if any error occurs.
     */
    @Test
    public void testPrintStatistics() throws Exception {
        manager.get("djfka;l");
        // create an manager of PrintWriter.
        File logFile = new File("test_files/log2.txt");
        PrintWriter printWriter = null;
        OutputStream out = null;
        try {
            out = new FileOutputStream(logFile);
            printWriter = new PrintWriter(out);

            manager.printStatistics(printWriter);
            printWriter.flush();

            // check the output.
            String[] expect = new String[] {"Number of items in the cache: 0", "Number of misses: 1"};
            AccuracyTestHelper.assertFileContents("test_files/log2.txt", expect);
        } catch (IOException e) {
            // ignore
        } finally {
            printWriter.close();
            AccuracyTestHelper.closeOutputStream(out);
            logFile.delete();
        }
    }

    /**
     * <p>
     * The accuracy test method for {@link CacheManagerImpl#get(String)}.
     * </p>
     *
     * @throws Exception if any error occurs.
     */
    @Test
    public void testGet() throws Exception {
        manager.put("key", foo);
        AccuracyTestHelper.assertFoo(foo, manager.get("key"));
    }

    /**
     * <p>
     * The accuracy test method for {@link CacheManagerImpl#get(String)}. the id is null. result should be null. No
     * Exception should be thrown.
     * </p>
     *
     * @throws Exception if any error occurs.
     */
    @Test
    public void testGet_null() throws Exception {
        assertNull("Null should be returned.", manager.get(null));
    }

    /**
     * <p>
     * The failure test method for {@link CacheManagerImpl#get(String)}. the id is empty. result should be null. No
     * Exception should be thrown.
     * </p>
     *
     * @throws Exception if any error occurs.
     */
    @Test
    public void testGet_empty() throws Exception {
        assertNull("Null should be returned.", manager.get(" "));
    }

    /**
     * <p>
     * The accuracy test method for {@link CacheManagerImpl#put(String, java.io.Serializable)}. put manager of Foo
     * into memory. No exception should be thrown.
     * </p>
     *
     * @throws Exception if any error occurs.
     */
    @Test
    public void testPut() throws Exception {
        manager.put("key", foo);
        AccuracyTestHelper.assertFoo(foo, manager.get("key"));

        // put again
        manager.put("key", foo);
        AccuracyTestHelper.assertFoo(foo, manager.get("key"));

        manager.put("key", foo);
        manager.clear();
    }

    /**
     * <p>
     * The accuracy test method for {@link CacheManagerImpl#put(String, java.io.Serializable)}. put manager of Foo
     * into data base. No exception should be thrown.
     * </p>
     *
     * @throws Exception if any error occurs.
     */
    @Test
    public void testPutInDb() throws Exception {
        manager.put("key1", foo);
        manager.put("key2", foo);

        Foo otherFoo = new Foo();
        otherFoo.setAge(12);
        otherFoo.setName("dfa");
        manager.put("key3", otherFoo);
        AccuracyTestHelper.assertFoo(otherFoo, manager.get("key3"));
        AccuracyTestHelper.assertFoo(foo, manager.get("key1"));
        manager.clear();
    }

    /**
     * <p>
     * The accuracy test method for {@link CacheManagerImpl#clear()}.
     * </p>
     *
     * @throws Exception if any error occurs.
     */
    @Test
    public void testClear() throws Exception {
        manager.clear();
        assertNull("the result should be null.", manager.get("key1"));
    }

    /**
     * <p>
     * The accuracy test method for {@link CacheManagerImpl#getCacheStatistics()}.
     * </p>
     *
     * @throws Exception if any error occurs.
     */
    @Test
    public void testGetCacheStatistics() throws Exception {
        manager.put("key", foo);
        manager.put("key2", foo);
        assertEquals("The InMemoryItemCount is incorrect. ", 2, manager.getCacheStatistics()
            .getInMemoryItemCount());

        manager.get("not exist");
        manager.get("not exist");
        assertEquals("The MissCount is incorrect.", 2, manager.getCacheStatistics().getMissCount());
    }
}
