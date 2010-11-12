/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.SubmissionPhaseHandler;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;

import java.sql.Connection;

import java.util.Date;


/**
 * Accuracy tests for change functions in version 1.1 of SubmissionPhaseHandler class.
 *
 * @author myxgyy
 * @version 1.0
 */
public class SubmissionPhaseHandlerAccTestsV11 extends BaseTestCase {
    /** Target instance. */
    private SubmissionPhaseHandler handler;

    /**
     * Sets up the environment required for test cases for this class.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        handler = new SubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);
    }

    /**
     * Cleans up the environment required for test cases for this class.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the canPerform with scheduled statuses.
     *
     * @throws Exception not under test.
     */
    public void testCanPerform1() throws Exception {
        Project project = super.setupPhases();
        Phase[] phases = project.getAllPhases();
        Phase submission = phases[1];

        // test with scheduled status.
        submission.setPhaseStatus(PhaseStatus.SCHEDULED);

        // time has not passed, nor dependencies met
        assertFalse("canPerform should have returned false", handler.canPerform(submission));

        // time has passed, but dependency not met.
        submission.setActualStartDate(new Date());
        assertFalse("canPerform should have returned false", handler.canPerform(submission));

        // time has passed and dependency met.
        submission.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
        assertTrue("canPerform should have returned true", handler.canPerform(submission));
    }

    /**
     * Tests the canPerform with open statuses.
     *
     * @throws Exception not under test.
     */
    public void testCanPerform2() throws Exception {
        Project project = super.setupPhases();
        Phase[] phases = project.getAllPhases();
        Phase submissionPhase = phases[1];

        // test with open status.
        submissionPhase.setPhaseStatus(PhaseStatus.OPEN);

        // change dependency type to F2F
        submissionPhase.getAllDependencies()[0].setDependentStart(false);

        // time has not passed, nor dependencies met
        assertFalse("canPerform should have returned false", handler.canPerform(submissionPhase));

        // time has passed, but dependency not met.
        submissionPhase.setActualStartDate(new Date(System.currentTimeMillis() - 1000));
        submissionPhase.setActualEndDate(new Date());
        assertFalse("canPerform should have returned false", handler.canPerform(submissionPhase));

        // time has passed and dependency met, reviews passed.
        submissionPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
        submissionPhase.setAttribute("Manual Screening", "Yes");
        submissionPhase.setAttribute("Submission Number", "1");

        Connection conn = getConnection();

        Phase screeningPhase = phases[2];
        long screeningPhaseId = screeningPhase.getId();
        Resource submitter = super.createResource(1, submissionPhase.getId(), 1, 1);
        Resource reviewer1 = super.createResource(2, screeningPhaseId, 1, 2);
        Resource reviewer2 = super.createResource(3, screeningPhaseId, 1, 3);

        Upload upload = createUpload(1, 1, submitter.getId(), 1, 1, "parameter");
        Submission submission = createSubmission(1, 1, 1);
        Scorecard scorecard = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);
        Review review = createReview(1, reviewer1.getId(), submission.getId(), scorecard.getId(), true, 80.0f);

        insertResources(conn, new Resource[] { submitter, reviewer1, reviewer2 });

        super.insertResourceInfo(conn, submitter.getId(), 1, "10001");
        super.insertResourceInfo(conn, reviewer1.getId(), 1, "10002");
        super.insertResourceInfo(conn, reviewer2.getId(), 1, "10003");

        insertUploads(conn, new Upload[] { upload });
        insertSubmissions(conn, new Submission[] { submission });
        insertScorecards(conn, new Scorecard[] { scorecard });
        insertReviews(conn, new Review[] { review });
        assertTrue("canPerform should have returned true", handler.canPerform(submissionPhase));
    }

    /**
     * Tests the perform with Scheduled and Open statuses and checks whether a post-mortem phase is inserted
     * when there is no submission.
     *
     * @throws Exception to JUnit.
     */
    public void testPerform1() throws Exception {
        // test with scheduled status.
        Phase submissionPhase = createPhase(1, 1, "Scheduled", 2, "Submission");
        String operator = "1000001";
        Connection conn = getConnection();
        insertProject(conn);
        insertProjectInfo(getConnection(), 1, new long[] {44}, new String[] {"true"});

        // test with scheduled status
        handler.perform(submissionPhase, operator);
        assertFalse("Post-mortem phase should not be inserted", havePostMortemPhase(conn));

        // test with open status
        submissionPhase.setPhaseStatus(PhaseStatus.OPEN);
        handler.perform(submissionPhase, operator);

        assertTrue("Post-mortem phase should be inserted", havePostMortemPhase(conn));
    }

    /**
     * Tests the perform with Open statuses and a post-mortem phase should NOT be inserted when there are
     * submissions.
     *
     * @throws Exception to JUnit.
     */
    public void testPerform2() throws Exception {
        // test with scheduled status.
        Project project = super.setupPhases();
        Phase[] phases = project.getAllPhases();
        Phase submissionPhase = phases[1];

        // test with scheduled status.
        String operator = "1000001";
        Connection conn = getConnection();

        // test with open status
        submissionPhase.setPhaseStatus(PhaseStatus.OPEN);

        // create a submission
        Resource resource = super.createResource(1, 102, 1, 1);
        super.insertResources(conn, new Resource[] { resource });
        super.insertResourceInfo(conn, resource.getId(), 1, "10001");

        Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
        super.insertUploads(conn, new Upload[] { upload });

        Submission submission = super.createSubmission(1, upload.getId(), 1);
        super.insertSubmissions(conn, new Submission[] { submission });

        handler.perform(submissionPhase, operator);

        // Post-Mortem phase should NOT be inserted
        // when there is submission
        assertFalse("Post-mortem phase should NOT be inserted", havePostMortemPhase(conn));
    }
}
