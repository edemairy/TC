/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.accuracytests;

import junit.framework.Assert;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Field;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.ibm.support.electronic.cache.model.CachedObject;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * the helper class for accuracy test.
 *
 * @author KLW
 * @version 1.0
 */
public class AccuracyTestHelper {
    /**
     * the configuration file path.
     */
    private static final String CONFIG_PATH = "test_files/accuracytests/config.properties";

    /**
     * <p>
     * Private constructor. Make sure no instance of this class will be created.
     * </p>
     */
    private AccuracyTestHelper() {
        // empty
    }

    /**
     * check the value of instance by the field name.
     *
     * @param fieldName
     *            the field name
     * @param instance
     *            the instance
     * @param expected
     *            the expected value
     * @throws Exception
     *             throw exception to junit.
     */
    public static void assertFieldValue(String fieldName, Object instance, Object expected) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Assert.assertEquals("The " + fieldName + " is incorrect.", expected, field.get(instance));
    }

    /**
     * Asserts the contents of the log file.
     *
     * @param logFile
     *            the log file which contains the log information
     * @param expectedContents
     *            the expected contents
     * @throws Exception
     *             to JUnit
     */
    public static void assertFileContents(String logFile, String[] expectedContents) throws Exception {
        StringBuffer msg = readFile(logFile);

        for (String expectedContent : expectedContents) {
            assertTrue("The log file should contain the information : " + expectedContent,
                    msg.indexOf(expectedContent) >= 0);
        }
    }

    /**
     * Closes the file reader.
     *
     * @param reader
     *            the file reader to close
     */
    private static void closeFileReader(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                // logError(new JobExecutionException("There exists an error when closing the file reader", e));
            }
        }
    }

    /**
     * Reads the given file.
     *
     * @param path
     *            the path to the file to read
     * @return the contents of the file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private static StringBuffer readFile(String path) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));

            String line;
            StringBuffer sb = new StringBuffer();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            return sb;
        } finally {
            closeFileReader(br);
        }
    }

    /**
     * close the OutputStream.
     *
     * @param out
     *            the OutputStream to close.
     */
    public static void closeOutputStream(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * get the properties in "test_files/config.properties".
     * @return the properties.
     * @throws IOException if any ioException occurs.
     */
    public static Properties getProperties() throws IOException {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(CONFIG_PATH);
            prop.load(in);
        } catch (IOException e) {
            // ignore
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return prop;
    }

    /**
     * get data base connection with the properties set in "test_files/config.properties".
     *
     * @return the db connection
     * @throws Exception
     *             if any error occurs.
     */
    public static Connection getDBConnection() throws Exception {
        Properties prop = getProperties();
        String driver = prop.getProperty("driver");
        if (driver == null) {
            driver = "com.ibm.db2.jcc.DB2Driver";
        }
        Class.forName(driver);
        String url = prop.getProperty("url");
        String user = prop.getProperty("user");
        String password = prop.getProperty("password");
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * check the instance of CachedObject.
     *
     * @param result
     *            the actual result.
     * @param expect
     *            the expect value.
     */
    public static void checkCacheObject(CachedObject result, CachedObject expect) {
        assertEquals("The cache name of cachedObject is incorrect.", expect.getCacheSetName(),
                result.getCacheSetName());
        assertEquals("The id of cachedObject is incorrect.", expect.getId(), result.getId());
        assertArrayEquals("The content of cachedObject is incorrect.", expect.getContent(), result.getContent());
    }

    /**
     * check the instance of Foo.
     *
     * @param result
     *            the actual result.
     * @param expect
     *            the expect value.
     */
    public static void assertFoo(Foo expect, Object result) {
        Foo foo = (Foo) result;
        assertEquals("The age of foo is incorrect.", expect.getAge(), foo.getAge());
        assertEquals("The name of foo is incorrect.", expect.getName(), foo.getName());
    }
}
