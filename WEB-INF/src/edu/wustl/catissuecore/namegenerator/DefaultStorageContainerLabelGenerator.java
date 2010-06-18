
package edu.wustl.catissuecore.namegenerator;

import java.util.Collection;
import java.util.List;

import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;

/**
 * This class contains the default  Storage container label  implementation.
 * @author falguni_sachde
 *
 */
public class DefaultStorageContainerLabelGenerator implements LabelGenerator
{

	/**
	 * Current label.
	 */
	protected Long currentLabel;

	/**
	 * Default Constructor.
	 * @throws ApplicationException Application Exception
	 */
	public DefaultStorageContainerLabelGenerator() throws ApplicationException
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
		this.currentLabel = new Long(0);
		final String sql = "select max(IDENTIFIER) as MAX_NAME from CATISSUE_STORAGE_CONTAINER";
		this.currentLabel = AppUtility.getLastAvailableValue(sql);
	}

	/**
	 * set label.
	 * @param obj SC object
	 */
	public void setLabel(Object obj)
	{
		final StorageContainer objStorageContainer = (StorageContainer) obj;
		this.currentLabel = this.currentLabel + 1;
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
		containerName = maxSiteName + "_" + maxTypeName + "_" + this.currentLabel;
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
			final StorageContainer objStorageContainer = (StorageContainer) storageContainerList
					.get(i);
			this.setLabel(objStorageContainer);
		}
	}

	/**
	 * Returns label for the given domain object.
	 * @param obj SC object
	 * @return sc name
	 */
	public String getLabel(Object obj)
	{
		final StorageContainer objStorageContainer = (StorageContainer) obj;
		this.setLabel(objStorageContainer);
		return objStorageContainer.getName();
	}

	public void setLabel(Collection<AbstractDomainObject> object) throws LabelGenException
	{
		// TODO Auto-generated method stub

	}

	public void setLabel(Object object, boolean ignoreCollectedStatus) throws LabelGenException
	{
		// TODO Auto-generated method stub
		
	}
}
