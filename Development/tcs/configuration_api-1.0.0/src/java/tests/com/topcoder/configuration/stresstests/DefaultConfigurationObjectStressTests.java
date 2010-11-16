/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.stresstests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import junit.framework.TestCase;

/**
 * <p>
 * Stress tests of {@link DefaultConfigurationObject} class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DefaultConfigurationObjectStressTests extends TestCase {
    /**
     * This instance is used in the test.
     */
    private DefaultConfigurationObject root = new DefaultConfigurationObject("root");

    /**
     * Set up the fixture.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {

    }

    /**
     * Run getAllDescendants 1000 times.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetAllDescendantsCase1() throws Exception {
        DefaultConfigurationObject previous = root;
        for (int i = 0; i < 1000; i++) {
            DefaultConfigurationObject child = new DefaultConfigurationObject("child" + i);
            previous.addChild(child);
            previous = child;
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ConfigurationObject[] configurationObjects = root.getAllDescendants();
            assertEquals("result is incorrect, root should have 1000 descendants.", 1000,
                configurationObjects.length);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run getAllDescendants 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run getAllDescendants 1000 times.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetAllDescendantsCase2() throws Exception {
        for (int i = 0; i < 1000; i++) {
            DefaultConfigurationObject child = new DefaultConfigurationObject("child" + i);
            root.addChild(child);
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ConfigurationObject[] configurationObjects = root.getAllDescendants();
            assertEquals("result is incorrect, root should have 1000 descendants.", 1000,
                configurationObjects.length);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run getAllDescendants 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run findDescendants 1000 times.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testFindDescendantsCase1() throws Exception {
        DefaultConfigurationObject previous = root;
        for (int i = 0; i < 1000; i++) {
            DefaultConfigurationObject child = new DefaultConfigurationObject("child" + i);
            previous.addChild(child);
            previous = child;
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ConfigurationObject[] configurationObjects = root.findDescendants("child0/child1/child2");
            assertEquals("result is incorrect, 1 descendants should be found.", 1, configurationObjects.length);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run findDescendants 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run findDescendants 1000 times.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testFindDescendantsCase2() throws Exception {
        for (int i = 0; i < 1000; i++) {
            DefaultConfigurationObject child = new DefaultConfigurationObject("child" + i);
            root.addChild(child);
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ConfigurationObject[] configurationObjects = root.findDescendants("child*");
            assertEquals("result is incorrect, 1000 descendants should be found.", 1000,
                configurationObjects.length);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run findDescendants 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run getDescendants 1000 times.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDescendantsCase1() throws Exception {
        DefaultConfigurationObject previous = root;
        for (int i = 0; i < 1000; i++) {
            DefaultConfigurationObject child = new DefaultConfigurationObject("child" + i);
            previous.addChild(child);
            previous = child;
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ConfigurationObject[] configurationObjects = root.getDescendants("child.*");
            assertEquals("result is incorrect, 1000 descendants should be found.", 1000,
                configurationObjects.length);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run getDescendants 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run getDescendants 1000 times.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDescendantsCase2() throws Exception {
        for (int i = 0; i < 1000; i++) {
            DefaultConfigurationObject child = new DefaultConfigurationObject("child" + i);
            root.addChild(child);
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ConfigurationObject[] configurationObjects = root.getDescendants("child.*");
            assertEquals("result is incorrect, 1000 descendants should be found.", 1000,
                configurationObjects.length);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run getDescendants 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run deleteDescendants 1000 times.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDeleteDescendantsCase() throws Exception {
        for (int i = 0; i < 1000; i++) {
            DefaultConfigurationObject child = new DefaultConfigurationObject("child" + i);
            root.addChild(child);
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ConfigurationObject[] configurationObjects = root.deleteDescendants("child" + i);
            assertEquals("result is incorrect, 1 descendants should be deleted.", 1, configurationObjects.length);
        }
        assertEquals("result is incorrect, there should no descendants exist.", 0, root.getAllDescendants().length);
        long endTime = System.currentTimeMillis();
        System.out.println("Run deleteDescendants 1000 times costs " + (endTime - startTime) + "ms");
    }
}
