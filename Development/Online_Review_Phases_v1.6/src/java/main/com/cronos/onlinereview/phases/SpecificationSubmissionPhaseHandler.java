/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * This class implements <code>PhaseHandler</code> interface to provide methods to check
 * if a phase can be executed and to add extra logic to execute a phase. It will be used
 * by Phase Management component. It is configurable using an input namespace. The
 * configurable parameters include database connection and email sending parameters. This
 * class handles the specification submission phase. If the input is of other phase types,
 * <code>PhaseNotSupportedException</code> will be thrown.
 * </p>
 * <p>
 * The specification review phase can start when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>Phase start date/time is reached (if specified)</li>
 * <li>This is the first phase in the project and all parent projects are completed</li>
 * </ul>
 * </p>
 * <p>
 * The specification review phase can stop when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>If one active submission with &quot;Specification Submission&quot; type exists</li>
 * </ul>
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 *
 * @author saarixx, myxgyy
 * @version 1.4
 * @since 1.4
 */
public class SpecificationSubmissionPhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE =
        "com.cronos.onlinereview.phases.SpecificationSubmissionPhaseHandler";

    /**
     * Represents the logger for this class. Is initialized during class loading and never
     * changed after that.
     */
    private static final Log LOG = LogFactory
        .getLog(SpecificationSubmissionPhaseHandler.class.getName());

    /**
     * Constant for Specification Review phase type.
     */
    private static final String PHASE_TYPE_SPECIFICATION_SUBMISSION = "Specification Submission";

    /**
     * Create a new instance of SpecificationSubmissionPhaseHandler using the default
     * namespace for loading configuration settings.
     *
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     */
    public SpecificationSubmissionPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of SpecificationSubmissionPhaseHandler using the given
     * namespace for loading configuration settings.
     *
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     * @throws IllegalArgumentException
     *             if the input is null or empty string.
     * @param namespace
     *            the namespace to load configuration settings from.
     */
    public SpecificationSubmissionPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Check if the input phase can be executed or not. This method will check the phase
     * status to see what will be executed. This method will be called by canStart() and
     * canEnd() methods of PhaseManager implementations in Phase Management component.
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase can be
     * started using the following conditions: the dependencies are met, Phase start
     * date/time is reached (if specified), if this is not the first phase in the project
     * or all parent projects are completed.
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped
     * using the following conditions: The dependencies are met and if one active
     * submission with &quot;Specification Submission&quot; type exists.
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     *
     * @param phase
     *            The input phase to check.
     * @return True if the input phase can be executed, false otherwise.
     * @throws PhaseNotSupportedException
     *             if the input phase type is not &quot;Specification Submission&quot;
     *             type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input is null.
     */
    public boolean canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, PHASE_TYPE_SPECIFICATION_SUBMISSION);

        // will throw exception if phase status is
        // neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        if (toStart) {
            if (!PhasesHelper.canPhaseStart(phase)) {
                return false;
            }

            // This is NOT the first phase in the project
            // or all parent projects are completed
            return !PhasesHelper.isFirstPhase(phase)
                || PhasesHelper.areParentProjectsCompleted(phase, createConnection(), this.getManagerHelper(), LOG);
        } else {
            // Check phase dependencies
            boolean depsMeet = PhasesHelper.arePhaseDependenciesMet(phase, false);

            if (depsMeet) {
                Connection conn = null;
                try {
                    conn = createConnection();
                    return PhasesHelper.hasOneSpecificationSubmission(phase, getManagerHelper(),
                        createConnection(), LOG) != null;
                } finally {
                    PhasesHelper.closeConnection(conn);
                }
            }
            // dependencies not met
            return false;
        }
    }

    /**
     * Provides additional logic to execute a phase. This method will be called by start()
     * and end() methods of PhaseManager implementations in Phase Management component.
     * This method can send email to a group of users associated with timeline
     * notification for the project. The email can be send on start phase or end phase
     * base on configuration settings.
     *
     * @param phase
     *            The input phase to check.
     * @param operator
     *            The operator that execute the phase.
     * @throws PhaseNotSupportedException
     *             if the input phase type is not &quot;Specification Submission&quot;
     *             type.
     * @throws PhaseHandlingException
     *             if phase status is neither &quot;Scheduled&quot; nor &quot;Open&quot;
     *             or there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, PHASE_TYPE_SPECIFICATION_SUBMISSION);
        PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        sendEmail(phase);
    }
}