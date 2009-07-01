
package edu.wustl.catissuecore.namegenerator;

import java.util.List;

import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.util.logger.Logger;

/**
 * This  class which contains the default StorageContainer barcode implementation.
 * @author falguni_sachde
 *
 */
public class DefaultStorageContainerBarcodeGenerator implements BarcodeGenerator
{

	/**
	 * Logger object 
	 */
	private static final transient Logger logger = Logger.getCommonLogger(DefaultStorageContainerBarcodeGenerator.class);
	/**
	 * Current label.
	 */
	protected Long currentBarcode;
	
	/**
	 * Default Constructor.
	 */
	public DefaultStorageContainerBarcodeGenerator()
	{
		super();
		init();
	}

	/**
	 * This is a init() function it is called from the
	 * default constructor of Base class.When getInstance of base class
	 * called then this init function will be called.
	 * This method will first check the Datatbase Name and then set function name that will convert
	 * lable from int to String
	 */
	protected void init()
	{
		currentBarcode = new Long(0);
		String sql = "select max(IDENTIFIER) as MAX_NAME from CATISSUE_STORAGE_CONTAINER";
		currentBarcode=AppUtility.getLastAvailableValue(sql);
	}

	/**
	 * Set barcode.
	 * @param obj SC obj
	 */
	public void setBarcode(Object obj)
	{
		StorageContainer objStorageContainer = (StorageContainer) obj;
		//TODO :Write a logic to generate barcode.
		String barcode = "";
		objStorageContainer.setBarcode(barcode);

	}

	/**
	 * Set barcode.
	 * @param storageContainerList SC objList
	 */
	public void setBarcode(List storageContainerList)
	{

		for (int i = 0; i < storageContainerList.size(); i++)
		{
			StorageContainer objStorageContainer = (StorageContainer) storageContainerList.get(i);
			setBarcode(objStorageContainer);

		}

	}
}
