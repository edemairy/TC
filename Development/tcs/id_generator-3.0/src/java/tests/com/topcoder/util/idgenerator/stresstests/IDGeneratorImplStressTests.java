/**
 *
 * Copyright � 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator.stresstests;

import junit.framework.TestCase;

import java.util.Arrays;
import java.io.File;

import com.topcoder.util.idgenerator.*;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>This test case aggregates all Stress test cases for ID Generator v2.</p>
 *
 * @author XuChuan
 * @version 3.0
 */
public class IDGeneratorImplStressTests extends TestCase {
    
    /**
     * Used for stress test
     */
    private class MyThread extends Thread {
        
        private IDGenerator generator = null;
        private int low;
        private int high;
        
        public MyThread(IDGenerator g, int low, int high) {
            this.generator = g;
            this.low = low;
            this.high = high;
        }
        
        public void run() {
            try {
                for (int i = low; i < high; i++) {
                    ids[i] = this.generator.getNextID();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private final static String DB_FACTORY_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";
    
	/**
	 * Array to store the id generated by IDGenerator
	 */
	private long[] ids = new long[10000];
	
	/**
	 * ConfigManager instance used for stress test
	 */
	ConfigManager configManager = null;

    /**
     * Setup the environment
     * @throws Exception if any unexpected exception occurs
     */
    protected void setUp() throws Exception {
        configManager = ConfigManager.getInstance();
        if (configManager.existsNamespace(DB_FACTORY_NAMESPACE)) {
            configManager.removeNamespace(DB_FACTORY_NAMESPACE);
        }
        configManager.add(new File("test_files/stresstest/DBConnectionFactoryImpl.xml").getAbsolutePath());
    }
    
    /**
     * Restore the environment
     * @throws Exception if any unexpected exception occurs
     */
    protected void tearDown() throws Exception {
        if (configManager.existsNamespace(DB_FACTORY_NAMESPACE)) {
            configManager.removeNamespace(DB_FACTORY_NAMESPACE);
        }
    }
    
    /**
	 * Test the getNextID method of the IDGeneratorImpl class
	 * @throws Exception if any unexpected exception occurs
	 */
    public void testGetNextID() throws Exception {
        runGetNextID(IDGeneratorFactory.getIDGenerator("test100"), 100);
        runGetNextID(IDGeneratorFactory.getIDGenerator("test250"), 250);
        runGetNextID(IDGeneratorFactory.getIDGenerator("test500"), 500);
    }

	private void runGetNextID(IDGenerator generator, int b) throws Exception {
	    MyThread[] t = new MyThread[20];
	    long start = System.currentTimeMillis();
		for (int i = 0; i < 20; i++) {
		    t[i] = new MyThread(generator, i * 500, (i + 1) * 500);
		    t[i].start();
		}
		
		for (int i = 0; i < 20; i++) {
		    t[i].join();
		}
		System.out.println("IDGenerator#getNextID: block size " + b + ", 20 threads, 500 invocation each, "
            + (System.currentTimeMillis() - start) + " milliseconds");
		Arrays.sort(ids);
		for (int i = 0; i < ids.length - 1; i++) {
		    assertEquals("Incorrect result", ids[i] + 1, ids[i + 1]);
		}
	}
}
