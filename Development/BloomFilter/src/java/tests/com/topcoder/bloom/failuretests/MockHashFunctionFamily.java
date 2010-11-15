/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.bloom.failuretests;

import com.topcoder.bloom.HashFunctionFamily;


/**
 * Mock implementation for HashFunctionFamily.
 *
 * @author mgmg
 * @version 1.0
 */
public class MockHashFunctionFamily implements HashFunctionFamily {
    /**
     *
     */
    public int getFunctionCount() {
        return 1;
    }

    /**
     *
     */
    public int computeHash(int functionIndex, int maxHash, Object obj) {
        return obj.hashCode() % (maxHash + 1);
    }

    /**
     *
     */
    public String getSerialized() {
        return "";
    }

    /**
     *
     */
    public int hashCode() {
        return super.hashCode();
    }

    /**
     *
     */
    public boolean equals(Object obj) {
        if (obj instanceof MockHashFunctionFamily) {
            return true;
        } else {
            return false;
        }
    }
}
