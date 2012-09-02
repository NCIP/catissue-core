/*
 * Created on Jul 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.storage;

import krishagni.catissueplus.mobile.dto.StoragePositionDTO;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StorageContainerGridObject
{

	private long id;

	private String type;

	private Integer oneDimensionCapacity;

	private Integer twoDimensionCapacity;

	private String name;
	
	private String oneDimensionLabellingScheme=Constants.LABELLING_SCHEME_NUMBERS;
	
	private String twoDimensionLabellingScheme=Constants.LABELLING_SCHEME_NUMBERS;
	
	private String oneDimensionLabel;
	
	private String twoDimensionLabel;
	
	private boolean[][] availablePositions;
	
	private StoragePositionDTO[][] positionDetails;
	
	private Integer occupiedPositons;
	
	private Integer emptyPositons;
	
	public StoragePositionDTO[][] getPositionDetails() {
		return positionDetails;
	}

	public void setPositionDetails(StoragePositionDTO[][] positionDetails) {
		this.positionDetails = positionDetails;
	}

	public Integer getOccupiedPositons() {
		return occupiedPositons;
	}

	public void setOccupiedPositons(Integer occupiedPositons) {
		this.occupiedPositons = occupiedPositons;
	}

	public Integer getEmptyPositons() {
		return emptyPositons;
	}

	public void setEmptyPositons(Integer emptyPositons) {
		this.emptyPositons = emptyPositons;
	}

	public String getOneDimensionLabellingScheme()
	{
		return oneDimensionLabellingScheme;
	}

	public void setOneDimensionLabellingScheme(String oneDimensionLabellingScheme)
	{
		this.oneDimensionLabellingScheme = oneDimensionLabellingScheme;
	}
	
	public String getTwoDimensionLabellingScheme()
	{
		return twoDimensionLabellingScheme;
	}
	public void setTwoDimensionLabellingScheme(String twoDimensionLabellingScheme)
	{
		this.twoDimensionLabellingScheme = twoDimensionLabellingScheme;
	}


	/**
	 * @return Returns the oneDimensionCapacity.
	 */
	public Integer getOneDimensionCapacity()
	{
		return this.oneDimensionCapacity;
	}

	/**
	 * @param oneDimensionCapacity The oneDimensionCapacity to set.
	 */
	public void setOneDimensionCapacity(Integer oneDimensionCapacity)
	{
		this.oneDimensionCapacity = oneDimensionCapacity;
	}

	/**
	 * @return Returns the twoDimensionCapacity.
	 */
	public Integer getTwoDimensionCapacity()
	{
		return this.twoDimensionCapacity;
	}

	/**
	 * @param twoDimensionCapacity The twoDimensionCapacity to set.
	 */
	public void setTwoDimensionCapacity(Integer twoDimensionCapacity)
	{
		this.twoDimensionCapacity = twoDimensionCapacity;
	}

	/**
	 * @return Returns the id.
	 */
	public long getId()
	{
		return this.id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(long id)
	{
		this.id = id;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return this.name;
	}
	
	public String getOneDimensionLabel()
	{
		return oneDimensionLabel;
	}

	
	public void setOneDimensionLabel(String oneDimensionLabel)
	{
		this.oneDimensionLabel = oneDimensionLabel;
	}

	
	public String getTwoDimensionLabel()
	{
		return twoDimensionLabel;
	}
	
	public void setTwoDimensionLabel(String twoDimensionLabel)
	{
		this.twoDimensionLabel = twoDimensionLabel;
	}

	
	public boolean[][] getAvailablePositions()
	{
		return availablePositions;
	}

	
	public void setAvailablePositions(boolean[][] availablePositions)
	{
		this.availablePositions = availablePositions;
	}
}