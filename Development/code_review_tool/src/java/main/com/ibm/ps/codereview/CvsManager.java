package com.ibm.ps.codereview;

import java.util.*;
import java.lang.*;

/**
 * This is a contract for CVS manager. It defines methods for CVS operations, specifically the "checkout", "log", "tag" and "diff" operations. It also defines setters for configuring the working directory (the directory to which the projects will be checked out, where the files for log generation will be looked for, etc) and CVSROOT.
 * Implementations of this interface are not required to be thread-safe.
 */
public interface CvsManager {
    /**
     * Checks out the specified project. The folders and files will be put into configured working directory.
     * 
     * ---Parameters---
     * project - The name of project to checkout.
     * 
     * ---Exceptions---
     * IllegalArgumentException if argument is null/empty.
     * CvsOperationException if any error occurs with performing CVS operation.
     * @param project 
     */
    public void checkout(String project);

    /**
     * Checks out the specified branch of specified project. The folders and files will be put into configured working directory.
     * 
     * ---Parameters---
     * project - The name of project to checkout.
     * branch - The branch to checkout.
     * 
     * ---Exceptions---
     * IllegalArgumentException if any argument is null/empty.
     * CvsOperationException if any error occurs with performing CVS operation.
     * @param project 
     * @param branch 
     */
    public void checkout(String project, String branch);

    /**
     * Generates the full change log for all files of all the projects checked out to the configured working directory.
     * 
     * ---Parameters---
     * return - The content of the generated log.
     * 
     * ---Exceptions---
     * CvsOperationException if any error occurs with performing CVS operation.
     * @return 
     */
    public String log();

    /**
     * Generates the partial change log (since specified date) for all files of all the projects checked out to the configured working directory.
     * 
     * ---Parameters---
     * beginDate - The date since which the changes should be looked for.
     * return - The content of the generated log.
     * 
     * ---Exceptions---
     * IllegalArgumentException if argument is null.
     * CvsOperationException if any error occurs with performing CVS operation.
     * @param beginDate 
     * @return 
     */
    public String log(Date beginDate);

    /**
     * Creates tag for all files of all the projects checked out to the configured working directory.
     * 
     * ---Parameters---
     * tag - The name of tag to create.
     * return - The stream containing the generated log.
     * 
     * ---Exceptions---
     * IllegalArgumentException if argument is null/empty.
     * CvsOperationException if any error occurs with performing CVS operation.
     * @param tag 
     */
    public void tag(String tag);

    /**
     * Generates the difference log (between specified revisions) for specified file.
     * 
     * ---Parameters---
     * beginRevision - The revisiosn since which the changes should be looked for.
     * endsRevision - The revisiosn due which the changes should be looked for.
     * filename - The filename (with either full path or path relative to working directory) of file for which the difference log should be generated.
     * return - The content of the generated difference log.
     * 
     * ---Exceptions---
     * IllegalArgumentException if any argument is null/empty.
     * CvsOperationException if any error occurs with performing CVS operation.
     * @param endRevision 
     * @param beginRevision 
     * @param filename 
     * @return 
     */
    public String diff(String beginRevision, String endRevision, String filename);

    /**
     * Sets the working directory for all CVS commands.
     * 
     * ---Parameters---
     * directory - The working directory for all CVS commands.
     * 
     * ---Exceptions---
     * IllegalArgumentException if argument is null/empty.
     * @param directory 
     */
    public void setDirectory(String directory);

    /**
     * Sets the value which will be populated to CVSROOT environment variable before invoking CVS commands.
     * 
     * ---Parameters---
     * cvsRoot - The value which will be populated to CVSROOT environment variable before invoking CVS commands.
     * 
     * ---Exceptions---
     * IllegalArgumentException if argument is null/empty.
     * @param cvsRoot 
     */
    public void setCvsRoot(String cvsRoot);
}

