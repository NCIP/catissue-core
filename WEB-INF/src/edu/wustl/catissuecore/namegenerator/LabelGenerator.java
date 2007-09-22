package edu.wustl.catissuecore.namegenerator;

import java.util.List;

import edu.wustl.common.domain.AbstractDomainObject;

/**
 * Interface for  label generation.
 *  
 * @author Falguni_Sachde
 * 
 *
 */
public interface LabelGenerator
{
	public void setLabel(AbstractDomainObject obj);
	public void setLabel(List<AbstractDomainObject> obj);
	
}