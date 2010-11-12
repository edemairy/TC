/*
 * Copyright (C) 2009-2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ValidationException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.scoreaggregator.AggregatedSubmission;
import com.topcoder.management.review.scoreaggregator.InconsistentDataException;
import com.topcoder.management.review.scoreaggregator.RankedSubmission;
import com.topcoder.management.review.scoreaggregator.ReviewScoreAggregator;

import com.topcoder.project.phases.Phase;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a
 * phase can be executed and to add extra logic to execute a phase. It will be
 * used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection and email
 * sending. This class handle the appeals response phase. If the input is of
 * other phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * The appeals response phase can start as soon as the dependencies are met and
 * can stop when the following conditions met: The dependencies are met and all
 * appeals are resolved.
 * </p>
 * <p>
 * The additional logic for executing this phase is:<br>
 * When Appeals Response is stopping:
 * <ul>
 * <li>All submissions with failed review scores will be set to the status
 * &quot;Failed Review&quot;.</li>
 * <li>Overall score for the passing submissions will be calculated and saved to
 * the submitters&#146; resource properties together with their placements. The
 * property names are &quot;Final Score&quot; and &quot;Placement&quot;.</li>
 * <li>The winner and runner-up will be populated in the project properties. The
 * property names are &quot;Winner External Reference ID&quot; and
 * &quot;Runner-up External Reference ID&quot;.</li>
 * <li>Submissions that do not win will be set to the status &quot;Completed
 * Without Win&quot;.</li>
 * </ul>
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 *
 * <p>
 * version 1.1 change notes: Modify the method <code>perform</code> to add a
 * post-mortem phase if there is no submission passes review after appeal
 * response.
 * </p>
 *
 * <p>
 * Version 1.2 changes note:
 * <ul>
 * <li>
 * Added capability to support different email template for different role (e.g. Submitter, Reviewer, Manager, etc).
 * </li>
 * <li>
 * Support for more information in the email generated:
 * for start/stop, puts the submissions info/scores into the values map for email generation.
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * Version 1.3 (Online Review End Of Project Analysis Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #perform(Phase, String)} method to use updated
 *     PhasesHelper#insertPostMortemPhase(com.topcoder.project.phases.Project, Phase, ManagerHelper, String)
 *     method for creating <code>Post-Mortem</code> phase.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.4 Change notes:
 *   <ol>
 *     <li>Updated {@link #perform(Phase, String)} method to calculate the number of aggregators for project and bind it
 *     to map used for filling email template.</li>
 *   </ol>
 * </p>
 *
 * @author tuenm, bose_java, argolite, waits, isv
 * @version 1.4
 */
public class AppealsResponsePhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.AppealsResponsePhaseHandler";

    /** constant for appeals response phase type. */
    private static final String PHASE_TYPE_APPEALS_RESPONSE = "Appeals Response";

    /**
     * Create a new instance of AppealsResponsePhaseHandler using the default
     * namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading
     *         configuration settings.
     */
    public AppealsResponsePhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of AppealsResponsePhaseHandler using the given
     * namespace for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading
     *         configuration settings.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public AppealsResponsePhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * <p>
     * Check if the input phase can be executed or not. This method will check
     * the phase status to see what will be executed. This method will be called
     * by canStart() and canEnd() methods of PhaseManager implementations in
     * Phase Management component.
     * </p>
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase
     * can be started using the following conditions: The dependencies are met
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be
     * stopped using the following conditions: The dependencies are met and All
     * appeals are resolved.
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be
     * thrown.
     * </p>
     *
     * @param phase The input phase to check.
     * @return True if the input phase can be executed, false otherwise.
     * @throws PhaseNotSupportedException if the input phase type is not
     *         &quot;Appeals Response&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while
     *         processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public boolean canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, PHASE_TYPE_APPEALS_RESPONSE);

        // will throw exception if phase status is neither "Scheduled" nor
        // "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        if (toStart) {
            // return true if all dependencies have stopped and start time has
            // been reached.
            return PhasesHelper.canPhaseStart(phase);
        } else {
            return (PhasesHelper.arePhaseDependenciesMet(phase, false) && allAppealsResolved(phase));
        }
    }

    /**
     * <p>
     * Provides additional logic to execute a phase. This method will be called
     * by start() and end() methods of PhaseManager implementations in Phase
     * Management component. This method can send email to a group os users
     * associated with timeline notification for the project. The email can be
     * send on start phase or end phase base on configuration settings.
     * </p>
     * <p>
     * If the input phase status is Scheduled, then it will do nothing.
     * </p>
     * <p>
     * If the input phase status is Open, then it will perform the following
     * additional logic to stop the phase:
     * </p>
     * <ul>
     * <li>All submissions with failed review scores will be set to the status
     * &quot;Failed Review&quot;.</li>
     * <li>Overall score for the passing submissions will be calculated and
     * saved to the submitters&#146; resource properties together with their
     * placements. The property names are &quot;Final Score&quot; and
     * &quot;Placement&quot;.</li>
     * <li>The winner and runner-up will be populated in the project properties.
     * The property names are &quot;Winner External Reference ID&quot; and
     * &quot;Runner-up External Reference ID&quot;.</li>
     * <li>Submissions that do not win will be set to the status &quot;Completed
     * Without Win&quot;.</li>
     * </ul>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be
     * thrown.
     * </p>
     *
     * <p>
     * Version 1.1. change notes: Add a post-mortem phase after this phase if
     * there is no submission passes review after appeal response.
     * </p>
     *
     * <p>
     * version 1.2. change notes: put the scores of the submissions/submitter handle into the values map.
     *                            for each submission, there will be a sub-map, all in a list with key 'SUBMITTER'.
     * </p>
     *
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     * @throws PhaseNotSupportedException if the input phase type is not
     *         &quot;Appeals Response&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while
     *         processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty
     *         string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, PHASE_TYPE_APPEALS_RESPONSE);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        Map<String, Object> values = new HashMap<String, Object>();

        if (toStart) {
            //for start phase, puts the  submission info/initial score
            values.put("SUBMITTER", PhasesHelper.getSubmitterValueArray(createConnection(),
                    getManagerHelper(), phase.getProject().getId(), false));
        } else {
            //it is going to calculate the final score for every submission
            //and puts the scores into the values after calculation
            boolean passedReview = updateSubmissions(phase, operator, values);
            if (!passedReview) {
                // if there is no submission passes review after appeal response
                // insert the post-mortem phase
                PhasesHelper.insertPostMortemPhase(phase.getProject(), phase, getManagerHelper(), operator);
            }
            Resource[] aggregators = getAggregators(PhasesHelper.locatePhase(phase, "Aggregation", true, true));
            values.put("N_AGGREGATOR", aggregators.length);
        }

        sendEmail(phase, values);
    }

    /**
     * <p>Gets the list of resources assigned <code>Aggregator</code> role.</p>
     *
     * @param aggregationPhase a <code>Phase</code> providing the details for <code>Aggregation</code> phase.
     * @return a <code>Resource</code> array listing the resources granted <code>Aggregator</code> role.
     * @throws PhaseHandlingException if an unexpected error occurs while accessing the data store.
     * @since 1.4
     */
    private Resource[] getAggregators(Phase aggregationPhase) throws PhaseHandlingException {
        Resource[] aggregators;
        Connection connection = createConnection();
        try {
            aggregators = PhasesHelper.searchResourcesForRoleNames(getManagerHelper(), connection,
                    new String[]{"Aggregator"}, aggregationPhase.getId());
        } finally {
            PhasesHelper.closeConnection(connection);
        }
        return aggregators;
    }


    /**
     * This method is called from perform method when phase is stopping. It does
     * the following:
     * <ul>
     * <li>all submissions with failed review scores are set to the status
     * Failed Review.</li>
     * <li>Overall score for the passing submissions are calculated and saved.</li>
     * <li>The winner and runner-up are populated in the project properties.</li>
     * <li>Submissions that do not win are set to the status Completed Without
     * Win.</li>
     * </ul>
     *
     * <p>
     * version 1.1. change notes: Change return type from void to boolean,
     * returning true indicates there is at least one submission passes review
     * after appeal response, false otherwise.
     * </p>
     *
     * <p>
     * version 1.2. change notes: put the scores of the submissions/submitter handle into the values map.
     *                            for each submission, there will be a sub-map, all in a list with key 'SUBMITTER'.
     * </p>
     *
     * @param phase the phase instance.
     * @param operator operator name.
     * @param values the values map
     * @return true if there is at least one submission passed review after
     *         appeal response, false otherwise.
     * @throws PhaseHandlingException if there was an error updating data.
     * @version 1.2
     */
    private boolean updateSubmissions(Phase phase, String operator, Map<String, Object> values)
        throws PhaseHandlingException {
        // A : Update failed review submission status to "Failed Review"
        Connection conn = null;

        // boolean flag to indicate whether all submissions fail review
        boolean allSubmissionFailed = true;

        try {
            conn = createConnection();

            // Search all "Active" submissions for current project
            // change in version 1.5
            Submission[] subs = PhasesHelper.searchActiveSubmissions(
                            getManagerHelper().getUploadManager(), conn, phase
                                            .getProject().getId(), PhasesHelper.CONTEST_SUBMISSION_TYPE);

            // locate previous review phase
            Phase reviewPhase = PhasesHelper.locatePhase(phase, PhasesHelper.REVIEW,
                            false, true);
            long reviewPhaseId = reviewPhase.getId();

            // Search all review scorecard for the review phase
            Review[] reviews = PhasesHelper.searchReviewsForResourceRoles(conn,
                            getManagerHelper(), reviewPhaseId,
                            PhasesHelper.REVIEWER_ROLE_NAMES, null);

            if (reviews.length == 0) {
                throw new PhaseHandlingException("No reviews found for phase: "
                                + reviewPhaseId);
            }

            // Get minimum review score
            float minScore = PhasesHelper.getScorecardMinimumScore(
                            getManagerHelper().getScorecardManager(),
                            reviews[0]);

            // create array to hold scores from all reviewers for all
            // submissions
            com.topcoder.management.review.scoreaggregator.Submission[] submissionScores;
            submissionScores = new com.topcoder.management.review.scoreaggregator.Submission[subs.length];

            // for each submission, populate scores array to use with review
            // score aggregator.
            for (int iSub = 0; iSub < subs.length; iSub++) {
                Submission submission = subs[iSub];
                long subId = submission.getId();
                List<Float> scoresList = new ArrayList<Float>();

                // Match the submission with its reviews
                for (int j = 0; j < reviews.length; j++) {
                    if (subId == reviews[j].getSubmission()) {
                        // get review score
                        scoresList.add(reviews[j].getScore());
                    }
                }

                // create float array
                float[] scores = new float[scoresList.size()];

                for (int iScore = 0; iScore < scores.length; iScore++) {
                    scores[iScore] = ((Float) scoresList.get(iScore))
                                    .floatValue();
                }

                submissionScores[iSub] = new com.topcoder.management.review.scoreaggregator.Submission(
                                subId, scores);
            }

            // now calculate the aggregated scores and placements
            ReviewScoreAggregator scoreAggregator = getManagerHelper()
                            .getScorecardAggregator();

            // this will hold as many elements as submissions
            AggregatedSubmission[] aggregations = scoreAggregator
                            .aggregateScores(submissionScores);
            RankedSubmission[] placements = scoreAggregator
                            .calcPlacements(aggregations);

            // status objects
            SubmissionStatus failedStatus = PhasesHelper.getSubmissionStatus(
                            getManagerHelper().getUploadManager(),
                            "Failed Review");
            SubmissionStatus noWinStatus = PhasesHelper.getSubmissionStatus(
                            getManagerHelper().getUploadManager(),
                            "Completed Without Win");

            Resource winningSubmitter = null;
            Resource runnerUpSubmitter = null;

            // again iterate over submissions to set the initial score and
            // placement
            for (int iSub = 0; iSub < placements.length; iSub++) {
                RankedSubmission rankedSubmission = placements[iSub];
                rankedSubmission = breakTies(rankedSubmission, subs, placements);
                Submission submission = getSubmissionById(subs,
                                rankedSubmission.getId());
                float aggScore = rankedSubmission.getAggregatedScore();
                int placement = rankedSubmission.getRank();

                // update submitter's final score
                long submitterId = submission.getUpload().getOwner();
                Resource submitter = getManagerHelper().getResourceManager()
                                .getResource(submitterId);

                // OrChange - update the final score and the placement into the
                // submission table.
                submission.setFinalScore(Double.valueOf(aggScore + ""));
                submission.setPlacement(new Long(placement));

                // Old Code Begins
                // submitter.setProperty("Final Score",
                // String.valueOf(aggScore));
                // submitter.setProperty("Placement",
                // String.valueOf(placement));
                // Old Code Ends

                // if failed review, then update the status
                // Only if the status of the current submission is "Active",
                // since "Failed Screening" should not be updated.
                if (aggScore < minScore
                                && submission.getSubmissionStatus()
                                                .getDescription().equals(
                                                                "Active")) {
                    submission.setSubmissionStatus(failedStatus);
                } else {

                    // there is submission passed review, set flag
                    // allSubmissionFailed to false
                    allSubmissionFailed = false;

                    // cache winning submitter.
                    if (placement == 1) {
                        winningSubmitter = submitter;
                    } else {
                        // cache runner up submitter.
                        if (placement == 2) {
                            runnerUpSubmitter = submitter;
                        }
                        submission.setSubmissionStatus(noWinStatus);
                    }
                }
                // persist the change
                getManagerHelper().getUploadManager().updateSubmission(
                                submission, operator);
                // getManagerHelper().getResourceManager().updateResource(submitter,
                // operator);
            } // end for

            // cannot be the case where there is a runner up but no winner
            if (runnerUpSubmitter != null && winningSubmitter == null) {
                throw new PhaseHandlingException(
                                "Runner up present, but no winner for project with id:"
                                                + phase.getProject().getId());
            }

            // if there is a winner
            if (winningSubmitter != null) {
                // Set project properties to store the winner and the runner up
                // Get the project instance
                Project project = getManagerHelper().getProjectManager()
                                .getProject(phase.getProject().getId());

                Object winnerExtId = winningSubmitter.getProperty(PhasesHelper.EXTERNAL_REFERENCE_ID);
                project.setProperty("Winner External Reference ID", winnerExtId);

                // if there is a runner up
                if (runnerUpSubmitter != null) {
                    Object runnerExtId = runnerUpSubmitter.getProperty(PhasesHelper.EXTERNAL_REFERENCE_ID);
                    project.setProperty("Runner-up External Reference ID", runnerExtId);
                }

                // update the project
                getManagerHelper().getProjectManager().updateProject(project,
                                "Update the winner and runner up.", operator);
            }


            //for each submission, get the submitter and its scores
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            for (Submission submission : subs) {
                Map<String, Object> infos = new HashMap<String, Object>();
                infos.put("SUBMITTER_HANDLE", PhasesHelper.notNullValue(
                                              getManagerHelper().getResourceManager().getResource(
                                               submission.getUpload().getOwner()).getProperty(PhasesHelper.HANDLE)));
                infos.put("SUBMITTER_PRE_APPEALS_SCORE", submission.getInitialScore());
                infos.put("SUBMITTER_POST_APPEALS_SCORE", submission.getFinalScore());
                infos.put("SUBMITTER_RESULT", submission.getPlacement());
                result.add(infos);
            }
            values.put("SUBMITTER", result);
        } catch (SQLException e) {
            throw new PhaseHandlingException("Problem when looking up id", e);
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException(
                            "Problem with resource persistence", e);
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("Problem with upload persistence",
                            e);
        } catch (PersistenceException e) {
            throw new PhaseHandlingException(
                            "Problem with project persistence", e);
        } catch (ValidationException e) {
            throw new PhaseHandlingException(
                            "Problem with project persistence", e);
        } catch (InconsistentDataException e) {
            throw new PhaseHandlingException("Problem when aggregating scores",
                            e);
        } finally {
            PhasesHelper.closeConnection(conn);
        }

        // return whether there is at least one submission passes review after
        // appeal response
        return !allSubmissionFailed;
    }

    /**
     * Return suitable submission for given submissionId.
     *
     * @param submissions the submission array
     * @param submissionId the submissionId
     * @return submission
     * @throws PhaseHandlingException if no submission found
     */
    private Submission getSubmissionById(Submission[] submissions,
                    long submissionId) throws PhaseHandlingException {
        for (int i = 0; i < submissions.length; i++) {
            if (submissions[i].getId() == submissionId) {
                return submissions[i];
            }
        }
        throw new PhaseHandlingException(
                        "submissions not found for submissionId: "
                                        + submissionId);
    }

    /**
     * Break ties by submission timestamp.
     *
     * @param submission the submission to calculate
     * @param submissions all the submission records
     * @param placements all the ranked submission records
     * @return the submission with fixed placement
     * @throws PhaseHandlingException if an error occurs when retrieving data.
     */
    private RankedSubmission breakTies(RankedSubmission submission,
                    Submission[] submissions, RankedSubmission[] placements) throws PhaseHandlingException {
        int rank = submission.getRank();
        Date timestamp1 = getSubmissionById(submissions, submission.getId())
                        .getUpload().getCreationTimestamp();
        for (int i = 0; i < placements.length; ++i) {
            if (placements[i].getRank() == submission.getRank()) {
                Submission tie = getSubmissionById(submissions, placements[i]
                                .getId());
                Date timestamp2 = tie.getUpload().getCreationTimestamp();

                if (timestamp1 != null && timestamp2 != null
                                && timestamp2.before(timestamp1)) {
                    ++rank;
                }
            }
        }
        return new RankedSubmission(submission, rank);
    }

    /**
     * This method returns true if all the appeals have been responded to.
     *
     * @param phase phase instance.
     * @return true if all appeals are resolved, false otherwise.
     * @throws PhaseHandlingException if an error occurs when retrieving data.
     */
    private boolean allAppealsResolved(Phase phase) throws PhaseHandlingException {
        // Find appeals : Go back to the nearest Review phase
        Phase reviewPhase = PhasesHelper.locatePhase(phase, PhasesHelper.REVIEW, false,
                        false);
        if (reviewPhase == null) {
            return false;
        }
        long reviewPhaseId = reviewPhase.getId();
        Connection conn = null;

        try {
            conn = createConnection();
            // Get all reviews
            Review[] reviews = PhasesHelper.searchReviewsForResourceRoles(conn,
                            getManagerHelper(), reviewPhaseId,
                            PhasesHelper.REVIEWER_ROLE_NAMES, null);

            // for each review
            for (int i = 0; i < reviews.length; i++) {
                int appealCount = 0;
                int responseCount = 0;

                Comment[] comments = reviews[i].getAllComments();

                for (int c = 0; c < comments.length; c++) {
                    String commentType = comments[c].getCommentType().getName();

                    if ("Appeal".equals(commentType)) {
                        appealCount++;
                    } else if ("Appeal Response".equals(commentType)) {
                        responseCount++;
                    }
                }

                Item[] items = reviews[i].getAllItems();

                for (int j = 0; j < items.length; ++j) {
                    comments = items[j].getAllComments();

                    for (int c = 0; c < comments.length; c++) {
                        String commentType = comments[c].getCommentType()
                                        .getName();

                        if ("Appeal".equals(commentType)) {
                            appealCount++;
                        } else if ("Appeal Response".equals(commentType)) {
                            responseCount++;
                        }
                    }
                }

                // if appeals count does not match response count, return false.
                if (appealCount != responseCount) {
                    return false;
                }
            }

            return true;
        } finally {
            PhasesHelper.closeConnection(conn);
        }
    }
}
