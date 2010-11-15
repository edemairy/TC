/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom;
import java.util.Random;

/**
 * <p>
 * This class is a simple implementation of HashFunctionFamily interface.
 * Hash Codes are calculated based on the hash codes returned by
 * Object.hashCode() method.
 * See computeHash() documentation for hash computing algorithm description.
 * </p>
 * <p>
 * All the methods check their arguments for validity.
 * NullPointerException and IllegalArgumentException are thrown if parameters are invalid.
 * BloomFilterSerializeException is thrown if the object couldn't be serialized or deserialized.
 * </p>
 * <p>
 * This class is immutable as required by the interface, so it is thread-safe.
 * </p>
 *
 * @author real_vg, cucu
 * @version 1.0
 */
public class DefaultHashFunctionFamily implements HashFunctionFamily {

    /**
     * <p>
     * Represents the array of seeds used by computeHash() method to compute hash codes.
     * It is initialized in the constructor with a length equal to the number of hash functions
     * in the family, and containing "random" numbers generated always with the same seed
     * so that the instance can be reproduced later.
     * </p>
     */
    private final int[] functionSeeds;

    /**
     * <p>
     * Creates a hash function family having the specified number of hash functions.
     * The number of hash functions fully identify the hash function family
     * because even if the function seeds are randomly generated, the random seed is always the same.
     * </p>
     *
     * @param functionCount the number of hash functions in this family.
     * It must be a positive integer
     * @throws IllegalArgumentException if functionCount is not positive
     */
    public DefaultHashFunctionFamily(int functionCount) {
        // createSeeds throws IllegalArgumentException if functionCount is not positive
        functionSeeds = createSeeds(functionCount);
    }

    /**
     * <p>
     * Creates a hash function family restored from a string representation.
     * The string representation is just the number of functions.
     * </p>
     *
     * @param serialized a string representation of the hash function family (number of functions)
     * @throws NullPointerException if serialized is null
     * @throws BloomFilterSerializeException if serialized is not the string representation of a positive integer
     */
    public DefaultHashFunctionFamily(String serialized) {
        if (serialized == null) {
            throw new NullPointerException("String must be non null");
        }

        try {
            functionSeeds = createSeeds(Integer.parseInt(serialized));
        } catch (NumberFormatException e) {
            throw new BloomFilterSerializeException("Couldn't parse " + serialized + " as integer.", e);
        } catch (IllegalArgumentException iae) {
            throw new BloomFilterSerializeException("Serialized string must be a positive integer", iae);
        }
    }

    /**
     * <p>
     * Returns the number of functions in the family.
     * </p>
     *
     * @return the number of functions in the family
     */
    public int getFunctionCount() {
        return functionSeeds.length;
    }

    /**
     * <p>
     * Computes the hash code for the specified object, using the function given by functionIndex,
     * and using the range from 0 to maxHash inclusive.
     * </p>
     * <p>
     * The hash code is computed based on the hashCode() method of the specified object.
     * The formula used is:
     * <pre>
     * hashCode = abs(seed ^ obj.hashCode()) % (maxHash + 1).
     * </pre>
     * Where "seed" is an integer value different for every function in the family.
     * </p>
     *
     * @param functionIndex the index of function to use for computation,
     * between 0 and the number of functions in the family - 1.
     * @param maxHash the maximal value that can be returned, it must be a positive integer.
     * @param obj the object to compute its hash code
     * @return a hash code for the object, between 0 and maxHash inclusive
     * @throws NullPointerException if obj is null
     * @throws IllegalArgumentException if functionIndex is negative or not less than number of functions,
     * or if maxHash is not positive
     */
    public int computeHash(int functionIndex, int maxHash, Object obj) {
        if (obj == null) {
            throw new NullPointerException("obj can't be null");
        }
        if ((functionIndex < 0) || (functionIndex >= getFunctionCount())) {
            throw new IllegalArgumentException("functionIndex must be between 0 and functionCount-1");
        }
        if (maxHash <= 0) {
            throw new IllegalArgumentException("maxHash must be positive. maxHash=" + maxHash);
        }

        return Math.abs(functionSeeds[functionIndex] ^ obj.hashCode()) % (maxHash + 1);
    }

    /**
     * <p>
     * Returns the string representation of this hash function family.
     * It can be used to regenerate this object using the constructor that takes the "serialized" parameter.
     * </p>
     * <p>
     * This method is used by BloomFilterSerializer class while building a
     * string representation of the entire Bloom filter.
     * </p>
     *
     * @return the string representation of this hash function family
     */
    public String getSerialized() {
        return functionSeeds.length + "";
    }

    /**
     * <p>
     * Compares the specified object with this one.
     * Returns true if the specified object is also a DefaultHashFunctionFamily instance,
     * and they're identical, meaning that both objects return the same value for computeHash
     * when called with the same parameters.  If not, it returns false.
     * </p>
     * <p>
     * Overrides the Object.equals() method.
     * </p>
     *
     * @param obj Object to be compared for equality with this hash function family
     * @return true if the specified object is also a DefaultHashFunctionFamily instance and
     *  the two instances are identical
     */
    public boolean equals(Object obj) {
        if ((obj != null) && (obj instanceof DefaultHashFunctionFamily)) {
            return getFunctionCount() == ((DefaultHashFunctionFamily) obj).getFunctionCount();
        }
        return false;
    }

    /**
     * <p>
     * Returns the hash code for this DefaultHashFunctionFamily.
     *  </p>
     *
     * @return the hash code for this HashFunctionFamily
     */
    public int hashCode() {
        return getFunctionCount();
    }

    /**
     * <p>
     * Creates a seed array, consisting of functionCount elements.
     * The array returned is always the same when the parameter is the same.
     * </p>
     * @param functionCount the number of elements returned in the array, must be a positive integer.
     * @return an array of functionCount integers
     * @throws IllegalArgumentException if function count is not positive
     */
    private int[] createSeeds(int functionCount) {
        if (functionCount <= 0) {
            throw new IllegalArgumentException("functionCount must be positive");
        }

        // Use a fixed seed for the random number generator so that the instance could be reproduced.
        Random random = new Random(0);

        int seeds[] = new int [functionCount];

        for (int i = 0; i < functionCount; i++) {
            seeds[i] = random.nextInt();
        }
        return seeds;
    }

}
