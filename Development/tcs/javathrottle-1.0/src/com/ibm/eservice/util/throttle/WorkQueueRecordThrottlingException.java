/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.eservice.util.throttle;

/**
 * <p>
 * This exception is thrown by WorkQueueRecordThrottle when some error occurs while using the specified work queues.
 * Also this exception is used as a base class for other specific custom exceptions defined in this component.
 * </p>
 * 
 * <p>
 * <strong>Thread Safety:</strong> This class is not thread safe because its base class is not thread safe.
 * </p>
 * 
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class WorkQueueRecordThrottlingException extends Exception {
    /**
     * <p>
     * The serial version UID.
     * </p>
     */
    private static final long serialVersionUID = -1235547291982958259L;

    /**
     * <p>
     * Creates a new instance of this exception with the given message.
     * </p>
     * 
     * @param message
     *            the detailed error message of this exception.
     */
    public WorkQueueRecordThrottlingException(String message) {
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
    public WorkQueueRecordThrottlingException(String message, Throwable cause) {
        super(message, cause);
    }
}
