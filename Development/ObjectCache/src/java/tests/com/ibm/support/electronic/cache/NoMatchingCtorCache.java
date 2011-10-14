/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import com.ibm.support.electronic.cache.model.CachedObject;


/**
 * <p>
 * Mock implementation of MemoryCache that does not have constructor with one Properties parameter.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class NoMatchingCtorCache implements MemoryCache {
    /**
     * Creates a new instance.
     */
    public NoMatchingCtorCache() {
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
