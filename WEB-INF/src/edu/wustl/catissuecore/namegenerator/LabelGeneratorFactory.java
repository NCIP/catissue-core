
package edu.wustl.catissuecore.namegenerator;

import java.util.HashMap;

import edu.wustl.common.util.logger.Logger;

/**
 * This is the factory Class to retrieve singleton instance of LabelGenerator.
 * @author Falguni_Sachde
 */
public class LabelGeneratorFactory
{

	private static Logger logger = Logger.getCommonLogger(LabelGeneratorFactory.class);
	/**
	 * Singleton instance of SpecimenLabelGenerator.
	 */
	private static HashMap<String,Object> labelgeneratorMap = new HashMap<String, Object>();

	/**
	 * Get singleton instance of SpecimenLabelGenerator.
	 * The class name of an instance is picked up from properties file
	 * @param generatorType Property key name for specific Object's
	 * Label generator class (eg.specimenLabelGeneratorClass)
	 * @return LabelGenerator
	 * @throws NameGeneratorException NameGeneratorException
	 */
	public static LabelGenerator getInstance(String generatorType) throws NameGeneratorException
	{
		try
		{

			if (labelgeneratorMap.get(generatorType) == null)
			{

				String className = PropertyHandler.getValue(generatorType);
				if (className != null)
				{
					labelgeneratorMap.put
					(generatorType, Class.forName(className).newInstance());
				}
				else
				{
					return null;
				}
			}

			return (LabelGenerator) labelgeneratorMap.get(generatorType);
		}
		catch (IllegalAccessException e)
		{
			logger.debug(e.getMessage(), e);
			throw new NameGeneratorException
			("Could not create LabelGenerator instance: "+ e.getMessage());
		}
		catch (InstantiationException e)
		{
			logger.debug(e.getMessage(), e);
			throw new NameGeneratorException
			("Could not create LabelGenerator instance: "+ e.getMessage());
		}
		catch (ClassNotFoundException e)
		{
			logger.debug(e.getMessage(), e);
			throw new NameGeneratorException
			("Could not create LabelGenerator instance: "+ e.getMessage());
		}
		catch (Exception ex)
		{
			logger.debug(ex.getMessage(), ex);
			throw new NameGeneratorException(ex.getMessage(), ex);
		}
	}
}
