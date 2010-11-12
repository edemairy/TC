/*
 * Copyright (C) 2009-2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.shared.util.DBMS;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;
import com.topcoder.web.ejb.project.ProjectRoleTermsOfUse;
import com.topcoder.web.ejb.project.ProjectRoleTermsOfUseLocator;
import com.topcoder.web.ejb.user.UserTermsOfUse;
import com.topcoder.web.ejb.user.UserTermsOfUseLocator;

import javax.ejb.CreateException;
import javax.naming.NamingException;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a
 * phase can be executed and to add extra logic to execute a phase. It will be
 * used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection and email
 * sending. This class handle the Post-Mortem phase. If the input is of other
 * phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * The Post-Mortem phase can start as soon as the dependencies are met and can
 * stop when the following conditions met: - The dependencies are met; - The
 * post-mortem scorecards are committed. - At least the required number of
 * Post-Mortem Reviewer resources have filled in a scorecard
 * </p>
 *
 * <p>
 * Version 1.2 change: add the logic to send out email within enhanced template and role-options.
 * </p>
 *
 * <p>
 * Version 1.3 (Online Review End Of Project Analysis Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #allPostMortemReviewsDone(Phase)} method to use appropriate logic for searching for review
 *     scorecards tied to project but not to phase type.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.4 (Members Post-Mortem Reviews Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #perform(Phase, String)} method to add submitters and reviewers as Post-Mortem reviewers for
 *     project.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 *
 * @author argolite, waits, isv
 * @version 1.4
 * @since 1.1
 */
public class PostMortemPhaseHandler extends AbstractPhaseHandler {
    /**
     * The default namespace of PostMortemPhaseHandler.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.PostMortemPhaseHandler";

    /**
     * Constant for post-mortem reviewer role name.
     */
    private static final String POST_MORTEM_REVIEWER_ROLE_NAME = "Post-Mortem Reviewer";

    /**
     * The name for the post-mortem phase.
     */
    private static final String PHASE_TYPE_POST_MORTEM = "Post-Mortem";

    /**
     * <p>A <code>String</code> providing the reviewer role name.</p>
     *
     * @since 1.4
     */
    private static final String REVIEWER_ROLE_NAME = "Reviewer";

    /**
     * <p>A <code>String</code> array listing the names for roles for resources which are candidates for adding to
     * project as Post-Mortem reviewers when Post-Mortem phase starts.</p>
     *
     * @since 1.4
     */
    private static final String[] POST_MORTEM_REVIEWER_CANDIDATE_ROLE_NAMES = new String[] {
        PhasesHelper.SUBMITTER_ROLE_NAME, REVIEWER_ROLE_NAME,
        PhasesHelper.ACCURACY_REVIEWER_ROLE_NAME, PhasesHelper.FAILURE_REVIEWER_ROLE_NAME,
        PhasesHelper.STRESS_REVIEWER_ROLE_NAME};

    /**
     * <p>A <code>String</code> array listing the name for Post-Mortem Reviewer role.</p>
     *
     * @since 1.4
     */
    private static final String[] POST_MORTEM_REVIEWER_FILTER = new String[] {POST_MORTEM_REVIEWER_ROLE_NAME};

    /**
     * <p>A <code>String</code> providing the name for email property of the resource.</p>
     *
     * @since 1.4
     */
    private static final String EMAIL = "Email";

    /**
     * <p>A <code>String</code> providing the name for payment status property of the resource.</p>
     *
     * @since 1.4
     */
    private static final String PAYMENT_STATUS = "Payment Status";

    /**
     * <p>A <code>String</code> providing the name for registration date property of the resource.</p>
     *
     * @since 1.4
     */
    private static final String REGISTRATION_DATE = "Registration Date";

    /**
     * <p>A <code>String</code> providing the name for rating property of the resource.</p>
     *
     * @since 1.4
     */
    private static final String RATING = "Rating";

    /**
     * <p>A <code>Log</code> used for logging the events.</p>
     *
     * @since 1.4
     */
    private static final Log LOG = LogFactory.getLog(PostMortemPhaseHandler.class.getName());

    /**
     * Creates a PostMortemPhaseHandler with default namespace.
     *
     * @throws ConfigurationException if there is configuration error.
     */
    public PostMortemPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Creates a PostMortemPhaseHandler with the given namespace.
     *
     * @param namespace the namespace of PostMortemPhaseHandler.
     * @throws ConfigurationException if there is configuration error.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public PostMortemPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * <p>
     * Check if the input phase can be executed or not. This method will check
     * the phase status to see what will be executed. This method will be called
     * by canStart() and canEnd() methods of PhaseManager implementations in
     * Phase Management component.
     * </p>
     *
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase
     * can be started using the following conditions: The dependencies are met.
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be
     * stopped using the following conditions:
     * <ul>
     * <li>The dependencies are met</li>
     * <li>All post-mortem scorecards are committed.</li>
     * <li>At least the required number of Post-Mortem Reviewer resources have
     * filled in a scorecard (use the Reviewer Number phase criteria).</li>
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be
     * thrown.
     * </p>
     *
     * @param phase The input phase to check.
     * @return True if the input phase can be executed, false otherwise.
     * @throws PhaseNotSupportedException if the input phase type is not
     *         "Post-Mortem Review" type.
     * @throws PhaseHandlingException if there is any error occurred while
     *         processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public boolean canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, PHASE_TYPE_POST_MORTEM);

        // will throw exception if phase status is
        // neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        if (toStart) {
            // return true if all dependencies have stopped
            // and start time has been reached.
            return PhasesHelper.canPhaseStart(phase);
        } else {
            // Check phase dependencies
            boolean depsMeet = PhasesHelper.arePhaseDependenciesMet(phase, false);

            // Return true is dependencies are met and minimum number of reviews committed in time before phase
            // deadline.
            return depsMeet && PhasesHelper.reachedPhaseEndTime(phase) && allPostMortemReviewsDone(phase);
        }
    }

    /**
     * <p>Provides additional logic to execute a phase. If the phase starts then all submitters who have submitted for
     * project and all reviewers are assigned <code>Post-Mortem Reviewer</code> role for project (if not already) and
     * emails are sent on phase state transition.</p>
     *
     * @param phase the input phase.
     * @param operator the operator name.
     * @throws PhaseHandlingException if there is any error occurs.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        // perform parameters checking
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, PHASE_TYPE_POST_MORTEM);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        // send email now if it is going to start
        Map<String, Object> values = new HashMap<String, Object>();
        if (toStart) {
            ManagerHelper managerHelper = getManagerHelper();
            ResourceManager resourceManager = managerHelper.getResourceManager();
            DateFormat dateFormatter = new SimpleDateFormat("MM.dd.yyyy hh:mm a", Locale.US);

            Connection conn = null;
            try {
                // Locate the role for Post-Mortem Reviewer
                ResourceRole postMortemReviewerRole = null;
                ResourceRole[] resourceRoles = resourceManager.getAllResourceRoles();
                for (ResourceRole role : resourceRoles) {
                    if (role.getName().equals(POST_MORTEM_REVIEWER_ROLE_NAME)) {
                        postMortemReviewerRole = role;
                        break;
                    }
                }

                conn = createConnection();

                long projectId = phase.getProject().getId();

                // Get the list of existing Post-Mortem reviewers assigned to project so far
                Resource[] existingPostMortemResources
                    = PhasesHelper.searchProjectResourcesForRoleNames(managerHelper, conn, POST_MORTEM_REVIEWER_FILTER,
                                                                      projectId);
                // Add Submitters (who have submitted), reviewers as Post-Mortem Reviewers but only if they are not
                // assigned to project as Post-Mortem reviewers already and have all necessary terms of use accepted
                Resource[] candidatePostMortemResources
                    = PhasesHelper.searchProjectResourcesForRoleNames(managerHelper, conn,
                                                                      POST_MORTEM_REVIEWER_CANDIDATE_ROLE_NAMES,
                                                                      projectId);
                for (Resource resource : candidatePostMortemResources) {
                    boolean valid = false;
                    String roleName = resource.getResourceRole().getName();
                    if (PhasesHelper.SUBMITTER_ROLE_NAME.equalsIgnoreCase(roleName)) {
                        Long[] submissionIds = resource.getSubmissions();
                        if ((submissionIds != null) && (submissionIds.length > 0)) {
                            valid = true;
                        }
                    } else {
                        valid = true;
                    }
                    if (valid) {
                        String candidateId = (String) resource.getProperty(PhasesHelper.EXTERNAL_REFERENCE_ID);
                        boolean alreadyPostMortemReviewer = false;
                        for (Resource existingPostMortemReviewer : existingPostMortemResources) {
                            String existingId = (String) existingPostMortemReviewer.getProperty(
                                PhasesHelper.EXTERNAL_REFERENCE_ID);
                            if (existingId.equals(candidateId)) {
                                alreadyPostMortemReviewer = true;
                                break;
                            }
                        }
                        if (!alreadyPostMortemReviewer) {
                            long candidateUserId = Long.parseLong(candidateId);
                            String candidateUserHandle = (String) resource.getProperty(PhasesHelper.HANDLE);
                            boolean hasPendingTerms = hasPendingTermsOfUse(projectId, candidateUserId,
                                                                           postMortemReviewerRole.getId());
                            if (!hasPendingTerms) {
                                Resource newPostMortemReviewer = new Resource();
                                newPostMortemReviewer.setResourceRole(postMortemReviewerRole);
                                newPostMortemReviewer.setProject(projectId);
                                newPostMortemReviewer.setPhase(phase.getId());
                                newPostMortemReviewer.setProperty(PhasesHelper.EXTERNAL_REFERENCE_ID, candidateId);
                                newPostMortemReviewer.setProperty(PhasesHelper.HANDLE, candidateUserHandle);
                                newPostMortemReviewer.setProperty(EMAIL, resource.getProperty(EMAIL));
                                newPostMortemReviewer.setProperty(RATING, resource.getProperty(RATING));
                                newPostMortemReviewer.setProperty(PhasesHelper.PAYMENT_PROPERTY_KEY, "0.00");
                                newPostMortemReviewer.setProperty(PAYMENT_STATUS, "No");
                                newPostMortemReviewer.setProperty(REGISTRATION_DATE, dateFormatter.format(new Date()));

                                resourceManager.updateResource(newPostMortemReviewer, operator);
                            } else {
                                LOG.log(Level.WARN,  "Can not assign Post-Mortem Reviewer role to user "
                                                     + candidateUserHandle + "(ID: " + candidateId + ") "
                                                     + "for project " + projectId + " as user does not have accepted "
                                                     + "all necessary terms of use for project");
                            }
                        }
                    }
                }

                Resource[] postMortemResources
                    = PhasesHelper.searchProjectResourcesForRoleNames(managerHelper, conn, POST_MORTEM_REVIEWER_FILTER,
                                                                      projectId);

                //according to discussion here http://forums.topcoder.com/?module=Thread&threadID=659556&start=0
                //if the attribute is not set, default value would be 0
                int requiredRN = 0;
                if (phase.getAttribute(PhasesHelper.REVIEWER_NUMBER_PROPERTY) != null) {
                    requiredRN = PhasesHelper.getIntegerAttribute(phase, PhasesHelper.REVIEWER_NUMBER_PROPERTY);
                }
                values.put("N_REQUIRED_POST_MORTEM_REVIEWERS", requiredRN);
                values.put("N_POST_MORTEM_REVIEWERS", postMortemResources.length);
                values.put("NEED_POST_MORTEM_REVIEWERS", requiredRN <= postMortemResources.length ? 0 : 1);
            } catch (ResourcePersistenceException e) {
                throw new PhaseHandlingException("Failed to add new Post-Mortem Reviewer resource", e);
            } catch (RemoteException e) {
                throw new PhaseHandlingException("Failed to add new Post-Mortem Reviewer resource", e);
            } catch (CreateException e) {
                throw new PhaseHandlingException("Failed to add new Post-Mortem Reviewer resource", e);
            } catch (NamingException e) {
                throw new PhaseHandlingException("Failed to add new Post-Mortem Reviewer resource", e);
            } finally {
                PhasesHelper.closeConnection(conn);
            }
        }
        sendEmail(phase, values);
    }

    /**
     * Checks whether all post-mortem reviews are committed, and whether the
     * reviews number meets the requirement.
     *
     * @param phase the phase to check.
     * @return true if all post-mortem reviews are committed and meet
     *         requirement, false otherwise.
     * @throws PhaseHandlingException if there was an error retrieving data.
     */
    private boolean allPostMortemReviewsDone(Phase phase) throws PhaseHandlingException {
        Connection conn = null;

        try {
            // Search all post-mortem review scorecards for the current phase
            conn = createConnection();
            Review[] reviews
                = PhasesHelper.searchProjectReviewsForResourceRoles(conn, getManagerHelper(),
                                                                    phase.getProject().getId(),
                                                                    POST_MORTEM_REVIEWER_FILTER, null);

            // Count number of committed reviews
            int committedReviewsCount = 0;
            for (int i = 0; i < reviews.length; ++i) {
                if (reviews[i].isCommitted()) {
                    committedReviewsCount++;
                }
            }

            // Deny to close phase if minimum number of reviews is not committed
            if (phase.getAttribute(PhasesHelper.REVIEWER_NUMBER_PROPERTY) != null) {
                int reviewerNum = PhasesHelper.getIntegerAttribute(phase,
                    PhasesHelper.REVIEWER_NUMBER_PROPERTY);
                if (committedReviewsCount < reviewerNum) {
                    return false;
                }
            }

            return true;
        } catch (SQLException e) {
            throw new PhaseHandlingException("Error retrieving data from persistence", e);
        } finally {
            PhasesHelper.closeConnection(conn);
        }
    }

    /**
     * <p>Validates that specified user which is going to be assigned specified role for specified project has accepted
     * all terms of use set for specified role in context of the specified project.</p>
     *
     * @param projectId a <code>long</code> providing the project ID.
     * @param userId a <code>long</code> providing the user ID.
     * @param roleId a <code>long</code> providing the role ID.
     * @return a <code>true</code> if there are terms of user which are not yet accepted by the specified user or
     *         <code>false</code> if all necessary terms of use are accepted.
     * @throws NamingException if any errors occur during EJB lookup.
     * @throws RemoteException if any errors occur during EJB remote invocation.
     * @throws CreateException if any errors occur during EJB creation.
     * @since 1.4
     */
    private boolean hasPendingTermsOfUse(long projectId, long userId, long roleId)
        throws CreateException, NamingException, RemoteException {
        // get remote services
        ProjectRoleTermsOfUse projectRoleTermsOfUse = ProjectRoleTermsOfUseLocator.getService();
        UserTermsOfUse userTermsOfUse = UserTermsOfUseLocator.getService();

        List<Long>[] necessaryTerms = projectRoleTermsOfUse.getTermsOfUse(
            (int) projectId, new int[] {new Long(roleId).intValue()}, DBMS.COMMON_OLTP_DATASOURCE_NAME);

        for (int j = 0; j < necessaryTerms.length; j++) {
            if (necessaryTerms[j] != null) {
                for (Long termsId : necessaryTerms[j]) {
                    if (!userTermsOfUse.hasTermsOfUse(userId, termsId, DBMS.COMMON_OLTP_DATASOURCE_NAME)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}