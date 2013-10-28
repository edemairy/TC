/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.eservice.util.throttle.criteria.persistence;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ibm.eservice.util.throttle.criteria.RecentMatchedRecordCountCriteriaPersistence;
import com.ibm.eservice.util.throttle.criteria.RecentMatchedRecordCountCriteriaPersistenceException;

/**
 * <p>
 * This class is an implementation of RecentMatchedRecordCountCriteriaPersistence that stores IDs and timestamps of
 * matched records in plain text file. The format of the file is described in the section 1.3.2 of CS. The user must
 * initialize file path parameter via setter before using this class. This class supports processing of requests in a
 * batch. At the beginning of the batch it opens and locks the configured file. During the first getMatchedRecordCount()
 * call it finds and reads all the records that can affect results of getMatchedRecordCount() method calls in the
 * current batch. In saveMatchedRecordTimestamp() method this class simply caches added records in memory. And at the
 * end of the batch it flushes all new cached records to the file and unlocks it.
 * </p>
 * 
 * <p>
 * <strong>Thread Safety:</strong> This class is mutable and not thread safe. Since this class locks access to the file,
 * multiple FlatFileRecentMatchedRecordCountCriteriaPersistence instances (possibly from different JVMs) can be used
 * simultaneously for the same file on a disk.
 * </p>
 * 
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class FlatFileRecentMatchedRecordCountCriteriaPersistence implements RecentMatchedRecordCountCriteriaPersistence {
    /**
     * <p>
     * This constant string represents the format of timestamps in the persistence flat file. It is used in constructor
     * and loadRecordsFromFile().
     * </p>
     */
    private static final String TIMESTAMP_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * <p>
     * The path of the file used as a persistence for matched record data. Is <code>null</code> when not yet
     * initialized. Is expected to be set via setter method before any business method is used. Cannot be
     * <code>null</code> or empty after initialization. Is used in startBatch().
     * </p>
     */
    private String filePath;

    /**
     * <p>
     * The file lock that is used for locking the persistence file during the batch processing. Is <code>null</code>
     * when batch processing is not in progress, and never <code>null</code> otherwise. Is used in startBatch(),
     * getMatchedRecordCount(), saveMatchedRecordTimestamp() and endBatch().
     * </p>
     */
    private FileLock fileLock;

    /**
     * <p>
     * This timestamp format object used for formatting and parsing timestamps in the persistence flat file. Is
     * initialized in the constructor and never changed after that. Cannot be <code>null</code>. Is used in endBatch()
     * and parseTimestamp().
     * </p>
     */
    private final DateFormat timestampFormat;

    /**
     * <p>
     * The list of cached records used to increase the performance of processing getMatchedRecordCount() and
     * saveMatchedRecordTimestamp() requests. During the first getMatchedRecordCount() call in the batch, this list is
     * filled with records from the persistence file (only the most recent records from the file are read - the ones
     * that can affect getMatchedRecordCount() result). Then this list is used in both getMatchedRecordCount() and
     * saveMatchedRecordTimestamp() methods instead of accessing the persistence file directly. In endBatch() method the
     * newly added records from this list are flushed to the persistence file. Collection instance is initialized in the
     * constructor and never changed after that. Cannot be <code>null</code>, cannot contain <code>null</code>. Is used
     * in getMatchedRecordCount(), saveMatchedRecordTimestamp(), endBatch() and loadRecordsFromFile().
     * </p>
     */
    private final List<CachedRecord> cachedRecords;

    /**
     * <p>
     * The value indicating whether the recent records from the persistence file were loaded during the first
     * getMatchedRecordCount() call in the batch. Is used in getMatchedRecordCount(), endBatch() and
     * loadRecordsFromFile().
     * </p>
     */
    private boolean loadedRecordsFromFile;

    /**
     * <p>
     * The random access file instance used for reading and writing data to/from the persistence file. Is
     * <code>null</code> if batch processing is not in progress, and never <code>null</code> otherwise. Is used in
     * startBatch(), endBatch() and loadRecordsFromFile().
     * </p>
     */
    private RandomAccessFile file;

    /**
     * <p>
     * Creates an instance of FlatFileRecentMatchedRecordCountCriteriaPersistence.
     * </p>
     */
    public FlatFileRecentMatchedRecordCountCriteriaPersistence() {
        timestampFormat = new SimpleDateFormat(TIMESTAMP_FORMAT_STRING);
        cachedRecords = new ArrayList<CachedRecord>();
    }

    /**
     * <p>
     * Starts a batch of getMatchedRecordCount() and saveMatchedRecordTimestamp() requests. This method opens and locks
     * a file used as a persistence.
     * </p>
     * 
     * @throws IllegalStateException
     *            if persistence instance was not initialized or used properly.
     * @throws RecentMatchedRecordCountCriteriaPersistenceException
     *            if some error occurred when accessing the persistence.
     */
    public void startBatch() throws RecentMatchedRecordCountCriteriaPersistenceException {
        if (file != null) {
            throw new IllegalStateException(...);
        }
        // Create random access file instance
        file = new RandomAccessFile(filePath, "rw");
        // Get file channel
        FileChannel channel = file.getChannel();
        // Lock the file
        fileLock = channel.lock();
    }

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
    public int getMatchedRecordCount(String id, Date minTimestamp) throws RecentMatchedRecordCountCriteriaPersistenceException {
        if (fileLock == null) {
            throw new IllegalStateException(...);
        }
        if (!loadedRecordsFromFile) {
            // Retrieve recent records from the file
            loadRecordsFromFile(minTimestamp);
        }
        int result = 0;
        for (each record in cachedRecords) {
            // Get ID of the record
            String recordId = record.getId();
            if (recordId is not equal to id) {
                continue;
            }
            // Get timestamp of the record
            Date recordTimestamp = record.getTimestamp();
            // Check if timestamp is too early to be counted
            boolean isTimestampEarly = recordTimestamp.before(minTimestamp);
            if (!isTimestampEarly) {
                result++;
            }
        }
        return result;
    }

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
     */
    public void saveMatchedRecordTimestamp(String id, Date timestamp) {
        if (fileLock == null) {
            throw new IllegalStateException(...);
        }
        // Create new CachedRecord instance
        CachedRecord record = new CachedRecord();
        // Set ID to the cached record
        record.setId(id);
        // Set timestamp to the cached record
        record.setTimestamp(timestamp);
        // Set flag indicating the the record was just added
        record.setAdded(true);
        // Add cached record to the list
        cachedRecords.add(record);
    }

    /**
     * <p>
     * Ends a batch of getMatchedRecordCount() and saveMatchedRecordTimestamp() requests. This method appends all new
     * records to the persistence file and unlocks it.
     * </p>
     * 
     * @throws IllegalStateException
     *            if persistence instance was not initialized or used properly.
     * @throws RecentMatchedRecordCountCriteriaPersistenceException
     *            if some error occurred when accessing the persistence.
     */
    public void endBatch() throws RecentMatchedRecordCountCriteriaPersistenceException {
        if (fileLock == null) {
            throw new IllegalStateException(...);
        }
        // Get system default line separator
        String lineSeparator = System.getProperty("line.separator");
        // Create StringBuilder instance
        StringBuilder sb = new StringBuilder();
        for (each record in cachedRecords) {
            // Check if record was just added
            boolean added = record.isAdded();
            if (added) {
                // Get timestamp of the record
                Date timestamp = record.getTimestamp();
                // Convert timestamp to string
                String timestampStr = timestampFormat.format(timestamp);
                // Get ID of the record
                String recordId = record.getId();
                // Append line to the string builder
                sb.append(timestampStr).append(' ').append(recordId).append(lineSeparator);
            }
        }
        // Get text from the string builder
        String text = sb.toString();
        // Write text to file
        file.writeBytes(text);
        Perform in finally block:
            // Release the file lock
            fileLock.release();
            // Close the file
            file.close();
            fileLock = null;
            file = null;
            // Clear the list of cached records
            cachedRecords.clear();
            loadedRecordsFromFile = false;
        }
    }

    /**
     * <p>
     * Parses the given timestamp string.
     * </p>
     * 
     * @param text
     *            the timestamp text to be parsed.
     * 
     * @return the parsed timestamp (<code>null</code> if parsing error occurred).
     */
    private Date parseTimestamp(String text) {
        ParsePosition pos = new ParsePosition(0);
        Date timestamp = timestampFormat.parse(text, pos);
        if (pos.getIndex() != text.length()) {
            return null;
        }
        return timestamp;
    }

    /**
     * <p>
     * Load the information about all recently matched records (with timestamp equal to or after the provided one) from
     * the persistence file and stores data for these records into cachedRecords list. The file is not read fully,
     * instead RandomAccessFile is used to jump backward every 16 Kb from the end of the file until the record going
     * after the file pointer has too early timestamp to affect getMatchedRecordCount() results. This approach is
     * possible since all records are processed in chronological order.
     * </p>
     * 
     * @param minTimestamp
     *            the minimum timestamp of records to be retrieved.
     * 
     * @throws RecentMatchedRecordCountCriteriaPersistenceException
     *            if some error occurred when accessing the persistence file.
     */
    private void loadRecordsFromFile(Date minTimestamp) throws RecentMatchedRecordCountCriteriaPersistenceException {
        // Get the length of the file
        long currentPointer = file.length();
        // While true do
        // this loop is ended when early enough timestamp is found or beginning of the file is reached
            currentPointer -= 16384; // 16 Kb
            if (currentPointer < 0) {
                currentPointer = 0;
            }
            // Set pointer to the new position
            file.seek(currentPointer);
            if (currentPointer == 0) {
                break;
            }
            // Skip the current line since the pointer can be in the middle of it
            file.readLine();
            // Read the next line fully
            String line = file.readLine();
            if (line == null || line.length() < TIMESTAMP_FORMAT_STRING.length()) {
                continue;
            }
            // Extract timestamp string from the line
            String timestampStr = line.substring(0, TIMESTAMP_FORMAT_STRING.length());
            // Parse timestamp
            Date timestamp = parseTimestamp(timestampStr); // throw RecentMatchedRecordCountCriteriaPersistenceException if null returned
            // Check if timestamp is early enough
            boolean earlyEnough = timestamp.before(minTimestamp);
            if (earlyEnough) {
                break;
            }
        }
        // While true do
        // this loop is ended when end of file is reached
            // Read line from the file
            String line = file.readLine();
            if (line is empty) {
                continue; if line is null then break;
            }
            // Extract timestamp string from the line
            String timestampStr = line.substring(0, TIMESTAMP_FORMAT_STRING.length());
            // Parse timestamp
            Date timestamp = parseTimestamp(timestampStr); // throw RecentMatchedRecordCountCriteriaPersistenceException if null returned
            if (timestamp.before(minTimestamp)) {
                continue;
            }
            // Extract record ID from the line
            String recordId = line.substring(TIMESTAMP_FORMAT_STRING.length() + 1);
            // Create new cached record
            CachedRecord record = new CachedRecord();
            // Set ID to the entity
            record.setId(recordId);
            // Set timestamp to the entity
            record.setTimestamp(timestamp);
            // Add cached record to the list
            cachedRecords.add(record);
        }
        loadedRecordsFromFile = true;
    }

    /**
     * <p>
     * Sets the path of the file used as a persistence for matched record data.
     * </p>
     * 
     * @param filePath
     *            the path of the file used as a persistence for matched record data.
     * 
     * @throws IllegalArgumentException
     *            if filePath is <code>null</code> or empty.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
