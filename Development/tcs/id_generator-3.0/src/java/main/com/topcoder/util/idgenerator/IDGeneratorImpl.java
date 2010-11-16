/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.idgenerator;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;

import java.math.BigInteger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * <p>
 * This class is the core of the component and actually generates the IDs. It is also used by IDGeneratorBean. This
 * class queries a database to find the current state of the sequence -- the lowest ID not yet generated, and the
 * current block size. It allocates to itself the next &quot;block&quot; of IDs and then updates the ID sequence state
 * in the database accordingly, then allocates IDs from this block until exhausted. Note that larger block sizes are
 * more efficient, as this class has to make fewer database updates. However also note that any IDs not yet assigned
 * when this instance is destroyed are simply lost, so large block sizes may also result in larger &quot;gaps&quot; in
 * the ID sequence. This class requires a javax.sql.DataSource instance to be made available under the JNDI name
 * &quot;java:comp/env/jdbc/com/topcoder/util/idgenerator/IDGeneratorDataSource&quot;; this DataSource is used to
 * access the ID table. This class is synchronized for thread-safety.
 * </p>
 *
 * <p>
 * Notes: the default connection name is 'DefaultSequence' for DBConnectionFactory component.
 * </p>
 *
 * @author srowen, iggy36, gua
 * @version 3.0
 */
public class IDGeneratorImpl implements IDGenerator {
    /**
     * <p>
     * Represents the namespace used by DBConnection component to get the Connection.
     * </p>
     */
    private static final String DBFACTORY_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /** Represents the connection name of oracle sequence. */
    private static final String CONNECTION_NAME = "DefaultSequence";

    /** The next_block_start field name of table id_sequences. */
    private static final String NEXT_BLOCK_START = "next_block_start";

    /** The block_size field name of table id_sequences. */
    private static final String BLOCK_SIZE = "block_size";

    /** The exausted field name of table id_sequences. */
    private static final String EXHAUSTED = "exhausted";

    /** The select sql sentence used for retrieving data from table. */
    private static final String SELECT_NEXT_BLOCK =
        "SELECT next_block_start, block_size, exhausted FROM id_sequences WHERE name = ? FOR UPDATE";

    /** Update the next_block_start of the table. */
    private static final String UPDATE_NEXT_BLOCK_START = "UPDATE id_sequences SET next_block_start = ? WHERE name = ?";

    /** The sql sentence to set the exausted to 1. */
    private static final String UPDATE_EXHAUSTED = "UPDATE id_sequences SET exhausted = 1 WHERE name = ?";

    /**
     * The name of the ID sequence which this instance encapsulates. This is set by the constructor and so is not given
     * an initial value in the model.
     */
    private final String idName;

    /** This is the next value that will be generated for this sequence. It is returned and updated by getNextID(). */
    private long nextID;

    /** Indicate the ids left in the current block for the getNextID method. */
    private int idsLeft = 0;

    /**
     * <p>
     * Represents DBConnectionFactory instance. this variable will be instantiate lazily in the first invoking
     * getConnection() method.
     * </p>
     */
    private DBConnectionFactory factory = null;

    /**
     * Creates a new IDGeneratorImpl for the named ID sequence.
     *
     * @param idName name of the ID sequence encapsulated by this instance.
     *
     * @throws NoSuchIDSequenceException if name is null, or no such ID  sequence is configured in the database
     * @throws IDGenerationException if an error occurs while retrieving ID  sequence configuration (for example,
     *         database errors)
     */
    public IDGeneratorImpl(String idName) throws IDGenerationException {
        if (idName == null) {
            throw new NoSuchIDSequenceException("The specified IDName is null");
        }

        this.idName = idName;

        ResultSet rs = null;
        Connection connection = null;
        PreparedStatement selectStmt = null;

        // Check if the given id generator exist on the underlying persistence
        try {
            // get statement by sql string
            connection = getConnection();
            selectStmt = connection.prepareStatement(SELECT_NEXT_BLOCK);
            selectStmt.setString(1, idName);

            rs = selectStmt.executeQuery();

            if (!rs.next()) {
                throw new NoSuchIDSequenceException(
                    "The specified IDName does not exist in the underlying persistence.");
            }
        } catch (SQLException e) {
            throw new IDGenerationException("Error occurs while accessing the underlying persistence.", e);
        } finally {
            IDGeneratorHelper.closeResultSet(rs);
            IDGeneratorHelper.closeStatement(selectStmt);
            IDGeneratorHelper.close(connection);
        }
    }

    /**
     * Get connection from DBFactory.
     *
     * @return the connection from DBFactory
     *
     * @throws IDGenerationException if an error occurs while retrieving ID sequence configuration (for example,
     *         database errors)
     */
    private Connection getConnection() throws IDGenerationException {
        Connection connection = null;

        try {
            // Instantiate the factory lazily
            if (factory == null) {
                factory = new DBConnectionFactoryImpl(DBFACTORY_NAMESPACE);
            }

            // If the given connection name does not exist, a default connection will be created
            try {
                connection = factory.createConnection(CONNECTION_NAME);
            } catch (UnknownConnectionException e) {
                connection = factory.createConnection();
            }

            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        } catch (Exception e) {
            throw new IDGenerationException("Failed to get connection from db factory.", e);
        }

        return connection;
    }

    /**
     * Return the name of the ID sequence which this instance encapsulates.
     *
     * @return the name of the ID sequence which this instance encapsulates
     */
    public String getIDName() {
        return idName;
    }

    /**
     * Returns the next ID in the ID sequence encapsulated by this instance. Internal state is updated so that this ID
     * is not returned again from this method.
     *
     * @return the next ID in the ID sequence
     *
     * @throws IDsExhaustedException if all possible values in the ID sequence have been assigned and no more can be
     *         assigned
     * @throws IDGenerationException if an error occurs while generating the ID (for example, error while connecting to
     *         the database)
     * @throws NoSuchIDSequenceException if there is no appropriate sequence defined in the backing DB
     */
    public synchronized long getNextID() throws IDGenerationException {
        if (idsLeft <= 0) {
            // if no ids left,
            // acquire a new block
            synchronized (IDGeneratorImpl.class) {
                getNextBlock();
            }
        }

        --idsLeft;

        return nextID++;
    }

    /**
     * <p>
     * Wraps the value that would be returned by getNextID() in a BigInteger instance and returns it.
     * </p>
     *
     * @return next value that would be returned by getNextID() as a BigInteger
     *
     * @throws IDsExhaustedException if all possible values in the ID sequence have been assigned and no more can be
     *         assigned
     * @throws IDGenerationException if an error occurs while generating the ID (for example, error while connecting to
     *         the database)
     * @throws NoSuchIDSequenceException if there is no appropriate sequence defined in the backing DB
     */
    public synchronized BigInteger getNextBigID() throws IDGenerationException {
        return BigInteger.valueOf(getNextID());
    }

    /**
     * Reading the database for the next new start id.
     *
     * @throws IDsExhaustedException if all possible values in the ID sequence have been assigned and no more can be
     *         assigned
     * @throws IDGenerationException if an error occurs while generating the ID (for example, error while connecting to
     *         the database)
     * @throws NoSuchIDSequenceException if there is no appropriate sequence defined in the backing DB
     */
    private synchronized void getNextBlock() throws IDGenerationException {
        // access the database
        ResultSet rs = null;
        Connection connection = getConnection();
        PreparedStatement selectStmt = null;
        PreparedStatement updateExaustedStmt = null;
        PreparedStatement updateStartStmt = null;

        try {
            selectStmt = connection.prepareStatement(SELECT_NEXT_BLOCK);
            selectStmt.setString(1, idName);
            rs = selectStmt.executeQuery();

            if (!rs.next()) {
                throw new NoSuchIDSequenceException("The specified IDName does not exist in the database.");
            }

            // if the ids are exausted yet, simply throw exception
            if (rs.getBoolean(EXHAUSTED)) {
                throw new IDsExhaustedException("The ids of specified IDName are exausted yet.");
            }

            // otherwise, read the new block and update this id
            long myNextID = rs.getLong(NEXT_BLOCK_START);
            int blockSize = rs.getInt(BLOCK_SIZE);

            // if the ids left are not sufficient to make a full block,
            // throw exception
            if ((myNextID - 1) > (Long.MAX_VALUE - blockSize)) {
                throw new IDsExhaustedException("The ids left are not sufficient to make a block.");
            }

            // From here, we need to consider the rollback problem while error occurs
            // if the ids are exausted, set the flag
            if ((myNextID - 1) >= (Long.MAX_VALUE - blockSize)) {
                updateExaustedStmt = connection.prepareStatement(UPDATE_EXHAUSTED);
                updateExaustedStmt.setString(1, idName);
                updateExaustedStmt.executeUpdate();
            }

            long myMaxBlockID = (myNextID + blockSize) - 1;

            // update the next block start
            updateStartStmt = connection.prepareStatement(UPDATE_NEXT_BLOCK_START);
            updateStartStmt.setLong(1, myMaxBlockID + 1);
            updateStartStmt.setString(2, idName);
            updateStartStmt.executeUpdate();

            // Come here, we can commit all db changed
            commit(connection);

            // it is safe to assign all the value now
            idsLeft = blockSize;
            nextID = myNextID;
        } catch (SQLException e) {
            // rollback for SQL error
            // IDGenerationException will be thrown only while try to get connection. in this case we needn't rollback
            // while thrown IDsExhaustedException, no any updating at the underlying persistence.
            // Selection operation needn't to be rollback while error occurs. So it needn't to rollback too
            rollback(connection);
            throw new IDGenerationException("Failed to get next block.", e);
        } finally {
            IDGeneratorHelper.closeResultSet(rs);
            IDGeneratorHelper.closeStatement(selectStmt);
            IDGeneratorHelper.closeStatement(updateExaustedStmt);
            IDGeneratorHelper.closeStatement(updateStartStmt);
            IDGeneratorHelper.close(connection);
        }
    }

    /**
     * <p>
     * Rollbacks the current connection.
     * </p>
     *
     * @param connection the connection to rollback
     */
    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (Exception e) {
            // Just ignore
        }
    }

    /**
     * <p>
     * Commits the given connection.
     * </p>
     *
     * @param connection the connection to commit
     */
    private void commit(Connection connection) {
        try {
            connection.commit();
        } catch (Exception e) {
            // Just ignore
        }
    }
}
