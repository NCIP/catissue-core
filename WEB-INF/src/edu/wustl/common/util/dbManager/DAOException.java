package edu.wustl.common.util.dbManager;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DAOException extends Exception
{
	private Exception wrapException; 
	
	public DAOException(String message)
	{
		this(message,null);
	}
	
	public DAOException(Exception ex)
	{
		this("",ex);
	}
	
	/**
	 * @param wrapException The wrapException to set.
	 */
	public DAOException(String message, Exception wrapException)
	{
		super(message);
		this.wrapException = wrapException;
	}
	
	/**
	 * @return Returns the wrapException.
	 */
	private Exception getWrapException()
	{
		return wrapException;
	}
	
	/**
	 * @param wrapException The wrapException to set.
	 */
	private void setWrapException(Exception wrapException)
	{
		this.wrapException = wrapException;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace()
	 */
	public void printStackTrace()
	{
		super.printStackTrace();
		if(wrapException!=null)
			wrapException.printStackTrace();
	}
	public void printStackTrace(PrintWriter thePrintWriter)
	{
		super.printStackTrace(thePrintWriter);
		if(wrapException!=null)
			wrapException.printStackTrace(thePrintWriter);
	}
	public void printStackTrace(PrintStream thePrintStream)
	{
		super.printStackTrace(thePrintStream);
		if(wrapException!=null)
			wrapException.printStackTrace(thePrintStream);
	}
}