/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate;

import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.TestHelper;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;


/**
 * <p>
 * Unit test cases for <code>HibernateHelper</code>.
 * </p>
 *
 * @author myxgyy
 * @version 1.4
 */
public class HibernateHelperTests extends TestCase {
    /**
     * The setUp of the unit test.
     *
     * @throws Exception
     *             to Junit
     */
    protected void setUp() throws Exception {
        // add the configuration
        TestHelper.clearConfig();
        ConfigManager.getInstance().add("HibernateHelper.xml");
    }

    /**
     * The tearDown of the unit test.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        // remove the configuration in tearDown
        TestHelper.clearConfig();
    }

    /**
     * Accuracy test case for
     * {@link HibernateHelper#getConfigPropertyValue(String, String, boolean)}.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetConfigPropertyValueAccuracy1() throws Exception {
        String value = HibernateHelper.getConfigPropertyValue("HibernateHelper",
                "hibernateConfigFilePath", true);
        assertEquals("should be same value", value,
            "sampleHibernateConfig.cfg.xml");
    }

    /**
     * Accuracy test case for
     * {@link HibernateHelper#getConfigPropertyValue(String, String, boolean)}.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetConfigPropertyValueAccuracy2() throws Exception {
        String value = HibernateHelper.getConfigPropertyValue("HibernateHelper",
                "not exist", false);
        assertNull("should be null", value);
    }

    /**
     * <p>
     * Failure test case for
     * {@link HibernateHelper#getConfigPropertyValue(String, String, boolean)}.
     * </p>
     * <p>
     * The namespace does not exist, SearchBuilderConfigurationException expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetConfigPropertyValueFailure1() throws Exception {
        try {
            HibernateHelper.getConfigPropertyValue("not known",
                "hibernateConfigFilePath", true);
            fail("should have thrown SearchBuilderConfigurationException");
        } catch (SearchBuilderConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for
     * {@link HibernateHelper#getConfigPropertyValue(String, String, boolean)}.
     * </p>
     * <p>
     * The property does not exist when mandatory is true,
     * SearchBuilderConfigurationException expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetConfigPropertyValueFailure2() throws Exception {
        try {
            HibernateHelper.getConfigPropertyValue("HibernateHelper",
                "not exit", true);
            fail("should have thrown SearchBuilderConfigurationException");
        } catch (SearchBuilderConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for
     * {@link HibernateHelper#getConfigPropertyValue(String, String, boolean)}.
     * </p>
     * <p>
     * The property is empty when mandatory is true, SearchBuilderConfigurationException
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetConfigPropertyValueFailure3() throws Exception {
        try {
            HibernateHelper.getConfigPropertyValue("HibernateHelper", "empty",
                true);
            fail("should have thrown SearchBuilderConfigurationException");
        } catch (SearchBuilderConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for
     * {@link HibernateHelper#getConfigPropertyValue(String, String, boolean)}.
     * </p>
     * <p>
     * The property is empty when mandatory is false, SearchBuilderConfigurationException
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetConfigPropertyValueFailure4() throws Exception {
        try {
            HibernateHelper.getConfigPropertyValue("HibernateHelper", "empty",
                false);
            fail("should have thrown SearchBuilderConfigurationException");
        } catch (SearchBuilderConfigurationException e) {
            // pass
        }
    }
}
