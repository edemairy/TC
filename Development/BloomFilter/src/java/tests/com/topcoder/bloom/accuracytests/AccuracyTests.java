/**
 *
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.bloom.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Accuracy test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        //suite.addTest(XXX.suite());
        suite.addTestSuite(BloomFilterAccuracyTests.class);
        suite.addTestSuite(DefaultHashFunctionFamilyAccuracyTests.class);
        return suite;
    }

}
