/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom.failuretests;

import com.topcoder.bloom.BloomFilter;
import com.topcoder.bloom.BloomFilterSerializeException;
import com.topcoder.bloom.DefaultHashFunctionFamily;
import com.topcoder.bloom.IncompatibleBloomFiltersException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Failure tests for BloomFilter.
 *
 * @author mgmg
 * @version 1.0
 */
public class BloomFilterFailureTest extends TestCase {
    /**
     * The instance for test purpose.
     */
    private BloomFilter instance = null;

    /**
     * The instances for incompatible test purpose.
     */
    private BloomFilter[] instances = new BloomFilter[4];

    /**
     * Aggregate the tests in this class.
     *
     * @return
     */
    public static Test suite() {
        return new TestSuite(BloomFilterFailureTest.class);
    }

    /**
     * Create the test environment.
     */
    public void setUp() throws Exception {
        this.instance = new BloomFilter(100, 0.1f);

        this.instances[0] = new BloomFilter(1000,
                new DefaultHashFunctionFamily(7));
        this.instances[1] = new BloomFilter(1001,
                new DefaultHashFunctionFamily(7));
        this.instances[2] = new BloomFilter(1000,
                new DefaultHashFunctionFamily(8));
        this.instances[3] = new BloomFilter(1000, new MockHashFunctionFamily());
    }

    /**
     * Failure tests for constructor1
     * The capacity isn't positive.
     * IAE should be thrown.
     */
    public void testConstructor1_InvalidCapacity() {
        try {
            new BloomFilter(0, 0.1f);
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException iae) {
            // success.
        }
    }

    /**
     * Failure tests for constructor1
     * The errorRate is not in 0 to 1.
     * IAE should be thrown.
     */
    public void testConstructor1_InvalidErrorRate1() {
        try {
            new BloomFilter(20, 0.0f);
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException iae) {
            // success.
        }
    }

    /**
     * Failure tests for constructor1
     * The errorRate is not in 0 to 1
     * IAE should be thrown.
     */
    public void testConstructor1_InvalidErrorRate2() {
        try {
            new BloomFilter(20, 1.0f);
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException iae) {
            // success.
        }
    }

    /**
     * Failure tests for constructor2
     * The capacity isn't positive.
     * IAE should be thrown.
     */
    public void testConstructor2_InvalidCapacity() {
        try {
            new BloomFilter(0, 0.1f, DefaultHashFunctionFamily.class);
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException iae) {
            // success.
        }
    }

    /**
     * Failure tests for constructor2
     * The errorRate is not in 0 to 1.
     * IAE should be thrown.
     */
    public void testConstructor2_InvalidErrorRate1() {
        try {
            new BloomFilter(20, 0.0f, DefaultHashFunctionFamily.class);
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException iae) {
            // success.
        }
    }

    /**
     * Failure tests for constructor2
     * The errorRate is not in 0 to 1
     * IAE should be thrown.
     */
    public void testConstructor2_InvalidErrorRate2() {
        try {
            new BloomFilter(20, 1.0f, DefaultHashFunctionFamily.class);
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException iae) {
            // success.
        }
    }

    /**
     * Failure tests for constructor2
     * The hashFunctionsClass is null
     * NPE should be thrown.
     */
    public void testConstructor2_NullHashFunctionClass() {
        try {
            new BloomFilter(20, 0.1f, null);
            fail("NPE should be thrown.");
        } catch (NullPointerException npe) {
            // success.
        }
    }

    /**
     * Failure tests for constructor2
     * The hashFunctionsClass is not correct
     * NPE should be thrown.
     */
    public void testConstructor2_InvalidHashFunctionClass() {
        try {
            new BloomFilter(20, 0.1f, String.class);
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException iae) {
            // success.
        }
    }

    /**
     * Failure tests for constructor3
     * The bit set length is too large.
     * IAE should be thrown.
     */
    public void testConstructor3_TooLarge() {
        try {
            new BloomFilter(1000000000, new DefaultHashFunctionFamily(69315));
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException iae) {
            // success.
        }
    }

    /**
     * Failure tests for constructor3
     * The bit set length is too small.
     * IAE should be thrown.
     */
    public void testConstructor3_InvalidLength() {
        try {
            new BloomFilter(1, new DefaultHashFunctionFamily(5));
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException iae) {
            // success.
        }
    }

    /**
     * Failure tests for constructor3
     * The hashFunctionsClass is null
     * NPE should be thrown.
     */
    public void testConstructor3_NullHashFunctionClass() {
        try {
            new BloomFilter(100, null);
            fail("NPE should be thrown.");
        } catch (NullPointerException npe) {
            // success.
        }
    }

    /**
     * Failure tests for constructor4
     * The serialized is null
     * NPE should be thrown.
     */
    public void testConstructor4_NullString() {
        try {
            new BloomFilter(null, new DefaultHashFunctionFamily(100));
            fail("NPE should be thrown.");
        } catch (NullPointerException npe) {
            // success.
        }
    }

    /**
     * Failure tests for constructor4
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor4_InvalidString1() {
        try {
            new BloomFilter(" ", new DefaultHashFunctionFamily(100));
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor4
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor4_InvalidString2() {
        try {
            new BloomFilter("13abc", new DefaultHashFunctionFamily(10));
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor4
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor4_InvalidString3() {
        try {
            new BloomFilter("13:c", new DefaultHashFunctionFamily(10));
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor4
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor4_InvalidString4() {
        try {
            new BloomFilter("a:ac", new DefaultHashFunctionFamily(10));
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor4
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor4_InvalidString5() {
        try {
            new BloomFilter("13:a|c", new DefaultHashFunctionFamily(10));
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor4
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor4_InvalidString6() {
        try {
            new BloomFilter("1:a", new DefaultHashFunctionFamily(10));
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor4
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor4_InvalidString7() {
        try {
            new BloomFilter("-13:abc", new DefaultHashFunctionFamily(10));
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor4
     * The size is too large.
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor4_TooLarge() {
        try {
            new BloomFilter("1000000000:aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                new DefaultHashFunctionFamily(10));
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor4
     * The HashFunctionFamily is null.
     * NPE should be thrown.
     */
    public void testConstructor4_NullHashFunctionFamily() {
        try {
            new BloomFilter("13:abc", null);
            fail("NPE should be thrown.");
        } catch (NullPointerException npe) {
            // success.
        }
    }

    /**
     * Failure tests for constructor4
     * The serialized is null
     * NPE should be thrown.
     */
    public void testConstructor5_NullString() {
        try {
            new BloomFilter(null);
            fail("NPE should be thrown.");
        } catch (NullPointerException npe) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString1() {
        try {
            new BloomFilter(" ");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString2() {
        try {
            new BloomFilter(
                "com.topcoder.bloom.DefaultHashFunctionFamily|10|13abc");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString3() {
        try {
            new BloomFilter(
                "com.topcoder.bloom.DefaultHashFunctionFamily|10|13:c");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString4() {
        try {
            new BloomFilter(
                "com.topcoder.bloom.DefaultHashFunctionFamily|10|a:ac");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString5() {
        try {
            new BloomFilter(
                "com.topcoder.bloom.DefaultHashFunctionFamily|10|13:a^c");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString6() {
        try {
            new BloomFilter(
                "com.topcoder.bloom.DefaultHashFunctionFamily|10|13:abc|123");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString7() {
        try {
            new BloomFilter(
                "com.topcoder.bloom.DefaultHashFunctionFamily|a|13:abc");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString8() {
        try {
            new BloomFilter("  |10|13:abc");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString9() {
        try {
            new BloomFilter("java.lang.String|10|13:abc");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString10() {
        try {
            new BloomFilter("abcdefg|10|13:abc");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString11() {
        try {
            new BloomFilter(
                "com.topcoder.bloom.DefaultHashFunctionFamily|0|13:abc");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString12() {
        try {
            new BloomFilter(
                "com.topcoder.bloom.DefaultHashFunctionFamily|-10|13:abc");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString13() {
        try {
            new BloomFilter(
                "com.topcoder.bloom.DefaultHashFunctionFamily|10|1:a");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString14() {
        try {
            new BloomFilter(
                "com.topcoder.bloom.DefaultHashFunctionFamily|10|-13:abc");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The serialized is invalid
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_InvalidString15() {
        try {
            new BloomFilter(
                "com.topcoder.bloom.DefaultHashFunctionFamily|13:abc");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure tests for constructor5
     * The size is too large.
     * BloomFilterSerializeException should be thrown.
     */
    public void testConstructor5_TooLarge() {
        try {
            new BloomFilter(
                "com.topcoder.bloom.DefaultHashFunctionFamily|10|1000000000:abc");
            fail("BloomFilterSerializeException should be thrown.");
        } catch (BloomFilterSerializeException e) {
            // success.
        }
    }

    /**
     * Failure test for add.
     * NPE should be thrown because the parameter is null.
     */
    public void testAdd_NullObj() {
        try {
            instance.add(null);
            fail("NPE should be thrown.");
        } catch (NullPointerException npe) {
            //success
        }
    }

    /**
     * Failure test for addAll.
     * NPE should be thrown because the parameter is null.
     */
    public void testAddAll_NullObj() {
        try {
            instance.addAll(null);
            fail("NPE should be thrown.");
        } catch (NullPointerException npe) {
            //success
        }
    }

    /**
     * Failure test for addAll.
     * IAE should be thrown because the parameter contains null.
     */
    public void testAddAll_InvalidObj() {
        try {
            Collection coll = new ArrayList();
            coll.add(new Integer(10));
            coll.add(null);

            instance.addAll(coll);
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException npe) {
            //success
        }
    }

    /**
     * Failure test for contains.
     * NPE should be thrown because the parameter is null.
     */
    public void testContains_NullObj() {
        try {
            instance.add(null);
            fail("NPE should be thrown.");
        } catch (NullPointerException npe) {
            //success
        }
    }

    /**
     * Failure test for containsAll.
     * NPE should be thrown because the parameter is null.
     */
    public void testContainsAll_NullObj() {
        try {
            instance.addAll(null);
            fail("NPE should be thrown.");
        } catch (NullPointerException npe) {
            //success
        }
    }

    /**
     * Failure test for containsAll.
     * IAE should be thrown because the parameter contains null.
     */
    public void testContainsAll_InvalidObj() {
        try {
            Collection coll = new ArrayList();
            coll.add(new Integer(10));
            coll.add(null);

            instance.addAll(coll);
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException npe) {
            //success
        }
    }

    /**
     * Failure test for unite.
     * NPE should be thrown because the parameter is null.
     */
    public void testUnite_NullObj() {
        try {
            instance.unite(null);
            fail("NPE should be thrown.");
        } catch (NullPointerException npe) {
            //success
        }
    }

    /**
     * Failure test for unite.
     * IncompatibleBloomFiltersException should be thrown because the parameter is not compatible.
     */
    public void testUnite_InvalidObj1() {
        try {
            instances[0].unite(instances[1]);
            fail("IncompatibleBloomFiltersException should be thrown.");
        } catch (IncompatibleBloomFiltersException iae) {
            //success
        }
    }

    /**
     * Failure test for unite.
     * IncompatibleBloomFiltersException should be thrown because the parameter is not compatible.
     */
    public void testUnite_InvalidObj2() {
        try {
            instances[0].unite(instances[2]);
            fail("IncompatibleBloomFiltersException should be thrown.");
        } catch (IncompatibleBloomFiltersException iae) {
            //success
        }
    }

    /**
     * Failure test for unite.
     * IncompatibleBloomFiltersException should be thrown because the parameter is not compatible.
     */
    public void testUnite_InvalidObj3() {
        try {
            instances[0].unite(instances[3]);
            fail("IncompatibleBloomFiltersException should be thrown.");
        } catch (IncompatibleBloomFiltersException iae) {
            //success
        }
    }

    /**
     * Failure test for intersect.
     * NPE should be thrown because the parameter is null.
     */
    public void testIntersect_NullObj() {
        try {
            instance.intersect(null);
            fail("NPE should be thrown.");
        } catch (NullPointerException npe) {
            //success
        }
    }

    /**
     * Failure test for intersect.
     * IncompatibleBloomFiltersException should be thrown because the parameter is not compatible.
     */
    public void testIntersect_InvalidObj1() {
        try {
            instances[0].intersect(instances[1]);
            fail("IncompatibleBloomFiltersException should be thrown.");
        } catch (IncompatibleBloomFiltersException iae) {
            //success
        }
    }

    /**
     * Failure test for intersect.
     * IncompatibleBloomFiltersException should be thrown because the parameter is not compatible.
     */
    public void testIntersect_InvalidObj2() {
        try {
            instances[0].intersect(instances[2]);
            fail("IncompatibleBloomFiltersException should be thrown.");
        } catch (IncompatibleBloomFiltersException iae) {
            //success
        }
    }

    /**
     * Failure test for intersect.
     * IncompatibleBloomFiltersException should be thrown because the parameter is not compatible.
     */
    public void testIntersect_InvalidObj3() {
        try {
            instances[0].intersect(instances[3]);
            fail("IncompatibleBloomFiltersException should be thrown.");
        } catch (IncompatibleBloomFiltersException iae) {
            //success
        }
    }
}
