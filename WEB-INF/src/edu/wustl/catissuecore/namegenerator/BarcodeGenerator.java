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
	public void setBarcode(Object obj);
	public void setBarcode(List<Object> obj);
	
}