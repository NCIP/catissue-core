
package krishagni.catissueplus.dto;

public class StorageContainerUtilizationDTO
{

	private Long containerId;
	private Integer numberOfSpecimensAssigned;
	private Integer totalStorageCapacity;

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
	public Integer getNumberOfSpecimensAssigned()
	{
		return numberOfSpecimensAssigned;
	}

	/**
	 * @param numberOfSpecimensAssigned the numberOfSpecimensAssigned to set
	 */
	public void setNumberOfSpecimensAssigned(Integer numberOfSpecimensAssigned)
	{
		this.numberOfSpecimensAssigned = numberOfSpecimensAssigned;
	}

	/**
	 * @return the totalStorageCapacity
	 */
	public Integer getTotalStorageCapacity()
	{
		return totalStorageCapacity;
	}

	/**
	 * @param totalStorageCapacity the totalStorageCapacity to set
	 */
	public void setTotalStorageCapacity(Integer totalStorageCapacity)
	{
		this.totalStorageCapacity = totalStorageCapacity;
	}

}
