package com.ibm.tools.ffvt;

import java.lang.*;
import com.ibm.tools.ffvt.*;

/**
 * This interface represents a file format validator. It defines a method for validating a file identified with a file path. Implementations of this interface must provide a constructor that accepts only configuration file path to be compatible with FileFormatValidationTool.
 * 
 * Thread Safety:
 * Implementations of this interface must be thread safe.
 */
public interface FileFormatValidator {
    /**
     * Validates a file with the specified path.
     * 
     * Parameters:
     * filePath - the path of the file to be validated
     * 
     * Returns:
     * the validation result (not null)
     * 
     * Throws:
     * IllegalArgumentException if filePath is null/empty
     * FileFormatValidationException if some fatal error occurred when performing the validation (exception is not thrown when file has invalid format)
     * @throws IllegalArgumentException if filePath is null/empty
     * @throws FileFormatValidationException if some fatal error occurred when performing the validation (exception is not thrown when file has invalid format)
     * @param filePath the path of the file to be validated
     * @return the validation result (not null)
     */
    public ValidationResult validate(String filePath);
}

