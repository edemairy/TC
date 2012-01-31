/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import java.util.Properties;

import com.ibm.support.electronic.cache.model.CachedObject;


/**
 * <p>
 * Abstract implementation of MemoryCache for testing.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public abstract class AbstractMemoryCache implements MemoryCache {
    /**
     * <p>
     * Creates a new instance.
     * </p>
     *
     * @param prop not used.
     */
    public AbstractMemoryCache(Properties prop) {
    }

    /**
     * <p>
     * Does nothing. Simply returns null.
     * </p>
     *
     * @return null.
     */
    public CachedObject getReplaceableCachedObject() {
        return null;
    }

    /**
     * <p>
     * Does nothing. Simply returns null.
     * </p>
     *
     * @param id not used.
     * @return null.
     */
    public CachedObject get(String id) {
        return null;
    }

    /**
     * <p>
     * Does nothing.
     * </p>
     *
     * @param id not used.
     * @param cachedObject not used.
     */
    public void put(String id, CachedObject cachedObject) {
    }

    /**
     * <p>
     * Does nothing.
     * </p>
     */
    public void clear() {
    }

    /**
     * <p>
     * Does nothing. Simply returns null.
     * </p>
     *
     * @param id not used.
     * @return null.
     */
    public CachedObject remove(String id) {
        return null;
    }
}
