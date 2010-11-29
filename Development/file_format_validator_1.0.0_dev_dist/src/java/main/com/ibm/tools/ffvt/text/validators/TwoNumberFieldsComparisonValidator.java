package com.ibm.tools.ffvt.text.validators;

import java.lang.*;

/**
 * This class is an implementation of FieldsValidator that compares two numeric field values to each other when validating them.
 * 
 * Thread Safety:
 * This class is immutable and thread safe when provided collections are used by the caller in thread safe manner.
 */
public class TwoNumberFieldsComparisonValidator extends TwoFieldsComparisonValidator {
    /**
     * Creates an instance of TwoNumberFieldsComparisonValidator.
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
     * @throws IllegalArgumentException if configElement is null
     * @throws FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * @param configElement the configuration DOM element
     */
    public TwoNumberFieldsComparisonValidator(Element configElement) {
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
     * 1. fieldNumber1 = new BigDecimal(fieldValue1);
     * 2. If parsing error occurred then
     *      2.1. errors.add("field value at position " + (getFieldIndex1() + 1) + " (" + fieldValue1 + ") should be a number");
     *      2.2. Return.
     * 3. fieldNumber2 = new BigDecimal(fieldValue2);
     * 4. If parsing error occurred then
     *      4.1. errors.add("field value at position " + (getFieldIndex2() + 1) + " (" + fieldValue2 + ") should be a number");
     *      4.2. Return.
     * 5. int result = fieldNumber1.compareTo(fieldNumber2);
     * 6. boolean success = checkComparisonResult(result);
     * 7. If not success then
     *      7.1. errors.add("field value at position " + (getFieldIndex1() + 1) + " (" + fieldValue1 + ") must be " + getComparisonType().toString().toLowerCase().replace('_', ' ').replace("equal", "equal to") + " field value at position " + (getFieldIndex2() + 1) + " (" + fieldValue2 + ")");
     * @throws IllegalArgumentException if fieldValue1, fieldValue2 or errors is null
     * @param errors the list for found errors
     * @param fieldValue2 the second field value to be validated
     * @param fieldValue1 the first field value to be validated
     */
    protected synchronized void validate(String fieldValue1, String fieldValue2, List<String> errors) {
    }
}

