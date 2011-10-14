/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import java.io.PrintWriter;

import com.ibm.support.electronic.cache.model.CacheStatistics;


/**
 * <p>
 * Abstract implementation of CacheStatisticsPrinter for testing.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public abstract class AbstractCacheStatisticsPrinter implements CacheStatisticsPrinter {
    /**
     * <p>
     * Creates a new instance.
     * </p>
     */
    public AbstractCacheStatisticsPrinter() {
    }

    /**
     * <p>
     * Does nothing.
     * </p>
     *
     * @param cacheStatistics not used.
     * @param printWriter not used.
     */
    public void printStatistics(CacheStatistics cacheStatistics, PrintWriter printWriter) {
    }
}
