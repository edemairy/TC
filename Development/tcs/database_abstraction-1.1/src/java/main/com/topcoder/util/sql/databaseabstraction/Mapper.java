/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * The Mapper class simply maintains a column name to Converter map of the mapping to apply when the remap
 * method of the CustomResultSet class is called. This class is simply a wrapper around a HashMap.
 * </p>
 * <p>
 * This class of version 1.1 is the same with that of version 1.0.
 * </p>
 * <p>
 * Thread Safety: - This class is mutable, and not thread-safe.
 * </p>
 *
 * @author argolite, WishingBone
 * @author aubergineanode, justforplay
 * @version 1.1
 * @since 1.0
 */
public class Mapper {

    /**
     * <p>
     * The mapping of column names to Converter instances. The names in the map are all non-null String values
     * (all lowercase) and the values in the map are all non-null Converter instances. The map can be
     * retrieved through the getMap function and set through the setMap function.
     * </p>
     */
    private Map map;

    /**
     * Creates a new Mapper with an initially empty map.
     */
    public Mapper() {
        // do nothing.
    }

    /**
     * Creates a new Mapper from the given map.
     *
     * @param map The map that defines the mapping of column values.
     */
    public Mapper(Map map) {
        setMap(map);
    }

    /**
     * Get the mapping from String names to Converters. Return a clone of the map field.
     *
     * @return The mapping from column names to Converters
     */
    public Map getMap() {
        return map;
    }

    /**
     * Sets the mapping to the given map. To be consistent with the previous version, this method copies all
     * pairs in the map for which the key is a non-null String and the value is a non-null Converter. The
     * {key, value} pair is put into the map field using the lower case version of the string key. Pairs in
     * the argument map which do not meet the type restrictions are simply ignored.
     *
     * @param map The mapping to replace the current one in this mapper with
     */
    public void setMap(Map map) {
        if (map == null) {
            this.map = null;
        } else {
            this.map = new HashMap();
            for (Iterator itr = map.entrySet().iterator(); itr.hasNext();) {
                Map.Entry entry = (Map.Entry) itr.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key instanceof String && value instanceof Converter) {
                    this.map.put(((String) key).toLowerCase(), value);
                }
            }
        }
    }
}
