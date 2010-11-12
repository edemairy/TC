/*
 * Copyright (C) 2009 - 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.cronos.onlinereview.autoscreening.management.ScreeningTask;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.review.data.Review;

import com.topcoder.project.phases.Phase;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a
 * phase can be executed and to add extra logic to execute a phase. It will be
 * used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection, email
 * sending and the required number of submissions that pass screening. This
 * class handle the submission phase. If the input is of other phase types,
 * PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * The submission phase can start as soon as the dependencies are met and start
 * time is reached, and can stop when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>The phase's end time is reached.</li>
 * <li>If manual screening is absent, the number of submissions that have passed
 * auto-screening meets the required number;</li>
 * <li>If manual screening is required, the number of submissions that have
 * passed manual screening meets the required number.</li>
 * </ul>
 * </p>
 * <p>
 * Note that screening phase can be started during a submission phase.
 * </p>
 * <p>
 * There is no additional logic for executing this phase.
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 *
 * <p>
 * Version 1.1 changes note:
 * <li>Insert a post-mortem phase if there is no submission in
 * <code>perform</code>.</li>
 * </p>
 *
 * <p>
 * Version 1.2 changes note:
 * <ul>
 * <li>
 * Added capability to support different email template for different role (e.g. Submitter, Reviewer, Manager, etc).
 * </li>
 * <li>
 * Support for more information in the email generated: for stop, the number of submission and info about Submitter.
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * Version 1.3 (Online Review End Of Project Analysis Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #perform(Phase, String)} method to use updated
 *     PhasesHelper#insertPostMortemPhase(Project, Phase, ManagerHelper, String) method for creating
 *     <code>Post-Mortem</code> phase.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.4 Change notes:
 *   <ol>
 *     <li>Updated {@link #getAutoScreeningPasses(Phase)} method to create connection before calling
 *     PhasesHelper#getScreeningTasks(ManagerHelper, Connection, Phase) method.</li>
 *   </ol>
 * </p>
 *
 * @author tuenm, bose_java, argolite, bramandia, saarixx, myxgyy
 * @version 1.4
 * @since 1.0
 */
public class SubmissionPhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.SubmissionPhaseHandler";

    /** constant for appeals phase type. */
    private static final String PHASE_TYPE_SUBMISSION = "Submission";

    /**
     * Create a new instance of SubmissionPhaseHandler using the default
     * namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading
     *         configuration settings.
     */
    public SubmissionPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of SubmissionPhaseHandler using the given namespace
     * for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading
     *         configuration settings or required properties missing.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public SubmissionPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Check if the input phase can be executed or not. This method will check
     * the phase status to see what will be executed. This method will be called
     * by canStart() and canEnd() methods of PhaseManager implementations in
     * Phase Management component.
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase
     * can be started using the following conditions: the dependencies are met
     * and Its start time is reached.
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be
     * stopped using the following conditions:
     * <ul>
     * <li>The dependencies are met</li>
     * <li>The phase's end time is reached.</li>
     * <li>If manual screening is absent, the number of submissions that have
     * passed auto-screening meets the required number;</li>
     * <li>If manual screening is required, the number of submissions that have
     * passed manual screening meets the required number.</li>
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be
     * thrown.
     * </p>
     *
     * @param phase The input phase to check.
     *
     * @return True if the input phase can be executed, false otherwise.
     *
     * @throws PhaseNotSupportedException if the input phase type is not
     *         &quot;Submission&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while
     *         processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public boolean canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, PHASE_TYPE_SUBMISSION);

        // will throw exception if phase status is neither "Scheduled" nor
        // "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        if (toStart) {
            return PhasesHelper.canPhaseStart(phase);
        } else {
            boolean dependencyMet = PhasesHelper.arePhaseDependenciesMet(phase,
                            false);
            boolean reachEndTime = PhasesHelper.reachedPhaseEndTime(phase);

            // version 1.1 : can stop if there is no submission
            return (dependencyMet && reachEndTime
                    && (!hasAnySubmission(phase, null) || arePassedSubmissionsEnough(phase)));
        }
    }

    /**
     * Provides additional logic to execute a phase. This method will be called
     * by start() and end() methods of PhaseManager implementations in Phase
     * Management component. This method can send email to a group os users
     * associated with timeline notification for the project. The email can be
     * send on start phase or end phase base on configuration settings.
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be
     * thrown.
     * </p>
     *
     * <p>
     * Version 1.1. changes note: insert a post-mortem phase if there is no
     * submission.
     * </p>
     *
     * <p>
     * Update in version 1.2: Support for more information in the email generated:
     * for stop, the number of submission and info about Submitter.
     * </p>
     *
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     *
     * @throws PhaseNotSupportedException if the input phase type is not
     *         "Submission" type.
     * @throws PhaseHandlingException if there is any error occurred while
     *         processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty
     *         string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, PHASE_TYPE_SUBMISSION);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        Map<String, Object> values = new HashMap<String, Object>();
        // Check whether we get submissions, if not, insert post-mortem phase
        if (!toStart && !hasAnySubmission(phase, values)) {
            PhasesHelper.insertPostMortemPhase(phase.getProject(), phase, getManagerHelper(), operator);
        }

        sendEmail(phase, values);
    }

    /**
     * This method checks whether there is any submission in submission phase.
     * <p>
     * Update in version 1.2: Support for more information in the email generated:
     * for stop, the number of submission and info about Submitter.
     * </p>
     * @param phase the phase to check.
     * @param values the values map to hold the information for email generation
     * @return true if there is at least one submission, false otherwise.
     * @throws PhaseHandlingException if any error occurs during processing.
     */
    private boolean hasAnySubmission(Phase phase, Map<String, Object> values) throws PhaseHandlingException {
        Connection conn = null;
        Submission[] subs = null;
        try {
            conn = createConnection();

            subs = PhasesHelper.searchActiveSubmissions(
                            getManagerHelper().getUploadManager(), conn, phase
                                            .getProject().getId(), PhasesHelper.CONTEST_SUBMISSION_TYPE);
        } catch (SQLException e) {
            throw new PhaseHandlingException(
                            "Error retrieving submission status id", e);
        } finally {
            PhasesHelper.closeConnection(conn);
        }

        //for stop phase, we are going to support more information now from version 1.2
        if (values != null) {
            values.put("N_SUBMITTERS", subs.length);
            values.put("SUBMITTER", PhasesHelper.constructSubmitterValues(
                                    subs, getManagerHelper().getResourceManager(), false));
        }
        return subs.length > 0;
    }

    /**
     * This method returns true if the number of submissions that passed
     * screenings meets the required number.
     *
     * @param phase phase to check.
     *
     * @return true if the number of submissions that passed screenings meets
     *         the required number.
     * @throws PhaseHandlingException in case of any error.
     */
    private boolean arePassedSubmissionsEnough(Phase phase) throws PhaseHandlingException {
        boolean bManual = PhasesHelper.isScreeningManual(phase);

        if (phase.getAttribute("Submission Number") == null) {
            return true;
        }

        int submissionNum = PhasesHelper.getIntegerAttribute(phase,
                        "Submission Number");
        int passedNum = 0;

        if (bManual) {
            passedNum = getManualScreeningPasses(phase);
        } else {
            passedNum = getAutoScreeningPasses(phase);
        }
        return (passedNum >= submissionNum);
    }

    /**
     * Returns the number of submissions which passed manual screening.
     *
     * @param phase phase instance.
     *
     * @return the number of submissions which passed manual screening.
     *
     * @throws PhaseHandlingException in case of retrieval error.
     */
    private int getManualScreeningPasses(Phase phase) throws PhaseHandlingException {
        Connection conn = null;
        try {
            conn = createConnection();

            // get the next screening phase
            Phase screeningPhase = PhasesHelper.locatePhase(phase, "Screening",
                            true, false);
            if (screeningPhase == null) {
                return 0;
            }
            long screeningPhaseId = screeningPhase.getId();

            // get reviews for the phase
            Review[] screenReviews = PhasesHelper
                            .searchReviewsForResourceRoles(conn,
                                            getManagerHelper(),
                                            screeningPhaseId, new String[] {"Primary Screener", "Screener"}, null);
            if (screenReviews.length == 0) {
                return 0;
            }
            float minScore = PhasesHelper.getScorecardMinimumScore(
                            getManagerHelper().getScorecardManager(),
                            screenReviews[0]);
            int passedNum = 0;

            for (int i = 0; i < screenReviews.length; i++) {
                if (screenReviews[i].getScore().floatValue() >= minScore) {
                    passedNum++;
                }
            }

            return passedNum;
        } finally {
            PhasesHelper.closeConnection(conn);
        }
    }

    /**
     * Returns the number of submissions which passed automatic screening.
     * <p>
     * Change in version 1.4: create connection before calling
     * {@link PhasesHelper#getScreeningTasks(ManagerHelper, Connection, Phase)} method.
     * </p>
     *
     * @param phase phase instance.
     *
     * @return the number of submissions which passed automatic screening.
     *
     * @throws PhaseHandlingException in case of retrieval error.
     */
    private int getAutoScreeningPasses(Phase phase) throws PhaseHandlingException {
        Connection conn = null;
        try {
            conn = createConnection();

            ScreeningTask[] screeningTasks = PhasesHelper.getScreeningTasks(getManagerHelper(), conn, phase);
            int passedNum = 0;

            for (int i = 0; i < screeningTasks.length; i++) {
                String status = screeningTasks[i].getScreeningStatus().getName();

                if ("Passed".equals(status) || "Passed with Warning".equals(status)) {
                    passedNum++;
                }
            }

            return passedNum;
        } catch (SQLException e) {
            throw new PhaseHandlingException("Problem when getting screening tasks.", e);
        } finally {
            PhasesHelper.closeConnection(conn);
        }
    }
}