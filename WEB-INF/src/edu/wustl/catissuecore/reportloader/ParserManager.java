package edu.wustl.catissuecore.reportloader;

import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.InitUtility;
import edu.wustl.common.util.logger.Logger;

/**
 * @author sandeep_ranade
 * Represents a factory pattern for parsers. It creates particular parser instance
 * depending upon the parameter.   
 */
public final class ParserManager
{

	/**
	 * ParserManager 
	 */
	private static ParserManager parserManager=null;
	
	/**
	 * Constructor
	 */
	private ParserManager()
	{
	}
		
	/**
	 * @return instance of ParserManager.
	 */
	public static synchronized ParserManager getInstance()
	{
		if(parserManager==null)
		{
			parserManager=new ParserManager();
			parserManager.initialize();
		}
		return parserManager;
	}
	
	/**
	 * initializes the parsser manager
	 */
	private void initialize()
	{
		InitUtility.initializeParticipantCache();
	}
	
	/**
	 * @param type represents the parser type.
	 * @return the parser depending upon the type specified as input.   
	 */
	public Parser getParser()
	{
		Parser parser=null;
		try 
		{
			parser = (Parser)Class.forName(CaTIESConstants.PARSER_CLASS).newInstance();
		} 
		catch (InstantiationException e) 
		{
			Logger.out.error("InstantiationException while instantiating class "+CaTIESConstants.PARSER_CLASS,e);
		} 
		catch (IllegalAccessException e) 
		{
			Logger.out.error("IllegalAccessException while instantiating class "+CaTIESConstants.PARSER_CLASS,e);
		} 
		catch (ClassNotFoundException e) 
		{
			Logger.out.error("ClassNotFoundException while instantiating class "+CaTIESConstants.PARSER_CLASS,e);
		}	
		return parser;
	}
}
