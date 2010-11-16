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

import com.topcoder.util.idgenerator.*;
import com.topcoder.util.idgenerator.ejb.IDGeneratorHome;


/**
 * This tests the situation that results in IDsExhaustedException thrown</p>
 *
 * @author garyk
 * @version 2.0
 */
public class TestIDsExhaustedException extends TestCase {
    /* The id name 1 */
    private static final String ID_NAME1 = "accuracytests_exh_ex1";

    /* The id name 2 */
    private static final String ID_NAME2 = "accuracytests_exh_ex2";

    /* The id name 3 */
    private static final String ID_NAME3 = "accuracytests_exh_ex3";

    /* The id name 4 */
    private static final String ID_NAME4 = "accuracytests_exh_ex4";

    /* The id name 5 */
    private static final String ID_NAME5 = "accuracytests_exh_ex5";

    /* The id name 6 */
    private static final String ID_NAME6 = "accuracytests_exh_ex6";

    /* The block size */
    private static final long BLOCK_SIZE_NUM = 3;

    /* The number to start from */
    private static final long START_NUM = Long.MAX_VALUE - 2;

    public void testIDGeneratorFactory() throws Exception {
        long expectedID = START_NUM;
        long nextID;

        IDGenerator gen = IDGeneratorFactory.getIDGenerator(ID_NAME1);

        /* Get some ids */
        for (long i = 0; i < BLOCK_SIZE_NUM; i++, expectedID++) {
            nextID = gen.getNextID();

            assertEquals("The next id should be " + expectedID, 
                expectedID, nextID);
        }

        try {
            nextID = gen.getNextID();

            // bad, exception not thrown
            fail("Should have thrown IDsExhaustedException");
        } catch (IDsExhaustedException e) {
            // good, caught as expected
        }


        gen = IDGeneratorFactory.getIDGenerator(ID_NAME2);

        try {
            nextID = gen.getNextID();

            // bad, exception not thrown
            fail("Should have thrown IDsExhaustedException");
        } catch (IDsExhaustedException e) {
            // good, caught as expected
        }
    }

    public void testIDGeneratorImpl() throws Exception {
        long expectedID = START_NUM;
        long nextID;

        IDGenerator gen = IDGeneratorFactory.getIDGenerator(ID_NAME3);

        /* Get some ids */
        for (long i = 0; i < BLOCK_SIZE_NUM; i++, expectedID++) {
            nextID = gen.getNextID();

            assertEquals("The next id should be " + expectedID, 
                expectedID, nextID);
        }

        try {
            nextID = gen.getNextID();

            // bad, exception not thrown
            fail("Should have thrown IDsExhaustedException");
        } catch (IDsExhaustedException e) {
            // good, caught as expected
        }

        gen = IDGeneratorFactory.getIDGenerator(ID_NAME4);

        try {
            nextID = gen.getNextID();

            // bad, exception not thrown
            fail("Should have thrown IDsExhaustedException");
        } catch (IDsExhaustedException e) {
            // good, caught as expected
        }
    }

    public void testIDGeneratorEJB() throws Exception {
        long expectedID = START_NUM;
        long nextID;

        Context jndiContext = new InitialContext();
        Object obj = jndiContext.lookup("IDGeneratorHome");
        IDGeneratorHome home = (IDGeneratorHome)
            PortableRemoteObject.narrow(obj, IDGeneratorHome.class);
      
        com.topcoder.util.idgenerator.ejb.IDGenerator gen = home.create();

        /* Get some ids */
        for (long i = 0; i < BLOCK_SIZE_NUM; i++, expectedID++) {
            nextID = gen.getNextID(ID_NAME5);

            assertEquals("The next id should be " + expectedID, 
                expectedID, nextID);
        }

        try {
            nextID = gen.getNextID(ID_NAME5);

            // bad, exception not thrown
            fail("Should have thrown IDsExhaustedException");
        } catch (IDsExhaustedException e) {
            // good, caught as expected
        }

        try {
            nextID = gen.getNextID(ID_NAME6);

            // bad, exception not thrown
            fail("Should have thrown IDsExhaustedException");
        } catch (IDsExhaustedException e) {
            // good, caught as expected
        }
    }

}