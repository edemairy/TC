package com.ibm.tools.ffvt.text.validators;

import java.lang.*;
import com.ibm.tools.ffvt.text.validators.*;

/**
 * This class is an implementation of FieldsValidator that checks whether a field value can be converted to a predefined type.
 * 
 * Thread Safety:
 * This class is immutable and thread safe when provided collections are used by the caller in thread safe manner.
 */
public class FieldTypeValidator extends SingleFieldValidator {
    /**
     * The expected field type. Is initialized in the constructor and never changed after that. Cannot be null. Is used in validate().
     */
    private final FieldType fieldType;

    /**
     * The format of the value to be used. Currently format is supported for Date field type only. Is initialized in the constructor and never changed after that. Cannot be null or empty. Is used in validate().
     */
    private final String format;

    /**
     * Creates an instance of FieldTypeValidator.
     * See section 3.2.8 of CS for details about the configuration parameters. Please use value constraints provided in that section to check whether values read from the configuration element are valid.
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
     * 2. String fieldTypeStr = configElement.getAttribute("fieldType");
     * 3. If fieldTypeStr.equalsIgnoreCase("Integer") then
     *      3.1. fieldType = FieldType.INTEGER;
     * 4. Else if fieldTypeStr.equalsIgnoreCase("BigDecimal") then
     *      4.1. fieldType = FieldType.BIG_DECIMAL;
     * 5. Else if fieldTypeStr.equalsIgnoreCase("Date") then
     *      5.1. fieldType = FieldType.DATE;
     * 6. Else throw FileFormatValidatorConfigurationException.
     * 7. If configElement.hasAttribute("format") then
     *      7.1. format = configElement.getAttribute("format");
     * 8. Else format = null;
     * @throws IllegalArgumentException if configElement is null
     * @throws FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * @param configElement the configuration DOM element
     */
    public FieldTypeValidator(Element configElement) {
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
     * 1. If fieldType == FieldType.INTEGER then
     *      1.1. Integer.parseInt(fieldValue);
     *      1.2. If parsing error occurred then
     *              1.2.1. errors.add("field value at position " + (getFieldIndex() + 1) + " (" + fieldValue + ") cannot be parsed as integer");
     * 2. Else if fieldType == FieldType.BIG_DECIMAL then
     *      2.1. new BigDecimal(fieldValue);
     *      2.2. If parsing error occurred then
     *              2.2.1. errors.add("field value at position " + (getFieldIndex() + 1) + " (" + fieldValue + ") cannot be parsed as big decimal");
     * 3. Else
     *      3.1. dateFormat = new SimpleDateFormat(format);
     *      3.2. dateFormat.setLenient(false);
     *      3.2. dateFormat.parse(fieldValue);
     *      3.3. If parsing error occurred then
     *              3.3.1. errors.add("field value at position " + (getFieldIndex() + 1) + " (" + fieldValue + ") cannot be parsed as date with predefined format (" + format + ")");
     * @throws IllegalArgumentException if fieldValue or errors is null
     * @param errors the list for found errors
     * @param fieldValue the field value to be validated
     */
    protected synchronized void validate(String fieldValue, List<String> errors) {
    }
}

