/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import com.topcoder.util.datavalidator.AbstractObjectValidator;
import com.topcoder.util.datavalidator.ObjectValidator;

/**
 * <p>
 * This validator is always true for any provided value. It is used as a
 * substitute value for null constraints on searchable field maps. This is done
 * as a convenient alternative to encapsulating null-handling behavior into all
 * filter classes.
 * </p>
 *
 * <p>
 * Thread Safety: This class is stateless and the methods can handle concurrent
 * requests, so its therefore thread-safe.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
class AlwaysTrueValidator extends AbstractObjectValidator {

    /**
     * <p>
     * Default Cosntructor.
     * </p>
     *
     */
    public AlwaysTrueValidator() {
    }

    /**
     * <p>
     * Always returns null, since this validator considers any object to be
     * valid.
     * </p>
     *
     * @return null always.
     * @param obj
     *            The object to validate (this parameter is ignored)
     */
    public String getMessage(Object obj) {
        return null;
    }

    /**
     * <p>
     * Always returns true, since this validator considers any object to be
     * valid.
     * </p>
     *
     * @return True always.
     * @param obj
     *            The object to validate (this parameter is ignored)
     */
    public boolean valid(Object obj) {
        return true;
    }
}
