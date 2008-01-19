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
	public void setLabel(Object obj);
	public void setLabel(List obj);
	public String getLabel(Object obj);
}