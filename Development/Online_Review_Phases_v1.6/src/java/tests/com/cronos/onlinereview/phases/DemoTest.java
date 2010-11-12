/*
 * Copyright (C) 2006 - 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.phase.DefaultPhaseManager;
import com.topcoder.management.phase.PhaseHandler;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.PhaseOperationEnum;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;

/**
 * Shows a demo of how to use this component.
 * <p>
 * For version 1.2, the email templates and email options for different role has been
 * enhanced.
 * </p>
 * <p>
 * For version 1.4, add demo tests to show the newly added two handlers.
 * </p>
 *
 * @author bose_java, waits, myxgyy
 * @version 1.4
 */
public class DemoTest extends BaseTest {
    /**
     * sets up the environment required for test cases for this class.
     *
     * @throws Exception
     *             not under test.
     */
    protected void setUp() throws Exception {
        super.setUp();

        ConfigManager configManager = ConfigManager.getInstance();

        configManager.add(PHASE_HANDLER_CONFIG_FILE);
        configManager.add(DOC_GENERATOR_CONFIG_FILE);
        configManager.add(EMAIL_CONFIG_FILE);
        configManager.add(MANAGER_HELPER_CONFIG_FILE);

        // add the component configurations as well
        for (int i = 0; i < COMPONENT_FILE_NAMES.length; i++) {
            configManager.add(COMPONENT_FILE_NAMES[i]);
        }
    }

    /**
     * cleans up the environment required for test cases for this class.
     *
     * @throws Exception
     *             not under test.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * This method shows a demo of how to use the RegistrationPhaseHandler with Phase
     * Management component. Use of other Phase Handlers is similar to the Registration
     * Phase Handler as the same set of APIs are to be used in those cases as well.
     *
     * @throws Exception
     *             not under test.
     */
    public void testDemo() throws Exception {
        // init the phase management component.
        PhaseManager phaseManager = new DefaultPhaseManager(PHASE_MANAGER_NAMESPACE);

        // init the phase handler class.
        PhaseHandler phaseHandler = new ScreeningPhaseHandler(ScreeningPhaseHandler.DEFAULT_NAMESPACE);

        // register a phase handler for dealing with canStart() and canEnd()
        PhaseType phaseType = new PhaseType(3, "Screening");
        phaseManager.registerHandler(phaseHandler, phaseType, PhaseOperationEnum.START);
        phaseManager.registerHandler(phaseHandler, phaseType, PhaseOperationEnum.END);

        // get the phase instance.
        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("Screening");
            Phase[] phases = project.getAllPhases();
            Phase screeningPhase = phases[2];

            Connection conn = getConnection();

            // since it is in screening, we need setup some data for screening
            // create a registration and add its submission
            Resource resource = createResource(4, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource});
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "prunthaban");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            // create submission/upload
            Upload upload = super.createUpload(132, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[] {upload});

            Submission submission1 = super.createSubmission(112, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission1});

            // another register
            resource = createResource(5, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource});
            insertResourceInfo(conn, resource.getId(), 1, "5");
            insertResourceInfo(conn, resource.getId(), 2, "fastprogrammer");
            insertResourceInfo(conn, resource.getId(), 4, "3338");
            insertResourceInfo(conn, resource.getId(), 5, "90");
            upload = super.createUpload(232, project.getId(), resource.getId(), 1, 1, "Paramter");
            super.insertUploads(conn, new Upload[] {upload});

            Submission submission2 = super.createSubmission(233, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission2});

            // insert screener for screening
            Resource screener = createResource(633, screeningPhase.getId(), 1, 2);
            super.insertResources(conn, new Resource[] {screener});
            insertResourceInfo(conn, screener.getId(), 1, "2");

            // close the submission phase first
            phases[1].setPhaseStatus(PhaseStatus.CLOSED);
            // canStart method will call canPerform() and perform() methods of
            // the phaseHandler.
            if (phaseManager.canStart(screeningPhase)) {
                phaseManager.start(screeningPhase, "1001");
            }

            // to stop
            Scorecard scorecard1 = createScorecard(1, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);

            // insert a screening review result for submission one
            Review screenReview = createReview(11, screener.getId(), submission1.getId(), scorecard1
                .getId(), true, 90.0f);
            Scorecard scorecard2 = createScorecard(2, 1, 1, 1, "name", "1.0", 75.0f, 100.0f);
            // insert a screening review result for submission two
            Review screenReview2 = createReview(13, screener.getId(), submission2.getId(), scorecard2
                .getId(), true, 70.0f);
            this.insertScorecards(conn, new Scorecard[] {scorecard1, scorecard2});
            this.insertReviews(conn, new Review[] {screenReview, screenReview2});

            // canEnd method will call canPerform() and perform() methods of the
            // phaseHandler.
            screeningPhase.setPhaseStatus(PhaseStatus.OPEN);

            if (phaseManager.canEnd(screeningPhase)) {
                phaseManager.end(screeningPhase, "1001");
            }
            // check the email manually
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Demo to show the usage of <code>SpecificationSubmissionPhaseHandler</code> class.
     *
     * @throws Exception
     *             to Junit.
     * @since 1.4
     */
    public void testDemoV14_SpecificationSubmission() throws Exception {
        SpecificationSubmissionPhaseHandler handler = new SpecificationSubmissionPhaseHandler(
            PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("all", false, true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[0];

            // set with scheduled status.
            phase.setPhaseStatus(PhaseStatus.SCHEDULED);

            Connection conn = getConnection();
            // phase can be start now
            if (handler.canPerform(phase)) {
                // start the phase
                handler.perform(phase, "operator");
            }
            // we will stop the phase now
            // set with open status.
            phase.setPhaseStatus(PhaseStatus.OPEN);
            Resource reviewer = createResource(1, 102L, 1, 15);
            insertResources(conn, new Resource[] {reviewer});
            insertResourceInfo(conn, reviewer.getId(), 1, "3");

            // create a registration
            Resource resource = createResource(4, 101L, 1, 17);
            insertResources(conn, new Resource[] {resource});
            insertResourceInfo(conn, resource.getId(), 1, "4");

            // insert upload/submission
            Upload upload = createUpload(1, project.getId(), 1, 1, 1, "parameter");
            insertUploads(conn, new Upload[] {upload});
            Submission submission = createSubmission(1, upload.getId(), 1, 2);
            insertSubmissions(conn, new Submission[] {submission});
            // we can stop the phase
            if (handler.canPerform(phase)) {
                // stop it now
                handler.perform(phase, "operator");
            }
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Demo to show the usage of <code>SpecificationReviewPhaseHandler</code> class.
     *
     * @throws Exception
     *             to Junit.
     * @since 1.4
     */
    public void testDemoV14_SpecificationReview() throws Exception {
        SpecificationReviewPhaseHandler handler = new SpecificationReviewPhaseHandler(
            PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("all", false, true);
            Phase[] phases = project.getAllPhases();
            phases[0].setPhaseStatus(PhaseStatus.CLOSED);

            Phase phase = phases[1];
            phase.setPhaseStatus(PhaseStatus.SCHEDULED);

            Connection conn = getConnection();

            // insert a submission with specification submission type
            Resource resource = createResource(4, 101L, 1, 17);
            insertResources(conn, new Resource[] {resource});
            Upload upload = createUpload(1, project.getId(), 4, 1, 1, "parameter");
            insertUploads(conn, new Upload[] {upload});

            Submission submission = createSubmission(1, upload.getId(), 1, 2);
            insertSubmissions(conn, new Submission[] {submission});
            // it now can perform start
            if (handler.canPerform(phase)) {
                // start
                handler.perform(phase, "1001");
            }
            // we will close the phase now
            // create a reviewer
            Resource reviewer = createResource(5, 102L, 1, 18);
            insertResources(conn, new Resource[] {reviewer});
            insertResourceInfo(conn, reviewer.getId(), 1, "3");
            Scorecard scorecard = createScorecard(1, 1, 2, 6, "name", "1.0", 75.0f, 100.0f);
            Review review = createReview(11, 5, 1, 1, true, 90.0f);
            //  add a rejected comment
            review.addComment(createComment(1111, reviewer.getId(), "Approved", 14,
                "Specification Review Comment"));
            insertScorecards(conn, new Scorecard[] {scorecard});
            insertReviews(conn, new Review[] {review});
            insertCommentsWithExtraInfo(conn, new long[] {1}, new long[] {reviewer.getId()},
                new long[] {review.getId()}, new String[] {"Approved Comment"}, new long[] {14},
                new String[] {"Approved"});

            // change the phase status to open
            phase.setPhaseStatus(PhaseStatus.OPEN);
            // we can stop the phase now
            if (handler.canPerform(phase)) {
                // stop the phase
                handler.perform(phase, "1001");
            }
        } finally {
            cleanTables();
            closeConnection();
        }
    }
}
