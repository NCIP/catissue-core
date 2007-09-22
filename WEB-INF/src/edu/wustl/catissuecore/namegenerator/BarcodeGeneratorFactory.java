package edu.wustl.catissuecore.namegenerator;

import java.util.HashMap;

import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.XMLPropertyHandler;

/**
 * Factory Class to retrieve singleton instance of label generator
 * 
 * @author Falguni_Sachde
 */
public class BarcodeGeneratorFactory 
{
	
	/**
	 * Singleton instance of BarcodeGenerator
     */
    private static HashMap generatorMap = new HashMap() ;
	
	/**
	 * Get singleton instance of SpecimenLabelGenerator. The class name of an instance is picked up from properties file
	 * @return SpecimenLabelGenerator
	 */
	public static BarcodeGenerator getInstance(String generatorType) throws BizLogicException
	{
		try
		{
			
			
			if(generatorMap.get(generatorType) == null)
			{
				String className = XMLPropertyHandler.getValue(generatorType);
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
			throw new BizLogicException("Could not create BarcodeGenerator instance: " +e.getMessage());			
		}
		catch(InstantiationException e)
		{
			throw new BizLogicException("Could not create BarcodeGenerator instance: " +e.getMessage());			
		}
		catch(ClassNotFoundException e)
		{
			throw new BizLogicException("Could not create BarcodeGenerator instance: " +e.getMessage());			
		}
	}
}
