package com.ibm.tools.ffvt;

import java.lang.*;
import com.ibm.tools.ffvt.*;

/**
 * This interface represents a file type validation report generator. It defines a method for generating a text report for the given ValidationResult instance. Implementations of this interface must provide a constructor that accepts only Properties instance with configuration parameters to be compatible with FileFormatValidationTool.
 * 
 * Thread Safety:
 * Implementations of this interface must be thread safe assuming that the provided streams and entities are used by the caller in thread safe manner.
 */
public interface ValidationReportGenerator {
    /**
     * Generates a text report for the given validation result.
     * 
     * Parameters:
     * validationResult - the validation result
     * 
     * Returns:
     * the generated text report for the given validation result (not null)
     * 
     * Throws:
     * IllegalArgumentException if validationResult is null, validationResult.getExpectedFormatName() is null/empty, validationResult.getTimestamp() is null
     * ValidationReportGenerationException if some error occurred when generating a report for file type validation result
     * @throws IllegalArgumentException if validationResult is null, validationResult.getExpectedFormatName() is null/empty, validationResult.getTimestamp() is null
     * @throws ValidationReportGenerationException if some error occurred when generating a report for file type validation result
     * @param validationResult the validation result
     * @return the generated text report for the given validation result (not null)
     */
    public String generate(ValidationResult validationResult);
}

