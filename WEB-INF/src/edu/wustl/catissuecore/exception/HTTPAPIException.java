/**
 * <p>Title: HTTPAPIException Class>
 * <p>Description:	This is the wrapper class over exceptions occured while
 * accessing the system through HTTP APIs.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Dec 16, 2005
 */

package edu.wustl.catissuecore.exception;

/**
 * This is the wrapper class over exceptions occured while accessing the system through HTTP APIs.
 * @author aniruddha_phadnis
 */
public class HTTPAPIException extends Exception
{
	/**
	 * httpApiException.
	 */
	private Exception httpApiException;
	/**
	 * default constructor.
	 */
	public HTTPAPIException()
	{
	}
	/**
	 * @param message : message
	 */
	public HTTPAPIException(String message)
	{
		this(message, null);
	}
	/**
	 * @param ex : ex
	 */
	public HTTPAPIException(Exception ex)
	{
		this("", ex);
	}
	/**
	 * @param message : message
	 * @param ex : ex
	 */
	public HTTPAPIException(String message, Exception ex)
	{
		super(message);
		this.httpApiException = ex;
	}
}