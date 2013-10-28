/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.eservice.util.throttle;

import java.util.Date;

/**
 * <p>
 * This interface represents a work queue record that has an alphanumeric identifier and timestamp associated with it.
 * All work queue records must implement this interface to be supported by this component. Multiple WorkQueueRecord
 * instances can be associated with the same ID.
 * </p>
 * 
 * <p>
 * <strong>Thread Safety:</strong> Implementations of this interface must be thread safe since records will be processed
 * in work queues by multiple threads at a time. Additionally ID and timestamp parameters of WorkQueueRecord
 * implementation instance are expected to be fixed (methods should always return the same values for the same record).
 * </p>
 * 
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public interface WorkQueueRecord {
    /**
     * <p>
     * Retrieves the alphanumeric identifier associated with the the record.
     * </p>
     * 
     * @return the retrieved alphanumeric identifier associated with the the record (not <code>null</code>).
     */
    public String getId();

    /**
     * <p>
     * Retrieves the timestamp of the record.
     * </p>
     * 
     * @return the retrieved timestamp of the record (not <code>null</code>).
     */
    public Date getTimestamp();
}
