/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom.accuracytests;

import junit.framework.TestCase;

import java.util.Random;

import com.topcoder.bloom.DefaultHashFunctionFamily;


/**
 * <p>This AccuracyTests of DefaultHashFunctionFamily.</p>
 *
 * @author telly12
 * @version 1.0
 */
public class DefaultHashFunctionFamilyAccuracyTests extends TestCase {
    /**
     * <p>
     * The instance of DefaultHashFunctionFamily used to test.
     * </p>
     */
    private DefaultHashFunctionFamily functionFamily = null;

    /**
     * <p>
     * The setUp of the unit AccuracyTests.
     * </p>
     *
     */
    protected void setUp() {
        functionFamily = new DefaultHashFunctionFamily(5);
    }

    /**
     * <p>Test construct Withserialized.</p>
     *
     */
    public void testconstruct_Withserialized() {
        functionFamily = new DefaultHashFunctionFamily("3");
        assertNotNull("can not construct the DefaultHashFunctionFamily.",
            functionFamily);
    }
    /**
     * <p>Test construct WithfunctionCount.</p>
     *
     */
    public void testconstruct_WithfunctionCount() {
        assertNotNull("can not construct the DefaultHashFunctionFamily.",
            functionFamily);
    }

    /**
     * <p>Test the method getFunctionCount.</p>
     *
     */
    public void testgetFunctionCount() {
        assertTrue("The length should be == 5.",
            5 == functionFamily.getFunctionCount());
    }

    /**
     * <p>Test the method getSerialized.</p>
     *
     */
    public void testgetSerialized() {
        assertTrue("The Serialized should be '5'.",
            "5".equals(functionFamily.getSerialized()));
    }

    /**
     * <p>Test the method equals.</p>
     *
     */
    public void testequals1() {
        assertTrue("The two should be equals.",
            functionFamily.equals(new DefaultHashFunctionFamily(5)));
    }

    /**
     * <p>Test the method equals.</p>
     *
     */
    public void testequals2() {
        assertFalse("The two should no be equals.",
            functionFamily.equals(new DefaultHashFunctionFamily(4)));
    }

    /**
     * <p>Test the method hashCode.</p>
     *
     */
    public void testhashCode() {
        assertTrue("The length should be == 5.", 5 == functionFamily.hashCode());
    }

    /**
     * <p>
     * Test the method computeHash.
     * </p>
     *
     */
    public void testcomputeHash() {
        int maxHash = 5;
        int index = 3;
        Object object = new Object();
        int [] functionSeeds = new int[5];

        Random random = new Random(0);

        for (int i = 0; i < 5; i++) {
            functionSeeds[i] = random.nextInt();
        }
        int expected = Math.abs((functionSeeds[index] ^ object.hashCode()) % (maxHash
                + 1));
        assertTrue("The value should be same expected.",
            expected == functionFamily.computeHash(index, maxHash, object));
    }
}
