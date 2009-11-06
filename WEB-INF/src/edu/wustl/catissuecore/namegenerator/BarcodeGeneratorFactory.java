
package edu.wustl.catissuecore.namegenerator;

import java.util.HashMap;

import edu.wustl.common.util.logger.Logger;

/**
 * This is the factory Class to retrieve singleton instance of BarcodeGenerator.
 * @author Falguni_Sachde
 */
public final class BarcodeGeneratorFactory
{

	/**
	 * Logger Object.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(BarcodeGeneratorFactory.class);

	/**
	 * Private constructor.
	 */
	private BarcodeGeneratorFactory()
	{

	}

	/**
	 * Singleton instance of BarcodeGenerator.
	 */
	private static HashMap<String, Object> generatorMap = new HashMap<String, Object>();

	/**
	 * Get singleton instance of BarcodeGenerator.
	 * The class name of an instance is picked up from properties file
	 * @param generatorType  Property key name for specific Object's
	 * Bar code generator class (example.specimenBarcodeGeneratorClass)
	 * @return BarcodeGenerator BarcodeGenerator
	 * @throws NameGeneratorException  NameGeneratorException
	 */
	public static BarcodeGenerator getInstance(String generatorType) throws NameGeneratorException
	{
		try
		{
			if (generatorMap.get(generatorType) == null)
			{
				final String className = PropertyHandler.getValue(generatorType);
				if (className != null && !"".equals(className))
				{
					generatorMap.put(generatorType, Class.forName(className).newInstance());
				}
				else
				{
					return null;
				}
			}
			return (BarcodeGenerator) generatorMap.get(generatorType);
		}
		catch (final IllegalAccessException e)
		{
			BarcodeGeneratorFactory.LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			throw new NameGeneratorException("Could not create BarcodeGenerator instance: "
					+ e.getMessage());
		}
		catch (final InstantiationException e)
		{
			BarcodeGeneratorFactory.LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			throw new NameGeneratorException("Could not create BarcodeGenerator instance: "
					+ e.getMessage());
		}
		catch (final ClassNotFoundException e)
		{
			BarcodeGeneratorFactory.LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			throw new NameGeneratorException("Could not create BarcodeGenerator instance: "
					+ e.getMessage());
		}
		catch (final Exception ex)
		{
			BarcodeGeneratorFactory.LOGGER.error(ex.getMessage(), ex);
			ex.printStackTrace();
			throw new NameGeneratorException(ex.getMessage(), ex);
		}
	}
}
