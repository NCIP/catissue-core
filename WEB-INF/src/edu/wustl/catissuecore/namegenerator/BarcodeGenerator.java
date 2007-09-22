package edu.wustl.catissuecore.namegenerator;

import java.util.List;

import edu.wustl.common.domain.AbstractDomainObject;

/**
 * Interface for  Barcode generation.
 *  
 * @author Falguni_Sachde
 * 
 *
 */
public interface BarcodeGenerator
{
	public void setBarcode(AbstractDomainObject obj);
	public void setBarcode(List<AbstractDomainObject> obj);
	
}