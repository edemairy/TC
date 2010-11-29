package com.ibm.tools.ffvt.text;

import java.util.List;

/**
 * This interface represents a fields validator. It defines a method for validating the specified row field values. Implementations of this interface must provide a constructor that accepts only configuration DOM Element instance to be compatible with TextFileFormatValidator.
 * 
 * Thread Safety:
 * Implementations of this interface are required to be thread safe when collections passed to them are used by the caller in thread safe manner.
 */
public interface FieldsValidator {
    /**
     * Validates the given row field values and if any error is found, adds the error description to the given errors list.
     * 
     * Parameters:
     * fieldValues - the field values to be validated
     * errors - the list for found errors
     * 
     * Throws:
     * IllegalArgumentException if fieldValues or errors is null
     * @throws IllegalArgumentException if fieldValues or errors is null
     * @param errors the list for found errors
     * @param fieldValues the field values to be validated
     */
    public void validate(List<String> fieldValues, List<String> errors);
}

