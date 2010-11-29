package com.ibm.tools.ffvt.text.validators;

import java.lang.*;

/**
 * This class is an implementation of FieldsValidator that compares two date/time field values to each other when validating them.
 * 
 * Thread Safety:
 * This class is immutable and thread safe when provided collections are used by the caller in thread safe manner.
 */
public class TwoDateFieldsComparisonValidator extends TwoFieldsComparisonValidator {
    /**
     * The date/time format to be used for parsing field values. Is initialized in the constructor and never changed after that. Cannot be null or empty. Is used in validate().
     */
    private final String format;

    /**
     * Creates an instance of TwoDateFieldsComparisonValidator.
     * See section 3.2.13 of CS for details about the configuration parameters. Please use value constraints provided in that section to check whether values read from the configuration element are valid.
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
     * @throws IllegalArgumentException if configElement is null
     * @throws FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * @param configElement the configuration DOM element
     */
    public TwoDateFieldsComparisonValidator(Element configElement) {
    }

    /**
     * Validates the given field values and if any error is found, adds the error description to the given errors list.
     * 
     * Parameters:
     * fieldValue1 - the first field value to be validated
     * fieldValue2 - the second field value to be validated
     * errors - the list for found errors
     * 
     * Throws:
     * IllegalArgumentException if fieldValue1, fieldValue2 or errors is null
     * 
     * Implementation Notes:
     * 1. dateFormat = new SimpleDateFormat(format);
     * 2. dateFormat.setLenient(false);
     * 3. Date dateFieldValue1 = dateFormat.parse(fieldValue1);
     * 4. If parsing error occurred then
     *      4.1. errors.add("field value at position " + (getFieldIndex1() + 1) + " (" + fieldValue1 + ") cannot be parsed as date with predefined format (" + format + ")");
     *      4.2. Return.
     * 5. Date dateFieldValue2 = dateFormat.parse(fieldValue2);
     * 6. If parsing error occurred then
     *      6.1. errors.add("field value at position " + (getFieldIndex2() + 1) + " (" + fieldValue2 + ") cannot be parsed as date with predefined format (" + format + ")");
     *      6.2. Return.
     * 7. int result = dateFieldValue1.compareTo(dateFieldValue2);
     * 8. boolean success = checkComparisonResult(result);
     * 9. If not success then
     *      9.1. errors.add("field value at position " + (getFieldIndex1() + 1) + " (" + fieldValue1 + ") must be " + getComparisonType().toString().toLowerCase().replace('_', ' ').replace("equal", "equal to") + " field value at position " + (getFieldIndex2() + 1) + " (" + fieldValue2 + ")");
     * @throws IllegalArgumentException if fieldValue1, fieldValue2 or errors is null
     * @param errors the list for found errors
     * @param fieldValue2 the second field value to be validated
     * @param fieldValue1 the first field value to be validated
     */
    protected synchronized void validate(String fieldValue1, String fieldValue2, List<String> errors) {
    }
}

