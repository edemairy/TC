package com.ibm.tools.ffvt.text.validators;

import java.lang.*;

/**
 * This class is an abstract implementation of FieldsValidator that is a base class for all validators that can validate a single field with configured index only. This class uses Apache Commons Logging library to perform logging of errors and debug information.
 * 
 * Thread Safety:
 * This class is immutable and thread safe when provided collections are used by the caller in thread safe manner.
 * It's assumed that internal logging implementation used by Apache Commons Logging library is thread safe.
 */
public abstract class SingleFieldValidator implements FieldsValidator {
    /**
     * The 0-based index of the field to be validated. Is initialized in the constructor and never changed after that. Cannot be negative. Is used in validate(). Has a protected getter.
     */
    private final int fieldIndex;

    /**
     * The value indicating whether field validation must be skipped in case if the field value is empty. Is initialized in the constructor and never changed after that. Is used in validate().
     */
    private final boolean skipIfEmpty;

    /**
     * The Apache Commons logger to be used by this class for logging errors and debug information. Is initialized during construction and never changed after that. Cannot be null. Is used in validate(). Has a protected getter to be used by subclasses.
     */
    private final Log log = LogFactory.getLog(this.getClass());

    /**
     * Creates an instance of SingleFieldValidator.
     * See section 3.2.6 of CS for details about the configuration parameters. Please use value constraints provided in that section to check whether values read from the configuration element are valid.
     * 
     * Parameters:
     * configElement - the configuration DOM element
     * 
     * Throws:
     * IllegalArgumentException if configElement is null
     * FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * 
     * Implementation Notes:
     * 1. fieldIndex = Integer.parseInt(configElement.getAttribute("fieldIndex")) - 1; // convert 1-based to 0-based
     * 2. skipIfEmpty = configElement.getAttribute("skipIfEmpty").equalsIgnoreCase("yes");
     * @throws IllegalArgumentException if configElement is null
     * @throws FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * @param configElement the configuration DOM element
     */
    protected SingleFieldValidator(Element configElement) {
    }

    /**
     * Validates the given row field values and if any error is found, adds the error description to the given errors list.
     * 
     * Parameters:
     * fieldValues - the field values to be validated
     * errors - the list for found errors
     * 
     * Throws:
     * IllegalArgumentException if fieldValues or errors is null
     * 
     * Implementation Notes:
     * 1. If fieldValues.size() <= fieldIndex then
     *      1.1. errors.add("field at position " + (fieldIndex + 1) + " is missing");
     *      1.2. Return.
     * 2. String fieldValue = fieldValues.get(fieldIndex);
     * 3. If fieldValue == null then return.
     * 4. If fieldValue.trim().length() == 0 and skipIfEmpty then return.
     * 5. validate(fieldValue, errors);
     * @throws IllegalArgumentException if fieldValues or errors is null
     * @param errors the list for found errors
     * @param fieldValues the field values to be validated
     */
    public synchronized void validate(List<String> fieldValues, List<String> errors) {
    }

    /**
     * Validates the given field value and if any error is found, adds the error description to the given errors list.
     * 
     * Parameters:
     * fieldValue - the field value to be validated
     * errors - the list for found errors
     * 
     * Throws:
     * IllegalArgumentException if fieldValue or errors is null
     * @throws IllegalArgumentException if fieldValue or errors is null
     * @param errors the list for found errors
     * @param fieldValue the field value to be validated
     */
    protected abstract void validate(String fieldValue, List<String> errors);

    /**
     * Retrieves the Apache Commons logger to be used by subclasses for logging errors and debug information.
     * 
     * Returns:
     * the Apache Commons logger to be used by subclasses for logging errors and debug information
     * @return the Apache Commons logger to be used by subclasses for logging errors and debug information
     */
    protected Log getLog() {
        return null;
    }

    /**
     * Retrieves the 0-based index of the field to be validated.
     * 
     * Returns:
     * the 0-based index of the field to be validated
     * @return the 0-based index of the field to be validated
     */
    protected int getFieldIndex() {
        return 0;
    }
}

