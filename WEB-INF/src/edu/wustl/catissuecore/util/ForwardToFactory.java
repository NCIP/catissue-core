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

import edu.wustl.common.factory.IForwordToFactory;

/**
 * ForwardToFactory is a factory that returns instance of ForwardToProcessor
 * @author Krunal Thakkar
 */
public final class ForwardToFactory implements IForwordToFactory
{
	/*
	 * create singleton object
	 */
	private static ForwardToFactory  fwdToFactory= new ForwardToFactory();
	/*
	 * Private constructor
	 */
	public ForwardToFactory ()
	{
		
	}
	/*
	 * return single object
	 */
	public static ForwardToFactory getInstance()
	{
		return fwdToFactory;
	}
	
	public ForwardToProcessor getForwardToProcessor()
    {
        return new ForwardToProcessor();
    }
    public ForwardToPrintProcessor getForwardToPrintProcessor()
    {
        return new ForwardToPrintProcessor();
    }
}
