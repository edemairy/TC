/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import com.topcoder.util.config.ConfigManager;

import java.io.File;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Test Helper for the component.
 *
 * @author mgmg
 * @version 2.0
 */
public final class TestHelper {
    /**
     * Private test helper.
     */
    private TestHelper() {
    }

    /**
     * Get the URL string of the file.
     *
     * @param fileName the file name
     * @return the url string.
     */
    public static String getURLString(String fileName) {
        return "file:///" + new File(fileName).getAbsolutePath();
    }

    /**
     * Load a config file to ConfigManager.
     *
     * @throws Exception exception to the caller.
     */
    public static void loadSingleConfigFile() throws Exception {
        clearConfig();
        ConfigManager.getInstance().add("config.xml");
    }

    /**
     * Clear all the configs.
     *
     * @throws Exception exception to the caller.
     */
    public static void clearConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator it = cm.getAllNamespaces();
        List nameSpaces = new ArrayList();

        while (it.hasNext()) {
            nameSpaces.add(it.next());
        }

        for (int i = 0; i < nameSpaces.size(); i++) {
            cm.removeNamespace((String) nameSpaces.get(i));
        }
    }
}
