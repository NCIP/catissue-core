/**
 * <p>Title: CVSLogger Class>
 * <p>Description:  CVSLogger for Report Loader class</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Vijay Pande
 * 
 * FIXME: Java doc. 
 */
package edu.wustl.catissuecore.reportloader;

public abstract class CSVLogger
{
	public static org.apache.log4j.Logger out;
	
    /**
     * Configures the logger with the properties of the specified category name in the log4j.xml file.
     * The category should be present in the logger.properties file.
     * @param categoryName The category name. 
     */
    public static void configure(String categoryName)
	{
		if(out==null)
		{
		    out=org.apache.log4j.Logger.getLogger(categoryName);
		}
	}
    
    /**
     * Configures the logger properties from the log4j.properties file.
     * The log4j.properties file should be present in the classpath for configuring.
     */
    public static void configure()
    {
        if (out == null)
        {
            out=org.apache.log4j.Logger.getLogger("");
        }
    }
    
	private CSVLogger()
	{
	}
}