/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import java.util.Properties;


/**
 * <p>
 * Mock implementation of MemoryCache that always throws exception when instantiated.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MockErrorCache extends AbstractMemoryCache {
    /**
     * Creates a new instance.
     *
     * @param prop not used.
     * @throws ObjectCacheConfigurationException in any case.
     */
    public MockErrorCache(Properties prop) {
        super(prop);
        throw new ObjectCacheConfigurationException("Always thrown for test only");
    }
}
