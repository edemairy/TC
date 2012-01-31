/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import java.util.Properties;

import com.ibm.support.electronic.cache.model.CachedObject;


/**
 * <p>
 * Abstract implementation of CachedObjectDAO for testing.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public abstract class AbstractCachedObjectDAO implements CachedObjectDAO {
    /**
     * <p>
     * Creates a new instance.
     * </p>
     *
     * @param prop not used.
     */
    public AbstractCachedObjectDAO(Properties prop) {
    }

    /**
     * <p>
     * Does nothing.
     * </p>
     *
     * @param cacheSetName not used.
     */
    public void deleteByCacheSet(String cacheSetName) {
    }

    /**
     * <p>
     * Does nothing. Simply returns null.
     * </p>
     *
     * @param cacheSetName not used.
     * @param id not used.
     * @return null.
     */
    public CachedObject get(String cacheSetName, String id) {
        return null;
    }

    /**
     * <p>
     * Does nothing.
     * </p>
     *
     * @param cachedObject not used.
     */
    public void save(CachedObject cachedObject) {
    }

    /**
     * <p>
     * Does nothing.
     * </p>
     *
     * @param cacheSetName not used.
     * @param id not used.
     */
    public void delete(String cacheSetName, String id) {
    }
}
