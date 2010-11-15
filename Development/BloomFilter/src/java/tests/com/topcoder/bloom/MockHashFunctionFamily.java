/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom;

/**
 * <p>
 * This class is a Mock implementation of HashFunctionFamily used for testing.
 * </p>
 *
 * @author cucu
 * @version 1.0
 */
public class MockHashFunctionFamily implements HashFunctionFamily {

    /**
     * Number of functions in the family.
     */
    private final int functionCount;

    /**
     * The value that will be returned in computeHash.
     * It is incremented each time the hash is computed
     */
    private int currentValue = 0;

    /**
     * <p>
     * Creates a Mock hash function family. The functionCount is saved.
     * </p>
     *
     * @param functionCount the number of hash functions the created family should consist of
     */
    public  MockHashFunctionFamily(int functionCount) {
        this.functionCount = functionCount;
    }

    /**
     * <p>
     * Creates a Mock hash function family. The functionCount is saved.
     * </p>
     *
     * @param serialized a string representation of the hash function family.
     */
    public  MockHashFunctionFamily(String serialized) {
        this(Integer.parseInt(serialized));
    }

    /**
     * <p>Returns the number of functions in the family.</p>
     *
     * @return the number of functions in the family
     */
    public int getFunctionCount() {
        return functionCount;
    }

    /**
     * <p>
     * Returns a value that increments each time this method is called.
     * </p>
     *
     * @param functionIndex not taken into account
     * @param maxHash not taken into account
     * @param obj not taken into account
     * @return a value that increments each time
     */
    public int computeHash(int functionIndex, int maxHash, Object obj) {
        return currentValue++ % (maxHash + 1);
    }

    /**
     * <p>
     * Returns the string representation of this hash function family.
     * </p>
     * @return the string representation of this hash function family
     */
    public String getSerialized() {
        return functionCount + "";
    }

    /**
     * <p>
     * Compares the specified object with this object for equality.
     * Returns true if the specified object is also a MockHashFunctionFamily instance,
     * and the two instances have the same function count.
     * </p>
     * <p>
     * Overrides the Object.equals() method.
     * </p>
     *
     * @param obj Object to be compared for equality with this hash function family
     * @return true if the specified object is also a HashFunctionFamily instance,  the two instances are identical
     */
    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof MockHashFunctionFamily)) {
            return getFunctionCount() == ((MockHashFunctionFamily) obj).getFunctionCount();
        }
        return false;
    }

    /**
     * <p>
     * Returns the hash code value for this HashFunctionFamily.
     *  </p>
     *
     * @return the hash code value for this HashFunctionFamily
     */
    public int hashCode() {
        return getFunctionCount();
    }
}
