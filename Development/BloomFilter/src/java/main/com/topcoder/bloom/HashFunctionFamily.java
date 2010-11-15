/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom;

/**
 * <p>
 * This interface represents a family of hash functions used by BloomFilter.
 * The implementations of this interface should be able to provide as many
 * different hash functions as the user requires.
 * The hashing algorithm may be the same but with different parameters.
 * Hash functions should return hash for a given Object, in the range
 * from 0 to specified maximal value.
 * </p>
 *
 * <p>
 * Implementations of interface are required to have the constructors:
 * <ul>
 * <li>HashFunctionFamily(int functionCount)
 * <li>HashFunctionFamily(String serialized)
 * </ul>
 * Both constructors are required to create identical instances for the same parameters.
 * The first constructor should create a hash function family consisting of
 * functionCount functions.
 * The second constructor should restore the hash function family from serialized String, which
 * can be obtained by getSerialized() method.
 * Two instances are identical if computeHash() method returns the same value when called with
 * the same parameters.
 * </p>
 * <p>
 * Implementation should also implement equals()
 * and hashCode() methods that compare objects based on identity as defined above.
 * </p>
 * <p>
 * All methods of the interface implementation should check their arguments for validity.
 * NullPointerException and IllegalArgumentException should be thrown if parameters are invalid.
 * BloomFilterSerializeException must be thrown if the object can't be built from a serialized string.
 * </p>
 * <p>
 * The implementation is required to be immutable and so thread-safe.
 * </p>
 * @author real_vg, cucu
 * @version 1.0
 */
public interface HashFunctionFamily {

    /**
     * <p>Returns the number of functions in the family.</p>
     *
     * @return the number of functions in the family
     */
    int getFunctionCount();

    /**
     * <p>
     * Computes the hash code for the specified object, using the function given by functionIndex,
     * and using the range from 0 to maxHash inclusive.
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
    int computeHash(int functionIndex, int maxHash, Object obj);

    /**
     * <p>
     * Returns the string representation of this hash function family.
     * Knowing the class of HashFunctionFamily implementation, the application
     * can use this string to restore the hash function family from it.
     * Returned string should consist only of printable characters, not containing the '|' character.
     * </p>
     * <p>
     * This method is used by BloomFilterSerializer class while building a
     * string representation of the entire Bloom filter.
     * </p>
     *
     * @return the string representation of this hash function family
     */
    String getSerialized();

    /**
     * <p>
     * Compares the specified object with this instance.
     * Returns true if the specified object is of the same class and they're identical,
     * meaning that both objects return the same value for computeHash
     * when called with the same parameters.  If not, it returns false.
     * </p>
     * <p>
     * Overrides the Object.equals() method.
     * </p>
     *
     * @param obj Object to be compared for equality with this hash function family
     * @return true if the specified object is of the same class and the two instances are identical
     */
    boolean equals(Object obj);

    /**
     * <p>
     * Returns the hash code value for this HashFunctionFamily.
     * It is needed to provide the expected behavior when equals() method defined.
     * </p>
     * <p>
     * Overrides the Object.hashCode() method.
     *  </p>
     *
     * @return the hash code value for this HashFunctionFamily
     */
    int hashCode();
}


