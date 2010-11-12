/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.lookup;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * This class provides the function to get lookup id from a lookup name of
 * &quot;submission_type_lu&quot; table. Since lookup id/value pairs do not change in the database
 * per installation, this class caches the id/value pairs to minimize queries to the database.
 * This classes is used in various methods of <code>PhaseHandler</code> implementations.
 * </p>
 * <p>
 * Thread safety: This class is thread safe because its static method are synchronized.
 * </p>
 *
 * @author saarixx, myxgyy
 * @version 1.4
 *
 * @since 1.4
 */
public class SubmissionTypeLookupUtility {
    /**
     * Represents the map to store cached value/id pairs from the database. Keys is
     * &quot;lookup value&quot; of type String and values is lookup id of type Long. It is
     * initialized during class loading to an empty map and never be null. New entries will be
     * added to this map when lookUpId() method is called. Cached ids will be used to minimize
     * database queries.
     */
    private static final Map<String, Long> CACHED_PAIRS = new HashMap<String, Long>();

    /**
     * constant representing the sql query string to get the id for a given lookup value.
     */
    private static final String SQL_QUERY = "SELECT submission_type_id FROM submission_type_lu WHERE name = ?";

/**
     * Private constructor to prevent class initialization.
     */
    private SubmissionTypeLookupUtility() {
    }

    /**
     * Gets the lookup id from the given lookup value for &quot;submission_type_lu&quot;
     * table. This method uses caching mechanism for id/value pairs to minimize queries to the
     * database. It is synchronized to ensure thread safety.
     *
     * @param connection The connection used to access the persistence.
     * @param value The lookup value of a table.
     *
     * @return The corresponding lookup id.
     *
     * @throws IllegalArgumentException if any input is null or value is empty.
     * @throws SQLException if there is any problem accessing the database.
     */
    public static synchronized long lookUpId(Connection connection, String value) throws SQLException {
        return LookupHelper.lookUpId(CACHED_PAIRS, value, connection, SQL_QUERY);
    }
}