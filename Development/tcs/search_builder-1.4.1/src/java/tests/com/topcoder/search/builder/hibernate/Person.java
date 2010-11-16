/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate;


/**
 * <p>
 * Person class used for testing.
 * </p>
 *
 * @author myxgyy
 * @version 1.4
 */
public class Person {
    /**
     * The id of the person.
     */
    private Integer id;

    /**
     * The name of the person.
     */
    private String name;

    /**
     * The sex of the person.
     */
    private String sex;

    /**
     * The age of the person.
     */
    private Integer age;

    /**
     * The company of the person.
     */
    private Company company;

    /**
     * Get the age.
     *
     * @return the age.
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Set the age.
     *
     * @param age
     *            the age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * Get the company.
     *
     * @return the company.
     */
    public Company getCompany() {
        return company;
    }

    /**
     * Set the company.
     *
     * @param company
     *            the company
     */
    public void setCompany(Company company) {
        this.company = company;
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
     * Get the sex.
     *
     * @return the sex.
     */
    public String getSex() {
        return sex;
    }

    /**
     * Setter for sex.
     *
     * @param sex
     *            the sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }
}
