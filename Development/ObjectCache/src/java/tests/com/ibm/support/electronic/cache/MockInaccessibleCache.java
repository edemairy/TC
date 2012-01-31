/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import java.util.Properties;


/**
 * <p>
 * Mock implementation of CacheStatisticsPrinter that its constructor can not be accessed.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MockInaccessibleCache extends AbstractMemoryCache {
    /**
     * Creates new instance.
     *
     * @param prop not used.
     */
    private MockInaccessibleCache(Properties prop) {
        super(prop);
    }
}
