/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.failuretests;

import com.topcoder.configuration.BaseConfigurationObject;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Failure test cases for BaseConfigurationObject.
 * </p>
 *
 * @author biotrail
 * @version 1.0
 */
public class BaseConfigurationObjectFailureTests extends TestCase {
    /**
     * <p>
     * The BaseConfigurationObject instance for testing.
     * </p>
     */
    private BaseConfigurationObject base;

    /**
     * <p>
     * Setup test environment.
     * </p>
     *
     */
    protected void setUp() {
        base = new MockBaseConfigurationObject();
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    protected void tearDown() {
        base = null;
    }

    /**
     * <p>
     * Return all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(BaseConfigurationObjectFailureTests.class);
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#containsProperty(String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testContainsProperty() throws Exception {
        try {
            base.containsProperty("key");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#getPropertyValue(String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testGetPropertyValue() throws Exception {
        try {
            base.getPropertyValue("key");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#getPropertyValuesCount(String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testGetPropertyValuesCount() throws Exception {
        try {
            base.getPropertyValuesCount("key");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#getPropertyValues(String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testGetPropertyValues() throws Exception {
        try {
            base.getPropertyValues("key");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#setPropertyValue(String,Object) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testSetPropertyValue1() throws Exception {
        try {
            base.setPropertyValue("key", "value");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#setPropertyValue(String,String,Object) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testSetPropertyValue2() throws Exception {
        try {
            base.setPropertyValue("path", "key", "value");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#setPropertyValues(String,Object[]) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testSetPropertyValues1() throws Exception {
        try {
            base.setPropertyValues("key", new Object[] {"key"});
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#setPropertyValues(String,String,Object[]) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testSetPropertyValues2() throws Exception {
        try {
            base.setPropertyValues("path", "key", new Object[] {"key"});
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#removeProperty(String,String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testRemoveProperty1() throws Exception {
        try {
            base.removeProperty("path", "key");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#removeProperty(String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testRemoveProperty2() throws Exception {
        try {
            base.removeProperty("path");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#clearProperties(String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testClearProperties() throws Exception {
        try {
            base.clearProperties("path");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#getPropertyKeys(String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testGetPropertyKeys() throws Exception {
        try {
            base.getPropertyKeys("pattern");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#containsChild(String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testContainsChild() throws Exception {
        try {
            base.containsChild("name");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#addChild(String,ConfigurationObject) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testAddChild1() throws Exception {
        try {
            base.addChild("path", base);
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#addChild(ConfigurationObject) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testAddChild2() throws Exception {
        try {
            base.addChild(base);
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#clearChildren(String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testClearChildren1() throws Exception {
        try {
            base.clearChildren("path");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#clearChildren() for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testClearChildren2() throws Exception {
        try {
            base.clearChildren();
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#getAllChildrenNames() for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testGetAllChildrenNames() throws Exception {
        try {
            base.getAllChildrenNames();
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#getAllChildren() for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testGetAllChildren() throws Exception {
        try {
            base.getAllChildren();
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#getAllDescendants() for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testGetAllDescendants() throws Exception {
        try {
            base.getAllDescendants();
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#getDescendants(String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testGetDescendants() throws Exception {
        try {
            base.getDescendants("pattern");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#findDescendants(String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testFindDescendants() throws Exception {
        try {
            base.findDescendants("path");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#deleteDescendants(String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testDeleteDescendants() throws Exception {
        try {
            base.deleteDescendants("path");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#processDescendants(String,Processor) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testProcessDescendants() throws Exception {
        try {
            base.processDescendants("path", new MockProcessor());
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#getName() for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testGetName() throws Exception {
        try {
            base.getName();
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#getChild(String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testGetChild() throws Exception {
        try {
            base.getChild("name");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#removeChild(String,String) for failure.
     * It tests the case that when the method is called and expects UnsupportedOperationException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testRemoveChild1() throws Exception {
        try {
            base.removeChild("path", "name");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#removeChild(String) for failure.
     * It tests the case that when name is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testRemoveChild2() throws Exception {
        try {
            base.removeChild("name");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests BaseConfigurationObject#getChildren(String) for failure.
     * It tests the case that when name is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception to JUnit
     */
    public void testGetChildren() throws Exception {
        try {
            base.getChildren("pattern");
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            //good
        }
    }

}