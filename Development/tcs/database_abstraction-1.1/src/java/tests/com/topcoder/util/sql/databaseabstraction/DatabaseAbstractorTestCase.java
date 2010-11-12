/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import junit.framework.TestCase;

/**
 * <p>
 * This tests DatabaseAbstractor.
 * </p>
 *
 * @author WishingBone,justforplay
 * @version 1.1
 * @since 1.0
 */
public class DatabaseAbstractorTestCase extends TestCase {

    /**
     * Database connection.
     */
    private Connection connection;

    /**
     * Instance of java sql statement.
     */
    private Statement statement;

    /**
     * Set up database connection.
     *
     * @throws Exception exception to JUnit.
     */
    protected void setUp() throws Exception {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        connection = DriverManager.getConnection("jdbc:odbc:Tester");
        statement = connection.createStatement();
    }

    /**
     * Close database connection.
     *
     * @throws Exception exception to JUnit.
     */
    protected void tearDown() throws Exception {
        connection.close();
    }

    /**
     * Test convert result set.
     */
    public void testDatabaseAbstractor() {
        try {
            ResultSet rs = statement.executeQuery("select * from tests");
            DatabaseAbstractor da = new DatabaseAbstractor();
            CustomResultSet crs = da.convertResultSet(rs);
            assertTrue(crs.getRecordCount() == 5);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test remap data when converting.
     */
    public void testDatabaseAbstractorRemap() {
        try {
            ResultSet rs = statement.executeQuery("select * from tests");
            HashMap map = new HashMap();
            map.put("VARCHAR", new Converter() {
                public Object convert(Object obj, int column, CustomResultSetMetaData metaData) {
                    return "" + ((String) obj).substring(1) + ((String) obj).substring(0, 1);
                }
            });
            DatabaseAbstractor da = new DatabaseAbstractor(new Mapper(map));
            CustomResultSet crs = da.convertResultSet(rs);
            crs.sortAscending("D_Text_To_Number");
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("32"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("54"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("54"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("76"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("98"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
