/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all accuracy test cases.
 * </p>
 *
 * @author KLW
 * @version 1.0
 */
public class AccuracyTests extends TestCase {
    /**
     * <p>
     * Aggregates all accuracy test cases.
     * </p>
     *
     * @return All accuracy test cases.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(ObjectCacheConfigurationExceptionAccuracyTest.class);
        suite.addTestSuite(ObjectCacheExceptionAccuracyTest.class);
        suite.addTestSuite(PersistenceExceptionAccuracyTest.class);
        suite.addTestSuite(StatisticsPrintingExceptionAccuracyTest.class);

        suite.addTestSuite(CachedObjectAccuracyTest.class);
        suite.addTestSuite(CacheStatisticsAccuracyTest.class);
        suite.addTestSuite(CacheStatisticsPrinterImplAccuracyTest.class);
        suite.addTestSuite(MemoryCacheImplAccuracyTest.class);
        suite.addTestSuite(CachedObjectDAOImplAccuracyTest.class);
        suite.addTestSuite(CacheManagerImplAccuracyTest.class);
        return suite;
    }
}
