package edu.wustl.catissuecore.namegenerator;

import java.util.HashMap;
/**
 * Factory Class to retrieve singleton instance of BarcodeGenerator 
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
	 * Get singleton instance of BarcodeGenerator. The class name of an instance is picked up from properties file
	 * @param generatorType 
	 * @return BarcodeGenerator
	 * @throws NameGeneratorException 
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
			throw new NameGeneratorException
			("Could not create BarcodeGenerator instance: " +e.getMessage());			
		}
		catch(InstantiationException e)
		{
			throw new NameGeneratorException("Could not create BarcodeGenerator instance: " +e.getMessage());			
		}
		catch(ClassNotFoundException e)
		{
			throw new NameGeneratorException("Could not create BarcodeGenerator instance: " +e.getMessage());			
		}catch(Exception ex){
			throw new NameGeneratorException(ex.getMessage(),ex);
		}
	}
}
