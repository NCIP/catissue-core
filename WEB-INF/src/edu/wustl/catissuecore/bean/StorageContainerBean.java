package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.wustl.common.util.global.Status;

public class StorageContainerBean implements Serializable
{
	private static final long serialVersionUID = 1L;

	private long typeId = -1;

	private String activityStatus = Status.ACTIVITY_STATUS_ACTIVE.toString();
	/**
	 * An name which refers to the type of the storage.
	 */
	private String typeName;

	/**
	 * An id which refers to Parent Container of this container.
	 */
	private long parentContainerId;

	/**
	 * Position label shown after selecting from storage container map viewer.
	 */
	private String positionInParentContainer;

	/**
	 * An id which refers to the site of the container if it is parent container.
	 */
	private long siteId;
	/**
	 * An name which refers to the site of the container if it is parent container.
	 */
	private String siteName;

	/**
	 * A default temperature of the storage container.
	 */
	private String defaultTemperature;

	/**
	 * Capacity in dimension one.
	 */
	private int oneDimensionCapacity;

	/**
	 * Capacity in dimension two.
	 */
	private int twoDimensionCapacity = 1;

	/**
	 * Text label for dimension one.
	 */
	private String oneDimensionLabel;

	/**
	 * Text label for dimension two.
	 */
	private String twoDimensionLabel;

	/**
	 * Starting Number.
	 */
	private String startNumber = "";

	/** newly added by vaishali on 20th June 4.04 pm
	 * 
	 */
	private String containerName = "";
	/**
	 * No. of containers.
	 */
	private int noOfContainers = 1;

	/**
	 * Text label for dimension two.
	 */
	private String barcode;
	/**
	 * Key.
	 */
	private String key;

	/**
	 * Radio button to choose site/parentContainer.
	 */
	private int checkedButton = 1;

	/**
	 * Radio button to choose dropdown or map to select storage container.
	 */
	private int stContSelection = 1;
	/**
	 * Storage container name selected from map
	 */
	private String selectedContainerName;
	/**
	 * Storage pos1 selected from map
	 */
	private String pos1;
	/**
	 * Storage pos2 selected from map
	 */
	private String pos2;
	/**
	 * Storage Id selected from map
	 */
	private String containerId;
	/**
	 * Storage contianer parent type
	 */
	private String parentContainerSelected;

	private long id;
	/**
	 * Returns an id which refers to the type of the storage.
	 * @return An id which refers to the type of the storage.
	 * @see #setTypeId(long)
	 */
	public long getTypeId()
	{
		return this.typeId;
	}
	/**
	 * Returns the activity status
	 * @return the activityStatus.
	 * @see #setActivityStatus(String)
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}
	/**
	 * Sets the activity status.
	 * @param activityStatus activity status.
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}
	/**
	 * @return Returns the containerId.
	 */
	public String getContainerId()
	{
		return containerId;
	}
	/**
	 * @param containerId The containerId to set.
	 */
	public void setContainerId(String containerId)
	{
		this.containerId = containerId;
	}
	/**
	 * @return Returns the pos1.
	 */
	public String getPos1()
	{
		return pos1;
	}
	/**
	 * @param pos1 The pos1 to set.
	 */
	public void setPos1(String pos1)
	{
		this.pos1 = pos1;
	}
	/**
	 * @return Returns the pos2.
	 */
	public String getPos2()
	{
		return pos2;
	}
	/**
	 * @param pos2 The pos2 to set.
	 */
	public void setPos2(String pos2)
	{
		this.pos2 = pos2;
	}
	/**
	 * For parent container type.
	 */
	public String getParentContainerSelected()
	{
		return parentContainerSelected;
	}
	public void setParentContainerSelected(String parentContainerSelected)
	{
		this.parentContainerSelected=parentContainerSelected;
	}
	/**
	 * @return Returns the selectedContainerName.
	 */
	public String getSelectedContainerName()
	{
		return selectedContainerName;
	}
	/**
	 * @param selectedContainerName The selectedContainerName to set.
	 */
	public void setSelectedContainerName(String selectedContainerName)
	{
		this.selectedContainerName = selectedContainerName;
	}
	/**
	 * @return Returns the stContSelection.
	 */
	public int getStContSelection()
	{
		return stContSelection;
	}
	/**
	 * @param stContSelection The stContSelection to set.
	 */
	public void setStContSelection(int stContSelection)
	{
		this.stContSelection = stContSelection;
	}
	/**
	 * Sets an id which refers to the type of the storage.
	 * @param typeId An id which refers to the type of the storage.
	 * @see #getTypeId()
	 */
	public void setTypeId(long typeId)
	{
		this.typeId = typeId;
	}


	/**
	 * Sets an name which refers to the type of the storage.
	 * @param typeName An id which refers to the type of the storage.
	 * @see #getTypeName()
	 */
	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}
	
	/**
	 * Returns an name which refers to the type of the storage.
	 * @return An name which refers to the type of the storage.
	 * @see #setTypeName(String)
	 */
	public String getTypeName()
	{
		return this.typeName;
	}

		/**
	 * Returns the default temperature of the storage container.
	 * @return double the default temperature of the storage container to be set.
	 * @see #setDefaultTemperature(double)
	 */
	public String getDefaultTemperature()
	{
		return this.defaultTemperature;
	}

	/**
	 * Sets the default temperature of the storage container.
	 * @param defaultTemperature the default temperature of the storage container to be set.
	 * @see #getDefaultTemperature()
	 */
	public void setDefaultTemperature(String defaultTemperature)
	{
		this.defaultTemperature = defaultTemperature;
	}

	/**
	 * Returns the capacity of dimension one.
	 * @return int the capacity of dimension one.
	 * @see #setOneDimensionCapacity(int)
	 */
	public int getOneDimensionCapacity()
	{
		return this.oneDimensionCapacity;
	}

	/**
	 * Sets the capacity of dimension one.
	 * @param oneDimensionCapacity the capacity of dimension one to be set.
	 * @see #getOneDimensionCapacity()
	 */
	public void setOneDimensionCapacity(int oneDimensionCapacity)
	{
		this.oneDimensionCapacity = oneDimensionCapacity;
	}

	/**
	 * Returns the capacity of dimension two.
	 * @return int the capacity of dimension two.
	 * @see #setTwoDimensionCapacity(int)
	 */
	public int getTwoDimensionCapacity()
	{
		return this.twoDimensionCapacity;
	}

	/**
	 * Sets the capacity of dimension two.
	 * @param twoDimensionCapacity the capacity of dimension two to be set.
	 * @see #getTwoDimensionCapacity()
	 */
	public void setTwoDimensionCapacity(int twoDimensionCapacity)
	{
		this.twoDimensionCapacity = twoDimensionCapacity;
	}

	/**
	 * Returns the label of dimension one.
	 * @return String the label of dimension one.
	 * @see #setOneDimensionLabel(String)
	 */
	public String getOneDimensionLabel()
	{
		return this.oneDimensionLabel;
	}

	/**
	 * Sets the label of dimension one.
	 * @param oneDimensionLabel the label of dimension one to be set.
	 * @see #getOneDimensionLabel()
	 */
	public void setOneDimensionLabel(String oneDimensionLabel)
	{
		this.oneDimensionLabel = oneDimensionLabel;
	}

	/**
	 * Returns the label of dimension two.
	 * @return String the label of dimension two.
	 * @see #setTwoDimensionLabel(String)
	 */
	public String getTwoDimensionLabel()
	{
		return this.twoDimensionLabel;
	}

	/**
	 * Sets the label of dimension two.
	 * @param twoDimensionLabel the label of dimension two to be set.
	 * @see #getTwoDimensionLabel()
	 */
	public void setTwoDimensionLabel(String twoDimensionLabel)
	{
		this.twoDimensionLabel = twoDimensionLabel;
	}

	/**
	 * Returns an id which refers to Parent Container of this container.
	 * @return long An id which refers to Parent Container of this container.
	 * @see #setParentContainerId(long)
	 */
	public long getParentContainerId()
	{
		return parentContainerId;
	}

	/**
	 * Sets an id which refers to Parent Container of this container.
	 * @param parentContainerId An id which refers to Parent Container of this container.
	 * @see #getParentContainerId()
	 */
	public void setParentContainerId(long parentContainerId)
	{
		this.parentContainerId = parentContainerId;
	}

	/**
	 * @return Returns the positionInParentContainer.
	 */
	public String getPositionInParentContainer()
	{
		return positionInParentContainer;
	}

	/**
	 * @param positionInParentContainer The positionInParentContainer to set.
	 */
	public void setPositionInParentContainer(String positionInParentContainer)
	{
		this.positionInParentContainer = positionInParentContainer;
	}

	/**
	 * Returns an id which refers to the site of the container if it is parent container.
	 * @return long An id which refers to the site of the container if it is parent container.
	 * @see #setSiteId(long)
	 */
	public long getSiteId()
	{
		return siteId;
	}

	/**
	 * Sets an id which refers to the site of the container if it is parent container.
	 * @param siteId An id which refers to the site of the container if it is parent container.
	 * @see #getSiteId()
	 */
	public void setSiteId(long siteId)
	{
		this.siteId = siteId;
	}


	/**
	 * Returns an name which refers to the site of the container if it is parent container.
	 * @return String An name which refers to the site of the container if it is parent container.
	 * @see #setSiteName(String)
	 */
	public String getSiteName()
	{
		return siteName;
	}

	/**
	 * Sets an name which refers to the site of the container if it is parent container.
	 * @param siteName An name which refers to the site of the container if it is parent container.
	 * @see #getSiteName()
	 */
	public void setSiteName(String siteName)
	{
		this.siteName = siteName;
	}
	
	
	public void setId(long id)
	{
		this.id=id;
	}
	
	public long getID()
	{
		return id;
	}
	
	/**
	 * Tells whether this container is full or not.
	 */
	
	private String isFull = "False";

	/**
	 * Map to handle values of all the CollectionProtocol Events
	 */
	//protected Map values = new HashMap();

	/** 
	 * Positon for dimension 1
	 */
	private int positionDimensionOne;

	/**
	 * Position for dimension 2
	 */
	private int positionDimensionTwo;

	/**
	 * site name for particular parent container
	 */
	private String siteForParentContainer;
	/**
	 * collectionIds contains Ids of collection Protocols that this container can hold
	 */
	protected long[] collectionIds = new long[]{-1};

	/**
	 * holdStorageTypeIds contains Ids of Storage Types that this container can hold
	 */
	protected long[] holdsStorageTypeIds;

	private long[] holdsSpecimenArrTypeIds;
	/**
	 * holdSpecimenClassTypeIds contains Ids of Specimen Types that this container can hold
	 */
	protected String[] holdsSpecimenClassTypes;

	/**
	 * A map that contains distinguished fields (container name,barcode,parent location) per container.
	 */
	private Map similarContainersMap = new HashMap();

	private String specimenOrArrayType;
	
	

	/**
	 * @return Returns the noOfContainers.
	 */
	public int getNoOfContainers()
	{
		return noOfContainers;
	}

	/**
	 * @param noOfContainers The noOfContainers to set.
	 */
	public void setNoOfContainers(int noOfContainers)
	{
		this.noOfContainers = noOfContainers;
	}

	/**
	 * @return Returns the startNumber.
	 */
	public String getStartNumber()
	{
		return startNumber;
	}

	/**
	 * @param startNumber The startNumber to set.
	 */
	public void setStartNumber(String startNumber)
	{
		this.startNumber = startNumber;
	}

	/**
	 * @return Returns the barcode.
	 */
	public String getBarcode()
	{
		return barcode;
	}

	/**
	 * @param barcode The barcode to set.
	 */
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}

	/**
	 * @return Returns the key.
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * @param key The key to set.
	 */
	public void setKey(String key)
	{
		this.key = key;
	}

	/**
	 * @return Returns the checkedButton.
	 */
	public int getCheckedButton()
	{
		return checkedButton;
	}

	/**
	 * @param checkedButton The checkedButton to set.
	 */
	public void setCheckedButton(int checkedButton)
	{
		this.checkedButton = checkedButton;
	}

	/**
	 * @return Returns the isFull.
	 */
	public String getIsFull()
	{
		return isFull;
	}

	/**
	 * @param isFull The isFull to set.
	 */
	public void setIsFull(String isFull)
	{
		this.isFull = isFull;
	}

	/**
	 * Gets the Container Name
	 * @return container Name
	 */
	public String getContainerName()
	{
		return this.containerName;
	}

	/**
	 * sets the name of the container
	 * @param containerName container Name to set
	 */
	public void setContainerName(String containerName)
	{
		this.containerName = containerName;
	}

	/**
	 * @return Returns the positionDimensionOne.
	 */
	public int getPositionDimensionOne()
	{
		return positionDimensionOne;
	}

	/**
	 * @param positionDimensionOne The positionDimensionOne to set.
	 */
	public void setPositionDimensionOne(int positionDimensionOne)
	{
		this.positionDimensionOne = positionDimensionOne;
	}

	/**
	 * @return Returns the positionDimensionTwo.
	 */
	public int getPositionDimensionTwo()
	{
		return positionDimensionTwo;
	}

	/**
	 * @param positionDimensionTwo The positionDimensionTwo to set.
	 */
	public void setPositionDimensionTwo(int positionDimensionTwo)
	{
		this.positionDimensionTwo = positionDimensionTwo;
	}

	/**
	 * getitng collection Ids that this container can hold
	 * @return collection Id's array
	 */
	public long[] getCollectionIds()
	{
		return this.collectionIds;
	}

	/**
	 * setitng the Collection Id array
	 * @param collectionIds - array of collection Ids to set
	 */
	public void setCollectionIds(long[] collectionIds)
	{
		this.collectionIds = collectionIds;
	}

	/**
	 * getting Specimen class Type Ids that this container can hold 
	 * @return specimenClassType Id's array
	 */
	public String[] getHoldsSpecimenClassTypes()
	{
		return holdsSpecimenClassTypes;
	}

	/**
	 * setitng the SpecimenClassType Id array
	 * @param holdsSpecimenClassTypes - array of SpecimenClassType Id's to set
	 */
	public void setHoldsSpecimenClassTypes(String[] holdsSpecimenClassTypes)
	{
		this.holdsSpecimenClassTypes = holdsSpecimenClassTypes;
	}

	/**
	 * getitng StorageType Id's that this container can hold
	 * @return StorageType Id' array
	 */
	public long[] getHoldsStorageTypeIds()
	{
		return holdsStorageTypeIds;
	}

	/**
	 * setting the StorageType Id array
	 * @param holdsStorageTypeIds - array of StorageType id's to set
	 */
	public void setHoldsStorageTypeIds(long[] holdsStorageTypeIds)
	{
		this.holdsStorageTypeIds = holdsStorageTypeIds;
	}

	
	/**
     * Sets the Specimen Array Type Holds List.
     * @param holdsSpecimenArrTypeIds the list of specimen array type Ids to be set.
     * @see #getHoldsSpecimenArrTypeIds()
     */
	public void setHoldsSpecimenArrTypeIds(long[] holdsSpecimenArrTypeIds)
	{
		this.holdsSpecimenArrTypeIds = holdsSpecimenArrTypeIds;
	}

	 /**
     * Returns the list of specimen array type Ids that this Storage Type can hold.
     * @return long[] the list of specimen array type Ids.
     * @see #setHoldsSpecimenArrTypeIds(long[])
     */
    public long[] getHoldsSpecimenArrTypeIds()
	{
		return holdsSpecimenArrTypeIds;
	}
	/**
	 * Returns the map that contains distinguished fields per aliquots.
	 * @return The map that contains distinguished fields per aliquots.
	 * @see #setAliquotMap(Map)
	 */
	public Map getSimilarContainersMap()
	{
		//System.out.println("AliquotForm : getAliquotMap "+similarContainersMap);
		return similarContainersMap;
	}

	/**
	 * Sets the map of distinguished fields of aliquots.
	 * @param similarContainersMap A map of distinguished fields of aliquots.
	 * @see #getAliquotMap()
	 */
	public void setSimilarContainersMap(Map similarContainersMap)
	{
		//System.out.println("AliquotForm : setAliquotMap "+similarContainersMap);
		this.similarContainersMap = similarContainersMap;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is to be mapped.
	 */
	public void setSimilarContainerMapValue(String key, Object value)
	{
		//System.out.println("simCont: setValue -> "+key+" "+value);
		similarContainersMap.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getSimilarContainerMapValue(String key)
	{
		//System.out.println("simCont: getValue <- "+key+" "+similarContainersMap.get(key));
		return similarContainersMap.get(key);
	}
	/**
	 * @return siteForParentContainer
	 */
	public String getSiteForParentContainer()
	{
		return siteForParentContainer;
	}

	/**
	 * @param siteForParentContainer Setting siteForParentContainer
	 */
	public void setSiteForParentContainer(String siteForParentContainer)
	{
		this.siteForParentContainer = siteForParentContainer;
	}

	/**
	 * @return specimenOrArrayType
	 */
	public String getSpecimenOrArrayType()
	{
		return specimenOrArrayType;
	}

	/**
	 * @param specimenOrArrayType Setting specimenOrArrayType
	 */
	public void setSpecimenOrArrayType(String specimenOrArrayType)
	{
		this.specimenOrArrayType = specimenOrArrayType;
	}
}
