package edu.wustl.catissuecore.reportloader;

/**
 * @author vijay_pande
 * Common Logger class that is used by all the caTIES server to log CSV formated status for each report
 */
public abstract class CSVLogger
{
	/**
	 * Logger for file poller
	 */
	private static org.apache.log4j.Logger filePollerOut;
	/**
	 * Logger for queue processor
	 */
	private static org.apache.log4j.Logger queueProcessorOut;
	/**
	 * Logger for deid server
	 */
	private static org.apache.log4j.Logger deidServerOut;
	/**
	 * Logger for concept coder
	 */
	private static org.apache.log4j.Logger conceptCoderOut;
	
	/**
	 * @param categoryName categoryNmae for logger
	 */
	public static void configure(String categoryName)
	{
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_FILE_POLLER))
		{
			filePollerOut=org.apache.log4j.Logger.getLogger(categoryName);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_QUEUE_PROCESSOR))
		{
			queueProcessorOut=org.apache.log4j.Logger.getLogger(categoryName);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_DEID_SERVER))
		{
			deidServerOut=org.apache.log4j.Logger.getLogger(categoryName);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_CONCEPT_CODER))
		{
			conceptCoderOut=org.apache.log4j.Logger.getLogger(categoryName);
		}
	}
	
	/**
	 * @param categoryName categoryNmae for logger
	 * @param message message log as info
	 */
	public static void info(String categoryName, String message)
	{
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_FILE_POLLER))
		{
			filePollerOut.info(message);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_QUEUE_PROCESSOR))
		{
			queueProcessorOut.info(message);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_DEID_SERVER))
		{
			deidServerOut.info(message);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_CONCEPT_CODER))
		{
			conceptCoderOut.info(message);
		}
	}
	
	/**
	 * @param categoryName categoryNmae for logger
	 * @param message message log as debug
	 */
	public static void debug(String categoryName, String message)
	{
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_FILE_POLLER))
		{
			filePollerOut.debug(message);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_QUEUE_PROCESSOR))
		{
			queueProcessorOut.debug(message);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_DEID_SERVER))
		{
			deidServerOut.debug(message);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_CONCEPT_CODER))
		{
			conceptCoderOut.debug(message);
		}
	}
	
	/**
	 * @param categoryName categoryNmae for logger
	 * @param message message log as error
	 */
	public static void error(String categoryName, String message)
	{
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_FILE_POLLER))
		{
			filePollerOut.error(message);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_QUEUE_PROCESSOR))
		{
			queueProcessorOut.error(message);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_DEID_SERVER))
		{
			deidServerOut.error(message);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_CONCEPT_CODER))
		{
			conceptCoderOut.error(message);
		}
	}
	
	/**
	 * @param categoryName categoryNmae for logger
	 * @param message message log as error
	 * @param obj Throwable object
	 */
	public static void error(String categoryName, String message,Throwable obj)
	{
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_FILE_POLLER))
		{
			filePollerOut.error(message, obj);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_QUEUE_PROCESSOR))
		{
			queueProcessorOut.error(message, obj);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_DEID_SERVER))
		{
			deidServerOut.error(message, obj);
		}
		if(categoryName.equalsIgnoreCase(Parser.LOGGER_CONCEPT_CODER))
		{
			conceptCoderOut.error(message, obj);
		}
	}
}
