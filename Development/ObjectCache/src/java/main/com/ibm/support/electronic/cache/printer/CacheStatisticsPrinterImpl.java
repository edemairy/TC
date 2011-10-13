package com.ibm.support.electronic.cache.printer;

import com.ibm.support.electronic.cache.model.*;
import java.io.*;

/**
 * This is the default implementation of CacheStatisticsPrinter. It simply prints out the properties of the passed in CacheStatistics one by one using the passed in print writer.
 * Thread Safety:
 * This class is not thread-safe because the passed in parameters are not thread-safe.
 */
public class CacheStatisticsPrinterImpl {

    /**
     * This is the default constructor for the class.
     * 
     * Parameters:
     * None
     * 
     * Exception:
     * None
     * 
     * Implementation Notes:
     * Do nothing
     */
    public CacheStatisticsPrinterImpl() {
    }

    /**
     * Print the passed in cache statistics to the given print writer. This method will not close the print writer.
     * 
     * Parameters:
     * cacheStatistics - the statistics of the cache. 
     * printWriter - the print writer used to print statistics
     * 
     * Return:
     * None
     * 
     * Exception:
     * StatisticsPrintingException if any error occurs
     * IllegalArgumentException if
     *  cacheStatistics doesn't conform to the following legal value specification:
     *        It can't be null and all its properties must not be null but can be empty.
     *  printWriter is null;
     * 
     * Implementation Notes:
     * 1 printWriter.println("Number of items in the cache: " + (cacheStatistics.getInMemoryItemCount() + cacheStatistics.getPersistedItemCount()));
     * 2 printWriter.println("Keys in the cache:");
     * 3 For each String key in cacheStatistics.getAccessCountsById().keySet()
     *         printWriter.println("Key " + key + " is accessed " + cacheStatistics.getAccessCountsById().get(key) + " times.");
     * 4 printWriter.println("Number of items in memory: " + cacheStatistics.getInMemoryItemCount());
     * 5 printWriter.println("Number of items in persistent storage: " + cacheStatistics.getPersistedItemCount());
     * 6 printWriter.println("Number of misses: " + cacheStatistics.getMissCount());
     * @param printWriter 
     * @param cacheStatistics 
     */
    public void printStatistics(CacheStatistics cacheStatistics, PrintWriter printWriter) {
        printWriter.println("Number of items in the cache: " + (cacheStatistics.getInMemoryItemCount() + cacheStatistics.getPersistedItemCount()));
        printWriter.println("Keys in the cache:");
        for (String key : cacheStatistics.getAccessCountsById().keySet()) {


            printWriter.println("Key " + key + " is accessed " + cacheStatistics.getAccessCountsById().get(key) + " times.");
        }
        printWriter.println("Number of items in memory: " + cacheStatistics.getInMemoryItemCount());
        printWriter.println("Number of items in persistent storage: " + cacheStatistics.getPersistedItemCount());
        printWriter.println("Number of misses: " + cacheStatistics.getMissCount());
    }
}
