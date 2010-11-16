/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.failuretests;

import com.topcoder.configuration.InvalidConfigurationException;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Failure test cases for DefaultConfigurationObject.
 * </p>
 *
 * @author biotrail
 * @version 1.0
 */
public class DefaultConfigurationObjectFailureTests extends TestCase {
    /**
     * <p>
     * The DefaultConfigurationObject instance for testing.
     * </p>
     */
    private DefaultConfigurationObject defaultObject;

    /**
     * <p>
     * Setup test environment.
     * </p>
     *
     */
    protected void setUp() {
        defaultObject = new DefaultConfigurationObject("name");
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    protected void tearDown() {
        defaultObject = null;
    }

    /**
     * <p>
     * Return all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(DefaultConfigurationObjectFailureTests.class);
    }

    /**
     * <p>
     * Tests ctor DefaultConfigurationObject#DefaultConfigurationObject(String) for failure.
     * It tests the case that when name is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullName() {
        try {
            new DefaultConfigurationObject(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor DefaultConfigurationObject#DefaultConfigurationObject(String) for failure.
     * It tests the case that when name is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_EmptyName() {
        try {
            new DefaultConfigurationObject(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#containsProperty(String) for failure.
     * It tests the case that when key is null and expects IllegalArgumentException.
     * </p>
     */
    public void testContainsProperty_NullKey() {
        try {
            defaultObject.containsProperty(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#containsProperty(String) for failure.
     * It tests the case that when key is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testContainsProperty_EmptyKey() {
        try {
            defaultObject.containsProperty(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#getPropertyValue(String) for failure.
     * It tests the case that when key is null and expects IllegalArgumentException.
     * </p>
     */
    public void testGetPropertyValue_NullKey() {
        try {
            defaultObject.getPropertyValue(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#getPropertyValue(String) for failure.
     * It tests the case that when key is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testGetPropertyValue_EmptyKey() {
        try {
            defaultObject.getPropertyValue(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#getPropertyValuesCount(String) for failure.
     * It tests the case that when key is null and expects IllegalArgumentException.
     * </p>
     */
    public void testGetPropertyValuesCount_NullKey() {
        try {
            defaultObject.getPropertyValuesCount(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#getPropertyValuesCount(String) for failure.
     * It tests the case that when key is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testGetPropertyValuesCount_EmptyKey() {
        try {
            defaultObject.getPropertyValuesCount(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#getPropertyValues(String) for failure.
     * It tests the case that when key is null and expects IllegalArgumentException.
     * </p>
     */
    public void testGetPropertyValues_NullKey() {
        try {
            defaultObject.getPropertyValues(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#getPropertyValues(String) for failure.
     * It tests the case that when key is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testGetPropertyValues_EmptyKey() {
        try {
            defaultObject.getPropertyValues(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#setPropertyValue(String,Object) for failure.
     * It tests the case that when key is null and expects IllegalArgumentException.
     * </p>
     * @throws InvalidConfigurationException to JUnit
     */
    public void testSetPropertyValue_NullKey() throws InvalidConfigurationException {
        try {
            defaultObject.setPropertyValue(null, "value");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#setPropertyValue(String,Object) for failure.
     * It tests the case that when key is empty and expects IllegalArgumentException.
     * </p>
     * @throws InvalidConfigurationException to JUnit
     */
    public void testSetPropertyValue_EmptyKey() throws InvalidConfigurationException {
        try {
            defaultObject.setPropertyValue(" ", "value");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#setPropertyValue(String,Object) for failure.
     * It tests the case that when value is null and expects IllegalArgumentException.
     * </p>
     * @throws InvalidConfigurationException to JUnit
     */
    public void testSetPropertyValue_NullValue() throws InvalidConfigurationException {
        try {
            defaultObject.setPropertyValue(" ", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#setPropertyValue(String,Object) for failure.
     * It tests the case that when value is not Serializable type and expects InvalidConfigurationException.
     * </p>
     */
    public void testSetPropertyValue_InvalidConfigurationException() {
        try {
            defaultObject.setPropertyValue("key", new Object());
            fail("InvalidConfigurationException expected.");
        } catch (InvalidConfigurationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#setPropertyValues(String,Object[]) for failure.
     * It tests the case that when key is null and expects IllegalArgumentException.
     * </p>
     * @throws InvalidConfigurationException to JUnit
     */
    public void testSetPropertyValues_NullKey() throws InvalidConfigurationException {
        try {
            defaultObject.setPropertyValues(null, new Object[] {"value"});
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#setPropertyValues(String,Object[]) for failure.
     * It tests the case that when key is empty and expects IllegalArgumentException.
     * </p>
     * @throws InvalidConfigurationException to JUnit
     */
    public void testSetPropertyValues_EmptyKey() throws InvalidConfigurationException {
        try {
            defaultObject.setPropertyValues(" ", new Object[] {"value"});
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#setPropertyValues(String,Object[]) for failure.
     * It tests the case that when values contains elements which are not Serializable
     * type and expects InvalidConfigurationException.
     * </p>
     */
    public void testSetPropertyValues_InvalidConfigurationException() {
        try {
            defaultObject.setPropertyValues("key", new Object[] {new Object()});
            fail("InvalidConfigurationException expected.");
        } catch (InvalidConfigurationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#removeProperty(String) for failure.
     * It tests the case that when key is null and expects IllegalArgumentException.
     * </p>
     */
    public void testRemoveProperty_NullKey() {
        try {
            defaultObject.removeProperty(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#removeProperty(String) for failure.
     * It tests the case that when key is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testRemoveProperty_EmptyKey() {
        try {
            defaultObject.removeProperty(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#getPropertyKeys(String) for failure.
     * It tests the case that when pattern is null and expects IllegalArgumentException.
     * </p>
     */
    public void testGetPropertyKeys_NullPattern() {
        try {
            defaultObject.getPropertyKeys(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#getPropertyKeys(String) for failure.
     * It tests the case that when pattern is invalid and expects IllegalArgumentException.
     * </p>
     */
    public void testGetPropertyKeys_InvalidPattern() {
        try {
            defaultObject.getPropertyKeys("[^DD##$$%%^^");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#containsChild(String) for failure.
     * It tests the case that when name is null and expects IllegalArgumentException.
     * </p>
     */
    public void testContainsChild_NullName() {
        try {
            defaultObject.containsChild(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#containsChild(String) for failure.
     * It tests the case that when name is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testContainsChild_EmptyName() {
        try {
            defaultObject.containsChild(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#addChild(ConfigurationObject) for failure.
     * It tests the case that when child is null and expects IllegalArgumentException.
     * </p>
     * @throws InvalidConfigurationException to JUnit
     */
    public void testAddChild_NullChild() throws InvalidConfigurationException {
        try {
            defaultObject.addChild(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#addChild(ConfigurationObject) for failure.
     * It tests the case when child is not the DefaultConfigurationObject and expects
     * InvalidConfigurationException.
     * </p>
     */
    public void testAddChild_NotDefaultConfigurationObjectType() {
        try {
            defaultObject.addChild(new MockBaseConfigurationObject());
            fail("InvalidConfigurationException expected.");
        } catch (InvalidConfigurationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#addChild(ConfigurationObject) for failure.
     * It tests the case when child is the current node and expects
     * InvalidConfigurationException.
     * </p>
     */
    public void testAddChild_CurrentNode() {
        try {
            defaultObject.addChild(defaultObject);
            fail("InvalidConfigurationException expected.");
        } catch (InvalidConfigurationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#addChild(ConfigurationObject) for failure.
     * It tests the case when adding the child will result in an cycle and expects
     * InvalidConfigurationException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testAddChild_Cycle() throws Exception {
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        child.addChild(defaultObject);

        try {
            defaultObject.addChild(child);
            fail("InvalidConfigurationException expected.");
        } catch (InvalidConfigurationException e) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#getDescendants(String) for failure.
     * It tests the case that when pattern is null and expects IllegalArgumentException.
     * </p>
     */
    public void testGetDescendants_NullPattern() {
        try {
            defaultObject.getDescendants(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#getDescendants(String) for failure.
     * It tests the case that when pattern is invalid and expects IllegalArgumentException.
     * </p>
     */
    public void testGetDescendants_InvalidPattern() {
        try {
            defaultObject.getDescendants("[^DD##$$%%^^");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#findDescendants(String) for failure.
     * It tests the case that when path is null and expects IllegalArgumentException.
     * </p>
     */
    public void testFindDescendants_NullPath() {
        try {
            defaultObject.findDescendants(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#deleteDescendants(String) for failure.
     * It tests the case that when path is null and expects IllegalArgumentException.
     * </p>
     */
    public void testDeleteDescendants_NullPath() {
        try {
            defaultObject.deleteDescendants(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#getChild(String) for failure.
     * It tests the case that when name is null and expects IllegalArgumentException.
     * </p>
     */
    public void testGetChild_NullName() {
        try {
            defaultObject.getChild(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#getChild(String) for failure.
     * It tests the case that when name is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testGetChild_EmptyName() {
        try {
            defaultObject.getChild(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#removeChild(String) for failure.
     * It tests the case that when name is null and expects IllegalArgumentException.
     * </p>
     */
    public void testRemoveChild_NullName() {
        try {
            defaultObject.removeChild(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#removeChild(String) for failure.
     * It tests the case that when name is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testRemoveChild_EmptyName() {
        try {
            defaultObject.removeChild(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#getChildren(String) for failure.
     * It tests the case that when pattern is null and expects IllegalArgumentException.
     * </p>
     */
    public void testGetChildren_NullPattern() {
        try {
            defaultObject.getChildren(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests DefaultConfigurationObject#getChildren(String) for failure.
     * It tests the case that when pattern is invalid and expects IllegalArgumentException.
     * </p>
     */
    public void testGetChildren_InvalidPattern() {
        try {
            defaultObject.getChildren("[^DD##$$%%^^");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

}