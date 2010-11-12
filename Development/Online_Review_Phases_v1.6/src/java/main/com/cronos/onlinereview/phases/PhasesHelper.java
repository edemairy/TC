/*
 * Copyright (C) 2004 - 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.cronos.onlinereview.autoscreening.management.ScreeningTask;
import com.cronos.onlinereview.autoscreening.management.ScreeningTaskDoesNotExistException;
import com.cronos.onlinereview.phases.logging.LogMessage;
import com.cronos.onlinereview.phases.lookup.ResourceRoleLookupUtility;
import com.cronos.onlinereview.phases.lookup.SubmissionStatusLookupUtility;
import com.cronos.onlinereview.phases.lookup.SubmissionTypeLookupUtility;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.link.ProjectLink;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.resource.search.ResourceRoleFilterBuilder;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.Scorecard;

import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;

import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * A class having helper methods to perform argument validation and other phase related
 * methods used by the PhaseHandler implementations.
 * </p>
 * <p>
 * Version 1.1 (Appeals Early Completion Release Assembly 1.0) Change notes:
 * <ol>
 * <li>Added support for Early Appeals Completion.</li>
 * <li>Removed aggregator/final reviewer payment duplication.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.2 (Contest Dependency Automation Release Assembly 1.0) Change notes:
 * <ol>
 * <li>Added a method for checking if all projects which requested project depends on are
 * completed to the project could start.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.3 (Online Review End Of Project Analysis Release Assembly 1.0) Change notes:
 * <ol>
 * <li>Updated {@link #insertPostMortemPhase(Project, Phase, ManagerHelper, String)}
 * method to fix the bugs with creation of <code>Post-Mortem</code> phase.</li>
 * <li>Added {@link #insertApprovalPhase(Project, Phase, ManagerHelper, String)} method.</li>
 * <li>Added {@link #searchProjectResourcesForRoleNames(ManagerHelper, Connection,
 * String[], long)} method.</li>
 * <li>Added {@link #getApprovalPhaseReviews(Review[], Phase)} method.</li>
 * <li>Added {@link #searchProjectReviewsForResourceRoles(Connection, ManagerHelper,
 * long, String[], Long)} method to handle Post-Mortem phase correctly.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.4 (Member Post-Mortem Review Assembly 1.0) Change notes:
 * <ol>
 * <li>Updated {@link #insertPostMortemPhase(Project, Phase, ManagerHelper, String)}
 * method to create Post-Mortem phase only if there is respective flag set in project
 * properties.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.4 Change notes:
 * <ol>
 * <li>Updated {@link #getScreeningTasks(ManagerHelper, Connection, Phase)} method to add
 * function to get submission with "Contest Submission" submission type.</li>
 * <li>Updated {@link #searchActiveSubmissions(UploadManager, Connection, long, String)}
 * method to add function to search all active submissions with specific submission type.</li>
 * <li>Added {@link #isFirstPhase(Phase)} method.</li>
 * <li>Added {@link #insertSpecSubmissionSpecReview(Phase, PhaseManager, String)} method.</li>
 * <li>Added {@link #hasOneSpecificationSubmission(long, ManagerHelper, Connection, Log)}
 * method.</li>
 * <li>Added some constants.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.4.3 Change notes:
 * <ol>
 * <li>Phase attributes are copied for newly created Specification Review phase.</li>
 * </ol>
 * </p>
 *
 * @author tuenm, bose_java, pulky, aroglite, waits, isv, saarixx, myxgyy
 * @version 1.4.3
 * @since 1.0
 */
final class PhasesHelper {
    /**
     * Constant for reviewer role names to be used when searching for reviewer resources
     * and review scorecards.
     */
    static final String[] REVIEWER_ROLE_NAMES = new String[] {"Reviewer", "Accuracy Reviewer",
        "Failure Reviewer", "Stress Reviewer"};

    /**
     * One The property name of resource.
     */
    static final String REVIEWER_NUMBER_PROPERTY = "Reviewer Number";

    /**
     * The property name external reference id constant.
     */
    static final String EXTERNAL_REFERENCE_ID = "External Reference ID";

    /**
     * This constant stores Payment property key.
     *
     * @since 1.1
     */
    static final String PAYMENT_PROPERTY_KEY = "Payment";

    /**
     * This constant stores Payment Status property key.
     *
     * @since 1.4
     */
    static final String PAYMENT_STATUS_PROPERTY_KEY = "Payment Status";

    /**
     * This constant stores Submitter role name.
     *
     * @since 1.1
     */
    static final String SUBMITTER_ROLE_NAME = "Submitter";


    /**
     * constant for &quot;Scheduled&quot; phase status.
     */
    static final String PHASE_STATUS_SCHEDULED = "Scheduled";

    /**
     * <p>
     * A <code>String</code> providing the final reviewer role name.
     * </p>
     *
     * @since 1.4
     */
    static final String FINAL_REVIEWER_ROLE_NAME = "Final Reviewer";

    /**
     * <p>
     * A <code>String</code> providing the final reveiw phase.
     * </p>
     *
     * @since 1.4
     */
    static final String PHASE_FINAL_REVIEW = "Final Review";

    /**
     * <p>
     * A <code>String</code> providing the accuracy reviewer role name.
     * </p>
     *
     * @since 1.4
     */
    static final String ACCURACY_REVIEWER_ROLE_NAME = "Accuracy Reviewer";

    /**
     * <p>
     * A <code>String</code> providing the failure reviewer role name.
     * </p>
     *
     * @since 1.4
     */
    static final String FAILURE_REVIEWER_ROLE_NAME = "Failure Reviewer";

    /**
     * <p>
     * A <code>String</code> providing the stress reviewer role name.
     * </p>
     *
     * @since 1.4
     */
    static final String STRESS_REVIEWER_ROLE_NAME = "Stress Reviewer";

    /**
     * <p>
     * A <code>String</code> providing the name for handle property of the resource.
     * </p>
     *
     * @since 1.4
     */
    static final String HANDLE = "Handle";

    /**
     * Constant for &quot;Contest Submission&quot; submission type.
     *
     * @since 1.4
     */
    static final String CONTEST_SUBMISSION_TYPE = "Contest Submission";

    /**
     * Constant for &quot;Approved&quot; comment extra info.
     *
     * @since 1.4
     */
    static final String APPROVED = "Approved";

    /**
     * Constant for &quot;Accepted&quot; comment extra info.
     *
     * @since 1.4
     */
    static final String ACCEPTED = "Accepted";

    /**
     * Constant for &quot;Rejected&quot; comment extra info.
     *
     * @since 1.4
     */
    static final String REJECTED = "Rejected";

    /**
     * Constant for &quot;Review&quot; phase.
     *
     * @since 1.4
     */
    static final String REVIEW = "Review";

    /**
     * This constant for one hour.
     *
     * @since 1.4
     */
    private static final long HOUR = 3600 * 1000;

    /**
     * This constant for three.
     *
     * @since 1.4
     */
    private static final int THREE = 3;

    /**
     * This constant for seven.
     *
     * @since 1.4
     */
    private static final int SEVEN = 7;

    /**
     * This constant stores Appeals Completed Early flag property key.
     *
     * @since 1.1
     */
    private static final String APPEALS_COMPLETED_EARLY_PROPERTY_KEY = "Appeals Completed Early";

    /**
     * This constant stores &quot;Yes&quot; value for Appeals Completed Early flag property.
     *
     * @since 1.1
     */
    private static final String YES_VALUE = "Yes";

    /**
     * constant for &quot;Open&quot; phase status.
     */
    private static final String PHASE_STATUS_OPEN = "Open";

    /**
     * constant for &quot;Closed&quot; phase status.
     */
    private static final String PHASE_STATUS_CLOSED = "Closed";

    /**
     * an array of comment types which denote that a comment is a reviewer comment.
     */
    private static final String[] REVIEWER_COMMENT_TYPES = new String[] {"Comment", "Required",
        "Recommended"};

    /**
     * private to prevent instantiation.
     */
    private PhasesHelper() {
        // do nothing.
    }

    /**
     * Checks whether the given Object is null and throws IllegalArgumentException if yes.
     *
     * @param arg
     *            the argument to check
     * @param name
     *            the name of the argument
     * @throws IllegalArgumentException
     *             if the given Object is null
     */
    static void checkNull(Object arg, String name) {
        if (arg == null) {
            throw new IllegalArgumentException(name + " should not be null.");
        }
    }

    /**
     * Checks whether the given String is null or empty.
     *
     * @param arg
     *            the String to check
     * @param name
     *            the name of the argument
     * @throws IllegalArgumentException
     *             if the given string is null or empty
     */
    static void checkString(String arg, String name) {
        checkNull(arg, name);

        if (arg.trim().length() == 0) {
            throw new IllegalArgumentException(name + " should not be empty.");
        }
    }

    /**
     * Returns true if given string is either null or empty, false otherwise.
     *
     * @param str
     *            string to check.
     * @return true if given string is either null or empty, false otherwise.
     */
    static boolean isStringNullOrEmpty(String str) {
        return ((str == null) || (str.trim().length() == 0));
    }

    /**
     * Checks if the map is valid. The key should not be null/empty, the value should not
     * be null.
     *
     * @param map
     *            the map to verify
     * @throws IllegalArgumentException
     *             if the map is invalid
     */
    static void checkValuesMap(Map<String, Object> map) {
        checkNull(map, "Values map");

        for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Object> entry = it.next();
            checkString(entry.getKey(), "key in values map");
            checkNull(entry.getValue(), "value in values map");
        }
    }

    /**
     * Helper method to retrieve a property value from given configuration namespace. If
     * the isRequired flag is true and if the property does not exist, then this method
     * throws a ConfigurationException.
     *
     * @param namespace
     *            configuration namespace to use.
     * @param propertyName
     *            name of the property.
     * @param isRequired
     *            whether property is required.
     * @return value for given property name.
     * @throws ConfigurationException
     *             if namespace is unknown or if required property does not exist.
     */
    static String getPropertyValue(String namespace, String propertyName, boolean isRequired)
        throws ConfigurationException {
        ConfigManager configManager = ConfigManager.getInstance();

        try {
            String value = configManager.getString(namespace, propertyName);

            if (isRequired && isStringNullOrEmpty(value)) {
                throw new ConfigurationException("Property '" + propertyName + "' must have a value.");
            }

            return value;
        } catch (UnknownNamespaceException ex) {
            throw new ConfigurationException("Namespace '" + namespace + "' does not exist.", ex);
        }
    }

    /**
     * Returns true if the property by the given name exists in the namespace, false
     * otherwise.
     *
     * @param namespace
     *            configuration namespace to use.
     * @param propertyName
     *            name of the property.
     * @return true if the property by the given name exists in the namespace, false
     *         otherwise.
     * @throws ConfigurationException
     *             if namespace is unknown.
     */
    static boolean doesPropertyExist(String namespace, String propertyName)
        throws ConfigurationException {
        try {
            ConfigManager configManager = ConfigManager.getInstance();
            Enumeration<?> propNames = configManager.getPropertyNames(namespace);

            while (propNames.hasMoreElements()) {
                if (propNames.nextElement().equals(propertyName)) {
                    return true;
                }
            }

            return false;
        } catch (UnknownNamespaceException e) {
            throw new ConfigurationException("Namespace '" + namespace + "' does not exist.", e);
        }
    }

    /**
     * Returns true if the property by the given name exists in the namespace, false
     * otherwise.
     *
     * @param namespace
     *            configuration namespace to use.
     * @param propertyName
     *            name of the property.
     * @return true if the property by the given name exists in the namespace, false
     *         otherwise.
     * @throws ConfigurationException
     *             if namespace is unknown.
     */
    static boolean doesPropertyObjectExist(String namespace, String propertyName)
        throws ConfigurationException {
        try {
            return ConfigManager.getInstance().getPropertyObject(namespace, propertyName) != null;
        } catch (UnknownNamespaceException e) {
            throw new ConfigurationException("Namespace '" + namespace + "' does not exist.", e);
        }
    }

    /**
     * A helper method to create an instance of DBConnectionFactory. This method retrieves
     * the value for connection factory namespace from the given property name and
     * namespace and uses the same to create an instance of DBConnectionFactoryImpl.
     *
     * @param namespace
     *            configuration namespace to use.
     * @param connFactoryNSPropName
     *            name of property which holds connection factory namespace value.
     * @return DBConnectionFactory instance.
     * @throws ConfigurationException
     *             if property is missing or if there was an error when instantiating
     *             DBConnectionFactory.
     */
    @SuppressWarnings("deprecation")
    static DBConnectionFactory createDBConnectionFactory(String namespace, String connFactoryNSPropName)
        throws ConfigurationException {
        String connectionFactoryNS = getPropertyValue(namespace, connFactoryNSPropName, true);

        try {
            return new DBConnectionFactoryImpl(connectionFactoryNS);
        } catch (UnknownConnectionException ex) {
            throw new ConfigurationException("Could not instantiate DBConnectionFactoryImpl", ex);
        } catch (com.topcoder.db.connectionfactory.ConfigurationException ex) {
            throw new ConfigurationException("Could not instantiate DBConnectionFactoryImpl", ex);
        }
    }

    /**
     * Verifies that the phase is of desired type. Throws PhaseNotSupportedException if
     * not.
     *
     * @param phase
     *            phase to check.
     * @param type
     *            desired phase type.
     * @throws PhaseNotSupportedException
     *             if phase is not of desired type.
     */
    static void checkPhaseType(Phase phase, String type) throws PhaseNotSupportedException {
        String givenPhaseType = phase.getPhaseType().getName();

        if (!type.equals(givenPhaseType)) {
            throw new PhaseNotSupportedException("Phase must be of type " + type + ". It is of type "
                + givenPhaseType);
        }
    }

    /**
     * Returns true if phase status is &quot;Scheduled&quot;, false if status is &quot;Open&quot; and throws
     * PhaseHandlingException for any other status value.
     *
     * @param phaseStatus
     *            the phase status.
     * @return true if phase status is &quot;Scheduled&quot;, false if status is &quot;Open&quot;.
     * @throws PhaseHandlingException
     *             if phase status is neither &quot;Scheduled&quot; nor &quot;Open&quot;.
     */
    static boolean checkPhaseStatus(PhaseStatus phaseStatus) throws PhaseHandlingException {
        checkNull(phaseStatus, "phaseStatus");

        if (isPhaseToStart(phaseStatus)) {
            return true;
        } else if (isPhaseToEnd(phaseStatus)) {
            return false;
        } else {
            throw new PhaseHandlingException("Phase status '" + phaseStatus.getName()
                + "' is not valid.");
        }
    }

    /**
     * Returns whether the phase is to end or not by checking if status is &quot;Scheduled&quot;.
     *
     * @param status
     *            the phase status.
     * @return true if status is &quot;Scheduled&quot;, false otherwise.
     */
    static boolean isPhaseToStart(PhaseStatus status) {
        return (PHASE_STATUS_SCHEDULED.equals(status.getName()));
    }

    /**
     * Returns whether the phase is to end or not by checking if status is &quot;Open&quot;.
     *
     * @param status
     *            the phase status.
     * @return true if status is &quot;Open&quot;, false otherwise.
     */
    static boolean isPhaseToEnd(PhaseStatus status) {
        return isPhaseOpen(status);
    }

    /**
     * Returns if phase is closed, i.e. has status &quot;Closed&quot;.
     *
     * @param status
     *            the phase status.
     * @return true if phase status is &quot;Closed&quot;, false otherwise.
     */
    static boolean isPhaseClosed(PhaseStatus status) {
        return (PHASE_STATUS_CLOSED.equals(status.getName()));
    }

    /**
     * Returns if phase has started, i.e. has status &quot;Open&quot;.
     *
     * @param status
     *            the phase status.
     * @return true if phase status is &quot;Closed&quot;, false otherwise.
     */
    static boolean isPhaseOpen(PhaseStatus status) {
        return (PHASE_STATUS_OPEN.equals(status.getName()));
    }

    /**
     * Returns true if all the dependencies of the given phase have started/stopped based
     * on the type of dependency, or if the phase has no dependencies.
     * <p>
     * Change in version 1.4:<br/ > If phase B is configured to start when phase A
     * starts, if the phase A is already closed, phase B should start.<br/ > If phase B
     * is configured to end when phase A starts. It should end if the phase A is already
     * closed.
     * </p>
     *
     * @param phase
     *            the phase to check.
     * @param bPhaseStarting
     *            true if phase is starting, false if phase is ending.
     * @return true if all the dependencies of the given phase have stopped or if the
     *         phase has no dependencies.
     */
    static boolean arePhaseDependenciesMet(Phase phase, boolean bPhaseStarting) {
        Dependency[] dependencies = phase.getAllDependencies();

        if ((dependencies == null) || (dependencies.length == 0)) {
            return true;
        }

        for (int i = 0; i < dependencies.length; i++) {
            // get the dependency phase.
            Phase dependency = dependencies[i].getDependency();

            if (bPhaseStarting) {
                if (dependencies[i].isDependencyStart() && dependencies[i].isDependentStart()) {
                    // S2S dependencies should be started
                    // change in version 1.4
                    // If phase B is configured to start when phase A starts, if the phase
                    // A is already closed,
                    // phase B should start in this case
                    if (!(isPhaseOpen(dependency.getPhaseStatus()) || isPhaseClosed(dependency
                        .getPhaseStatus()))) {
                        return false;
                    }
                } else if (!dependencies[i].isDependencyStart() && dependencies[i].isDependentStart()) {
                    // S2F dependencies should be closed
                    if (!isPhaseClosed(dependency.getPhaseStatus())) {
                        return false;
                    }
                }
            } else {
                if (dependencies[i].isDependencyStart() && !dependencies[i].isDependentStart()) {
                    // F2S dependencies should be started
                    // change in version 1.4
                    // If phase B is configured to end when phase A starts. It should end
                    // if the phase A is
                    // already closed.
                    if (!(isPhaseOpen(dependency.getPhaseStatus()) || isPhaseClosed(dependency
                        .getPhaseStatus()))) {
                        return false;
                    }
                } else if (!dependencies[i].isDependencyStart() && !dependencies[i].isDependentStart()) {
                    // F2F dependencies should be closed
                    if (!isPhaseClosed(dependency.getPhaseStatus())) {
                        return false;
                    }
                }
            }
        }
        // all are met.
        return true;
    }

    /**
     * Returns true if current time is later or equal to the start time of the given
     * phase. This will return true in case phase.calcStartDate() returns null.
     *
     * @param phase
     *            the phase to check.
     * @return true if current time is later or equal to the start time of the given
     *         phase.
     */
    static boolean reachedPhaseStartTime(Phase phase) {
        Date startDate = phase.calcStartDate();

        if (startDate == null) {
            return true;
        } else {
            return (!new Date().before(startDate));
        }
    }

    /**
     * Returns true if current time is later or equal to the end time of the given phase.
     *
     * @param phase
     *            the phase to check.
     * @return true if current time is later or equal to the end time of the given phase.
     */
    static boolean reachedPhaseEndTime(Phase phase) {
        return (!new Date().before(phase.calcEndDate()));
    }

    /**
     * This method is used to check if a phase can start. It checks for following:<br/ >
     * 1. if phase dependencies have been met.<br/ > 2. if start time has been reached.<br/ >
     * The method will return true only if both the conditions are true, false otherwise.
     *
     * @param phase
     *            the phase instance to start.
     * @return true if a phase can start, false otherwise.
     */
    static boolean canPhaseStart(Phase phase) {
        return (PhasesHelper.arePhaseDependenciesMet(phase, true) && PhasesHelper
            .reachedPhaseStartTime(phase));
    }

    /**
     * Helper method to close the connection. Throws PhaseHandlingException if connection
     * could not be closed.
     *
     * @param conn
     *            connection to close.
     * @throws PhaseHandlingException
     *             if connection couldnot be closed.
     */
    static void closeConnection(Connection conn) throws PhaseHandlingException {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                throw new PhaseHandlingException("Could not close connection", ex);
            }
        }
    }

    /**
     * Helper method to find a backward phase or forward phase from a given phase with
     * given phase type.
     *
     * @param phase
     *            phase to search from.
     * @param phaseType
     *            phase type to search.
     * @param forward
     *            true to search forwards, false to search backwards.
     * @param required
     *            whether the target phase is required.
     * @return nearest backward or forward phase.
     * @throws PhaseHandlingException
     *             if no such phase exists.
     */
    static Phase locatePhase(Phase phase, String phaseType, boolean forward, boolean required)
        throws PhaseHandlingException {
        // get all phases for the project
        Phase[] phases = phase.getProject().getAllPhases();
        int currentPhaseIndex = 0;

        for (int i = 0; i < phases.length; i++) {
            if (phase.getId() == phases[i].getId()) {
                currentPhaseIndex = i;

                break;
            }
        }

        if (forward) {
            // get the next phase with desired type
            for (int i = currentPhaseIndex + 1; i < phases.length; i++) {
                if (phaseType.equals(phases[i].getPhaseType().getName())) {
                    return phases[i];
                }
            }
        } else {
            // get the previous phase with desired type
            for (int i = currentPhaseIndex - 1; i >= 0; i--) {
                if (phaseType.equals(phases[i].getPhaseType().getName())) {
                    return phases[i];
                }
            }
        }

        // could not find phase with desired type...
        if (required) {
            throw new PhaseHandlingException("Could not find nearest phase of type " + phaseType);
        } else {
            return null;
        }
    }

    /**
     * Returns all the reviews for a phase based on resource role names.
     *
     * @param conn
     *            Connection to use to lookup resource role id.
     * @param managerHelper
     *            ManagerHelper instance.
     * @param phaseId
     *            phase id to be used as filter.
     * @param resourceRoleNames
     *            resource role names to be used as filter.
     * @param submissionId
     *            submission id to be used as filter when searching for reviews.
     * @return Review[] which match filter conditions.
     * @throws PhaseHandlingException
     *             if there was an error during retrieval.
     */
    static Review[] searchReviewsForResourceRoles(Connection conn, ManagerHelper managerHelper,
        long phaseId, String[] resourceRoleNames, Long submissionId) throws PhaseHandlingException {
        Resource[] reviewers = searchResourcesForRoleNames(managerHelper, conn, resourceRoleNames,
            phaseId);
        if (reviewers.length == 0) {
            return new Review[0];
        }

        try {
            // create reviewer ids array
            Long[] reviewerIds = new Long[reviewers.length];

            for (int i = 0; i < reviewers.length; i++) {
                reviewerIds[i] = new Long(reviewers[i].getId());
            }

            Filter reviewFilter = new InFilter("reviewer", Arrays.asList(reviewerIds));
            Filter fullReviewFilter = reviewFilter;

            // if submission id filter is given, add it as filter condition
            if (submissionId != null) {
                Filter submissionFilter = new EqualToFilter("submission", submissionId);
                fullReviewFilter = new AndFilter(reviewFilter, submissionFilter);
            }

            return managerHelper.getReviewManager().searchReviews(fullReviewFilter, true);
        } catch (ReviewManagementException e) {
            throw new PhaseHandlingException("Problem with review retrieval", e);
        }
    }

    /**
     * Returns all the reviews for a phase based on resource.
     *
     * @param conn
     *            Connection to use to lookup resource role id.
     * @param managerHelper
     *            ManagerHelper instance.
     * @param reviewers
     *            the reviewer resource array
     * @param submissionId
     *            submission id to be used as filter when searching for reviews.
     * @return Review[] which match filter conditions.
     * @throws PhaseHandlingException
     *             if there was an error during retrieval.
     * @throws SQLException
     *             in case of error when looking up resource role id.
     */
    static Review[] searchReviewsForResources(Connection conn, ManagerHelper managerHelper,
        Resource[] reviewers, Long submissionId) throws PhaseHandlingException, SQLException {
        if (reviewers.length == 0) {
            return new Review[0];
        }

        try {
            // create reviewer ids array
            Long[] reviewerIds = new Long[reviewers.length];

            for (int i = 0; i < reviewers.length; i++) {
                reviewerIds[i] = new Long(reviewers[i].getId());
            }

            Filter reviewFilter = new InFilter("reviewer", Arrays.asList(reviewerIds));
            Filter fullReviewFilter = reviewFilter;

            // if submission id filter is given, add it as filter condition
            if (submissionId != null) {
                Filter submissionFilter = new EqualToFilter("submission", submissionId);
                fullReviewFilter = new AndFilter(reviewFilter, submissionFilter);
            }

            return managerHelper.getReviewManager().searchReviews(fullReviewFilter, true);
        } catch (ReviewManagementException e) {
            throw new PhaseHandlingException("Problem with review retrieval", e);
        }
    }

    /**
     * Gets the scorecard minimum score from the given review.
     *
     * @param scorecardManager
     *            ScorecardManager instance.
     * @param review
     *            Review instance.
     * @return the scorecard minimum score from the given review.
     * @throws PhaseHandlingException
     *             if a problem occurs when retrieving scorecard.
     */
    static float getScorecardMinimumScore(ScorecardManager scorecardManager, Review review)
        throws PhaseHandlingException {
        long scorecardId = review.getScorecard();

        try {
            Scorecard[] scoreCards = scorecardManager.getScorecards(new long[] {scorecardId}, false);

            if (scoreCards.length == 0) {
                throw new PhaseHandlingException("No scorecards found for scorecard id: " + scorecardId);
            }

            Scorecard scoreCard = scoreCards[0];

            return scoreCard.getMinScore();
        } catch (PersistenceException e) {
            throw new PhaseHandlingException("Problem with scorecard retrieval", e);
        }
    }

    /**
     * Retrieves all the submissions for the given project id.
     * <p>
     * Change in version 1.4: add submissionTypeId to filter submissions by type.
     * </p>
     *
     * @param uploadManager
     *            UploadManager instance to use for searching.
     * @param projectId
     *            project id.
     * @param submissionTypeId
     *            the submission type id.
     * @return submissions for the given project id.
     * @throws PhaseHandlingException
     *             if an error occurs during retrieval.
     */
    static Submission[] searchSubmissionsForProject(UploadManager uploadManager, long projectId,
        long submissionTypeId) throws PhaseHandlingException {
        Filter submissionFilter = SubmissionFilterBuilder.createProjectIdFilter(projectId);

        // changes in version 1.5, add a submission type filter
        Filter submissionTypeFilter = SubmissionFilterBuilder
            .createSubmissionTypeIdFilter(submissionTypeId);
        Filter fullFilter = new AndFilter(submissionFilter, submissionTypeFilter);

        try {
            return uploadManager.searchSubmissions(fullFilter);
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("There was a submission retrieval error", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("There was a search builder error", e);
        }
    }

    /**
     * searches for resources based on resource role names and phase id filters.
     *
     * @param managerHelper
     *            ManagerHelper instance.
     * @param conn
     *            connection to connect to db with.
     * @param resourceRoleNames
     *            array of resource role names.
     * @param phaseId
     *            phase id.
     * @return Resource[] which match search criteria.
     * @throws PhaseHandlingException
     *             if an error occurs during retrieval.
     */
    static Resource[] searchResourcesForRoleNames(ManagerHelper managerHelper, Connection conn,
        String[] resourceRoleNames, long phaseId) throws PhaseHandlingException {
        List<Long> resourceRoleIds = new ArrayList<Long>();

        try {
            for (int i = 0; i < resourceRoleNames.length; i++) {
                resourceRoleIds.add(new Long(ResourceRoleLookupUtility.lookUpId(conn,
                    resourceRoleNames[i])));
            }

            Filter resourceRoleFilter = new InFilter(
                ResourceRoleFilterBuilder.RESOURCE_ROLE_ID_FIELD_NAME, resourceRoleIds);
            Filter phaseIdFilter = ResourceFilterBuilder.createPhaseIdFilter(phaseId);
            Filter fullFilter = new AndFilter(resourceRoleFilter, phaseIdFilter);

            return managerHelper.getResourceManager().searchResources(fullFilter);
        } catch (SQLException e) {
            throw new PhaseHandlingException("There was a database connection error", e);
        } catch (SearchBuilderConfigurationException e) {
            throw new PhaseHandlingException("Problem with search builder configuration", e);
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("There was a resource retrieval error", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Problem with search builder", e);
        }
    }

    /**
     * A utility method to get the integer value for the given phase attribute. This
     * method throws PhaseHandlingException if the attribute value is null or could not be
     * parsed into an integer.
     *
     * @param phase
     *            phase instance.
     * @param attrName
     *            name of attribute.
     * @return integer value for the given phase attribute.
     * @throws PhaseHandlingException
     *             if the attribute value is null or could not be parsed into an integer.
     */
    static int getIntegerAttribute(Phase phase, String attrName) throws PhaseHandlingException {
        String sValue = (String) phase.getAttribute(attrName);

        if (sValue == null) {
            throw new PhaseHandlingException("Phase attribute '" + attrName + "' was null.");
        }

        try {
            return Integer.parseInt(sValue);
        } catch (NumberFormatException e) {
            throw new PhaseHandlingException("Phase attribute '" + attrName + "' was non-integer:"
                + sValue, e);
        }
    }

    /**
     * A utility method to get the long value for the given resource property. This method
     * throws PhaseHandlingException if the attribute value is null or could not be parsed
     * into an integer.
     *
     * @param resource
     *            Resource instance.
     * @param propName
     *            name of property.
     * @return integer value for the given phase attribute.
     * @throws PhaseHandlingException
     *             if the attribute value is null or could not be parsed into an integer.
     */
    static long getIntegerProp(Resource resource, String propName) throws PhaseHandlingException {
        Object sValue = resource.getProperty(propName);

        if (sValue == null) {
            throw new PhaseHandlingException("Resource property '" + propName + "' was null.");
        }

        try {
            return Long.parseLong((String) sValue);
        } catch (ClassCastException e) {
            throw new PhaseHandlingException("Resource property '" + propName + "' was in string:"
                + sValue, e);
        } catch (NumberFormatException e) {
            throw new PhaseHandlingException("Resource property '" + propName + "' was non-integer:"
                + sValue, e);
        }
    }

    /**
     * Returns whether the screening is of manual type by checking the "Manual Screening"
     * phase attribute.
     *
     * @param phase
     *            the phase instance.
     * @return true if screening is of manual type, false otherwise.
     */
    static boolean isScreeningManual(Phase phase) {
        String screeningType = (String) phase.getAttribute("Manual Screening");

        return ((screeningType != null) && screeningType.equals("Yes"));
    }

    /**
     * Helper method to get all the screening tasks for the project.
     * <p>
     * Change in version 1.4: it will now search the screening task for the submissions
     * with Contest Submission type.
     * </p>
     *
     * @param managerHelper
     *            ManagerHelper instance.
     * @param connection
     *            the DB connection to be used
     * @param phase
     *            phase instance.
     * @return ScreeningTask[] array that meet search criteria.
     * @throws PhaseHandlingException
     *             in case of error when retrieving data.
     * @throws SQLException
     *             if an error occurred when looking up "Contest Submission" id.
     */
    static ScreeningTask[] getScreeningTasks(ManagerHelper managerHelper, Connection connection,
        Phase phase) throws PhaseHandlingException, SQLException {
        try {
            // change in version 1.4
            // Lookup submission type ID with "Contest Submission" name
            long submissionTypeId = SubmissionTypeLookupUtility.lookUpId(connection,
                "Contest Submission");

            // get the submissions for the project
            Submission[] submissions = searchSubmissionsForProject(managerHelper.getUploadManager(),
                phase.getProject().getId(), submissionTypeId);

            // get upload ids for all submissions
            long[] uploadIds = new long[submissions.length];

            for (int i = 0; i < submissions.length; i++) {
                uploadIds[i] = submissions[i].getUpload().getId();
            }

            // get screening tasks for the upload ids
            if (uploadIds.length == 0) {
                return new ScreeningTask[] {};
            } else {
                return managerHelper.getScreeningManager().getScreeningTasks(uploadIds);
            }
        } catch (ScreeningTaskDoesNotExistException e) {
            throw new PhaseHandlingException("There was a screening retrieval error", e);
        } catch (com.cronos.onlinereview.autoscreening.management.PersistenceException e) {
            throw new PhaseHandlingException("There was a screening retrieval error", e);
        }
    }

    /**
     * utility method to get a SubmissionStatus object for the given status name.
     *
     * @param uploadManager
     *            UploadManager instance used to search for submission status.
     * @param statusName
     *            submission status name.
     * @return a SubmissionStatus object for the given status name.
     * @throws PhaseHandlingException
     *             if submission status could not be found.
     */
    static SubmissionStatus getSubmissionStatus(UploadManager uploadManager, String statusName)
        throws PhaseHandlingException {
        SubmissionStatus[] statuses = null;

        try {
            statuses = uploadManager.getAllSubmissionStatuses();
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("Error finding submission status with name: " + statusName,
                e);
        }

        for (int i = 0; i < statuses.length; i++) {
            if (statusName.equals(statuses[i].getName())) {
                return statuses[i];
            }
        }

        throw new PhaseHandlingException("Could not find submission status with name: " + statusName);
    }

    /**
     * retrieves all active submissions for the given project id.
     * <p>
     * Change in version 1.4: it will search active submissions with given type now.
     * </p>
     *
     * @param uploadManager
     *            UploadManager instance to use for searching.
     * @param conn
     *            the connection.
     * @param projectId
     *            project id.
     * @param submissionTypeName
     *            the submission type name.
     * @return all active submissions for the given project id.
     * @throws PhaseHandlingException
     *             if an error occurs during retrieval.
     * @throws SQLException
     *             if an error occurred when looking up id.
     */
    static Submission[] searchActiveSubmissions(UploadManager uploadManager, Connection conn,
        long projectId, String submissionTypeName) throws PhaseHandlingException, SQLException {
        // first get submission status id for "Active" status
        long activeStatusId = SubmissionStatusLookupUtility.lookUpId(conn, "Active");

        // then get submission type id for given type
        long submissionTypeId = SubmissionTypeLookupUtility.lookUpId(conn, submissionTypeName);

        // search for submissions
        Filter projectIdFilter = SubmissionFilterBuilder.createProjectIdFilter(projectId);
        Filter submissionActiveStatusFilter = SubmissionFilterBuilder
            .createSubmissionStatusIdFilter(activeStatusId);

        // change in version 1.4
        // use a submission type filter
        Filter submissionTypeFilter = SubmissionFilterBuilder
            .createSubmissionTypeIdFilter(submissionTypeId);

        Filter fullFilter = new AndFilter(Arrays.asList(new Filter[] {projectIdFilter,
            submissionActiveStatusFilter, submissionTypeFilter}));

        try {
            return uploadManager.searchSubmissions(fullFilter);
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("There was a submission retrieval error", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("There was a search builder error", e);
        }
    }

    /**
     * This method checks if the winning submission has one aggregated review scorecard
     * committed and returns the same.
     *
     * @param conn
     *            database connection
     * @param managerHelper
     *            ManagerHelper instance.
     * @param aggPhaseId
     *            aggregation phase id.
     * @return aggregated review scorecard, null if does not exist.
     * @throws PhaseHandlingException
     *             if an error occurs when retrieving data or if there are multiple
     *             scorecards.
     */
    static Review getAggregationWorksheet(Connection conn, ManagerHelper managerHelper, long aggPhaseId)
        throws PhaseHandlingException  {
        // Search the aggregated review scorecard
        Review[] reviews = searchReviewsForResourceRoles(conn, managerHelper, aggPhaseId,
            new String[] {"Aggregator"}, null);

        if (reviews.length == 0) {
            return null;
        } else if (reviews.length == 1) {
            return reviews[0];
        } else {
            throw new PhaseHandlingException("Cannot have multiple aggregation scorecards.");
        }
    }

    /**
     * returns the final review worksheet for the given final review phase id.
     *
     * @param conn
     *            the Connection
     * @param managerHelper
     *            ManagerHelper instance.
     * @param finalReviewPhaseId
     *            final review phase id.
     * @return the final review worksheet, or null if not existing.
     * @throws PhaseHandlingException
     *             if an error occurs when retrieving data.
     * @throws SQLException
     *             if an error occurs when looking up resource role id.
     */
    static Review getFinalReviewWorksheet(Connection conn, ManagerHelper managerHelper,
        long finalReviewPhaseId) throws PhaseHandlingException, SQLException {
        Review[] reviews = searchReviewsForResourceRoles(conn, managerHelper, finalReviewPhaseId,
            new String[] {"Final Reviewer"}, null);

        if (reviews.length == 0) {
            return null;
        } else if (reviews.length == 1) {
            return reviews[0];
        } else {
            throw new PhaseHandlingException("Multiple final review worksheets found");
        }
    }

    /**
     * utility method to create a PhaseType instance with given name.
     *
     * @param phaseManager
     *            PhaseManager instance used to search for submission status.
     * @param typeName
     *            phase type name.
     * @return PhaseType instance.
     * @throws PhaseHandlingException
     *             if phase status could not be found.
     */
    static PhaseType getPhaseType(PhaseManager phaseManager, String typeName)
        throws PhaseHandlingException {
        PhaseType[] types = null;

        try {
            types = phaseManager.getAllPhaseTypes();
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Error finding phase type with name: " + typeName, e);
        }

        for (int i = 0; i < types.length; i++) {
            if (typeName.equals(types[i].getName())) {
                return types[i];
            }
        }

        throw new PhaseHandlingException("Could not find phase type with name: " + typeName);
    }

    /**
     * utility method to create a PhaseStatus instance with given name.
     *
     * @param phaseManager
     *            PhaseManager instance used to search for submission status.
     * @param statusName
     *            phase status name.
     * @return PhaseStatus instance.
     * @throws PhaseHandlingException
     *             if phase status could not be found.
     */
    static PhaseStatus getPhaseStatus(PhaseManager phaseManager, String statusName)
        throws PhaseHandlingException {
        PhaseStatus[] statuses = null;

        try {
            statuses = phaseManager.getAllPhaseStatuses();
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Error finding phase status with name: " + statusName, e);
        }

        for (int i = 0; i < statuses.length; i++) {
            if (statusName.equals(statuses[i].getName())) {
                return statuses[i];
            }
        }

        throw new PhaseHandlingException("Could not find phase status with name: " + statusName);
    }

    /**
     * utility method to create a CommentType instance with given name.
     *
     * @param reviewManager
     *            ReviewManager instance used to search for comment type.
     * @param typeName
     *            comment type name.
     * @return CommentType instance.
     * @throws PhaseHandlingException
     *             if comment type could not be found.
     */
    static CommentType getCommentType(ReviewManager reviewManager, String typeName)
        throws PhaseHandlingException {
        CommentType[] types = null;

        try {
            types = reviewManager.getAllCommentTypes();
        } catch (ReviewManagementException e) {
            throw new PhaseHandlingException("Error finding comment type with name: " + typeName, e);
        }

        for (int i = 0; i < types.length; i++) {
            if (typeName.equals(types[i].getName())) {
                return types[i];
            }
        }

        throw new PhaseHandlingException("Could not find comment type with name: " + typeName);
    }

    /**
     * utility method to create a UploadStatus instance with given name.
     *
     * @param uploadManager
     *            UploadManager instance used to search for submission status.
     * @param statusName
     *            upload status name.
     * @return UploadStatus instance.
     * @throws PhaseHandlingException
     *             if submission status could not be found.
     */
    static UploadStatus getUploadStatus(UploadManager uploadManager, String statusName)
        throws PhaseHandlingException {
        UploadStatus[] statuses = null;

        try {
            statuses = uploadManager.getAllUploadStatuses();
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("Error finding upload status with name: " + statusName, e);
        }

        for (int i = 0; i < statuses.length; i++) {
            if (statusName.equals(statuses[i].getName())) {
                return statuses[i];
            }
        }

        throw new PhaseHandlingException("Could not find upload status with name: " + statusName);
    }

    /**
     * Returns the winning submitter for the given project id.
     *
     * @param resourceManager
     *            ResourceManager instance.
     * @param projectManager
     *            {@link ProjectManager} instance
     * @param conn
     *            connection to connect to db with.
     * @param projectId
     *            project id.
     * @return the winning submiter Resource.
     * @throws PhaseHandlingException
     *             if an error occurs when searching for resource.
     */
    static Resource getWinningSubmitter(ResourceManager resourceManager, ProjectManager projectManager,
        Connection conn, long projectId) throws PhaseHandlingException {
        try {
            com.topcoder.management.project.Project project = projectManager.getProject(projectId);
            String winnerId = (String) project.getProperty("Winner External Reference ID");

            if (winnerId != null) {
                long submitterRoleId = ResourceRoleLookupUtility.lookUpId(conn, "Submitter");
                ResourceFilterBuilder.createExtensionPropertyNameFilter(EXTERNAL_REFERENCE_ID);

                AndFilter fullFilter = new AndFilter(Arrays.asList(new Filter[] {
                    ResourceFilterBuilder.createResourceRoleIdFilter(submitterRoleId),
                    ResourceFilterBuilder.createProjectIdFilter(projectId),
                    ResourceFilterBuilder.createExtensionPropertyNameFilter(EXTERNAL_REFERENCE_ID),
                    ResourceFilterBuilder.createExtensionPropertyValueFilter(winnerId)}));

                Resource[] submitters = resourceManager.searchResources(fullFilter);

                if (submitters.length > 0) {
                    return submitters[0];
                }

                return null;
            }

            return null;
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem when retrieving resource", e);
        } catch (com.topcoder.management.project.PersistenceException e) {
            throw new PhaseHandlingException("Problem retrieving project id: " + projectId, e);
        } catch (SQLException e) {
            throw new PhaseHandlingException("Problem when looking up id", e);
        } catch (SearchBuilderConfigurationException e) {
            throw new PhaseHandlingException("Problem with search builder configuration", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Problem with search builder", e);
        }
    }

    /**
     * <p>
     * Inserts a post-mortem phase into persistence.
     * </p>
     *
     * @param currentPrj
     *            current project.
     * @param currentPhase
     *            current phase to insert a post-mortem phase.
     * @param managerHelper
     *            a helper for accessing various managers.
     * @param operator
     *            the operator name.
     * @throws PhaseHandlingException
     *             if any error occurs.
     * @since 1.1
     */
    static void insertPostMortemPhase(Project currentPrj, Phase currentPhase,
        ManagerHelper managerHelper, String operator) throws PhaseHandlingException {
        // Check if Post-Mortem phase already exists for the project. If so then do
        // nothing
        if (null != getPostMortemPhase(currentPrj)) {
            return;
        }

        // Check if Post-Mortem is required based on project properties. If not then do
        // nothing
        ProjectManager projectManager = managerHelper.getProjectManager();

        try {
            com.topcoder.management.project.Project project = projectManager.getProject(currentPrj
                .getId());
            String postMortemRequired = (String) project.getProperty("Post-Mortem Required");

            if ((postMortemRequired == null) || !(postMortemRequired.equalsIgnoreCase("true"))) {
                return;
            }
        } catch (com.topcoder.management.project.PersistenceException e) {
            throw new PhaseHandlingException("Failed to retrieve details for project "
                + currentPrj.getId(), e);
        }

        PhaseManager phaseManager = managerHelper.getPhaseManager();

        // create phase type and status objects
        PhaseType postMortemPhaseType = getPhaseType(phaseManager, "Post-Mortem");
        PhaseStatus phaseStatus = PhasesHelper.getPhaseStatus(phaseManager, "Scheduled");

        try {
            // Create new Post-Mortem phase
            String postMortemPhaseDuration = getPropertyValue(PostMortemPhaseHandler.class.getName(),
                "PostMortemPhaseDuration", true);

            createNewPhases(currentPrj, currentPhase, new PhaseType[] {postMortemPhaseType},
                phaseStatus, new long[] {Long.parseLong(postMortemPhaseDuration) * HOUR}, false);

            // Set the number of required reviewers for Post-Mortem phase to default value
            String postMortemPhaseDefaultReviewerNumber = getPropertyValue(PostMortemPhaseHandler.class
                .getName(), "PostMortemPhaseDefaultReviewersNumber", true);
            String postMortemPhaseDefaultScorecardID = getPropertyValue(PostMortemPhaseHandler.class
                .getName(), "PostMortemPhaseDefaultScorecardID", true);
            Phase postMortemPhase = getPostMortemPhase(currentPrj);
            postMortemPhase.setAttribute("Reviewer Number", postMortemPhaseDefaultReviewerNumber);
            postMortemPhase.setAttribute("Scorecard ID", postMortemPhaseDefaultScorecardID);

            phaseManager.updatePhases(currentPrj, operator);
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases", e);
        } catch (ConfigurationException e) {
            throw new PhaseHandlingException("Problem when reading configuration file", e);
        }
    }

    /**
     * <p>
     * Inserts a Approval phase into persistence.
     * </p>
     *
     * @param currentPrj
     *            current project.
     * @param currentPhase
     *            current phase to insert a approval phase.
     * @param managerHelper
     *            a helper for accessing various managers.
     * @param operator
     *            the operator name.
     * @throws PhaseHandlingException
     *             if any error occurs.
     * @since 1.3
     */
    static void insertApprovalPhase(Project currentPrj, Phase currentPhase, ManagerHelper managerHelper,
        String operator) throws PhaseHandlingException {
        PhaseManager phaseManager = managerHelper.getPhaseManager();

        // create phase type and status objects
        PhaseType approvalPhaseType = getPhaseType(phaseManager, "Approval");
        PhaseStatus phaseStatus = PhasesHelper.getPhaseStatus(phaseManager, "Scheduled");

        try {
            // Create new Approval phase
            String approvalPhaseDuration = getPropertyValue(ApprovalPhaseHandler.class.getName(),
                "ApprovalPhaseDuration", true);

            // Find lst Approval phase (if any)
            Phase lastApprovalPhase = locatePhase(currentPhase, "Approval", false, false);

            createNewPhases(currentPrj, currentPhase, new PhaseType[] {approvalPhaseType}, phaseStatus,
                new long[] {Long.parseLong(approvalPhaseDuration) * HOUR}, false);

            // Set the number of required reviewers for Approval phase to default value or
            // to value taken
            // from previous Approval phase if it exists
            String approvalPhaseDefaultReviewerNumber;
            String approvalPhaseDefaultScorecardID;

            if (lastApprovalPhase == null) {
                approvalPhaseDefaultReviewerNumber = getPropertyValue(ApprovalPhaseHandler.class
                    .getName(), "ApprovalPhaseDefaultReviewersNumber", true);

                approvalPhaseDefaultScorecardID = getPropertyValue(ApprovalPhaseHandler.class.getName(),
                    "ApprovalPhaseDefaultScorecardID", true);
            } else {
                approvalPhaseDefaultReviewerNumber = (String) lastApprovalPhase
                    .getAttribute("Reviewer Number");
                approvalPhaseDefaultScorecardID = (String) lastApprovalPhase
                    .getAttribute("Scorecard ID");
            }

            Phase approvalPhase = getApprovalPhase(currentPrj);
            approvalPhase.setAttribute("Reviewer Number", approvalPhaseDefaultReviewerNumber);
            approvalPhase.setAttribute("Scorecard ID", approvalPhaseDefaultScorecardID);

            phaseManager.updatePhases(currentPrj, operator);
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases", e);
        } catch (ConfigurationException e) {
            throw new PhaseHandlingException("Problem when reading configuration file", e);
        }
    }

    /**
     * Inserts a final fix and final review phases.
     *
     * @param currentPhase
     *            current phase to insert a post-mortem phase.
     * @param phaseManager
     *            the PhaseManager instance.
     * @param operator
     *            the operator name.
     * @return index of current phase.
     * @throws PhaseHandlingException
     *             if any error occurs.
     * @since 1.1
     */
    static int insertFinalFixAndFinalReview(Phase currentPhase, PhaseManager phaseManager,
        String operator) throws PhaseHandlingException {
        PhaseType finalFixPhaseType = PhasesHelper.getPhaseType(phaseManager, "Final Fix");
        PhaseType finalReviewPhaseType = PhasesHelper.getPhaseType(phaseManager, "Final Review");
        PhaseStatus phaseStatus = PhasesHelper.getPhaseStatus(phaseManager, "Scheduled");

        // find current phase index and also the lengths of 'final fix'
        // and 'final review' phases.
        Project currentPrj = currentPhase.getProject();

        // use helper method to create the new phases
        int currentPhaseIndex = PhasesHelper.createNewPhases(currentPrj, currentPhase, new PhaseType[] {
            finalFixPhaseType, finalReviewPhaseType}, phaseStatus, phaseManager, operator, false);

        // save the phases
        try {
            phaseManager.updatePhases(currentPrj, operator);
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases", e);
        }

        return currentPhaseIndex;
    }

    /**
     * Checks if the given phase if the first phase in the project. Note that if multiple
     * phases start at the same date/time at the beginning of the project, all they are
     * considered to be first phases of the project.
     *
     * @param phase
     *            the phase to be checked.
     * @return true if phase is the first phase in the project, false otherwise.
     * @since 1.4
     */
    static boolean isFirstPhase(Phase phase) {
        // Get all phases for the project
        Phase[] phases = phase.getProject().getAllPhases();

        // Get index of the input phase in phases array
        int phaseIndex = 0;

        for (int i = 0; i < phases.length; i++) {
            if (phases[i].getId() == phase.getId()) {
                phaseIndex = i;
            }
        }

        while ((phaseIndex > 0)
            && (phases[phaseIndex - 1].calcStartDate().compareTo(phases[phaseIndex].calcStartDate()) == 0)) {
            phaseIndex--;
        }

        return (phaseIndex == 0) ? true : false;
    }

    /**
     * Inserts a specification submission and specification review phases.
     *
     * @param currentPhase
     *            the current phase after which new phases must be inserted.
     * @param phaseManager
     *            the phase manager instance.
     * @param operator
     *            the operator name.
     * @return the index of the current phase.
     * @throws PhaseHandlingException
     *             if any error occurs.
     * @since 1.4
     */
    static int insertSpecSubmissionSpecReview(Phase currentPhase, PhaseManager phaseManager,
        String operator) throws PhaseHandlingException {
        PhaseType specSubmissionPhaseType = PhasesHelper.getPhaseType(phaseManager,
            "Specification Submission");
        PhaseType specReviewPhaseType = PhasesHelper.getPhaseType(phaseManager, "Specification Review");
        PhaseStatus phaseStatus = PhasesHelper.getPhaseStatus(phaseManager, "Scheduled");

        // find current phase index and also the lengths of 'Specification Submission'
        // and 'Specification Review' phases.
        Project currentPrj = currentPhase.getProject();

        // use helper method to create the new phases
        int currentPhaseIndex = PhasesHelper.createNewPhases(currentPrj, currentPhase, new PhaseType[] {
            specSubmissionPhaseType, specReviewPhaseType}, phaseStatus, phaseManager, operator, true);

        // save the phases
        try {
            phaseManager.updatePhases(currentPrj, operator);
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases", e);
        }

        return currentPhaseIndex;
    }

    /**
     * <p>
     * Helper method to add new phases of given type to the given project. This method
     * does the following:
     * <ol>
     * <li>finds the index of given phase in the current phases array of the project.</li>
     * <li>finds the lengths of current phases of the given types.</li>
     * <li>creates new Phase instance with given type and status</li>
     * <li>creates a new Phases array with additional elements for new phase instances.</li>
     * <li>removes all phases of the project.</li>
     * <li>adds each Phase from the new Phases array to the project.</li>
     * <li>if necessary the attributes of current phase are copied to newly created phase of same type if such a phase
     * is created</li>
     * </ol>
     * </p>
     *
     * @param currentPrj
     *            project to add/remove phases from.
     * @param currentPhase
     *            current phase instance.
     * @param newPhaseTypes
     *            types of new phases to create.
     * @param newPhaseStatus
     *            the status to set for all the phases.
     * @param phaseManager
     *            the manager
     * @param operator
     *            the operator
     * @param copyCurrentPhaseAttributes <code>true</code> if attributes of current phase must be copied to created new
     *        phase of same type; <code>false</code> otherwise.
     * @return returns the index of the current phase in the phases array.
     */
    static int createNewPhases(Project currentPrj, Phase currentPhase, PhaseType[] newPhaseTypes,
                               PhaseStatus newPhaseStatus, PhaseManager phaseManager, String operator,
                               boolean copyCurrentPhaseAttributes) {
        // find current phase index and also the lengths of aggregation and
        // aggregation review phases.
        Phase[] phases = currentPrj.getAllPhases();
        int currentPhaseIndex = 0;
        long[] newPhaseLengths = new long[newPhaseTypes.length];

        for (int i = 0; i < phases.length; i++) {
            if (phases[i].getId() == currentPhase.getId()) {
                currentPhaseIndex = i;
            }

            // find the lengths of corresponding phase types
            for (int p = 0; p < newPhaseTypes.length; p++) {
                if (phases[i].getPhaseType().getId() == newPhaseTypes[p].getId()) {
                    newPhaseLengths[p] = phases[i].getLength();
                }
            }
        }

        // create new phases array which will hold the new phase order
        Phase[] newPhases = new Phase[phases.length + newPhaseTypes.length];
        // set the old phases into the new phases array
        for (int i = 0; i < phases.length; i++) {
            if (i > currentPhaseIndex) {
                newPhases[i + newPhaseTypes.length] = phases[i];
            } else {
                newPhases[i] = phases[i];
            }
        }

        // create new phases for each phase type...
        for (int p = 0; p < newPhaseTypes.length; p++) {
            Phase newPhase = new Phase(currentPrj, newPhaseLengths[p]);
            newPhase.setPhaseStatus(newPhaseStatus);
            newPhase.setPhaseType(newPhaseTypes[p]);

            // the new phase is dependent on the earlier phase
            newPhase.addDependency(new Dependency(newPhases[currentPhaseIndex + p], newPhase, false,
                true, 0));

            // Copy current phase attributes if necessary
            if (copyCurrentPhaseAttributes) {
                if (newPhase.getPhaseType().getId() == currentPhase.getPhaseType().getId()) {
                    Set<Serializable> currentPhaseAttributeKeys
                        = (Set<Serializable>) currentPhase.getAttributes().keySet();
                    for (Serializable attributeName : currentPhaseAttributeKeys) {
                        newPhase.setAttribute(attributeName, currentPhase.getAttribute(attributeName));
                    }
                }
            }

            newPhases[currentPhaseIndex + (p + 1)] = newPhase;
        }

        // if there was a phase after the new phases, change the dependencies of
        // that phase
        // to move to last new phase.
        if (newPhases.length > (currentPhaseIndex + newPhaseTypes.length + 1)) {
            Phase afterPhase = newPhases[currentPhaseIndex + newPhaseTypes.length + 1];
            Phase lastNewPhase = newPhases[currentPhaseIndex + newPhaseTypes.length];

            Dependency[] dependencies = afterPhase.getAllDependencies();

            for (int d = 0; d < dependencies.length; d++) {
                Dependency dependency = dependencies[d];
                dependency.getDependent().removeDependency(dependency);
                dependency.getDependent().addDependency(
                    new Dependency(lastNewPhase, dependency.getDependent(), dependency
                        .isDependencyStart(), dependency.isDependentStart(), dependency.getLagTime()));
            }
        }

        // add the new phases to the project
        for (int p = 0; p < newPhaseTypes.length; p++) {
            Phase newPhase = newPhases[currentPhaseIndex + (p + 1)];
            currentPrj.addPhase(newPhase);
        }

        // set the scheduled start and end times after dependencies are changed
        for (int p = 0; p < newPhases.length; p++) {
            Phase phase = newPhases[p];
            phase.setScheduledStartDate(phase.calcStartDate());
            phase.setScheduledEndDate(phase.calcEndDate());
        }

        return currentPhaseIndex;
    }

    /**
     * Creates an aggregator/Final Reviewer/Specification Reviewer resource. This method is
     * called when a new aggregation/review, new finalfix/review or specification submission/review
     * cycle is inserted when aggregation of final review worksheet or specification review is rejected.
     * It simply copies the old aggregator/final/specification reviewer properties, except for the id and
     * phase id and inserts the new resource in the database.
     * <p>
     * Change in version 1.4: it now can create specification reviewer resource.
     * </p>
     * @param oldPhase
     *            the aggregation or final review phase instance.
     * @param managerHelper
     *            ManagerHelper instance.
     * @param conn
     *            connection to use.
     * @param roleName
     *            &quot;Aggregator&quot;, &quot;Final Reviewer&quot; or &quot;Specification Reviewer&quot;.
     * @param newPhaseId
     *            the new phase id the new resource is to be associated with.
     * @param operator
     *            operator name.
     * @return the id of the newly created resource.
     * @throws PhaseHandlingException if any error occurred.
     */
    static long createAggregatorOrFinalReviewer(Phase oldPhase, ManagerHelper managerHelper,
        Connection conn, String roleName, long newPhaseId, String operator)
        throws PhaseHandlingException {
        // search for the old "Aggregator", "Final Reviewer" or "Specification Reviewer"
        // resource
        Resource[] resources = PhasesHelper.searchResourcesForRoleNames(managerHelper, conn,
            new String[] {roleName}, oldPhase.getId());

        if (resources.length == 0) {
            throw new PhaseHandlingException("unable for find resource for role - " + roleName);
        }

        Resource oldResource = resources[0];

        // copy resource properties
        Resource newResource = new Resource();
        newResource.setProject(oldResource.getProject());
        newResource.setResourceRole(oldResource.getResourceRole());
        // OrChange - modified to set the submissions
        newResource.setSubmissions(oldResource.getSubmissions());

        Map<?, ?> properties = oldResource.getAllProperties();

        if ((properties != null) && !properties.isEmpty()) {
            Set<?> entries = properties.entrySet();

            for (Iterator<?> itr = entries.iterator(); itr.hasNext();) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) itr.next();

                if ( !PAYMENT_PROPERTY_KEY.equals((String)entry.getKey()) ) {
                    newResource.setProperty((String) entry.getKey(), entry.getValue());
                }
            }
        }

        // don't duplicate payments
        newResource.setProperty(PAYMENT_STATUS_PROPERTY_KEY, "N/A");

        // set phase id
        newResource.setPhase(new Long(newPhaseId));

        // update resource into persistence.
        try {
            managerHelper.getResourceManager().updateResource(newResource, operator);

            return newResource.getId();
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem when persisting resource with role:" + roleName, e);
        }
    }

    /**
     * copies the comments from one worksheet to another. Which comments are copied are
     * determined by the typesToCopy and extraInfoToCheck parameters.
     *
     * @param fromWorkSheet
     *            source worksheet for the comments.
     * @param toWorkSheet
     *            destination worksheet.
     * @param typesToCopy
     *            types of comments to copy.
     * @param extraInfoToCheck
     *            extra info to check the comment against.
     */
    static void copyComments(Review fromWorkSheet, Review toWorkSheet, String[] typesToCopy,
        String extraInfoToCheck) {
        Comment[] comments = fromWorkSheet.getAllComments();

        // copy all comments with given type and extra info.
        for (int c = 0; c < comments.length; c++) {
            Comment comment = comments[c];

            if (isCommentToBeCopied(comment, typesToCopy, extraInfoToCheck)) {
                toWorkSheet.addComment(copyComment(comment));
            }
        }
    }

    /**
     * This helper method copies the review items from one worksheet to another. It will
     * also copy the comments for each review item from one worksheet to another. Which
     * comments are copied are determined by the typesToCopy.
     *
     * @param fromWorkSheet
     *            source worksheet for the comments.
     * @param toWorkSheet
     *            destination worksheet.
     * @param typesToCopy
     *            types of comments to copy.
     */
    static void copyReviewItems(Review fromWorkSheet, Review toWorkSheet, String[] typesToCopy) {
        Item[] reviewItems = fromWorkSheet.getAllItems();

        for (int r = 0; r < reviewItems.length; r++) {
            Item item = reviewItems[r];

            // create a new review item and copy all properties
            Item newItem = new Item(item.getId());
            newItem.setDocument(item.getDocument());
            newItem.setQuestion(item.getQuestion());
            newItem.setAnswer(item.getAnswer());

            // copy all comments with given type and extra info.
            Comment[] comments = item.getAllComments();

            for (int c = 0; c < comments.length; c++) {
                Comment comment = comments[c];

                if (isCommentToBeCopied(comment, typesToCopy, null)) {
                    newItem.addComment(copyComment(comment));
                }
            }

            // add the item to the destination worksheet
            toWorkSheet.addItem(newItem);
        }
    }

    /**
     * This helper method copies the review items from one worksheet to another. It will
     * also copy the comments for each review item from one worksheet to another. Only
     * reviewer item comments which are marked as "Accept" will be copied. Once such an
     * item comment is found, the follow-up comments are copied until the next reviewer
     * item is found which is not accepted.
     *
     * @param fromWorkSheet
     *            source worksheet for the comments.
     * @param toWorkSheet
     *            destination worksheet.
     */
    static void copyFinalReviewItems(Review fromWorkSheet, Review toWorkSheet) {
        Item[] reviewItems = fromWorkSheet.getAllItems();

        for (int r = 0; r < reviewItems.length; r++) {
            Item item = reviewItems[r];

            // create a new review item and copy all properties
            Item newItem = new Item(item.getId());
            newItem.setDocument(item.getDocument());
            newItem.setQuestion(item.getQuestion());
            newItem.setAnswer(item.getAnswer());

            // copy all comments with given type and extra info.
            Comment[] comments = item.getAllComments();
            boolean copy = false;

            for (int c = 0; c < comments.length; c++) {
                Comment comment = comments[c];

                // if it is a reviewer comment
                if (isReviewerComment(comment)) {
                    // mark copy flag as true or false based on whether it is
                    // accepted.
                    copy = "Accept".equals(comment.getExtraInfo());
                }

                // if copy flag is marked true, then copy all comments.
                if (copy) {
                    newItem.addComment(copyComment(comment));
                }
            }

            // add the item to the destination worksheet
            toWorkSheet.addItem(newItem);
        }
    }

    /**
     * Deep clone a review effectually making all items new.
     *
     * @param review
     *            the review to be cloned.
     * @return the cloned review.
     */
    static Review cloneReview(Review review) {
        Review copiedReview = new Review();
        copiedReview.setAuthor(review.getAuthor());
        copiedReview.setCommitted(review.isCommitted());
        copiedReview.setScore(review.getScore());
        copiedReview.setScorecard(review.getScorecard());
        copiedReview.setSubmission(review.getSubmission());

        Comment[] comments = review.getAllComments();

        for (int i = 0; i < comments.length; ++i) {
            Comment copiedComment = new Comment();
            copiedComment.setAuthor(comments[i].getAuthor());
            copiedComment.setComment(comments[i].getComment());
            copiedComment.setCommentType(comments[i].getCommentType());
            copiedComment.setExtraInfo(comments[i].getExtraInfo());
            copiedReview.addComment(copiedComment);
        }

        Item[] items = review.getAllItems();

        for (int i = 0; i < items.length; ++i) {
            Item copiedItem = new Item();
            copiedItem.setAnswer(items[i].getAnswer());
            copiedItem.setDocument(items[i].getDocument());
            copiedItem.setQuestion(items[i].getQuestion());
            copiedReview.addItem(copiedItem);

            comments = items[i].getAllComments();

            for (int j = 0; j < comments.length; ++j) {
                Comment copiedComment = new Comment();
                copiedComment.setAuthor(comments[j].getAuthor());
                copiedComment.setComment(comments[j].getComment());
                copiedComment.setCommentType(comments[j].getCommentType());
                copiedComment.setExtraInfo(comments[j].getExtraInfo());
                copiedItem.addComment(copiedComment);
            }
        }

        return copiedReview;
    }

    /**
     * <p>
     * Gets the submitter information and submission result for different phases.
     * </p>
     * <p>
     * Change in version 1.4, it will now search active submission with contest submission
     * type.
     * </p>
     * @param conn
     *            the database connection, will be closed in this method after query
     * @param helper
     *            the ManagerHelper class
     * @param projectId
     *            the project id
     * @param appealPhase
     *            true if it is in appeals phase
     * @return List of values map, map for each submission
     * @throws PhaseHandlingException
     *             if any error occurs
     */
    static List<Map<String, Object>> getSubmitterValueArray(Connection conn, ManagerHelper helper,
        long projectId, boolean appealPhase) throws PhaseHandlingException {
        // get the submissions
        Submission[] submissions = null;

        try {
            // changes in version 1.4
            submissions = PhasesHelper.searchActiveSubmissions(helper.getUploadManager(), conn,
                projectId, CONTEST_SUBMISSION_TYPE);
        } catch (SQLException e) {
            throw new PhaseHandlingException("Problem when looking up submissions.", e);
        } finally {
            PhasesHelper.closeConnection(conn);
        }

        // for each submission, get the submitter and its scores
        try {
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

            for (Submission submission : submissions) {
                Map<String, Object> values = new HashMap<String, Object>();
                values.put("SUBMITTER_HANDLE", notNullValue(helper.getResourceManager().getResource(
                    submission.getUpload().getOwner()).getProperty("Handle")));
                values.put(appealPhase ? "SUBMITTER_SCORE" : "SUBMITTER_PRE_APPEALS_SCORE", submission
                    .getInitialScore());
                result.add(values);
            }

            return result;
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem when looking up resource for the submission.", e);
        }
    }

    /**
     * <p>
     * Constructs the submitter information value map list for email generation content.
     * </p>
     *
     * @param submissions
     *            the submissions
     * @param resourceManager
     *            the ResourceManager in the application
     * @param screeningEnd
     *            true if it is the end of screening phase
     * @return List of map values
     * @throws PhaseHandlingException
     *             any error occurs
     * @since 1.2
     */
    static List<Map<String, Object>> constructSubmitterValues(Submission[] submissions,
        ResourceManager resourceManager, boolean screeningEnd) throws PhaseHandlingException {
        try {
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

            for (Submission submission : submissions) {
                Map<String, Object> values = new HashMap<String, Object>();

                // find the submitter (it is a resource) by the id
                Resource submitter = resourceManager.getResource(submission.getUpload().getOwner());
                values.put("SUBMITTER_HANDLE", notNullValue(submitter.getProperty("Handle")));

                if (!screeningEnd) {
                    values.put("SUBMITTER_RELIABILITY", notNullValue(submitter
                        .getProperty("Reliability")));
                    values.put("SUBMITTER_RATING", notNullValue(submitter.getProperty("Rating")));
                } else {
                    values.put("SUBMITTER_SCORE", notNullValue(submission.getScreeningScore()));
                    values.put("SUBMITTER_RESULT",
                        ((submission.getSubmissionStatus() != null) && submission.getSubmissionStatus()
                            .getName().equalsIgnoreCase("Failed Screening")) ? "Failed Screening"
                            : "Pass Screening");
                }

                result.add(values);
            }

            return result;
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem when looking up resource for the submission.", e);
        }
    }

    /**
     * <p>
     * Gets not null/empty property value for the given value. If it is null/empty, return
     * 'N/A'.
     * </p>
     *
     * @param value
     *            the value of property
     * @return not null value
     * @since 1.2
     */
    static Object notNullValue(Object value) {
        if ((value == null) || (value instanceof String && (((String) value).trim().length() == 0))) {
            return "N/A";
        }

        return value;
    }

    /**
     * Returns true if the comment is a reviewer comment, false otherwise. The comment is
     * said to be a reviewer comment if it is one of the REVIEWER_COMMENT_TYPES elements.
     *
     * @param comment
     *            comment to check.
     * @return true if the comment is a reviewer comment, false otherwise.
     */
    private static boolean isReviewerComment(Comment comment) {
        String commentType = comment.getCommentType().getName();

        for (int i = 0; i < REVIEWER_COMMENT_TYPES.length; i++) {
            if (commentType.equals(REVIEWER_COMMENT_TYPES[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a new comment which is a copy of the given comment, only with no extra info
     * set.
     *
     * @param comment
     *            comment to be copied.
     * @return a new comment which is a copy of the given comment.
     */
    private static Comment copyComment(Comment comment) {
        Comment newComment = new Comment(comment.getId());
        newComment.setAuthor(comment.getAuthor());
        newComment.setComment(comment.getComment());
        newComment.setCommentType(comment.getCommentType());

        return newComment;
    }

    /**
     * checks if the comment is to be copied i.e. is one of the comment types that have to
     * be copied to the review worksheet.
     *
     * @param comment
     *            comment to check.
     * @param typesToCopy
     *            types of comments to copy.
     * @param extraInfoToCheck
     *            extra info to check the comment against.
     * @return true if it is to be copied, false otherwise.
     */
    private static boolean isCommentToBeCopied(Comment comment, String[] typesToCopy,
        String extraInfoToCheck) {
        String commentType = comment.getCommentType().getName();

        for (int i = 0; i < typesToCopy.length; i++) {
            // return true if it is one of the types to be copied and it is
            // marked as "Accept"
            if (commentType.equals(typesToCopy[i])) {
                if (extraInfoToCheck != null) {
                    if (extraInfoToCheck.equals(comment.getExtraInfo())) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns whether all submitters agreed to early appeals phase completion.
     *
     * @param resourceManager
     *            ResourceManager instance.
     * @param uploadManager
     *            upload manager
     * @param conn
     *            connection to connect to db with.
     * @param projectId
     *            project id.
     * @return true if all submitters agreed to early appeals phase completion
     * @throws PhaseHandlingException
     *             if an error occurs when searching for resource.
     * @since 1.1
     */
    static boolean canCloseAppealsEarly(ResourceManager resourceManager, UploadManager uploadManager,
        Connection conn, long projectId) throws PhaseHandlingException {
        try {
            long submitterRoleId = ResourceRoleLookupUtility.lookUpId(conn, SUBMITTER_ROLE_NAME);

            AndFilter fullFilter = new AndFilter(Arrays.asList(new Filter[] {
                ResourceFilterBuilder.createResourceRoleIdFilter(submitterRoleId),
                ResourceFilterBuilder.createProjectIdFilter(projectId),
                ResourceFilterBuilder
                    .createExtensionPropertyNameFilter(APPEALS_COMPLETED_EARLY_PROPERTY_KEY),
                ResourceFilterBuilder.createExtensionPropertyValueFilter(YES_VALUE)}));

            Resource[] earlyAppealCompletionsSubmitters = resourceManager.searchResources(fullFilter);

            // move resource ids to a hashset to speed up lookup
            Set<Long> earlyAppealResourceIds = new HashSet<Long>(earlyAppealCompletionsSubmitters.length);

            for (Resource r : earlyAppealCompletionsSubmitters) {
                earlyAppealResourceIds.add(r.getId());
            }

            // check all submitters with active submission statuses (this will
            // leave out failed screening and deleted)
            // CHANGE in version 1.4
            Submission[] activeSubmissions = searchActiveSubmissions(uploadManager, conn, projectId,
                CONTEST_SUBMISSION_TYPE);

            for (Submission s : activeSubmissions) {
                if (!earlyAppealResourceIds.contains(new Long(s.getUpload().getOwner()))) {
                    return false;
                }
            }

            return true;
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem when retrieving resource", e);
        } catch (SQLException e) {
            throw new PhaseHandlingException("Problem when looking up id", e);
        } catch (SearchBuilderConfigurationException e) {
            throw new PhaseHandlingException("Problem with search builder configuration", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Problem with search builder", e);
        }
    }

    /**
     * <p>
     * Checks if all direct parent projects for specified project are completed or not.
     * The check is performed only for those parent projects which are linked with links
     * which have <code>allow_overlap</code> flag set to <code>false</code>.
     * </p>
     *
     * @param projectId
     *            a <code>long</code> providing the ID of a project to check the
     *            completeness of parent projects for.
     * @param managerHelper
     *            a <code>ManagerHelper</code> to be used for getting the links to
     *            parent projects.
     * @param conn
     *            a <code>Connection</code> providing connection to target database.
     * @return <code>true</code> if all parent projects for specified project are
     *         completed or there are no parent projects at all; <code>false</code>
     *         otherwise.
     * @throws com.topcoder.management.project.PersistenceException
     *             if an unexpected error occurs while accessing the persistent data
     *             store.
     * @throws com.topcoder.management.phase.PhaseManagementException
     *             if an error occurs while reading phases data.
     * @throws java.sql.SQLException
     *             if an SQL error occurs.
     * @since 1.3
     */
    static boolean areParentProjectsCompleted(long projectId, ManagerHelper managerHelper,
        Connection conn) throws com.topcoder.management.project.PersistenceException,
        com.topcoder.management.phase.PhaseManagementException, SQLException {
        ProjectLink[] links = managerHelper.getProjectLinkManager().getDestProjectLinks(projectId);

        for (ProjectLink link : links) {
            if (!link.getType().isAllowOverlap()) {
                com.topcoder.management.project.Project parentProject = link.getDestProject();

                if (parentProject.getProjectStatus().getId() != SEVEN) { // project status
                                                                        // is not
                                                                        // Completed

                    // if not active
                    if (parentProject.getProjectStatus().getId() != 1) {
                        return false;
                    }

                    Project phasesProject = managerHelper.getPhaseManager().getPhases(
                        parentProject.getId());

                    // Check if all phases are closed.
                    Phase[] phases = phasesProject.getAllPhases();

                    for (int i = 0; i < phases.length; i++) {
                        if (phases[i].getPhaseStatus().getId() != THREE) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * <p>
     * Returns all the reviews for a project based on resource role names. This method is
     * useful for finding reviews for resources which are not tied to specified phase.
     * </p>
     *
     * @param conn
     *            Connection to use to lookup resource role id.
     * @param managerHelper
     *            ManagerHelper instance.
     * @param projectId
     *            project id to be used as filter.
     * @param resourceRoleNames
     *            resource role names to be used as filter.
     * @param submissionId
     *            submission id to be used as filter when searching for reviews.
     * @return Review[] which match filter conditions.
     * @throws PhaseHandlingException
     *             if there was an error during retrieval.
     * @throws SQLException
     *             in case of error when looking up resource role id.
     * @since 1.3
     */
    static Review[] searchProjectReviewsForResourceRoles(Connection conn, ManagerHelper managerHelper,
        long projectId, String[] resourceRoleNames, Long submissionId) throws PhaseHandlingException,
        SQLException {
        Resource[] reviewers = searchProjectResourcesForRoleNames(managerHelper, conn,
            resourceRoleNames, projectId);

        if (reviewers.length == 0) {
            return new Review[0];
        }

        try {
            // create reviewer ids array
            Long[] reviewerIds = new Long[reviewers.length];

            for (int i = 0; i < reviewers.length; i++) {
                reviewerIds[i] = new Long(reviewers[i].getId());
            }

            Filter reviewFilter = new InFilter("reviewer", Arrays.asList(reviewerIds));
            Filter fullReviewFilter = reviewFilter;

            // if submission id filter is given, add it as filter condition
            if (submissionId != null) {
                Filter submissionFilter = new EqualToFilter("submission", submissionId);
                fullReviewFilter = new AndFilter(reviewFilter, submissionFilter);
            }

            return managerHelper.getReviewManager().searchReviews(fullReviewFilter, true);
        } catch (ReviewManagementException e) {
            throw new PhaseHandlingException("Problem with review retrieval", e);
        }
    }

    /**
     * <p>
     * Searches for resources associated with specified project and the specified resource
     * roles assigned.
     * </p>
     *
     * @param managerHelper
     *            ManagerHelper instance.
     * @param conn
     *            connection to connect to db with.
     * @param resourceRoleNames
     *            array of resource role names.
     * @param projectId
     *            ID for the project to find associated resources for.
     * @return Resource[] which match search criteria.
     * @throws PhaseHandlingException
     *             if an error occurs during retrieval.
     * @since 1.3
     */
    static Resource[] searchProjectResourcesForRoleNames(ManagerHelper managerHelper, Connection conn,
        String[] resourceRoleNames, long projectId) throws PhaseHandlingException {
        List<Long> resourceRoleIds = new ArrayList<Long>();

        try {
            for (int i = 0; i < resourceRoleNames.length; i++) {
                resourceRoleIds.add(new Long(ResourceRoleLookupUtility.lookUpId(conn,
                    resourceRoleNames[i])));
            }

            Filter resourceRoleFilter = new InFilter(
                ResourceRoleFilterBuilder.RESOURCE_ROLE_ID_FIELD_NAME, resourceRoleIds);
            Filter projectIdFilter = ResourceFilterBuilder.createProjectIdFilter(projectId);
            Filter fullFilter = new AndFilter(resourceRoleFilter, projectIdFilter);

            return managerHelper.getResourceManager().searchResources(fullFilter);
        } catch (SQLException e) {
            throw new PhaseHandlingException("There was a database connection error", e);
        } catch (SearchBuilderConfigurationException e) {
            throw new PhaseHandlingException("Problem with search builder configuration", e);
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("There was a resource retrieval error", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Problem with search builder", e);
        }
    }

    /**
     * <p>
     * Gets the reviews (if any) for specified <code>Approval</code> phase.
     * </p>
     *
     * @param reviews
     *            a <code>Review</code> array providing the <code>Approval</code>
     *            reviews for project.
     * @param thisPhase
     *            a <code>Phase</code> providing the <code>Approval</code> phases to
     *            get reviews for.
     * @return a <code>Review</code> array listing the reviews (if any) for specified
     *         <code>Approval</code> phase.
     * @since 1.3
     */
    static Review[] getApprovalPhaseReviews(Review[] reviews, Phase thisPhase) {
        int count = 0;
        List<Review> thisPhaseReviews = new ArrayList<Review>();
        Phase[] phases = thisPhase.getProject().getAllPhases();

        for (int i = 0; i < phases.length; i++) {
            Phase phase = phases[i];

            if (phase.getPhaseType().getName().equals("Approval")) {
                int reviewerNumber = Integer.parseInt((String) phase.getAttribute("Reviewer Number"));

                if (phase.getId() != thisPhase.getId()) {
                    count += reviewerNumber;
                } else {
                    int start = count;

                    for (int j = 0; j < reviewerNumber; j++) {
                        if ((start + j) < reviews.length) {
                            Review review = reviews[start + j];
                            thisPhaseReviews.add(review);
                        }
                    }

                    break;
                }
            }
        }

        return thisPhaseReviews.toArray(new Review[thisPhaseReviews.size()]);
    }

    /**
     * <p>
     * Gets the <code>Post-Mortem</code> phase for specified project.
     * </p>
     *
     * @param project
     *            a <code>Project</code> to get the post-mortem phase for.
     * @return a <code>Phase</code> providing details for <code>Post-Mortem</code>
     *         phase for specified project or <code>null</code> if such phase does not
     *         exist.
     * @since 1.3
     */
    static Phase getPostMortemPhase(Project project) {
        return getLastPhaseByType(project, "Post-Mortem");
    }

    /**
     * <p>
     * Gets the <code>Approval</code> phase for specified project.
     * </p>
     *
     * @param project
     *            a <code>Project</code> to get the approval phase for.
     * @return a <code>Phase</code> providing details for <code>Approval</code> phase
     *         for specified project or <code>null</code> if such phase does not exist.
     * @since 1.3
     */
    private static Phase getApprovalPhase(Project project) {
        return getLastPhaseByType(project, "Approval");
    }

    /**
     * <p>
     * Gets the last phase of specified type for specified project.
     * </p>
     *
     * @param project
     *            a <code>Project</code> to get the phase for.
     * @param phaseTypeName
     *            a <code>String</code> providing the name of the phase type.
     * @return a <code>Phase</code> providing details for phase of specified type for
     *         specified project or <code>null</code> if such phase does not exist.
     * @since 1.3
     */
    private static Phase getLastPhaseByType(Project project, String phaseTypeName) {
        Phase[] phases = project.getAllPhases();

        for (int i = phases.length - 1; i >= 0; i--) {
            Phase phase = phases[i];

            if (phase.getPhaseType().getName().equals(phaseTypeName)) {
                return phase;
            }
        }

        return null;
    }

    /**
     * <p>
     * Helper method to add new phases of given type to the given project. This method
     * does the following:
     * <ol>
     * <li>finds the index of given phase in the current phases array of the project.</li>
     * <li>finds the lengths of current phases of the given types.</li>
     * <li>creates new Phase instance with given type and status</li>
     * <li>creates a new Phases array with additional elements for new phase instances.</li>
     * <li>removes all phases of the project.</li>
     * <li>adds each Phase from the new Phases array to the project.</li>
     * </ol>
     * </p>
     *
     * @param currentPrj
     *            project to add/remove phases from.
     * @param currentPhase
     *            current phase instance.
     * @param newPhaseTypes
     *            types of new phases to create.
     * @param newPhaseStatus
     *            the status to set for all the phases.
     * @param newPhaseLengths
     *            the lengths for the new phases.
     * @param adjustSubsequentPhaseDependencies
     *            <code>true</code> if sub-sequent phases must be set depending on new
     *            phase; <code>false</code> otherwise.
     * @return returns the index of the current phase in the phases array.
     * @since 1.3
     */
    private static int createNewPhases(Project currentPrj, Phase currentPhase,
        PhaseType[] newPhaseTypes, PhaseStatus newPhaseStatus, long[] newPhaseLengths,
        boolean adjustSubsequentPhaseDependencies) {
        // find current phase index and also the lengths of aggregation and aggregation
        // review phases.
        Phase[] phases = currentPrj.getAllPhases();
        int currentPhaseIndex = 0;

        for (int i = 0; i < phases.length; i++) {
            if (phases[i].getId() == currentPhase.getId()) {
                currentPhaseIndex = i;
            }
        }

        // create new phases array which will hold the new phase order
        Phase[] newPhases = new Phase[phases.length + newPhaseTypes.length];

        // set the old phases into the new phases array
        for (int i = 0; i < phases.length; i++) {
            if (i > currentPhaseIndex) {
                newPhases[i + newPhaseTypes.length] = phases[i];
            } else {
                newPhases[i] = phases[i];
            }
        }

        // create new phases for each phase type and make them depending on current phase
        for (int p = 0; p < newPhaseTypes.length; p++) {
            Phase newPhase = new Phase(currentPrj, newPhaseLengths[p]);
            newPhase.setPhaseStatus(newPhaseStatus);
            newPhase.setPhaseType(newPhaseTypes[p]);
            newPhase.addDependency(new Dependency(newPhases[currentPhaseIndex + p], newPhase, false,
                true, 0));
            newPhases[currentPhaseIndex + (p + 1)] = newPhase;
        }

        // if there is a phase after the new phases change the dependencies of that phase
        if (adjustSubsequentPhaseDependencies) {
            if (newPhases.length > (currentPhaseIndex + newPhaseTypes.length + 1)) {
                Phase afterPhase = newPhases[currentPhaseIndex + newPhaseTypes.length + 1];
                Phase lastNewPhase = newPhases[currentPhaseIndex + newPhaseTypes.length];

                Dependency[] dependencies = afterPhase.getAllDependencies();

                for (int d = 0; d < dependencies.length; d++) {
                    Dependency dependency = dependencies[d];
                    dependency.getDependent().removeDependency(dependency);
                    dependency.getDependent()
                        .addDependency(
                            new Dependency(lastNewPhase, dependency.getDependent(), dependency
                                .isDependencyStart(), dependency.isDependentStart(), dependency
                                .getLagTime()));
                }
            }
        }

        // add the new phases to the project
        for (int p = 0; p < newPhaseTypes.length; p++) {
            Phase newPhase = newPhases[currentPhaseIndex + (p + 1)];
            currentPrj.addPhase(newPhase);
        }

        // set the scheduled start and end times after dependencies are changed
        for (int p = 0; p < newPhases.length; p++) {
            Phase phase = newPhases[p];
            phase.setScheduledStartDate(phase.calcStartDate());
            phase.setScheduledEndDate(phase.calcEndDate());
        }

        return currentPhaseIndex;
    }

    /**
     * Searches the specification submission for the project.
     *
     * @param phase
     *            the phase.
     * @param managerHelper
     *            the manager helper.
     * @param conn
     *            the database connection.
     * @param log
     *            the log instance.
     * @return the specification submission or null if no record found.
     * @throws PhaseHandlingException
     *             if any error occurred when searching submission or more than one
     *             submission exist.
     * @since 1.4
     */
    static Submission hasOneSpecificationSubmission(Phase phase, ManagerHelper managerHelper,
        Connection conn, Log log) throws PhaseHandlingException {
        try {
            // Check if one specification submission exists
            Submission[] submissions = PhasesHelper.searchActiveSubmissions(managerHelper
                .getUploadManager(), conn, phase.getProject().getId(), "Specification Submission");

            if (submissions.length > 1) {
                log.log(Level.ERROR, "Multiple specification submissions exist.");
                throw new PhaseHandlingException("Multiple specification submissions exist.");
            }

            return (submissions.length == 0) ? null : submissions[0];
        } catch (SQLException e) {
            log.log(Level.ERROR, new LogMessage(new Long(phase.getId()), null,
                "Fail to searching active specification submission.", e));
            throw new PhaseHandlingException(
                "SQL error when searching active specification submission.", e);
        }
    }

    /**
     * Checks whether all parent projects are completed. Note the connection is closed.
     *
     * @param phase
     *            the phase.
     * @param conn database connection.
     * @param managerHelper manager helper.
     * @param log the log.
     * @return true if the all parent projects are completed, false otherwise.
     * @throws PhaseHandlingException
     *             if any error occurred when checking parent projects completed.
     *  @since 1.4
     */
    static boolean areParentProjectsCompleted(Phase phase, Connection conn,
        ManagerHelper managerHelper, Log log) throws PhaseHandlingException {
        try {
            return PhasesHelper.areParentProjectsCompleted(phase.getProject().getId(),
                managerHelper, conn);
        } catch (com.topcoder.management.project.PersistenceException e) {
            log.log(Level.ERROR, new LogMessage(new Long(phase.getId()), null,
                "PersistenceException when checking parent projects completed.", e));
            throw new PhaseHandlingException(
                "PersistenceException when checking parent projects completed.", e);
        } catch (PhaseManagementException e) {
            log.log(Level.ERROR, new LogMessage(new Long(phase.getId()), null,
                "Fail to check parent projects completed.", e));
            throw new PhaseHandlingException(
                "PhaseManagementException when checking parent projects completed.", e);
        } catch (SQLException e) {
            log.log(Level.ERROR, new LogMessage(new Long(phase.getId()), null,
                "SQL error when checking parent projects completed.", e));
            throw new PhaseHandlingException("SQL error when checking parent projects completed.", e);
        } finally {
            PhasesHelper.closeConnection(conn);
        }
    }
}
