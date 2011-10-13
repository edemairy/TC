/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import java.util.Properties;


/**
 * <p>
 * This class provide helper methods for this component.
 * </p>
 * <p>
 * <strong>Thread-Safety:</strong> This class is immutable as it is final and can not be instantiated thus thread
 * safe.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public final class Helper {
    /**
     * <p>
     * Private modifier prevents the creation of new instance.
     * </p>
     */
    private Helper() {
    }

    /**
     * <p>
     * Check if the given value is <code>null</code>.
     * </p>
     *
     * @param value the value to check.
     * @param message the exception message if value is <code>null</code>.
     * @throws IllegalArgumentException if <code>value</code> is <code>null</code>.
     */
    public static void checkNull(Object value, String message) {
        if (value != null) {
            return;
        }

        throw new IllegalArgumentException(message);
    }

    /**
     * <p>
     * Check if the given value is <code>null</code> or an empty string.
     * </p>
     *
     * @param value the value to check.
     * @param message the exception message if value is <code>null</code> or empty.
     * @throws IllegalArgumentException if <code>value</code> is <code>null</code> or empty.
     */
    public static void checkNullOrEmpty(String value, String message) {
        checkNull(value, message);

        if (value.trim().length() > 0) {
            return;
        }

        throw new IllegalArgumentException(message);
    }

    /**
     * <p>
     * Gets optional property value in specified key from the given properties.
     * </p>
     *
     * @param prop the properties.
     * @param key the property key to get its value.
     * @param defaultValue the default value to be returned if optional property is missing.
     * @return the property value.
     * @throws ObjectCacheConfigurationException if the value is not String, or if the value is empty.
     */
    public static String getProperty(Properties prop, String key, String defaultValue) {
        String value = getProperty(prop, key, false);
        return value == null ? defaultValue : value;
    }

    /**
     * <p>
     * Gets property value in specified key from the given properties.
     * </p>
     *
     * @param prop the properties
     * @param key the property key to get its value
     * @param required whether the property is required or not
     * @return the property value, null if optional property is missing
     * @throws ObjectCacheConfigurationException if required value is missing, or if the value is empty, or if
     *         value is not String.
     */
    public static String getProperty(Properties prop, String key, boolean required) {
        Object value = prop.get(key);

        if (value != null) {
            if (!(value instanceof String)) {
                throw new ObjectCacheConfigurationException(concat("The value of property '", key,
                    "' is not an instance of ", String.class.getName()));
            }
            // trim the value
            String strVal = ((String) value).trim();
            if (strVal.length() == 0) {
                throw new ObjectCacheConfigurationException(concat("The value of property '", key, "' is empty"));
            }

            return strVal;
        } else if (required) {
            throw new ObjectCacheConfigurationException(concat("Missing required property '", key, "'"));
        }

        return null;
    }

    /**
     * <p>
     * Concatenates all given values into one string.
     * </p>
     *
     * @param values the objects to concatenate
     * @return concatenated string
     */
    public static String concat(Object... values) {
        StringBuilder sb = new StringBuilder();
        for (Object value : values) {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * <p>
     * Gets the value of optional property as an integer value.
     * </p>
     *
     * @param prop the Properties to get the value from.
     * @param key the key of the property to get its value.
     * @param defaultValue the default value to return if optional property is not specified.
     * @return the property value.
     * @throws ObjectCacheConfigurationException if the value is not String, or if value is an empty String, or if
     *         value cannot be parsed as integer, or if value is negative.
     */
    public static int getIntProperty(Properties prop, String key, int defaultValue) {
        String value = getProperty(prop, key, false);
        if (value == null) {
            return defaultValue;
        }
        try {
            int intValue = Integer.parseInt(value);
            if (intValue < 0) {
                throw new ObjectCacheConfigurationException(concat("The value of property '", key,
                    "' should be non-negative"));
            }
            return intValue;
        } catch (NumberFormatException e) {
            throw new ObjectCacheConfigurationException(concat("The value of property '", key,
                "' cannot be parsed as integer"));
        }
    }
}
