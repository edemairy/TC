/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.ibm.support.electronic.cache.cachemanager.CacheManagerImplConstructorTest;
import com.ibm.support.electronic.cache.cachemanager.CacheManagerImplTest;
import com.ibm.support.electronic.cache.dao.CachedObjectDAOImplTest;
import com.ibm.support.electronic.cache.memorycache.MemoryCacheImplTest;
import com.ibm.support.electronic.cache.model.CacheStatisticsTest;
import com.ibm.support.electronic.cache.model.CachedObjectTest;
import com.ibm.support.electronic.cache.printer.CacheStatisticsPrinterImplTest;


/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UnitTests extends TestCase {
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // unit tests for classes under package 'com.ibm.support.electronic.cache'
        suite.addTest(HelperTest.suite());
        suite.addTest(ObjectCacheConfigurationExceptionTest.suite());
        suite.addTest(ObjectCacheExceptionTest.suite());
        suite.addTest(PersistenceExceptionTest.suite());
        suite.addTest(StatisticsPrintingExceptionTest.suite());

        // unit tests for classes under package 'com.ibm.support.electronic.cache.cachemanager'
        suite.addTest(CacheManagerImplConstructorTest.suite());
        suite.addTest(CacheManagerImplTest.suite());

        // unit tests for classes under package 'com.ibm.support.electronic.cache.dao'
        suite.addTest(CachedObjectDAOImplTest.suite());

        // unit tests for classes under package 'com.ibm.support.electronic.cache.memorycache'
        suite.addTest(MemoryCacheImplTest.suite());

        // unit tests for classes under package 'com.ibm.support.electronic.cache.model'
        suite.addTest(CachedObjectTest.suite());
        suite.addTest(CacheStatisticsTest.suite());

        // unit tests for classes under package 'com.ibm.support.electronic.cache.printer'
        suite.addTest(CacheStatisticsPrinterImplTest.suite());

        // demo
        suite.addTest(Demo.suite());
        return suite;
    }
}
