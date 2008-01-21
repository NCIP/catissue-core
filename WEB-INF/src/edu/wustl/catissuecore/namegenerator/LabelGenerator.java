package edu.wustl.catissuecore.namegenerator;

import java.util.List;

/**
 * Interface for  label generation.
 *  
 * @author Falguni_Sachde
 * 
 *
 */
public interface LabelGenerator
{
	
	/**
	 * @param object
	 */
	public void setLabel(Object object);
	
	/**
	 * @param object
	 */
	public void setLabel(List object);
	
	/**
	 * @param object
	 * @return label
	 */
	public String getLabel(Object object);
}