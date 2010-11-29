package com.ibm.tools.ffvt.text.validators;

import java.lang.*;

/**
 * This is a base class for all single field validators that compare a numeric field value with some predefined number. This class extends SingleFieldComparisonValidator that holds the comparison type to be used.
 * 
 * Thread Safety:
 * This class is immutable and thread safe when provided collections are used by the caller in thread safe manner.
 */
public class NumberComparisonValidator extends SingleFieldComparisonValidator {
    /**
     * The constant value to be used for comparison. Is initialized in the constructor and never changed after that. Cannot be null. Is used in validate().
     */
    private final BigDecimal value;

    /**
     * Creates an instance of NumberComparisonValidator.
     * See section 3.2.10 of CS for details about the configuration parameters. Please use value constraints provided in that section to check whether values read from the configuration element are valid.
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
     * 2. value = new BigDecimal(configElement.getAttribute("value"));
     * @throws IllegalArgumentException if configElement is null
     * @throws FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * @param configElement the configuration DOM element
     */
    public NumberComparisonValidator(Element configElement) {
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
     * 1. fieldNumber = new BigDecimal(fieldValue);
     * 2. If parsing error occurred then
     *      2.1. errors.add("field value at position " + (getFieldIndex() + 1) + " (" + fieldValue + ") should be a number");
     *      2.2. Return.
     * 3. int result = fieldNumber.compareTo(value);
     * 4. boolean success = checkComparisonResult(result);
     * 5. If not success then
     *      5.1. errors.add("field value at position " + (getFieldIndex() + 1) + " (" + fieldValue + ") must be " + getComparisonType().toString().toLowerCase().replace('_', ' ').replace("equal", "equal to") + " " + value.toString());
     * @throws IllegalArgumentException if fieldValue or errors is null
     * @param errors the list for found errors
     * @param fieldValue the field value to be validated
     */
    protected synchronized void validate(String fieldValue, List<String> errors) {
    }
}

