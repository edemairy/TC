/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author cucu
 * @version 1.0
 */
public class UnitTests extends TestCase {

    /**
     * Suite to run the unit tests.
     *
     * @return suite with tests
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(BloomFilterTests.class);
        suite.addTestSuite(DefaultHashFunctionFamilyTests.class);
        suite.addTestSuite(BloomFilterExceptionTests.class);
        suite.addTestSuite(IncompatibleBloomFilterExceptionTests.class);
        suite.addTestSuite(BloomFilterSerializeExceptionTests.class);
        suite.addTestSuite(Demo.class);

        return suite;
    }

}
