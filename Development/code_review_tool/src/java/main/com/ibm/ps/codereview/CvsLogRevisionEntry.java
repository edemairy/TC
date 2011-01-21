package com.ibm.ps.codereview;

import java.util.*;
import java.lang.*;

/**
 * This class describes a revision entry of CVS log entry.
 * It has empty default constructor and several fields, each with getter and setter. Setters don't perform any validation.
 * This class is mutable and not thread-safe.
 */
public class CvsLogRevisionEntry {
    /**
     * Revision number.
     * This varialbe is intially null and fully mutable, has getter and setter with no validation.
     */
    private String revision;

    /**
     * Revision date.
     * This varialbe is intially null and fully mutable, has getter and setter with no validation.
     */
    private Date date;

    /**
     * Revision file author.
     * This varialbe is intially null and fully mutable, has getter and setter with no validation.
     */
    private String author;

    /**
     * Revision file comment.
     * This varialbe is intially null and fully mutable, has getter and setter with no validation.
     */
    private String comment;

    /**
     * Default empty constructor.
     */
    public CvsLogRevisionEntry() {
    }

    /**
     * Gets revision number.
     * 
     * ---Parameters---
     * return - Revision number.
     * @return 
     */
    public String getRevision() {
        return null;
    }

    /**
     * Sets revision number.
     * 
     * ---Parameters---
     * revision - Revision number.
     * @param revision 
     */
    public void setRevision(String revision) {
    }

    /**
     * Gets revision date.
     * 
     * ---Parameters---
     * return - Revision date.
     * @return 
     */
    public Date getDate() {
        return null;
    }

    /**
     * Sets revision date.
     * 
     * ---Parameters---
     * date - Revision date.
     * @param date 
     */
    public void setDate(Date date) {
    }

    /**
     * Gets revision file author.
     * 
     * ---Parameters---
     * return - Revision file author.
     * @return 
     */
    public String getAuthor() {
        return null;
    }

    /**
     * Sets revision file author.
     * 
     * ---Parameters---
     * author - Revision file author.
     * @param author 
     */
    public void setAuthor(String author) {
    }

    /**
     * Gets revision file comment.
     * 
     * ---Parameters---
     * return - Revision file comment.
     * @return 
     */
    public String getComment() {
        return null;
    }

    /**
     * Sets revision file comment.
     * 
     * ---Parameters---
     * comment - Revision file comment.
     * @param comment 
     */
    public void setComment(String comment) {
    }
}

