package com.ibm.support.electronic.cache.dao;

import com.ibm.support.electronic.cache.Helper;
import com.ibm.support.electronic.cache.ObjectCacheConfigurationException;
import com.ibm.support.electronic.cache.PersistenceException;
import com.ibm.support.electronic.cache.model.*;
import java.io.IOException;
import java.util.*;
import java.lang.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.BASE64Decoder;

/**
 * This class is a JDBC implementation of CachedObjectDAO. It simply executes proper SQL statements to perform the operation. Auto-commit mode is turned off.
 * 
 * Thread Safety:
 * This class is thread-safe because it's immutable assuming the passed in parameters are used thread-safely by the caller.
 * 
 * Changed in version1.1:
 * expiration timestamp of CachedObject is retrieved and saved respectively in get() and save() method implementation.
 */
public class CachedObjectDAOImpl {

    /**
     * <p>
     * The SQL query to retrieve a cached object by cache set name and id.
     * </p>
     */
    private static final String GET_QUERY = "SELECT T.CONTENT FROM ALERT.CACHED_OBJECT T WHERE T.ID=? AND T.CACHE_SET_NAME=?";
    /**
     * <p>
     * The SQL query to delete all cached objects by cache set name.
     * </p>
     */
    private static final String DELETE_BY_CACHE_SET_QUERY = "DELETE FROM ALERT.CACHED_OBJECT WHERE CACHE_SET_NAME=?";
    /**
     * The username used for connecting to the database. 
     * LegalValue:
     * It cannot be null or empty.
     * Initialization and Mutability:
     * It's initialized within constructor, won't change afterwards. 
     * Usage:
     * It is used in CachedObjectDAOImpl() (for initialization), getConnection().
     */
    private final String user;
    /**
     * The password used for connecting to the database. 
     * LegalValue:
     * It cannot be null or empty.
     * Initialization and Mutability:
     * It's initialized within constructor, won't change afterwards. 
     * Usage:
     * It is used in CachedObjectDAOImpl() (for initialization), getConnection().
     */
    private final String password;
    /**
     * The connection string used when establishing a database connection. 
     * LegalValue:
     * It cannot be null or empty.
     * Initialization and Mutability:
     * It's initialized within constructor, won't change afterwards. 
     * Usage:
     * It is used in CachedObjectDAOImpl() (for initialization), getConnection().
     */
    private final String url;

    /**
     * Create an instance of the class
     * 
     * 
     * Parameters:
     * prop - configuration parameters
     * 
     * Exception:
     * ObjectCacheConfigurationException if any error occurs
     * IllegalArgumentException if
     *  prop is null;
     * 
     * Implementation Notes:
     *   user,password,url,driver = extract the namesake properties from prop(if null or empty, use default value in CS3.1)
     *   Load the database driver:
     *   Class.forName(driver);
     * @param prop 
     */
    public CachedObjectDAOImpl(Properties prop) {
        Helper.checkNull(prop, "prop should not be null");
        user = Helper.getProperty(prop, "user", false);
        String pw = Helper.getProperty(prop, "password", false);
        //  decode password
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] outputBytes = null;
        try {
            outputBytes = decoder.decodeBuffer(pw.trim());
        } catch (IOException e) {
        }
        password = new String(outputBytes);
        // end decode password
        url = Helper.getProperty(prop, "url", true);
        String driverClassName = Helper.getProperty(prop, "driver",
                "com.ibm.db2.jcc.DB2Driver");
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new ObjectCacheConfigurationException(Helper.concat(
                    "The driver class name '", driverClassName,
                    "' cannot be found"), e);
        }
    }

    /**
     * Delete all cached objects of a given cache set
     * 
     * 
     * Parameters:
     * cacheSetName - the name of the cache set to delete. 
     * 
     * Return:
     * None
     * 
     * Exception:
     * PersistenceException if any error occurs
     * IllegalArgumentException if
     *  cacheSetName doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     * 
     * Implementation Notes:
     * 1 Get a Connection object:
     *   Connection connection = getConnection();
     * 2 This sql is used to delete all rows that match a cache set name:
     *   String sql = "DELETE FROM CACHED_OBJECT WHERE CACHE_SET_NAME=?";
     * 3 Create a prepared statement:
     *   PreparedStatement statement = connection.prepareStatement(sql);
     * 4 Set the parameter value for cacheSetName:
     *   statement.setString(1, cacheSetName);
     * 5 Execute the statement:
     *   statement.executeUpdate();
     * 6 Close statement:
     *   statement.close();
     * 7 Commit transaction:
     *   connection.commit();
     * 8 Close the connection:
     *   connection.close();
     * @param cacheSetName 
     */
    public void deleteByCacheSet(String cacheSetName) throws PersistenceException {
        try {
            Helper.checkNullOrEmpty(cacheSetName,
                    "cacheSetName should not be null or empty");
            Connection connection = getConnection();
            String sql = "DELETE FROM CACHED_OBJECT WHERE CACHE_SET_NAME=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, cacheSetName);
            statement.close();
            connection.commit();
            connection.close();
        } catch (SQLException ex) {
            throw new PersistenceException("Exception in CachedObjectDAOImpl#deleteByCacheSet", ex);
        }
    }

    /**
     * Changed in version 1.1:
     * the expiration timestamp should also be retrieved from persistence layer. Implementation should be modified as below:
     * 
     * Step1: except the data retrieved, addtional EXPIRE_TS data from table should be selected.
     * Step7: Add additional     
     * 7.5 Get the value of EXPIRE_TS and set to cachedObject:
     *         cachedObject.setExpirationTimestamp(rs.getTimestamp("EXPIRE_TS"));
     * ------------------------------------------------------------------------------------------------------------------
     * Get a CachedObject by cache set name and ID
     * 
     * 
     * Parameters:
     * cacheSetName - the cache set name of the object. 
     * id - the id of the object. 
     * 
     * Return:
     * the CachedObject matching cache set name and id
     * Exception:
     * PersistenceException if any error occurs
     * IllegalArgumentException if
     *  cacheSetName doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     *  id doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     * 
     * Implementation Notes:
     * 1 This sql is used to retrieve the CachedObject instance:
     *   String sql = "SELECT T.ID,T.CACHE_SET_NAME,T.CONTENT FROM CACHED_OBJECT T WHERE T.ID=? AND T.CACHE_SET_NAME=?";
     * 2 Get a Connection object:
     *   Connection connection = getConnection();
     * 3 Create a prepared statement:
     *   PreparedStatement statement = connection.prepareStatement(sql);
     * 4 This block set the SQL parameters
     *         statement.setString(1, id);
     *         statement.setString(2, cacheSetName);
     * 5 Execute the query:
     *   ResultSet rs = statement.executeQuery();
     * 6 Init this to null so that if no CachedObject instance is retrieved from database, null is returned:
     *   CachedObject cachedObject = null;
     * 7 If rs.next() then
     *     7.1 Create a new CachedObject:
     *         cachedObject = new CachedObject();
     *     7.2 Get the value of ID and set to cachedObject:
     *         cachedObject.setId(rs.getString("ID"));
     *     7.3 Get the value of CACHE_SET_NAME and set to cachedObject:
     *         cachedObject.setCacheSetName(rs.getString("CACHE_SET_NAME"));
     *     7.4 Get the value of CONTENT and set to cachedObject:
     *         cachedObject.setContent(rs.getBytes("CONTENT"));
     * 
     * 8 Close statement:
     *   statement.close();
     * 9 Close the ResultSet:
     *   rs.close();
     * 10 Commit transaction:
     *    connection.commit();
     * 11 Close the connection:
     *    connection.close();
     * 12 return cachedObject;
     * @param id 
     * @param cacheSetName 
     * @return 
     */
    public CachedObject get(String cacheSetName, String id) throws PersistenceException {
        try {
            String sql = "SELECT T.ID,T.CACHE_SET_NAME,T.CONTENT FROM CACHED_OBJECT T WHERE T.ID=? AND T.CACHE_SET_NAME=?";
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            statement.setString(2, cacheSetName);
            ResultSet rs = statement.executeQuery();
            CachedObject cachedObject = null;
            if (rs.next()) {
                cachedObject = new CachedObject();
                cachedObject.setId(rs.getString("ID"));
                cachedObject.setCacheSetName(rs.getString("CACHE_SET_NAME"));
                cachedObject.setContent(rs.getBytes("CONTENT"));
            }

            statement.close();

            rs.close();

            connection.commit();

            connection.close();
            return cachedObject;
        } catch (SQLException ex) {
            throw new PersistenceException("Exception in CachedObjectDAOImpl#get(String, String)", ex);
        }
    }

    /**
     * Changed in version 1.1:
     * the expiration timestamp should also be saved to persistence layer. Implementation should be modified as below:
     * 
     * Step2: except the data saved, addtional EXPIRE_TS data from table should also be saved.
     * String updateSql = "UPDATE CACHED_OBJECT SET CONTENT=? AND EXPIRE_TS=? WHERE ID=? AND CACHE_SET_NAME=?";
     * 
     * After step4, insert: statement.setTimestamp(2, cachedObject.getExpirationTimestamp()); should be inserted.
     * Step5&6 will be changed to:
     * 5 Set the parameter value for id:
     *   statement.setString(3, cachedObject.getId());
     * 6 Set the parameter value for cacheSetName:
     *   statement.setString(4, cachedObject.getCacheSetName());
     * 
     * Step9.1 should be modified to
     * String insertSql = "INSERT INTO CACHED_OBJECT (ID,CACHE_SET_NAME,CONTENT,EXPIRE_TS) values(?,?,?,?)";
     * before step9.6,statement.setTimestamp(4, cachedObject.getExpirationTimestamp()); should be inserted.
     * ------------------------------------------------------------------------------------------------------------------
     * 
     * Insert or update a CachedObject to database
     * 
     * 
     * Parameters:
     * cachedObject - the cached object to save
     * 
     * Return:
     * None
     * 
     * Exception:
     * PersistenceException if any error occurs
     * IllegalArgumentException if
     *  cachedObject is null;
     * 
     * Implementation Notes:
     * 1 Get a Connection object:
     *   Connection connection = getConnection();
     * 2 This sql is used to try to update a CachedObject instance:
     *   String updateSql = "UPDATE CACHED_OBJECT SET CONTENT=? WHERE ID=? AND CACHE_SET_NAME=?";
     * 3 Create a prepared statement:
     *   PreparedStatement statement = connection.prepareStatement(updateSql);
     * 4 Set the parameter value for content:
     *   statement.setBytes(1, cachedObject.getContent());
     * 5 Set the parameter value for id:
     *   statement.setString(2, cachedObject.getId());
     * 6 Set the parameter value for cacheSetName:
     *   statement.setString(3, cachedObject.getCacheSetName());
     * 7 Execute the statement:
     *   int count = statement.executeUpdate();
     * 8 Close statement:
     *   statement.close();
     * 9 If count == 0 then
     *     9.1 This sql is used to insert a CachedObject instance:
     *         String insertSql = "INSERT INTO CACHED_OBJECT (ID,CACHE_SET_NAME,CONTENT) values(?,?,?)";
     *     9.2 Create a prepared statement:
     *         statement = connection.prepareStatement(insertSql);
     *     9.3 Set the parameter value for id:
     *         statement.setString(1, cachedObject.getId());
     *     9.4 Set the parameter value for cacheSetName:
     *         statement.setString(2, cachedObject.getCacheSetName());
     *     9.5 Set the parameter value for content:
     *         statement.setBytes(3, cachedObject.getContent());
     *     9.6 Execute the statement:
     *         statement.executeUpdate();
     *     9.7 Close statement:
     *         statement.close();
     * 
     * 10 Commit transaction:
     *    connection.commit();
     * 11 Close the connection:
     *    connection.close();
     * @param cachedObject 
     */
    public void save(CachedObject cachedObject) throws PersistenceException {
        try {
            Connection connection = getConnection();
            String updateSql = "UPDATE CACHED_OBJECT SET CONTENT=? WHERE ID=? AND CACHE_SET_NAME=?";
            PreparedStatement statement = connection.prepareStatement(updateSql);
            statement.setBytes(1, cachedObject.getContent());
            statement.setString(2, cachedObject.getId());
            statement.setString(3, cachedObject.getCacheSetName());
            int count = statement.executeUpdate();
            statement.close();
            if (count == 0) {
                String insertSql = "INSERT INTO CACHED_OBJECT (ID,CACHE_SET_NAME,CONTENT) values(?,?,?)";
                statement = connection.prepareStatement(insertSql);
                statement.setString(1, cachedObject.getId());
                statement.setString(2, cachedObject.getCacheSetName());
                statement.setBytes(3, cachedObject.getContent());
                statement.executeUpdate();

                statement.close();
            }
            connection.commit();

            connection.close();
        } catch (SQLException ex) {
            throw new PersistenceException("Exception in CachedObjectDAOImpl#save(CachedObject, CachedObject)");
        }
    }

    /**
     * Delete a particular cached object by cache set name and id
     * 
     * 
     * Parameters:
     * cacheSetName - the cache set name of the object to delete. 
     * id - the id of the object to delete. 
     * 
     * Return:
     * None
     * 
     * Exception:
     * PersistenceException if any error occurs
     * IllegalArgumentException if
     *  cacheSetName doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     *  id doesn't conform to the following legal value specification:
     *        It cannot be null or empty.
     * 
     * Implementation Notes:
     * 1 Get a Connection object:
     *   Connection connection = getConnection();
     * 2 This sql is used to delete a CachedObject instance:
     *   String sql = "DELETE FROM CACHED_OBJECT WHERE ID=? AND CACHE_SET_NAME=?";
     * 3 Create a prepared statement:
     *   PreparedStatement statement = connection.prepareStatement(sql);
     * 4 Set the parameter value for id:
     *   statement.setString(1, id);
     * 5 Set the parameter value for cacheSetName:
     *   statement.setString(2, cacheSetName);
     * 6 Execute the statement:
     *   statement.executeUpdate();
     * 7 Close statement:
     *   statement.close();
     * 8 Commit transaction:
     *   connection.commit();
     * 9 Close the connection:
     *   connection.close();
     * @param id 
     * @param cacheSetName 
     */
    public void delete(String cacheSetName, String id) throws PersistenceException {
        try {
            Connection connection = getConnection();
            String sql = "DELETE FROM CACHED_OBJECT WHERE ID=? AND CACHE_SET_NAME=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            statement.setString(2, cacheSetName);
            statement.executeUpdate();
            statement.close();
            connection.commit();
            connection.close();
        } catch (SQLException ex) {
            throw new PersistenceException("Exception in CachedObjectDAOImpl#delete(String, String)");
        }
    }

    /**
     * Get a Connection object
     * 
     * 
     * Parameters:
     * None
     * 
     * Return:
     * the Connection object
     * Exception:
     * PersistenceException if any error occurs during getting the Connection object
     * 
     * Implementation Notes:
     * 1 If user.trim().length() > 0 then
     *         Get the connection with authentication:
     *         connection = DriverManager.getConnection(url, user, password);
     * 2 Else
     *         Get the connection without authentication:
     *         connection = DriverManager.getConnection(url);
     * 3 Set auto-commit to false:
     *   connection.setAutoCommit(false);
     * 4 return connection;
     * @return 
     */
    protected final Connection getConnection() throws PersistenceException {
        Connection connection;
        try {
        if (user.trim().length() > 0) {
            connection = DriverManager.getConnection(url, user, password);
        } else {
            connection = DriverManager.getConnection(url);
        }
        connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new PersistenceException("CachedObjectDAOImpl#getConnection", e);
        }
        return connection;
    }
}
