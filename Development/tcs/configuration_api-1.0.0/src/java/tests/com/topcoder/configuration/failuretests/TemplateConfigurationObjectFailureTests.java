/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.failuretests;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.TemplateConfigurationObject;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Failure test cases for TemplateConfigurationObject.
 * </p>
 *
 * @author biotrail
 * @version 1.0
 */
public class TemplateConfigurationObjectFailureTests extends TestCase {
    /**
     * <p>
     * The TemplateConfigurationObject instance for testing.
     * </p>
     */
    private TemplateConfigurationObject object;

    /**
     * <p>
     * Setup test environment.
     * </p>
     *
     */
    protected void setUp() {
        object = new MockTemplateConfigurationObject();
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    protected void tearDown() {
        object = null;
    }

    /**
     * <p>
     * Return all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(TemplateConfigurationObjectFailureTests.class);
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#setPropertyValue(String,String,Object) for failure.
     * It tests the case that when path is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testSetPropertyValue_NullPath() throws Exception {
        try {
            object.setPropertyValue(null, "key", "value");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#setPropertyValue(String,String,Object) for failure.
     * It tests the case that when key is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testSetPropertyValue_NullKey() throws Exception {
        try {
            object.setPropertyValue("path", null, "value");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#setPropertyValue(String,String,Object) for failure.
     * It tests the case that when key is empty and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testSetPropertyValue_EmptyKey() throws Exception {
        try {
            object.setPropertyValue("path", " ", "value");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#setPropertyValues(String,String,Object[]) for failure.
     * It tests the case that when path is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testSetPropertyValues_NullPath() throws Exception {
        try {
            object.setPropertyValues(null, "key", new Object[] {"values"});
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#setPropertyValues(String,String,Object[]) for failure.
     * It tests the case that when key is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testSetPropertyValues_NullKey() throws Exception {
        try {
            object.setPropertyValues("path", null, new Object[] {"values"});
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#setPropertyValues(String,String,Object[]) for failure.
     * It tests the case that when key is empty and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testSetPropertyValues_EmptyKey() throws Exception {
        try {
            object.setPropertyValues("path", " ", new Object[] {"values"});
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#removeProperty(String,String) for failure.
     * It tests the case that when path is null and expects IllegalArgumentException.
     * </p>
     * @throws ConfigurationAccessException to JUnit
     */
    public void testRemoveProperty_NullPath() throws ConfigurationAccessException {
        try {
            object.removeProperty(null, "key");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#removeProperty(String,String) for failure.
     * It tests the case that when key is null and expects IllegalArgumentException.
     * </p>
     * @throws ConfigurationAccessException to JUnit
     */
    public void testRemoveProperty_NullKey() throws ConfigurationAccessException {
        try {
            object.removeProperty("path", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#removeProperty(String,String) for failure.
     * It tests the case that when key is empty and expects IllegalArgumentException.
     * </p>
     * @throws ConfigurationAccessException to JUnit
     */
    public void testRemoveProperty_EmptyKey() throws ConfigurationAccessException {
        try {
            object.removeProperty("path", " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#clearProperties(String) for failure.
     * It tests the case that when path is null and expects IllegalArgumentException.
     * </p>
     * @throws ConfigurationAccessException to JUnit
     */
    public void testClearProperties_NullPath() throws ConfigurationAccessException {
        try {
            object.clearProperties(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#addChild(String,ConfigurationObject) for failure.
     * It tests the case that when path is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testAddChild_NullPath() throws Exception {
        try {
            object.addChild(null, new MockBaseConfigurationObject());
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#addChild(String,ConfigurationObject) for failure.
     * It tests the case that when child is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testAddChild_NullChild() throws Exception {
        try {
            object.addChild("path", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#clearChildren(String) for failure.
     * It tests the case that when path is null and expects IllegalArgumentException.
     * </p>
     * @throws ConfigurationAccessException to JUnit
     */
    public void testClearChildren_NullPath() throws ConfigurationAccessException {
        try {
            object.clearChildren(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#processDescendants(String,Processor) for failure.
     * It tests the case that when path is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testProcessDescendants_NullPath() throws Exception {
        try {
            object.processDescendants(null, new MockProcessor());
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#processDescendants(String,Processor) for failure.
     * It tests the case that when processor is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testProcessDescendants_NullProcessor() throws Exception {
        try {
            object.processDescendants("path", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#removeChild(String,String) for failure.
     * It tests the case that when path is null and expects IllegalArgumentException.
     * </p>
     * @throws ConfigurationAccessException to JUnit
     */
    public void testRemoveChild_NullPath() throws ConfigurationAccessException {
        try {
            object.removeChild(null, "name");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#removeChild(String,String) for failure.
     * It tests the case that when name is null and expects IllegalArgumentException.
     * </p>
     * @throws ConfigurationAccessException to JUnit
     */
    public void testRemoveChild_NullName() throws ConfigurationAccessException {
        try {
            object.removeChild("path", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests TemplateConfigurationObject#removeChild(String,String) for failure.
     * It tests the case that when name is empty and expects IllegalArgumentException.
     * </p>
     * @throws ConfigurationAccessException to JUnit
     */
    public void testRemoveChild_EmptyName() throws ConfigurationAccessException {
        try {
            object.removeChild("path", " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }
}