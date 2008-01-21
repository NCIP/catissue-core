package edu.wustl.catissuecore.namegenerator;

import java.util.List;
/**
 * Interface for  Barcode generation.
 *  
 * @author Falguni_Sachde
 * 
 *
 */
public interface BarcodeGenerator
{
	/**
	 * @param object
	 */
	public void setBarcode(Object object);
	/**
	 * @param object
	 */
	public void setBarcode(List<Object> object);
	
}