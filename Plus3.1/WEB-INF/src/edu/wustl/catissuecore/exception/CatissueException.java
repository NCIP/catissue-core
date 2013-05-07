/**
 *
 */

package edu.wustl.catissuecore.exception;

import java.io.Serializable;

/**
 * @author mandar_deshmukh
 *
 */
public class CatissueException extends Exception implements Serializable
{

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * default constructor.
	 */
	public CatissueException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message : message
	 */
	public CatissueException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message : message
	 * @param cause : cause
	 */
	public CatissueException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause : cause
	 */
	public CatissueException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
