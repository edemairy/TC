package com.ibm.tools.ffvt.text;

import com.ibm.tools.ffvt.text.*;

/**
 * This is an enumeration for line types that are supported by TextFileFormatValidator.
 * 
 * Thread Safety:
 * This class is immutable and thread safe.
 */
enum LineType {
    /**
     * Represents the header line type.
     */
    HEADER,

    /**
     * Represents the data line type.
     */
    DATA,

    /**
     * Represents the footer line type.
     */
    FOOTER
}

