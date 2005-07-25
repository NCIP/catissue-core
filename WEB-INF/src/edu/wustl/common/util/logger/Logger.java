/**
 * <p>Title: AppLogger Class>
 * <p>Description:  Application Logger class</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Kapil Kaveeshwar
 * @version 1.00
 * 
 * FIXME: Java doc. 
 */
package edu.wustl.common.util.logger;

import java.util.ResourceBundle;

public abstract class Logger
{
	public static org.apache.log4j.Logger out;
	public static ResourceBundle resourcebundle;
	public static void configure(String propertiesFile)
	{
		if(out==null)
		{
//			out = org.apache.log4j.Logger.getLogger("");
//			PropertyConfigurator.configure(propertiesFile);
		    out=org.apache.log4j.Logger.getLogger("catissuecore.logger");
		}
		
	}
	
	
	private Logger()
	{
	}
	
	
}