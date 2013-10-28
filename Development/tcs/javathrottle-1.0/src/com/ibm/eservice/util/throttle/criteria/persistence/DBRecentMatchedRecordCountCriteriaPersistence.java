/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.eservice.util.throttle.criteria.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import com.ibm.eservice.util.throttle.criteria.RecentMatchedRecordCountCriteriaPersistence;
import com.ibm.eservice.util.throttle.criteria.RecentMatchedRecordCountCriteriaPersistenceException;

/**
 * <p>
 * This class is an implementation of RecentMatchedRecordCountCriteriaPersistence that stores IDs and timestamps of
 * matched records in database. The database schema used by this class is described in the section 1.3.1 of CS. The user
 * must initialize database connection parameters via setters before using this class. Also the user is responsible for
 * loading an appropriate JDBC driver. This class supports processing of requests in a batch. At the beginning of the
 * batch it establishes a database connection that is closed at the end of the batch.
 * </p>
 * 
 * <p>
 * <strong>Thread Safety:</strong> This class is mutable and not thread safe. Note that DB connection is used in auto
 * commit mode. The reason for this is to save information about newly matched records as soon as possible in case if
 * multiple DBRecentMatchedRecordCountCriteriaPersistence instances (possibly from different JVMs) use the same database
 * at a time.
 * </p>
 * 
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class DBRecentMatchedRecordCountCriteriaPersistence implements RecentMatchedRecordCountCriteriaPersistence {
    /**
     * <p>
     * The URL used when establishing a database connection. Is <code>null</code> when not yet initialized. Is expected
     * to be specified via setter method before any business method is used. Cannot be <code>null</code> or empty after
     * initialization. Is used in startBatch().
     * </p>
     */
    private String url;

    /**
     * <p>
     * The username used when connecting to the database. If <code>null</code>, anonymous access is used. Cannot be
     * empty. Has a setter. Is used in startBatch().
     * </p>
     */
    private String username;

    /**
     * <p>
     * The password used when connecting to the database. Is not used when username is <code>null</code>. Can be any
     * value. Has a setter. Is used in startBatch().
     * </p>
     */
    private String password;

    /**
     * <p>
     * The database connection used for storing and retrieving matched record data. Is <code>null</code> when batch
     * processing is not in progress, and never <code>null</code> otherwise. Is used in startBatch(),
     * getMatchedRecordCount(), saveMatchedRecordTimestamp() and endBatch().
     * </p>
     */
    private Connection connection;

    /**
     * <p>
     * The SQL statement for counting the number of records with specific ID and timestamp. Is <code>null</code> when
     * not yet initialized or when batch processing is not in progress. This field is used for caching a prepared
     * statement so that only one instance of it used while processing the whole batch of requests. Is used in
     * getMatchedRecordCount() and endBatch().
     * </p>
     */
    private PreparedStatement countRecordsPreparedStatement;

    /**
     * <p>
     * The SQL statement for inserting a new row to processed_records table. Is <code>null</code> when not yet
     * initialized or when batch processing is not in progress. This field is used for caching a prepared statement so
     * that only one instance of it used while processing the whole batch of requests. Is used in
     * saveMatchedRecordTimestamp() and endBatch().
     * </p>
     */
    private PreparedStatement insertRecordPreparedStatement;

    /**
     * <p>
     * Creates an instance of DBRecentMatchedRecordCountCriteriaPersistence.
     * </p>
     */
    public DBRecentMatchedRecordCountCriteriaPersistence() {
        // Empty
    }

    /**
     * <p>
     * Starts a batch of getMatchedRecordCount() and saveMatchedRecordTimestamp() requests. This method establishes a
     * connection to be used for all requests in a batch.
     * </p>
     * 
     * @throws IllegalStateException
     *            if persistence instance was not initialized or used properly.
     * @throws RecentMatchedRecordCountCriteriaPersistenceException
     *            if some error occurred when accessing the persistence.
     */
    public void startBatch() throws RecentMatchedRecordCountCriteriaPersistenceException {
        if (connection != null, url == null || (username != null && password == null)) {
            throw new IllegalStateException(...);
        }
        if (username != null) {
            // Connect to the database using credentials
            connection = DriverManager.getConnection(url, username, password);
        } else {
            // Connect to the database without credentials
            connection = DriverManager.getConnection(url);
        }
        // Enable auto commit mode
        connection.setAutoCommit(true);
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
        if (connection == null) {
            throw new IllegalStateException(...);
        }
        if (countRecordsPreparedStatement == null) {
            // Prepare the SQL statement for counting the number of records with specific ID and timestamp
            countRecordsPreparedStatement = connection.prepareStatement("SELECT count(id) FROM processed_records WHERE record_id = ? AND record_timestamp >= ?");
        }
        // Set record ID to the prepared statement
        countRecordsPreparedStatement.setString(1, id);
        // Convert Date timestamp to long
        long time = minTimestamp.getTime();
        // Convert long timestamp to Timestamp instance
        Timestamp sqlTimestamp = new Timestamp(time);
        // Set timestamp to the prepared statement
        countRecordsPreparedStatement.setTimestamp(2, sqlTimestamp);
        // Execute the SQL query
        ResultSet resultSet = countRecordsPreparedStatement.executeQuery();
        // Move pointer to the first row in the result set
        resultSet.next();
        // Get the number of matched records from the result set
        int result = resultSet.getInt(1);
        // Close the result set
        resultSet.close();
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
     * @throws RecentMatchedRecordCountCriteriaPersistenceException
     *            if some error occurred when accessing the persistence.
     */
    public void saveMatchedRecordTimestamp(String id, Date timestamp) throws RecentMatchedRecordCountCriteriaPersistenceException {
        if (connection == null) {
            throw new IllegalStateException(...);
        }
        if (insertRecordPreparedStatement == null) {
            // Prepare the statement for inserting a new row to processed_records table
            insertRecordPreparedStatement = connection.prepareStatement("INSERT INTO processed_records (record_id, record_timestamp) VALUES (?, ?)");
        }
        // Set record ID to the prepared statement
        insertRecordPreparedStatement.setString(1, id);
        // Convert Date timestamp to long
        long time = timestamp.getTime();
        // Convert long timestamp to Timestamp instance
        Timestamp sqlTimestamp = new Timestamp(time);
        // Set timestamp to the prepared statement
        insertRecordPreparedStatement.setTimestamp(2, sqlTimestamp);
        // Execute the INSERT statement
        insertRecordPreparedStatement.executeUpdate();
    }

    /**
     * <p>
     * Ends a batch of getMatchedRecordCount() and saveMatchedRecordTimestamp() requests. This method closes the
     * database connection.
     * </p>
     * 
     * @throws IllegalStateException
     *            if persistence instance was not initialized or used properly.
     * @throws RecentMatchedRecordCountCriteriaPersistenceException
     *            if some error occurred when accessing the persistence.
     */
    public void endBatch() throws RecentMatchedRecordCountCriteriaPersistenceException {
        if (connection == null) {
            throw new IllegalStateException(...);
        }
        if (countRecordsPreparedStatement != null) {
            countRecordsPreparedStatement.close();
        }
        if (insertRecordPreparedStatement != null) {
            insertRecordPreparedStatement.close();
        }
        // Close the connection
        connection.close();
        connection = null;
    }

    /**
     * <p>
     * Sets the URL used when establishing a database connection.
     * </p>
     * 
     * @param url
     *            the URL used when establishing a database connection.
     * 
     * @throws IllegalArgumentException
     *            if url is <code>null</code> or empty.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * <p>
     * Sets the username used when connecting to the database.
     * </p>
     * 
     * @param username
     *            the username used when connecting to the database (<code>null</code> if authentication is not
     *            required).
     * 
     * @throws IllegalArgumentException
     *            if username is empty.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * <p>
     * Sets the password used when connecting to the database.
     * </p>
     * 
     * @param password
     *            the password used when connecting to the database.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
