/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.eservice.util.throttle;

import java.util.ArrayList;
import java.util.List;

import com.ibm.eservice.common.workqueue.ArrayWorkQueue;
import com.ibm.eservice.common.workqueue.EndOfWorkListenerIF;
import com.ibm.eservice.common.workqueue.WorkQueue;

/**
 * <p>
 * This is the main class of this component. It performs throttling by getting records from the configured input work
 * queue, matching them with the configured RecordMatchingCriteria, and forwarding records to output or discard queue
 * depending on the matching result. The user must configure all queues and output record matching criteria via setters
 * before using performThrottling() method. Note that performThrottling() method is blocking. It returns when the input
 * working queue is finished (i.e. the queue is empty and has no registered producers). This class analyzes records in
 * batches to increase performance and decrease resource usage.
 * </p>
 * 
 * <p>
 * <strong>Thread Safety:</strong> This class is mutable and not thread safe. It can be used for running a single
 * throttling process at a time.
 * </p>
 * 
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class WorkQueueRecordThrottle<E> implements EndOfWorkListenerIF {
    /**
     * <p>
     * The constant representing the maximum number of input records to be processed in a single batch. Is used in
     * performThrottling().
     * </p>
     */
    private static final int RECORD_BATCH_SIZE = 100;

    /**
     * <p>
     * The input work queue. It is used for getting input records that are next forwarded to output or discard queue
     * based on the configured criteria. Is <code>null</code> when not yet initialized. Is expected to be injected via
     * setter method before performThrottling() method is used. Cannot be <code>null</code> after initialization. Is
     * used in performThrottling().
     * </p>
     */
    private WorkQueue<E> inputWorkQueue;

    /**
     * <p>
     * The output work queue to which all input records matched with the configured criteria are forwarded. Is
     * <code>null</code> when not yet initialized. Is expected to be injected via setter method before
     * performThrottling() method is used. Cannot be <code>null</code> after initialization. Is used in
     * performThrottling().
     * </p>
     */
    private WorkQueue<E> outputWorkQueue;

    /**
     * <p>
     * The work queue to which all discarded input records (the ones that are not matched with the configured criteria)
     * are forwarded. Is <code>null</code> when not yet initialized. Is expected to be injected via setter method before
     * performThrottling() method is used. Cannot be <code>null</code> after initialization. Is used in
     * performThrottling().
     * </p>
     */
    private WorkQueue<E> discardWorkQueue;

    /**
     * <p>
     * The criteria used for detecting whether the record should be forwarded to "output" or "discard" work queue. If
     * this criteria is met, the record is forwarded to outputWorkQueue, otherwise the record is forwarded to
     * discardWorkQueue. Is <code>null</code> when not yet initialized. Is expected to be injected via setter method
     * before performThrottling() method is used. Cannot be <code>null</code> after initialization. Is used in
     * performThrottling().
     * </p>
     */
    private RecordMatchingCriteria recordOutputCriteria;

    /**
     * <p>
     * The value indicating whether the input work queue is empty and has no registered producers, thus throttling
     * process is expected to be finished. Also this field is set to true when throttling is not in progress. Is used in
     * performThrottling() and endOfWork().
     * </p>
     */
    private boolean finished = true;

    /**
     * <p>
     * Creates an instance of WorkQueueRecordThrottle.
     * </p>
     */
    public WorkQueueRecordThrottle() {
        // Empty
    }

    /**
     * <p>
     * Performs the throttling of records obtained from the configured input work queue. Forwards the retrieved records
     * to the configured output or discard queue based on the specified criteria. This method is blocking - it returns
     * when the input working queue is finished. In case of error this method terminates immediately and throws an
     * exception.
     * </p>
     * 
     * @throws IllegalStateException
     *            if this class was not configured properly or throttling is already in progress.
     * @throws RecordMatchingCriteriaException
     *            if some other error occurred when checking input records using the configured criteria.
     * @throws WorkQueueRecordThrottlingException
     *            if some error occurred when using the specified work queues.
     */
    public void performThrottling() throws WorkQueueRecordThrottlingException {
        if (!finished) {
            throw new IllegalStateException(...);
        }
        if (inputWorkQueue, outputWorkQueue, discardWorkQueue || recordOutputCriteria == null) {
            throw new IllegalStateException(...);
        }
        finished = false;
        // Register this class as end of work listener of input queue
        inputWorkQueue.registerEndOfWorkListener(this);
        // Register this class as a producer for output queue
        outputWorkQueue.registerProducer(this);
        // Register this class as a producer for discard queue
        discardWorkQueue.registerProducer(this);
        // Create an empty records array
        List<WorkQueueRecord> records = new ArrayList<WorkQueueRecord>();
        while (!finished) {
            // Get available records from input queue to the list
            int recordsNum = inputWorkQueue.drainTo(records, RECORD_BATCH_SIZE);
            if (recordsNum > 0) {
                // Check the obtained records to decide where they should be forwarded
                boolean[] outputFlags = recordOutputCriteria.match(records);
                for (int i = 0; i < recordsNum; i++) {
                    // Get the next record from the list
                    WorkQueueRecord record = records.get(i);
                    if (outputFlags[i]) {
                        // Put record to the output queue
                        outputWorkQueue.put(record);
                    } else {
                        // Put record to the discard queue
                        discardWorkQueue.put(record);
                    }
                }
                // Clear the list
                records.clear();
            } else {
                // Peek input queue
                inputWorkQueue.peek(); // ideally this call would be not required, but ArrayWorkQueue has a bug in its implementation - it doesn't call notifyAllConsumers() in drainTo()
                if (finished) {
                    break;
                }
            }
            if (recordsNum != RECORD_BATCH_SIZE) {
                // Sleep during small amount of time
                Thread.sleep(10);
            }
        }
        Perform in finally block:
            // Unregister this class as output queue producer
            outputWorkQueue.unregisterProducer(this);
            // Unregister this class as discard queue producer
            discardWorkQueue.unregisterProducer(this);
            finished = true; // required in case of exception was thrown
        }
    }

    /**
     * <p>
     * This is a callback method that indicates to this class that no more work is coming into the input work queue.
     * This method should not be called by the user (the only correct way to stop throttling is to remove all producers
     * from the input work queue and stop adding records to it).
     * </p>
     */
    public void endOfWork() {
        finished = true;
    }

    /**
     * <p>
     * Sets the input work queue.
     * </p>
     * 
     * @param inputWorkQueue
     *            the input work queue.
     * 
     * @throws IllegalArgumentException
     *            if inputWorkQueue is <code>null</code>.
     */
    public void setInputWorkQueue(WorkQueue<E> inputWorkQueue) {
        this.inputWorkQueue = inputWorkQueue;
    }

    /**
     * <p>
     * Sets the output work queue to which all input records matched with the configured criteria are forwarded.
     * </p>
     * 
     * @param outputWorkQueue
     *            the output work queue to which all input records matched with the configured criteria are forwarded.
     * 
     * @throws IllegalArgumentException
     *            if outputWorkQueue is <code>null</code>.
     */
    public void setOutputWorkQueue(WorkQueue<E> outputWorkQueue) {
        this.outputWorkQueue = outputWorkQueue;
    }

    /**
     * <p>
     * Sets the work queue to which all discarded input records (the ones that are not matched with the configured
     * criteria) are forwarded.
     * </p>
     * 
     * @param discardWorkQueue
     *            the work queue to which all discarded input records (the ones that are not matched with the configured
     *            criteria) are forwarded.
     * 
     * @throws IllegalArgumentException
     *            if discardWorkQueue is <code>null</code>.
     */
    public void setDiscardWorkQueue(WorkQueue<E> discardWorkQueue) {
        this.discardWorkQueue = discardWorkQueue;
    }

    /**
     * <p>
     * Sets the criteria used for detecting whether the record should be forwarded to "output" or "discard" work queue.
     * If this criteria is met, the record is forwarded to outputWorkQueue, otherwise the record is forwarded to
     * discardWorkQueue.
     * </p>
     * 
     * @param recordOutputCriteria
     *            the criteria used for detecting whether the record should be forwarded to "output" or "discard" work
     *            queue.
     * 
     * @throws IllegalArgumentException
     *            if recordOutputCriteria is <code>null</code>.
     */
    public void setRecordOutputCriteria(RecordMatchingCriteria recordOutputCriteria) {
        this.recordOutputCriteria = recordOutputCriteria;
    }
}
