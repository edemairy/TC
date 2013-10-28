/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.eservice.util.throttle;

/**
 * <p>
 * This exception is thrown by WorkQueueRecordThrottle and implementations of RecordMatchingCriteria when some other
 * error occurs while checking input records using the configured criteria.
 * </p>
 * 
 * <p>
 * <strong>Thread Safety:</strong> This class is not thread safe because its base class is not thread safe.
 * </p>
 * 
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class RecordMatchingCriteriaException extends WorkQueueRecordThrottlingException {
    /**
     * <p>
     * The serial version UID.
     * </p>
     */
    private static final long serialVersionUID = -1257641204748892356L;

    /**
     * <p>
     * Creates a new instance of this exception with the given message.
     * </p>
     * 
     * @param message
     *            the detailed error message of this exception.
     */
    public RecordMatchingCriteriaException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new instance of this exception with the given message and cause.
     * </p>
     * 
     * @param message
     *            the detailed error message of this exception.
     * @param cause
     *            the inner cause of this exception.
     */
    public RecordMatchingCriteriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
