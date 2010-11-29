package com.ibm.tools.ffvt.text.validators;

import java.util.regex.*;
import java.lang.*;

/**
 * This class is an implementation of FieldsValidator that checks whether a field value matches some predefined regular expression pattern.
 * 
 * Thread Safety:
 * This class is immutable and thread safe when provided collections are used by the caller in thread safe manner.
 */
public class RegexFieldValidator extends SingleFieldValidator {
    /**
     * The regular expression pattern to be used for checking field values. Is initialized in the constructor and never changed after that. Cannot be null. Is used in validate().
     */
    private final Pattern pattern;

    /**
     * Creates an instance of RegexFieldValidator.
     * See section 3.2.7 of CS for details about the configuration parameters. Please use value constraints provided in that section to check whether values read from the configuration element are valid.
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
     * 2. String patternStr = configElement.getAttribute("pattern");
     * 3. pattern = Pattern.compile(patternStr);
     * @throws IllegalArgumentException if configElement is null
     * @throws FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * @param configElement the configuration DOM element
     */
    public RegexFieldValidator(Element configElement) {
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
     * 1. Matcher matcher = pattern.matcher(fieldValue);
     * 2. If not matcher.matches() then
     *      2.1. errors.add("field value at position " + (getFieldIndex() + 1) + " (" + fieldValue + ") is not matched with regexp pattern (" + pattern.pattern() + ")");
     * @throws IllegalArgumentException if fieldValue or errors is null
     * @param errors the list for found errors
     * @param fieldValue the field value to be validated
     */
    protected synchronized void validate(String fieldValue, List<String> errors) {
    }
}

