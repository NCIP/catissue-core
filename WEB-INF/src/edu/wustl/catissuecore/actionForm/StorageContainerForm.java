/**
 * <p>Title: StorageContainerForm Class>
 * <p>Description:  This Class is used to encapsulate all the request parameters passed 
 * from StorageContainer.jsp page. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 15, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.util.HibernateMetaData;
/**
 * This Class is used to encapsulate all the request parameters passed from StorageType.jsp page.
 * @author aniruddha_phadnis
 * */
public class StorageContainerForm extends AbstractActionForm implements IPrinterTypeLocation
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(StorageContainerForm.class);
	private static final long serialVersionUID = 1234567890L;
	/**
	 * An id which refers to the type of the storage.
	 */
	private long typeId = -1;

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
	 * An name for parent container type.
	 */
	private String parentContainerSelected = "Site";
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

	private String printCheckbox;

	private String printerType;

	private String printerLocation;

	private String nextForwardTo;

	/**
	 * Specifies if the barcode is editable or not
	 */
	private String isBarcodeEditable = (String) DefaultValueManager.getDefaultValue(Constants.IS_BARCODE_EDITABLE);

	/**
	 * No argument constructor for StorageTypeForm class 
	 */
	public StorageContainerForm()
	{
		//reset();
	}

	/**
	 * This function Copies the data from an storage type object to a StorageTypeForm object.
	 * @param abstractDomain A StorageType object containing the information about storage type of the container.  
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{

		StorageContainer container = (StorageContainer) abstractDomain;

		this.setId(container.getId().longValue());
		this.setActivityStatus(Utility.toString(container.getActivityStatus()));
		this.containerName = container.getName();
		isFull = AppUtility.initCap(Utility.toString(container.isFull()));
		logger.debug("isFULL />/>/> " + isFull);

		this.typeId = container.getStorageType().getId().longValue();
		this.typeName = container.getStorageType().getName();

		ContainerPosition cntPos = container.getLocatedAtPosition();
		Container parent = null;
		if (cntPos != null)
		{
			parent = cntPos.getParentContainer();
		}
		if (parent != null)
		{
			this.parentContainerId = parent.getId().longValue();
			this.parentContainerSelected = "Auto";

			StorageContainer parentContainer = (StorageContainer) HibernateMetaData.getProxyObjectImpl(parent);
			if (container != null && container.getLocatedAtPosition() != null)
			{
				this.positionInParentContainer = parentContainer.getStorageType().getName() + " : " + parentContainer.getId() + " Pos("
						+ container.getLocatedAtPosition().getPositionDimensionOne() + ","
						+ container.getLocatedAtPosition().getPositionDimensionTwo() + ")";

				//Sri: Fix for bug #

				this.positionDimensionOne = container.getLocatedAtPosition().getPositionDimensionOne().intValue();
				this.positionDimensionTwo = container.getLocatedAtPosition().getPositionDimensionTwo().intValue();
			}

			this.siteName = parentContainer.getSite().getName();
		}

		if (container.getSite() != null)
		{
			this.siteId = container.getSite().getId().longValue();
			this.siteName = container.getSite().getName();
		}

		this.defaultTemperature = Utility.toString(container.getTempratureInCentigrade());
		this.oneDimensionCapacity = container.getCapacity().getOneDimensionCapacity().intValue();
		this.twoDimensionCapacity = container.getCapacity().getTwoDimensionCapacity().intValue();
		this.oneDimensionLabel = container.getStorageType().getOneDimensionLabel();
		this.twoDimensionLabel = Utility.toString(container.getStorageType().getTwoDimensionLabel());

		if (container.getNoOfContainers() != null)
		{
			this.noOfContainers = container.getNoOfContainers().intValue();
		}

		if (container.getStartNo() != null)
		{
			this.startNumber = String.valueOf(container.getStartNo().intValue());
		}

		this.barcode = Utility.toString(container.getBarcode());

		//Populating the collection protocol id array
		Collection collectionProtocolCollection = container.getCollectionProtocolCollection();

		if (collectionProtocolCollection != null && collectionProtocolCollection.size() > 0)
		{
			this.collectionIds = new long[collectionProtocolCollection.size()];
			int i = 0;

			Iterator it = collectionProtocolCollection.iterator();
			while (it.hasNext())
			{
				CollectionProtocol cp = (CollectionProtocol) it.next();
				this.collectionIds[i] = cp.getId().longValue();
				i++;
			}

		}

		//Populating the storage type-id array
		Collection storageTypeCollection = container.getHoldsStorageTypeCollection();

		if (storageTypeCollection != null)
		{
			this.holdsStorageTypeIds = new long[storageTypeCollection.size()];
			int i = 0;

			Iterator it = storageTypeCollection.iterator();
			while (it.hasNext())
			{
				StorageType storageType = (StorageType) it.next();
				this.holdsStorageTypeIds[i] = storageType.getId().longValue();
				i++;
			}
		}

		//Populating the specimen class type-id array
		Collection specimenClassCollection = container.getHoldsSpecimenClassCollection();

		if (specimenClassCollection != null)
		{
			if (specimenClassCollection.size() == AppUtility.getSpecimenClassTypes().size())
			{
				holdsSpecimenClassTypes = new String[1];
				holdsSpecimenClassTypes[0] = "-1";
				this.specimenOrArrayType = "Specimen";
			}
			else
			{
				this.holdsSpecimenClassTypes = new String[specimenClassCollection.size()];
				int i = 0;

				Iterator it = specimenClassCollection.iterator();
				while (it.hasNext())
				{
					String specimenClass = (String) it.next();
					this.holdsSpecimenClassTypes[i] = specimenClass;
					i++;
					this.specimenOrArrayType = "Specimen";
				}
			}
		}
		//      Populating the specimen array type-id array
		Collection specimenArrayTypeCollection = container.getHoldsSpecimenArrayTypeCollection();

		if (specimenArrayTypeCollection != null)
		{
			holdsSpecimenArrTypeIds = new long[specimenArrayTypeCollection.size()];
			int i = 0;

			Iterator it = specimenArrayTypeCollection.iterator();
			while (it.hasNext())
			{
				SpecimenArrayType holdSpArrayType = (SpecimenArrayType) it.next();
				holdsSpecimenArrTypeIds[i] = holdSpArrayType.getId().longValue();
				i++;
				this.specimenOrArrayType = "SpecimenArray";
			}
		}

	}

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

	public String getParentContainerSelected()
	{
		return parentContainerSelected;
	}

	public void setParentContainerSelected(String parentContainerSelected)
	{
		this.parentContainerSelected = parentContainerSelected;
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

	/**
	 * @return Returns the id assigned to form bean
	 */
	public int getFormId()
	{
		int formId;
		if (getNoOfContainers() > 1)
		{
			formId = Constants.SIMILAR_CONTAINERS_FORM_ID;
		}
		else
		{
			formId = Constants.STORAGE_CONTAINER_FORM_ID;
		}
		return formId;
	}

	/**
	 * Resets the values of all the fields.
	 * Is called by the overridden reset method defined in ActionForm.  
	 * */
	protected void reset()
	{

	}

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
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	/*public void setValue(String key, Object value)
	{
		if (isMutable())
			values.put(key, value);
	}*/

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	/*public Object getValue(String key)
	{
		return values.get(key);
	}*/

	/**
	 * @return Returns the values.
	 */
	/*	public Collection getAllValues()
		{
			return values.values();
		}*/

	/**
	 * @param values The values to set.
	 */
	/*public void setValues(Map values)
	{
		this.values = values;
	}*/

	/**
	 * @return values The values to set.
	 */
	/*public Map getValues()
	{
		return this.values;
	}*/

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
	 * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
	 * @param addNewFor - FormBean ID of the object inserted
	 *  @param addObjectIdentifier - Identifier of the Object inserted 
	 */
	public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
	{
		if ("storageType".equals(addNewFor))
		{
			setTypeId(addObjectIdentifier.longValue());
		}
		else if ("site".equals(addNewFor))
		{
			setSiteId(addObjectIdentifier.longValue());
		}
	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();

		try
		{
			logger.info("No of containers---------in validate::" + this.noOfContainers);
			//if (this.noOfContainers == 1)
			//{
			if (this.typeId == -1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR,
						new ActionError("errors.item.required", ApplicationProperties.getValue("storageContainer.type")));
			}
			if (!validator.isValidOption(isFull) && this.noOfContainers == 1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected", ApplicationProperties
						.getValue("storageContainer.isContainerFull")));
			}

			if (parentContainerSelected.equals(Constants.SITE) && siteId == -1 && this.noOfContainers == 1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR,
						new ActionError("errors.item.required", ApplicationProperties.getValue("storageContainer.site")));
			}
			else if ("Auto".equals(parentContainerSelected) && this.noOfContainers == 1)
			{
				if ("Auto".equals(parentContainerSelected))
				{
					if (!validator.isNumeric(String.valueOf(positionDimensionOne), 1)
							|| !validator.isNumeric(String.valueOf(positionDimensionTwo), 1)
							|| !validator.isNumeric(String.valueOf(parentContainerId), 1))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
								.getValue("storageContainer.parentContainer")));
					}
				}
				else
				{
					checkPositionForParent(errors);
				}

			}
			else if (parentContainerSelected.equals("Manual") && this.noOfContainers >= 1)
			{

				checkPositionForParent(errors);
				/*	if (!validator.isNumeric(String.valueOf(pos1), 1)
							|| !validator.isNumeric(String.valueOf(pos2), 1))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
								ApplicationProperties.getValue("storageContainer.parentContainer")));
					} */
			}

			/*if (this.noOfContainers == 1)
			{*/
			checkValidNumber(String.valueOf(noOfContainers), "storageContainer.noOfContainers", errors, validator);
			/*}*/
			//validations for Container name
			//Modified by falguni
			if (!edu.wustl.catissuecore.util.global.Variables.isStorageContainerLabelGeneratorAvl && validator.isEmpty(containerName)
					&& this.noOfContainers == 1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR,
						new ActionError("errors.item.required", ApplicationProperties.getValue("storageContainer.name")));
			}

			//validation for collection protocol
			if (collectionIds.length > 1)
			{
				for (int i = 0; i < collectionIds.length; i++)
				{
					if (collectionIds[i] == -1)
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
								.getValue("storageContainer.collectionProtocolTitle")));
					}

				}
			}

			//validation for holds storage type
			checkValidSelectionForAny(holdsStorageTypeIds, "storageContainer.containerType", errors);
			//validation for holds specimen class
			/*new chnage checkValidSelectionForAny(holdsSpecimenClassTypeIds, "storageContainer.specimenType",
					errors);*/

			if (this.getOperation().equals(Constants.EDIT) && !validator.isValidOption(this.getActivityStatus()))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties.getValue("site.activityStatus")));
			}
			// validations for temperature
			if (!validator.isEmpty(defaultTemperature) && (!validator.isDouble(defaultTemperature, false)))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
						.getValue("storageContainer.temperature")));
			}

			//VALIDATIONS FOR DIMENSION 1.
			if (validator.isEmpty(String.valueOf(oneDimensionCapacity)))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties
						.getValue("storageContainer.oneDimension")));
			}
			else
			{
				if (!validator.isNumeric(String.valueOf(oneDimensionCapacity)))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
							.getValue("storageContainer.oneDimension")));
				}
			}

			//Validations for dimension 2
			if (!validator.isEmpty(String.valueOf(twoDimensionCapacity)) && (!validator.isNumeric(String.valueOf(twoDimensionCapacity))))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
						.getValue("storageContainer.twoDimension")));
			}

			if (this.noOfContainers > 1 && this.getSimilarContainersMap().size() > 0)
			{

				String containerPrefixKey = "simCont:";

				for (int i = 1; i <= this.noOfContainers; i++)
				{
					String iBarcode = (String) this.getSimilarContainerMapValue("simCont:" + i + "_barcode"); //simCont:1_barcode
					if (iBarcode != null && iBarcode.equals("")) // this is done because barcode is empty string set by struts
					{ // but barcode in DB is unique but can be null.
						this.setSimilarContainerMapValue("simCont:" + i + "_barcode", null);
					}

					int checkedButtonStatus = Integer.parseInt((String) getSimilarContainerMapValue("checkedButton"));
					String containerName = (String) getSimilarContainerMapValue("simCont:" + i + "_name");
					if (!edu.wustl.catissuecore.util.global.Variables.isStorageContainerLabelGeneratorAvl && validator.isEmpty(containerName))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties
								.getValue("storageContainer.name")));
					}
					String siteId = (String) getSimilarContainerMapValue("simCont:" + i + "_siteId");
					if (checkedButtonStatus == 2 || siteId == null)
					{

						String radioButonKey = "radio_" + i;
						String containerIdKey = containerPrefixKey + i + "_parentContainerId";
						String containerNameKey = containerPrefixKey + i + "_StorageContainer_name";
						String posDim1Key = containerPrefixKey + i + "_positionDimensionOne";
						String posDim2Key = containerPrefixKey + i + "_positionDimensionTwo";

						if (((String) getSimilarContainerMapValue(radioButonKey)).equals("1"))
						{
							String parentContId = (String) getSimilarContainerMapValue(containerIdKey);
							String positionDimensionOne = (String) getSimilarContainerMapValue(posDim1Key);
							String positionDimensionTwo = (String) getSimilarContainerMapValue(posDim2Key);

							if (parentContId.equals("-1") || positionDimensionOne.equals("-1") || positionDimensionTwo.equals("-1"))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties
										.getValue("similarcontainers.location")));
								this.setSimilarContainerMapValue("checkedButton", "2");
							}
						}
						else
						{
							String positionDimensionOne = (String) getSimilarContainerMapValue(posDim1Key + "_fromMap");
							String positionDimensionTwo = (String) getSimilarContainerMapValue(posDim2Key + "_fromMap");

							if (positionDimensionOne != null && !positionDimensionOne.trim().equals("") && !validator.isDouble(positionDimensionOne)
									|| positionDimensionTwo != null && !positionDimensionTwo.trim().equals("")
									&& !validator.isDouble(positionDimensionTwo))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
										.getValue("specimen.positionInStorageContainer")));
								break;

							}

						}
					}
					else
					{
						if (siteId.equals("-1"))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties
									.getValue("storageContainer.site")));
						}
					}
				}
			}
			//}

		}
		catch (Exception excp)
		{
			logger.error(excp.getMessage(), excp);
		}
		return errors;
	}

	/**
	 * @param errors ActionErrors
	 */
	private void checkPositionForParent(ActionErrors errors)
	{
		boolean flag = StorageContainerUtil.checkPos1AndPos2(this.pos1, this.pos2);
		if (flag)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
					.getValue("storageContainer.parentContainer")));
		}

	}

	/**
	 * This function if 'any' option is selected then no other option should be selected
	 * @param errors Action Errors
	 * @param Ids Array of long ids
	 * @param message Message used in ApplicationProperties
	 * */
	void checkValidSelectionForAny(long[] Ids, String message, ActionErrors errors)
	{
		if (Ids != null && Ids.length > 1)
		{
			for (int i = 0; i < Ids.length; i++)
			{
				if (Ids[i] == 1)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties.getValue(message)));
					break;
				}
			}
		}
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

	public String getNextForwardTo()
	{
		return nextForwardTo;
	}

	public void setNextForwardTo(String nextForwardTo)
	{
		this.nextForwardTo = nextForwardTo;
	}

	public String getPrintCheckbox()
	{
		return printCheckbox;
	}

	public void setPrintCheckbox(String printCheckbox)
	{
		this.printCheckbox = printCheckbox;
	}

	public String getPrinterLocation()
	{
		return printerLocation;
	}

	public void setPrinterLocation(String printerLocation)
	{
		this.printerLocation = printerLocation;
	}

	public String getPrinterType()
	{
		return printerType;
	}

	public void setPrinterType(String printerType)
	{
		this.printerType = printerType;
	}

	/**
	 * @return isBarcodeEditable
	 */
	public String getIsBarcodeEditable()
	{
		return isBarcodeEditable;
	}

	/** 
	 * @param isBarcodeEditable Setter method for isBarcodeEditable
	 */
	public void setIsBarcodeEditable(String isBarcodeEditable)
	{
		this.isBarcodeEditable = isBarcodeEditable;
	}
}