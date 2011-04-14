package com.ibm.ps.codereview.log;

import com.ibm.ps.codereview.*;
import java.io.InputStream;

/**
 * This is a default implementation of CVS log reader. It defines method for reading next entry (described by CvsLogRevisionEntry class) from the given log stream.
 * The format of log entry follows:
 * 
 * RCS file: {rcsFile}
 * Working file: {workingFile}
 * head: {headRevision}
 * branch: {branch}
 * locks: {ignore}
 * access list: {ignore}
 * keyword substitution: {ignore}
 * total revisions: {ignore};	selected revisions: {ignore}
 * description: {description}
 * ----------------------------
 * revision {revision.revision}
 * date: {revision.date};  author: {revision.author};  state: {ignore};  lines: {ignore};
 * {revision.comment}
 * ----------------------------
 * revision {revision.revision}
 * date: {revision.date};  author: {revision.author};  state: {ignore};  lines: {ignore};
 * {revision.comment}
 * =============================================================================
 * 
 * So, an entry is identified as line between "RCF file..." and "===...". Revision entries are locatd between "---..." and either "---..." or "===..." lines. There are could be 0 or more revision entries, each should be popupated to CvsLogEntry.revisions list in the same order as they go in the log.
 * The text between curly brackets refers to the field name (of CvsLogEntry or CvsLogRevisionEntry (if prefixed with "revision.")) to which the corresponding value should be populated.
 * The {revision.date} is formatted as per current locale.
 * This class is immutable and thread-safe as long as invoker uses input parameters thread safely.
 */
public class CvsLogReaderImpl implements CvsLogReader {
    /**
     * Default empty constructor.
     */
    public CvsLogReaderImpl() {
    }

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
     * 
     * ---Implementation notes---
     * 1. Create CvsLogEntry object.
     * 2. Read data from given stream line by line and parse it into a CvsLogEntry object (populate aggregated CvsLogRevisionEntry objects as well) as per format shown in the class docs.
     * 3. Return populate CvsLogEntry object.
     * @param logStream 
     * @return 
     */
    public CvsLogEntry readNextEntry(InputStream logStream) {
        return null;
    }
}

