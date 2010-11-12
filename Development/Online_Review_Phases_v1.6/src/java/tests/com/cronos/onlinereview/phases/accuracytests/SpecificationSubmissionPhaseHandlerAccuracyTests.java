/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import java.util.Date;

import com.cronos.onlinereview.phases.SpecificationSubmissionPhaseHandler;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;

/**
 * <p>
 * Accuracy tests for the {@link SpecificationSubmissionPhaseHandler} class.
 * </p>
 *
 * @author akinwale
 * @version 1.4
 */
public class SpecificationSubmissionPhaseHandlerAccuracyTests extends BaseTestCase {
    /**
     * <p>
     * The {@link SpecificationSubmissionPhaseHandler} instance to be tested.
     * </p>
     */
    private SpecificationSubmissionPhaseHandler handler;

    /**
     * <p>
     * Setup for each test in the test case.
     * </p>
     */
    public void setUp() throws Exception {
        super.setUp();
        handler = new SpecificationSubmissionPhaseHandler();
    }

    /**
     * <p>
     * Tests that the canPerform(Phase) method works properly.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCanPerform_Scheduled() throws Exception {
        String error = "canPerform does not work properly";
        Project project = super.setupPhasesWithSpecificationPhases(true);
        Phase[] phases = project.getAllPhases();
        Phase phase = phases[1];

        phase.setPhaseStatus(PhaseStatus.SCHEDULED);

        // The phase time has elapsed, but a phase dependency has not been met
        phase.setActualStartDate(new Date(new Date().getTime() - 28 * 60 * 60 * 1000));
        assertFalse(error, handler.canPerform(phase));

        // The phase time has elapsed with the dependency met
        phase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
        assertTrue(error, handler.canPerform(phase));
    }

    /**
     * <p>
     * Tests that the canPerform(Phase) method works properly.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCanPerform_Open_DependencyNotMet() throws Exception {
        String error = "canPerform does not work properly";
        Project project = super.setupPhasesWithSpecificationPhases(true);
        Phase[] phases = project.getAllPhases();
        Phase phase = phases[1];

        // Test with a phase status of OPEN
        phase.setPhaseStatus(PhaseStatus.OPEN);

        // Change the dependency type to F2F
        phase.getAllDependencies()[0].setDependentStart(false);

        // The phase time has elapsed but the dependency has not been met
        phase.setActualStartDate(new Date(System.currentTimeMillis() - 1000));
        phase.setActualEndDate(new Date());
        assertFalse(error, handler.canPerform(phase));
    }

    /**
     * <p>
     * Tests that the canPerform(Phase) method works properly with no parent projects completed.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     * @throws Exception
     */
    public void testCanPerform_Scheduled_NoParentProjectsCompleted() throws Exception {
        Project project = super.setupPhasesWithSpecificationPhases(true);
        Phase[] phases = project.getAllPhases();

        // Get the submission phase and check the SCHEDULED phase status
        Phase phase = phases[1];
        phase.setPhaseStatus(PhaseStatus.SCHEDULED);

        assertFalse("canPerform does not work properly", handler.canPerform(phase));
    }

    /**
     * <p>
     * Tests that the canPerform(Phase) method works properly with no specification submission.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     * @throws Exception
     */
    public void testCanPerform_Open_NoSubmission() throws Exception {
        Project project = super.setupPhasesWithSpecificationPhases(true);
        Phase[] phases = project.getAllPhases();

        // Get the submission phase and check the OPEN phase status
        Phase phase = phases[1];
        phase.setPhaseStatus(PhaseStatus.OPEN);
        assertFalse("canPerform does not work properly", handler.canPerform(phase));
    }

    /**
     * <p>
     * Tests that the perform(Phase, String) method works properly.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testPerform() throws Exception {
        Project project = setupPhasesWithSpecificationPhases(true);
        Phase phase = project.getAllPhases()[1];
        handler.perform(phase, "tc1");
        // An email should be sent

        phase.setPhaseStatus(PhaseStatus.OPEN);
        handler.perform(phase, "tc2");
        // An email should be sent

        // Total expected number of emails at this point: 2
    }
}