package com.ibm.tools.ffvt.text;

import com.ibm.tools.ffvt.text.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class holds information that is specific to a file type validation context. Separate ValidationContext instance is created and used by TextFileFormatValidator for each validated file.
 * 
 * Thread Safety:
 * This class is mutable and not thread safe. But it is always used by TextFileFormatValidator in thread safe manner.
 */
public class ValidationContext {

    /**
     * The list of validation errors. Collection instance is initialized in the constructor and never changed after that. Cannot be null, cannot contain null/empty. Has a getter. Is used in addError().
     */
    private final List<String> errors;
    /**
     * The mapping from line type to the number of lines of this type that have been already processed. Collection instance is initialized in the constructor and never changed after that. Cannot be null, cannot contain null key/value or not positive value. Is used in increaseProcessedLinesNum() and getProcessedLinesNumByType().
     */
    private final Map<LineType, Integer> processedLinesNum;
    /**
     * The 0-based index of the currently processed line. Can be nay value. Has getter and setter.
     */
    private int lineIndex;
    /**
     * The 0-based position of the current line from the end of the file. Can be any value. Has getter and setter.
     */
    private int positionFromEnd;

    /**
     * Creates an instance of ValidationContext.
     * 
     * Implementation Notes:
     * 1. errors = new ArrayList<String>();
     * 2. processedLinesNum = new HashMap<LineType, Integer>();
     */
    public ValidationContext() {
        errors = new ArrayList<String>();
        processedLinesNum = new HashMap<LineType, Integer>();
    }

    /**
     * Adds the given validation error to the context.
     * 
     * Parameters:
     * error - the error to be added
     * 
     * Throws:
     * IllegalArgumentException if error is null/empty
     * 
     * Implementation Notes:
     * 1. errors.add(error);
     * @throws IllegalArgumentException if error is null/empty
     * @param error the error to be added
     */
    public void addError(String error) {
        if (error == null) throw new IllegalArgumentException("error cannot be null");
        if (error == "") throw new IllegalArgumentException("error cannot be empty");
        errors.add(error);
    }

    /**
     * Retrieves the list of validation errors.
     * 
     * Returns:
     * the list of validation errors
     * 
     * Implementation Notes:
     * 1. Return new ArrayList<String>(errors);
     * @return the list of validation errors
     */
    public List<String> getErrors() {
        return new ArrayList<String>(errors);
    }

    /**
     * Increases the processed lines number for the specified line type.
     * 
     * Parameters:
     * type - the line type
     * 
     * Throws:
     * IllegalArgumentException if type is null
     * 
     * Implementation Notes:
     * 1. Integer num = processedLinesNum.get(type);
     * 2. If num == null then num = 0.
     * 3. num = num + 1;
     * 4. processedLinesNum.put(type, num);
     * @throws IllegalArgumentException if type is null
     * @param type the line type
     */
    public void increaseProcessedLinesNum(LineType type) {
        if (type==null) throw new IllegalArgumentException("type cannot be null");
        Integer num = processedLinesNum.get(type);
        if (num == null) {
            num = 0;
        }
        num = num + 1;
        this.processedLinesNum.put(type, num);
    }

    /**
     * Retrieves the processed lines number for the specified line type.
     * 
     * Parameters:
     * type - the line type
     * 
     * Returns:
     * the retrieved processed lines number
     * 
     * Throws:
     * IllegalArgumentException if type is null
     * 
     * Implementation Notes:
     * 1. Integer num = processedLinesNum.get(type);
     * 2. Return (num == null ? 0 : num).
     * @throws IllegalArgumentException if type is null
     * @param type the line type
     * @return the retrieved processed lines number
     */
    public int getProcessedLinesNumByType(LineType type) {
        if (type==null) throw new IllegalArgumentException("type cannot be null");
        Integer num = processedLinesNum.get(type);
        return (num == null ? 0 : num);
    }

    /**
     * Retrieves the 0-based index of the currently processed line.
     * 
     * Returns:
     * the 0-based index of the currently processed line
     * @return the 0-based index of the currently processed line
     */
    public int getLineIndex() {
        return this.lineIndex;
    }

    /**
     * Sets the 0-based index of the currently processed line.
     * 
     * Parameters:
     * lineIndex - the 0-based index of the currently processed line
     * @param lineIndex the 0-based index of the currently processed line
     */
    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    /**
     * Retrieves the 0-based position of the current line from the end of the file.
     * 
     * Returns:
     * the 0-based position of the current line from the end of the file
     * @return the 0-based position of the current line from the end of the file
     */
    public int getPositionFromEnd() {
        return this.positionFromEnd;
    }

    /**
     * Sets the 0-based position of the current line from the end of the file.
     * 
     * Parameters:
     * positionFromEnd - the 0-based position of the current line from the end of the file
     * @param positionFromEnd the 0-based position of the current line from the end of the file
     */
    public void setPositionFromEnd(int positionFromEnd) {
        this.positionFromEnd = positionFromEnd;
    }
}
