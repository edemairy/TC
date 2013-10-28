/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.eservice.util.throttle.criteria.persistence;

import java.util.Date;

/**
 * <p>
 * This class is an inner private class of FlatFileRecentMatchedRecordCountCriteriaPersistence. It is a container for
 * information about a single cached record. It holds ID, timestamp and flag indicating whether the record was added
 * when processing the current batch of requests. It is a simple JavaBean (POJO) that provides getters and setters for
 * all private attributes and performs no argument validation in the setters.
 * </p>
 * 
 * <p>
 * <strong>Thread Safety:</strong> This class is mutable and not thread safe.
 * </p>
 * 
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
private class CachedRecord {
    /**
     * <p>
     * The ID of the record. Can be any value. Has getter and setter.
     * </p>
     */
    private String id;

    /**
     * <p>
     * The timestamp of the record. Can be any value. Has getter and setter.
     * </p>
     */
    private Date timestamp;

    /**
     * <p>
     * The value indicating whether this record was added in the current batch (true), or read from the persistence file
     * before processing the batch (false). Has getter and setter.
     * </p>
     */
    private boolean added;

    /**
     * <p>
     * Creates an instance of CachedRecord.
     * </p>
     */
    public CachedRecord() {
        // Empty
    }

    /**
     * <p>
     * Retrieves the ID of the record.
     * </p>
     * 
     * @return the ID of the record.
     */
    public String getId() {
        return id;
    }

    /**
     * <p>
     * Sets the ID of the record.
     * </p>
     * 
     * @param id
     *            the ID of the record.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * <p>
     * Retrieves the timestamp of the record.
     * </p>
     * 
     * @return the timestamp of the record.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * <p>
     * Sets the timestamp of the record.
     * </p>
     * 
     * @param timestamp
     *            the timestamp of the record.
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * <p>
     * Retrieves the value indicating whether this record was added in the current batch (true), or read from the
     * persistence file before processing the batch (false).
     * </p>
     * 
     * @return the value indicating whether this record was added in the current batch (true), or read from the
     *         persistence file before processing the batch (false).
     */
    public boolean isAdded() {
        return added;
    }

    /**
     * <p>
     * Sets the value indicating whether this record was added in the current batch (true), or read from the persistence
     * file before processing the batch (false).
     * </p>
     * 
     * @param added
     *            the value indicating whether this record was added in the current batch (true), or read from the
     *            persistence file before processing the batch (false).
     */
    public void setAdded(boolean added) {
        this.added = added;
    }
}
