package com.ibm.tools.ffvt.text.validators;

import java.lang.*;
import com.ibm.tools.ffvt.text.validators.*;

/**
 * This class is an implementation of FieldsValidator that validates two fields at predefined positions by comparing them to each other using the configured comparison type. This class uses Apache Commons Logging library to perform logging of errors and debug information.
 * 
 * Thread Safety:
 * This class is immutable and thread safe when provided collections are used by the caller in thread safe manner.
 * It's assumed that internal logging implementation used by Apache Commons Logging library is thread safe.
 */
public abstract class TwoFieldsComparisonValidator implements FieldsValidator {
    /**
     * The 0-based index of the first field to be validated. Is initialized in the constructor and never changed after that. Cannot be negative. Is used in validate(). Has a protected getter.
     */
    private final int fieldIndex1;

    /**
     * The 0-based index of the second field to be validated. Is initialized in the constructor and never changed after that. Cannot be negative. Is used in validate(). Has a protected getter.
     */
    private final int fieldIndex2;

    /**
     * The comparison type to be used by this class. The field with index equal to fieldIndex1 goes as the first operand, field with index equal to fieldIndex2 - as the second operand. Is initialized in the constructor and never changed after that. Cannot be null. Has a protected getter. Is used in checkComparisonResult().
     */
    private final ComparisonType comparisonType;

    /**
     * The value indicating whether field validation must be skipped in case if any of two field values is empty. Is initialized in the constructor and never changed after that. Is used in validate().
     */
    private final boolean skipIfEmpty;

    /**
     * The Apache Commons logger to be used by this class for logging errors and debug information. Is initialized during construction and never changed after that. Cannot be null. Is used in validate(). Has a protected getter to be used by subclasses.
     */
    private final Log log = LogFactory.getLog(this.getClass());

    /**
     * Creates an instance of TwoFieldsComparisonValidator.
     * See section 3.2.12 of CS for details about the configuration parameters. Please use value constraints provided in that section to check whether values read from the configuration element are valid.
     * 
     * Parameters:
     * configElement - the configuration DOM element
     * 
     * Throws:
     * IllegalArgumentException if configElement is null
     * FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * 
     * Implementation Notes:
     * 1. fieldIndex1 = Integer.parseInt(configElement.getAttribute("fieldIndex1")) - 1; // convert 1-based to 0-based
     * 2. fieldIndex2 = Integer.parseInt(configElement.getAttribute("fieldIndex2")) - 1; // convert 1-based to 0-based
     * 3. skipIfEmpty = configElement.getAttribute("skipIfEmpty").equalsIgnoreCase("yes");
     * 4. String comparisonTypeStr = configElement.getAttribute("operator");
     * 5. Convert comparisonTypeStr to comparisonType:ComparisonType using the following mapping and case insensitive matching (move this logic to a helper class to avoid code duplication):
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
    protected TwoFieldsComparisonValidator(Element configElement) {
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
     * 1. If fieldValues.size() <= fieldIndex1 then
     *      1.1. errors.add("field at position " + (fieldIndex1 + 1) + " is missing");
     *      1.2. Return.
     * 2. If fieldValues.size() <= fieldIndex2 then
     *      2.1. errors.add("field at position " + (fieldIndex2 + 1) + " is missing");
     *      2.2. Return.
     * 3. String fieldValue1 = fieldValues.get(fieldIndex1);
     * 4. String fieldValue2 = fieldValues.get(fieldIndex2);
     * 5. If fieldValue1 == null or fieldValue2 == null then return.
     * 6. If skipIfEmpty and (fieldValue1.trim().length() == 0 or fieldValue2.trim().length() == 0) then return.
     * 7. validate(fieldValue1, fieldValue2, errors);
     * @throws IllegalArgumentException if fieldValues or errors is null
     * @param errors the list for found errors
     * @param fieldValues the field values to be validated
     */
    public synchronized void validate(List<String> fieldValues, List<String> errors) {
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
     * @throws IllegalArgumentException if fieldValue1, fieldValue2 or errors is null
     * @param errors the list for found errors
     * @param fieldValue2 the second field value to be validated
     * @param fieldValue1 the first field value to be validated
     */
    protected abstract void validate(String fieldValue1, String fieldValue2, List<String> errors);

    /**
     * Checks the given comparison result.
     * 
     * Parameters:
     * result - the comparison result: 0 if both operands are equal, negative if field1 is less than field2, positive if field1 is greater than field2
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
     * @param result the comparison result: 0 if both operands are equal, negative if field1 is less than field2, positive if field1 is greater than field2
     * @return true if validation succeeded, false if validation failed
     */
    protected boolean checkComparisonResult(int result) {
        return false;
    }

    /**
     * Retrieves the 0-based index of the first field to be validated.
     * 
     * Returns:
     * the 0-based index of the first field to be validated
     * @return the 0-based index of the first field to be validated
     */
    protected int getFieldIndex1() {
        return 0;
    }

    /**
     * Retrieves the 0-based index of the second field to be validated.
     * 
     * Returns:
     * the 0-based index of the second field to be validated
     * @return the 0-based index of the second field to be validated
     */
    protected int getFieldIndex2() {
        return 0;
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

    /**
     * Retrieves the Apache Commons logger to be used by this class for logging errors and debug information.
     * 
     * Returns:
     * the Apache Commons logger to be used by this class for logging errors and debug information
     * @return the Apache Commons logger to be used by this class for logging errors and debug information
     */
    protected Log getLog() {
        return null;
    }
}

