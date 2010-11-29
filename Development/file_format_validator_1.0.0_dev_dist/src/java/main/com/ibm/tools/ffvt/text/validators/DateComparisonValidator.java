package com.ibm.tools.ffvt.text.validators;

import java.util.*;
import java.lang.*;

/**
 * This is a base class for all single field validators that compare a date/time field value with some predefined date/time constant. This class extends SingleFieldComparisonValidator that holds the comparison type to be used.
 * 
 * Thread Safety:
 * This class is immutable and thread safe when provided collections are used by the caller in thread safe manner.
 */
public class DateComparisonValidator extends SingleFieldComparisonValidator {
    /**
     * The date/time format to be used for parsing field value. Is initialized in the constructor and never changed after that. Cannot be null or empty. Is used in validate().
     */
    private final String format;

    /**
     * The constant date/time value to be used for comparison. Is initialized in the constructor and never changed after that. Cannot be null. Is used in validate().
     */
    private final Date value;

    /**
     * Creates an instance of DateComparisonValidator.
     * See section 3.2.11 of CS for details about the configuration parameters. Please use value constraints provided in that section to check whether values read from the configuration element are valid.
     * 
     * Parameters:
     * configElement - the configuration DOM element
     * 
     * Throws:
     * IllegalArgumentException if configElement is null
     * FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * 
     * Implementation Notes:
     * 1. super(configElement);
     * 2. format = configElement.getAttribute("format");
     * 3. value = new SimpleDateFormat(format).parse(configElement.getAttribute("value"));
     * @throws IllegalArgumentException if configElement is null
     * @throws FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * @param configElement the configuration DOM element
     */
    public DateComparisonValidator(Element configElement) {
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
     * 
     * Implementation Notes:
     * 1. dateFormat = new SimpleDateFormat(format);
     * 2. dateFormat.setLenient(false);
     * 3. Date dateFieldValue = dateFormat.parse(fieldValue);
     * 4. If parsing error occurred then
     *      4.1. errors.add("field value at position " + (getFieldIndex() + 1) + " (" + fieldValue + ") cannot be parsed as date with predefined format (" + format + ")");
     *      4.2. Return.
     * 5. int result = dateFieldValue.compareTo(value);
     * 6. boolean success = checkComparisonResult(result);
     * 7. If not success then
     *      7.1. errors.add("field value at position " + (getFieldIndex() + 1) + " (" + fieldValue + ") must be " + getComparisonType().toString().toLowerCase().replace('_', ' ').replace("equal", "equal to") + " " + dateFormat.format(value));
     * @throws IllegalArgumentException if fieldValue or errors is null
     * @param errors the list for found errors
     * @param fieldValue the field value to be validated
     */
    protected synchronized void validate(String fieldValue, List<String> errors) {
    }
}

