/*
 * Copyright (C) 2009-2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.cronos.onlinereview.phases.logging.LogMessage;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a
 * phase can be executed and to add extra logic to execute a phase. It will be
 * used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection and email
 * sending. This class handle the aggregation review phase. If the input is of
 * other phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * The aggregation review phase can start as soon as the dependencies are met
 * and can stop when the following conditions met:
 * </p>
 * <ul>
 * <li>The dependencies are met</li>
 * <li>The aggregation review is performed by two reviewers other than the
 * aggregator, and the winning submitter.</li>
 * </ul>
 * <p>
 * The additional logic for executing this phase is:
 * </p>
 * <ul>
 * <li>When Aggregation Review phase is stopping, if the aggregation is rejected
 * by anyone, another aggregation/aggregation review cycle is inserted.</li>
 * </ul>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 *
 * <p>
 * Version 1.1 (Appeals Early Completion Release Assembly 1.0) Change notes:
 * <ol>
 * <li>Added support for automated aggregation review completion when phase
 * ends.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.4 Change notes:
 * <ol>
 * <li>Made some duplicate String values as constants of class.</li>
 * <li>Updated {@link #perform(Phase, String)} method to calculate the number of final reviewers for project and
 * bind it to map used for filling email template.</li>
 * </ol>
 * </p>
 *
 * @author tuenm, bose_java, pulky, argolite, waits, saarixx, myxgyy, isv
 * @version 1.4
 */
public class AggregationReviewPhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.AggregationReviewPhaseHandler";

    /** constant for aggregation Review phase type. */
    private static final String PHASE_TYPE_AGGREGATION_REVIEW = "Aggregation Review";

    /**
     * constant for aggregation review comment.
     *
     * @since 1.4
     */
    private static final String AGGREGATION_REVIEW_COMMENT = "Aggregation Review Comment";

    /**
     * constant for Submitter comment.
     *
     * @since 1.4
     */
    private static final String SUBMITTER_COMMENT = "Submitter Comment";

    /**
     * constant for Aggregation phase.
     *
     * @since 1.4
     */
    private static final String AGGREGATION = "Aggregation";

    /**
     * constant for Aggregator.
     *
     * @since 1.4
     */
    private static final String AGGREGATOR = "Aggregator";

    /**
     * Log used for this class.
     */
    private static final Log LOG = LogFactory
                    .getLog(AggregationReviewPhaseHandler.class.getName());

    /**
     * Create a new instance of AggregationReviewPhaseHandler using the default
     * namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading
     *         configuration settings.
     */
    public AggregationReviewPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of AggregationReviewPhaseHandler using the given
     * namespace for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading
     *         configuration settings.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public AggregationReviewPhaseHandler(String namespace) throws ConfigurationException {
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
     * can be started using the following conditions:
     * <ul>
     * <li>The dependencies are met.</li>
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be
     * stopped using the following conditions:
     * <ul>
     * <li>The dependencies are met</li>
     * <li>The aggregation review is performed by two reviewers other than the
     * aggregator, and the winning submitter or the phase reaches its end.</li>
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
     *         "Aggregation Review" type.
     * @throws PhaseHandlingException if there is any error occurred while
     *         processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public boolean canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");

        LOG.log(Level.DEBUG, "entering canPerform: phaseId" + phase.getId());

        PhasesHelper.checkPhaseType(phase, PHASE_TYPE_AGGREGATION_REVIEW);

        // will throw exception if phase status is neither "Scheduled" nor
        // "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        LOG.log(Level.DEBUG, "toStart: " + toStart);
        if (toStart) {
            // return true if all dependencies have stopped and start time has
            // been reached.
            return PhasesHelper.canPhaseStart(phase);
        } else {

            boolean dependencyMet = PhasesHelper.arePhaseDependenciesMet(phase,
                            false);

            if (!dependencyMet) {
                // dependency not met
                return false;
            }

            boolean aggregationReviewDone = aggregationReviewDone(phase);

            if (PhasesHelper.reachedPhaseEndTime(phase)
                            && !aggregationReviewDone) {
                // Approve pending aggregation reviews and continue
                aggregationReviewDone = true;
                try {
                    LOG.log(Level.DEBUG, "Automatically approving pending aggregation reviews");
                    approvePendingAggregationReview(phase);
                } catch (Exception e) {
                    LOG.log(Level.WARN, new LogMessage(new Long(phase.getId()),
                                                       null,
                                        "Was not able to automatically approve pending aggregation reviews.",
                                                       e));
                    aggregationReviewDone = false;
                }
            }

            LOG.log(Level.DEBUG, "ret: " + aggregationReviewDone);

            return aggregationReviewDone;
        }
    }

    /**
     * Provides additional logic to execute a phase. This method will be called
     * by start() and end() methods of PhaseManager implementations in Phase
     * Management component. This method can send email to a group os users
     * associated with timeline notification for the project. The email can be
     * send on start phase or end phase base on configuration settings. This
     * method will perform the following additional logic: If the input phase
     * status is Scheduled, then it will do nothing. If the input phase status
     * is Open, then it will perform the following additional logic to stop the
     * phase:
     * <ul>
     * <li>If the aggregation is rejected by anyone, another
     * aggregation/aggregation review cycle is inserted.</li>
     * </ul>
     * If the input phase status is Closed, then PhaseHandlingException will be
     * thrown.
     *
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     *
     * @throws PhaseNotSupportedException if the input phase type is not
     *         "Aggregation Review" type.
     * @throws PhaseHandlingException if there is any error occurred while
     *         processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty
     *         string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, PHASE_TYPE_AGGREGATION_REVIEW);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        Map<String, Object> values = new HashMap<String, Object>();
        if (!toStart) {
            checkAggregationReview(phase, operator);

            Resource[] finalReviewers = getFinalReviewers(PhasesHelper.locatePhase(phase, "Final Review", true, true));
            values.put("N_FINAL_REVIEWERS", finalReviewers.length);
        }

        sendEmail(phase, values);
    }

    /**
     * <p>Gets the list of resources assigned <code>Final Reviewer</code> role.</p>
     *
     * @param finalReviewPhase a <code>Phase</code> providing the details for <code>Final Review</code> phase.
     * @return a <code>Resource</code> array listing the resources granted <code>Final Reviewer</code> role.
     * @throws PhaseHandlingException if an unexpected error occurs while accessing the data store.
     * @since 1.4
     */
    private Resource[] getFinalReviewers(Phase finalReviewPhase) throws PhaseHandlingException {
        Resource[] finalReviewers;
        Connection connection = createConnection();
        try {
            finalReviewers = PhasesHelper.searchResourcesForRoleNames(getManagerHelper(),
                    connection, new String[] {"Final Reviewer"}, finalReviewPhase.getId());
        } finally {
            PhasesHelper.closeConnection(connection);
        }
        return finalReviewers;
    }

    /**
     * This method approves all pending aggregation reviews.
     *
     * @param phase the phase instance.
     *
     * @throws PhaseHandlingException if an error occurs when retrieving or
     *         storing the data.
     * @since 1.1
     */
    private void approvePendingAggregationReview(Phase phase) throws PhaseHandlingException {
        LOG.log(Level.INFO, "Approving pending aggregation reviews");

        Review aggregationWorksheet = getAggregationWorksheet(phase);

        // Obtain an instance of review manager
        ReviewManager revMgr = getManagerHelper().getReviewManager();
        try {
            for (int i = 0; i < aggregationWorksheet.getNumberOfComments(); i++) {
                Comment comment = aggregationWorksheet.getComment(i);

                if ((comment.getCommentType().getName().equals(
                                SUBMITTER_COMMENT) || comment
                                .getCommentType().getName().equals(
                                        AGGREGATION_REVIEW_COMMENT))
                                && !PhasesHelper.APPROVED.equals(comment.getExtraInfo())
                                && !PhasesHelper.REJECTED.equals(comment.getExtraInfo())) {
                    comment.setExtraInfo(PhasesHelper.APPROVED);
                }
            }

            revMgr.updateReview(aggregationWorksheet, "System");
        } catch (ReviewManagementException rme) {
            throw new PhaseHandlingException(
                            "There were problems while approving pending aggregation reviews",
                            rme);
        }
    }

    /**
     * This method is called by perform method when the phase is stopping. If
     * the aggregation is rejected by anyone, another aggregation/aggregation
     * review cycle is inserted by this method.
     *
     * @param phase phase instance.
     * @param operator operator name.
     *
     * @throws PhaseHandlingException if an error occurs when retrieving/saving
     *         data.
     */
    private void checkAggregationReview(Phase phase, String operator) throws PhaseHandlingException {
        Review aggregationWorksheet = getAggregationWorksheet(phase);
        Comment[] comments = aggregationWorksheet.getAllComments();

        Connection conn = null;

        // Locate the winning submitter
        try {
            conn = createConnection();

            // check for approved/rejected comments.
            boolean rejected = false;

            for (int i = 0; i < comments.length; i++) {
                String value = (String) comments[i].getExtraInfo();
                String commentType = comments[i].getCommentType().getName();

                if (commentType.equals(AGGREGATION_REVIEW_COMMENT)
                                || commentType.equals(SUBMITTER_COMMENT)) {
                    if (PhasesHelper.APPROVED.equalsIgnoreCase(value)
                                    || PhasesHelper.ACCEPTED.equalsIgnoreCase(value)) {
                        continue;
                    } else if (PhasesHelper.REJECTED.equalsIgnoreCase(value)) {
                        rejected = true;

                        break;
                    } else {
                        throw new PhaseHandlingException(
                                        "Comment can either be Approved or Rejected.");
                    }
                }
            }

            if (rejected) {
                // Extra info for Aggregation Review Comment and Submitter
                // Comment must be cleared.
                for (int i = 0; i < comments.length; i++) {
                    String commentType = comments[i].getCommentType().getName();

                    if (commentType.equals(AGGREGATION_REVIEW_COMMENT)
                                    || commentType.equals(SUBMITTER_COMMENT)) {
                        comments[i].resetExtraInfo();
                    }
                }

                // Insert new agg/agg review cycle
                Project currentPrj = phase.getProject();

                // create phase type and status objects
                PhaseType aggPhaseType = PhasesHelper.getPhaseType(
                                getManagerHelper().getPhaseManager(),
                                AGGREGATION);
                PhaseType aggReviewPhaseType = PhasesHelper.getPhaseType(
                                getManagerHelper().getPhaseManager(),
                                PHASE_TYPE_AGGREGATION_REVIEW);
                PhaseStatus phaseStatus = PhasesHelper.getPhaseStatus(
                                getManagerHelper().getPhaseManager(),
                                PhasesHelper.PHASE_STATUS_SCHEDULED);

                // use helper method to create and save the new phases
                int currentPhaseIndex = PhasesHelper.createNewPhases(
                                currentPrj, phase, new PhaseType[] {aggPhaseType, aggReviewPhaseType},
                                phaseStatus, getManagerHelper().getPhaseManager(), operator, false);

                // save the phases
                getManagerHelper().getPhaseManager().updatePhases(currentPrj,
                                operator);

                Phase[] allPhases = currentPrj.getAllPhases();
                // get the id of the newly created aggregation phase
                long newAggPhaseId = allPhases[currentPhaseIndex + 1].getId();

                PhasesHelper.createAggregatorOrFinalReviewer(
                                allPhases[currentPhaseIndex - 1],
                                getManagerHelper(), conn, AGGREGATOR,
                                newAggPhaseId, operator);
            }
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases",
                            e);
        } finally {
            PhasesHelper.closeConnection(conn);
        }
    }

    /**
     * This method checks if the aggregation review has been performed by two
     * reviewers other than the aggregator, and the winning submitter.
     *
     * @param phase the phase instance.
     *
     * @return true if aggregation review is done.
     *
     * @throws PhaseHandlingException if an error occurs when retrieving data.
     */
    private boolean aggregationReviewDone(Phase phase) throws PhaseHandlingException {
        Comment[] comments = getAggregationWorksheet(phase).getAllComments();

        // Locate the nearest Review and Aggregation phase
        Phase reviewPhase = PhasesHelper.locatePhase(phase, PhasesHelper.REVIEW, false,
                        false);
        Phase aggregationPhase = PhasesHelper.locatePhase(phase, AGGREGATION,
                        false, false);
        if (reviewPhase == null || aggregationPhase == null) {
            LOG.log(Level.INFO, "Can't start phase: reviewPhase == null || aggregationPhase == null");
            return false;
        }
        Connection conn = null;

        try {
            conn = createConnection();

            // will hold all reviewers
            Resource[] reviewers = PhasesHelper.searchResourcesForRoleNames(
                            getManagerHelper(), conn,
                            PhasesHelper.REVIEWER_ROLE_NAMES, reviewPhase
                                            .getId());
            Resource[] aggregators = PhasesHelper.searchResourcesForRoleNames(
                            getManagerHelper(), conn,
                            new String[] {AGGREGATOR}, aggregationPhase
                                            .getId());
            if (aggregators.length == 0) {
                LOG.log(Level.DEBUG, "No Aggregator resource found for phase: "
                                + aggregationPhase.getId());
                throw new PhaseHandlingException(
                                "No Aggregator resource found for phase: "
                                                + aggregationPhase.getId());
            }
            String aggregatorUserId = (String) aggregators[0]
                            .getProperty(PhasesHelper.EXTERNAL_REFERENCE_ID);

            // winning submitter.
            Resource winningSubmitter = PhasesHelper.getWinningSubmitter(
                            getManagerHelper().getResourceManager(),
                            getManagerHelper().getProjectManager(), conn, phase
                                            .getProject().getId());

            // The reviewers that are not aggregator should have a review
            // comment of type
            // "Aggregation Review Comment" with extra info of either "Approved"
            // or "Rejected".
            for (int i = 0; i < reviewers.length; i++) {
                if (reviewers[i].getProperty(PhasesHelper.EXTERNAL_REFERENCE_ID) == null
                                || reviewers[i].getProperty(
                                                PhasesHelper.EXTERNAL_REFERENCE_ID)
                                                .equals(aggregatorUserId)) {
                    continue;
                }
                if (!doesCommentExist(comments, reviewers[i].getId(),
                                AGGREGATION_REVIEW_COMMENT)) {
                    LOG.log(Level.INFO,
                            "cant't start phase: not exists comment type 'Aggregation Review Comment' for reviewer: "
                            + reviewers[i].getId());
                    return false;
                }
            }
            if (doesCommentExist(comments, winningSubmitter.getId(),
                            SUBMITTER_COMMENT)) {
                return true;
            }
            LOG.log(Level.INFO,
                    "cant't start phase: not exists comment type 'Submitter Comment' for reviewer: "
                    + winningSubmitter.getId());
            return false;
        } finally {
            PhasesHelper.closeConnection(conn);
        }
    }

    /**
     * This method returns the aggregation review scorecard.
     *
     * @param phase phase instance.
     *
     * @return aggregation review scorecard.
     *
     * @throws PhaseHandlingException if an error occurs when retrieving data.
     */
    private Review getAggregationWorksheet(Phase phase) throws PhaseHandlingException {
        // Locate the nearest backward Aggregation phase
        Phase aggPhase = PhasesHelper.locatePhase(phase, AGGREGATION, false,
                        true);
        Connection conn = null;

        try {
            conn = createConnection();
            // Search the aggregated review scorecard
            Review aggWorksheet = PhasesHelper.getAggregationWorksheet(conn,
                            getManagerHelper(), aggPhase.getId());

            if (aggWorksheet == null) {
                throw new PhaseHandlingException(
                                "aggregation worksheet does not exist.");
            }
            return aggWorksheet;
        } finally {
            PhasesHelper.closeConnection(conn);
        }
    }

    /**
     * Returns true if the specified resourceId is the author of atleast one
     * comment in the given array.
     *
     * @param comments comments array.
     * @param resourceId resource id.
     * @param commentType the type
     *
     * @return true if the specified resourceId is the author of atleast one
     *         comment in the given array.
     */
    private boolean doesCommentExist(Comment[] comments, long resourceId,
                    String commentType) {
        for (int i = 0; i < comments.length; i++) {
            Comment comment = comments[i];

            // the comment should have "Approved" or "Rejected" extra info
            String extraInfo = (String) comment.getExtraInfo();
            if (comment.getCommentType().getName().equals(commentType)
                            && (PhasesHelper.APPROVED.equalsIgnoreCase(extraInfo)
                                            || PhasesHelper.ACCEPTED.equalsIgnoreCase(extraInfo)
                                            || PhasesHelper.REJECTED.equalsIgnoreCase(extraInfo))) {
                if (comment.getAuthor() == resourceId) {
                    return true;
                }
            }
        }

        return false;
    }
}
