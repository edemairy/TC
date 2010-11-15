package com.topcoder.bloom.stresstests;

import com.topcoder.bloom.BloomFilter;
/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
import com.topcoder.bloom.DefaultHashFunctionFamily;
import com.topcoder.bloom.HashFunctionFamily;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;


/**
 * <code>BloomFilter</code> stress test cases.
 * @author waits
 * @version 1.0
 */
public class BloomFilterStressTests extends TestCase {
    /** The number of operations in the stress tests. */
    private static final int OPERATIONS = 10000;

    /** reprents the capacity. */
    private static final int CAPACITY = 100;

    /** represents the errorRate to init target. */
    private static final float ERROR_RATE = 0.1f;

    /** BloomFilter instance for testing. */
    private BloomFilter target = null;

    /**
     * create BloomFilter instance.
     */
    protected void setUp() {
        target = new BloomFilter(CAPACITY, ERROR_RATE);
    }

    /**
     * test two ctor. With capacity, error_rate and serialized string.
     */
    public void testCtor() {
        BloomFilter[] filters = new BloomFilter[OPERATIONS];

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < OPERATIONS; i++) {
            filters[i] = new BloomFilter(CAPACITY, ERROR_RATE);
            new BloomFilter(filters[i].getSerialized());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Stress tests ------ " + " create BloomFilter instance in " + (OPERATIONS * 2) +
            " times in " + Integer.toString((int) (endTime - startTime)) + " ms");
    }

    /**
     * test ctor with too large bitsize.
     *
     */
    public void testCtor2() {
        try {
            new BloomFilter(Integer.MAX_VALUE, new DefaultHashFunctionFamily(10000));
            fail("The length of bitset is too large.");
        } catch(IllegalArgumentException e) {
            //good
        }
    }
    /**
     * test add and contains.
     */
    public void testAdd() {
        Object[] objects = new String[OPERATIONS];

        for (int i = 0; i < OPERATIONS; i++) {
            objects[i] = new String("stress" + i);
        }

        //test add.
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < OPERATIONS; i++) {
            target.add(objects[i]);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Stress tests ------ " + " add(Object) in " + OPERATIONS + " times in " +
            Integer.toString((int) (endTime - startTime)) + " ms");

        //verify contains.
        Collection collection = new ArrayList();

        for (int i = 0; i < OPERATIONS; i++) {
            assertTrue("not contained.", target.contains(objects[i]));
            collection.add(objects[i]);
        }

        assertTrue("The collection should be contained.", target.containsAll(collection));
    }

    /**
     * test unit().
     */
    public void testUnit() {
        String bitSetString1 = "30:11111";
        String bitSetString2 = "30:22222";

        HashFunctionFamily hashFunctions = new DefaultHashFunctionFamily(12);
        target = new BloomFilter(bitSetString2, hashFunctions);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < OPERATIONS; i++) {
            target.unite(new BloomFilter(bitSetString1, hashFunctions));
            assertEquals("The result is incorrect.", "30:33333", target.getSerializedBitSet());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Stress tests ------ " + " unit(BloomFilter) in " + OPERATIONS + " times in " +
            Integer.toString((int) (endTime - startTime)) + " ms");
    }

    /**
     * test intersect().
     */
    public void testIntersect() {
        String bitSetString1 = "30:11111";
        String bitSetString2 = "30:22222";

        HashFunctionFamily hashFunctions = new DefaultHashFunctionFamily(12);
        target = new BloomFilter(bitSetString2, hashFunctions);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < OPERATIONS; i++) {
            target.intersect(new BloomFilter(bitSetString1, hashFunctions));
            assertEquals("The result is incorrect.", "30:00000", target.getSerializedBitSet());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Stress tests ------ " + " Intersect(BloomFilter) in " + OPERATIONS + " times in " +
            Integer.toString((int) (endTime - startTime)) + " ms");
    }
}
