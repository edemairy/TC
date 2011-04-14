package com.ibm.ps.codereview;

import java.lang.*;
import java.util.List;

/**
 * This class describes a CVS log entry.
 * It has empty default constructor and several fields, each with getter and setter. Setters don't perform any validation.
 * This class is mutable and not thread-safe.
 */
public class CvsLogEntry {
    /**
     * RCS filename.
     * This varialbe is intially null and fully mutable, has getter and setter with no validation.
     */
    private String rcsFile;

    /**
     * Work filename.
     * This varialbe is intially null and fully mutable, has getter and setter with no validation.
     */
    private String workingFile;

    /**
     * The name of branch from which the file is.
     * This varialbe is intially null and fully mutable, has getter and setter with no validation.
     */
    private String branch;

    /**
     * File head revision.
     * This varialbe is intially null and fully mutable, has getter and setter with no validation.
     */
    private String headRevision;

    /**
     * File description.
     * This varialbe is intially null and fully mutable, has getter and setter with no validation.
     */
    private String description;

    /**
     * File revision entries.
     * This varialbe is intially null and fully mutable, has getter and setter with no validation.
     */
    private List<CvsLogRevisionEntry> revisions;

    /**
     * Default empty constructor.
     */
    public CvsLogEntry() {
    }

    /**
     * Gets RCS filename.
     * 
     * ---Parameters---
     * return - RCS filename.
     * @return 
     */
    public String getRcsFile() {
        return null;
    }

    /**
     * Sets RCS filename.
     * 
     * ---Parameters---
     * rcsFile - RCS filename.
     * @param rcsFile 
     */
    public void setRcsFile(String rcsFile) {
    }

    /**
     * Gets working filename.
     * 
     * ---Parameters---
     * return - Working filename.
     * @return 
     */
    public String getWorkingFile() {
        return null;
    }

    /**
     * Sets working filename.
     * 
     * ---Parameters---
     * workingFile - Working filename.
     * @param workingFile 
     */
    public void setWorkingFile(String workingFile) {
    }

    /**
     * Gets file head revision.
     * 
     * ---Parameters---
     * return - File head revision.
     * @return 
     */
    public String getHeadRevision() {
        return null;
    }

    /**
     * Sets file head revision.
     * 
     * ---Parameters---
     * headRevision - File head revision.
     * @param headRevision 
     */
    public void setHeadRevision(String headRevision) {
    }

    /**
     * Gets file description.
     * 
     * ---Parameters---
     * return - File description.
     * @return 
     */
    public String getDescription() {
        return null;
    }

    /**
     * Sets file description.
     * 
     * ---Parameters---
     * description - File description.
     * @param description 
     */
    public void setDescription(String description) {
    }

    /**
     * Gets file revision entries.
     * 
     * ---Parameters---
     * return - File revision entries.
     * @return 
     */
    public List<CvsLogRevisionEntry> getRevisions() {
        return null;
    }

    /**
     * Sets file revision entries.
     * 
     * ---Parameters---
     * revisions - File revision entries.
     * @param revisions 
     */
    public void setRevisions(List<CvsLogRevisionEntry> revisions) {
    }

    /**
     * Gets the name of branch from which the file is.
     * 
     * ---Parameters---
     * return - The name of branch from which the file is.
     * @return 
     */
    public String getBranch() {
        return null;
    }

    /**
     * Sets the name of branch from which the file is.
     * 
     * ---Parameters---
     * branch - The name of branch from which the file is.
     * @param branch 
     */
    public void setBranch(String branch) {
    }
}

