
package edu.wustl.catissuecore.namegenerator;

import java.util.List;

import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;

/**
 * This class contains the default  Storage container label  implementation.
 * @author falguni_sachde
 *
 */
public class DefaultStorageContainerLabelGenerator implements LabelGenerator
{

	/**
	 * Logger Object.
	 */
	private static final transient Logger logger = Logger
			.getCommonLogger(DefaultStorageContainerLabelGenerator.class);
	/**
	 * Current label.
	 */
	protected Long currentLabel;

	/**
	 * Default Constructor.
	 */
	public DefaultStorageContainerLabelGenerator() throws ApplicationException
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
	protected void init() throws ApplicationException
	{
		currentLabel = new Long(0);
		String sql = "select max(IDENTIFIER) as MAX_NAME from CATISSUE_STORAGE_CONTAINER";
		currentLabel = AppUtility.getLastAvailableValue(sql);
	}

	/**
	 * set label.
	 * @param obj SC object
	 */
	public void setLabel(Object obj)
	{
		StorageContainer objStorageContainer = (StorageContainer) obj;
		currentLabel = currentLabel + 1;
		String containerName = "";
		String maxSiteName = objStorageContainer.getSite().getName();
		String maxTypeName = objStorageContainer.getStorageType().getName();
		if (maxSiteName.length() > 40)
		{
			maxSiteName = maxSiteName.substring(0, 39);
		}
		if (maxTypeName.length() > 40)
		{
			maxTypeName = maxTypeName.substring(0, 39);
		}
		containerName = maxSiteName + "_" + maxTypeName + "_" + String.valueOf(currentLabel);
		objStorageContainer.setName(containerName);
	}

	/**
	 * set label.
	 * @param storageContainerList SC object list
	 */
	public void setLabel(List storageContainerList)
	{
		for (int i = 0; i < storageContainerList.size(); i++)
		{
			StorageContainer objStorageContainer = (StorageContainer) storageContainerList.get(i);
			setLabel(objStorageContainer);
		}
	}

	/**
	 * Returns label for the given domain object.
	 * @param obj SC object
	 * @return sc name
	 */
	public String getLabel(Object obj)
	{
		StorageContainer objStorageContainer = (StorageContainer) obj;
		setLabel(objStorageContainer);
		return (objStorageContainer.getName());
	}
}
