/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * <p>
 * This tests Mapper.
 * </p>
 *
 * @author WishingBone, justforplay
 * @version 1.1
 * @since 1.0
 */
public class MapperTestCase extends TestCase {

    /**
     * Test functionality of Mapper.
     */
    public void testMapper() {
        try {
            Mapper m = new Mapper();
            assertNull(m.getMap());
            Map map = new HashMap();
            m.setMap(map);
            map = m.getMap();
            assertNotNull(map);
            m = new Mapper(map);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
