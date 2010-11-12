/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * Dummy sub-class of AbstractPhaseHandler for testing purpose.
 *
 * @author bose_java
 * @version 1.0
 */
class AbstractPhaseHandlerSubClass extends AbstractPhaseHandler {

    /**
     * will call super constructor.
     *
     * @param namespace namespace.
     * @throws ConfigurationException if config error occurs.
     */
    public AbstractPhaseHandlerSubClass(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * dummy implementation always returning false.
     *
     * @param phase input phase to check.
     *
     * @return always false.
     *
     * @throws PhaseHandlingException if phase could not be processed.
     */
    public boolean canPerform(Phase phase) throws PhaseHandlingException {
        return false;
    }

    /**
     * Dummy do-nothing implementation.
     *
     * @param phase phase to check.
     * @param operator operator.
     *
     * @throws PhaseHandlingException if phase could not be processed.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
    }
}
