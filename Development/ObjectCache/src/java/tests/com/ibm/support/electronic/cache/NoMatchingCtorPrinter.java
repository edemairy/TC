/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import java.io.PrintWriter;

import com.ibm.support.electronic.cache.model.CacheStatistics;


/**
 * <p>
 * Mock implementation of CacheStatisticsPrinter that does not have no argument constructor.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class NoMatchingCtorPrinter implements CacheStatisticsPrinter {
    /**
     * Creates a new instance.
     *
     * @param name not used.
     */
    public NoMatchingCtorPrinter(String name) {
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
