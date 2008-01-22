package edu.wustl.catissuecore.namegenerator;

import java.util.List;
/**
 * This is the base interface for  Barcode generation.
 *  
 * @author Falguni_Sachde
 * 
 *
 */
public interface BarcodeGenerator
{
	/**
	 * Set Barcode for given Object
	 * @param object Object for which barcode will be generated 
	 */
	public void setBarcode(Object object);
	/**
	 * Set Barcode for given Object
	 * @param object Object for which barcode will be generated 
	 */
	public void setBarcode(List<Object> object);
	
}