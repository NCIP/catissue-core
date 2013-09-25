/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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
