package com.ibm.tools.ffvt.text;

import java.lang.*;
import java.util.List;

/**
 * This interface represents a field extractor to be used by TextFileFormatValidator. It defines a method for extracting field values from a text line. Implementations of this interface must provide a constructor that accepts only configuration DOM Element instance to be compatible with TextFileFormatValidator.
 * 
 * Thread Safety:
 * Implementations of this interface must be thread safe.
 */
public interface FieldExtractor {
    /**
     * Extracts the field values from the given text line.
     * 
     * Parameters:
     * line - the text line from which field values must be extracted
     * 
     * Returns:
     * the list with field values (not null)
     * 
     * Throws:
     * IllegalArgumentException if line is null
     * FieldExtractionException if some error occurred when extracting the field values (e.g. line has invalid format)
     * @throws IllegalArgumentException if line is null
     * @throws FieldExtractionException if some error occurred when extracting the field values (e.g. line has invalid format)
     * @param line the text line from which field values must be extracted
     * @return the list with field values (not null)
     */
    public List<String> extract(String line);
}

