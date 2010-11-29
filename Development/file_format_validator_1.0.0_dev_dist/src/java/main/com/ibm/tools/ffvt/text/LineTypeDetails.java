package com.ibm.tools.ffvt.text;

import com.ibm.tools.ffvt.text.*;
import java.util.List;
import java.util.regex.*;

/**
 * This is an inner class of TextFileFormatValidator that is a container for configuration parameters associated with a single line type. It is a simple JavaBean (POJO) that provides getters and setters for all private attributes and performs no argument validation in the setters.
 * 
 * Thread Safety:
 * This class is mutable and not thread safe.
 */
class LineTypeDetails {
    /**
     * The minimum number of lines of this type that have to be present in the validated file. If equal to -1, this means that no minimum limit is set. Can be any value. Has getter and setter.
     */
    private int minLinesNum = -1;

    /**
     * The maximum number of lines of this type that can be present in the validated file. If equal to -1, this means that no maximum limit is set. Can be any value. Has getter and setter.
     */
    private int maxLinesNum = -1;

    /**
     * The exact number of lines of this type that have to be present in the validated file. If equal to -1, this means that expected number of lines is not limited or limit is specified with a range (see minLinesNum and maxLinesNum). Can be any value. Has getter and setter.
     */
    private int exactLinesNum = -1;

    /**
     * The regular expression pattern that identifies lines of this type. If null, lines will be identified by indices. Can be any value. Has getter and setter.
     */
    private Pattern pattern;

    /**
     * The field extractor to be used for this line type. Can be any value. Has getter and setter.
     */
    private FieldExtractor fieldExtractor;

    /**
     * The fields validators to be used for this line type. Can be any value, can contain any values. Has getter and setter.
     */
    private List<FieldsValidator> fieldsValidators;

    /**
     * Creates an instance of LineTypeDetails.
     * 
     * Implementation Notes:
     * Do nothing.
     */
    public LineTypeDetails() {
    }

    /**
     * Retrieves the minimum number of lines of this type that have to be present in the validated file.
     * 
     * Returns:
     * the minimum number of lines of this type that have to be present in the validated file
     * @return the minimum number of lines of this type that have to be present in the validated file
     */
    public int getMinLinesNum() {
        return 0;
    }

    /**
     * Sets the minimum number of lines of this type that have to be present in the validated file.
     * 
     * Parameters:
     * minLinesNum - the minimum number of lines of this type that have to be present in the validated file
     * @param minLinesNum the minimum number of lines of this type that have to be present in the validated file
     */
    public void setMinLinesNum(int minLinesNum) {
    }

    /**
     * Retrieves the maximum number of lines of this type that can be present in the validated file.
     * 
     * Returns:
     * the maximum number of lines of this type that can be present in the validated file
     * @return the maximum number of lines of this type that can be present in the validated file
     */
    public int getMaxLinesNum() {
        return 0;
    }

    /**
     * Sets the maximum number of lines of this type that can be present in the validated file.
     * 
     * Parameters:
     * maxLinesNum - the maximum number of lines of this type that can be present in the validated file
     * @param maxLinesNum the maximum number of lines of this type that can be present in the validated file
     */
    public void setMaxLinesNum(int maxLinesNum) {
    }

    /**
     * Retrieves the exact number of lines of this type that have to be present in the validated file.
     * 
     * Returns:
     * the exact number of lines of this type that have to be present in the validated file
     * @return the exact number of lines of this type that have to be present in the validated file
     */
    public int getExactLinesNum() {
        return 0;
    }

    /**
     * Sets the exact number of lines of this type that have to be present in the validated file.
     * 
     * Parameters:
     * exactLinesNum - the exact number of lines of this type that have to be present in the validated file
     * @param exactLinesNum the exact number of lines of this type that have to be present in the validated file
     */
    public void setExactLinesNum(int exactLinesNum) {
    }

    /**
     * Retrieves the regular expression pattern that identifies lines of this type.
     * 
     * Returns:
     * the regular expression pattern that identifies lines of this type
     * @return the regular expression pattern that identifies lines of this type
     */
    public Pattern getPattern() {
        return null;
    }

    /**
     * Sets the regular expression pattern that identifies lines of this type.
     * 
     * Parameters:
     * pattern - the regular expression pattern that identifies lines of this type
     * @param pattern the regular expression pattern that identifies lines of this type
     */
    public void setPattern(Pattern pattern) {
    }

    /**
     * Retrieves the field extractor to be used for this line type.
     * 
     * Returns:
     * the field extractor to be used for this line type
     * @return the field extractor to be used for this line type
     */
    public FieldExtractor getFieldExtractor() {
        return null;
    }

    /**
     * Sets the field extractor to be used for this line type.
     * 
     * Parameters:
     * fieldExtractor - the field extractor to be used for this line type
     * @param fieldExtractor the field extractor to be used for this line type
     */
    public void setFieldExtractor(FieldExtractor fieldExtractor) {
    }

    /**
     * Retrieves the fields validators to be used for this line type.
     * 
     * Returns:
     * the fields validators to be used for this line type
     * @return the fields validators to be used for this line type
     */
    public List<FieldsValidator> getFieldsValidators() {
        return null;
    }

    /**
     * Sets the fields validators to be used for this line type.
     * 
     * Parameters:
     * fieldsValidators - the fields validators to be used for this line type
     * @param fieldsValidators the fields validators to be used for this line type
     */
    public void setFieldsValidators(List<FieldsValidator> fieldsValidators) {
    }
}

