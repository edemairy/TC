package com.ibm.tools.ffvt;

import java.lang.*;

/**
 * This exception is thrown by implementations of ValidationReportGenerator when some error occurs while generating a report for file type validation result.
 * 
 * Thread Safety:
 * This class is not thread safe because its base class is not thread safe.
 */
public class ValidationReportGenerationException extends Exception {
    /**
     * Creates a new instance of this exception with the given message.
     * 
     * Parameters:
     * message - the detailed error message of this exception
     * @param message the detailed error message of this exception
     */
    public ValidationReportGenerationException(String message) {
    }

    /**
     * Creates a new instance of this exception with the given message and cause.
     * 
     * Parameters:
     * message - the detailed error message of this exception
     * cause - the inner cause of this exception
     * @param message the detailed error message of this exception
     * @param cause the inner cause of this exception
     */
    public ValidationReportGenerationException(String message, Throwable cause) {
    }
}

