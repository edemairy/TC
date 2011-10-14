/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.accuracytests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.ibm.support.electronic.cache.CacheStatisticsPrinter;
import com.ibm.support.electronic.cache.model.CacheStatistics;
import com.ibm.support.electronic.cache.printer.CacheStatisticsPrinterImpl;

/**
 * The accuracy test for class {@link CacheStatisticsPrinterImpl}.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CacheStatisticsPrinterImplAccuracyTest extends TestCase {

    /**
     * <p>
     * the instance for test.
     * </p>
     */
    private CacheStatisticsPrinterImpl instance;


    /**
     * <p>
     * the instance of CacheStatistics for test.
     * </p>
     */
    private CacheStatistics cacheStatistics;

    /**
     * <p>
     * set up the test environment.
     *
     * </p>
     */
    @Before
    public void setUp() {
        instance = new CacheStatisticsPrinterImpl();
        cacheStatistics = new CacheStatistics();
        Map<String, Integer> accessCountsById = new HashMap<String, Integer>();
        // set the property of cacheStatistics.
        accessCountsById.put("key1", 1);
        accessCountsById.put("key2", 2);
        cacheStatistics.setAccessCountsById(accessCountsById);
        cacheStatistics.setInMemoryItemCount(1);
        cacheStatistics.setMissCount(2);
        cacheStatistics.setPersistedItemCount(3);
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatisticsPrinterImpl#CacheStatisticsPrinterImpl()}.
     * </p>
     */
    @Test
    public void testCacheStatisticsPrinterImpl() {
        assertNotNull("The instance should not be null!.", instance);
        assertTrue("The instance type is incorrect.", instance instanceof CacheStatisticsPrinter);
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheStatisticsPrinterImpl#printStatistics(CacheStatistics, PrintWriter)} . No
     * exception should be thrown.
     *
     * @throws Exception
     *             if any error occurs.
     * </p>
     */
    @Test
    public void testPrintStatistics() throws Exception {
        // create an instance of PrintWriter.
        File logFile = new File("test_files/log.txt");
        PrintWriter printWriter = null;
        OutputStream out = null;
        try {
            out = new FileOutputStream(logFile);
            printWriter = new PrintWriter(out);

            instance.printStatistics(cacheStatistics, printWriter);
            printWriter.flush();

            // check the output.
            String[] expect = new String[] {"Number of items in the cache: 4", "Keys in the cache:",
                "Key key1 is accessed 1 times.", "Key key2 is accessed 2 times.", "Number of items in memory: 1",
                "Number of items in persistent storage: 3", "Number of misses: 2" };
            AccuracyTestHelper.assertFileContents("test_files/log.txt", expect);
        } catch (IOException e) {
            // ignore
        } finally {
            printWriter.close();
            AccuracyTestHelper.closeOutputStream(out);
            logFile.delete();
        }
    }

}
