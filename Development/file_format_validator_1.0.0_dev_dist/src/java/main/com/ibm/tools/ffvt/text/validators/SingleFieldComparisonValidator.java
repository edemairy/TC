package com.ibm.tools.ffvt.text.validators;

import com.ibm.tools.ffvt.text.validators.*;

/**
 * This is a base class for all single field validators that compare a field value with some predefined constant.
 * 
 * Thread Safety:
 * This class is immutable and thread safe when provided collections are used by the caller in thread safe manner.
 */
public abstract class SingleFieldComparisonValidator extends SingleFieldValidator {
    /**
     * The comparison type to be used by this class. The field value goes as the first operand, fixed value - as the second operand. Is initialized in the constructor and never changed after that. Cannot be null. Has a protected getter. Is used in checkComparisonResult().
     */
    private final ComparisonType comparisonType;

    /**
     * Creates an instance of SingleFieldComparisonValidator.
     * See section 3.2.9 of CS for details about the configuration parameters. Please use value constraints provided in that section to check whether values read from the configuration element are valid.
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
     * 2. String comparisonTypeStr = configElement.getAttribute("operator");
     * 3. Convert comparisonTypeStr to comparisonType:ComparisonType using the following mapping and case insensitive matching (move this logic to a helper class to avoid code duplication):
     *      "equal" - ComparisonType.EQUAL
     *      "notEqual" - ComparisonType.NOT_EQUAL
     *      "lessThan" - ComparisonType.LESS_THAN
     *      "greaterThan" - ComparisonType.GREATER_THAN
     *      "lessOrEqual" - ComparisonType.LESS_OR_EQUAL
     *      "greaterOrEqual" - ComparisonType.GREATER_OR_EQUAL
     * @throws IllegalArgumentException if configElement is null
     * @throws FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * @param configElement the configuration DOM element
     */
    protected SingleFieldComparisonValidator(Element configElement) {
    }

    /**
     * Checks the given comparison result.
     * 
     * Parameters:
     * result - the comparison result: 0 if both operands are equal, negative if field value is less than fixed value, positive if field value is greater than fixed value
     * 
     * Returns:
     * true if validation succeeded, false if validation failed
     * 
     * Implementation Notes:
     * 1. If comparisonType == ComparisonType.EQUAL then
     *      1.1. Return (result == 0).
     * 2. If comparisonType == ComparisonType.NOT_EQUAL then
     *      2.1. Return (result != 0).
     * 3. If comparisonType == ComparisonType.LESS_THAN then
     *      3.1. Return (result < 0).
     * 4. If comparisonType == ComparisonType.GREATER_THAN then
     *      4.1. Return (result > 0).
     * 5. If comparisonType == ComparisonType.LESS_OR_EQUAL then
     *      5.1. Return (result <= 0).
     * 6. Return (result >= 0).
     * 
     * Note: logic from SingleFieldComparisonValidator#checkComparisonResult() and TwoFieldsComparisonValidator#checkComparisonResult() must be moved to a helper class to avoid code duplication.
     * @param result the comparison result: 0 if both operands are equal, negative if field value is less than fixed value, positive if field value is greater than fixed value
     * @return true if validation succeeded, false if validation failed
     */
    protected boolean checkComparisonResult(int result) {
        return false;
    }

    /**
     * Retrieves the comparison type to be used by this class.
     * 
     * Returns:
     * the comparison type to be used by this class
     * @return the comparison type to be used by this class
     */
    protected ComparisonType getComparisonType() {
        return null;
    }
}

