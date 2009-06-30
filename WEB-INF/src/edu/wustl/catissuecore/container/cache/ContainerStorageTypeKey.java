
package edu.wustl.catissuecore.container.cache;

public class ContainerStorageTypeKey implements IContainerCacheKey
{

	private static final long serialVersionUID = 1L;

	private Long storageTypeID;
	private static StorageTypeMap storageTypeMap = (StorageTypeMap) StorageTypeMap.getInstance();

	public ContainerStorageTypeKey(Long storageTypeID)
	{
		this.storageTypeID = storageTypeID;
	}

	public Long getStorageTypeID()
	{
		return storageTypeID;
	}

	public void setStorageTypeID(Long storageTypeID)
	{
		this.storageTypeID = storageTypeID;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return 1;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		boolean returnValue = false;
		if (obj instanceof ContainerStorageTypeKey)
		{
			ContainerStorageTypeKey key = (ContainerStorageTypeKey) obj;
			if (key.getStorageTypeID().intValue() == storageTypeID.intValue())
			{
				returnValue = true;
			}
		}
		return returnValue;
	}

	public IContainerMap getCorrespondingContainerMap()
	{
		return storageTypeMap;
	}
}