/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.cronos.onlinereview.phases.logging.LogMessage;
import com.cronos.onlinereview.phases.lookup.SubmissionTypeLookupUtility;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a
 * phase can be executed and to add extra logic to execute a phase. It will be
 * used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection and email
 * sending. This class handle the aggregation phase. If the input is of other
 * phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * If the input phase status is Scheduled, then it will check if the phase can
 * be started using the following conditions:
 * <ul>
 * <li>The dependencies are met.</li>
 * </ul>
 * </p>
 * <p>
 * If the input phase status is Open, then it will check if the phase can be
 * stopped using the following conditions:
 * <ul>
 * <li>The dependencies are met.</li>
 * <li>The winning submission have one aggregated review scorecard committed.</li>
 * </ul>
 * </p>
 * <p>
 * The additional logic for executing this phase is:
 * </p>
 * <p>
 * If the input phase status is Scheduled, then it will perform the following
 * additional logic to start the phase:
 * <ul>
 * <li>If Aggregation worksheet is not created, it should be created; otherwise
 * it should be marked uncommitted, as well as the aggregation review comments.</li>
 * </ul>
 * </p>
 * <p>
 * If the input phase status is Open, then it will do nothing.
 * </p>
 *
 * <p>
 * Version 1.2 changes note:
 * <ul>
 * <li>
 * Added capability to support different email template for different role (e.g. Submitter, Reviewer, Manager, etc).
 * </li>
 * <li>
 * Support for more information in the email generated: for start, puts the aggregators number into the values map.
 * </li>
 * </ul>
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 *
 * @author tuenm, bose_java, argolite, waits
 * @version 1.2
 */
public class AggregationPhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.AggregationPhaseHandler";

    /** constant for aggregation phase type. */
    private static final String PHASE_TYPE_AGGREGATION = "Aggregation";

    /**
     * array of comment types to be copied from individual review scorecards to
     * new aggregation worksheet.
     */
    private static final String[] COMMENT_TYPES_TO_COPY = new String[] {
        "Comment", "Required", "Recommended", "Appeal",
        "Appeal Response", "Aggregation Comment",
        "Aggregation Review Comment", "Submitter Comment",
        "Manager Comment"};

    /**
     * The log instance used by this handler.
     */
    private static final Log LOG = LogFactory
                    .getLog(AggregationPhaseHandler.class.getName());

    /**
     * Create a new instance of AggregationPhaseHandler using the default
     * namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading
     *         configuration settings.
     */
    public AggregationPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of AggregationPhaseHandler using the given
     * namespace for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading
     *         configuration settings.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public AggregationPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Check if the input phase can be executed or not. This method will check
     * the phase status to see what will be executed. This method will be called
     * by canStart() and canEnd() methods of PhaseManager implementations in
     * Phase Management component.
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase
     * can be started using the following conditions:
     * <ul>
     * <li>The dependencies are met.</li>
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be
     * stopped using the following conditions:
     * <ul>
     * <li>The dependencies are met.</li>
     * <li>The winning submission have one aggregated review scorecard
     * committed.</li>
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
     *         &quot;Aggregation&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while
     *         processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public boolean canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, PHASE_TYPE_AGGREGATION);

        // will throw exception if phase status is neither "Scheduled" nor
        // "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        if (toStart) {
            // return true if all dependencies have stopped and start time has
            // been reached and there is a winner
            // and one aggregator.
            if (!PhasesHelper.canPhaseStart(phase)) {
                return false;
            }

            Connection conn = null;
            try {
                conn = createConnection();
                Resource winner = PhasesHelper.getWinningSubmitter(
                                getManagerHelper().getResourceManager(),
                                getManagerHelper().getProjectManager(), conn,
                                phase.getProject().getId());

                Resource[] aggregator = PhasesHelper
                                .searchResourcesForRoleNames(
                                                getManagerHelper(), conn,
                                                new String[] {"Aggregator"},
                                                phase.getId());

                if (winner == null) {
                    LOG.log(Level.WARN,
                                    "can't open aggregation because there is no winner for project: "
                                                    + phase.getProject()
                                                                    .getId());
                }
                if (aggregator.length != 1) {
                    LOG.log(Level.WARN,
                                    "can't open aggregation because there is no Aggregator for project: "
                                                    + phase.getProject()
                                                                    .getId());
                }
                // return true if there is a winner and an aggregator
                return (winner != null) && (aggregator.length == 1);
            } finally {
                PhasesHelper.closeConnection(conn);
            }
        } else {
            // return true if all dependencies have stopped and aggregation
            // worksheet exists.
            return (PhasesHelper.arePhaseDependenciesMet(phase, false) && isAggregationWorksheetPresent(phase));
        }
    }

    /**
     * <p>
     * Provides additional logic to execute a phase. This method will be called
     * by start() and end() methods of PhaseManager implementations in Phase
     * Management component. This method can send email to a group of users
     * associated with timeline notification for the project.The email can be
     * send on start phase or end phase base on configuration settings.
     * </p>
     * <p>
     * If the input phase status is Scheduled, then it will perform the
     * following additional logic to start the phase:
     * <ul>
     * <li>If Aggregation worksheet is not created, it should be created;
     * otherwise it should be marked uncommitted, as well as the aggregation
     * review comments.</li>
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Open, then it will do nothing.
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be
     * thrown.
     * </p>
     * <p>
     * In version 1.2, put the aggregator number into the values map.
     * </p>
     *
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     *
     * @throws PhaseNotSupportedException if the input phase type is not
     *         "Aggregation" type.
     * @throws PhaseHandlingException if there is any error occurred while
     *         processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty
     *         string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, PHASE_TYPE_AGGREGATION);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        Map<String, Object> values = new HashMap<String, Object>();
        if (toStart) {
            //put the aggregator number into the values map
            values.put("N_AGGREGATOR", checkAggregationWorksheet(phase, operator));
        }
        sendEmail(phase, values);
    }

    /**
     * This method is called from perform method when phase is starting. If the
     * Aggregation worksheet is not created, it is created; otherwise it is
     * marked uncommitted, as well as the aggregation review comments.
     *
     * @param phase the phase.
     * @param operator operator name.
     * @return the number of aggregators
     *
     * @throws PhaseHandlingException if an error occurs when retrieving/saving
     *         data.
     */
    private int checkAggregationWorksheet(Phase phase, String operator)
        throws PhaseHandlingException {
        // Check if the Aggregation worksheet is created
        Phase previousAggregationPhase = PhasesHelper.locatePhase(phase,
                        "Aggregation", false, false);

        Connection conn = null;

        try {
            conn = createConnection();

            // Search for id of the Aggregator
            Resource[] aggregators = PhasesHelper.searchResourcesForRoleNames(
                            getManagerHelper(), conn,
                            new String[] {"Aggregator"}, phase.getId());
            if (aggregators.length == 0) {
                throw new PhaseHandlingException(
                                "No Aggregator resource found for phase: "
                                                + phase.getId());
            }
            String aggregatorUserId = (String) aggregators[0]
                            .getProperty(PhasesHelper.EXTERNAL_REFERENCE_ID);

            Review aggWorksheet = null;
            if (previousAggregationPhase != null) {
                aggWorksheet = PhasesHelper.getAggregationWorksheet(conn,
                                getManagerHelper(), previousAggregationPhase
                                                .getId());
            }

            if (aggWorksheet == null) {
                // create the worksheet
                aggWorksheet = new Review();
                aggWorksheet.setAuthor(aggregators[0].getId());

                // copy the comments from review scorecards
                Phase reviewPhase = PhasesHelper.locatePhase(phase, PhasesHelper.REVIEW,
                                false, true);
                Resource[] reviewers = PhasesHelper
                                .searchResourcesForRoleNames(
                                                getManagerHelper(),
                                                conn,
                                                PhasesHelper.REVIEWER_ROLE_NAMES,
                                                reviewPhase.getId());

                // find winning submitter.
                Resource winningSubmitter = PhasesHelper.getWinningSubmitter(
                                getManagerHelper().getResourceManager(),
                                getManagerHelper().getProjectManager(), conn,
                                phase.getProject().getId());
                if (winningSubmitter == null) {
                    throw new PhaseHandlingException(
                                    "No winner for project[Winner id not set] with id : "
                                                    + phase.getProject()
                                                                    .getId());
                }

                // find the winning submission
                Filter filter = SubmissionFilterBuilder
                                .createResourceIdFilter(winningSubmitter
                                                .getId());
                // change in version 1.4
                // AND submission type ID = "Contest Submission"
                long submissionTypeId = SubmissionTypeLookupUtility.lookUpId(conn,
                        PhasesHelper.CONTEST_SUBMISSION_TYPE);
                Filter typeFilter = SubmissionFilterBuilder.createSubmissionTypeIdFilter(submissionTypeId);
                Submission[] submissions = getManagerHelper()
                                .getUploadManager().searchSubmissions(new AndFilter(filter, typeFilter));

                // OrChange - Modified to handle multiple submissions for a
                // single resource
                Long winningSubmissionId = null;
                if (submissions == null || submissions.length == 0) {
                    LOG.log(Level.ERROR, "No winner for project with id"
                                    + phase.getProject().getId());
                    throw new PhaseHandlingException(
                                    "No winning submission for project[Winner id search"
                                                    + " returned empty result for submission] with id : "
                                                    + phase.getProject()
                                                                    .getId());
                } else if (submissions.length >= 1) {
                    // loop through the submissions and find out the one with
                    // the placement as first
                    for (int i = 0; i < submissions.length; i++) {
                        Submission submission = submissions[i];
                        if (submission.getPlacement() != null
                                        && submission.getPlacement()
                                                        .longValue() == 1) {
                            winningSubmissionId = new Long(submission.getId());
                            break;
                        }
                    }
                } else {
                    winningSubmissionId = new Long(submissions[0].getId());
                }
                // No winner id set at this point means, none of the submissions
                // have placement as 1
                if (winningSubmissionId == null) {
                    throw new PhaseHandlingException(
                                    "No winning submission for project[Submissions from the"
                                                    + " winner id does not have placement 1] with id : "
                                                    + phase.getProject()
                                                                    .getId());
                }

                // Search all review scorecard from review phase for the winning
                // submitter
                Review[] reviews = PhasesHelper.searchReviewsForResourceRoles(
                                conn, getManagerHelper(), reviewPhase.getId(),
                                PhasesHelper.REVIEWER_ROLE_NAMES,
                                winningSubmissionId);

                for (int r = 0; r < reviews.length; r++) {
                    aggWorksheet.setScorecard(reviews[r].getScorecard());
                    aggWorksheet.setSubmission(reviews[r].getSubmission());
                    PhasesHelper.copyComments(reviews[r], aggWorksheet,
                                    COMMENT_TYPES_TO_COPY, null);
                    PhasesHelper.copyReviewItems(reviews[r], aggWorksheet,
                                    COMMENT_TYPES_TO_COPY);
                }

                // Adding empty comments
                CommentType aggregationReviewCommentType = PhasesHelper
                                .getCommentType(getManagerHelper()
                                                .getReviewManager(),
                                                "Aggregation Review Comment");
                CommentType submitterCommentType = PhasesHelper.getCommentType(
                                getManagerHelper().getReviewManager(),
                                "Submitter Comment");
                for (int i = 0; i < reviewers.length; i++) {
                    if (reviewers[i].getProperty(PhasesHelper.EXTERNAL_REFERENCE_ID) == null
                                    || reviewers[i].getProperty(
                                            PhasesHelper.EXTERNAL_REFERENCE_ID)
                                                    .equals(aggregatorUserId)) {
                        continue;
                    }
                    Comment comment = new Comment();
                    comment.setAuthor(reviewers[i].getId());
                    comment.setComment("");
                    comment.setCommentType(aggregationReviewCommentType);
                    aggWorksheet.addComment(comment);
                }
                Comment comment = new Comment();
                comment.setAuthor(winningSubmitter.getId());
                comment.setComment("");
                comment.setCommentType(submitterCommentType);
                aggWorksheet.addComment(comment);

                getManagerHelper().getReviewManager().createReview(
                                aggWorksheet, operator);
            } else {
                // Mark uncommitted for the worksheet:
                aggWorksheet.setAuthor(aggregators[0].getId());
                aggWorksheet.setCommitted(false);

                // Mark uncommitted for comments:
                Comment[] comments = aggWorksheet.getAllComments();

                // For each comment, reset comment extra info
                for (int i = 0; i < comments.length; i++) {
                    Comment comment = comments[i];
                    if (PhasesHelper.APPROVED.equalsIgnoreCase((String) comment
                                    .getExtraInfo())
                                    || PhasesHelper.ACCEPTED
                                                    .equalsIgnoreCase((String) comment
                                                                    .getExtraInfo())) {
                        comment.setExtraInfo("Approving");
                    } else if (PhasesHelper.REJECTED.equalsIgnoreCase((String) comment
                                    .getExtraInfo())) {
                        comment.setExtraInfo(null);
                    }
                }

                // persist the copy
                aggWorksheet = PhasesHelper.cloneReview(aggWorksheet);
                getManagerHelper().getReviewManager().createReview(
                                aggWorksheet, operator);
            }
            LOG.log(Level.INFO, new LogMessage(new Long(phase.getId()),
                            operator, "create aggregate worksheet."));
            return aggregators.length;
        } catch (ReviewManagementException e) {
            throw new PhaseHandlingException("Problem when persisting review",
                            e);
        } catch (SQLException e) {
            LOG.log(Level.ERROR, new LogMessage(new Long(phase.getId()),
                operator, "Fail to create aggregate worksheet.", e));
            throw new PhaseHandlingException("Problem when looking up ids.", e);
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException(
                            "Problem when retrieving winning submission.", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException(
                            "Problem when retrieving winning submission.", e);
        } finally {
            PhasesHelper.closeConnection(conn);
        }
    }

    /**
     * returns true if aggregation worksheets exists.
     *
     * @param phase phase to check.
     *
     * @return whether aggregation worksheets exists.
     * @throws PhaseHandlingException if an error occurs when retrieving/saving
     *         data.
     */
    private boolean isAggregationWorksheetPresent(Phase phase)
        throws PhaseHandlingException {
        Connection conn = null;
        try {
            conn = createConnection();
            Review review = PhasesHelper.getAggregationWorksheet(conn, getManagerHelper(), phase.getId());
            return (review != null && review.isCommitted());
        } finally {
            PhasesHelper.closeConnection(conn);
        }

    }
}
