/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import junit.framework.TestCase;
/**
 * The demo of Configuration API Component.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class Demo extends TestCase {
    /**
     * The constant String name.
     */
    private static final String NAME = "name";
    /**
     * The DefaultConfigurationObject instance for test.
     */
    private ConfigurationObject defaultCo = null;
    /**
     * Set up the DefaultConfigurationObject testcase,
     * create the DefaultConfigurationObject instance for test.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        defaultCo = new DefaultConfigurationObject(NAME);
    }
    /**
     * The demo show how to create the ConfigurationObject.
     *
     */
    public void test_demo_createConfigurationObject() {
        //create a DefaultConfigurationObject
        ConfigurationObject instance = new DefaultConfigurationObject("the name");
        //create a SynchronizedConfigurationObject with the inner object
        ConfigurationObject synchronizedCo = new SynchronizedConfigurationObject(instance);
        //DefaultConfigurationObject is also can be used as TemplateConfigurationObject
        TemplateConfigurationObject templateCo = (TemplateConfigurationObject) instance;
    }
    /**
     * The demo show how to manipulate the properties of a ConfigurationObject.
     *
     * @throws Exception to JUnit
     */
    public void test_demo_manipulate_properties() throws Exception {
        //set the value, can be null, and the old value will be returned
        Object[] values = defaultCo.setPropertyValue("key", "value");
        //set a array of values with the key
        values = defaultCo.setPropertyValues("key", new Object[] {"value1", "value2"});

        //check whether a ConfigurationObject contains a key
        boolean contained = defaultCo.containsProperty("key");

        //get all the values with the key
        values = defaultCo.getPropertyValues("key");
        //get the first value of the key
        Object value = defaultCo.getPropertyValue("key");
        //get the count of values with the key
        int count = defaultCo.getPropertyValuesCount("key");

        //remove the values of the key
        defaultCo.removeProperty("key");
        defaultCo.clearChildren();
        //get all the keys of properties
        String [] keys = defaultCo.getAllPropertyKeys();
        //get the keys with the regex pattern
        keys = defaultCo.getPropertyKeys("[a\\*b]");
    }
    /**
     * The demo show how to manipulate the children of a ConfigurationObject.
     *
     * @throws Exception to JUnit
     */
    public void test_demo_manipulate_child() throws Exception {
        //add a child
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        defaultCo.addChild(child);

        //check contains child
        boolean contained = defaultCo.containsChild("child");
        //get the child bu name
        ConfigurationObject thechild = defaultCo.getChild("child");
        //remove the child by name
        thechild = defaultCo.removeChild("child");
        //clear the child
        defaultCo.clearChildren();

        //get all the children
        ConfigurationObject[] children = defaultCo.getAllChildren();
        //get all the children by a regex pattern
        children = defaultCo.getChildren("[abc]");
    }
    /**
     * The demo show how to manipulate the Descendants of a ConfigurationObject.
     *
     * @throws Exception to JUnit
     */
    public void test_demo_manipulate_Descendants() throws Exception {
        //get all the descendants
        ConfigurationObject[] descendants = defaultCo.getAllDescendants();
        //find the descendants by a path
        descendants = defaultCo.findDescendants("path");
        //delete the descendants by a path
        descendants = defaultCo.deleteDescendants("path");
        //get descendants with the regex pattern
        descendants = defaultCo.getDescendants("pattern");
    }
    /**
     * The demo show how to use as TemplateConfigurationObject.
     *
     * @throws Exception to JUnit
     */
    public void test_demo_useas_TemplateConfigurationObject() throws Exception {
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        TemplateConfigurationObject templateCo = (TemplateConfigurationObject) defaultCo;
        //set the property value with a path
        templateCo.setPropertyValue("a", "key", "value");

        //set the property values with a path
        templateCo.setPropertyValues("a/b", "key", new Object[]{"value"});

        //remove the property values with a path
        templateCo.removeProperty("a*\\/b", "key");

        //clear property with a path
        templateCo.clearProperties("path/*c");
        //add a child with a path
        templateCo.addChild("path", child);
        //remove child with a path and child name
        templateCo.removeChild("path", child.getName());
        //clear children with a path
        templateCo.clearChildren("b");

        //processDescendants with a path
        templateCo.processDescendants("path", new ProcessorMock());
    }
    /**
     * The demo show the clone of the ConfigurationObject.
     *
     */
    public void test_demo_clone() {
        //clone the ConfigurationObject
        ConfigurationObject clone = (ConfigurationObject) defaultCo.clone();
        ConfigurationObject synchronizedCo = new SynchronizedConfigurationObject(clone);
        SynchronizedConfigurationObject synchronizedClone = (SynchronizedConfigurationObject) synchronizedCo.clone();
    }
}
