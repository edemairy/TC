/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.failuretests;

import java.io.Serializable;

/**
 * the class to cache, only used for demo.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class Dummy implements Serializable {
    /**
     * <p>
     * the serial Version UID.
     * </p>
     */
    private static final long serialVersionUID = 6790475365530606697L;
    /**
     * <p>
     * the name.
     * </p>
     */
    private String name;
    /**
     * <p>
     * the age.
     * </p>
     */
    private int age;

    /**
     * <p>
     * This is for making this POJO larger for demo purpose.
     * </p>
     */
    private byte[] bytes = new byte[1000];

    /**
     * <p>
     * getter for name.
     *
     * @return the name.
     * </p>
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * setter for name.
     *
     * @param name
     *            the name to set.
     * </p>
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>
     * getter for the age.
     *
     * @return the age
     * </p>
     */
    public int getAge() {
        return age;
    }

    /**
     * <p>
     * setter for the age.
     *
     * @param age
     *            the age to set.
     * </p>
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * <p>
     * getter for the bytes.
     *
     * @return the bytes.
     * </p>
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * <p>
     * setter for the bytes.
     *
     * @param bytes
     *            the bytes to set
     * </p>
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
