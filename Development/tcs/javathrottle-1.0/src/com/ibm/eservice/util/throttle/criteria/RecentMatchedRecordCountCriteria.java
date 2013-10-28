/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.eservice.util.throttle.criteria;

import java.util.Date;
import java.util.List;

import com.ibm.eservice.util.throttle.RecordMatchingCriteria;
import com.ibm.eservice.util.throttle.WorkQueueRecord;

/**
 * <p>
 * This class is an implementation of RecordMatchingCriteria that checks the number of records with the same ID that
 * were matched during some configured interval before the record timestamp. If the obtained number is less than the
 * configured maximum limit (maxCount), the current record is matched. Next this matched record will be used while
 * detecting whether future records with the same ID meet the requirements of these criteria. By default this criteria
 * class matches only one record with the same ID per hour. These default parameters can be modified via setters before
 * business method is called. This criteria class needs to store information about previously matched records to work
 * properly, thus it uses a pluggable RecentMatchedRecordCountCriteriaPersistence instance to save and retrieve such
 * data. The user must inject the persistence implementation instance via setter before using this class.
 * </p>
 * 
 * <p>
 * <strong>Thread Safety:</strong> This class is mutable and not thread safe. It uses
 * RecentMatchedRecordCountCriteriaPersistence interface that is also not thread safe.
 * </p>
 * 
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class RecentMatchedRecordCountCriteria implements RecordMatchingCriteria {
    /**
     * <p>
     * The time interval in milliseconds before the timestamp of the currently processed record used when counting the
     * recent matched records with the same ID. By default is equal to 1 hour. Cannot be not positive. Has a setter. Is
     * used in match().
     * </p>
     */
    private long interval = 3600000;

    /**
     * <p>
     * The maximum acceptable number of matched records with the same ID for the specified time interval. If this number
     * is exceeded including the currently processed record, the record is discarded. By default is equal to 1. Together
     * with the default value of interval this means that only one record with the same ID can be matched during any one
     * hour interval. Cannot be not positive. Has a setter. Is used in match().
     * </p>
     */
    private int maxCount = 1;

    /**
     * <p>
     * The persistence used for storing and retrieving information about IDs and timestamps of the previously matched
     * records. Is <code>null</code> when not yet initialized. Is expected to be injected via setter method before
     * match() method is used. Cannot be <code>null</code> after initialization. Is used in match().
     * </p>
     */
    private RecentMatchedRecordCountCriteriaPersistence persistence;

    /**
     * <p>
     * Creates an instance of RecentMatchedRecordCountCriteria.
     * </p>
     */
    public RecentMatchedRecordCountCriteria() {
        // Empty
    }

    /**
     * <p>
     * Checks whether the provided records are matched by this criteria class. The records are processed strictly in the
     * provided order.
     * </p>
     * 
     * @param records
     *            the records to be checked.
     * 
     * @return the check results (not <code>null</code>; the number of elements is equal to the number of elements in
     *         records parameter), i-th elements in this array corresponds to the i-th element of the input list (true
     *         means that the record is matched).
     * 
     * @throws IllegalArgumentException
     *            if records is <code>null</code>/empty, contains <code>null</code> or record with <code>null</code> ID
     *             or timestamp.
     * @throws IllegalStateException
     *            if criteria instance or its persistence was not initialized properly.
     * @throws RecentMatchedRecordCountCriteriaPersistenceException
     *            if some error occurred when accessing the persistence.
     */
    public boolean[] match(List<WorkQueueRecord> records) throws RecentMatchedRecordCountCriteriaPersistenceException {
        if (persistence == null) {
            throw new IllegalStateException(...);
        }
        // Get the number of records in the list
        int recordsNum = records.size();
        boolean[] result = new boolean[recordsNum];
        // Start batch processing
        persistence.startBatch();
        for (int i = 0; i < recordsNum; i++) {
            // Get the next record from the list
            WorkQueueRecord record = records.get(i);
            // Get ID of the record
            String recordId = record.getId();
            // Get timestamp of the record
            Date timestamp = record.getTimestamp();
            // Decrease timestamp by the configured interval
            Date minTimestamp = new Date(timestamp.getTime() - interval);
            // Get the number of matched records with the same ID
            int count = persistence.getMatchedRecordCount(recordId, minTimestamp);
            // Check if the number of records with the same ID is not exceeded
            result[i] = count < maxCount; // maxCount includes the currently checked record
            if (result[i]) {
                // Save data for newly matched record to persistence
                persistence.saveMatchedRecordTimestamp(recordId, timestamp);
            }
        }
        // End batch processing
        persistence.endBatch(); // perform this call in finally block
        return result;
    }

    /**
     * <p>
     * Sets the time interval in milliseconds before the timestamp of the currently processed record used when counting
     * the recent matched records with the same ID.
     * </p>
     * 
     * @param interval
     *            the time interval in milliseconds before the timestamp of the currently processed record used when
     *            counting the recent matched records with the same ID.
     * 
     * @throws IllegalArgumentException
     *            if interval is not positive.
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * <p>
     * Sets the maximum acceptable number of matched records with the same ID for the specified time interval. If this
     * number is exceeded including the currently processed record, the record is discarded.
     * </p>
     * 
     * @param maxCount
     *            the maximum acceptable number of matched records with the same ID for the specified time interval.
     * 
     * @throws IllegalArgumentException
     *            if maxCount is not positive.
     */
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    /**
     * <p>
     * Sets the persistence used for storing and retrieving information about IDs and timestamps of the previously
     * matched records.
     * </p>
     * 
     * @param persistence
     *            the persistence used for storing and retrieving information about IDs and timestamps of the previously
     *            matched records.
     * 
     * @throws IllegalArgumentException
     *            if persistence is <code>null</code>.
     */
    public void setPersistence(RecentMatchedRecordCountCriteriaPersistence persistence) {
        this.persistence = persistence;
    }
}
