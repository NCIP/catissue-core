package edu.wustl.catissuecore.namegenerator;

import java.util.HashMap;

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
	public static LabelGenerator getInstance(String generatorType) throws NameGeneratorException
	{
		try
		{
			System.out.println("Inside LabelGenerator factory..");
			if(labelgeneratorMap.get(generatorType) == null)
			{
				System.out.println("Inside LabelGenerator factory..");
				String className = PropertyHandler.getValue(generatorType);
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
			throw new NameGeneratorException("Could not create LabelGenerator instance: " +e.getMessage());			
		}
		catch(InstantiationException e)
		{
			throw new NameGeneratorException("Could not create LabelGenerator instance: " +e.getMessage());			
		}
		catch(ClassNotFoundException e)
		{
			throw new NameGeneratorException("Could not create LabelGenerator instance: " +e.getMessage());			
		}catch(Exception ex)
		{
			throw new NameGeneratorException(ex.getMessage(),ex);
		}
	}
}
