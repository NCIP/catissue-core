package edu.wustl.catissuecore.namegenerator;

import java.util.HashMap;

import edu.wustl.common.util.logger.Logger;
/**
 * This is the factory Class to retrieve singleton instance of BarcodeGenerator.
 * @author Falguni_Sachde
 */
public class BarcodeGeneratorFactory
{
	/**
	 * Logger Object.
	 */
	private static final Logger logger = Logger.getCommonLogger(BarcodeGeneratorFactory.class);
	/**
	 * Singleton instance of BarcodeGenerator.
     */
    private static HashMap<String,Object> generatorMap = new HashMap<String, Object>() ;

	/**
	 * Get singleton instance of BarcodeGenerator.
	 * The class name of an instance is picked up from properties file
	 * @param generatorType  Property key name for specific Object's
	 * Barcode generator class (eg.specimenBarcodeGeneratorClass)
	 * @return BarcodeGenerator BarcodeGenerator
	 * @throws NameGeneratorException  NameGeneratorException
	 */
	public static BarcodeGenerator getInstance(String generatorType) throws NameGeneratorException
	{
		try
		{
			if(generatorMap.get(generatorType) == null)
			{
				String className = PropertyHandler.getValue(generatorType);
				if(className!=null)
				{
					generatorMap.put(generatorType,Class.forName(className).newInstance());
				}
				else
				{
					return null;
				}
			}
			return (BarcodeGenerator)generatorMap.get(generatorType);
		}
		catch(IllegalAccessException e)
		{
			logger.debug(e.getMessage(), e);
			throw new NameGeneratorException
			("Could not create BarcodeGenerator instance: " +e.getMessage());
		}
		catch(InstantiationException e)
		{
			logger.debug(e.getMessage(), e);
			throw new NameGeneratorException
			("Could not create BarcodeGenerator instance: " +e.getMessage());
		}
		catch(ClassNotFoundException e)
		{
			logger.debug(e.getMessage(), e);
			throw new NameGeneratorException
			("Could not create BarcodeGenerator instance: " +e.getMessage());
		}
		catch(Exception ex)
		{
			logger.debug(ex.getMessage(), ex);
			throw new NameGeneratorException(ex.getMessage(),ex);
		}
	}
}
