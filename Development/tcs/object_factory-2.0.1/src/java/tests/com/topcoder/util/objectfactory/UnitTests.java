/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactoryUnitTest;
import com.topcoder.util.objectfactory.impl.IllegalReferenceExceptionUnitTest;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationExceptionUnitTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>This test case aggregates all unit test cases.</p>
 *
 * @author mgmg
 * @version 2.0
 */
public class UnitTests extends TestCase {
    /**
     * Aggregate all unit tests.
     *
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(ObjectFactoryExceptionUnitTest.class);
        suite.addTestSuite(ObjectCreationExceptionUnitTest.class);
        suite.addTestSuite(SpecificationFactoryExceptionUnitTest.class);
        suite.addTestSuite(UnknownReferenceExceptionUnitTest.class);
        suite.addTestSuite(InvalidClassSpecificationExceptionUnitTest.class);
        suite.addTestSuite(SpecificationConfigurationExceptionUnitTest.class);
        suite.addTestSuite(IllegalReferenceExceptionUnitTest.class);

        suite.addTestSuite(ObjectSpecificationUnitTest.class);
        suite.addTestSuite(ConfigManagerSpecificationFactoryUnitTest.class);
        suite.addTestSuite(ObjectFactoryUnitTest.class);

        suite.addTestSuite(Demo.class);

        return suite;
    }
}
