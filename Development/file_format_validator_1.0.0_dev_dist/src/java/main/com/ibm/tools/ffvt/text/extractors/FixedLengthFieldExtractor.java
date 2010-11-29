package com.ibm.tools.ffvt.text.extractors;

import java.lang.*;

/**
 * This class is an implementation of FieldExtractor that can extract field values from files that have fixed length field format. This class can optionally trim each retrieved field value. This class uses Apache Commons Logging library to perform logging of errors and debug information.
 * 
 * Thread Safety:
 * This class is immutable and thread safe.
 * It's assumed that internal logging implementation used by Apache Commons Logging library is thread safe.
 */
public class FixedLengthFieldExtractor implements FieldExtractor {
    /**
     * The list of field parameters. Collection instance is initialized in the constructor and never changed after that. Cannot be null/empty. Cannot contain null. Is used in extract().
     */
    private final List<FieldParams> fields;

    /**
     * The Apache Commons logger to be used by this class for logging errors and debug information. Is initialized during construction and never changed after that. Cannot be null. Is used in validate().
     */
    private final Log log = LogFactory.getLog(this.getClass());

    /**
     * Creates an instance of FixedLengthFieldExtractor.
     * See section 3.2.5 of CS for details about the configuration parameters. Please use value constraints provided in that section to check whether values read from the configuration element are valid.
     * 
     * Parameters:
     * configElement - the configuration DOM element
     * 
     * Throws:
     * IllegalArgumentException if configElement is null
     * FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * 
     * Implementation Notes:
     * 1. fields = new ArrayList<FieldParams>();
     * 2. For each fieldElement:Element from configElement.getElementsByTagName("field") do:
     *      2.1. int startPos = Integer.parseInt(fieldElement.getAttribute("startPos"));
     *      2.2. int endPos = Integer.parseInt(fieldElement.getAttribute("endPos"));
     *      2.3. boolean trimmingRequired = fieldElement.getAttribute("trim").equalsIgnoreCase("yes"); // note that "yes" should be used as default
     *      2.4. fieldParams = new FieldParams();
     *      2.5. Set start position to the field parameters (convert to 0-based):
     *              fieldParams.setStartPos(startPos - 1);
     *      2.6. Set end position to the field parameters (convert to not inclusive):
     *              fieldParams.setEndPos(endPos);
     *      2.7. Set trimming required flag to the field parameters:
     *              fieldParams.setTrimmingRequired(trimmingRequired);
     *      2.8. fields.add(fieldParams);
     * @throws IllegalArgumentException if configElement is null
     * @throws FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration element
     * @param configElement the configuration DOM element
     */
    public FixedLengthFieldExtractor(Element configElement) {
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
     * FieldExtractionException if some error occurred when extracting the field values (e.g. line has invalid format)
     * 
     * Implementation Notes:
     * 1. Create a list for result:
     *     result = new ArrayList<String>();
     * 2. For each fieldParams from fields do:
     *      2.1. Get start position from the field parameters:
     *              int startPos = fieldParams.getStartPos();
     *      2.2. Get end position from the field parameters:
     *              int endPos = fieldParams.getEndPos();
     *      2.3. Get trimming required flag from the field parameters:
     *              boolean trimmingRequired = fieldParams.isTrimmingRequired();
     *      2.4. If endPos > line.length() then throw FieldExtractionException.
     *      2.5. Get field value from the line:
     *              String fieldValue = line.substring(startPos, endPos);
     *      2.6. If trimmingRequired then
     *              2.6.1. Trim the field value:
     *                         fieldValue = fieldValue.trim();
     *      2.7. Add field value to the list:
     *              result.add(fieldValue);
     * 3. Return result.
     * @throws IllegalArgumentException if line is null
     * @throws FieldExtractionException if some error occurred when extracting the field values (e.g. line has invalid format)
     * @param line the text line from which field values must be extracted
     * @return the list with field values (not null)
     */
    public synchronized List<String> extract(String line) {
        return null;
    }
}

