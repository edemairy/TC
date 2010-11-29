package com.ibm.tools.ffvt;

import java.util.*;
import java.lang.*;

/**
 * This class is a container for file format validation result data. It is a simple JavaBean (POJO) that provides getters and setters for all private attributes and performs no argument validation in the setters.
 * 
 * Thread Safety:
 * This class is mutable and not thread safe.
 */
public class ValidationResult {
    /**
     * The value indicating if validation was successful. The validation is considered to be successful if no validation errors were found. Has getter and setter.
     */
    private boolean successful;

    /**
     * The list of validation errors. Can be any value, can contain any values. Has getter and setter.
     */
    private List<String> errors;

    /**
     * The validation timestamp. Can be any value. Has getter and setter.
     */
    private Date timestamp;

    /**
     * The expected file format name. Can be any value. Has getter and setter.
     */
    private String expectedFormatName;

    /**
     * The path of the file that was validated. Cannot be any value. Has getter and setter.
     */
    private String filePath;

    /**
     * Creates an instance of ValidationResult.
     * 
     * Implementation Notes:
     * Do nothing.
     */
    public ValidationResult() {
    }

    /**
     * Retrieves the value indicating if validation was successful.
     * 
     * Returns:
     * the value indicating if validation was successful
     * @return the value indicating if validation was successful
     */
    public boolean isSuccessful() {
        return false;
    }

    /**
     * Sets the value indicating if validation was successful.
     * 
     * Parameters:
     * successful - the value indicating if validation was successful
     * @param successful the value indicating if validation was successful
     */
    public void setSuccessful(boolean successful) {
    }

    /**
     * Retrieves the list of validation errors.
     * 
     * Returns:
     * the list of validation errors
     * @return the list of validation errors
     */
    public List<String> getErrors() {
        return null;
    }

    /**
     * Sets the list of validation errors.
     * 
     * Parameters:
     * errors - the list of validation errors
     * @param errors the list of validation errors
     */
    public void setErrors(List<String> errors) {
    }

    /**
     * Retrieves the validation timestamp.
     * 
     * Returns:
     * the validation timestamp
     * @return the validation timestamp
     */
    public Date getTimestamp() {
        return null;
    }

    /**
     * Sets the validation timestamp.
     * 
     * Parameters:
     * timestamp - the validation timestamp
     * @param timestamp the validation timestamp
     */
    public void setTimestamp(Date timestamp) {
    }

    /**
     * Retrieves the expected file format name.
     * 
     * Returns:
     * the expected file format name
     * @return the expected file format name
     */
    public String getExpectedFormatName() {
        return null;
    }

    /**
     * Sets the expected file format name.
     * 
     * Parameters:
     * expectedFormatName - the expected file format name
     * @param expectedFormatName the expected file format name
     */
    public void setExpectedFormatName(String expectedFormatName) {
    }

    /**
     * Retrieves the path of the file that was validated.
     * 
     * Returns:
     * the path of the file that was validated
     * @return the path of the file that was validated
     */
    public String getFilePath() {
        return null;
    }

    /**
     * Sets the path of the file that was validated.
     * 
     * Parameters:
     * filePath - the path of the file that was validated
     * @param filePath the path of the file that was validated
     */
    public void setFilePath(String filePath) {
    }
}

