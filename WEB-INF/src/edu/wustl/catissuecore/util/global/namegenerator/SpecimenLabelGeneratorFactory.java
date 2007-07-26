package edu.wustl.catissuecore.util.global.namegenerator;

import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.XMLPropertyHandler;

/**
 * Factory Class to retrieve singleton instance of label generator
 * 
 * @author smita_kadam
 */
public class SpecimenLabelGeneratorFactory 
{
	
	/**
	 * Singleton instance of SpecimenLabelGenerator
     */
    private static SpecimenLabelGenerator generator;
	
	/**
	 * Get singleton instance of SpecimenLabelGenerator. The class name of an instance is picked up from properties file
	 * @return SpecimenLabelGenerator
	 */
	public static SpecimenLabelGenerator getInstance() throws BizLogicException
	{
		try
		{
			if(generator == null)
			{
				String className = XMLPropertyHandler.getValue("specimenLabelGeneratorClass");
				generator = (SpecimenLabelGenerator)Class.forName(className).newInstance();
			}
			
			return generator;
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
