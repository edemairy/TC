package com.ibm.ps.codereview;

import java.io.OutputStream;

/**
 * This is a contract for change log writer. It defines method for writing info to given CVS log entry (described by CvsLogEntry class) to the given change log stream (format is up to implementations).
 * Implementations of this interface are not required to be thread-safe.
 */
public interface ChangeLogWriter {
    /**
     * Writes entry to given log stream. The stream will not be closed or flushed by this method.
     * 
     * ---Parameters---
     * logStream - The log stream where to write.
     * logEntry - The CVS log entity from which the information should be written to the given log stream.
     * 
     * ---Exceptions---
     * IllegalArgumentException if any argument is null or logEntry.revisions is null or logEntry.workingFile is null/empty or any field of any of the elements of logEntry.revisions is null (or author is empty or revision is empty).
     * ChangeLogException if any error occurs with writing the log.
     * @param logStream 
     * @param logEntry 
     */
    public void write(OutputStream logStream, CvsLogEntry logEntry);
}

