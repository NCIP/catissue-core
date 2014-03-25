
package krishagni.catissueplus.dto;

public class StorageContainerUtilizationDTO
{

	private Long containerId;
	private Long siteId;
	private Long numberOfSpecimensAssigned = 0l;
	private Long totalStorageCapacity = 0l;
	

	
    public Long getSiteId()
    {
        return siteId;
    }

    
    public void setSiteId(Long siteId)
    {
        this.siteId = siteId;
    }

    /**
	 * @return the containerId
	 */
	public Long getContainerId()
	{
		return containerId;
	}

	/**
	 * @param containerId the containerId to set
	 */
	public void setContainerId(Long containerId)
	{
		this.containerId = containerId;
	}

	/**
	 * @return the numberOfSpecimensAssigned
	 */
	public Long getNumberOfSpecimensAssigned()
	{
		return numberOfSpecimensAssigned;
	}

	/**
	 * @param numberOfSpecimensAssigned the numberOfSpecimensAssigned to set
	 */
	public void setNumberOfSpecimensAssigned(Long numberOfSpecimensAssigned)
	{
		this.numberOfSpecimensAssigned = numberOfSpecimensAssigned;
	}

	/**
	 * @return the totalStorageCapacity
	 */
	public Long getTotalStorageCapacity()
	{
		return totalStorageCapacity;
	}

	/**
	 * @param totalStorageCapacity the totalStorageCapacity to set
	 */
	public void setTotalStorageCapacity(Long totalStorageCapacity)
	{
		this.totalStorageCapacity = totalStorageCapacity;
	}

}
