/**
 * <p>Title: ForwardToFactory Class>
 * <p>Description:	ForwardToFactory is a factory that returns instance of ForwardToProcessor</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on May 08, 2006
 */

package edu.wustl.catissuecore.util;

/**
 * ForwardToFactory is a factory that returns instance of ForwardToProcessor
 * @author Krunal Thakkar
 */
public final class ForwardToFactory 
{
	/*
	 * create singleton object
	 */
	private static ForwardToFactory  fwdToFactory= new ForwardToFactory();
	/*
	 * Private constructor
	 */
	private ForwardToFactory ()
	{
		
	}
	/*
	 * return single object
	 */
	public static ForwardToFactory getInstance()
	{
		return fwdToFactory;
	}
	
	public static ForwardToProcessor getForwardToProcessor()
    {
        return new ForwardToProcessor();
    }
    public static ForwardToPrintProcessor getForwardToPrintProcessor()
    {
        return new ForwardToPrintProcessor();
    }
}
