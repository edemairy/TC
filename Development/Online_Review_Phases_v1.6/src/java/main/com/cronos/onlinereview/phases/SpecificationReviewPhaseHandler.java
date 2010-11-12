/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.cronos.onlinereview.phases.logging.LogMessage;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * This class implements <code>PhaseHandler</code> interface to provide methods to check
 * if a phase can be executed and to add extra logic to execute a phase. It will be used
 * by Phase Management component. It is configurable using an input namespace. The
 * configurable parameters include database connection and email sending parameters. This
 * class handles the specification review phase. If the input is of other phase types,
 * <code>PhaseNotSupportedException</code> will be thrown.
 * </p>
 * <p>
 * The specification review phase can start when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>Phase start date/time is reached (if specified)</li>
 * <li>If one active submission with &quot;Specification Submission&quot; type exists</li>
 * </ul>
 * </p>
 * <p>
 * The specification review phase can stop when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>If the specification review is done by Specification Reviewer</li>
 * </ul>
 * </p>
 * <p>
 * The additional logic for executing this phase is: when specification Review phase is
 * stopping, if the specification review is rejected, another specification
 * submission/review cycle is inserted.
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 *
 * @author saarixx, myxgyy
 * @version 1.4
 * @since 1.4
 */
public class SpecificationReviewPhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.SpecificationReviewPhaseHandler";

    /**
     * Represents the logger for this class. Is initialized during class loading and never
     * changed after that.
     */
    private static final Log LOG = LogFactory.getLog(SpecificationReviewPhaseHandler.class.getName());

    /**
     * Constant for Specification Review phase type.
     */
    private static final String PHASE_TYPE_SPECIFICATION_REVIEW = "Specification Review";

    /**
     * Constant for Specification Reviewer role type.
     */
    private static final String ROLE_TYPE_SPECIFICATION_REVIEWER = "Specification Reviewer";

    /**
     * Create a new instance of SpecificationReviewPhaseHandler using the default
     * namespace for loading configuration settings.
     *
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     */
    public SpecificationReviewPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of SpecificationReviewPhaseHandler using the given namespace
     * for loading configuration settings.
     *
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     * @throws IllegalArgumentException
     *             if the input is null or empty string.
     * @param namespace
     *            the namespace to load configuration settings from.
     */
    public SpecificationReviewPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Check if the input phase can be executed or not. This method will check the phase
     * status to see what will be executed. This method will be called by canStart() and
     * canEnd() methods of PhaseManager implementations in Phase Management component.
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase can be
     * started using the following conditions: the dependencies are met, Phase start
     * date/time is reached (if specified) and If one active submission with
     * &quot;Specification Submission&quot; type exists.
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped
     * using the following conditions: The dependencies are met and the specification
     * review is done by Specification Reviewer
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     *
     * @param phase
     *            The input phase to check.
     * @return True if the input phase can be executed, false otherwise.
     * @throws PhaseNotSupportedException
     *             if the input phase type is not "Specification Review" type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input is null.
     */
    public boolean canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, PHASE_TYPE_SPECIFICATION_REVIEW);

        // will throw exception if phase status is
        // neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        if (toStart) {
            if (!PhasesHelper.canPhaseStart(phase)) {
                return false;
            }

            // can start if one active submission with "Specification Submission" type
            // exists
            Connection conn = null;
            try {
                conn = createConnection();
                return PhasesHelper.hasOneSpecificationSubmission(phase, getManagerHelper(),
                    createConnection(), LOG) != null;
            } finally {
                PhasesHelper.closeConnection(conn);
            }
        } else {
            // Check phase dependencies
            boolean depsMeet = PhasesHelper.arePhaseDependenciesMet(phase, false);

            return depsMeet && isSpecificationReviewCommitted(phase);
        }
    }

    /**
     * Checks whether the specification review is committed.
     *
     * @param phase
     *            the current phase
     * @return true if the specification review is committed, false otherwise.
     * @throws PhaseHandlingException
     *             if any error occurred.
     */
    private boolean isSpecificationReviewCommitted(Phase phase) throws PhaseHandlingException {
        Connection conn = null;

        try {
            conn = createConnection();

            Submission submission = PhasesHelper.hasOneSpecificationSubmission(phase,
                getManagerHelper(), conn, LOG);

            if (null == submission) {
                return false;
            }

            Review review = getSpecificationReview(phase, submission, conn);

            return (review == null) ? false : review.isCommitted();
        } finally {
            PhasesHelper.closeConnection(conn);
        }
    }

    /**
     * Searches the Specification Review worksheet.
     *
     * @param phase
     *            the phase.
     * @param submission
     *            the submission.
     * @param connection
     *            the database connection.
     * @return the review or null if no review worksheet exists.
     * @throws PhaseHandlingException
     *             if there is any error occurred while searching reviews.
     */
    private Review getSpecificationReview(Phase phase, Submission submission, Connection connection)
        throws PhaseHandlingException {
        Review[] reviews = PhasesHelper.searchReviewsForResourceRoles(connection,
            getManagerHelper(), phase.getId(), new String[] {ROLE_TYPE_SPECIFICATION_REVIEWER},
            submission.getId());

        if (reviews.length > 1) {
            LOG.log(Level.ERROR, "Multiple specification reviews exist");
            throw new PhaseHandlingException("Illegal data : Multiple specification reviews.");
        }

        return (reviews.length == 0) ? null : reviews[0];
    }

    /**
     * Provides additional logic to execute a phase. This method will be called by start()
     * and end() methods of PhaseManager implementations in Phase Management component.
     * This method can send email to a group of users associated with timeline
     * notification for the project. The email can be send on start phase or end phase
     * base on configuration settings.
     * <p>
     * If the input phase status is Scheduled, then it will get the number of
     * specification reviewers assigned.
     * </p>
     * <p>
     * If the input phase status is Open, then it will perform the following additional
     * logic to stop the phase: if the specification review is rejected, another
     * specification submission/review cycle is inserted.
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     *
     * @param phase
     *            The input phase to check.
     * @param operator
     *            The operator that execute the phase.
     * @throws PhaseNotSupportedException
     *             if the input phase type is not &quot;Specification Review&quot; type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, PHASE_TYPE_SPECIFICATION_REVIEW);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        Map<String, Object> values = new HashMap<String, Object>();

        if (toStart) {
            values.put("N_SPECIFICATION_REVIEWERS", getSpecificationReviewerNumber(phase));
        } else {
            values.put("RESULT", checkSpecificationReview(phase, operator) ? "rejected" : "approved");
        }

        sendEmail(phase, values);
    }

    /**
     * <p>
     * Get the specification reviewer number of the project.
     * </p>
     *
     * @param phase
     *            the current Phase
     * @return the number of specification reviewer
     * @throws PhaseHandlingException
     *             if any error occurs
     */
    private int getSpecificationReviewerNumber(Phase phase) throws PhaseHandlingException {
        Connection conn = null;

        try {
            conn = createConnection();

            return PhasesHelper.searchResourcesForRoleNames(getManagerHelper(), conn,
                new String[] {ROLE_TYPE_SPECIFICATION_REVIEWER}, phase.getId()).length;
        } finally {
            PhasesHelper.closeConnection(conn);
        }
    }

    /**
     * This method is called from perform method when the phase is stopping. It checks if
     * the specification review is rejected, and inserts another specification
     * submission/review cycle.
     *
     * @param phase
     *            phase instance.
     * @param operator
     *            operator name
     * @return if pass the specification review of not
     * @throws PhaseHandlingException
     *             if an error occurs when retrieving/saving data.
     */
    private boolean checkSpecificationReview(Phase phase, String operator)
        throws PhaseHandlingException {
        Connection conn = null;
        ManagerHelper managerHelper = getManagerHelper();
        boolean rejected = false;

        try {
            conn = createConnection();

            Submission submission = PhasesHelper.hasOneSpecificationSubmission(phase,
                managerHelper, conn, LOG);

            if (null == submission) {
                LOG.log(Level.ERROR, "The specification submission does not exist.");
                throw new PhaseHandlingException("No specification submission exists.");
            }

            rejected = wasSpecificationReviewRejected(managerHelper, conn, submission, phase);

            if (rejected) {
                // update specification submission status
                try {
                    // Lookup submission status id for "Failed Review" status
                    SubmissionStatus status = PhasesHelper.getSubmissionStatus(managerHelper
                        .getUploadManager(), "Failed Review");
                    submission.setSubmissionStatus(status);
                    managerHelper.getUploadManager().updateSubmission(submission, operator);
                } catch (UploadPersistenceException e) {
                    LOG.log(Level.ERROR, new LogMessage(new Long(phase.getId()), null,
                        "Persistence layer error when updating submission status.", e));
                    throw new PhaseHandlingException("Fails to update submission.", e);
                }

                // use helper method to insert Specification submission/Specification
                // review phases
                int currentPhaseIndex = PhasesHelper.insertSpecSubmissionSpecReview(phase,
                    managerHelper.getPhaseManager(), operator);

                long specReviewPhaseId = phase.getProject().getAllPhases()[currentPhaseIndex + 2]
                    .getId();

                PhasesHelper.createAggregatorOrFinalReviewer(phase, managerHelper, conn,
                    ROLE_TYPE_SPECIFICATION_REVIEWER, specReviewPhaseId, operator);
            }
        } finally {
            PhasesHelper.closeConnection(conn);
        }

        return rejected;
    }

    /**
     * Finds out if the specification review is rejected. Note if the specification review
     * is found being rejected, the extra info will be reset here.
     *
     * @param managerHelper
     *            the manager helper.
     * @param connection
     *            the database connection.
     * @param submission
     *            the submission.
     * @param phase
     *            the phase.
     * @return true if the specification review is rejected, false otherwise.
     * @throws PhaseHandlingException
     *             if any error occurred.
     */
    private boolean wasSpecificationReviewRejected(ManagerHelper managerHelper,
        Connection connection, Submission submission, Phase phase) throws PhaseHandlingException {
        Review review = getSpecificationReview(phase, submission, connection);

        if (null == review) {
            LOG.log(Level.ERROR, "The review for the specification does not exist.");
            throw new PhaseHandlingException("The review for the specification does not exist.");
        }

        if (!review.isCommitted()) {
            LOG.log(Level.ERROR, "The review is not committed.");
            throw new PhaseHandlingException("The review is not committed.");
        }

        Comment[] comments = review.getAllComments();

        Comment comment = null;
        for (int i = 0; i < comments.length; i++) {
            if ("Specification Review Comment".equals(comments[i].getCommentType().getName())) {
                comment = comments[i];
                // use the first found one without checking how many of them exist
                break;
            }
        }

        if (null == comment) {
            LOG.log(Level.ERROR, "No comment with [Specification Review Comment] type.");
            throw new PhaseHandlingException("No comment with [Specification Review Comment] type.");
        }

        String value = (String) comment.getExtraInfo();
        if (PhasesHelper.REJECTED.equalsIgnoreCase(value)) {
            // when the submission is rejected, we need to insert another specification
            // submission/specification review cycle,
            // Extra info for Specification Review Comment must be cleared.
            comment.resetExtraInfo();
            return true;
        } else if (!PhasesHelper.APPROVED.equalsIgnoreCase(value)
            && !PhasesHelper.ACCEPTED.equalsIgnoreCase(value)) {
            LOG.log(Level.ERROR, "Invalid comment[" + value + "],"
                + " comment can either be Approved, Accepted or Rejected.");
            throw new PhaseHandlingException(
                "Comment can either be Approved, Accepted or Rejected.");
        }

        return false;
    }
}