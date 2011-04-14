package com.ibm.ps.codereview.log;

import com.ibm.ps.codereview.ChangeLogWriter;
import com.ibm.ps.codereview.CvsLogEntry;
import java.io.OutputStream;

/**
 * This is a default implementation of change log writer. It defines method for writing next entry (described by CvsLogEntry class) to the given log stream.
 * The format of log entry looks like this:
 * 
 * {project1}<tab>{path}<tab>{revision}<tab>{date}<tab>{author}<tab>{comment}
 * {project1}<tab>-------------<tab>{revision}<tab>{date}<tab>{author}<tab>{comment}
 * {project1}<tab>-------------<tab>{revision}<tab>{date}<tab>{author}<tab>{comment}
 * {project2}<tab>{path}<tab>{revision}<tab>{date}<tab>{author}<tab>{comment}
 * {project2}<tab>-------------<tab>{revision}<tab>{date}<tab>{author}<tab>{comment}
 * {project2}<tab>-------------<tab>{revision}<tab>{date}<tab>{author}<tab>{comment}
 * 
 * If project has zero revisions, it will not be printed to the log. If project has more than 1 revision, for 2nd and next revisions "-------------" will be printed instead of path.
 * {projectX} states for the name of the project which is determined as a top-level directory in the working file path relative to the CVS log entry (CvsLogEntry.workingFile).
 * {path} states for the working file path relative to the project directory (the CvsLogEntry.workingFile without top-level directory).
 * {revision} states for file revision (CvsLogRevisionEntry.revision).
 * {date} states for date of file revision (CvsLogRevisionEntry.date) in "YYYY/MM/DD hh:mm:ss" format.
 * {author} states for file revision author (CvsLogRevisionEntry.author).
 * {comment} states for file revision comment (CvsLogRevisionEntry.comment).<tab> states for a TAB character.
 * This class is immutable and thread-safe as long as invoker uses input parameters thread safely.
 */
public class ChangeLogWriterImpl implements ChangeLogWriter {
    /**
     * Default empty constructor.
     */
    public ChangeLogWriterImpl() {
    }

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
     * 
     * ---Implementation notes---
     * For each CvsLogRevisionEntry in logEntry.revisions, write line to given stream with revision data in the format specified in the class docs.
     * @param logStream 
     * @param logEntry 
     */
    public void write(OutputStream logStream, CvsLogEntry logEntry) {
    }
}

