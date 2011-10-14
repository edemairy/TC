/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.ibm.support.electronic.cache.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.ibm.support.electronic.cache.ObjectCacheConfigurationException;
import com.ibm.support.electronic.cache.PersistenceException;
import com.ibm.support.electronic.cache.TestHelper;
import com.ibm.support.electronic.cache.model.CachedObject;


/**
 * <p>
 * Unit test case of {@link CachedObjectDAOImpl}.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CachedObjectDAOImplTest {
    /**
     * <p>
     * The CachedObjectDAOImpl instance to test against.
     * </p>
     */
    private CachedObjectDAOImpl dao;

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
        return new JUnit4TestAdapter(CachedObjectDAOImplTest.class);
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
        prop = TestHelper.loadProperties(TestHelper.JDBC_PROPERTIES);
        dao = new CachedObjectDAOImpl(prop);
        TestHelper.initDB();
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
        dao = null;
        prop = null;
        TestHelper.clearDB();
    }

    /**
     * <p>
     * Accuracy test method for {@link CachedObjectDAOImpl#CachedObjectDAOImpl(java.util.Properties)} when all
     * optional properties are defined.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testCtor1() throws Exception {
        Assert.assertNotNull("Unable to instantiate CachedObjectDAOImpl", dao);
        Assert.assertEquals("Incorrect user", prop.getProperty("user"), TestHelper.getField(dao, "user"));
        Assert.assertEquals("Incorrect password", prop.getProperty("password"),
            TestHelper.getField(dao, "password"));
        Assert.assertEquals("Incorrect url", prop.getProperty("url"), TestHelper.getField(dao, "url"));
    }

    /**
     * <p>
     * Accuracy test method for {@link CachedObjectDAOImpl#CachedObjectDAOImpl(java.util.Properties)} when optional
     * properties are not defined.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testCtor2() throws Exception {
        Properties p = new Properties();
        p.setProperty("url", prop.getProperty("url"));
        dao = new CachedObjectDAOImpl(p);
        Assert.assertNotNull("Unable to instantiate CachedObjectDAOImpl", dao);
        Assert.assertNull("Incorrect user", TestHelper.getField(dao, "user"));
        Assert.assertNull("Incorrect password", TestHelper.getField(dao, "password"));
        Assert.assertEquals("Incorrect url", prop.getProperty("url"), TestHelper.getField(dao, "url"));
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#CachedObjectDAOImpl(java.util.Properties)} when prop is
     * null.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_NullProp() {
        new CachedObjectDAOImpl(null);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#CachedObjectDAOImpl(java.util.Properties)} when the value
     * of property user is empty.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_EmptyUser() {
        prop.setProperty("user", "  ");
        new CachedObjectDAOImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#CachedObjectDAOImpl(java.util.Properties)} when the value
     * of property user is not a String.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_InvalidUser() {
        prop.put("user", 1);
        new CachedObjectDAOImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#CachedObjectDAOImpl(java.util.Properties)} when the value
     * of property password is empty.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_EmptyPassword() {
        prop.setProperty("password", "  ");
        new CachedObjectDAOImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#CachedObjectDAOImpl(java.util.Properties)} when the value
     * of property password is not a String.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_InvalidPassword() {
        prop.put("password", 1);
        new CachedObjectDAOImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#CachedObjectDAOImpl(java.util.Properties)} when the
     * property url is not found.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_MissingUrl() {
        prop.remove("url");
        new CachedObjectDAOImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#CachedObjectDAOImpl(java.util.Properties)} when the value
     * of property url is empty.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_EmptyUrl() {
        prop.setProperty("url", " ");
        new CachedObjectDAOImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#CachedObjectDAOImpl(java.util.Properties)} when the value
     * of property url is not a String.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_InvalidUrl() {
        prop.put("url", 1);
        new CachedObjectDAOImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#CachedObjectDAOImpl(java.util.Properties)} when the value
     * of property driver is empty.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_EmptyDriver() {
        prop.setProperty("driver", "  ");
        new CachedObjectDAOImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#CachedObjectDAOImpl(java.util.Properties)} when the value
     * of property driver is not a String.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_InvalidDriver() {
        prop.put("driver", 1);
        new CachedObjectDAOImpl(prop);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#CachedObjectDAOImpl(java.util.Properties)} when the
     * driver class specified by the value of property driver is not found.
     * </p>
     */
    @Test(expected = ObjectCacheConfigurationException.class)
    public void testCtor_DriverNotFound() {
        prop.setProperty("driver", "com.ibm.support.electronic.cache.UnknownDriver");
        new CachedObjectDAOImpl(prop);
    }

    /**
     * <p>
     * Accuracy test method for {@link CachedObjectDAOImpl#deleteByCacheSet(java.lang.String)}.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testDeleteByCacheSet() throws Exception {
        String cacheSetName = "default";
        // delete all cached object in cache set 'default'
        dao.deleteByCacheSet(cacheSetName);

        // verify
        Connection conn = TestHelper.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT ID FROM CACHED_OBJECT WHERE CACHE_SET_NAME=?");
            pstmt.setString(1, cacheSetName);
            rs = pstmt.executeQuery();
            Assert.assertFalse("All cached objects should be deleted", rs.next());
        } finally {
            close(rs);
            close(pstmt);
            TestHelper.close(conn);
        }
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#deleteByCacheSet(java.lang.String)} when cacheSetName is
     * null.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByCacheSet_NullCacheSetName() throws Exception {
        dao.deleteByCacheSet(null);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#deleteByCacheSet(java.lang.String)} when cacheSetName is
     * empty.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByCacheSet_EmptyCacheSetName() throws Exception {
        dao.deleteByCacheSet("  ");
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#deleteByCacheSet(java.lang.String)} when fails to open
     * connection.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = PersistenceException.class)
    public void testDeleteByCacheSet_ConnectionFail() throws Exception {
        prop.setProperty("url", "url");
        dao = new CachedObjectDAOImpl(prop);
        dao.deleteByCacheSet("set");
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#deleteByCacheSet(java.lang.String)} when persistence
     * error occurs.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = PersistenceException.class)
    public void testDeleteByCacheSet_PersistenceError() throws Exception {
        PreparedStatement pstmt = Mockito.mock(PreparedStatement.class);
        Mockito.when(pstmt.executeUpdate()).thenThrow(new SQLException("For test only"));

        Connection mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(pstmt);

        Driver mockDriver = Mockito.mock(Driver.class);
        DriverManager.registerDriver(mockDriver);
        Mockito.when(mockDriver.connect(Mockito.eq("jdbc:mock"), (Properties) Mockito.any())).thenReturn(
            mockConnection);
        prop.setProperty("url", "jdbc:mock");
        dao = new CachedObjectDAOImpl(prop);
        try {
            dao.deleteByCacheSet("set");
        } finally {
            DriverManager.deregisterDriver(mockDriver);
        }
    }

    /**
     * <p>
     * Accuracy test method for {@link CachedObjectDAOImpl#get(java.lang.String, java.lang.String)} when the cached
     * object exists.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testGet1() throws Exception {
        CachedObject cachedObject = dao.get("default", "obj1");
        Assert.assertNotNull("Cached object should exist", cachedObject);
        Assert.assertEquals("Incorrect id", "obj1", cachedObject.getId());
        Assert.assertEquals("Incorrect cacheSetName", "default", cachedObject.getCacheSetName());
        Assert.assertTrue("Incorrect content", Arrays.equals("test".getBytes(), cachedObject.getContent()));
    }

    /**
     * <p>
     * Accuracy test method for {@link CachedObjectDAOImpl#get(java.lang.String, java.lang.String)} when the cached
     * object does not exist.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testGet2() throws Exception {
        CachedObject cachedObject = dao.get("custom", "obj1");
        Assert.assertNull("Cached object should not be existed", cachedObject);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#get(java.lang.String, java.lang.String)} when
     * cacheSetName is null.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGet_NullCacheSetName() throws Exception {
        dao.get(null, "obj1");
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#get(java.lang.String, java.lang.String)} when
     * cacheSetName is empty.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGet_EmptyCacheSetName() throws Exception {
        dao.get(" ", "obj1");
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#get(java.lang.String, java.lang.String)} when id is null.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGet_NullId() throws Exception {
        dao.get("default", null);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#get(java.lang.String, java.lang.String)} when id is
     * empty.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGet_EmptyId() throws Exception {
        dao.get("default", " ");
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#get(java.lang.String, java.lang.String)} when fails to
     * open connection to the database.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = PersistenceException.class)
    public void testGet_ConnectionFail() throws Exception {
        prop.setProperty("url", "url");
        dao = new CachedObjectDAOImpl(prop);
        dao.get("default", "id");
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#get(java.lang.String, java.lang.String)} when persistence
     * error occurs.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = PersistenceException.class)
    public void testGet_PersistenceError() throws Exception {
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.next()).thenThrow(new SQLException("For test only"));

        PreparedStatement pstmt = Mockito.mock(PreparedStatement.class);
        Mockito.when(pstmt.executeQuery()).thenReturn(rs);

        Connection mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(pstmt);

        Driver mockDriver = Mockito.mock(Driver.class);
        DriverManager.registerDriver(mockDriver);
        Mockito.when(mockDriver.connect(Mockito.eq("jdbc:mock"), (Properties) Mockito.any())).thenReturn(
            mockConnection);
        prop.setProperty("url", "jdbc:mock");
        dao = new CachedObjectDAOImpl(prop);
        try {
            dao.get("default", "obj1");
        } finally {
            DriverManager.deregisterDriver(mockDriver);
        }
    }

    /**
     * <p>
     * Accuracy test method for
     * {@link CachedObjectDAOImpl#save(com.ibm.support.electronic.cache.model.CachedObject)} when cached object
     * does not exist in database.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testSave1() throws Exception {
        String cacheSetName = "custom";
        String id = "obj4";
        byte[] content = "test".getBytes();

        // create object to save
        CachedObject cachedObject = new CachedObject();
        cachedObject.setCacheSetName(cacheSetName);
        cachedObject.setId(id);
        cachedObject.setContent(content);

        CachedObject persisted = dao.get(cacheSetName, id);
        Assert.assertNull("Cached object should not be existed", persisted);

        // save the object
        dao.save(cachedObject);
        persisted = dao.get(cacheSetName, id);
        Assert.assertNotNull("Cached object should exist", persisted);
        Assert.assertEquals("Incorrect id", id, persisted.getId());
        Assert.assertEquals("Incorrect cacheSetName", cacheSetName, persisted.getCacheSetName());
        Assert.assertTrue("Incorrect content", Arrays.equals(content, persisted.getContent()));
    }

    /**
     * <p>
     * Accuracy test method for
     * {@link CachedObjectDAOImpl#save(com.ibm.support.electronic.cache.model.CachedObject)} when cached object
     * already exists in database.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testSave2() throws Exception {
        Properties p = new Properties();
        p.setProperty(
            "url",
            prop.getProperty("url") + ":user=" + prop.getProperty("user") + ";password="
                + prop.getProperty("password") + ";");
        dao = new CachedObjectDAOImpl(p);
        String cacheSetName = "custom";
        String id = "obj3";
        byte[] content = "update".getBytes();
        CachedObject persisted = dao.get(cacheSetName, id);
        Assert.assertNotNull("Cached object should be existed", persisted);

        // update
        persisted.setContent(content);

        // save the object
        dao.save(persisted);
        CachedObject updated = dao.get(cacheSetName, id);
        Assert.assertNotNull("Cached object should exist", updated);
        Assert.assertEquals("Incorrect id", id, updated.getId());
        Assert.assertEquals("Incorrect cacheSetName", cacheSetName, updated.getCacheSetName());
        Assert.assertTrue("Incorrect content", Arrays.equals(content, updated.getContent()));
    }

    /**
     * <p>
     * Failure test method for
     * {@link CachedObjectDAOImpl#save(com.ibm.support.electronic.cache.model.CachedObject)} when cachedObject is
     * null.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSave_NullCachedObject() throws Exception {
        dao.save(null);
    }

    /**
     * <p>
     * Failure test method for
     * {@link CachedObjectDAOImpl#save(com.ibm.support.electronic.cache.model.CachedObject)} when fails to open
     * connection to the database.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = PersistenceException.class)
    public void testSave_ConnectionFail() throws Exception {
        prop.setProperty("url", "url");
        dao = new CachedObjectDAOImpl(prop);
        dao.save(new CachedObject());
    }

    /**
     * <p>
     * Failure test method for
     * {@link CachedObjectDAOImpl#save(com.ibm.support.electronic.cache.model.CachedObject)} when persistence error
     * occurs.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = PersistenceException.class)
    public void testSave_PersistenceError() throws Exception {
        PreparedStatement pstmt = Mockito.mock(PreparedStatement.class);
        Mockito.when(pstmt.executeUpdate()).thenThrow(new SQLException("For test only"));

        Connection mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(pstmt);

        Driver mockDriver = Mockito.mock(Driver.class);
        DriverManager.registerDriver(mockDriver);
        Mockito.when(mockDriver.connect(Mockito.eq("jdbc:mock"), (Properties) Mockito.any())).thenReturn(
            mockConnection);
        prop.setProperty("url", "jdbc:mock");
        dao = new CachedObjectDAOImpl(prop);
        try {
            dao.save(new CachedObject());
        } finally {
            DriverManager.deregisterDriver(mockDriver);
        }
    }

    /**
     * <p>
     * Accuracy test method for {@link CachedObjectDAOImpl#delete(java.lang.String, java.lang.String)}.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test
    public void testDelete() throws Exception {
        String cacheSetName = "custom";
        String id = "obj3";
        CachedObject persisted = dao.get(cacheSetName, id);
        Assert.assertNotNull("Cached object should be existed", persisted);

        // delete
        dao.delete(cacheSetName, id);
        persisted = dao.get(cacheSetName, id);
        Assert.assertNull("Cached object should be deleted", persisted);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#delete(java.lang.String, java.lang.String)} when
     * cacheSetName is null.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDelete_NullCacheSetName() throws Exception {
        dao.delete(null, "id");
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#delete(java.lang.String, java.lang.String)} when
     * cacheSetName is empty.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDelete_EmptyCacheSetName() throws Exception {
        dao.delete(" ", "id");
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#delete(java.lang.String, java.lang.String)} when id is
     * null.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDelete_NullId() throws Exception {
        dao.delete("name", null);
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#delete(java.lang.String, java.lang.String)} when id is
     * empty.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDelete_EmptyId() throws Exception {
        dao.delete("name", " ");
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#delete(java.lang.String, java.lang.String)} when fails to
     * open connection to the database.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = PersistenceException.class)
    public void testDelete_ConnectionFail() throws Exception {
        prop.setProperty("url", "url");
        dao = new CachedObjectDAOImpl(prop);
        dao.delete("name", "id");
    }

    /**
     * <p>
     * Failure test method for {@link CachedObjectDAOImpl#delete(java.lang.String, java.lang.String)} when
     * persistence error occurs.
     * </p>
     *
     * @throws Exception to junit.
     */
    @Test(expected = PersistenceException.class)
    public void testDelete_PersistenceError() throws Exception {
        PreparedStatement pstmt = Mockito.mock(PreparedStatement.class);
        Mockito.when(pstmt.executeUpdate()).thenThrow(new SQLException("For test only"));

        Connection mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(pstmt);

        Driver mockDriver = Mockito.mock(Driver.class);
        DriverManager.registerDriver(mockDriver);
        Mockito.when(mockDriver.connect(Mockito.eq("jdbc:mock"), (Properties) Mockito.any())).thenReturn(
            mockConnection);
        prop.setProperty("url", "jdbc:mock");
        dao = new CachedObjectDAOImpl(prop);
        try {
            dao.delete("name", "id");
        } finally {
            DriverManager.deregisterDriver(mockDriver);
        }
    }

    /**
     * <p>
     * Closes the prepared statement.
     * </p>
     *
     * @param pstmt the statement to close.
     */
    private static void close(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    /**
     * <p>
     * Closes the result set.
     * </p>
     *
     * @param rs the result set to close.
     */
    private static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }
}
