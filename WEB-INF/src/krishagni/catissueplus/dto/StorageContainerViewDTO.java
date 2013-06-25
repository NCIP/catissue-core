
package krishagni.catissueplus.dto;

public class StorageContainerViewDTO
{

	private Long containerId;
	private String oneDimensionLabellingScheme;
	private String twoDimensionLabellingScheme;
	private String oneDimensionLabel;
	private String twoDimensionLabel;
	private Integer oneDimensionCapacity;
	private Integer twoDimensionCapacity;
	private StoragePositionDTO[][] storagePositionDTOCollection;

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
	 * @return the oneDimensionLabellingScheme
	 */
	public String getOneDimensionLabellingScheme()
	{
		return oneDimensionLabellingScheme;
	}

	/**
	 * @param oneDimensionLabellingScheme the oneDimensionLabellingScheme to set
	 */
	public void setOneDimensionLabellingScheme(String oneDimensionLabellingScheme)
	{
		this.oneDimensionLabellingScheme = oneDimensionLabellingScheme;
	}

	/**
	 * @return the twoDimensionLabellingScheme
	 */
	public String getTwoDimensionLabellingScheme()
	{
		return twoDimensionLabellingScheme;
	}

	/**
	 * @param twoDimensionLabellingScheme the twoDimensionLabellingScheme to set
	 */
	public void setTwoDimensionLabellingScheme(String twoDimensionLabellingScheme)
	{
		this.twoDimensionLabellingScheme = twoDimensionLabellingScheme;
	}

	/**
	 * @return the oneDimensionLabel
	 */
	public String getOneDimensionLabel()
	{
		return oneDimensionLabel;
	}

	/**
	 * @param oneDimensionLabel the oneDimensionLabel to set
	 */
	public void setOneDimensionLabel(String oneDimensionLabel)
	{
		this.oneDimensionLabel = oneDimensionLabel;
	}

	/**
	 * @return the twoDimensionLabel
	 */
	public String getTwoDimensionLabel()
	{
		return twoDimensionLabel;
	}

	/**
	 * @param twoDimensionLabel the twoDimensionLabel to set
	 */
	public void setTwoDimensionLabel(String twoDimensionLabel)
	{
		this.twoDimensionLabel = twoDimensionLabel;
	}

	/**
	 * @return the oneDimensionCapacity
	 */
	public Integer getOneDimensionCapacity()
	{
		return oneDimensionCapacity;
	}

	/**
	 * @param oneDimensionCapacity the oneDimensionCapacity to set
	 */
	public void setOneDimensionCapacity(Integer oneDimensionCapacity)
	{
		this.oneDimensionCapacity = oneDimensionCapacity;
	}

	/**
	 * @return the twoDimensionCapacity
	 */
	public Integer getTwoDimensionCapacity()
	{
		return twoDimensionCapacity;
	}

	/**
	 * @param twoDimensionCapacity the twoDimensionCapacity to set
	 */
	public void setTwoDimensionCapacity(Integer twoDimensionCapacity)
	{
		this.twoDimensionCapacity = twoDimensionCapacity;
	}

	/**
	 * @return the storagePositionDTOCollection
	 */
	public StoragePositionDTO[][] getStoragePositionDTOCollection()
	{
		return storagePositionDTOCollection;
	}

	/**
	 * @param storagePositionDTOCollection the storagePositionDTOCollection to set
	 */
	public void setStoragePositionDTOCollection(StoragePositionDTO[][] storagePositionDTOCollection)
	{
		this.storagePositionDTOCollection = storagePositionDTOCollection;
	}

}
