/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom.stresstests;

import com.topcoder.bloom.DefaultHashFunctionFamily;

import junit.framework.TestCase;

import java.util.Random;


/**
 * <p>
 * <code>DefaultHashFunctionFamily</code> stress test cases. The ctor and the computeHash has been tested.
 * </p>
 * @author waits
 * @version 1.0
 */
public class DefaultHashFunctionFamilyStressTestes extends TestCase {
    /** The number of operations in the stress tests. */
    private static final int OPERATIONS = 1000;

    /** The size of hash functions in family for test. */
    private static final int FUNCTION_COUNT = 1000;

    /** The size of threads. */
    private static final int THREADS = 1000;

    /** The array of the <code>String</code> for test. */
    private static Object[] objects = null;

    /** The array of the seed for test. */
    private static int[] seeds = null;

    /** The array of the max hashes for test. */
    private static int[] maxhashes = null;

    /** To hold the result. */
    private int[] results = null;

    /** DefaultHashFunctionFamily instance for testing. */
    private DefaultHashFunctionFamily target = null;

    /**
     * test the ctor.
     */
    public void testCreate() {
        DefaultHashFunctionFamily[] instances = new DefaultHashFunctionFamily[OPERATIONS];
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < OPERATIONS; i++) {
            instances[i] = new DefaultHashFunctionFamily(FUNCTION_COUNT);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Stress Tests ----- Create DefaultHashFunctionFamily instance : " + OPERATIONS +
            " times, " + Integer.toString((int) (endTime - startTime)) + " ms");

        //simply verify
        for (int i = 0; i < OPERATIONS; i++) {
            assertNotNull("The instance created is still null.", instances[i]);
            assertEquals("The functionCount is incorrect.", FUNCTION_COUNT, instances[i].getFunctionCount());
        }
    }

    /**
     * test the computeHash().
     *
     * @throws Exception into Junit
     */
    public void testComputeHash() throws Exception {
        target = new DefaultHashFunctionFamily(FUNCTION_COUNT);

        //create seeds
        Random random = new Random(0);
        seeds = new int[FUNCTION_COUNT];

        // compute seeds for every function.
        for (int i = 0; i < FUNCTION_COUNT; i++) {
            seeds[i] = random.nextInt();
        }

        //create object[] and maxHash[]
        objects = new String[FUNCTION_COUNT];
        maxhashes = new int[FUNCTION_COUNT];

        for (int i = 0; i < FUNCTION_COUNT; i++) {
            objects[i] = new String("stress");
            maxhashes[i] = (i * FUNCTION_COUNT) + THREADS;
        }

        //create result;
        results = new int[FUNCTION_COUNT];

        long startTime = System.currentTimeMillis();
        ComputeHashThread[] testThreads = new ComputeHashThread[THREADS];

        for (int i = 0; i < THREADS; i++) {
            testThreads[i] = new ComputeHashThread();
        }

        for (int i = 0; i < THREADS; i++) {
            testThreads[i].run();
        }

        for (int i = 0; i < THREADS; i++) {
            testThreads[i].join();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Stress Tests ----- computeHash  : " + OPERATIONS + " times, " + THREADS + " threads" +
            Integer.toString((int) (endTime - startTime)) + " ms");

        for (int i = 0; i < FUNCTION_COUNT; i++) {
            assertEquals("The result is incorrect.", computeHashValue(i, maxhashes[i], objects[i]), results[i]);
        }
    }

    /**
     * <p>
     * This method computes the value of hash function with specified index and specified value range for the specified
     * object.
     * </p>
     *
     * @param functionIndex the index of function to compute value of.
     * @param maxHash the maximal value for function.
     * @param obj the object to compute it's hash function value.
     *
     * @return the value of hash function for the specified object.
     */
    private int computeHashValue(int functionIndex, int maxHash, Object obj) {
        return Math.abs(seeds[functionIndex] ^ obj.hashCode()) % (maxHash + 1);
    }

    /**
     * The thread used for computeHash.
     */
    class ComputeHashThread extends Thread {
        /**
         * The actual test.
         */
        public void run() {
            for (int i = 0; i < FUNCTION_COUNT; i++) {
                results[i] = target.computeHash(i, maxhashes[i], objects[i]);
            }
        }
    }
}
