/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import junit.framework.TestCase;

/**
 * <p>This tests Column.</p>
 *
 * @author WishingBone, justforplay
 * @version 1.1
 * @since 1.0
 */
public class ColumnTestCase extends TestCase {

    /**
     * Test functionality of Column.
     */
    public void testColumn() {
        try {
            Column c = new Column("a", 1, "b", 2, "c", 3, 4, true, false, true);
            assertTrue(c.getColumnClassName().equals("a"));
            c.setColumnClassName("aa");
            assertTrue(c.getColumnClassName().equals("aa"));
            assertTrue(c.getColumnDisplaySize() == 1);
            assertTrue(c.getColumnLabel().equals("b"));
            c.setColumnLabel("bb");
            assertTrue(c.getColumnLabel().equals("bb"));
            assertTrue(c.getColumnType() == 2);
            assertTrue(c.getColumnTypeName().equals("c"));
            assertTrue(c.getColumnPrecision() == 3);
            assertTrue(c.getColumnScale() == 4);
            assertTrue(c.isAutoIncrement());
            assertFalse(c.isCurrency());
            assertTrue(c.isSigned());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
