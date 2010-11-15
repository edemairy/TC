/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

/**
 * <p>
 * Tests for DefaultHashFunctionFamily class.
 * </p>
 *
 * @author cucu
 * @version 1.0
 */
public class DefaultHashFunctionFamilyTests extends TestCase {

    /**
     * Object used for testing. It is instantiated in setUp
     */
    private HashFunctionFamily hff = null;

    /**
     * SetUp for each test: instantiate hff.
     */
    public void setUp() {
        hff = new DefaultHashFunctionFamily(4);
    }

    /**
     * Tests that DefaultHashFunctionFamilyTests is implementing HashFunctionFamily.
     */
    public void testImplements() {
        assertTrue("DefaultHashFunctionFamily must implement HashFunctionFamily", hff instanceof HashFunctionFamily);
    }

    /**
     * Tests the constructor taking an int with 0.
     * it must throw IllegalArgumentException
     */
    public void testConstructorIntZero() {
        try {
            new DefaultHashFunctionFamily(0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    /**
     * Tests the constructor taking an int with a negative value.
     * it must throw IllegalArgumentException
     */
    public void testConstructorIntNegative() {
        try {
            new DefaultHashFunctionFamily(-5);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    /**
     * Tests the constructor taking an int with a positive value, and the retrieving the function count,
     *  so that getFunctionCount is also tested.
     */
    public void testConstructorIntPositive() {
        hff = new DefaultHashFunctionFamily(4);
        assertEquals("bad function count value", 4, hff.getFunctionCount());
    }

    /**
     * Tests the constructor taking the serialized string with null.
     * it must throw NullPointerException
     */
    public void testConstructorStringNull() {
        try {
            new DefaultHashFunctionFamily(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // this is expected
        }
    }

    /**
     * Tests the constructor taking the serialized string with an string that doesn't represent an integer.
     * it must throw BloomFilterSerializeException
     */
    public void testConstructorStringInvalid() {
        try {
            new DefaultHashFunctionFamily("343x");
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // this is expected
        }
    }

    /**
     * Tests the constructor taking the serialized string with a string representing a negative integer.
     * It must throw BloomFilterSerializeException
     */
    public void testConstructorStringNegative() {
        try {
            new DefaultHashFunctionFamily("-5");
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // this is expected
        }
    }

    /**
     * Tests the constructor taking a String with a positive value, and the retrieving the function count,
     * so that getFunctionCount is also tested.
     */
    public void testConstructorStringPositive() {
        hff = new DefaultHashFunctionFamily("4");
        assertEquals("bad function count value", 4, hff.getFunctionCount());
    }

    /**
     * Test calling computeHash with a null object.
     * It must throw NullPointerException
     */
    public void testComputeHashNullObj() {
        try {
            hff.computeHash(1, 10, null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // this is expected
        }
    }

    /**
     * Test calling computeHash with a negative function index.
     * It must throw IllegalArgumentException
     */
    public void testComputeHashNegativeFunctionIndex() {
        try {
            hff.computeHash(-1, 10, "x");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    /**
     * Test calling computeHash with a function index equal to the number of functions.
     * It must throw IllegalArgumentException
     */
    public void testComputeHashBadFunctionIndex() {
        try {
            hff.computeHash(4, 10, "x");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    /**
     * Test calling computeHash with maxhash being 0.
     * It must throw IllegalArgumentException
     */
    public void testComputeHashZeroMaxHash() {
        try {
            hff.computeHash(4, 0, "x");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }


    /**
     * Tests that computing hash values for different function indexes gives
     * different values when the maxHash is big enough.
     * It also tests that the hash value is not greater than maxHash
     */
    public void testComputeHashDifferentFunctions() {
        Set s = new HashSet();

        for (int i = 0; i < 4; i++) {
            int hash = hff.computeHash(i, 10000, "hello");
            assertTrue("hash must be less or equal than maxHash", hash <= 10000);

            s.add(new Integer(hash));
        }
        assertEquals("computeHash expected to give different values", 4, s.size());
    }

    /**
     * Tests that computing hash values for different objects gives
     * different values when the maxHash is big enough.
     * It also tests that the hash value is not greater than maxHash
     */
    public void testComputeHashDifferentObjects() {
        Set s = new HashSet();

        for (int i = 0; i < 100; i++) {
            int hash = hff.computeHash(0, 100000, new Integer(i));
            assertTrue("hash must be less or equal than maxHash", hash <= 100000);

            s.add(new Integer(hash));
        }
        assertEquals("computeHash expected to give different values", 100, s.size());
    }


    /**
     * Tests that the getSerialized method returns the function count as a String.
     */
    public void testGetSerialized() {
        assertEquals("bad getSerialized value", "5", new DefaultHashFunctionFamily(5).getSerialized());
        assertEquals("bad getSerialized value", "9", new DefaultHashFunctionFamily(9).getSerialized());
        assertEquals("bad getSerialized value", "13", new DefaultHashFunctionFamily(13).getSerialized());
    }

    /**
     * Tests that the equals method is returning false with a null object.
     */
    public void testEqualsNull() {
        assertFalse("equals(null) must return false", hff.equals(null));
    }

    /**
     * Tests that the equals method is returning false with an object of a different class.
     */
    public void testEqualsDifferentClass() {
        assertFalse("equals with an object of another class must return false",
                hff.equals(new MockHashFunctionFamily(4)));
    }

    /**
     * Tests that the equals method is returning false with an object with different number of functions.
     */
    public void testEqualsDifferentFunctionCount() {
        assertFalse("equals with an instance with different number of functions must return false",
                hff.equals(new DefaultHashFunctionFamily(5)));
    }

    /**
     * Tests that the equals method is returning true with an object with the same number of functions.
     */
    public void testEqualsSameFunctionCount() {
        assertTrue("equals with an instance with equal number of functions must return true",
                hff.equals(new DefaultHashFunctionFamily(4)));
    }

    /**
     * Tests that the hashCode method returns the functionCount value.
     */
    public void testHashCode() {
        assertEquals("bad hashcode", 4, hff.hashCode());
        assertEquals("bad hashcode", 8, new DefaultHashFunctionFamily(8).hashCode());
        assertEquals("bad hashcode", 13, new DefaultHashFunctionFamily(13).hashCode());
    }
}
