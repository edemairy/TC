/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

/**
 * <p>
 * Mock implementation of CacheStatisticsPrinter that always throws exception when instantiated.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MockErrorPrinter extends AbstractCacheStatisticsPrinter {
    /**
     * Creates a new instance.
     *
     * @throws ObjectCacheConfigurationException in any case.
     */
    public MockErrorPrinter() {
        throw new ObjectCacheConfigurationException("Always thrown for test only");
    }
}
