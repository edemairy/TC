/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate;


/**
 * <p>
 * Company class used for testing.
 * </p>
 *
 * @author myxgyy
 * @version 1.4
 */
public class Company {
    /**
     * Id of the company.
     */
    private Integer id;

    /**
     * Name of the company.
     */
    private String name;

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     *
     * @param name
     *            the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the id.
     *
     * @return the id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the id.
     *
     * @param id
     *            the id
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
