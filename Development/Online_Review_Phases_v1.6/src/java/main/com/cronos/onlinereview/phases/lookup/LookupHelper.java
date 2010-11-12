/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.lookup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Map;

/**
 * <p>
 * A class having helper methods to perform argument validation. It also defines
 * a lookup method which is used by all the lookup utility classes in this
 * package.
 * </p>
 *
 * @author tuenm, bose_java, TCSDEVELOPER
 * @version 1.0
 */
final class LookupHelper {
    /**
     * private to prevent instantiation.
     */
    private LookupHelper() {
        // do nothing.
    }

    /**
     * Checks whether the given Object is null and throws
     * IllegalArgumentException if yes.
     *
     * @param arg the argument to check
     * @param name the name of the argument
     *
     * @throws IllegalArgumentException if the given Object is null
     */
    static void checkNull(Object arg, String name) {
        if (arg == null) {
            throw new IllegalArgumentException(name + " should not be null.");
        }
    }

    /**
     * Checks whether the given String is null or empty.
     *
     * @param arg the String to check
     * @param name the name of the argument
     *
     * @throws IllegalArgumentException if the given string is null or empty
     */
    static void checkString(String arg, String name) {
        checkNull(arg, name);

        if (arg.trim().length() == 0) {
            throw new IllegalArgumentException(name + " should not be empty.");
        }
    }

    /**
     * This method retrieves the id from the cache if present, else from the
     * database. This method is not synchronized as it is called from the
     * lookUpId() method of the various LookupUtility classes which itself is
     * synchronized.
     *
     * @param cachedPairs the cache of looked up values and ids.
     * @param value the value to be looked up.
     * @param connection The connection used to access the persistence.
     * @param sql the sql string to query the database with.
     *
     * @return The corresponding lookup id.
     *
     * @throws IllegalArgumentException if any input is null or value is empty.
     * @throws SQLException if there is any problem acessing the database.
     */
    static long lookUpId(Map<String, Long> cachedPairs, String value, Connection connection,
                    String sql) throws SQLException {
        checkNull(cachedPairs, "cachedPairs");
        checkString(value, "value");
        checkNull(connection, "connection");
        checkString(sql, "sql");

        Long lngId = (Long) cachedPairs.get(value);

        if (lngId != null) {
            return lngId.longValue();
        } else {
            long id = lookUpIdFromDatabase(connection, sql, value);
            cachedPairs.put(value, new Long(id));

            return id;
        }
    }

    /**
     * <p>
     * A helper method to lookup the id for the given value from the database.
     * </p>
     * <p>
     * It uses a PreparedStatement to fetch the results.
     * </p>
     *
     * @param connection The connection used to access the persistence.
     * @param sql the sql query string.
     * @param value the lookup value.
     *
     * @return id for the given lookup value.
     *
     * @throws SQLException if there is a problem connecting to database, or if
     *         value is not mapped to any record.
     */
    private static long lookUpIdFromDatabase(Connection connection, String sql,
                    String value) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, value);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new SQLException("The query '" + sql
                                + "' did not return any rows.");
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    // handle silently.
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    // handle silently.
                }
            }
        }
    }
}
