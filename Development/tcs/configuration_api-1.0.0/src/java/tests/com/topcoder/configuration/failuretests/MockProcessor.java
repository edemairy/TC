/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.failuretests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.ProcessException;
import com.topcoder.configuration.Processor;

/**
 * <p>
 * This class implements Processor interface.
 * It is only used for testing.
 * </p>
 *
 * @author biotrail
 * @version 1.0
 */
public class MockProcessor implements Processor {
    /**
     * <p>
     * Default empty constructor.
     * </p>
     */
    public MockProcessor() {
        // empty
    }

    /**
     * <p>
     * Implements the process(ConfigurationObject) method.
     * </p>
     *
     * @param config the {@link ConfigurationObject} to be processed
     * @throws ProcessException if any error occurs while processing
     */
    public void process(ConfigurationObject config) throws ProcessException {
        //empty

    }

}
