/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * Mock initial context factory for unit testing purposes only. Currently the only thing in the
 * context is the SessionFactory, required by hibernate search.
 *
 * @author mumujava
 * @version 1.0
 */
public class MockInitialContextFactory implements InitialContextFactory {
    /**
     * The Hashtable used to get InitialContext.
     *
     * @since 1.1
     */
    public static Hashtable env;

    /**
     * Returns an instance of our mock context that merely points at my MySql database for unit
     * testing purposes only.
     *
     * @param parm1 ignored.
     *
     * @return an initial context that points at the MySql database.
     *
     * @throws NamingException Only if InitialContext's constructor does.
     */
    public Context getInitialContext(final Hashtable parm1)
        throws NamingException {
        env = parm1;

        return new MockContext();
    }
}


/**
 * Extension of initial context that always only ever returns a DataSource to connect to the
 * database used for personal unit testing.
 */
class MockContext extends InitialContext {
    /**
     * The hibernate configuration file..
     */
    private static final String FILE = "sampleHibernateConfig.cfg.xml";    
    /**
     * Represents the SessionFactory. It is initialized in the static block.
     */
    private static final SessionFactory SESSION_FACTORY;
    
    /**
     * Default ctor tells the parent to only lazily instantiate itself.
     *
     * @throws NamingException if InitialContext does
     */
    MockContext() throws NamingException {
        super(true);
    }


    static {
        // Create the SessionFactory from hibernate.cfg.xml
        SESSION_FACTORY = new Configuration().configure(FILE).buildSessionFactory();
    }

    /**
     * In this simple mock implementation, this method only ever returns a SessionFactory object used for unit testing.
     *
     * @param key Ignored
     * @throws NamingException
     * @return a SessionFactory object.
     */
    public Object lookup(final String key) throws NamingException {
        if (key.equals("sessionFactoryJndiName")) {
            return SESSION_FACTORY;
        }
        if (key.equals("object")) {
            return new Object();
        }
        if (key.equals("throwNamingException")) {
            throw new NamingException();
        }
        return null;
    }
}

