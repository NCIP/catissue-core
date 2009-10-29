
package edu.wustl.catissuecore.reportloader;

import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author sandeep_ranade
 * Represents a factory pattern for parsers. It creates particular parser instance
 * depending upon the parameter.
 */
public final class ParserManager
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(ParserManager.class);

	/**
	 * ParserManager.
	 */
	private static ParserManager parserManager = null;

	/**
	 * Constructor.
	 */
	private ParserManager()
	{
	}

	/**
	 * @return instance of ParserManager.
	 */
	public static synchronized ParserManager getInstance()
	{
		if (parserManager == null)
		{
			parserManager = new ParserManager();
		}
		return parserManager;
	}

	/**
	 * @return the parser depending upon the type specified as input.
	 */
	public Parser getParser()
	{
		Parser parser = null;
		try
		{
			parser = (Parser) Class.forName(CaTIESConstants.PARSER_CLASS).newInstance();
		}
		catch (final InstantiationException e)
		{
			ParserManager.logger.error("InstantiationException while instantiating class "
					+ CaTIESConstants.PARSER_CLASS
					+ e.getMessage(), e);
			e.printStackTrace();
		}
		catch (final IllegalAccessException e)
		{
			ParserManager.logger.error("IllegalAccessException while instantiating class "
					+ CaTIESConstants.PARSER_CLASS
					+ e.getMessage(), e);
			e.printStackTrace();
		}
		catch (final ClassNotFoundException e)
		{
			ParserManager.logger.error("ClassNotFoundException while instantiating class "
					+ CaTIESConstants.PARSER_CLASS
					+ e.getMessage(), e);
			e.printStackTrace();
		}
		return parser;
	}
}