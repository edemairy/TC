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
 * This tests the situation that results in NoSuchIDSequenceException thrown</p>
 *
 * @author garyk
 * @version 2.0
 */
public class TestNoSuchIDSequenceException extends TestCase {
    /* The id name */
    private static final String ID_NAME = "accuracytests_no_id_ex";

    public void testIDGeneratorFactory() throws Exception {
        IDGenerator gen = null;

        try {
            gen = IDGeneratorFactory.getIDGenerator(ID_NAME);

            // bad, exception not thrown
            fail("Should have thrown NoSuchIDSequenceException");
        } catch (NoSuchIDSequenceException e) {
            // good, caught as expected            
        }

        try {
            gen = IDGeneratorFactory.getIDGenerator(null);

            // bad, exception not thrown
            fail("Should have thrown NoSuchIDSequenceException");
        } catch (NoSuchIDSequenceException e) {
            // good, caught as expected            
        }
    }

    public void testIDGeneratorImpl() throws Exception {
        IDGenerator gen = null;

        try {
            gen = new IDGeneratorImpl(ID_NAME);

            // bad, exception not thrown
            fail("Should have thrown NoSuchIDSequenceException");
        } catch (NoSuchIDSequenceException e) {
            // good, caught as expected            
        }

        try {
            gen = new IDGeneratorImpl(null);

            // bad, exception not thrown
            fail("Should have thrown NoSuchIDSequenceException");
        } catch (NoSuchIDSequenceException e) {
            // good, caught as expected            
        }
    }

    public void testIDGeneratorEJB() throws Exception {
        Context jndiContext = new InitialContext();
        Object obj = jndiContext.lookup("IDGeneratorHome");
        IDGeneratorHome home = (IDGeneratorHome)
            PortableRemoteObject.narrow(obj, IDGeneratorHome.class);
      
        com.topcoder.util.idgenerator.ejb.IDGenerator gen = home.create();

        try {
            gen.getNextID(ID_NAME);

            // bad, exception not thrown
            fail("Should have thrown NoSuchIDSequenceException");
        } catch (NoSuchIDSequenceException e) {
            // good, caught as expected            
        }

        try {
            gen.getNextID(null);

            // bad, exception not thrown
            fail("Should have thrown NoSuchIDSequenceException");
        } catch (NoSuchIDSequenceException e) {
            // good, caught as expected            
        }
    }

}