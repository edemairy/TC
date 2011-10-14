/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.cachemanager;

import java.io.File;
import java.util.Properties;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ibm.support.electronic.cache.ObjectCacheConfigurationException;
import com.ibm.support.electronic.cache.TestHelper;
import com.ibm.support.electronic.cache.dao.CachedObjectDAOImpl;
import com.ibm.support.electronic.cache.memorycache.MemoryCacheImpl;
import com.ibm.support.electronic.cache.printer.CacheStatisticsPrinterImpl;


/**
 * <p>
 * Unit test case of constructors of {@link CacheManagerImpl}.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CacheManagerImplConstructorTest {
    /**
     * <p>
     * The invalid configuration folder.
     * </p>
     */
    private static final String INVALID_CONFIG = TestHelper.TEST_FILES + "invalid" + File.separator;

    /**
     * <p>
     * The Properties instance used for testing.
     * </p>
     */
    private Properties prop;

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CacheManagerImplConstructorTest.class);
    }

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception to jUnit.
     */
    @Before
    public void setUp() throws Exception {
        prop = TestHelper.loadProperties(TestHelper.CONFIG_PROPERTIES);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception to jUnit.
     */
    @After
    public void tearDown() throws Exception {
        prop = null;
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#CacheManagerImpl()}.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testCtor1() throws Exception {
        CacheManagerImpl manager = new CacheManagerImpl();
        Assert.assertNotNull("Unable to instantiate CacheManagerImpl", manager);
        Assert.assertEquals("Incorrect maxMemorySize", Long.parseLong(prop.getProperty("maxMemorySize")) * 1024,
            TestHelper.getField(manager, "maxMemorySize"));
        Assert.assertEquals("Incorrect cachedObjectDAO", prop.getProperty("cachedObjectDAOClass"), TestHelper
            .getField(manager, "cachedObjectDAO").getClass().getName());
        Assert.assertEquals("Incorrect cacheSetName", prop.getProperty("cacheSetName"),
            TestHelper.getField(manager, "cacheSetName"));
        Assert.assertEquals("Incorrect cacheStatisticsPrinter", prop.getProperty("cacheStatisticsPrinterClass"),
            TestHelper.getField(manager, "cacheStatisticsPrinter").getClass().getName());
        Assert.assertEquals("Incorrect memoryCache", prop.getProperty("memoryCacheClass"),
            TestHelper.getField(manager, "memoryCache").getClass().getName());
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when default configuration file
     * classpath is not found.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_MissingConfigFile() throws Exception {
        File configFile = new File(TestHelper.CONFIG_PROPERTIES);
        configFile.delete();

        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when default configuration file contains
     * malformed Unicode escape sequence.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_MalformedUnicodeConfigFile() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager1.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * cachedObjectDAOClass is empty in default configuration file.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_EmptyCachedObjectDAOClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager2.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * cachedObjectDAOClass in default configuration file cannot be found.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_CachedObjectDAOClassNotFound() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager3.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * cachedObjectDAOClass in default configuration file is not a type of CachedObjectDAO.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_InvalidCachedObjectDAOClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager4.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * cachedObjectDAOClass in default configuration file is an abstract class.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_AbstractCachedObjectDAOClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager5.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * cachedObjectDAOClass in default configuration file does not have a constructor that take Properties
     * parameter.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_NoMatchingCtorCachedObjectDAOClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager6.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * cachedObjectDAOClass in default configuration file throws exception when it is instantiated.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_ErrorCachedObjectDAOClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager7.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * cachedObjectDAOClass in default configuration file has inaccessible constructor.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_InaccessibleCachedObjectDAOClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager8.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * memoryCacheClass is empty in default configuration file.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_EmptyMemoryCacheClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager9.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * memoryCacheClass in default configuration file cannot be found.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_MemoryCacheClassNotFound() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager10.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * memoryCacheClass in default configuration file is not a type of MemoryCache.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_InvalidMemoryCacheClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager11.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * memoryCacheClass in default configuration file is an abstract class.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_AbstractMemoryCacheClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager12.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * memoryCacheClass in default configuration file does not have a constructor that take Properties parameter.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_NoMatchingCtorMemoryCacheClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager13.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * memoryCacheClass in default configuration file throws exception when it is instantiated.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_ErrorMemoryCacheClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager14.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * memoryCacheClass in default configuration file has inaccessible constructor.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_InaccessibleMemoryCacheClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager15.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * cacheStatisticsPrinterClass is empty in default configuration file.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_EmptyCacheStatisticsPrinterClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager16.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * cacheStatisticsPrinterClass in default configuration file cannot be found.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_CacheStatisticsPrinterClassNotFound() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager17.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * cacheStatisticsPrinterClass in default configuration file is not a type of CacheStatisticsPrinter.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_InvalidCacheStatisticsPrinterClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager18.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * cacheStatisticsPrinterClass in default configuration file is an abstract class.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_AbstractCacheStatisticsPrinterClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager19.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * cacheStatisticsPrinterClass in default configuration file does not have a constructor that take Properties
     * parameter.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_NoMatchingCtorCacheStatisticsPrinterClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager20.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * cacheStatisticsPrinterClass in default configuration file throws exception when it is instantiated.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_ErrorCacheStatisticsPrinterClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager21.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property
     * cacheStatisticsPrinterClass in default configuration file has inaccessible constructor.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_InaccessibleCacheStatisticsPrinterClass() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager22.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property maxMemorySize
     * in default configuration file is empty.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_EmptyMaxMemorySize() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager23.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property maxMemorySize
     * in default configuration file is negative.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_IllegalMaxMemorySize() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager24.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property maxMemorySize
     * in default configuration file cannot be parsed as long.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_InvalidMaxMemorySize() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager25.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl()} when the value of property cacheSetName
     * in default configuration file is empty.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor1_EmptyCacheSetName() throws Exception {
        // copy the invalid config
        TestHelper.copyFile(INVALID_CONFIG + "invalid_manager26.properties", TestHelper.CONFIG_PROPERTIES);
        try {
            new CacheManagerImpl();
        } finally {
            TestHelper.storeProperties(prop, TestHelper.CONFIG_PROPERTIES);
        }
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)}.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testCtor2() throws Exception {
        CacheManagerImpl manager = new CacheManagerImpl(TestHelper.CONFIG_PROPERTIES);
        Assert.assertNotNull("Unable to instantiate CacheManagerImpl", manager);
        Assert.assertEquals("Incorrect maxMemorySize", Long.parseLong(prop.getProperty("maxMemorySize")) * 1024,
            TestHelper.getField(manager, "maxMemorySize"));
        Assert.assertEquals("Incorrect cachedObjectDAO", prop.getProperty("cachedObjectDAOClass"), TestHelper
            .getField(manager, "cachedObjectDAO").getClass().getName());
        Assert.assertEquals("Incorrect cacheSetName", prop.getProperty("cacheSetName"),
            TestHelper.getField(manager, "cacheSetName"));
        Assert.assertEquals("Incorrect cacheStatisticsPrinter", prop.getProperty("cacheStatisticsPrinterClass"),
            TestHelper.getField(manager, "cacheStatisticsPrinter").getClass().getName());
        Assert.assertEquals("Incorrect memoryCache", prop.getProperty("memoryCacheClass"),
            TestHelper.getField(manager, "memoryCache").getClass().getName());
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when configFilePath is
     * null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2_NullConfigFilePath() {
        new CacheManagerImpl((String) null);
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when configFilePath is
     * empty.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2_EmptyConfigFilePath() {
        new CacheManagerImpl(" ");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when configuration file
     * classpath is not found.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_MissingConfigFile() {
        new CacheManagerImpl(TestHelper.TEST_FILES + "Unknown.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when configuration file
     * contains malformed Unicode escape sequence.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_MalformedUnicodeConfigFile() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager1.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cachedObjectDAOClass is empty in configuration file.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_EmptyCachedObjectDAOClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager2.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cachedObjectDAOClass in configuration file cannot be found.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_CachedObjectDAOClassNotFound() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager3.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cachedObjectDAOClass in configuration file is not a type of CachedObjectDAO.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_InvalidCachedObjectDAOClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager4.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cachedObjectDAOClass in configuration file is an abstract class.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_AbstractCachedObjectDAOClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager5.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cachedObjectDAOClass in configuration file does not have a constructor that take Properties
     * parameter.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_NoMatchingCtorCachedObjectDAOClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager6.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cachedObjectDAOClass in configuration file throws exception when it is instantiated.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_ErrorCachedObjectDAOClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager7.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cachedObjectDAOClass in configuration file has inaccessible constructor.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_InaccessibleCachedObjectDAOClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager8.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property memoryCacheClass is empty in configuration file.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_EmptyMemoryCacheClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager9.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property memoryCacheClass in configuration file cannot be found.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_MemoryCacheClassNotFound() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager10.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property memoryCacheClass in configuration file is not a type of MemoryCache.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_InvalidMemoryCacheClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager11.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property memoryCacheClass in configuration file is an abstract class.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_AbstractMemoryCacheClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager12.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property memoryCacheClass in configuration file does not have a constructor that take Properties parameter.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_NoMatchingCtorMemoryCacheClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager13.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property memoryCacheClass in configuration file throws exception when it is instantiated.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_ErrorMemoryCacheClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager14.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property memoryCacheClass in configuration file has inaccessible constructor.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_InaccessibleMemoryCacheClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager15.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cacheStatisticsPrinterClass is empty in configuration file.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_EmptyCacheStatisticsPrinterClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager16.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cacheStatisticsPrinterClass in configuration file cannot be found.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_CacheStatisticsPrinterClassNotFound() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager17.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cacheStatisticsPrinterClass in configuration file is not a type of CacheStatisticsPrinter.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_InvalidCacheStatisticsPrinterClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager18.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cacheStatisticsPrinterClass in configuration file is an abstract class.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_AbstractCacheStatisticsPrinterClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager19.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cacheStatisticsPrinterClass in configuration file does not have a constructor that take Properties
     * parameter.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_NoMatchingCtorCacheStatisticsPrinterClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager20.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cacheStatisticsPrinterClass in configuration file throws exception when it is instantiated.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_ErrorCacheStatisticsPrinterClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager21.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cacheStatisticsPrinterClass in configuration file has inaccessible constructor.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_InaccessibleCacheStatisticsPrinterClass() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager22.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property maxMemorySize in configuration file is empty.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_EmptyMaxMemorySize() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager23.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property maxMemorySize in configuration file is negative.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_IllegalMaxMemorySize() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager24.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property maxMemorySize in configuration file cannot be parsed as long.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_InvalidMaxMemorySize() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager25.properties");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.lang.String)} when the value of
     * property cacheSetName in configuration file is empty.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor2_EmptyCacheSetName() {
        new CacheManagerImpl(INVALID_CONFIG + "invalid_manager26.properties");
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testCtor3() throws Exception {
        CacheManagerImpl manager = new CacheManagerImpl(prop, "custom");
        Assert.assertNotNull("Unable to instantiate CacheManagerImpl", manager);
        Assert.assertEquals("Incorrect maxMemorySize", Long.parseLong(prop.getProperty("maxMemorySize")) * 1024,
            TestHelper.getField(manager, "maxMemorySize"));
        Assert.assertEquals("Incorrect cachedObjectDAO", prop.getProperty("cachedObjectDAOClass"), TestHelper
            .getField(manager, "cachedObjectDAO").getClass().getName());
        Assert.assertEquals("Incorrect cacheSetName", "custom", TestHelper.getField(manager, "cacheSetName"));
        Assert.assertEquals("Incorrect cacheStatisticsPrinter", prop.getProperty("cacheStatisticsPrinterClass"),
            TestHelper.getField(manager, "cacheStatisticsPrinter").getClass().getName());
        Assert.assertEquals("Incorrect memoryCache", prop.getProperty("memoryCacheClass"),
            TestHelper.getField(manager, "memoryCache").getClass().getName());
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when prop is null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor3_NullProp() {
        new CacheManagerImpl(null, "name");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when cacheSetName is null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor3_NullCacheSetName() {
        new CacheManagerImpl(prop, null);
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when cacheSetName is empty.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor3_EmptyConfigFilePath() {
        new CacheManagerImpl(prop, " ");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property cachedObjectDAOClass is empty in configuration file.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_EmptyCachedObjectDAOClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager2.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property cachedObjectDAOClass in configuration file cannot be found.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_CachedObjectDAOClassNotFound() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager3.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property cachedObjectDAOClass in configuration file is not a type of CachedObjectDAO.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_InvalidCachedObjectDAOClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager4.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property cachedObjectDAOClass in configuration file is an abstract class.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_AbstractCachedObjectDAOClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager5.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property cachedObjectDAOClass in configuration file does not have a constructor that take
     * Properties parameter.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_NoMatchingCtorCachedObjectDAOClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager6.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property cachedObjectDAOClass in configuration file throws exception when it is
     * instantiated.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_ErrorCachedObjectDAOClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager7.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property cachedObjectDAOClass in configuration file has inaccessible constructor.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_InaccessibleCachedObjectDAOClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager8.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property memoryCacheClass is empty in configuration file.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_EmptyMemoryCacheClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager9.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property memoryCacheClass in configuration file cannot be found.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_MemoryCacheClassNotFound() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager10.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property memoryCacheClass in configuration file is not a type of MemoryCache.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_InvalidMemoryCacheClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager11.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property memoryCacheClass in configuration file is an abstract class.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_AbstractMemoryCacheClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager12.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property memoryCacheClass in configuration file does not have a constructor that take
     * Properties parameter.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_NoMatchingCtorMemoryCacheClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager13.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property memoryCacheClass in configuration file throws exception when it is instantiated.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_ErrorMemoryCacheClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager14.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property memoryCacheClass in configuration file has inaccessible constructor.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_InaccessibleMemoryCacheClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager15.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property cacheStatisticsPrinterClass is empty in configuration file.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_EmptyCacheStatisticsPrinterClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager16.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property cacheStatisticsPrinterClass in configuration file cannot be found.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_CacheStatisticsPrinterClassNotFound() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager17.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property cacheStatisticsPrinterClass in configuration file is not a type of
     * CacheStatisticsPrinter.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_InvalidCacheStatisticsPrinterClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager18.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property cacheStatisticsPrinterClass in configuration file is an abstract class.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_AbstractCacheStatisticsPrinterClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager19.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property cacheStatisticsPrinterClass in configuration file does not have a constructor
     * that take Properties parameter.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_NoMatchingCtorCacheStatisticsPrinterClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager20.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property cacheStatisticsPrinterClass in configuration file throws exception when it is
     * instantiated.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_ErrorCacheStatisticsPrinterClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager21.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property cacheStatisticsPrinterClass in configuration file has inaccessible constructor.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_InaccessibleCacheStatisticsPrinterClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager22.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property maxMemorySize in configuration file is empty.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_EmptyMaxMemorySize() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager23.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property maxMemorySize in configuration file is negative.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_IllegalMaxMemorySize() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager24.properties"), "custom");
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties, java.lang.String)}
     * when the value of property maxMemorySize in configuration file cannot be parsed as long.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor3_InvalidMaxMemorySize() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager25.properties"), "custom");
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)}.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testCtor4() throws Exception {
        CacheManagerImpl manager = new CacheManagerImpl(prop);
        Assert.assertNotNull("Unable to instantiate CacheManagerImpl", manager);
        Assert.assertEquals("Incorrect maxMemorySize", Long.parseLong(prop.getProperty("maxMemorySize")) * 1024,
            TestHelper.getField(manager, "maxMemorySize"));
        Assert.assertEquals("Incorrect cachedObjectDAO", prop.getProperty("cachedObjectDAOClass"), TestHelper
            .getField(manager, "cachedObjectDAO").getClass().getName());
        Assert.assertEquals("Incorrect cacheSetName", prop.getProperty("cacheSetName"),
            TestHelper.getField(manager, "cacheSetName"));
        Assert.assertEquals("Incorrect cacheStatisticsPrinter", prop.getProperty("cacheStatisticsPrinterClass"),
            TestHelper.getField(manager, "cacheStatisticsPrinter").getClass().getName());
        Assert.assertEquals("Incorrect memoryCache", prop.getProperty("memoryCacheClass"),
            TestHelper.getField(manager, "memoryCache").getClass().getName());
    }

    /**
     * <p>
     * Accuracy test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when optional
     * properties are not defined.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testCtor4_NoOptional() throws Exception {
        Properties p = new Properties();
        p.setProperty("url", prop.getProperty("url"));
        CacheManagerImpl manager = new CacheManagerImpl(p);
        Assert.assertNotNull("Unable to instantiate CacheManagerImpl", manager);
        Assert.assertEquals("Incorrect maxMemorySize", 0L, TestHelper.getField(manager, "maxMemorySize"));
        Assert.assertEquals("Incorrect cachedObjectDAO", CachedObjectDAOImpl.class.getName(),
            TestHelper.getField(manager, "cachedObjectDAO").getClass().getName());
        Assert.assertEquals("Incorrect cacheSetName", "default", TestHelper.getField(manager, "cacheSetName"));
        Assert.assertEquals("Incorrect cacheStatisticsPrinter", CacheStatisticsPrinterImpl.class.getName(),
            TestHelper.getField(manager, "cacheStatisticsPrinter").getClass().getName());
        Assert.assertEquals("Incorrect memoryCache", MemoryCacheImpl.class.getName(),
            TestHelper.getField(manager, "memoryCache").getClass().getName());
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when prop is null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor4_NullProp() {
        new CacheManagerImpl((Properties) null);
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property cachedObjectDAOClass is empty in configuration file.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_EmptyCachedObjectDAOClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager2.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property cachedObjectDAOClass in configuration file cannot be found.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_CachedObjectDAOClassNotFound() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager3.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property cachedObjectDAOClass in configuration file is not a type of CachedObjectDAO.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_InvalidCachedObjectDAOClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager4.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property cachedObjectDAOClass in configuration file is an abstract class.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_AbstractCachedObjectDAOClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager5.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property cachedObjectDAOClass in configuration file does not have a constructor that take Properties
     * parameter.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_NoMatchingCtorCachedObjectDAOClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager6.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property cachedObjectDAOClass in configuration file throws exception when it is instantiated.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_ErrorCachedObjectDAOClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager7.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property cachedObjectDAOClass in configuration file has inaccessible constructor.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_InaccessibleCachedObjectDAOClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager8.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property memoryCacheClass is empty in configuration file.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_EmptyMemoryCacheClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager9.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property memoryCacheClass in configuration file cannot be found.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_MemoryCacheClassNotFound() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager10.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property memoryCacheClass in configuration file is not a type of MemoryCache.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_InvalidMemoryCacheClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager11.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property memoryCacheClass in configuration file is an abstract class.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_AbstractMemoryCacheClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager12.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property memoryCacheClass in configuration file does not have a constructor that take Properties parameter.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_NoMatchingCtorMemoryCacheClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager13.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property memoryCacheClass in configuration file throws exception when it is instantiated.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_ErrorMemoryCacheClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager14.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property memoryCacheClass in configuration file has inaccessible constructor.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_InaccessibleMemoryCacheClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager15.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property cacheStatisticsPrinterClass is empty in configuration file.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_EmptyCacheStatisticsPrinterClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager16.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property cacheStatisticsPrinterClass in configuration file cannot be found.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_CacheStatisticsPrinterClassNotFound() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager17.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property cacheStatisticsPrinterClass in configuration file is not a type of CacheStatisticsPrinter.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_InvalidCacheStatisticsPrinterClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager18.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property cacheStatisticsPrinterClass in configuration file is an abstract class.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_AbstractCacheStatisticsPrinterClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager19.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property cacheStatisticsPrinterClass in configuration file does not have a constructor that take Properties
     * parameter.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_NoMatchingCtorCacheStatisticsPrinterClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager20.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property cacheStatisticsPrinterClass in configuration file throws exception when it is instantiated.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_ErrorCacheStatisticsPrinterClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager21.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property cacheStatisticsPrinterClass in configuration file has inaccessible constructor.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_InaccessibleCacheStatisticsPrinterClass() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager22.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property maxMemorySize in configuration file is empty.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_EmptyMaxMemorySize() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager23.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property maxMemorySize in configuration file is negative.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_IllegalMaxMemorySize() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager24.properties"));
    }

    /**
     * <p>
     * Failure test method for {@link CacheManagerImpl#CacheManagerImpl(java.util.Properties)} when the value of
     * property maxMemorySize in configuration file cannot be parsed as long.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor4_InvalidMaxMemorySize() throws Exception {
        new CacheManagerImpl(TestHelper.loadProperties(INVALID_CONFIG + "invalid_manager25.properties"));
    }
}
