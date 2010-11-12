/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import junit.framework.TestCase;


/**
 * Test cases about the class <code>EmailOptions</code>.
 *
 * @author waits
 * @version 1.0
 *
 * @since 1.2
 */
public class EmailOptionsTest extends TestCase {
    /** EmailOptions instance to test against. */
    private EmailOptions instance = null;

    /**
     * Setup the test environment.
     *
     * @throws Exception into JUnit
     */
    protected void setUp() throws Exception {
        instance = new EmailOptions();
    }

    /**
     * The default constructor Test case.
     */
    public void testConstructor() {
        assertNotNull("Fail to instantiate EmailOptions.", instance);
    }

    /**
     * <p>
     * Tests FromAddress getter.
     * </p>
     */
    public void testGetFromAddress() {
        //default value
        assertNull("Failed to get value.", instance.getFromAddress());

        //set value
        instance.setFromAddress("value");

        //verify again
        assertEquals("Failed to get value.", "value", instance.getFromAddress());
    }

    /**
     * <p>
     * Tests FromAddress setter.
     * </p>
     */
    public void testSetFromAddress() {
        //set value
        instance.setFromAddress("value");

        //verify
        assertEquals("Failed to get value.", "value", instance.getFromAddress());

        //with null value
        instance.setFromAddress(null);
        assertNull("Failed to get value.", instance.getFromAddress());
    }

    /**
     * <p>
     * Tests Subject getter.
     * </p>
     */
    public void testGetSubject() {
        //default value
        assertNull("Failed to get value.", instance.getSubject());

        //set value
        instance.setSubject("value");

        //verify again
        assertEquals("Failed to get value.", "value", instance.getSubject());
    }

    /**
     * <p>
     * Tests Subject setter.
     * </p>
     */
    public void testSetSubject() {
        //set value
        instance.setSubject("value");

        //verify
        assertEquals("Failed to get value.", "value", instance.getSubject());

        //with null value
        instance.setSubject(null);
        assertNull("Failed to get value.", instance.getSubject());
    }

    /**
     * <p>
     * Tests TemplateName getter.
     * </p>
     */
    public void testGetTemplateName() {
        //default value
        assertNull("Failed to get value.", instance.getTemplateName());

        //set value
        instance.setTemplateName("value");

        //verify again
        assertEquals("Failed to get value.", "value", instance.getTemplateName());
    }

    /**
     * <p>
     * Tests TemplateName setter.
     * </p>
     */
    public void testSetTemplateName() {
        //set value
        instance.setTemplateName("value");

        //verify
        assertEquals("Failed to get value.", "value", instance.getTemplateName());

        //with null value
        instance.setTemplateName(null);
        assertNull("Failed to get value.", instance.getTemplateName());
    }

    /**
     * <p>
     * Tests TemplateSource getter.
     * </p>
     */
    public void testGetTemplateSource() {
        //default value
        assertNull("Failed to get value.", instance.getTemplateSource());

        //set value
        instance.setTemplateSource("value");

        //verify again
        assertEquals("Failed to get value.", "value", instance.getTemplateSource());
    }

    /**
     * <p>
     * Tests TemplateSource setter.
     * </p>
     */
    public void testSetTemplateSource() {
        //set value
        instance.setTemplateSource("value");

        //verify
        assertEquals("Failed to get value.", "value", instance.getTemplateSource());

        //with null value
        instance.setTemplateSource(null);
        assertNull("Failed to get value.", instance.getTemplateSource());
    }
    /**
     * <p>
     * Tests Priority getter.
     * </p>
     */
    public void testGetPriority() {
        //default value
        assertEquals("Failed to get value.", 0, instance.getPriority());

        //set value
        instance.setPriority(2);

        //verify again
        assertEquals("Failed to get value.", 2, instance.getPriority());
    }

    /**
     * <p>
     * Tests Priority setter.
     * </p>
     */
    public void testSetPriority() {
        //set value
        instance.setPriority(0);

        //verify
        assertEquals("Failed to get value.", 0, instance.getPriority());
    }
    /**
     * <p>
     * Tests Send getter.
     * </p>
     */
    public void testGetSend() {
        //default value
        assertNull("Failed to get value.", instance.isSend());

        //set value
        instance.setSend(false);

        //verify again
        assertFalse("Failed to get value.", instance.isSend());
    }

    /**
     * <p>
     * Tests Send setter.
     * </p>
     */
    public void testSetSend() {
        //set value
        instance.setSend(Boolean.TRUE);

        //verify
        assertEquals("Failed to get value.", Boolean.TRUE, instance.isSend());

        //with null value
        instance.setSend(null);
        assertNull("Failed to get value.", instance.isSend());
    }
}
