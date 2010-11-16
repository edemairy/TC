/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.idgenerator;

import java.math.BigInteger;


/**
 * Implementations of this interface encapsulate ID generation logic. Currently there is only one implementation --
 * IDGeneratorImpl -- but only this public interface is exposed to callers to allow for the possibility of different
 * implementations later. IDGeneratorFactory returns implementations of this interface.
 *
 * @author srowen, iggy36, gua
 * @version 3.0
 */
public interface IDGenerator {
    /**
     * Return the name of the ID sequence which this instance encapsulates.
     *
     * @return the name of the ID sequence which this instance encapsulates
     */
    public String getIDName();

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
    public long getNextID() throws IDGenerationException;

    /**
     * <p>
     * Returns the next ID in the ID sequence encapsulated by this instance in the form of a BigInteger, rather than a
     * long.
     * </p>
     *
     * @return next ID in the ID sequence as a BigInteger
     *
     * @throws IDsExhaustedException if all possible values in the ID sequence have been assigned and no more can be
     *         assigned
     * @throws IDGenerationException if an error occurs while generating the ID (for example, error while connecting to
     *         the database)
     * @throws NoSuchIDSequenceException if there is no appropriate sequence defined in the backing DB
     */
    public BigInteger getNextBigID() throws IDGenerationException;
}
