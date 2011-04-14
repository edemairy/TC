package com.ibm.ps.codereview;

import com.ibm.ps.codereview.*;
import java.io.InputStream;

/**
 * This is a contract for CVS log reader. It defines method for reading next entry (described by CvsLogRevisionEntry class) from the given log stream.
 * Implementations of this interface are not required to be thread-safe.
 */
public interface CvsLogReader {
    /**
     * Reads next entry from given log stream. Returns null if there no more entries left there. This method doesn't close the stream.
     * 
     * ---Parameters---
     * logStream - The stream of the CVS log file.
     * return - The entity read from the given log stream or null if there are no more entries to read.
     * 
     * ---Exceptions---
     * IllegalArgumentException if given is null.
     * CvsLogException if any error occurs with reading the log (I/O error or unexpected data format).
     * @param logStream 
     * @return 
     */
    public CvsLogEntry readNextEntry(InputStream logStream);
}

