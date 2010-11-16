/**
 * Copyright (C) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.TestResult;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.naming.Context;

import com.topcoder.util.idgenerator.ejb.*;

/**
 * Tests public methods of IDGeneratorImpl class
 *
 * @author garyk
 * @version 2.0
 */
public class TestIDGeneratorEJB extends TestCase {
    /* The id name */
    private static final String ID_NAME = "accuracytests_ejb";

    /* The number to start from */
    private static final long START_NUM = 400;

    /* The block size */
    private static final long BLOCK_SIZE_NUM = 50;

    public void testIDGeneratorEJB() throws Exception {
        long expectedID = START_NUM;
        long nextID;

        Context jndiContext = new InitialContext();
        Object obj = jndiContext.lookup("IDGeneratorHome");
        IDGeneratorHome home = (IDGeneratorHome)
            PortableRemoteObject.narrow(obj, IDGeneratorHome.class);
      
        IDGenerator gen = home.create();

        assertNotNull("The generator should not be null", gen);

        /* Get some ids */
        for (long i = 0; i < 2 * BLOCK_SIZE_NUM; i++, expectedID++) {
            nextID = gen.getNextID(ID_NAME);

            assertEquals("The next id should be " + expectedID, 
                expectedID, nextID);
        }
    }


}
