/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.eservice.util.throttle.criteria;

import com.ibm.eservice.util.throttle.RecordMatchingCriteriaException;

/**
 * <p>
 * This exception is thrown by RecentMatchedRecordCountCriteria and implementations of
 * RecentMatchedRecordCountCriteriaPersistence when some error occurs while accessing the persistence.
 * </p>
 * 
 * <p>
 * <strong>Thread Safety:</strong> This class is not thread safe because its base class is not thread safe.
 * </p>
 * 
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class RecentMatchedRecordCountCriteriaPersistenceException extends RecordMatchingCriteriaException {
    /**
     * <p>
     * The serial version UID.
     * </p>
     */
    private static final long serialVersionUID = 3231237934797235160L;

    /**
     * <p>
     * Creates a new instance of this exception with the given message.
     * </p>
     * 
     * @param message
     *            the detailed error message of this exception.
     */
    public RecentMatchedRecordCountCriteriaPersistenceException(String message) {
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
    public RecentMatchedRecordCountCriteriaPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
