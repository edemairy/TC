package com.ibm.tools.ffvt.text.validators;

import com.ibm.tools.ffvt.text.validators.*;

/**
 * This is an enumeration for comparison types supported by SingleFieldComparisonValidator and TwoFieldsComparisonValidator subclasses.
 * 
 * Thread Safety:
 * This class is immutable and thread safe.
 */
public class ComparisonType {
    /**
     * Represents the "equal" comparison type.
     */
    public ComparisonType EQUAL;

    /**
     * Represents the "not equal" comparison type.
     */
    public ComparisonType NOT_EQUAL;

    /**
     * Represents the "less than" comparison type.
     */
    public ComparisonType LESS_THAN;

    /**
     * Represents the "greater than" comparison type.
     */
    public ComparisonType GREATER_THAN;

    /**
     * Represents the "less or equal" comparison type.
     */
    public ComparisonType LESS_OR_EQUAL;

    /**
     * Represents the "greater or equal" comparison type.
     */
    public ComparisonType GREATER_OR_EQUAL;
}

