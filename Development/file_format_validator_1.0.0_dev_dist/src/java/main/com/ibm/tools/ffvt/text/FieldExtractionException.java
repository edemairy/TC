package com.ibm.tools.ffvt.text;

import com.ibm.tools.ffvt.FileFormatValidationException;
import java.lang.*;

/**
 * This exception is thrown by implementations of FieldExtractor when some error occurs while extracting the field values (e.g. line has invalid format).
 * 
 * Thread Safety:
 * This class is not thread safe because its base class is not thread safe.
 */
public class FieldExtractionException extends FileFormatValidationException {
    /**
     * Creates a new instance of this exception with the given message.
     * 
     * Parameters:
     * message - the detailed error message of this exception
     * @param message the detailed error message of this exception
     */
    public FieldExtractionException(String message) {
        FileFormatValidationException(message);
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
    public FieldExtractionException(String message, Throwable cause) {
    }
}

