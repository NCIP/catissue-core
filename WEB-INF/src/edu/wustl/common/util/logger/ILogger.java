/**
 * <p>Title: AppLogger Class>
 * <p>Description:  Interface to define all the basic method for any loagger</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Kapil Kaveeshwar
 * @version 1.00
 * 
 * FIXME: Java doc. 
 */
package edu.wustl.common.util.logger;

public interface ILogger
{
	public abstract void info(Object message);
	public abstract void info(Object message, Throwable t);
	
	public abstract void warn(Object message);
	public abstract void warn(Object message, Throwable t);
	
	public abstract void debug(Object message);
	public abstract void debug(Object message, Throwable t);
	
	public abstract void error(Object message);
	public abstract void error(Object message, Throwable t);
	
	public abstract void fatal(Object message);
	public abstract void fatal(Object message, Throwable t);
}