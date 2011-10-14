/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import java.util.Properties;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;


/**
 * <p>
 * Unit test case of {@link Helper}.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class HelperTest {
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(HelperTest.class);
    }

    /**
     * <p>
     * Accuracy test method for {@link Helper#checkNull(java.lang.Object, java.lang.String)} when object is not
     * null.
     * </p>
     */
    @Test
    public void testCheckNull1() {
        Helper.checkNull(new Integer(1), "message");
    }

    /**
     * <p>
     * Failure test method for {@link Helper#checkNull(java.lang.Object, java.lang.String)} when object is null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckNull2() {
        Helper.checkNull(null, "message");
    }

    /**
     * <p>
     * Accuracy test method for {@link Helper#checkNullOrEmpty(java.lang.String, java.lang.String)} when value is
     * not null and not empty.
     * </p>
     */
    @Test
    public void testCheckNullOrEmpty1() {
        Helper.checkNullOrEmpty("value", "message");
    }

    /**
     * <p>
     * Failure test method for {@link Helper#checkNullOrEmpty(java.lang.String, java.lang.String)} when value is
     * null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullOrEmpty2() {
        Helper.checkNullOrEmpty(null, "message");
    }

    /**
     * <p>
     * Failure test method for {@link Helper#checkNullOrEmpty(java.lang.String, java.lang.String)} when value is
     * empty.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullOrEmpty3() {
        Helper.checkNullOrEmpty(" ", "message");
    }

    /**
     * <p>
     * Accuracy test method for
     * {@link Helper#getProperty(java.util.Properties, java.lang.String, java.lang.String)} when property exists.
     * </p>
     */
    @Test
    public void testGetProperty1_Exists() {
        Properties prop = new Properties();
        prop.setProperty("key", "value");
        String value = Helper.getProperty(prop, "key", "default");
        Assert.assertEquals("Incorrect property value", "value", value);
    }

    /**
     * <p>
     * Accuracy test method for
     * {@link Helper#getProperty(java.util.Properties, java.lang.String, java.lang.String)} when property does not
     * exist.
     * </p>
     */
    @Test
    public void testGetProperty1_NotExist() {
        Properties prop = new Properties();
        prop.setProperty("key", "value");
        String value = Helper.getProperty(prop, "key1", "default");
        Assert.assertEquals("Incorrect property value", "default", value);
    }

    /**
     * <p>
     * Accuracy test method for
     * {@link Helper#getProperty(java.util.Properties, java.lang.String, java.lang.String)} when property value is
     * not a string.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testGetProperty1_NotString() {
        Properties prop = new Properties();
        prop.put("key", 1L);
        Helper.getProperty(prop, "key", "default");
    }

    /**
     * <p>
     * Accuracy test method for
     * {@link Helper#getProperty(java.util.Properties, java.lang.String, java.lang.String)} when property value is
     * an empty string.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testGetProperty1_EmptyString() {
        Properties prop = new Properties();
        prop.setProperty("key", "  ");
        Helper.getProperty(prop, "key", "default");
    }

    /**
     * <p>
     * Accuracy test method for {@link Helper#getProperty(java.util.Properties, java.lang.String, boolean)} when
     * property exists.
     * </p>
     */
    @Test
    public void testGetProperty2_Exists() {
        Properties prop = new Properties();
        prop.setProperty("key", "value");
        String value = Helper.getProperty(prop, "key", true);
        Assert.assertEquals("Incorrect required property value", "value", value);

        value = Helper.getProperty(prop, "key", false);
        Assert.assertEquals("Incorrect optional property value", "value", value);
    }

    /**
     * <p>
     * Accuracy test method for {@link Helper#getProperty(java.util.Properties, java.lang.String, boolean)} when
     * required property is missing.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testGetProperty2_MissingRequired() {
        Properties prop = new Properties();
        prop.setProperty("key", "value");
        Helper.getProperty(prop, "key1", true);
    }

    /**
     * <p>
     * Accuracy test method for {@link Helper#getProperty(java.util.Properties, java.lang.String, boolean)} when
     * optional property is missing.
     * </p>
     */
    @Test
    public void testGetProperty2_MissingOptional() {
        Properties prop = new Properties();
        prop.setProperty("key", "value");
        String value = Helper.getProperty(prop, "key1", false);
        Assert.assertNull("Mission optioanl property value should be null", value);
    }

    /**
     * <p>
     * Accuracy test method for {@link Helper#getProperty(java.util.Properties, java.lang.String, boolean)} when
     * property value is not a string.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testGetProperty2_NotString() {
        Properties prop = new Properties();
        prop.put("key", 1L);
        Helper.getProperty(prop, "key", false);
    }

    /**
     * <p>
     * Accuracy test method for {@link Helper#getProperty(java.util.Properties, java.lang.String, boolean)} when
     * property value is an empty string.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testGetProperty2_EmptyString() {
        Properties prop = new Properties();
        prop.put("key", "  ");
        Helper.getProperty(prop, "key", true);
    }

    /**
     * <p>
     * Accuracy test method for {@link Helper#getIntProperty(java.util.Properties, java.lang.String, int)} when
     * property exists.
     * </p>
     */
    @Test
    public void testGetIntProperty_Exists() {
        Properties prop = new Properties();
        prop.setProperty("key", "1");
        int value = Helper.getIntProperty(prop, "key", 2);
        Assert.assertEquals("Incorrect property value", 1, value);
    }

    /**
     * <p>
     * Accuracy test method for {@link Helper#getIntProperty(java.util.Properties, java.lang.String, int)} when
     * property does not exist.
     * </p>
     */
    @Test
    public void testGetProperty_NotExist() {
        Properties prop = new Properties();
        prop.setProperty("key", "1");
        int value = Helper.getIntProperty(prop, "key1", 2);
        Assert.assertEquals("Incorrect property value", 2, value);
    }

    /**
     * <p>
     * Accuracy test method for {@link Helper#getIntProperty(java.util.Properties, java.lang.String, int)} when
     * property value is not a string.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testGetIntProperty_NotString() {
        Properties prop = new Properties();
        prop.put("key", 1L);
        Helper.getIntProperty(prop, "key", 1);
    }

    /**
     * <p>
     * Accuracy test method for {@link Helper#getIntProperty(java.util.Properties, java.lang.String, int)} when
     * property value is an empty string.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testGetIntProperty_EmptyString() {
        Properties prop = new Properties();
        prop.setProperty("key", "  ");
        Helper.getIntProperty(prop, "key", 2);
    }

    /**
     * <p>
     * Accuracy test method for {@link Helper#getIntProperty(java.util.Properties, java.lang.String, int)} when
     * property value cannot be parsed as integer.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testGetIntProperty_Invalid() {
        Properties prop = new Properties();
        prop.setProperty("key", "one");
        Helper.getIntProperty(prop, "key", 2);
    }

    /**
     * <p>
     * Accuracy test method for {@link Helper#getIntProperty(java.util.Properties, java.lang.String, int)} when
     * property value is negative.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testGetIntProperty_Illegal() {
        Properties prop = new Properties();
        prop.setProperty("key", "-2");
        Helper.getIntProperty(prop, "key", 2);
    }

    /**
     * <p>
     * Accuracy test method for {@link Helper#concat(java.lang.Object[])}.
     * </p>
     */
    @Test
    public void testConcat() {
        String value = Helper.concat("Value = ", 1);
        Assert.assertEquals("Incorrect result", "Value = 1", value);
    }
}
