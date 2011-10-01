/**
 *
 */

package edu.wustl.catissuecore.util.metadata;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

/**
 * @author Gaurav_mehta
 * This class is an event handler of Jaxb. When parsing a given XML file, if there is any exception
 * then this class is called and its overridden method handleEvent is called.
 * This method can be used to specify some specific messages as done below.
 */
public class XMLEventHandler implements ValidationEventHandler
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(XMLEventHandler.class);

	/**
	 * Handle event. This method is called in case there is an parsing exception.
	 * @param validationEvent the validation event.
	 * @return true, if handle event.
	 */
	public boolean handleEvent(ValidationEvent validationEvent)
	{
		if (validationEvent.getSeverity() == ValidationEvent.FATAL_ERROR
				|| validationEvent.getSeverity() == ValidationEvent.ERROR
				|| validationEvent.getSeverity() == ValidationEvent.WARNING)
		{
			ValidationEventLocator locator = validationEvent.getLocator();

			LOGGER.error(validationEvent.getMessage());
			LOGGER.error("At line number " + locator.getLineNumber() + " column number "
					+ locator.getColumnNumber());
			LOGGER.error(ApplicationProperties.getValue("pv.parse.exception"));
		}
		return false;
	}
}
