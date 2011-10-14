/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import java.util.Properties;


/**
 * <p>
 * Mock implementation of CachedObjectDAO that always throws exception when instantiated.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MockErrorDAO extends AbstractCachedObjectDAO {
    /**
     * Creates a new instance.
     *
     * @param prop not used
     * @throws ObjectCacheConfigurationException in any case.
     */
    public MockErrorDAO(Properties prop) {
        super(prop);
        throw new ObjectCacheConfigurationException("Always thrown for test");
    }
}
