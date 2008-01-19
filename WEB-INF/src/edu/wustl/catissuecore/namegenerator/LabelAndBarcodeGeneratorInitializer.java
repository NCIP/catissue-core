package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.exception.BizLogicException;

/**
 * Class for initializing  label and barcode generator
 *  
 * @author Falguni_Sachde
 * 
 *
 */
public class LabelAndBarcodeGeneratorInitializer {
	
	
	/**
	 * 
	 *
	 */
	public static void init()	
	{
		LabelGenerator specimenGeneratorInstance;
		LabelGenerator storageContainerGeneratorInstance;
		BarcodeGenerator specimenBarcodeGeneratorInstance;
		BarcodeGenerator storageContainerBarcodeGeneratorInstance;
		try 
		{
			
			specimenGeneratorInstance = LabelGeneratorFactory.getInstance(Constants.SPECIMEN_LABEL_GENERATOR_PROPERTY_NAME);    	
			if(specimenGeneratorInstance!= null)
			{	
				Variables.isSpecimenLabelGeneratorAvl = true;
			} 	
			
				
			storageContainerGeneratorInstance =LabelGeneratorFactory.getInstance(Constants.STORAGECONTAINER_LABEL_GENERATOR_PROPERTY_NAME);    	
			if(storageContainerGeneratorInstance!= null)
			{
				Variables.isStorageContainerLabelGeneratorAvl = true;
			}
    	
			specimenBarcodeGeneratorInstance = BarcodeGeneratorFactory.getInstance(Constants.SPECIMEN_BARCODE_GENERATOR_PROPERTY_NAME);
			if(specimenBarcodeGeneratorInstance!=null)
			{
				Variables.isSpecimenBarcodeGeneratorAvl = true;
			}
    	
			storageContainerBarcodeGeneratorInstance = BarcodeGeneratorFactory.getInstance(Constants.STORAGECONTAINER_BARCODE_GENERATOR_PROPERTY_NAME);
			if(storageContainerBarcodeGeneratorInstance!=null)
			{
				Variables.isStorageContainerBarcodeGeneratorAvl = true;
			}
		
			LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory.getInstance(Constants.SPECIMEN_COLL_GROUP_LABEL_GENERATOR_PROPERTY_NAME);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	

}
