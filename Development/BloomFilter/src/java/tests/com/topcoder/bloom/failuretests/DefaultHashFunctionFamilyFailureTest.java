/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom.failuretests;

import com.topcoder.bloom.BloomFilterSerializeException;
import com.topcoder.bloom.DefaultHashFunctionFamily;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Failure tests for DefaultHashFunctionFamily.
 *
 * @author mgmg
 * @version 1.0
 */
public class DefaultHashFunctionFamilyFailureTest extends TestCase {
    /**
     * The instance for test purpose.
     */
    private DefaultHashFunctionFamily instance = null;

    /**
     * Aggregate the tests in this class.
     *
     * @return
     */
    public static Test suite() {
        return new TestSuite(DefaultHashFunctionFamilyFailureTest.class);
    }

    /**
     * Create the test instance.
     */
    public void setUp() {
        this.instance = new DefaultHashFunctionFamily(10);
    }

    /**
     * Failure test for constructor1
     * IAE should be thrown because the parameter is not positive.
     */
    public void testConstructor1_InvalidInt() {
        try {
            new DefaultHashFunctionFamily(0);
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException iae) {
            //success.
        }
    }

    /**
     * Failure test for constructor2
     * NPE should be thrown because the parameter is null.
     */
    public void testConstructor2_NullParam() {
        try {
            new DefaultHashFunctionFamily(null);
            fail("NPE should be thrown.");
        } catch (NullPointerException npe) {
            //success.
        }
    }

    /**
     * Failure test for constructor2
     * BloomFilterSerializeException should be thrown because the parameter is not valid.
     */
    public void testConstructor2_InvalidString1() {
        try {
            new DefaultHashFunctionFamily("a");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException npe) {
            //success.
        }
    }

    /**
     * Failure test for constructor2
     * BloomFilterSerializeException should be thrown because the parameter is not valid.
     */
    public void testConstructor2_InvalidString2() {
        try {
            new DefaultHashFunctionFamily("0");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException npe) {
            //success.
        }
    }

    /**
     * Failure test for constructor2
     * BloomFilterSerializeException should be thrown because the parameter is not valid.
     */
    public void testConstructor2_InvalidString3() {
        try {
            new DefaultHashFunctionFamily("-5");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException npe) {
            //success.
        }
    }

    /**
     * Failure test for constructor2
     * BloomFilterSerializeException should be thrown because the parameter is not valid.
     */
    public void testConstructor2_InvalidString4() {
        try {
            new DefaultHashFunctionFamily("  ");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException npe) {
            //success.
        }
    }

    /**
     * Failure test for computeHash.
     * NPE should be thrown because the parameter is null.
     */
    public void testComputeHash_NullObj() {
        try {
            instance.computeHash(0, 5678, null);
            fail("NPE should be thrown.");
        } catch (NullPointerException npe) {
            //success.
        }
    }

    /**
     * Failure test for computeHash.
     * IAE should be thrown because the function index is not valid.
     */
    public void testComputeHash_InvalidFunctionIndex1() {
        try {
            instance.computeHash(-1, 5678, new Object());
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException iae) {
            //success.
        }
    }

    /**
     * Failure test for computeHash.
     * IAE should be thrown because the function index is not valid.
     */
    public void testComputeHash_InvalidFunctionIndex2() {
        try {
            instance.computeHash(10, 5678, new Object());
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException iae) {
            //success.
        }
    }

    /**
     * Failure test for computeHash.
     * IAE should be thrown because the maxHash is not valid.
     */
    public void testComputeHash_InvalidMaxHash() {
        try {
            instance.computeHash(2, 0, new Object());
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException iae) {
            //success.
        }
    }
}
