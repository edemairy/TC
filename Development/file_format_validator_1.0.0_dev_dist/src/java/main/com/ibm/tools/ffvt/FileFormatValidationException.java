package com.ibm.tools.ffvt;

import java.lang.*;

/**
 * This exception is thrown by implementations of FileFormatValidator when some fatal error occurs while performing the validation (exception is not thrown when file has invalid format). Also this exception is used as a base class for other specific custom exceptions.
 * 
 * Thread Safety:
 * This class is not thread safe because its base class is not thread safe.
 */
public class FileFormatValidationException extends Exception {
    /**
     * Creates a new instance of this exception with the given message.
     * 
     * Parameters:
     * message - the detailed error message of this exception
     * @param message the detailed error message of this exception
     */
    public FileFormatValidationException(String message) {
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
    public FileFormatValidationException(String message, Throwable cause) {
    }
}

