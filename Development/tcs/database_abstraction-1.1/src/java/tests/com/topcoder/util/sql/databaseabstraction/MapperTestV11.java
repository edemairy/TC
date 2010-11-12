/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.util.HashMap;

import junit.framework.TestCase;

/**
 * Test case for Mapper.
 *
 * @author justforplay
 * @version 1.1
 * @since 1.1
 */
public class MapperTestV11 extends TestCase {

    /**
     * <p>
     * Test Mapper().
     * </p>
     * <p>
     * Verify:Mapper can be instantiated correctly.
     * </p>
     */
    public void testMapper() {
        Mapper mapper = new Mapper();
        assertNotNull("Unable to instantiated Mapper.", mapper);
        assertNull("Mapper.map is not initialized correctly.", mapper.getMap());
    }

    /**
     * <p>
     * Test Mapper(Map).
     * </p>
     * <p>
     * Verify:Mapper can be instantiated correctly.
     * </p>
     */
    public void testMapper1() {
        HashMap map = new HashMap();
        ImplConverter converter = new ImplConverter();
        map.put("TEST", converter);
        Mapper mapper = new Mapper(map);
        assertNotNull("Unable to instantiated Mapper.", mapper);
        assertEquals("Mapper.map is not initialized correctly.", mapper.getMap().get("test"), converter);
    }

    /**
     * <p>
     * Test getMap().
     * </p>
     * <p>
     * Verify:map can be retrieved correctly.
     * </p>
     */
    public void testGetMap() {
        HashMap map = new HashMap();
        ImplConverter converter = new ImplConverter();
        map.put("TEST", converter);
        Mapper mapper = new Mapper(map);
        assertNotNull("Unable to instantiated Mapper.", mapper);
        assertEquals("map can't be retrieved correctly.", mapper.getMap().get("test"), converter);

    }

    /**
     * <p>
     * Test setMap(Map).
     * </p>
     * <p>
     * Verify: setMap is correct.
     * </p>
     */
    public void testSetMap() {
        HashMap map = new HashMap();
        ImplConverter converter = new ImplConverter();
        map.put("TEST", converter);
        Mapper mapper = new Mapper();
        assertNotNull("Unable to instantiated Mapper.", mapper);
        mapper.setMap(map);
        assertEquals("setMap is incorrect.", mapper.getMap().get("test"), converter);

    }

}
