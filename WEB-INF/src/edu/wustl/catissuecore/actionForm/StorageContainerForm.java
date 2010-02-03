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
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.util.HibernateMetaData;

/**
 * This Class is used to encapsulate all the request parameters passed from StorageType.jsp page.
 * @author aniruddha_phadnis
 * */
public class StorageContainerForm extends AbstractActionForm implements IPrinterTypeLocation, ISpecimenType
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(StorageContainerForm.class);
	/**
	 * SerialVersionUID.
	 */
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

	/**
	 * Container name.
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
	 * Storage container name selected from map.
	 */
	private String selectedContainerName;
	/**
	 * Storage pos1 selected from map.
	 */
	private String pos1;
	/**
	 * Storage pos2 selected from map.
	 */
	private String pos2;
	/**
	 * Storage Id selected from map.
	 */
	private String containerId;

	/**
	 * Tells whether this container is full or not.
	 */
	private String isFull = "False";
	/**
	 * Position for dimension 1.
	 */
	private int positionDimensionOne;

	/**
	 * Position for dimension 2.
	 */
	private int positionDimensionTwo;

	/**
	 * site name for particular parent container.
	 */
	private String siteForParentContainer;
	/**
	 * collectionIds contains Id's of collection Protocols that this container can hold.
	 */
	private long[] collectionIds = new long[]{-1};

	/**
	 * holdStorageTypeIds contains Id's of Storage Types that this container can hold.
	 */
	private long[] holdsStorageTypeIds;
	/**
	 * holdsSpecimenArrTypeIds contains Id's of SpecimenArray type that this container can hold.
	 */
	private long[] holdsSpecimenArrTypeIds;
	/**
	 * holdSpecimenClassTypeIds contains Ids of Specimen Types that this container can hold.
	 */
	private String[] holdsSpecimenClassTypes;
	/**
	 * List of Specimen Types that Storage Type can hold.
	 */
	private String[] holdsTissueSpType;
	/**
	 * List of Specimen Types that Storage Type can hold.
	 */
	private String[] holdsFluidSpType;
	/**
	 * List of Specimen Types that Storage Type can hold.
	 */
	private String[] holdsCellSpType;
	/**
	 * List of Specimen Types that Storage Type can hold.
	 */
	private String[] holdsMolSpType;
	/**
	 * @return holdsTissueSpType holdsTissueSpType.
	 */
	public String[] getHoldsTissueSpType()
	{
		return holdsTissueSpType;
	}

	/**
	 * @param holdsTissueSpType holdsTissueSpType
	 */
	public void setHoldsTissueSpType(String[] holdsTissueSpType)
	{
		this.holdsTissueSpType = holdsTissueSpType;
	}

	/**
	 * @return holdsFluidSpType
	 */
	public String[] getHoldsFluidSpType()
	{
		return holdsFluidSpType;
	}

	/**
	 * @param holdsFluidSpType holdsFluidSpType
	 */
	public void setHoldsFluidSpType(String[] holdsFluidSpType)
	{
		this.holdsFluidSpType = holdsFluidSpType;
	}

	/**
	 * @return holdsCellSpType
	 */
	public String[] getHoldsCellSpType()
	{
		return holdsCellSpType;
	}

	/**
	 * @param holdsCellSpType holdsCellSpType
	 */
	public void setHoldsCellSpType(String[] holdsCellSpType)
	{
		this.holdsCellSpType = holdsCellSpType;
	}

	/**
	 * @return holdsMolSpType
	 */
	public String[] getHoldsMolSpType()
	{
		return holdsMolSpType;
	}

	/**
	 *@param holdsMolSpType holdsMolSpType
	 */
	public void setHoldsMolSpType(String[] holdsMolSpType)
	{
		this.holdsMolSpType = holdsMolSpType;
	}

	/**
	 * A map that contains distinguished fields (container name,barcode,parent location) per container.
	 */
	private Map similarContainersMap = new HashMap();
	/**
	 * SpecimenOrArray Type.
	 */
	private String specimenOrArrayType;
	/**
	 * Print Check box.
	 */
	private String printCheckbox;
	/**
	 * Printer Type.
	 */
	private String printerType;
	/**
	 * Printer Location.
	 */
	private String printerLocation;
	/**
	 * Forward To.
	 */
	private String nextForwardTo;
	/**
	 * Specifies if the Bar code is editable or not.
	 */
	private String isBarcodeEditable = (String) DefaultValueManager
			.getDefaultValue(Constants.IS_BARCODE_EDITABLE);

	/**
	 * No argument constructor for StorageTypeForm class.
	 */
	public StorageContainerForm()
	{
		//reset();
	}

	/**
	 * This function Copies the data from an storage type object to a StorageTypeForm object.
	 * @param abstractDomain StorageContainerObject
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{

		final StorageContainer container = (StorageContainer) abstractDomain;

		this.setId(container.getId().longValue());
		this.setActivityStatus(CommonUtilities.toString(container.getActivityStatus()));
		this.containerName = container.getName();
		this.isFull = AppUtility.initCap(CommonUtilities.toString(container.getFull()));
		logger.debug("isFULL />/>/> " + this.isFull);
		this.typeId = container.getStorageType().getId().longValue();
		this.typeName = container.getStorageType().getName();

		final ContainerPosition cntPos = container.getLocatedAtPosition();
		Container parent = null;
		if (cntPos != null)
		{
			parent = cntPos.getParentContainer();
		}
		if (parent != null)
		{
			this.parentContainerId = parent.getId().longValue();
			this.parentContainerSelected = "Auto";

			final StorageContainer parentContainer = (StorageContainer) HibernateMetaData
					.getProxyObjectImpl(parent);
			if (container != null && container.getLocatedAtPosition() != null)
			{
				this.positionInParentContainer = parentContainer.getStorageType().getName() + " : "
						+ parentContainer.getId() + " Pos("
						+ container.getLocatedAtPosition().getPositionDimensionOne() + ","
						+ container.getLocatedAtPosition().getPositionDimensionTwo() + ")";

				//Sri: Fix for bug #

				this.positionDimensionOne = container.getLocatedAtPosition()
						.getPositionDimensionOne().intValue();
				this.positionDimensionTwo = container.getLocatedAtPosition()
						.getPositionDimensionTwo().intValue();
			}

			this.siteName = parentContainer.getSite().getName();
		}

		if (container.getSite() != null)
		{
			this.siteId = container.getSite().getId().longValue();
			this.siteName = container.getSite().getName();
		}

		this.defaultTemperature = CommonUtilities.toString(container.getTempratureInCentigrade());
		this.oneDimensionCapacity = container.getCapacity().getOneDimensionCapacity().intValue();
		this.twoDimensionCapacity = container.getCapacity().getTwoDimensionCapacity().intValue();
		this.oneDimensionLabel = container.getStorageType().getOneDimensionLabel();
		this.twoDimensionLabel = CommonUtilities.toString(container.getStorageType()
				.getTwoDimensionLabel());

		if (container.getNoOfContainers() != null)
		{
			this.noOfContainers = container.getNoOfContainers().intValue();
		}

		if (container.getStartNo() != null)
		{
			this.startNumber = String.valueOf(container.getStartNo().intValue());
		}

		this.barcode = CommonUtilities.toString(container.getBarcode());

		//Populating the collection protocol id array
		final Collection collectionProtocolCollection = container.getCollectionProtocolCollection();

		if (collectionProtocolCollection != null && collectionProtocolCollection.size() > 0)
		{
			this.collectionIds = new long[collectionProtocolCollection.size()];
			int i = 0;

			final Iterator it = collectionProtocolCollection.iterator();
			while (it.hasNext())
			{
				final CollectionProtocol cp = (CollectionProtocol) it.next();
				this.collectionIds[i] = cp.getId().longValue();
				i++;
			}

		}

		//Populating the storage type-id array
		final Collection storageTypeCollection = container.getHoldsStorageTypeCollection();

		if (storageTypeCollection != null)
		{
			this.holdsStorageTypeIds = new long[storageTypeCollection.size()];
			int i = 0;

			final Iterator it = storageTypeCollection.iterator();
			while (it.hasNext())
			{
				final StorageType storageType = (StorageType) it.next();
				this.holdsStorageTypeIds[i] = storageType.getId().longValue();
				i++;
			}
		}

		//Populating the specimen class type-id array
		final Collection<String> specimenClassCollection = container.getHoldsSpecimenClassCollection();
		if (specimenClassCollection != null)
		{
			if (specimenClassCollection.size() == AppUtility.getSpecimenClassTypes().size())
			{
				this.holdsSpecimenClassTypes = new String[1];
				this.holdsSpecimenClassTypes[0] = "-1";
				this.specimenOrArrayType = "Specimen";
			}
			else
			{
				this.holdsSpecimenClassTypes = new String[specimenClassCollection.size()];
				int i = 0;

				final Iterator it = specimenClassCollection.iterator();
				while (it.hasNext())
				{
					final String specimenClass = (String) it.next();
					this.holdsSpecimenClassTypes[i] = specimenClass;
					i++;
					this.specimenOrArrayType = "Specimen";
				}
			}
		}
		final Collection specimenArrayTypeCollection = container
				.getHoldsSpecimenArrayTypeCollection();

		if (specimenArrayTypeCollection != null)
		{
			this.holdsSpecimenArrTypeIds = new long[specimenArrayTypeCollection.size()];
			int i = 0;

			final Iterator it = specimenArrayTypeCollection.iterator();
			while (it.hasNext())
			{
				final SpecimenArrayType holdSpArrayType = (SpecimenArrayType) it.next();
				this.holdsSpecimenArrTypeIds[i] = holdSpArrayType.getId().longValue();
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
		return this.containerId;
	}

	/**
	 * @param containerId The containerId to set.
	 */
	public void setContainerId(String containerId)
	{
		this.containerId = containerId;
	}
	/**
	 * @return parentContainerSelected
	 */
	public String getParentContainerSelected()
	{
		return this.parentContainerSelected;
	}
	/**
	 * @param parentContainerSelected parentContainerSelected
	 */
	public void setParentContainerSelected(String parentContainerSelected)
	{
		this.parentContainerSelected = parentContainerSelected;
	}

	/**
	 * @return Returns the pos1.
	 */
	public String getPos1()
	{
		return this.pos1;
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
		return this.pos2;
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
		return this.selectedContainerName;
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
		return this.stContSelection;
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
		return this.parentContainerId;
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
		return this.positionInParentContainer;
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
		return this.siteId;
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
		return this.siteName;
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
	@Override
	public int getFormId()
	{
		int formId;
		if (this.getNoOfContainers() > 1)
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
	@Override
	protected void reset()
	{

	}

	/**
	 * @return Returns the noOfContainers.
	 */
	public int getNoOfContainers()
	{
		return this.noOfContainers;
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
		return this.startNumber;
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
		return this.barcode;
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
		return this.key;
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
		return this.checkedButton;
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
		return this.isFull;
	}

	/**
	 * @param isFull The isFull to set.
	 */
	public void setIsFull(String isFull)
	{
		this.isFull = isFull;
	}

	/**
	 * Gets the Container Name.
	 * @return container Name
	 */
	public String getContainerName()
	{
		return this.containerName;
	}

	/**
	 * sets the name of the container.
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
		return this.positionDimensionOne;
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
		return this.positionDimensionTwo;
	}

	/**
	 * @param positionDimensionTwo The positionDimensionTwo to set.
	 */
	public void setPositionDimensionTwo(int positionDimensionTwo)
	{
		this.positionDimensionTwo = positionDimensionTwo;
	}

	/**
	 * collection Id's that this container can hold.
	 * @return collection Id's array
	 */
	public long[] getCollectionIds()
	{
		return this.collectionIds;
	}

	/**
	 * Setting the Collection Id array.
	 * @param collectionIds - array of collection Id's to set
	 */
	public void setCollectionIds(long[] collectionIds)
	{
		this.collectionIds = collectionIds;
	}

	/**
	 * getting Specimen class Type Id's that this container can hold.
	 * @return specimenClassType Id's array
	 */
	public String[] getHoldsSpecimenClassTypes()
	{
		return this.holdsSpecimenClassTypes;
	}

	/**
	 * Setting the SpecimenClassType Id array.
	 * @param holdsSpecimenClassTypes - array of SpecimenClassType Id's to set
	 */
	public void setHoldsSpecimenClassTypes(String[] holdsSpecimenClassTypes)
	{
		this.holdsSpecimenClassTypes = holdsSpecimenClassTypes;
	}

	/**
	 * StorageType Id's that this container can hold.
	 * @return StorageType Id' array
	 */
	public long[] getHoldsStorageTypeIds()
	{
		return this.holdsStorageTypeIds;
	}

	/**
	 * setting the StorageType Id array.
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
		return this.holdsSpecimenArrTypeIds;
	}

	/**
	 * Returns the map that contains distinguished fields per aliquots.
	 * @return The map that contains distinguished fields per aliquots.
	 * @see #setAliquotMap(Map)
	 */
	public Map getSimilarContainersMap()
	{
		return this.similarContainersMap;
	}

	/**
	 * Sets the map of distinguished fields of aliquots.
	 * @param similarContainersMap A map of distinguished fields of aliquots.
	 * @see #getAliquotMap()
	 */
	public void setSimilarContainersMap(Map similarContainersMap)
	{
		this.similarContainersMap = similarContainersMap;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is to be mapped.
	 */
	public void setSimilarContainerMapValue(String key, Object value)
	{
		this.similarContainersMap.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getSimilarContainerMapValue(String key)
	{
		return this.similarContainersMap.get(key);
	}

	/**
	 * This method sets Identifier of Objects inserted by AddNew
	 * activity in Form-Bean which initialized AddNew action.
	 * @param addNewFor - FormBean ID of the object inserted
	 *  @param addObjectIdentifier - Identifier of the Object inserted
	 */
	public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
	{
		if ("storageType".equals(addNewFor))
		{
			this.setTypeId(addObjectIdentifier.longValue());
		}
		else if ("site".equals(addNewFor))
		{
			this.setSiteId(addObjectIdentifier.longValue());
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
		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();

		try
		{
			logger.info("No of containers---------in validate::" + this.noOfContainers);
			if("Specimen".equals(this.specimenOrArrayType) && this.holdsSpecimenClassTypes==null)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("storageContainer.specimenClass")));
			}
			if (this.typeId == -1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("storageContainer.type")));
			}
			if (!validator.isValidOption(this.isFull) && this.noOfContainers == 1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
						ApplicationProperties.getValue("storageContainer.isContainerFull")));
			}

			if (this.parentContainerSelected.equals(Constants.SITE) && this.siteId == -1
					&& this.noOfContainers == 1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("storageContainer.site")));
			}
			else if ("Auto".equals(this.parentContainerSelected) && this.noOfContainers == 1)
			{
				if ("Auto".equals(this.parentContainerSelected))
				{
					if (!validator.isNumeric(String.valueOf(this.positionDimensionOne), 1)
							|| !validator.isNumeric(String.valueOf(this.positionDimensionTwo), 1)
							|| !validator.isNumeric(String.valueOf(this.parentContainerId), 1))
					{
						errors
								.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.item.format", ApplicationProperties
												.getValue("storageContainer.parentContainer")));
					}
				}
				else
				{
					this.checkPositionForParent(errors);
				}

			}
			else if (this.parentContainerSelected.equals("Manual") && this.noOfContainers >= 1)
			{

				this.checkPositionForParent(errors);
			}
			this.checkValidNumber(String.valueOf(this.noOfContainers),
					"storageContainer.noOfContainers", errors, validator);
			if (!edu.wustl.catissuecore.util.global.Variables.isStorageContainerLabelGeneratorAvl
					&& Validator.isEmpty(this.containerName) && this.noOfContainers == 1)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("storageContainer.name")));
			}
			if (this.collectionIds.length > 1)
			{
				for (final long collectionId : this.collectionIds)
				{
					if (collectionId == -1)
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
								ApplicationProperties
										.getValue("storageContainer.collectionProtocolTitle")));
					}
				}
			}
			this.checkValidSelectionForAny(this.holdsStorageTypeIds,
					"storageContainer.containerType", errors);
			if (this.getOperation().equals(Constants.EDIT)
					&& !validator.isValidOption(this.getActivityStatus()))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("site.activityStatus")));
			}
			if (!Validator.isEmpty(this.defaultTemperature)
					&& (!validator.isDouble(this.defaultTemperature, false)))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
						ApplicationProperties.getValue("storageContainer.temperature")));
			}
			if (Validator.isEmpty(String.valueOf(this.oneDimensionCapacity)))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("storageContainer.oneDimension")));
			}
			else
			{
				if (!validator.isNumeric(String.valueOf(this.oneDimensionCapacity)))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
							ApplicationProperties.getValue("storageContainer.oneDimension")));
				}
			}
			if (!Validator.isEmpty(String.valueOf(this.twoDimensionCapacity))
					&& (!validator.isNumeric(String.valueOf(this.twoDimensionCapacity))))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
						ApplicationProperties.getValue("storageContainer.twoDimension")));
			}

			if (this.noOfContainers > 1 && this.getSimilarContainersMap().size() > 0)
			{

				final String containerPrefixKey = "simCont:";

				for (int i = 1; i <= this.noOfContainers; i++)
				{
					final String iBarcode = (String) this.getSimilarContainerMapValue("simCont:"
							+ i + "_barcode"); //simCont:1_barcode
					if (iBarcode != null && iBarcode.equals("")) // this is done because barcode is empty string set by struts
					{ // but barcode in DB is unique but can be null.
						this.setSimilarContainerMapValue("simCont:" + i + "_barcode", null);
					}
					final int checkedButtonStatus = Integer.parseInt((String) this
							.getSimilarContainerMapValue("checkedButton"));
					final String containerName = (String) this
							.getSimilarContainerMapValue("simCont:" + i + "_name");
					if (!edu.wustl.catissuecore.util.global.Variables.isStorageContainerLabelGeneratorAvl
							&& Validator.isEmpty(containerName))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"errors.item.required", ApplicationProperties
										.getValue("storageContainer.name")));
					}
					final String siteId = (String) this.getSimilarContainerMapValue("simCont:" + i
							+ "_siteId");
					if (checkedButtonStatus == 2 || siteId == null)
					{

						final String radioButonKey = "radio_" + i;
						final String containerIdKey = containerPrefixKey + i + "_parentContainerId";
						//String containerNameKey = containerPrefixKey + i + "_StorageContainer_name";
						final String posDim1Key = containerPrefixKey + i + "_positionDimensionOne";
						final String posDim2Key = containerPrefixKey + i + "_positionDimensionTwo";

						if (((String) this.getSimilarContainerMapValue(radioButonKey)).equals("1"))
						{
							final String parentContId = (String) this
									.getSimilarContainerMapValue(containerIdKey);
							final String positionDimensionOne = (String) this
									.getSimilarContainerMapValue(posDim1Key);
							final String positionDimensionTwo = (String) this
									.getSimilarContainerMapValue(posDim2Key);

							if (parentContId.equals("-1") || positionDimensionOne.equals("-1")
									|| positionDimensionTwo.equals("-1"))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.item.required", ApplicationProperties
												.getValue("similarcontainers.location")));
								this.setSimilarContainerMapValue("checkedButton", "2");
							}
						}
						else
						{
							final String positionDimensionOne = (String) this
									.getSimilarContainerMapValue(posDim1Key + "_fromMap");
							final String positionDimensionTwo = (String) this
									.getSimilarContainerMapValue(posDim2Key + "_fromMap");

							if (positionDimensionOne != null
									&& !positionDimensionOne.trim().equals("")
									&& !validator.isDouble(positionDimensionOne)
									|| positionDimensionTwo != null
									&& !positionDimensionTwo.trim().equals("")
									&& !validator.isDouble(positionDimensionTwo))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.item.format", ApplicationProperties
												.getValue("specimen.positionInStorageContainer")));
								break;
							}
						}
					}
					else
					{
						if (siteId.equals("-1"))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"errors.item.required", ApplicationProperties
											.getValue("storageContainer.site")));
						}
					}
				}
			}
		}
		catch (final Exception excp)
		{
			StorageContainerForm.logger.error(excp.getMessage(), excp);
			excp.printStackTrace();
		}
		return errors;
	}

	/**
	 * @param errors ActionErrors
	 */
	private void checkPositionForParent(ActionErrors errors)
	{
		final boolean flag = StorageContainerUtil.checkPos1AndPos2(this.pos1, this.pos2);
		if (flag)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
					ApplicationProperties.getValue("storageContainer.parentContainer")));
		}

	}

	/**
	 * This function if 'any' option is selected then no other option should be selected.
	 * @param errors Action Errors
	 * @param Ids Array of long ids
	 * @param message Message used in ApplicationProperties
	 * */
	void checkValidSelectionForAny(long[] Ids, String message, ActionErrors errors)
	{
		if (Ids != null && Ids.length > 1)
		{
			for (final long id2 : Ids)
			{
				if (id2 == 1)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
							ApplicationProperties.getValue(message)));
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
		return this.siteForParentContainer;
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
		return this.specimenOrArrayType;
	}

	/**
	 * @param specimenOrArrayType Setting specimenOrArrayType
	 */
	public void setSpecimenOrArrayType(String specimenOrArrayType)
	{
		this.specimenOrArrayType = specimenOrArrayType;
	}
	/**
	 * @return nextForwardTo
	 */
	public String getNextForwardTo()
	{
		return this.nextForwardTo;
	}
	/**
	 * @param printerLocation Printer Location
	 */
	public void setPrinterLocation(String printerLocation)
	{
		this.printerLocation = printerLocation;
	}
	/**
	 * @return printerType
	 */
	public String getPrinterType()
	{
		return this.printerType;
	}
	/**
	 * @param printerType Printer Type
	 */
	public void setPrinterType(String printerType)
	{
		this.printerType = printerType;
	}

	/**
	 * @return isBarcodeEditable
	 */
	public String getIsBarcodeEditable()
	{
		return this.isBarcodeEditable;
	}

	/** 
	 * @param isBarcodeEditable Setter method for isBarcodeEditable.
	 */
	public void setIsBarcodeEditable(String isBarcodeEditable)
	{
		this.isBarcodeEditable = isBarcodeEditable;
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
	 * @param nextForwardTo Forward To
	 */
	public void setNextForwardTo(String nextForwardTo)
	{
		this.nextForwardTo = nextForwardTo;
	}
	/**
	 * @return printCheckbox
	 */
	public String getPrintCheckbox()
	{
		return this.printCheckbox;
	}
	/**
	 * @param printCheckbox Print Check box
	 */
	public void setPrintCheckbox(String printCheckbox)
	{
		this.printCheckbox = printCheckbox;
	}
	/**
	 * @return printerLocation
	 */
	public String getPrinterLocation()
	{
		return this.printerLocation;
	}
}