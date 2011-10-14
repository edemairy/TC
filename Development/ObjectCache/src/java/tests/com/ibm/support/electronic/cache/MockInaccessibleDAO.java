/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import java.util.Properties;


/**
 * <p>
 * Mock implementation of CachedObjectDAO that its constructor can not be accessed.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MockInaccessibleDAO extends AbstractCachedObjectDAO {
    /**
     * Creates new instance.
     *
     * @param prop not used.
     */
    private MockInaccessibleDAO(Properties prop) {
        super(prop);
    }
}
