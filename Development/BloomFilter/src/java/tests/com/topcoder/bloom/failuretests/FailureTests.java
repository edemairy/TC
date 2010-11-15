/**
 *
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.bloom.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>This test case aggregates all Failure test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class FailureTests extends TestCase {
    /**
     * Aggregate all failure tests
     *
     * @return
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(BloomFilterFailureTest.suite());
        suite.addTest(DefaultHashFunctionFamilyFailureTest.suite());

        return suite;
    }
}
