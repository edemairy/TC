/**
 *
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.topcoder.configuration;

import com.topcoder.configuration.defaults.DefaultConfigurationObjectTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(BaseConfigurationObjectTests.class);
        suite.addTestSuite(ConfigurationAccessExceptionTests.class);
        suite.addTestSuite(ConfigurationExceptionTests.class);
        suite.addTestSuite(InvalidConfigurationExceptionTests.class);
        suite.addTestSuite(ProcessExceptionTests.class);
        suite.addTestSuite(SynchronizedConfigurationObjectTests.class);
        suite.addTestSuite(TemplateConfigurationObjectTests.class);
        suite.addTestSuite(DefaultConfigurationObjectTests.class);
        suite.addTestSuite(Demo.class);
        suite.addTestSuite(HelperTests.class);
        return suite;
    }

}
