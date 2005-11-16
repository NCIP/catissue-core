/*
 * Created on Aug 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.exception;


/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AssignDataException extends Exception
{
	private Exception wrapException;
	public AssignDataException()
	{
		
	}
	public AssignDataException(String message)
	{
		this(message,null);
	}
	
	public AssignDataException(Exception ex)
	{
		this("",ex);
	}
	/**
	 * @param wrapException The wrapException to set.
	 */
	public AssignDataException(String message, Exception wrapException)
	{
		super(message);
		this.wrapException = wrapException;
	}
	
	public static void main(String[] args) 
	{
	    String str = "%a";
	    
	    if(str.endsWith("%"))
	    {
	        
	    }
	    else
	    System.out.println("NOT WITH");
    }
}
