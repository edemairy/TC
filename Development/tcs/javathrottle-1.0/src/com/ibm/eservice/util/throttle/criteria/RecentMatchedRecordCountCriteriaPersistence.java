/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.eservice.util.throttle.criteria;

import java.util.Date;

/**
 * <p>
 * This interface represents a persistence used by RecentMatchedRecordCountCriteria class. The persistence stores IDs
 * and timestamps of all records matched by the criteria. Requests to persistence are expected to be provided in
 * batches. At the beginning of each batch startBatch() method is called, and at the end of each batch endBatch() method
 * is called. Between these two calls any number of getMatchedRecordCount() and saveMatchedRecordTimestamp() calls can
 * be performed. Such approach allows persistence implementations to be more efficient by initializing and releasing
 * shared resources just once per batch. But it's fully up to implementations whether to support batches or not
 * (implementation can do nothing in startBatch() and endBatch() methods when it doesn't provide support for batch-based
 * request processing). Persistence implementations are not responsible for cleaning up the persisted record data (and
 * this functionality is out of the scope of this component).
 * </p>
 * 
 * <p>
 * <strong>Thread Safety:</strong> Implementations of this interface are not required to be thread safe.
 * </p>
 * 
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public interface RecentMatchedRecordCountCriteriaPersistence {
    /**
     * <p>
     * Starts a batch of getMatchedRecordCount() and saveMatchedRecordTimestamp() requests. In this method
     * implementation can initialize common resources used for all requests in a batch.
     * </p>
     * 
     * @throws IllegalStateException
     *            if persistence instance was not initialized or used properly.
     * @throws RecentMatchedRecordCountCriteriaPersistenceException
     *            if some error occurred when accessing the persistence.
     */
    public void startBatch() throws RecentMatchedRecordCountCriteriaPersistenceException;

    /**
     * <p>
     * Retrieves the number of persisted records with the specified ID and minimum timestamp.
     * </p>
     * 
     * @param id
     *            the ID of the records to be counted.
     * @param minTimestamp
     *            the minimum timestamp of the records to be counted.
     * 
     * @return the retrieved number of persisted records with the specified ID and minimum timestamp.
     * 
     * @throws IllegalArgumentException
     *            if id or minTimestamp is <code>null</code>.
     * @throws IllegalStateException
     *            if persistence instance was not initialized or used properly.
     * @throws RecentMatchedRecordCountCriteriaPersistenceException
     *            if some error occurred when accessing the persistence.
     */
    public int getMatchedRecordCount(String id, Date minTimestamp) throws RecentMatchedRecordCountCriteriaPersistenceException;

    /**
     * <p>
     * Saves information about a matched record with the specified ID and timestamp to persistence.
     * </p>
     * 
     * @param id
     *            the ID of the matched record.
     * @param timestamp
     *            the timestamp of the matched record.
     * 
     * @throws IllegalArgumentException
     *            if id or timestamp is <code>null</code>.
     * @throws IllegalStateException
     *            if persistence instance was not initialized or used properly.
     * @throws RecentMatchedRecordCountCriteriaPersistenceException
     *            if some error occurred when accessing the persistence.
     */
    public void saveMatchedRecordTimestamp(String id, Date timestamp) throws RecentMatchedRecordCountCriteriaPersistenceException;

    /**
     * <p>
     * Ends a batch of getMatchedRecordCount() and saveMatchedRecordTimestamp() requests. In this method implementation
     * can dispose common resources used for all requests in a batch.
     * </p>
     * 
     * @throws IllegalStateException
     *            if persistence instance was not initialized or used properly.
     * @throws RecentMatchedRecordCountCriteriaPersistenceException
     *            if some error occurred when accessing the persistence.
     */
    public void endBatch() throws RecentMatchedRecordCountCriteriaPersistenceException;
}
