package com.ibm.tools.ffvt.text.extractors;

/**
 * This is an inner class of FixedLengthFieldExtractor that is a container for information about a single field of the file that has fixed length field format. It is a simple JavaBean (POJO) that provides getters and setters for all private attributes and performs no argument validation in the setters.
 * 
 * Thread Safety:
 * This class is mutable and not thread safe.
 */
private class FieldParams {
    /**
     * The start position of the field in a line (0-based index). Can be any value. Has getter and setter.
     */
    private int startPos;

    /**
     * The end position of the field in a line (0-based, not inclusive). Can be any value. Has getter and setter.
     */
    private int endPos;

    /**
     * The value indicating whether trimming of the field value is required. Has getter and setter.
     */
    private boolean trimmingRequired;

    /**
     * Creates an instance of FieldParams.
     * 
     * Implementation Notes:
     * Do nothing.
     */
    public FieldParams() {
    }

    /**
     * Retrieves the start position of the field in a line (0-based index).
     * 
     * Returns:
     * the start position of the field in a line (0-based index)
     * @return the start position of the field in a line (0-based index)
     */
    public int getStartPos() {
        return 0;
    }

    /**
     * Sets the start position of the field in a line (0-based index).
     * 
     * Parameters:
     * startPos - the start position of the field in a line (0-based index)
     * @param startPos the start position of the field in a line (0-based index)
     */
    public void setStartPos(int startPos) {
    }

    /**
     * Retrieves the end position of the field in a line (0-based, not inclusive).
     * 
     * Returns:
     * the end position of the field in a line (0-based, not inclusive)
     * @return the end position of the field in a line (0-based, not inclusive)
     */
    public int getEndPos() {
        return 0;
    }

    /**
     * Sets the end position of the field in a line (0-based, not inclusive).
     * 
     * Parameters:
     * endPos - the end position of the field in a line (0-based, not inclusive)
     * @param endPos the end position of the field in a line (0-based, not inclusive)
     */
    public void setEndPos(int endPos) {
    }

    /**
     * Retrieves the value indicating whether trimming of the field value is required.
     * 
     * Returns:
     * the value indicating whether trimming of the field value is required
     * @return the value indicating whether trimming of the field value is required
     */
    public boolean isTrimmingRequired() {
        return false;
    }

    /**
     * Sets the value indicating whether trimming of the field value is required.
     * 
     * Parameters:
     * trimmingRequired - the value indicating whether trimming of the field value is required
     * @param trimmingRequired the value indicating whether trimming of the field value is required
     */
    public void setTrimmingRequired(boolean trimmingRequired) {
    }
}

