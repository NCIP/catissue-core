
package krishagni.catissueplus.dto;

public class StoragePositionDTO
{

	private Integer posDimensionX;
	private Integer posDimensionY;
	private String objLabel;

	public String getObjLabel()
	{
		return objLabel;
	}

	/**
	 * @param objLabel the objLabel to set
	 */
	public void setObjLabel(String objLabel)
	{
		this.objLabel = objLabel;
	}

	/**
	 * @return the posDimensionX
	 */
	public Integer getPosDimensionX()
	{
		return posDimensionX;
	}

	/**
	 * @param posDimensionX the posDimensionX to set
	 */
	public void setPosDimensionX(Integer posDimensionX)
	{
		this.posDimensionX = posDimensionX;
	}

	/**
	 * @return the posDimensionY
	 */
	public Integer getPosDimensionY()
	{
		return posDimensionY;
	}

	/**
	 * @param posDimensionY the posDimensionY to set
	 */
	public void setPosDimensionY(Integer posDimensionY)
	{
		this.posDimensionY = posDimensionY;
	}

}
