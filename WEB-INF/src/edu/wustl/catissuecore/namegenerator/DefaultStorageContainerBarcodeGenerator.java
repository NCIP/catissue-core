
package edu.wustl.catissuecore.namegenerator;

import java.util.List;

import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;

/**
 * This  class which contains the default StorageContainer bar code implementation.
 * @author falguni_sachde
 *
 */
public class DefaultStorageContainerBarcodeGenerator implements BarcodeGenerator
{

	/**
	 * Logger object.
	 */
	private static final transient Logger LOGGER = Logger
			.getCommonLogger(DefaultStorageContainerBarcodeGenerator.class);
	/**
	 * Current label.
	 */
	protected Long currentBarcode;

	/**
	 * Default Constructor.
	 * @throws ApplicationException Application Exception
	 */
	public DefaultStorageContainerBarcodeGenerator() throws ApplicationException
	{
		super();
		this.init();
	}

	/**
	 * This is a init() function it is called from the
	 * default constructor of Base class.When getInstance of base class
	 * called then this init function will be called.
	 * This method will first check the Database Name and then set function name that will convert
	 * label from int to String
	 * @throws ApplicationException Application Exception
	 */
	protected void init() throws ApplicationException
	{
		this.currentBarcode = new Long(0);
		final String sql = "select max(IDENTIFIER) as MAX_NAME from CATISSUE_STORAGE_CONTAINER";
		this.currentBarcode = AppUtility.getLastAvailableValue(sql);
	}

	/**
	 * Set bar code.
	 * @param obj SC object
	 */
	public void setBarcode(Object obj)
	{
		final StorageContainer objStorageContainer = (StorageContainer) obj;
		//TODO :Write a logic to generate bar code.
		final String barcode = "";
		objStorageContainer.setBarcode(barcode);

	}

	/**
	 * Set bar code.
	 * @param storageContainerList SC objList
	 */
	public void setBarcode(List storageContainerList)
	{

		for (int i = 0; i < storageContainerList.size(); i++)
		{
			final StorageContainer objStorageContainer = (StorageContainer) storageContainerList
					.get(i);
			this.setBarcode(objStorageContainer);

		}

	}
}
