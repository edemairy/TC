package com.ibm.tools.ffvt.text.extractors;

import java.lang.*;

/**
 * This class is an implementation of FieldExtractor that can extract field values from CSV rows. This class supports not only a comma as a separator, but any separator matched with the configurable regexp pattern. This class uses Apache Commons Logging library to perform logging of errors and debug information.
 * 
 * Thread Safety:
 * This class is immutable and thread safe.
 * It's assumed that internal logging implementation used by Apache Commons Logging library is thread safe.
 */
public class CSVFieldExtractor implements FieldExtractor {
    /**
     * The regular expression for separator that separates field values in a line. Is initialized in the constructor and never changed after that. Cannot be null/empty. Is used in extract().
     */
    private final String separatorPattern;

    /**
     * The Apache Commons logger to be used by this class for logging errors and debug information. Is initialized during construction and never changed after that. Cannot be null. Is used in validate().
     */
    private final Log log = LogFactory.getLog(this.getClass());

    /**
     * Creates an instance of CSVFieldExtractor.
     * See section 3.2.4 of CS for details about the configuration parameters. Please use value constraints provided in that section to check whether values read from the configuration element are valid.
     * 
     * Parameters:
     * configElement - the configuration DOM element
     * 
     * Throws:
     * IllegalArgumentException if configElement is null
     * FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * 
     * Implementation Notes:
     * 1. If configElement.hasAttribute("separator") then
     *      1.1. separatorPattern = configElement.getAttribute("separator");
     * 2. Else
     *      2.1. separatorPattern = ",";
     * @throws IllegalArgumentException if configElement is null
     * @throws FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * @param configElement the configuration DOM element
     */
    public CSVFieldExtractor(Element configElement) {
    }

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
     * 
     * Implementation Notes:
     * 1. String[] values = line.split(separatorPattern, -1);
     * 2. result = new ArrayList<String>();
     * 3. For each value from values do:
     *      3.1. result.add(value);
     * 4. Return result.
     * @throws IllegalArgumentException if line is null
     * @param line the text line from which field values must be extracted
     * @return the list with field values (not null)
     */
    public synchronized List<String> extract(String line) {
        return null;
    }
}

