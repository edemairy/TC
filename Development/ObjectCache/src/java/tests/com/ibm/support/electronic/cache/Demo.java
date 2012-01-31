/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import java.io.PrintWriter;
import java.util.Properties;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ibm.support.electronic.cache.cachemanager.CacheManagerImpl;
import com.ibm.support.electronic.cache.model.CacheStatistics;


/**
 * <p>
 * Test case that demonstrates the usage of this component.
 * </p>
 *
 * @author mekanizumu, TCSDEVELOPER
 * @version 1.0
 */
public class Demo {
    /**
     * <p>
     * The CacheManagerImpl instance for demo.
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
        return new JUnit4TestAdapter(Demo.class);
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
        manager = new CacheManagerImpl(TestHelper.CONFIG_PROPERTIES);
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
    }

    /**
     * <p>
     * Demonstrates the creation of CacheManager instance.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void demoManagerCreation() throws Exception {
        // Create a CacheManagerImpl with default configuration file
        try {
            manager = new CacheManagerImpl();
        } catch (ObjectCacheConfigurationException e) {
            // will fail in this demo
        }

        // Create a CacheManagerImpl with a given configuration file
        manager = new CacheManagerImpl(TestHelper.CONFIG_PROPERTIES);

        // Assuming this Properties object contains configuration parameters
        Properties prop = TestHelper.loadProperties(TestHelper.CONFIG_PROPERTIES);

        // Create a CacheManagerImpl with a given Properties object
        manager = new CacheManagerImpl(prop);

        // Create a CacheManagerImpl with a given Properties object and a cache set name
        manager = new CacheManagerImpl(prop, "Set 1");
    }

    /**
     * <p>
     * Demonstrates the API usage.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void demoAPIUsage() throws Exception {
        // First clear the cache to delete any possible old data in persistence storage
        manager.clear();

        // Get an object that doesn't exist in the cache
        Object object = manager.get("fooA");

        // It should be null
        Assert.assertNull(object);

        // Create a Foo
        Foo foo1 = TestHelper.createFoo("fooA", 10);

        // Put it into the cache (this one will go to memory cache)
        manager.put("fooA", foo1);

        // Get it from cache
        Foo fooFromCache = (Foo) manager.get("fooA");

        // Its values should be the same as foo1
        TestHelper.assertFooEquals(foo1, fooFromCache);

        // Create another Foo
        Foo foo2 = TestHelper.createFoo("fooB", 20);

        // Put it into the cache (this one will go to memory cache)
        manager.put("fooB", foo2);

        // Get it from cache
        fooFromCache = (Foo) manager.get("fooB");

        // Its values should be the same as foo2
        TestHelper.assertFooEquals(foo2, fooFromCache);

        // Create a PrintWriter
        PrintWriter pw = new PrintWriter(System.out);

        // Print the current statistics
        manager.printStatistics(pw);
        // The following content will be printed:
        // Number of items in the cache: 2
        // Keys in the cache:
        // Key fooB is accessed 1 times.
        // Key fooA is accessed 1 times.
        // Number of items in memory: 2
        // Number of items in persistent storage: 0
        // Number of misses: 1

        // The one miss is caused by "manager.get("fooA")" at the beginning of this demo.

        // Create another Foo
        Foo foo3 = new Foo();
        foo3.setAge(30);
        foo3.setName("fooC");
        // This one will go directly to database because the 3 cached Foo require 1138 * 3 bytes, which is
        // larger than the configured 3K in-memory cache
        manager.put("fooC", foo3);
        // At this point, the table CACHED_OBJECT will have a row with id=fooC and
        // cache_set_name=default
        // Now accessing fooC will force the least recently used in-memory object fooA to be removed from
        // memory cache (because manager uses MemoryCacheImpl, which uses Least Recently Used
        // replacement strategy) and put to database, and table CACHED_OBJECT will have a new row with
        // id=fooA and cache_set_name=default. Note that because fooC is moved from database to memory
        // cache, its corresponding row is deleted. So at this time, CACHED_OBJECT only has one row (for
        // fooA)

        fooFromCache = (Foo) manager.get("fooC");

        // Print the current statistics
        manager.printStatistics(pw);
        // Remember to close the print writer when it's no longer in use
        pw.close();

        // The following content will be printed:
        // Number of items in the cache: 3
        // Keys in the cache:
        // Key fooB is accessed 1 times.
        // Key fooC is accessed 1 times.
        // Key fooA is accessed 1 times.
        // Number of items in memory: 2
        // Number of items in persistent storage: 1
        // Number of misses: 1
        // We can also get a CacheStatistics instead of printing it out
        CacheStatistics statistics = manager.getCacheStatistics();

        // Accessing its getter to get details
        System.out.println(statistics.getInMemoryItemCount());
        // We can remove one particular cached object by setting it to null (the associated table row will be
        // deleted)
        manager.put("fooA", null);
        Assert.assertNull(manager.get("fooA"));

        // We can replace a cached object with another. Note that replacing it with the same key will result in
        // the statistics for that key be reset. It's like it's a brand-new key.
        manager.put("fooB", foo1);
        fooFromCache = (Foo) manager.get("fooB");

        // Now the retrieved Foo's values are the same as those of foo1
        TestHelper.assertFooEquals(foo1, fooFromCache);

        // Clear the cache set (the associated table rows will be deleted)
        manager.clear();

        // After clearing the cache, there's nothing in the cache
        Assert.assertNull(manager.get("fooB"));
        Assert.assertNull(manager.get("fooC"));
    }
}
