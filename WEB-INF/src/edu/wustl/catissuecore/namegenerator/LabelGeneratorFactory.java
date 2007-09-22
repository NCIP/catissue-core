package edu.wustl.catissuecore.namegenerator;

import java.util.HashMap;

import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.XMLPropertyHandler;

/**
 * Factory Class to retrieve singleton instance of label generator
 * 
 * @author Falguni_Sachde
 */
public class LabelGeneratorFactory 
{
	
	/**
	 * Singleton instance of SpecimenLabelGenerator
     */
	 private static HashMap labelgeneratorMap = new HashMap() ;
	
	/**
	 * Get singleton instance of SpecimenLabelGenerator. The class name of an instance is picked up from properties file
	 * @return SpecimenLabelGenerator
	 */
	public static LabelGenerator getInstance(String generatorType) throws BizLogicException
	{
		try
		{
			
			if(labelgeneratorMap.get(generatorType) == null)
			{
				String className = XMLPropertyHandler.getValue(generatorType);
				if(className!=null)
				{
					labelgeneratorMap.put(generatorType,Class.forName(className).newInstance());
				}
				else
				{	
					return null;
				}
			}
			
			return (LabelGenerator)labelgeneratorMap.get(generatorType);
		}
		catch(IllegalAccessException e)
		{
			throw new BizLogicException("Could not create LabelGenerator instance: " +e.getMessage());			
		}
		catch(InstantiationException e)
		{
			throw new BizLogicException("Could not create LabelGenerator instance: " +e.getMessage());			
		}
		catch(ClassNotFoundException e)
		{
			throw new BizLogicException("Could not create LabelGenerator instance: " +e.getMessage());			
		}
	}
}
