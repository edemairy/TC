/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom;

import java.lang.reflect.Constructor;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * <p>
 * This class represents an implementation of a Bloom filter.
 * A Bloom filter is a probabilistic data structure that can be used to test
 * for set membership in constant space and constant time.
 * It may return false positives, but never false negatives.
 * </p>
 *
 * <p>
 * Descriptions of the algorithm can be found at http://en.wikipedia.org/wiki/Bloom_filter,
 * http://www.cs.wisc.edu/~cao/papers/summary-cache/node8.html,
 * and http://portal.acm.org/citation.cfm?id=362692&dl=ACM&coll=portal.
 * </p>
 *
 * Operations supported by the Bloom filter data structure are:
 *
 * <ul>
 * <li>item addition, either one by one or from a collection</li>
 * <li>checking item membership</li>
 * <li>union of two Bloom filters</li>
 * <li>intersection of two Bloom filters</li>
 * </ul>
 *
 * <p>
 * All methods of the class check their arguments for validity.
 * NullPointerException and IllegalArgumentException are thrown if parameters are invalid.
 * BloomFilterSerializeException is thrown if the object couldn't be serialized or deserialized.
 * IncompatibleBloomFiltersException is thrown by unite and intersect method when trying to
 * operate with non compatible filters.
 * <p>
 *
 * <p>
 * This class is mutable, so it is not thread-safe.
 * </p>
 * @author real_vg, cucu
 * @version 1.0
 */
public class BloomFilter implements Cloneable {

    /**
     * <p>
     * Represents the constant string used to look up characters for base64 coding/decoding.
     * </p>
     */
    private static final String BASE64_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_:";
    /**
     * Each base 64 character represents 6 bits.
     */
    private static final int BITS_PER_BASE64_CHAR = 6;
    /**
     * Separation character for serialization.
     */
    private static final char SEPARATION_CHAR = '|';
    /**
     * Regular expression used to divide a String using the separation character.
     */
    private static final String SEPARATION_REGEX = "[" + SEPARATION_CHAR + "]";
    /**
     * How many parts there are when doing a complete serialization (i.e. including also the hash function family)
     */
    private static final int COMPLETE_SERIALIZATION_PARTS = 3;
    /**
     * <p>
     * Represents the bit set used by Bloom filter to store
     * information about item membership.
     * Is initialized in constructor or clone() method, must not be null.
     * BitSet will have fixed length through all the object's lifetime.
     * It is accessed almost by all class methods.
     * </p>
     */
    private BitSet bitSet = null;
    /**
     * <p>
     * Represents the exact size of bitSet in bits.  This is needed because BitSet implementation does not
     * store the actual value; instead it rounds the value up.
     * </p>
     */
    private int bitSetSize = 0;
    /**
     * <p>
     * Represents the family of hash functions used by this
     * Bloom filter to calculate hash values.
     * Is initialized in constructor or clone() method and then never modified, must not be null.
     * Is used by add() and contains() methods to calculate hashes.
     * Can be read by getHashFunctionFamily() method.  A reference of this object is returned because
     * HashFunctionFamily implementors are intended to be immutable.
     * Is is used also when checking for Bloom filters compatibility.
     * </p>
     */
    private HashFunctionFamily hashFunctions = null;

    /**
     * <p>
     * Creates a new Bloom filter with specified capacity and error rate.
     * The number of bits and the number of hash functions is
     * calculated based on the specified capacity and maximum error rate.
     * Instance of DefaultHashFunctionFamily class is used for hash calculation.
     * </p>
     *
     * @param capacity the maximum number of items to be inserted while preserving specified maximum error rate,
     * must be a positive integer
     * @param errorRate the maximum error rate (probability of false positives), must be a positive float less than 1.0
     * @throws IllegalArgumentException if any of parameters is not positive, errorRate is not less than 1.0 or
     * there is not enough memory to allocate the filter.
     */
    public BloomFilter(final int capacity, final float errorRate) {
        this(capacity, errorRate, DefaultHashFunctionFamily.class);
    }

    /**
     * <p>Creates a new Bloom filter with a specified capacity, error rate and
     *  hash function family.
     * The number of bits and the number of hash functions is
     * calculated based on the specified capacity and maximum error rate.
     * An instance of the specified class is used for hash calculation.
     * </p>
     *
     * @param capacity the maximum number of items to be inserted while preserving specified maximum error rate,
     * must be a positive integer
     * @param errorRate the maximum error rate (probability of false positives), must be a positive float less than 1.0
     * @param hashFunctionsClass the class to use for hash calculating, must be an implementation of HashFunctionFamily,
     * must not be null
     * @throws IllegalArgumentException if capacity or errorRate are not positive, errorRate is not less than 1.0,
     * hashFunctionsClass can't be instantiated, or there is not enough memory to allocate the filter.
     * @throws NullPointerException if hashFunctionsClass is null
     */
    public BloomFilter(int capacity, float errorRate, Class hashFunctionsClass) {
        System.out.println(capacity);
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if ((errorRate <= 0) || (errorRate >= 1)) {
            throw new IllegalArgumentException("Error rate must be positive");
        }
        this.bitSetSize =
                (int) Math.ceil(
                -(capacity * Math.log(errorRate)) / (Math.log(2)*Math.log(2)));
        this.bitSetSize = Math.max(2,this.bitSetSize);
        this.bitSet = new BitSet(bitSetSize);
        if (this.bitSet == null) {
            throw new IllegalArgumentException("Not enough memory available to allocate the bitset.");
        }
        if (hashFunctionsClass == null) {
            throw new NullPointerException("hashFunctionClass must not be null.");
        }
        try {
            Constructor constructor = hashFunctionsClass.getConstructor(new Class[]{int.class});
            hashFunctions = (HashFunctionFamily) constructor.newInstance(bitSetSize);
        } catch (Exception e) {
            throw new IllegalArgumentException("Impossible to instantiate the hash function: " + e.getMessage());
        }
    }

    /**
     * <p>
     * Creates new Bloom filter with specified bit set size and HashFunctionFamily object.
     * The number of hash functions is retrieved from hashFunctions
     *
     * </p>
     *
     * @param bitSetSize the size of the bit set to be used by the Bloom filter, must be a positive integer
     * @param hashFunctions an instance of the HashFunctionFamily implementation class, must not be null
     * @throws IllegalArgumentException if bitSetSize is less than 2, or there is not enough memory
     * to allocate the filter.
     * @throws NullPointerException if hashFunctions is null
     */
    public BloomFilter(int bitSetSize, HashFunctionFamily hashFunctions) {
        if (bitSetSize < 2) {
            throw new IllegalArgumentException("bitSetSize must be greater or equal than 2.");
        }
        this.bitSetSize = bitSetSize;
        this.bitSet = new BitSet(bitSetSize);
        if (bitSet == null) {
            throw new IllegalArgumentException("Imposible to allocate bitset.");
        }
        if (hashFunctions == null) {
            throw new NullPointerException("hash functions must not be null.");
        }
        this.hashFunctions = hashFunctions;
    }

    /**
     * <p>
     * Creates a Bloom filter restored from the string representation of
     * its bit set and the given hashFunctions.
     * The string representation should be obtained via getSerializedBitSet() method.
     * </p>
     * <p>
     * The string representation is:
     * <pre>
     *  length:values
     * </pre>
     * Where length is a positive value indicating the length in bits, and values is
     * the representation of those bits in base 64.
     * </p>
     *
     * @param serialized a string representation of the Bloom filter bit set, should not be null or empty string
     * @param hashFunctions an instance of the HashFunctionFamily implementation class, should not be null
     * @throws NullPointerException if serialized or hashFunctions are null
     * @throws BloomFilterSerializeException if the specified string representation is invalid
     */
    public BloomFilter(String serialized, HashFunctionFamily hashFunctions) {
        if (serialized == null) {
            throw new NullPointerException("serialized must not be null");
        }
        if (hashFunctions == null) {
            throw new NullPointerException("hashFunctions must not be null");
        }
        buildBitSet(serialized);
        this.hashFunctions = hashFunctions;
    }

    /**
     * <p>
     * Creates a Bloom filter instance restored from given string representation.
     * The string representation should be obtained by the getSerialized() method.
     * </p>
     * <p>
     * The string representation is:
     * <pre>
     *  HashClassName|HashSerialized|length:values
     * </pre>
     * Where HashClassName is the name of the class implementing HashFunctionFamily, HashSerialized is a String that
     * will be passed to that class constructor, length is a positive value indicating the length in bits,
     * and values is the representation of those bits in base64.
     * bits in base64.
     * </p>
     *
     * @param serialized a string to restore the Bloom filter from
     * @throws NullPointerException if serialized is null
     * @throws BloomFilterSerializeException if specified string representation is invalid or
     * error happens when using Java reflection to restore hash function family
     */
    public BloomFilter(String serialized) {
        if (serialized == null) {
            throw new NullPointerException("serialized must not be null");
        }
        String lengthValues = "0:0";
        try {
            String[] splits = serialized.split(SEPARATION_REGEX);
            String hashClassNameString = splits[0];
            String hashSerializedString = splits[1];
            lengthValues = splits[2];
            Constructor constructor = Class.forName(hashClassNameString).getConstructor(new Class[]{String.class});
            this.hashFunctions = (HashFunctionFamily) constructor.newInstance(hashSerializedString);

        } catch (Exception ex) {
            throw new BloomFilterSerializeException("exception when building the hashFunctions object: " + ex.getMessage());
        }
        if (this.hashFunctions == null) {
            throw new NullPointerException("hash functions is null.");
        }
        try {
            String[] splitValues = lengthValues.split(":");
            bitSetSize = Integer.parseInt(splitValues[0]);
            bitSet = new BitSet(bitSetSize);
        } catch (Exception ex) {
            throw new BloomFilterSerializeException("exception when parsing length of serialized: " + ex.getMessage());
        }


    }

    /**
     * <p>
     * Adds an item to the Bloom filter.
     * It takes constant time.
     * </p>
     *
     * @param item an item to add to Bloom filter, must not be null
     * @throws NullPointerException if item is null
     */
    public void add(Object item) {
        if (item == null) {
            throw new NullPointerException("item is null.");
        }
        int maxHash = getBitSetLength() - 1;
        // compute the hash for each function in the family and set that bit.
        for (int i = 0; i < hashFunctions.getFunctionCount(); i++) {
            bitSet.set(hashFunctions.computeHash(i, maxHash, item));
        }
    }

    /**
     * <p>
     * Adds all items from the specified Collection to the Bloom filter.
     * It takes time proportional to the number of items in collection.
     * </p>
     *
     * @param collection a collection with the elements to be added to the Bloom filter,
     * must not be null, none of the elements must be null
     * @throws NullPointerException if collection is null
     * @throws IllegalArgumentException if collection has at least one null element
     */
    public void addAll(Collection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is null.");
        }
        try {
            for (Object object : collection) {
                this.add(object);
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("The collection must not contain any null element: " + e.getMessage());
        }
    }

    /**
     * <p>
     * Checks if this Bloom filter contains the specified item.
     * Returns true if it does contain the specified item.
     * Note that false positives may be returned, but never false negatives.
     * It doesn't modify the Bloom filter.
     * It takes constant time.
     * </p>
     *
     * @param item an item to check membership of, must not be null
     * @return true if item is contained in the filter, false if it is not.
     * @throws NullPointerException if item is null
     */
    public boolean contains(Object item) {
        if (item == null) {
            throw new NullPointerException("one object is null in the collection.");
        }
        boolean result = true;
        int maxHash = getBitSetLength() - 1;
        // compute the hash for each function in the family and set that bit.
        for (int i = 0; ((i < hashFunctions.getFunctionCount()) && (result)); i++) {
            result &= bitSet.get(hashFunctions.computeHash(i, maxHash, item));

        }
        return result;
    }

    /**
     * <p>
     * Checks if this Bloom filter contains all the items contained in the collection.
     * Returns true if it contains all the items.
     * Note that false positives may be returned, but never false negatives.
     * It doesn't modify the Bloom filter.
     * It takes linear time proportional to the number of items in collection.
     * </p>
     *
     * @param collection the collection to test membership of items of, should not be null, elements should not be null
     * @return true if all the elements in the collection are in the filter, or if the collection is empty.
     * @throws NullPointerException if collection is null
     * @throws IllegalArgumentException if any of the collection items is null
     */
    public boolean containsAll(Collection collection) {
        if (collection == null) {
            throw new NullPointerException("null collection");
        }
        boolean result = true;
        try {
            for (Iterator it = collection.iterator(); it.hasNext() && result;) {
                Object object = it.next();
                result &= this.contains(object);
            }
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("One item of the collection is null: " + ex);
        }
        return result;
    }

    /**
     * <p>
     * Compares the specified object with this Bloom filter for equality.
     * Returns true if the specified object is also a Bloom filter, the two Bloom filters
     * have the same bit sets and the same hash function families.
     * </p>
     * <p>
     * Overrides the Object.equals() method.
     * </p>
     *
     * @param obj  Object to be compared for equality with this Bloom filter, can be null
     * @return true if the specified Object is equal to this Bloom filter
     */
    public boolean equals(Object obj) {
        boolean result = true;
        BloomFilter filter = null;
        try {
            filter = (BloomFilter) obj;
        } catch (ClassCastException e) {
            return false;
        }
        result &= (filter != null);
        if (result) {
            result &= (this.bitSet.equals(filter.bitSet));
            result &= (this.hashFunctions.equals(filter.hashFunctions));
        }
        return result;
    }

    /**
     * <p>
     * Returns the hash code value for this Bloom filter.
     * </p>
     * <p>
     * Overrides the Object.hashCode() method.
     *  </p>
     *
     * @return the hash code value for this Bloom filter.
     */
    public int hashCode() {
        return 0;
    }

    /**
     * <p>
     * Returns true if this Bloom filter contains no items.
     * It doesn't modify the Bloom filter.
     * It takes constant time.
     * </p>
     *
     * @return true if this Bloom filter contains no items
     */
    public boolean isEmpty() {
        return bitSet.isEmpty();
    }

    /**
     * <p>
     * Performs union operation on this Bloom filter and specified Bloom filter.
     * The result is stored in this Bloom filter.
     * Two Bloom filters are required to be compatible to perform this operation, meaning that they
     * must have equal lengths of bit sets and equal hash function families.
     * </p>
     * <p>
     * This operation modifies this Bloom filter, taking time proportional to the bit set length.
     * </p>
     *
     * @param bloomFilter the Bloom filter to perform union with, must not be null, must be compatible with this filter.
     * @return this BloomFilter, which can be used to perform further operations
     * @throws NullPointerException if bloomFilter is null
     * @throws IncompatibleBloomFiltersException if the two Bloom filters are incompatible
     */
    public BloomFilter unite(BloomFilter bloomFilter) {
        if (bloomFilter == null) {
            throw new NullPointerException("bloomFilter is null.");
        }
        if ((this.bitSetSize != bloomFilter.bitSetSize) || (!this.hashFunctions.equals(bloomFilter.hashFunctions))) {
            throw new IncompatibleBloomFiltersException("the two Bloom filters are incompatible");
        }

        BitSet bs = bloomFilter.bitSet;
        for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) {
            this.bitSet.set(i);
        }
        return this;
    }

    /**
     * <p>
     * Performs intersection operation on this Bloom filter and specified Bloom filter.
     * The result is stored in this Bloom filter.
     * Two Bloom filters are required to be compatible to perform this operation, meaning that they
     * must have equal lengths of bit sets and equal hash function families.
     * <p>
     * This operation modifies this Bloom filter, taking time proportional to the bit set length.
     * </p>
     *
     * @param bloomFilter the Bloom filter to perform union with, must not be null, must be compatible with this filter.
     * @return this BloomFilter, which can be used to perform further operations
     * @throws NullPointerException if bloomFilter is null
     * @throws IncompatibleBloomFiltersException if the two Bloom filters are incompatible
     */
    public BloomFilter intersect(BloomFilter bloomFilter) {
        if (bloomFilter == null) {
            throw new NullPointerException("bloomFilter is null.");
        }
        if ((this.bitSetSize != bloomFilter.bitSetSize) || (!this.hashFunctions.equals(bloomFilter.hashFunctions))) {
            throw new IncompatibleBloomFiltersException("");
        }

        BitSet bs = bloomFilter.bitSet;
        for (int i = 0; i < bloomFilter.bitSetSize; ++i) {
            this.bitSet.set(i, this.bitSet.get(i) && bs.get(i));
        }
        return this;
    }

    /**
     * <p>
     * Removes all the items from the Bloom filter.
     * </p>
     */
    public void clear() {
        this.bitSet.clear();
    }

    /**
     * <p>
     * Returns the string representation of this Bloom filter's bit set.
     * The filter can be restored later using this string and the hashFamily object.
     * </p>
     * <p>
     * The returned string representation is:
     * <pre>
     *  length:values
     * </pre>
     * Where length is a positive value indicating the length in bits, and values is the representation of those
     * bits in base 64.
     * </p>
     *
     * @return string representation of this Bloom filter's bit set.
     */
    public String getSerializedBitSet() {
        String result = bitSetSize + ":";
        char val;
        for (int nbc = 0; nbc < bitSetSize; nbc += 6) {
            val = 0;
            for (int j = 5; j > 0; --j) {
                if (bitSet.get(nbc + (5 - j))) {
                    val += (2 ^ j);
                }
            }
            result += BASE64_CHARS.charAt(val);
        }
        return result;
    }

    /**
     * <p>
     * Builds a string representation of this Bloom filter.
     * The filter can be restored later using this string.
     * </p>
     * <p>
     * The string representation is:
     * <pre>
     *  HashClassName|HashSerialized|length:values
     * </pre>
     * Where HashClassName is the name of the class implementing HashFunctionFamily, HashSerialized is a String that
     * will be passed to that class constructor, length is a positive value indicating the length in bits,
     * and values is the representation of those bits in base64.
     * bits in base64.
     * </p>
     *
     * @return a string representation of the Bloom filter
     */
    public String getSerialized() {
        String result = hashFunctions.getClass().getName()
                + SEPARATION_CHAR
                + hashFunctions.getSerialized()
                + SEPARATION_CHAR
                + getSerializedBitSet();

        return result;
    }

    /**
     * <p>
     * Returns the hash function family used by this Bloom filter.
     * </p>
     *
     * @return  the hash function family used by this Bloom filter
     */
    public HashFunctionFamily getHashFunctionFamily() {
        return hashFunctions;
    }

    /**
     * <p>
     * Clone this Bloom Filter, generating an identical filter.
     * </p>
     * <p>
     * Overrides the clone() method of Object.
     * </p>
     *
     * @return a clone of this Bloom filter
     */
    public Object clone() {
        return this;
    }

    /**
     * <p>
     * Returns the length of bit set used by this Bloom filter.
     * This operation doesn't modify the Bloom filter.
     * </p>
     *
     * @return the length of bit set used by this Bloom filter.
     */
    public int getBitSetLength() {
        return bitSetSize;
    }

    /**
     * <p>
     * Helper method to build a BitSet from a serialization string.
     * It sets the object fields bitSetSize and bitSet.
     * </p>
     * <p>
     * The string representation is:
     * <pre>
     *  length:values
     * </pre>
     * Where length is a positive value indicating the length in bits, and values is the representation of those
     * bits in base 64.
     * </p>
     *
     * @throws BloomFilterSerializeException if the object can't be built from the serialized string.
     * @param bitSetString the serialized representation of a bitSet
     */
    private void buildBitSet(String bitSetString) {
        try {
            String[] splits = bitSetString.split(":",1);
            bitSetSize = Integer.parseInt(splits[0]);
            bitSet = new BitSet(bitSetSize);
            System.err.println("bitSetSize="+bitSetSize);
            bitSetString = splits[1];
            int nbc=0;
            for (int i = 0; i < bitSetSize; i += BITS_PER_BASE64_CHAR, nbc++) {
                int val = BASE64_CHARS.indexOf(bitSetString.charAt(nbc));
                for (int j = i+BITS_PER_BASE64_CHAR-1; j >=i ; j--) {
                    bitSet.set(j, val % 2);
                    val /= 2;
                }
            }
            int remainingBits = bitSetSize % BITS_PER_BASE64_CHAR;
            if ((remainingBits) > 0) {
                int val = BASE64_CHARS.indexOf(bitSetString.charAt(nbc));
                for (int j = bitSetSize-1; j > bitSetSize-remainingBits-1; j--) {
                    bitSet.set(remainingBits - j - 1, val % 2);
                    val /= 2;
                }
            }
        } catch (Exception e) {
            throw new BloomFilterSerializeException("object can't be built from the serialized string: " + e.getMessage());
        }
    }

    /**
     * <p>
     * Check that this filter is compatible with the specified filter, throwing IncompatibleBloomFiltersException
     * if they aren't.
     * Two filters are compatible if they have the same bit set length and their hash functions are equal.
     * </p>
     *
     * @param bloomFilter the filter to compare with.
     * @throws NullPointerException if bloomFilter is null
     * @throws IncompatibleBloomFiltersException if the filter is not compatible with this one.
     */
    private void checkCompatibility(BloomFilter bloomFilter) {
    }

    /**
     * <p>
     * Check that the collection is non null and all his elements are non-null.
     * </p>
     *
     * @param collection the collection to check
     * @throws NullPointerException if collection is null
     * @throws IllegalArgumentException if any of the elements of collection is null
     */
    private void checkCollection(Collection collection) {
    }

    /**
     * <p>
     * Create a HashFunctionFamily using reflection. It uses a constructor taking one parameter.
     * All the exceptions are wrapped in IllegalArgumentException.
     * </p>
     *
     * @param hashClass the class to be instantiated
     * @param constructorParameterType the type of the parameter for the constructor
     * @param constructorParameterValue the value of the parameter for the constructor
     * @return a HashFunctionFamily of the exact class specified
     * @throws IllegalArgumentException if the class can't be instantiated
     */
    private HashFunctionFamily createHashFunctions(Class hashClass, Class constructorParameterType,
            Object constructorParameterValue) {
        return hashFunctions;
    }
}
