
package edu.wustl.catissuecore.printserviceclient;

import java.util.HashMap;

import edu.wustl.common.util.logger.Logger;

/**
 * This is the factory class to retrieve singleton instance of LabelPrinter class.
 * @author falguni_sachde
 *
 */
public final class LabelPrinterFactory
{

	/**
	 * Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(LabelPrinterFactory.class);

	/**
	 * private constructor.
	 */
	private LabelPrinterFactory()
	{

	}

	/**
	 * Map of class.
	 */
	private static HashMap printClassMap = new HashMap();

	/**
	 * @param objectType  Property key name for specific Object's  Label Printer class
	 * @return LabelPrinter LabelPrinter class object.
	 * @throws Exception Exception.
	 */
	public static LabelPrinter getInstance(final String objectType) throws Exception
	{
		try
		{
			final String className = PropertyHandler.getValue(objectType);
			if (className != null)
			{
				printClassMap.put(objectType, Class.forName(className).newInstance());
			}
			else
			{
				return null;
			}
			return (LabelPrinter) printClassMap.get(objectType);

		}
		catch (final Exception e)
		{
			LabelPrinterFactory.logger.error(e.getMessage(), e);
			e.printStackTrace() ;
			throw e;

		}
	}
}