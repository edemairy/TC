package com.ibm.tools.ffvt;

import java.lang.*;

/**
 * This runtime exception is thrown by most of classes defined in this component when some error occurs while initializing the class instance using the provided configuration.
 * 
 * Thread Safety:
 * This class is not thread safe because its base class is not thread safe.
 */
public class FileFormatValidatorConfigurationException extends RuntimeException {
    /**
     * Creates a new instance of this exception with the given message.
     * 
     * Parameters:
     * message - the detailed error message of this exception
     * @param message the detailed error message of this exception
     */
    public FileFormatValidatorConfigurationException(String message) {
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
    public FileFormatValidatorConfigurationException(String message, Throwable cause) {
    }
}

