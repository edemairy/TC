/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

/**
 * <p>
 * Tests for BloomFilter class.
 * </p>
 *
 * @author cucu
 * @version 1.0
 */
public class BloomFilterTests extends TestCase {
    /**
     * Some integer values to be added in the filter.
     */
    private static final int INT_VALUES[]
        = new int[] {3, 26, 78, 145, 146, 207, 299, 315, 500, 549, 610, 790, 791, 792, 793, 810, 999};

    /**
     * BloomFilter used for testing. It is instantiated in SetUp.
     */
    private BloomFilter filter = null;


    /**
     * HashFunctionFamily used for testing. It is instantiated in SetUp.
     */
    private HashFunctionFamily hashFamily = null;


    /**
     * Set Up for each test: create a BloomFilter and a HashFunctionFamily.
     */
    public void setUp() {
        filter = new BloomFilter(1000, 0.01f);
        hashFamily = new DefaultHashFunctionFamily(4);
    }


    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate).
     * It uses a negative capacity.  IllegalArgumentException is expected to be thrown.
     */
    public void testConstructor1NegativeCapacity() {
        try {
            new BloomFilter(-10, 0.1f);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate).
     * It uses a capacity of 0.  IllegalArgumentException is expected to be thrown.
     */
    public void testConstructor1ZeroCapacity() {
        try {
            new BloomFilter(0, 0.1f);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }


    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate).
     * It uses a negative error rate.  IllegalArgumentException is expected to be thrown.
     */
    public void testConstructor1NegativeErrorRate() {
        try {
            new BloomFilter(10, -0.1f);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate).
     * It uses an error rate greater than 1.  IllegalArgumentException is expected to be thrown.
     */
    public void testConstructor1BadErrorRate1() {
        try {
            new BloomFilter(10, 1.1f);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate).
     * It uses an error rate of exactly 0.  IllegalArgumentException is expected to be thrown.
     */
    public void testConstructor1BadErrorRate2() {
        try {
            new BloomFilter(10, 0.0f);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate).
     * It uses an error rate of exactly 1.  IllegalArgumentException is expected to be thrown.
     */
    public void testConstructor1BadErrorRate3() {
        try {
            new BloomFilter(10, 1.0f);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate).
     * It test that the bit set length and function count are well calculated from the capacity and error rate.
     */
    public void testConstructor1VeryBigBitSet() {
        try {
            new BloomFilter(Integer.MAX_VALUE, 0.0001f);

            System.out.println("Even with the bigger size possible (2 GigaBits) there was memory enough "
                    + "for the filter in  testConstructor1VeryBig");
        } catch (IllegalArgumentException iae) {
            // this is the error that must be thrown if there is not enough memory to allocate the filter.
        } catch (OutOfMemoryError e) {
            fail("IllegalArgumentException was expected when there is not enough memory to allocate the filter.");
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate).
     * It builds the BloomFilter with the lowest capacity and a big error rate (but less than 1), so that
     * the bit set size calculated is 1, but being less than the minimum (length 2), 2 must be used.
     */
    public void testConstructor1VerySmallBitSet() {
        BloomFilter bf = new BloomFilter(1, 0.9999999f);
        assertEquals("Expected length 2", 2, bf.getBitSetLength());
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate).
     * It test that the bit set length and function count are well calculated from the capacity and error rate.
     */
    public void testConstructor1Accuracy1() {
        BloomFilter bf = new BloomFilter(100, 0.1f);

        assertEquals("Bad Bit Set size", 481, bf.getBitSetLength());
        assertEquals("Bad number of functions used", 3, bf.getHashFunctionFamily().getFunctionCount());
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate).
     * It test that the bit set length and function count are well calculated from the capacity and error rate.
     */
    public void testConstructor1Accuracy2() {
        BloomFilter bf = new BloomFilter(1000, 0.2f);

        assertEquals("Bad Bit Set size", 3374, bf.getBitSetLength());
        assertEquals("Bad number of functions used", 2, bf.getHashFunctionFamily().getFunctionCount());
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate).
     * It test that the bit set length and function count are well calculated from the capacity and error rate.
     */
    public void testConstructor1Accuracy3() {
        BloomFilter bf = new BloomFilter(20, 0.001f);

        assertEquals("Bad Bit Set size", 288, bf.getBitSetLength());
        assertEquals("Bad number of functions used", 10, bf.getHashFunctionFamily().getFunctionCount());
    }


    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate, Class hashFunctionsClass).
     * It uses a negative capacity.  IllegalArgumentException is expected to be thrown.
     */
    public void testConstructor2NegativeCapacity() {
        try {
            new BloomFilter(-10, 0.1f, DefaultHashFunctionFamily.class);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate, Class hashFunctionsClass).
     * It uses a capacity of zero.  IllegalArgumentException is expected to be thrown.
     */
    public void testConstructor2ZeroCapacity() {
        try {
            new BloomFilter(0, 0.1f, DefaultHashFunctionFamily.class);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate, Class hashFunctionsClass).
     * It uses a negative error rate.  IllegalArgumentException is expected to be thrown.
     */
    public void testConstructor2NegativeErrorRate() {
        try {
            new BloomFilter(10, -0.1f, DefaultHashFunctionFamily.class);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate, Class hashFunctionsClass).
     * It uses an error rate greater than 1.  IllegalArgumentException is expected to be thrown.
     */
    public void testConstructor2BadErrorRate1() {
        try {
            new BloomFilter(10, 1.1f, DefaultHashFunctionFamily.class);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate, Class hashFunctionsClass).
     * It uses an error rate of exactly 0.  IllegalArgumentException is expected to be thrown.
     */
    public void testConstructor2BadErrorRate2() {
        try {
            new BloomFilter(10, 0.0f, DefaultHashFunctionFamily.class);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate, Class hashFunctionsClass).
     * It uses an error rate of exactly 1.  IllegalArgumentException is expected to be thrown.
     */
    public void testConstructor2BadErrorRate3() {
        try {
            new BloomFilter(10, 1.0f, DefaultHashFunctionFamily.class);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate, Class hashFunctionsClass).
     * It uses null as the hash class.  NullPointerException is expected to be thrown.
     */
    public void testConstructor2NullClass() {
        try {
            new BloomFilter(10, 0.1f, null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate, Class hashFunctionsClass).
     * It uses a wrong class as the hash class.  IllegalArgumentException is expected to be thrown.
     */
    public void testConstructor2BadClass() {
        try {
            new BloomFilter(10, 0.1f, Integer.class);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate, Class hashFunctionsClass).
     * It test that the bit set length and function count are well calculated from the capacity and error rate.
     */
    public void testConstructor2VeryBigBitSet() {
        try {
            new BloomFilter(Integer.MAX_VALUE, 0.0001f, DefaultHashFunctionFamily.class);

            System.out.println("Even with the bigger size possible (2 GigaBits) there was memory enough "
                    + "for the filter.  ");
        } catch (IllegalArgumentException iae) {
            // this is the error that must be thrown if there is not enough memory to allocate the filter.
        } catch (OutOfMemoryError e) {
            fail("IllegalArgumentException was expected when there is not enough memory to allocate the filter.");
        }
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate, Class hashFunctionsClass).
     * It builds the BloomFilter with the lowest capacity and a big error rate (but less than 1), so that
     * the bit set size calculated is 1, but being less than the minimum (length 2), 2 must be used.
     */
    public void testConstructor2VerySmallBitSet() {
        BloomFilter bf = new BloomFilter(1, 0.9999999f, DefaultHashFunctionFamily.class);

        assertEquals("Expected length 2", 2, bf.getBitSetLength());
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate, Class hashFunctionsClass).
     * It tests that the bit set length and function count are well calculated from
     * the capacity and error rate, and that the hash class is correctly set.
     */
    public void testConstructor2Accuracy1() {
        BloomFilter bf = new BloomFilter(100, 0.1f, DefaultHashFunctionFamily.class);

        assertEquals("Bad Bit Set size", 481, bf.getBitSetLength());
        assertEquals("Bad number of functions used", 3, bf.getHashFunctionFamily().getFunctionCount());
        assertTrue("Bad hashFunctionFamily", bf.getHashFunctionFamily() instanceof DefaultHashFunctionFamily);
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate, Class hashFunctionsClass).
     * It tests that the bit set length and function count are well calculated from
     * the capacity and error rate, and that the hash class is correctly set.
     */
    public void testConstructor2Accuracy2() {
        BloomFilter bf = new BloomFilter(1000, 0.2f, MockHashFunctionFamily.class);

        assertEquals("Bad Bit Set size", 3374, bf.getBitSetLength());
        assertEquals("Bad number of functions used", 2, bf.getHashFunctionFamily().getFunctionCount());
        assertTrue("Bad hashFunctionFamily", bf.getHashFunctionFamily() instanceof MockHashFunctionFamily);
    }

    /**
     * Tests the constructor BloomFilter(int capacity, float errorRate, Class hashFunctionsClass).
     * It tests that the bit set length and function count are well calculated from
     * the capacity and error rate, and that the hash class is correctly set.
     */
    public void testConstructor2Accuracy3() {
        BloomFilter bf = new BloomFilter(20, 0.001f, DefaultHashFunctionFamily.class);

        assertEquals("Bad Bit Set size", 288, bf.getBitSetLength());
        assertEquals("Bad number of functions used", 10, bf.getHashFunctionFamily().getFunctionCount());
        assertTrue("Bad hashFunctionFamily", bf.getHashFunctionFamily() instanceof DefaultHashFunctionFamily);
    }

    /**
     * Tests the constructor BloomFilter(int bitSetSize, HashFunctionFamily hashFunctions).
     * It tests with bitSetSize = 0.  IllegalArgumentException must be thrown.
     */
    public void testConstructor3ZeroSize() {
        try {
            new BloomFilter(0, hashFamily);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int bitSetSize, HashFunctionFamily hashFunctions).
     * It tests with null hashFamily.  NullPointerException must be thrown.
     */
    public void testConstructor3NullHashFamily() {
        try {
            new BloomFilter(4, null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
    }


    /**
     * Tests the constructor BloomFilter(int bitSetSize, HashFunctionFamily hashFunctions).
     * It test that the bit set length and function count are well calculated from the capacity and error rate.
     */
    public void testConstructor3VeryBigBitSet() {
        try {
            new BloomFilter(Integer.MAX_VALUE, hashFamily);

            System.out.println("Even with the bigger size possible (2 GigaBits) there was memory enough "
                    + "for the filter.  ");
        } catch (IllegalArgumentException iae) {
            // this is the error that must be thrown if there is not enough memory to allocate the filter.
        } catch (OutOfMemoryError e) {
            fail("IllegalArgumentException was expected when there is not enough memory to allocate the filter.");
        }
    }

    /**
     * Tests the constructor BloomFilter(int bitSetSize, HashFunctionFamily hashFunctions).
     * It tests that when bitSetSize is 1, IllegalArgumentException is thrown.
     */
    public void testConstructor3BitSetLength1() {
        try {
            new BloomFilter(1, hashFamily);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(int bitSetSize, HashFunctionFamily hashFunctions).
     * It tests that the bitSet size and hashFamily are correctly set from the constructor.
     */
    public void testConstructor3Accuracy1() {
        BloomFilter bf = new BloomFilter(100, hashFamily);

        assertEquals("Bad Bit Set size", 100, bf.getBitSetLength());
        assertEquals("Bad hashFunctionFamily", hashFamily, bf.getHashFunctionFamily());
    }

    /**
     * Tests the constructor BloomFilter(int bitSetSize, HashFunctionFamily hashFunctions).
     * It tests that the bitSet size and hashFamily are correctly set from the constructor.
     */
    public void testConstructor3Accuracy2() {
        HashFunctionFamily mock = new MockHashFunctionFamily(5);
        BloomFilter bf = new BloomFilter(1000000, mock);

        assertEquals("Bad Bit Set size", 1000000, bf.getBitSetLength());
        assertEquals("Bad hashFunctionFamily", mock, bf.getHashFunctionFamily());
    }

    /**
     * Tests the constructor BloomFilter(String serialized, HashFunctionFamily hashFunctions).
     * It tests with null serialized.  NullPointerException must be thrown.
     */
    public void testConstructor4NullSerialized() {
        try {
            new BloomFilter(null, hashFamily);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized, HashFunctionFamily hashFunctions).
     * It tests with empty string as serialized.  BloomFilterSerializeException must be thrown.
     */
    public void testConstructor4EmptySerialized() {
        try {
            new BloomFilter("   ", hashFamily);
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized, HashFunctionFamily hashFunctions).
     * It tests with null hashFunctions. NullPointerException must be thrown.
     */
    public void testConstructor4NullHashFunctions() {
        try {
            new BloomFilter("24:0000", null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized, HashFunctionFamily hashFunctions).
     * It tests with an invalid serialized string (no ':' found).  BloomFilterSerializeException must be thrown.
     */
    public void testConstructor4InvalidSerialized1() {
        try {
            new BloomFilter("435345", hashFamily);
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized, HashFunctionFamily hashFunctions).
     * It tests with an invalid serialized string (size is not an integer).
     * BloomFilterSerializeException must be thrown.
     */
    public void testConstructor4InvalidSerialized2() {
        try {
            new BloomFilter("x30:00000", hashFamily);
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized, HashFunctionFamily hashFunctions).
     * It tests with an invalid serialized string (the base64 part has an invalid character).
     * BloomFilterSerializeException must be thrown.
     */
    public void testConstructor4InvalidSerialized3() {
        try {
            new BloomFilter("24:000?", hashFamily);
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }


    /**
     * Tests the constructor BloomFilter(String serialized, HashFunctionFamily hashFunctions).
     * It tests with an invalid serialized string (the size is bigger than the bits provided in base64).
     * BloomFilterSerializeException must be thrown.
     */
    public void testConstructor4InvalidSerialized4() {
        try {
            new BloomFilter("25:0000", hashFamily);
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized, HashFunctionFamily hashFunctions).
     * It tests that when the length is 1, BloomFilterSerializeException is thrown.
     */
    public void testConstructor4BitSetLength1() {
        try {
            new BloomFilter("1:1", hashFamily);
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized, HashFunctionFamily hashFunctions).
     * It tests that all the base 64 characters go through the validation, not throwing any exception.
     */
    public void testConstructor4AllBase64() {
        new BloomFilter("384:0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_:", hashFamily);
    }

    /**
     * Tests the constructor BloomFilter(String serialized, HashFunctionFamily hashFunctions).
     * It uses a serialized string representing all the bits in 0, so the filter is empty.
     */
    public void testConstructor4EmptyBitSet() {
        BloomFilter bf = new BloomFilter("40:0000000", hashFamily);
        assertEquals("hashFamily was not correctly set", hashFamily, bf.getHashFunctionFamily());
        assertTrue("the bit set is not well built from serialization", bf.isEmpty());
        assertEquals("bad size", 40, bf.getBitSetLength());
    }

    /**
     * Tests the constructor BloomFilter(String serialized, HashFunctionFamily hashFunctions).
     * It uses a serialized string representing all the bits in 1, so the filter has all the possible elements
     */
    public void testConstructor4FullBitSet() {
        BloomFilter bf = new BloomFilter("54::::::::::", hashFamily);
        assertEquals("hashFamily was not correctly set", hashFamily, bf.getHashFunctionFamily());
        assertEquals("the bit set is not well built from serialization", 1000, countPositives(bf, 1000));
        assertEquals("bad size", 54, bf.getBitSetLength());
    }

    /**
     * Tests the constructor BloomFilter(String serialized, HashFunctionFamily hashFunctions).
     * It uses a serialized string generated from a BloomFilter, and checks that they're equal.
     */
    public void testConstructor4FromSerialized() {
        filter.add("hello");
        filter.add("world");
        filter.add("bloom!");

        BloomFilter bf = new BloomFilter(filter.getSerializedBitSet(), filter.getHashFunctionFamily());
        assertEquals("filter created form serialized bit is different than original", filter, bf);
    }



    /**
     * Tests the constructor BloomFilter(String serialized).
     * It tests with null serialized.  NullPointerException must be thrown.
     */
    public void testConstructor5NullSerialized() {
        try {
            new BloomFilter(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized).
     * It tests with empty string as serialized.  BloomFilterSerializeException must be thrown.
     */
    public void testConstructor5EmptySerialized() {
        try {
            new BloomFilter("   ");
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized).
     * It tests with an invalid serialized string (just one '|' found instead of 2).
     * BloomFilterSerializeException must be thrown.
     */
    public void testConstructor5InvalidSerialized1() {
        try {
            new BloomFilter("com.topcoder.bloom.DefaultHashFunctionFamily10|54::::::::::");
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }
    /**
     * Tests the constructor BloomFilter(String serialized).
     * It tests with an invalid serialized string (three '|' found instead of 2).
     * BloomFilterSerializeException must be thrown.
     */
    public void testConstructor5InvalidSerialized2() {
        try {
            new BloomFilter("com.topcoder.bloom.DefaultHashFunctionFamily|10|54::::::::::|123");
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized).
     * It tests with an invalid serialized string (bad class name).
     * BloomFilterSerializeException must be thrown.
     */
    public void testConstructor5InvalidSerialized3() {
        try {
            new BloomFilter("ThisIsNotAClassname|10|54::::::::::");
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized).
     * It tests with an invalid serialized string (bad serialized for DefaultHashFunctionFamily).
     * BloomFilterSerializeException must be thrown.
     */
    public void testConstructor5InvalidSerialized4() {
        try {
            new BloomFilter("com.topcoder.bloom.DefaultHashFunctionFamily|10x|54::::::::::");
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized).
     * It tests with an invalid bit set serialized string (no ':' found).
     * BloomFilterSerializeException must be thrown.
     */
    public void testConstructor5InvalidSerialized5() {
        try {
            new BloomFilter("com.topcoder.bloom.DefaultHashFunctionFamily|10|435345");
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized).
     * It tests with an invalid bit set serialized string (size is not an integer).
     * BloomFilterSerializeException must be thrown.
     */
    public void testConstructor5InvalidSerialized6() {
        try {
            new BloomFilter("com.topcoder.bloom.DefaultHashFunctionFamily|10|4x:00000");
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized).
     * It tests with an invalid bit set serialized string (the base64 part has an invalid character).
     * BloomFilterSerializeException must be thrown.
     */
    public void testConstructor5InvalidSerialized7() {
        try {
            new BloomFilter("com.topcoder.bloom.DefaultHashFunctionFamily|10|24:000?");
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }


    /**
     * Tests the constructor BloomFilter(String serialized)
     * It tests with an invalid bit set serialized string (the size is bigger than the bits provided in base64).
     * BloomFilterSerializeException must be thrown.
     */
    public void testConstructor5InvalidSerialized8() {
        try {
            new BloomFilter("com.topcoder.bloom.DefaultHashFunctionFamily|10|25:0000");
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized).
     * It tests with a class that is not a HashFunctionFamily
     */
    public void testConstructor5InvalidSerialized9() {
        try {
            new BloomFilter("java.lang.String|8|40:0000000");
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized).
     * It tests that when the length is 1, BloomFilterSerializeException is thrown.
     */
    public void testConstructor5BitSetLength1() {
        try {
            new BloomFilter("com.topcoder.bloom.DefaultHashFunctionFamily|1|1:1");
            fail("BloomFilterSerializeException expected");
        } catch (BloomFilterSerializeException e) {
            // expected
        }
    }

    /**
     * Tests the constructor BloomFilter(String serialized)
     * It tests that all the base 64 characters go through the validation, not throwing any exception.
     */
    public void testConstructor5AllBase64() {
        new BloomFilter("com.topcoder.bloom.DefaultHashFunctionFamily|10|"
                + "384:0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_:");
    }


    /**
     * Tests the constructor BloomFilter(String serialized)
     * It tests that the HashFunctionFamily is of the right class and correctly constructed.
     */
    public void testConstructor5HashFunctionFamily1() {
        BloomFilter bf = new BloomFilter("com.topcoder.bloom.DefaultHashFunctionFamily|5|40:0000000");

        HashFunctionFamily family = bf.getHashFunctionFamily();
        assertTrue("Hash family is not of the correct class", family instanceof DefaultHashFunctionFamily);
        assertEquals("The parameter is not correctly passed", 5, family.getFunctionCount());

    }

    /**
     * Tests the constructor BloomFilter(String serialized)
     * It tests that the HashFunctionFamily is of the right class and correctly constructed.
     */
    public void testConstructor5HashFunctionFamily2() {
        BloomFilter bf = new BloomFilter("com.topcoder.bloom.MockHashFunctionFamily|8|40:0000000");

        HashFunctionFamily family = bf.getHashFunctionFamily();
        assertTrue("Hash family is not of the correct class", family instanceof MockHashFunctionFamily);
        assertEquals("The parameter is not correctly passed", 8, family.getFunctionCount());

    }

    /**
     * Tests the constructor BloomFilter(String serialized)
     * It uses a serialized string representing all the bits in 0, so the filter is empty.
     */
    public void testConstructor5EmptyBitSet() {
        BloomFilter bf = new BloomFilter("com.topcoder.bloom.DefaultHashFunctionFamily|5|40:0000000");

        assertTrue("the bit set is not well built from serialization", bf.isEmpty());
        assertEquals("bad size", 40, bf.getBitSetLength());
    }

    /**
     * Tests the constructor BloomFilter(String serialized).
     * It uses a serialized string representing all the bits in 1, so the filter has all the possible elements
     */
    public void testConstructor5FullBitSet() {
        BloomFilter bf = new BloomFilter("com.topcoder.bloom.DefaultHashFunctionFamily|5|54::::::::::");

        assertEquals("the bit set is not well built from serialization", 1000, countPositives(bf, 1000));
        assertEquals("bad size", 54, bf.getBitSetLength());
    }

    /**
     * Tests the constructor BloomFilter(String serialized).
     * It uses a serialized string generated from a BloomFilter, and checks that they're equal.
     */
    public void testConstructor5FromSerialized() {
        filter.add("hello");
        filter.add("world");
        filter.add("bloom!");

        BloomFilter bf = new BloomFilter(filter.getSerialized());
        assertEquals("filter created form serialized is different than original", filter, bf);
    }

    /**
     * Tests the add method.
     * It tests that it throws NullPointerException when called with null.
     */
    public void testAddNull() {
        try {
            filter.add(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected behavior
        }
    }

    /**
     * Tests the add method.
     * It adds some Strings to the filter and checks that they're present in the filter.
     */
    public void testAddAccuracy1() {
        filter.add("hello");
        assertTrue("add failed for 'hello'", filter.contains("hello"));

        filter.add("TopCoder");
        assertTrue("add failed for 'TopCoder'", filter.contains("TopCoder"));

        filter.add("TCSDEVELOPER");
        assertTrue("add failed for 'TCSDEVELOPER'", filter.contains("TCSDEVELOPER"));
    }

    /**
     * Tests the add method.
     * It adds many elements, verifying in each case that the element was correctly added, and that no false
     * positives are returned, due to the low error rate chosen.
     */
    public void testAddAccuracy2() {
        for (int i = 0; i < INT_VALUES.length; i++) {
            Integer value = new Integer(INT_VALUES[i]);
            filter.add(value);
            assertTrue("add failed for Integer " + value, filter.contains(value));

            assertEquals("Invalid number of positives received", i + 1, countPositives(filter, 1000));
        }
    }

    /**
     * Tests the addAll method.
     * It tests that it throws NullPointerException when called with null.
     */
    public void testAddAllNull() {
        try {
            filter.addAll(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected behavior
        }
    }

    /**
     * Tests the addAll method.
     * It tests that if an element of the collection is null, it throws IllegalArgumentException.
     */
    public void testAddAllNullElement() {
        List l = new ArrayList();
        try {
            l.add("a");
            l.add(null);
            l.add("b");
            filter.addAll(l);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }
    }

    /**
     * Tests the addAll method.
     * It adds some elements and check their presence.
     */
    public void testAddAllAccuracy1() {
        Set s = new HashSet();
        s.add("cat");
        s.add("dog");
        s.add("elephant");

        filter.addAll(s);
        assertTrue("element 'cat' not found", filter.contains("cat"));
        assertTrue("element 'dog' not found", filter.contains("dog"));
        assertTrue("element 'elephant' not found", filter.contains("elephant"));
    }

    /**
     * Tests the addAll method.
     * It adds many elements with addAll and checks that they have all been add, as well as that they are no
     * false positives, due to the low error rate of the filter.
     *
     */
    public void testAddAll() {
        // Put all the elements of INT_VALUES in the list l
        List l = new ArrayList();
        for (int i = 0; i < INT_VALUES.length; i++) {
            l.add(new Integer(INT_VALUES[i]));
        }

        filter.addAll(l);

        // Check that all the added elements are present
        for (int i = 0; i < INT_VALUES.length; i++) {
            assertTrue("element expected to be in filter: " + INT_VALUES[i],
                    filter.contains(new Integer(INT_VALUES[i])));
        }
        assertEquals("Invalid number of positives received", INT_VALUES.length, countPositives(filter, 1000));

    }

    /**
     * Tests the contains method.
     * It tests with null item. NullPointerException must be thrown.
     */
    public void testContainsNull() {
        try {
            filter.contains(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Tests the contains method.
     * It tests that when the filter is empty it always return false.
     */
    public void testContainsAccuracy1() {
        // the countPositives method executes contains for each Integer between 0 and the passed value, and
        // returns the number of positives. Because the filter is empty, it must return 0.
        assertEquals("contains must return always false for an empty filter", 0, countPositives(filter, 1000));
    }

    /**
     * Tests the contains method.
     * It tests adding some elements and checking that just those are present in the filter.
     */
    public void testContainsAccuracy2() {
        filter.add("x");
        assertTrue("'x' expected to be in filter", filter.contains("x"));
        assertEquals("Just 'x' expected to be in filter", 1, countPositives(filter, 1000));

        filter.add("y");
        assertTrue("'y' expected to be in filter", filter.contains("y"));
        assertEquals("Just 'x' and 'y' expected to be in filter", 2, countPositives(filter, 1000));
    }

    /**
     * Tests the contains method.
     * It tests adding elements to fill the max capacity of the filter, then checking that they're all present.
     */
    public void testContainsAccuracy3() {
        for (int i = 0; i < 1000; i++) {
            filter.add(new Integer(i));
        }

        assertEquals("Elements missing", 1000, countPositives(filter, 1000));
    }

    /**
     * Tests the containsAll method.
     * It tests that it throws NullPointerException when called with null.
     */
    public void testContainsAllNull() {
        try {
            filter.containsAll(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected behavior
        }
    }

    /**
     * Tests the containsAll method.
     * It tests that if an element of the collection is null, it throws IllegalArgumentException.
     */
    public void testContainsAllNullElement() {
        List l = new ArrayList();
        try {
            l.add("a");
            l.add(null);
            l.add("b");
            filter.containsAll(l);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }
    }

    /**
     * Tests the containsAll method.
     * It tests that if an element of the collection is null, it throws IllegalArgumentException.
     */
    public void testContainsAllAccuracyEmptyList() {
        assertTrue("containsAll must return true when the collection is empty", filter.containsAll(new ArrayList()));
    }

    /**
     * Tests the containsAll method.
     * It adds some elements to a list and filter and checks if containsAll returns the expected value.
     */
    public void testContainsAllAccuracy1() {
        List l = new ArrayList();
        filter.add("hello");

        l.add("hello");
        assertTrue("containsAll is not working fine", filter.containsAll(l));

        l.add("people");
        assertFalse("containsAll is not working fine", filter.containsAll(l));

        filter.add("people");
        assertTrue("containsAll is not working fine", filter.containsAll(l));

        filter.add("doesn't");
        filter.add("matter");
        filter.add("other elements");
        assertTrue("containsAll is not working fine", filter.containsAll(l));
    }


    /**
     * Tests the equals method.
     * It tests that equals returns false when the object is null.
     */
    public void testEqualsNull() {
        assertFalse("equals expected to return false when object is null", filter.equals(null));
    }

    /**
     * Tests the equals method.
     * It tests that equals returns false when the object is from another class.
     */
    public void testEqualsDifferentClass() {
        assertFalse("equals expected to return false when object is of another class", filter.equals(hashFamily));
    }

    /**
     * Tests the equals method.
     * It tests that equals return false if the bit size is different.
     */
    public void testEqualsAccuracyDifferentBitSize() {
        BloomFilter bf1 = new BloomFilter(100, new DefaultHashFunctionFamily(5));
        BloomFilter bf2 = new BloomFilter(101, new DefaultHashFunctionFamily(5));
        assertFalse("equals expected to return false when object have different bit size", bf1.equals(bf2));
    }

    /**
     * Tests the equals method.
     * It tests that equals return false if the hash function family is different
     */
    public void testEqualsAccuracyDifferentHashFunctionFamily() {
        BloomFilter bf1 = new BloomFilter(100, new DefaultHashFunctionFamily(5));
        BloomFilter bf2 = new BloomFilter(100, new MockHashFunctionFamily(5));
        assertFalse("equals expected to return false when object have different hash families", bf1.equals(bf2));
    }

    /**
     * Tests the equals method.
     * It tests that equals return false if the filters have different contents
     */
    public void testEqualsAccuracyDifferentContents() {
        BloomFilter bf1 = new BloomFilter(100, new DefaultHashFunctionFamily(5));
        BloomFilter bf2 = new BloomFilter(100, new DefaultHashFunctionFamily(5));
        bf1.add("word1");
        bf2.add("word2");
        assertFalse("equals expected to return false when object have different contents", bf1.equals(bf2));
    }

    /**
     * Tests the equals method.
     * It tests that equals return true if the filters are equal.
     */
    public void testEqualsAccuracyEqual() {
        BloomFilter bf1 = new BloomFilter(100, new DefaultHashFunctionFamily(5));
        BloomFilter bf2 = new BloomFilter(100, new DefaultHashFunctionFamily(5));
        bf1.add("word1");
        bf2.add("word1");
        assertTrue("equals expected to return false when objects are equal", bf1.equals(bf2));

        bf1.add("word2");
        bf2.add("word2");
        assertTrue("equals expected to return false when objects are equal", bf1.equals(bf2));

        bf1.add(new Integer(26));
        bf2.add(new Integer(26));
        assertTrue("equals expected to return false when objects are equal", bf1.equals(bf2));
    }

    /**
     * Tests the hashCode method.
     * It tests that the hashCode method returns a value that depends on the contents of the filter, with
     * a reasonable distribution.
     */
    public void testHashCodeAccuracyBitSet() {
        Set values = new HashSet();

        // Add 100 elements to the filter and compute the hashCode each time
        for (int i = 0; i < 100; i++) {
            values.add(new Integer(filter.hashCode()));
            filter.add(new Integer(i));
        }

        // if the distribution is good, the 100 hashCode's generated should be different
        assertEquals("hashCode doesn't seem to have a good distribution", 100, values.size());
    }

    /**
     * Tests the hashCode method.
     * It tests that the hashCode method returns a value that depends on the hash function family,
     * with a reasonable distribution.
     */
    public void testHashCodeAccuracyHashFunction() {
        Set values = new HashSet();

        // Create 100 filters with different hash function families and compute the hashCode each time
        for (int i = 0; i < 100; i++) {
            BloomFilter bf = new BloomFilter(100, new DefaultHashFunctionFamily(i + 1));
            values.add(new Integer(bf.hashCode()));
        }

        // if the distribution is good, the 100 hashCode's generated should be different
        assertEquals("hashCode doesn't seem to have a good distribution", 100, values.size());
    }


    /**
     * Tests the isEmpty method.
     * It checks that it returns true when the filter is just created, false when an element is created,
     * and true if the filter is cleared.
     */
    public void testIsEmpty() {
        assertTrue("expected to be empty", filter.isEmpty());

        filter.add("x");
        assertFalse("not expected to be empty", filter.isEmpty());

        filter.clear();
        assertTrue("expected to be empty", filter.isEmpty());
    }

    /**
     * Tests the unite method.
     * It tests calling it with null. It must throw NullPointerException.
     */
    public void testUniteNull() {
        try {
            filter.unite(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected behavior
        }
    }

    /**
     * Tests the unite method.
     * It tests calling it with a non compatible filter (different bit set size).
     * It must throw IncompatibleBloomFiltersException.
     */
    public void testUniteDifferentBitSetSize() {
        try {
            BloomFilter f1 = new BloomFilter(100, new DefaultHashFunctionFamily(5));
            BloomFilter f2 = new BloomFilter(102, new DefaultHashFunctionFamily(5));
            f1.unite(f2);
            fail("IncompatibleBloomFiltersException expected");
        } catch (IncompatibleBloomFiltersException e) {
            // expected behavior
        }
    }

    /**
     * Tests the unite method.
     * Test calling unite with a non compatible filter (different hash function family).
     * It must throw IncompatibleBloomFiltersException.
     */
    public void testUniteDifferentHashFunctionFamily() {
        try {
            BloomFilter f1 = new BloomFilter(100, new DefaultHashFunctionFamily(5));
            BloomFilter f2 = new BloomFilter(100, new DefaultHashFunctionFamily(6));
            f1.unite(f2);
            fail("IncompatibleBloomFiltersException expected");
        } catch (IncompatibleBloomFiltersException e) {
            // expected behavior
        }
    }

    /**
     * Test joining 2 filters with unite method.
     */
    public void testUniteAccuracy1() {
        BloomFilter filter2 = new BloomFilter(1000, 0.01f);
        filter.add("hello");
        filter.add("world");

        filter2.add("hello");
        filter2.add("people");
        filter2.add("TopCoder");

        BloomFilter result = filter.unite(filter2);

        assertSame("unite must return the object itself", result, filter);

        assertTrue("word 'hello' expected to be in filter", filter.contains("hello"));
        assertTrue("word 'world' expected to be in filter", filter.contains("world"));
        assertTrue("word 'people' expected to be in filter", filter.contains("people"));
        assertTrue("word 'TopCoder' expected to be in filter", filter.contains("TopCoder"));
        assertFalse("false positive!", filter.contains("bananas"));
        assertFalse("false positive!", filter.contains("chocolate"));
    }

    /**
     * Tests the intersect method.
     * It tests calling it with null. It must throw NullPointerException.
     */
    public void testIntersectNull() {
        try {
            filter.intersect(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected behavior
        }
    }

    /**
     * Tests the intersect method.
     * It tests calling it with a non compatible filter (different bit set size).
     * It must throw IncompatibleBloomFiltersException.
     */
    public void testIntersectDifferentBitSetSize() {
        try {
            BloomFilter f1 = new BloomFilter(100, new DefaultHashFunctionFamily(5));
            BloomFilter f2 = new BloomFilter(102, new DefaultHashFunctionFamily(5));
            f1.intersect(f2);
            fail("IncompatibleBloomFiltersException expected");
        } catch (IncompatibleBloomFiltersException e) {
            // expected behavior
        }
    }

    /**
     * Tests the intersect method.
     * Test calling it with a non compatible filter (different hash function family).
     * It must throw IncompatibleBloomFiltersException.
     */
    public void testIntersectDifferentHashFunctionFamily() {
        try {
            BloomFilter f1 = new BloomFilter(100, new DefaultHashFunctionFamily(5));
            BloomFilter f2 = new BloomFilter(100, new DefaultHashFunctionFamily(6));
            f1.intersect(f2);
            fail("IncompatibleBloomFiltersException expected");
        } catch (IncompatibleBloomFiltersException e) {
            // expected behavior
        }
    }

    /**
     * Test intersecting 2 filters with intersect method.
     */
    public void testIntersectAccuracy1() {
        BloomFilter filter2 = new BloomFilter(1000, 0.01f);
        filter.add("hello");
        filter.add("world");

        filter2.add("hello");
        filter2.add("people");
        filter2.add("TopCoder");


        BloomFilter result = filter.intersect(filter2);

        assertSame("unite must return the object itself", result, filter);

        assertTrue("word 'hello' expected to be in filter", filter.contains("hello"));
        assertFalse("word 'world' not expected to be in filter", filter.contains("world"));
        assertFalse("word 'people' not expected to be in filter", filter.contains("people"));
        assertFalse("word 'TopCoder' not expected to be in filter", filter.contains("TopCoder"));
    }

    /**
     * Tests the clear method.
     * It puts an element on the filter and then checks that clear is erasing it.
     */
    public void testClearAccuracy1() {
        Integer value = new Integer(30);
        filter.add(value);
        assertTrue("add/contains are not working fine", filter.contains(value));

        filter.clear();
        assertFalse("Clear is not working fine", filter.contains(value));
    }

    /**
     * Tests the getSerializedBitSet method.
     * It creates some BloomFilters with different lengths and checks that the serialized
     * bit set returned is fine.
     */
    public void testGetSerializedBitSetAccuracy1() {
        BloomFilter bf = new BloomFilter(6, new DefaultHashFunctionFamily(4));
        assertEquals("Bad serialization string returned", "6:0", bf.getSerializedBitSet());

        bf = new BloomFilter(7, new DefaultHashFunctionFamily(4));
        assertEquals("Bad serialization string returned", "7:00", bf.getSerializedBitSet());

        bf = new BloomFilter(60, new DefaultHashFunctionFamily(4));
        assertEquals("Bad serialization string returned", "60:0000000000", bf.getSerializedBitSet());

    }

    /**
     * Tests the getSerializedBitSet method.
     * It uses the MockHashFunctionFamily to easily know which bits will be set.
     */
    public void testGetSerializedBitSetAccuracy2() {
        // each time computeHash is called on MockHashFunctionFamily, it returns a value that is incremented
        // (starting from 0), so each call to add makes 4 more bits in the bit set to be 1.
        BloomFilter bf = new BloomFilter(30, new MockHashFunctionFamily(4));

        bf.add("x");
        assertEquals("Bad serialization string returned", "30:F0000", bf.getSerializedBitSet());

        bf.add("y");
        assertEquals("Bad serialization string returned", "30::3000", bf.getSerializedBitSet());

        bf.add("z");
        assertEquals("Bad serialization string returned", "30:::000", bf.getSerializedBitSet());
    }

    /**
     * Tests the getSerialized method.
     * It creates some BloomFilters with different lengths and checks that the serialized
     * string returned is fine, and also that the class name and parameter are ok.
     */
    public void testGetSerializedAccuracy1() {
        BloomFilter bf = new BloomFilter(6, new DefaultHashFunctionFamily(5));
        assertEquals("Bad serialization string returned",
                "com.topcoder.bloom.DefaultHashFunctionFamily|5|6:0", bf.getSerialized());

        bf = new BloomFilter(7, new DefaultHashFunctionFamily(6));
        assertEquals("Bad serialization string returned",
                "com.topcoder.bloom.DefaultHashFunctionFamily|6|7:00", bf.getSerialized());

        bf = new BloomFilter(60, new DefaultHashFunctionFamily(7));
        assertEquals("Bad serialization string returned",
                "com.topcoder.bloom.DefaultHashFunctionFamily|7|60:0000000000", bf.getSerialized());

    }

    /**
     * Tests the getSerialized method.
     * It uses the MockHashFunctionFamily to easily know which bits will be set.
     */
    public void testGetSerializedAccuracy2() {
        // each time computeHash is called on MockHashFunctionFamily, it returns a value that is incremented
        // (starting from 0), so each call to add makes 4 more bits in the bit set to be 1.
        BloomFilter bf = new BloomFilter(30, new MockHashFunctionFamily(4));

        bf.add("x");
        assertEquals("Bad serialization string returned",
                "com.topcoder.bloom.MockHashFunctionFamily|4|30:F0000", bf.getSerialized());

        bf.add("y");
        assertEquals("Bad serialization string returned",
                "com.topcoder.bloom.MockHashFunctionFamily|4|30::3000", bf.getSerialized());

        bf.add("z");
        assertEquals("Bad serialization string returned",
                "com.topcoder.bloom.MockHashFunctionFamily|4|30:::000", bf.getSerialized());
    }


    /**
     * Tests the getHashFunctionFamily.
     * It tests creating a filter with a DefaultHashFunctionFamily and retrieving it with getHashFunctionFamily.
     */
    public void testGetHashFunctionFamilyAccuracy1() {
        filter = new BloomFilter(100, new DefaultHashFunctionFamily(7));

        HashFunctionFamily family = filter.getHashFunctionFamily();
        assertEquals("bad object returned", DefaultHashFunctionFamily.class.getName(), family.getClass().getName());
        assertEquals("bad object returned", 7, family.getFunctionCount());
    }

    /**
     * Tests the getHashFunctionFamily.
     * It tests creating a filter with a MockHashFunctionFamily and retrieving it with getHashFunctionFamily.
     */
    public void testGetHashFunctionFamilyAccuracy2() {
        filter = new BloomFilter(100, new MockHashFunctionFamily(7));

        HashFunctionFamily family = filter.getHashFunctionFamily();
        assertEquals("bad object returned", MockHashFunctionFamily.class.getName(), family.getClass().getName());
        assertEquals("bad object returned", 7, family.getFunctionCount());
    }

    /**
     * Tests the clone method.
     * It tests cloning a filter and then relying on equals to compare it with the original
     */
    public void testCloneAccuracy1() {
        filter.add("tom");
        filter.add("jerry");

        BloomFilter bf = (BloomFilter) filter.clone();
        assertEquals("clone failed", filter, bf);
    }

    /**
     * Tests the clone method.
     * It tests cloning a filter and then manually checking that they're equal.
     */
    public void testCloneAccuracy2() {
        filter = new BloomFilter(100, new DefaultHashFunctionFamily(4));
        filter.add("orange");
        filter.add("apple");

        BloomFilter bf = (BloomFilter) filter.clone();
        HashFunctionFamily family = bf.getHashFunctionFamily();

        assertEquals("error in the hash function",
                DefaultHashFunctionFamily.class.getName(), family.getClass().getName());
        assertEquals("error in the hash function",
                4, family.getFunctionCount());

        assertEquals("error in bit set length ", 100, bf.getBitSetLength());
        assertTrue("error in filter contents", bf.contains("orange"));
        assertTrue("error in filter contents", bf.contains("apple"));
    }
    /**
     * Tests the clone method.
     * It tests that the cloning is deep.  For that purpose, it clones a filter and then changes the original
     * filter; the cloned filter must remain equal.
     */
    public void testCloneAccuracy3() {
        filter = new BloomFilter(100, new DefaultHashFunctionFamily(4));
        filter.add("hello");

        BloomFilter bf = (BloomFilter) filter.clone();
        filter.add("world");

        assertFalse("clone is making a shallow copy", bf.contains("world"));

        filter.clear();
        assertTrue("clone is making a shallow copy", bf.contains("hello"));
    }


    /**
     * Tests the getBitSetLength method.
     * It tests that the size is correctly retrieved using the constructor that takes the bit set size.
     */
    public void testGetBitSetLengthAccuracy1() {
        filter = new BloomFilter(50, new MockHashFunctionFamily(4));
        assertEquals("wrong bitSetLength", 50, filter.getBitSetLength());

        filter = new BloomFilter(100000, new DefaultHashFunctionFamily(6));
        assertEquals("wrong bitSetLength", 100000, filter.getBitSetLength());
    }

    /**
     * Tests the getBitSetLength method.
     * It tests that the size is correctly retrieved using the constructor that takes a serialized string.
     */
    public void testGetBitSetLengthAccuracy2() {
        filter = new BloomFilter("com.topcoder.bloom.MockHashFunctionFamily|4|30:F0000");
        assertEquals("wrong bitSetLength", 30, filter.getBitSetLength());

        filter = new BloomFilter("com.topcoder.bloom.MockHashFunctionFamily|4|59:0123456789");
        assertEquals("wrong bitSetLength", 59, filter.getBitSetLength());
    }

    /**
     * Helper method for tests.
     * It counts how many integers between 0 and max are contained in the filter.
     *
     * @param bf the filter to check if the elements are present.
     * @param max the maximum value (excluded) to check in the filter
     * @return the number of integers between 0 and max that are contained in the filter.
     */
    private int countPositives(BloomFilter bf, int max) {
        int positives = 0;
        for (int j = 0; j < max; j++) {
            if (bf.contains(new Integer(j))) {
                positives++;
            }
        }
        return positives;
    }
}
