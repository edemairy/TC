/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache;

import java.io.Serializable;


/**
 * <p>
 * A serializable object that used in testing and demo as the object to put in the cache.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class Foo implements Serializable {
    /**
     * The name.
     */
    private String name;

    /**
     * The age.
     */
    private int age;

    /**
     * This is for making this POJO larger for demo purpose.
     */
    private byte[] bytes = new byte[1000];

    /**
     * Creates the new instance.
     */
    public Foo() {
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
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the age.
     *
     * @return the age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Set the age.
     *
     * @param age the age to set.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Get the bytes.
     *
     * @return the bytes.
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Set the bytes.
     *
     * @param bytes the bytes to set.
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
