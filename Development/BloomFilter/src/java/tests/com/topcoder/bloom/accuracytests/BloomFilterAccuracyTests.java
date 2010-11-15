/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom.accuracytests;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.topcoder.bloom.BloomFilter;
import com.topcoder.bloom.DefaultHashFunctionFamily;


/**
 * <p>This ArruracyTests of BloomFilter.</p>
 *
 * @author telly12
 * @version 1.0
 */
public class BloomFilterAccuracyTests extends TestCase {
    /**
     * <p>The BloomFilter of to Arruracy tests.</p>
     */
    private BloomFilter bloomFilter = null;

    /**
     * <p>The setUp of the ArruracyTests.</p>
     *
     */
    protected void setUp() {
        bloomFilter = new BloomFilter(10, 0.01f);
    }

    /**
     * <p>Test the construct with capacity and errorRate.</p>
     *
     */
    public void testconstruct() {
        assertNotNull("Construct BloomFilter failed.", bloomFilter);
    }

    /**
     * <p>Test the constructor with capacity and errorRate, hashFunctionsClass.</p>
     *
     */
    public void testconstruct_withClass() {
        bloomFilter = new BloomFilter(10, 0.5f,
                DefaultHashFunctionFamily.class);
        assertNotNull("Construct BloomFilter failed.", bloomFilter);
    }

    /**
     * <p>Test the constructor with bitSetSize and hashFunctions.</p>
     *
     */
    public void testconstruct_withserializedAndFunctionFamily() {
        String serialized = "48:topcoder";
        bloomFilter = new BloomFilter(serialized,
                new DefaultHashFunctionFamily(2));
        assertNotNull("Construct BloomFilter failed.", bloomFilter);
    }
    /**
     * <p>Inheritance tests.</p>
     *
     */
    public void testInheritance() {
        assertTrue("bloomFilter should be implements Cloneable interface.", bloomFilter instanceof Cloneable);
    }

    /**
     * <p>Test the constructor with bitSetSize and hashFunctions.</p>
     *
     */
    public void testconstruct_withbitSetSize() {
        bloomFilter = new BloomFilter(2, new DefaultHashFunctionFamily(2));
        assertNotNull("Construct BloomFilter failed.", bloomFilter);
    }

    /**
     * <p>Test the constructor with bitSetSize and hashFunctions.</p>
     *
     */
    public void testconstruct_withserialized() {
        String serialized = DefaultHashFunctionFamily.class.getName() + "|5|48:topcoder";
        bloomFilter = new BloomFilter(serialized);
        assertNotNull("Construct BloomFilter failed.", bloomFilter);
    }
    /**
     * <p>Test the method add and constans.</p>
     *
     */
    public void testaddAndContains() {
        Object object = new Object();
        bloomFilter.add(object);
        assertTrue("The object is added.", bloomFilter.contains(object));
    }

    /**
     * <p>Test the method constainsAll and addAll.</p>
     *
     */
    public void testaddAllAndContainsAll() {
        Object object1 = new Object();
        Object object2 = new Object();
        Object object3 = new Object();
        List list = new ArrayList();
        list.add(object1);
        list.add(object2);
        list.add(object3);

        //add object1, object2, object3
        bloomFilter.addAll(list);

        //all the three objects should be contained by the bloomFilter
        assertTrue("The object1 is added.", bloomFilter.contains(object1));
        assertTrue("The object2 is added.", bloomFilter.contains(object2));
        assertTrue("The object3 is added.", bloomFilter.contains(object3));
        assertTrue("The list should be all contained.",
            bloomFilter.containsAll(list));
    }

    /**
     * <p>Test the method equals.</p>
     *
     */
    public void testEquals1() {
        boolean equal = false;
        equal = bloomFilter.equals(null);
        assertFalse("The bloomFilter not euqal to null.", equal);
    }

    /**
     * <p>Test the method equals.</p>
     *
     */
    public void testEquals2() {
        boolean equal = false;
        equal = bloomFilter.equals(new Object());
        assertFalse("The bloomFilter not euqal any Object.", equal);
    }

    /**
     * <p>Test the method equals.</p>
     *
     */
    public void testEquals3() {
        boolean equal = false;
        equal = bloomFilter.equals(new BloomFilter(10, 0.01f));
        assertTrue("The bloomFilter is euqal to the BloomFilter.", equal);
    }

    /**
     * <p>Test the method isEmpty.</p>
     *
     */
    public void testisEmpty() {
        assertTrue("The bloomFilter is empty.", bloomFilter.isEmpty());
        bloomFilter.add(new Object());
        //after add a Object, then the bloomFilter is not Empty.
        assertFalse("The bloomFilter is not empty.", bloomFilter.isEmpty());
    }
    /**
     * <p>
     * Test the method unite.
     * After unite, the object contained by another BloomFilter,
     * not it is contained by the BloomFilter.
     * </p>
     *
     */
    public void testunite() {
        Object object1 = new Object();
        Object object2 = new Object();
        Object object3 = new Object();
        BloomFilter f1 = new BloomFilter(10, 0.01f);
        BloomFilter f2 = new BloomFilter(10, 0.01f);
        //f1 BloomFilter add object1 and object2
        f1.add(object1);
        f1.add(object2);
        //f2 BloomFilter add object2 and object3
        f2.add(object2);
        f2.add(object3);
        //unite f1 and f2
        f1.unite(f2);
        //then f1 should contain f1,f2,f3
        assertTrue("f1 should contain object1.", f1.contains(object1));
        assertTrue("f1 should contain object2.", f1.contains(object2));
        assertTrue("f1 should contain object3.", f1.contains(object3));
    }
    /**
     * <p>
     * Test the method intersect.
     * After intersect, only the object both contained the two BloomFilters,
     * not it is contained by the BloomFilter.
     * </p>
     *
     */
    public void testintersect() {
        Object object1 = new Object();
        Object object2 = new Object();
        Object object3 = new Object();
        BloomFilter f1 = new BloomFilter(10, 0.01f);
        BloomFilter f2 = new BloomFilter(10, 0.01f);
        //f1 BloomFilter add object1 and object2
        f1.add(object1);
        f1.add(object2);
        //f2 BloomFilter add object2 and object3
        f2.add(object2);
        f2.add(object3);
        //unite f1 and f2
        f1.intersect(f2);
        //then f1 should only contain f2
        assertFalse("f1 should not contain object1.", f1.contains(object1));
        assertTrue("f1 should contain object2.", f1.contains(object2));
        assertFalse("f1 should not contain object3.", f1.contains(object3));
    }
    /**
     * <p>Test the method clear().</p>
     *
     */
    public void testClear() {
        Object object = new Object();
        bloomFilter.add(object);
        assertTrue("The bloomFilter should contain the object.", bloomFilter.contains(object));
        //do the clear
        bloomFilter.clear();

        //now the bloomFilter may not contain the  object.
        assertFalse("The bloomFilter should not contain the object.", bloomFilter.contains(object));
    }
    /**
     * <p>Test the method getSerializedBitSet.</p>
     *
     */
    public void testgetSerializedBitSet() {
        bloomFilter = new BloomFilter("48:topcoder", new DefaultHashFunctionFamily(5));
        assertEquals("The SerializedBitSet should be the same as the set one.", "48:topcoder",
                bloomFilter.getSerializedBitSet());
    }
    /**
     * <p>Test the method getSerialized.</p>
     *
     */
    public void testgetSerialized() {
        //get the Serialized String
        String s = DefaultHashFunctionFamily.class.getName() + "|5|48:topcoder";
        bloomFilter = new BloomFilter(s);
        assertEquals("The SerializedString should be the same as the set one.", s,
                bloomFilter.getSerialized());
    }
    /**
     * <p>Test the method getHashFunctionFamily.</p>
     *
     */
    public void testgetHashFunctionFamily() {
        assertTrue("The HashFunctionFamily should be DefaultHashFunctionFamily.",
                bloomFilter.getHashFunctionFamily() instanceof DefaultHashFunctionFamily);
    }
    /**
     * <p>Test the method hashCode.</p>
     *
     * @throws Exception to Junit
     *
     */
    public void testhashCode() throws Exception {
        BitSet set = new BitSet(2);
        DefaultHashFunctionFamily family = new DefaultHashFunctionFamily(5);
        int target = set.hashCode() ^ family.hashCode() ^ 2;
        bloomFilter = new BloomFilter(2, family);
        assertTrue("The value should the same as the target.", target == bloomFilter.hashCode());
    }
    /**
     * <p>Test the method getBitSetLength.</p>
     *
     */
    public void testgetBitSetLength() {
        bloomFilter = new BloomFilter(5, new DefaultHashFunctionFamily(5));
        assertEquals("The length of the BigSet should be 5.", 5, bloomFilter.getBitSetLength());
    }
    /**
     * <p>Test the method equals with the different bitSetSize.</p>
     *
     */
    public void testEquals() {
    	BloomFilter b1 = new BloomFilter(4, new DefaultHashFunctionFamily(5));
    	BloomFilter b2 = new BloomFilter(5, new DefaultHashFunctionFamily(5));
    	assertFalse("The two BloomFilters should not be equals.", b1.equals(b2));
    }
    /**
     * <p>Test the method equals and Clone with the different bitSetSize.</p>
     *
     * @throws Exception to Junit
     */
    public void testEqualsAndClone() throws Exception {
    	BloomFilter b1 = new BloomFilter(4, new DefaultHashFunctionFamily(5));
    	BloomFilter b2 = (BloomFilter)b1.clone();
    	assertTrue("The two BloomFilters should be the sample bitSetSize length.",
    		    b1.getBitSetLength() == b2.getBitSetLength());
    	assertTrue("The clone object should be equal to itself.", b1.equals(b2));
    }
}
