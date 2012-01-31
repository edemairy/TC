/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;


/**
 * <p>
 * A class providing helper methods for testing.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public final class TestHelper {
    /**
     * <p>
     * Represents the test_files directory.
     * </p>
     */
    public static final String TEST_FILES = "test_files" + File.separator;

    /**
     * <p>
     * The JDBC configuration file.
     * </p>
     */
    public static final String JDBC_PROPERTIES = TEST_FILES + "jdbc.properties";

    /**
     * <p>
     * The manager configuration file.
     * </p>
     */
    public static final String CONFIG_PROPERTIES = TEST_FILES + "config.properties";

    /**
     * <p>
     * Private modifier prevents the creation of new instance.
     * </p>
     */
    private TestHelper() {
    }

    /**
     * <p>
     * Store properties into the given file.
     * </p>
     *
     * @param p the properties to store.
     * @param file the file.
     * @throws Exception if any error occurs while storing the properties.
     */
    public static void storeProperties(Properties p, String file) throws Exception {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            p.store(fos, null);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * <p>
     * Copies file content from source to destination.
     * </p>
     *
     * @param fromFile the source file.
     * @param toFile the destination file.
     * @throws Exception if any error occurs while copying the file.
     */
    public static void copyFile(String fromFile, String toFile) throws Exception {
        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            from = new FileInputStream(fromFile);
            to = new FileOutputStream(toFile);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = from.read(buffer)) > 0) {
                to.write(buffer, 0, bytesRead); // write
            }
        } finally {
            if (from != null) {
                from.close();
            }
            if (to != null) {
                to.close();
            }
        }
    }

    /**
     * <p>
     * Loads properties list in the specified file path.
     * </p>
     *
     * @param configFile the properties file.
     * @return loaded properties.
     * @throws Exception if any error occurs while loading properties.
     */
    public static Properties loadProperties(String configFile) throws Exception {
        InputStream is = null;
        Properties properties = new Properties();

        try {
            is = new FileInputStream(configFile);
            properties.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return properties;
    }

    /**
     * <p>
     * Gets the field value of given object by using reflection. If field with given name is not found in the obj'
     * class then the field is searched in obj' superclass.
     * </p>
     *
     * @param obj the object to get its field value.
     * @param fieldName the field name.
     * @return the field value.
     * @throws Exception if any error occurs while retrieving field value.
     */
    public static Object getField(Object obj, String fieldName) throws Exception {
        Field field = null;
        try {
            // get the class
            Class<?> cls = obj.getClass();
            try {
                // get the field
                field = cls.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // field is not found
                // check the superclass
                field = cls.getSuperclass().getDeclaredField(fieldName);
            }
            field.setAccessible(true);
            return field.get(obj);
        } finally {
            if (field != null) {
                field.setAccessible(false);
            }
        }
    }

    /**
     * Executes list of INSERT/DELETE queries.
     *
     * @param queries the queries to execute.
     * @throws Exception if any error occurs.
     */
    private static void executeUpdate(List<String> queries) throws Exception {
        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);
            for (String query : queries) {
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.executeUpdate();
                pstmt.close();
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                // ignore
            }
            e.printStackTrace();
            throw e;
        } finally {
            close(conn);
        }
    }

    /**
     * <p>
     * Create a connection to database.
     * </p>
     *
     * @return database connection.
     * @throws Exception if any error occurs.
     */
    public static Connection getConnection() throws Exception {
        Properties prop = loadProperties(JDBC_PROPERTIES);
        Class.forName(prop.getProperty("driver"));
        return DriverManager.getConnection(prop.getProperty("url"), prop);
    }

    /**
     * <p>
     * Init the database with some data.
     * </p>
     *
     * @throws Exception if any error occurs.
     */
    public static void initDB() throws Exception {
        List<String> queries = new ArrayList<String>();
        queries.add("INSERT INTO CACHED_OBJECT VALUES('obj1', 'default', CAST(X'74657374' AS BLOB(1M)))");
        queries.add("INSERT INTO CACHED_OBJECT VALUES('obj2', 'default', CAST(X'74657374' AS BLOB(1M)))");
        queries.add("INSERT INTO CACHED_OBJECT VALUES('obj3', 'custom', CAST(X'74657374' AS BLOB(1M)))");
        executeUpdate(queries);
    }

    /**
     * <p>
     * Clears the database.
     * </p>
     *
     * @throws Exception if any error occurs.
     */
    public static void clearDB() throws Exception {
        executeUpdate(Arrays.asList("DELETE FROM CACHED_OBJECT"));
    }

    /**
     * <p>
     * Close database connection.
     * </p>
     *
     * @param conn the connection to close.
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    /**
     * <p>
     * Creates Foo object for testing.
     * </p>
     *
     * @param name the name.
     * @param age the age.
     * @return Foo object.
     */
    public static Foo createFoo(String name, int age) {
        Foo foo = new Foo();
        foo.setAge(age);
        foo.setName(name);
        return foo;
    }

    /**
     * <p>
     * Asserts the given two Foo objects are equal.
     * </p>
     *
     * @param foo1 the Foo object.
     * @param foo2 the Foo object.
     */
    public static void assertFooEquals(Foo foo1, Foo foo2) {
        Assert.assertEquals("Incorrect foo' age", foo1.getAge(), foo2.getAge());
        Assert.assertEquals("Incorrect foo' name", foo1.getName(), foo2.getName());
    }
}
