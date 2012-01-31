/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.stresstests;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.StringTokenizer;


/**
 * Test helper class.
 *
 * @author mumujava
 * @version 1.0
 */
final class TestHelper {

    /**
     * Private ctor.
     */
    private TestHelper() {
        // do nothing
    }

    /**
     * Clear the db.
     *
     * @throws Exception to Junit
     */
    public static void clear() throws Exception {
        Connection conn = getConnection();
        try {
            executeSqlFile(conn, "test_files/stresstests/clear.sql");
        } finally {
            conn.close();
        }
    }

    /**
     * Executes the sql file.
     *
     * @param connection the db connection
     * @param file the sql file
     * @throws Exception to JUnit
     */
    private static void executeSqlFile(Connection connection, String file) throws Exception {
        String sql = getSql(file);

        StringTokenizer st = new StringTokenizer(sql, ";");

        PreparedStatement preparedStatement = null;

        try {
            for (int count = 1; st.hasMoreTokens(); count++) {
                String statement = st.nextToken().trim();

                if (statement.length() > 0) {
                    // An empty statement is technically
                    // erroneous, but ignore them silently
                    preparedStatement = connection.prepareStatement(statement);
                    preparedStatement.executeUpdate();
                }
            }
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    /**
     * <p>
     * Gets a connection.
     * </p>
     *
     * @return the connection.
     * @throws Exception to JUnit.
     */
    private static Connection getConnection() throws Exception {
        Properties config = new Properties();

        FileInputStream fio = new FileInputStream("test_files/stresstests/config.properties");
        try {
            config.load(fio);
        } finally {
            fio.close();
        }
        String url = config.getProperty("url");
        String driver = config.getProperty("driver");
        String user = config.getProperty("user");
        String password = config.getProperty("password");

        Class.forName(driver);

        return DriverManager.getConnection(url, user, password);

    }

    /**
     * <p>
     * Gets the sql file content.
     * </p>
     *
     * @param file The sql file to get its content.
     * @return The content of sql file.
     * @throws Exception to JUnit
     */
    private static String getSql(String file) throws Exception {
        StringBuilder sql = new StringBuilder();

        BufferedReader in = new BufferedReader(new FileReader(file));

        try {
            for (String s = in.readLine(); s != null; s = in.readLine()) {
                int commentIndex = s.indexOf("--");

                if (commentIndex >= 0) { // Remove any SQL comment
                    s = s.substring(0, commentIndex);
                }

                sql.append(s).append(' '); // The BufferedReader drops newlines so insert whitespace
            }
        } finally {
            in.close();
        }

        return sql.toString();
    }

}
