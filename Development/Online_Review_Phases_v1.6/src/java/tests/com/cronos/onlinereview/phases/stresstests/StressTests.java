/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all stress test cases.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class StressTests extends TestCase {

    /**
     * Aggregates all the stress tests.
     *
     * @return the test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(SpecificationReviewPhaseHandlerStressTest.class);
        suite.addTestSuite(SpecificationSubmissionPhaseHandlerStressTest.class);

        suite.addTestSuite(AggregationPhaseHandlerTest.class);
        suite.addTestSuite(AggregationReviewPhaseHandlerTest.class);
        suite.addTestSuite(AppealsPhaseHandlerTest.class);
        suite.addTestSuite(AppealsResponsePhaseHandlerTest.class);
        suite.addTestSuite(ApprovalPhaseHandlerTest.class);
        suite.addTestSuite(FinalFixPhaseHandlerTest.class);
        suite.addTestSuite(FinalReviewPhaseHandlerTest.class);
        suite.addTestSuite(PostMortemPhaseHandlerTest.class);
        suite.addTestSuite(RegistrationPhaseHandlerTest.class);
        suite.addTestSuite(ReviewPhaseHandlerTest.class);
        suite.addTestSuite(ScreeningPhaseHandlerTest.class);
        suite.addTestSuite(SubmissionPhaseHandlerTest.class);
        suite.addTestSuite(ShowStressResult.class);
        return suite;
    }
}
