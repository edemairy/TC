package com.omega.distributedsupport.coordinator;

/**
 * Base exception thrown by Coordinators
 * 
 * @author Ross Bayer
 *
 */
public class CoordinatorException extends Exception {

	/**
	 * For serialization. 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Initializes a new CoordinatorException
	 */
	public CoordinatorException()
	{
	}

	/**
	 * Initializes a new CoordinatorException
	 * 
	 * @param message The error message
	 */
	public CoordinatorException(String message)
	{
		super(message);
	}

	/**
	 * Initializes a new CoordinatorException
	 * 
	 * @param cause The exception that caused this exception to be created.
	 */
	public CoordinatorException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * Initializes a new CoordinatorException
	 * 
	 * @param message The error message
	 * @param cause The exception that caused this exception to be created.
	 */
	public CoordinatorException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
