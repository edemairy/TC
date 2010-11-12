/**
 * 
 */

/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.SubmissionPhaseHandler;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.resource.Resource;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;

import java.sql.Connection;


/**
 * Accuracy tests for V1.2 <code>SubmissionPhaseHandler</code>.
 *
 * @author assistant
 * @version 1.2
 */
public class SubmissionPhaseHandlerTestV12 extends BaseTestCase {
    /** Instance to test. */
    private SubmissionPhaseHandler instance;

    /**
     * Sets up the environment.
     *
     * @throws java.lang.Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        instance = new SubmissionPhaseHandler();
    }

    /**
     * Cleans up the environment.
     *
     * @throws java.lang.Exception to JUnit
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link com.cronos.onlinereview.phases.SubmissionPhaseHandler
     * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
     *
     * @throws Exception to JUnit
     */
    public void testPerform_1() throws Exception {
        Project project = setupProjectResourcesNotification("Submission", true);
        Phase submission = project.getAllPhases()[1];

        Connection conn = getConnection();

        // create a registrant
        Resource resource = createResource(4, 101L, 1, 1);
        insertResources(conn, new Resource[] { resource });
        insertResourceInfo(conn, resource.getId(), 1, "4");
        insertResourceInfo(conn, resource.getId(), 2, "ACRush");
        insertResourceInfo(conn, resource.getId(), 4, "3808");
        insertResourceInfo(conn, resource.getId(), 5, "100");

        // open submission
        submission.setPhaseStatus(PhaseStatus.OPEN);

        Upload upload = super.createUpload(1, project.getId(), resource.getId(), 1, 1, "Paramter");
        super.insertUploads(conn, new Upload[] { upload });

        Submission sub = super.createSubmission(1, upload.getId(), 1);
        super.insertSubmissions(conn, new Submission[] { sub });

        instance.perform(submission, "1001");

        // the subject should be Phase End: Online Review Phases
        // there should be one submission information in the email
        // manager/observer/reviewer should receive the email
        closeConnection();
    }

    /**
     * Test method for {@link com.cronos.onlinereview.phases.SubmissionPhaseHandler
     * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
     *
     * @throws Exception to JUnit
     */
    public void testPerform_2() throws Exception {
        Project project = setupProjectResourcesNotification("Submission", true);
        project.getAllPhases()[1].setPhaseStatus(PhaseStatus.OPEN);

        instance.perform(project.getAllPhases()[1], "1001");

        // the subject should be Phase End: Online Review Phases
        // no submission
        // manager/observer should receive this email
    }

    /**
     * Test method for {@link com.cronos.onlinereview.phases.SubmissionPhaseHandler
     * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
     *
     * @throws Exception to JUnit
     */
    public void testPerform_3() throws Exception {
        Project project = setupProjectResourcesNotification("Submission", true);

        // test phase start
        project.getAllPhases()[1].setPhaseStatus(PhaseStatus.SCHEDULED);

        instance.perform(project.getAllPhases()[1], "1001");

        // the subject should be Phase Start: Online Review Phases
        // manager/observer should receive this email
    }
}
