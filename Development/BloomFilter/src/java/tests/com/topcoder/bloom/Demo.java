/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;

/**
 * <p>
 * Demonstrations for the BloomFilter component.
 * </p>
 *
 * @author cucu
 * @version 1.0
 */
public class Demo extends TestCase {

    /**
     * Shows the general usage of this component, using the component specification example.
     */
    public void testSpecDemo() {
        // Create a Bloom filter with the specified capacity and maximal error rate
        // and custom hash function family
        BloomFilter filter00 = new BloomFilter(1000, 0.01f, DefaultHashFunctionFamily.class);

        // Create a Bloom filter with the specified capacity and maximal error rate
        BloomFilter filter1 = new BloomFilter(1000, 0.01f);


        // Create a Bloom filter with the specified length of bit set - 10000
        // and number of hash functions - 10
        BloomFilter filter0 = new BloomFilter(10000, new DefaultHashFunctionFamily(10));


        // Add some items to Bloom filter
        filter1.add("item1");
        filter1.add("item2");
        filter1.add(new Integer(17));

        // Create a copy of a Bloom filter
        BloomFilter filter2 = (BloomFilter) filter1.clone();

        Collection c = new HashSet(); // collection should contain non-null items
        c.add("item3");

        // Add the items contained in collection to the Bloom filter
        filter2.addAll(c);

        // Compute the intersection of two Bloom filters
        BloomFilter intersectionFilter = ((BloomFilter) filter2.clone()).intersect(filter1);

        // Check filters for equality
        intersectionFilter.equals(filter1); // Should return true
        intersectionFilter.equals(filter2); // Should return false

        // Compute the union of two Bloom filters
        BloomFilter unionFilter = ((BloomFilter) filter2.clone()).unite(filter1);

        //Check filters for equality
        unionFilter.equals(filter2); // Should return true
        unionFilter.equals(filter1); // Should return false

        // Compute the union of two Bloom filters and store the result in the first Bloom filter
        filter1.unite(filter2);

        // Check filters for equality
        filter1.equals(filter2); // Should return true

        // Convert the Bloom filter to the string representation
        String serialized = filter1.getSerialized();

        // Restore the Bloom filter from the string representation
        filter2 = new BloomFilter(serialized);

        // Check filters for equality
        filter1.equals(filter2); // Should return true

        // Convert the bit set of the Bloom filter to the string representation
        String bitSetString = filter1.getSerializedBitSet();

        // Convert the hash function family to the string representation
        String hashFamilyString = filter1.getHashFunctionFamily().getSerialized();

        // Restore hash function family from the string representation
        HashFunctionFamily family = new DefaultHashFunctionFamily(hashFamilyString);

        // Restore Bloom filter
        BloomFilter filter5 = new BloomFilter(bitSetString, family);
    }

    /**
     * Shows the usage of add, addAll, contains and containsAll methods.
     */
    public void testDemoAddContains() {
        BloomFilter bf = new BloomFilter(1000, 0.01f);

        System.out.println("------------< Demo for Add and Contains >------------");

        // add some elements one by one
        bf.add("The Beatles");
        bf.add("Pink Floyd");

        // check elements
        System.out.println("'The Beatles' is included: " + bf.contains("The Beatles"));
        System.out.println("'Led Zeppelin' is included: " + bf.contains("Led Zeppelin"));

        // add more elements through a collection
        List l = new ArrayList();
        l.add("Led Zeppelin");
        l.add("Genesis");
        l.add("The Doors");

        bf.addAll(l);

        // check elements with contains all
        l.clear();
        l.add("The Beatles");
        l.add("Genesis");

        System.out.println("'The Beatles' and 'Genesis' are included: " + bf.containsAll(l));

        l.add("Elvis Presley");
        System.out.println("'The Beatles','Genesis' and 'Elvis Presley' are included: " + bf.containsAll(l));

        System.out.println("------------< End Demo for Add and Contains >------------");

    }

    /**
     * Shows the usage of unite and intersect methods.
     */
    public void testDemoUniteIntersect() {
        BloomFilter bf1 = new BloomFilter(100, 0.01f);
        BloomFilter bf2 = new BloomFilter(100, 0.01f);

        System.out.println("------------< Demo for Unite and Intersect >------------");

        List l = new ArrayList();
        l.add("cat");
        l.add("dog");
        l.add("mouse");
        bf1.addAll(l);

        l.clear();
        l.add("mouse");
        l.add("keyboard");
        l.add("screen");
        bf2.addAll(l);

        // create a filter that's the union between bf1 and bf2
        BloomFilter union = ((BloomFilter) bf1.clone()).unite(bf2);
        System.out.println("'cat' is included in union: " + union.contains("cat"));
        System.out.println("'keyboard' is included in union: " + union.contains("keyboard"));
        System.out.println("'mouse' is included in union: " + union.contains("mouse"));
        System.out.println("'pencil' is included in union: " + union.contains("pencil"));

        System.out.println("");

        // create a filter that's the intersection between bf1 and bf2
        BloomFilter intersection = ((BloomFilter) bf1.clone()).intersect(bf2);
        System.out.println("'cat' is included in intersection: " + intersection.contains("cat"));
        System.out.println("'keyboard' is included in intersection: " + intersection.contains("keyboard"));
        System.out.println("'mouse' is included in intersection: " + intersection.contains("mouse"));
        System.out.println("'pencil' is included in intersection: " + intersection.contains("pencil"));


        System.out.println("------------< End Demo for Unite and Intersect >------------");
    }

    /**
     * Shows the usage of the serialization/deserealization methods.
     */
    public void testDemoSerializing() {
        BloomFilter bf1 = new BloomFilter(100, 0.01f);

        System.out.println("------------< Demo for Serealization/Desrealization >------------");

        List l = new ArrayList();
        l.add(new Integer(26));
        l.add(new Integer(3));
        l.add(new Integer(78));
        bf1.addAll(l);


        // serialize the whole filter
        String serialized = bf1.getSerialized();

        // build a new filter from the serialization
        BloomFilter bf2 = new BloomFilter(serialized);

        System.out.println("'26' is included in bf2: " + bf2.contains(new Integer(26)));
        System.out.println("'0' is included in bf2: " + bf2.contains(new Integer(0)));

        System.out.println("");

        // serialize just the bit set and get the hash function family
        String serializedBitSet = bf1.getSerializedBitSet();
        HashFunctionFamily hff = bf1.getHashFunctionFamily();

        // rebuild from the bit set serialization and the hash function family
        BloomFilter bf3 = new BloomFilter(serializedBitSet, hff);
        System.out.println("'26' is included in bf3: " + bf3.contains(new Integer(26)));
        System.out.println("'0' is included in bf3: " + bf3.contains(new Integer(0)));


        System.out.println("------------< End Demo for Serealization/Desrealization >------------");

    }

}
