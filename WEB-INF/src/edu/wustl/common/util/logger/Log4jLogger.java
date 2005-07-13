/**
 * <p>Title: AppLogger Class>
 * <p>Description:  Log4j implementation for application logger</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Kapil Kaveeshwar
 * @version 1.00
 * 
 * FIXME: Java doc. 
 * 
 */
package edu.wustl.common.util.logger;

import org.apache.log4j.PropertyConfigurator;

public class Log4jLogger implements ILogger
{
	private org.apache.log4j.Logger out = org.apache.log4j.Logger.getLogger("");
	public Log4jLogger(String propertiesFile)
	{
		PropertyConfigurator.configure(propertiesFile);
	}
	
	public void info(Object message)
	{
		out.info(message);
	}
	
	public void info(Object message, Throwable t)
	{
		out.info(message,t);
	}

	public void warn(Object message)
	{
		out.warn(message);
	}
	public void warn(Object message, Throwable t)
	{
		out.warn(message,t);
	}
	
	public void debug(Object message)
	{
		out.debug(message);
	}
	public void debug(Object message, Throwable t)
	{
		out.debug(message,t);
	}
	
	public void error(Object message)
	{
		out.error(message);
	}
	
	public void error(Object message, Throwable t)
	{
		out.error(message,t);
	}
	
	public void fatal(Object message)
	{
		out.fatal(message);
	}
	
	public void fatal(Object message, Throwable t)
	{
		out.fatal(message,t);
	}
}