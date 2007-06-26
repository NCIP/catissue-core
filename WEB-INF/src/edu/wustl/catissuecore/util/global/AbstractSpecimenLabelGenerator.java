package edu.wustl.catissuecore.util.global;

import edu.wustl.common.util.global.ApplicationProperties;

/**
 * This is a abstract class for Specimen label generation.
 * During JBOSS Startup CatissueCoreServeletContextlistener class get instance of AbstractSpecimenGenerator
 * which inturn retrive currentlable from database and set value of current lable in a variable. 
 * @author virender_mehta
 *
 */
public abstract class AbstractSpecimenLabelGenerator
{
	/**
	 * This variable will store the unique value of specimen lable.
	 */
	protected static Long currentLable;
	/**
	 * Singleton instace
	 */
	protected static AbstractSpecimenLabelGenerator specimenGeneratorInstance = null;
	
	protected AbstractSpecimenLabelGenerator()
	{
		init();
	}
	
	protected abstract void init();
	
	/**
	 * This method will return the instance of a class depending on the class name.
	 * @return specimenGeneratorInstance :Return instance according to class name
	 * @throws Exception
	 */
	public static final AbstractSpecimenLabelGenerator getSpecimenLabelGeneratorInstance() throws Exception
	{
		if(specimenGeneratorInstance == null)
		{
			String className = ApplicationProperties.getValue("app.specimenLabelGeneratorClass");
			specimenGeneratorInstance = (AbstractSpecimenLabelGenerator)Class.forName(className).newInstance();
		}
		
		return specimenGeneratorInstance;
	}
	/**
	 * This abstract method will increment current Specimen Label. 
	 * @param  obj
	 * @return Specimen label
	 */
	public abstract String getNextAvailableSpecimenlabel(Object obj);
	
	/**
	 * This abstract method will increment current Derive Specimen Label. 
	 * @param  obj
	 * @return Derive Specimen label
	 */
	public abstract String getNextAvailableDeriveSpecimenlabel(Object obj);
	
	/**
	 * This abstract method will increment current Aliquot Specimen Label. 
	 * @param  obj
	 * @return Aliquot Specimen label
	 */
	public abstract String getNextAvailableAliquotSpecimenlabel(Object specimenId);
}
