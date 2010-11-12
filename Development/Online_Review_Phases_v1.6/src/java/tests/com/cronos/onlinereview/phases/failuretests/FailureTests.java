/**
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.cronos.onlinereview.phases.failuretests;

import com.cronos.onlinereview.phases.failuretests.lookup.PhaseStatusLookupUtilityFailureTest;
import com.cronos.onlinereview.phases.failuretests.lookup.PhaseTypeLookupUtilityFailureTest;
import com.cronos.onlinereview.phases.failuretests.lookup.ProjectInfoTypeLookupUtilityFailureTest;
import com.cronos.onlinereview.phases.failuretests.lookup.ResourceRoleLookupUtilityFailureTest;
import com.cronos.onlinereview.phases.failuretests.lookup.SubmissionStatusLookupUtilityFailureTest;
import com.cronos.onlinereview.phases.failuretests.lookup.SubmissionTypeLookupUtilityFailureTest;
import com.cronos.onlinereview.phases.failuretests.lookup.UploadStatusLookupUtilityFailureTest;
import com.cronos.onlinereview.phases.failuretests.lookup.UploadTypeLookupUtilityFailureTest;
import com.cronos.onlinereview.phases.failuretests.lookup.NotificationTypeLookupUtilityFailureTest;
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

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // com.cronos.onlinereview.phases.failuretests
        suite.addTest(AbstractPhaseHandlerFailureTest.suite());

        suite.addTest(AggregationPhaseHandlerFailureTest.suite());
        suite.addTest(AggregationReviewPhaseHandlerFailureTest.suite());
        suite.addTest(AppealsPhaseHandlerFailureTest.suite());

        suite.addTest(AppealsResponsePhaseHandlerFailureTest.suite());

        suite.addTest(ApprovalPhaseHandlerFailureTest.suite());
        suite.addTest(FinalFixPhaseHandlerFailureTest.suite());
        suite.addTest(FinalReviewPhaseHandlerFailureTest.suite());

        suite.addTest(ManagerHelperFailureTest.suite());

        suite.addTest(RegistrationPhaseHandlerFailureTest.suite());
        suite.addTest(ReviewPhaseHandlerFailureTest.suite());
        suite.addTest(ScreeningPhaseHandlerFailureTest.suite());
        suite.addTest(SubmissionPhaseHandlerFailureTest.suite());

        // com.cronos.onlinereview.phases.failuretests.lookup
        suite.addTest(NotificationTypeLookupUtilityFailureTest.suite());
        suite.addTest(PhaseStatusLookupUtilityFailureTest.suite());
        suite.addTest(PhaseTypeLookupUtilityFailureTest.suite());
        suite.addTest(ProjectInfoTypeLookupUtilityFailureTest.suite());
        suite.addTest(ResourceRoleLookupUtilityFailureTest.suite());
        suite.addTest(SubmissionStatusLookupUtilityFailureTest.suite());
        suite.addTest(UploadStatusLookupUtilityFailureTest.suite());
        suite.addTest(UploadTypeLookupUtilityFailureTest.suite());
        suite.addTest(SubmissionTypeLookupUtilityFailureTest.suite());

        // add new failure test class since 1.1
        suite.addTestSuite(ApprovalPhaseHandlerFailureTest2.class);
        suite.addTestSuite(PostMortemPhaseHandlerFailureTest.class);

        // add new failure test class since 1.4
        suite.addTest(SpecificationSubmissionPhaseHandlerFailureTest.suite());
        suite.addTest(SpecificationReviewPhaseHandlerFailureTest.suite());

        return suite;
    }

}
