/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate;

import com.topcoder.search.builder.SearchBuilderConfigurationException;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;


/**
 * <p>
 * This helper class of the com.topcoder.search.builder.hibernate package.
 * </p>
 *
 * @author myxgyy
 * @version 1.4
 * @since 1.4
 */
final class HibernateHelper {
    /**
     * The private construct.
     */
    private HibernateHelper() {
        // empty
    }

    /**
     * Gets the string property value of the property name. When the property is
     * mandatory, it must exist in the configuration manager.
     *
     * @param namespace
     *            the namespace where the configuration is read.
     * @param propertyName
     *            the property name of the property to be read.
     * @param mandatory
     *            <code>true</code> if the property is mandatory; <code>false</code>
     *            otherwise.
     * @return the string value of the property in the configuration.
     * @throws SearchBuilderConfigurationException
     *             if <code>namespace</code> is missing in configuration; or the
     *             property is missing in the configuration and the property is mandatory;
     *             or accessing the configuration manager fails.
     */
    static String getConfigPropertyValue(String namespace, String propertyName,
        boolean mandatory) throws SearchBuilderConfigurationException {
        String value;

        try {
            value = ConfigManager.getInstance()
                                 .getString(namespace, propertyName);
        } catch (UnknownNamespaceException e) {
            throw new SearchBuilderConfigurationException("The namespace '"
                + namespace + "' is missing in configuration.", e);
        }

        if (value == null) {
            if (mandatory) {
                throw new SearchBuilderConfigurationException(propertyName
                    + " is required in configuration.");
            }
        } else if (value.trim().length() == 0) {
            throw new SearchBuilderConfigurationException(propertyName
                + " should not be empty in configuration.");
        }

        return value;
    }
}
