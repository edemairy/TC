/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.eservice.util.throttle;

import java.util.List;

/**
 * <p>
 * This interface represents record matching criteria. It provides a method that accepts a list of work queue records
 * and returns a boolean array with flags indicating whether each of input record was matched by this criteria.
 * </p>
 * 
 * <p>
 * <strong>Thread Safety:</strong> Implementations of this interface are not required to be thread safe. They are always
 * used by WorkQueueRecordThrottle from a single thread only.
 * </p>
 * 
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public interface RecordMatchingCriteria {
    /**
     * <p>
     * Checks whether the provided records are matched by these criteria. The records are processed strictly in the
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
     *            if criteria instance was not initialized properly.
     * @throws RecordMatchingCriteriaException
     *            if some other error occurred when performing the check.
     */
    public boolean[] match(List<WorkQueueRecord> records) throws RecordMatchingCriteriaException;
}
