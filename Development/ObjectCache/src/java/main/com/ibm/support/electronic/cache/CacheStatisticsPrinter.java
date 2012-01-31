package com.ibm.support.electronic.cache;

import com.ibm.support.electronic.cache.model.*;
import java.io.*;

/**
 * This interface defines the contract for printing cache statistics to a print writer. Implementations should have a 0-argument constructor.
 * Thread Safety:
 * The implementation of this interface does not have to be thread-safe
 */
public interface CacheStatisticsPrinter {
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
     * @param printWriter 
     * @param cacheStatistics 
     */
    public void printStatistics(CacheStatistics cacheStatistics, PrintWriter printWriter);
}

