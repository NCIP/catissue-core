/**
 * <p>Title: StorageContainer Class>
 * <p>Description:  A class that models storage container's information. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;

/**
 * @author gautam_shetty
 * @hibernate.joined-subclass table="CATISSUE_STORAGE_CONTAINER"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class StorageContainer extends Container implements IActivityStatus, ISpecimenTypeDomain
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(StorageContainer.class);
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 7589264838581930051L;

	/**
	 * specimenPositionCollection.
	 */
	private Collection<SpecimenPosition> specimenPositionCollection;

	/**
	 * tempratureInCentigrade.
	 */
	private Double tempratureInCentigrade;

	/**
	 * storageType.
	 */
	private StorageType storageType;

	/**
	 * site.
	 */
	private Site site;

	/**
	 * HashSet containing collectionProtocol.
	 */
	private Collection<CollectionProtocol> collectionProtocolCollection = new HashSet<CollectionProtocol>();

	/**
	 * HashSet containing StorageType.
	 */
	private Collection<StorageType> holdsStorageTypeCollection = new HashSet<StorageType>();

	/**
	 * HashSet containing String.
	 */
	private Collection<String> holdsSpecimenClassCollection = new HashSet<String>();
	/**
	 * HashSet containing String.
	 */
	private Collection<String> holdsSpecimenTypeCollection = new HashSet<String>();

	/**
	 * @return holdsSpecimenTypeCollection
	 */
	public Collection<String> getHoldsSpecimenTypeCollection()
	{
		return holdsSpecimenTypeCollection;
	}

	/**
	 * Set holdsSpecimenTypeCollection.
	 * @param holdsSpecimenTypeCollection holdsSpecimenTypeCollection
	 */
	public void setHoldsSpecimenTypeCollection(Collection<String> holdsSpecimenTypeCollection)
	{
		this.holdsSpecimenTypeCollection = holdsSpecimenTypeCollection;
	}

	/**
	 * HashSet containing String.
	 */
	private Collection holdsSpecimenArrayTypeCollection = new HashSet();

	/**
	 * startNo of containers.
	 */
	private transient Integer startNo;

	/**
	 * Number of containers.
	 */
	private transient Integer noOfContainers;

	/**
	 * Map of similarContainerMap.
	 */
	private transient Map similarContainerMap;

	/**
	 * Boolean isParentChanged.
	 */
	private transient boolean isParentChanged = false;

	/**
	 * positionChanged.
	 */
	private boolean positionChanged = false;

	/**
	 * Default Constructor.
	 */
	public StorageContainer()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param oldContainer of StorageContainer type.
	 */
	public StorageContainer(StorageContainer oldContainer)
	{
		super();
		this.setId(oldContainer.getId());
		this.setActivityStatus(oldContainer.getActivityStatus());
		this.setName(oldContainer.getName());
		this.setBarcode(oldContainer.barcode);
		if (oldContainer.getLocatedAtPosition() != null)
		{
			if (this.locatedAtPosition == null)
			{
				this.locatedAtPosition = new ContainerPosition();
			}
			this.locatedAtPosition.setParentContainer(oldContainer.getLocatedAtPosition()
					.getParentContainer());
			this.locatedAtPosition.setPositionDimensionOne(oldContainer.getLocatedAtPosition()
					.getPositionDimensionOne());
			this.locatedAtPosition.setPositionDimensionTwo(oldContainer.getLocatedAtPosition()
					.getPositionDimensionTwo());
			this.locatedAtPosition.occupiedContainer = this;
		}
		this.setFull(oldContainer.getFull());
		this.setSite(oldContainer.getSite());
		this.capacity = new Capacity(oldContainer.getCapacity());

		this.setStorageType(oldContainer.getStorageType());
		this.setTempratureInCentigrade(oldContainer.getTempratureInCentigrade());
		this.setCollectionProtocolCollection(oldContainer.getCollectionProtocolCollection());
		this.setHoldsStorageTypeCollection(oldContainer.getHoldsStorageTypeCollection());
		this.setHoldsSpecimenClassCollection(oldContainer.getHoldsSpecimenClassCollection());
		this.setHoldsSpecimenTypeCollection(oldContainer.getHoldsSpecimenTypeCollection());
		this
				.setHoldsSpecimenArrayTypeCollection(oldContainer
						.getHoldsSpecimenArrayTypeCollection());
	}

	/**
	 * Parameterized Constructor.
	 * @param abstractActionForm of AbstractActionForm type.
	 * @throws AssignDataException AssignDataException.
	 */
	public StorageContainer(AbstractActionForm abstractActionForm) throws AssignDataException
	{
		super();
		this.setAllValues(abstractActionForm);
	}

	/**
	 * @return Returns the collectionProtocolCollection.
	 * @hibernate.set name="collectionProtocolCollection" table="CATISSUE_ST_CONT_COLL_PROT_REL"
	 * cascade="none" inverse="false" lazy="false"
	 * @hibernate.collection-key column="STORAGE_CONTAINER_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.CollectionProtocol"
	 * column="COLLECTION_PROTOCOL_ID"
	 */
	public Collection<CollectionProtocol> getCollectionProtocolCollection()
	{
		return this.collectionProtocolCollection;
	}

	/**
	 * @param collectionProtocolCollection The collectionProtocolCollection to set.
	 */
	public void setCollectionProtocolCollection(Collection collectionProtocolCollection)
	{
		this.collectionProtocolCollection = collectionProtocolCollection;
	}

	/**
	 * @return Returns the holdsSpecimenClassCollection.
	 * @hibernate.set name="holdsSpecimenClassCollection" table="CATISSUE_STOR_CONT_SPEC_CLASS"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="STORAGE_CONTAINER_ID"
	 * @hibernate.element type="string" column="NAME" length="30"
	 */
	public Collection<String> getHoldsSpecimenClassCollection()
	{
		return this.holdsSpecimenClassCollection;
	}

	/**
	 * @param holdsSpecimenClassCollection The holdsSpecimenClassCollection to set.
	 */
	public void setHoldsSpecimenClassCollection(Collection holdsSpecimenClassCollection)
	{
		this.holdsSpecimenClassCollection = holdsSpecimenClassCollection;
	}

	/**
	 * @return Returns the isParentChanged.
	 */
	public boolean isParentChanged()
	{
		return this.isParentChanged;
	}

	/**
	 * @param isParentChanged The isParentChanged to set.
	 */
	public void setParentChanged(boolean isParentChanged)
	{
		this.isParentChanged = isParentChanged;
	}

	/**
	 * @return Returns the noOfContainers.
	 */
	public Integer getNoOfContainers()
	{
		return this.noOfContainers;
	}

	/**
	 * @param noOfContainers The noOfContainers to set.
	 */
	public void setNoOfContainers(Integer noOfContainers)
	{
		this.noOfContainers = noOfContainers;
	}

	/**
	 * @return Returns the positionChanged.
	 */
	public boolean isPositionChanged()
	{
		return this.positionChanged;
	}

	/**
	 * @param positionChanged The positionChanged to set.
	 */
	public void setPositionChanged(boolean positionChanged)
	{
		this.positionChanged = positionChanged;
	}

	/**
	 * @return Returns the similarContainerMap.
	 */
	public Map getSimilarContainerMap()
	{
		return this.similarContainerMap;
	}

	/**
	 * @param similarContainerMap The similarContainerMap to set.
	 */
	public void setSimilarContainerMap(Map similarContainerMap)
	{
		this.similarContainerMap = similarContainerMap;
	}

	/**
	 * @return Returns the startNo.
	 */
	public Integer getStartNo()
	{
		return this.startNo;
	}

	/**
	 * @param startNo The startNo to set.
	 */
	public void setStartNo(Integer startNo)
	{
		this.startNo = startNo;
	}

	/**
	 * @return Returns the holdsStorageTypeCollection.
	 * @hibernate.set name="holdsStorageTypeCollection" table="CATISSUE_ST_CONT_ST_TYPE_REL"
	 * cascade="none" inverse="false" lazy="false"
	 * @hibernate.collection-key column="STORAGE_CONTAINER_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.StorageType"
	 * column="STORAGE_TYPE_ID"
	 */
	public Collection getHoldsStorageTypeCollection()
	{
		return this.holdsStorageTypeCollection;
	}

	/**
	 * @param holdsStorageTypeCollection The holdsStorageTypeCollection to set.
	 */
	public void setHoldsStorageTypeCollection(Collection holdsStorageTypeCollection)
	{
		this.holdsStorageTypeCollection = holdsStorageTypeCollection;
	}

	/**
	 * @return Returns the holdsSpArrayTypeCollection.
	 * Returns the collection of specimen array types associated with container
	 * @hibernate.set name="holdsSpecimenArrayTypeCollection" table="CATISSUE_CONT_HOLDS_SPARRTYPE"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="STORAGE_CONTAINER_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.SpecimenArrayType"
	 * column="SPECIMEN_ARRAY_TYPE_ID"
	 */
	public Collection getHoldsSpecimenArrayTypeCollection()
	{
		return this.holdsSpecimenArrayTypeCollection;
	}

	/**
	 * @param holdsSpecimenArrayTypeCollection The holdsSpecimenArrayTypeCollection to set.
	 */
	public void setHoldsSpecimenArrayTypeCollection(Collection holdsSpecimenArrayTypeCollection)
	{
		this.holdsSpecimenArrayTypeCollection = holdsSpecimenArrayTypeCollection;
	}

	/**
	 * @return Returns the site.
	 * @hibernate.many-to-one column="SITE_ID" class="edu.wustl.catissuecore.domain.Site"
	 * constrained="true"
	 */
	public Site getSite()
	{
		return this.site;
	}

	/**
	 * @param site The site to set.
	 */
	public void setSite(Site site)
	{
		this.site = site;
	}

	/**
	 * @return Returns the tempratureInCentigrade.
	 * @hibernate.property name="tempratureInCentigrade" type="double"
	 * column="TEMPERATURE" length="30"
	 */
	public Double getTempratureInCentigrade()
	{
		return this.tempratureInCentigrade;
	}

	/**
	 * @param tempratureInCentigrade The tempratureInCentigrade to set.
	 */
	public void setTempratureInCentigrade(Double tempratureInCentigrade)
	{
		this.tempratureInCentigrade = tempratureInCentigrade;
	}

	/**
	 * @return Returns the storageType.
	 * @hibernate.many-to-one column="STORAGE_TYPE_ID" class="edu.wustl.catissuecore.domain.StorageType"
	 */
	public StorageType getStorageType()
	{
		return this.storageType;
	}

	/**
	 * @param storageType The storageType to set.
	 */
	public void setStorageType(StorageType storageType)
	{
		this.storageType = storageType;
	}

	/**
	 * This function Copies the data from a StorageTypeForm object to a StorageType object.
	 * @param abstractForm - storageTypeForm A StorageTypeForm object containing the
	 * information about the StorageType.
	 * @throws AssignDataException : AssignDataException
	 */
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
			{
			final StorageContainerForm form = (StorageContainerForm) abstractForm;
			this.name = form.getContainerName();
			this.noOfContainers = new Integer(form.getNoOfContainers());
			if (CommonUtilities.toString(form.getDefaultTemperature()).trim().length() > 0)
			{
				this.tempratureInCentigrade = new Double(form.getDefaultTemperature());
			}
			if (form.getBarcode() != null && form.getBarcode().equals(""))
			{
				this.barcode = null;
			}
			else
			{
				this.barcode = form.getBarcode();
			}
			this.full = new Boolean(form.getIsFull());
			this.activityStatus = form.getActivityStatus();
			this.noOfContainers = new Integer(form.getNoOfContainers());
			this.storageType = new StorageType();
			this.storageType.setId(new Long(form.getTypeId()));
			this.storageType.setOneDimensionLabel(form.getOneDimensionLabel());
			this.storageType.setTwoDimensionLabel(form.getTwoDimensionLabel());
			this.storageType.setName(form.getTypeName());
			if (SearchUtil.isNullobject(this.capacity))
			{
				this.capacity = new Capacity();
			}
			this.capacity.setOneDimensionCapacity(new Integer(form.getOneDimensionCapacity()));
			this.capacity.setTwoDimensionCapacity(new Integer(form.getTwoDimensionCapacity()));
			if (!form.isAddOperation())
			{
				//Previously Container was in a site
				if (this.locatedAtPosition != null
						&& this.locatedAtPosition.getParentContainer() == null)
				{
					if (form.getStContSelection() == 1)
					{
						//Put this in another container
						if (form.getParentContainerId() > 0)
						{
							this.isParentChanged = true;
						}
					}
					else
					{
						if (!form.getContainerId().equals("0"))
						{
							this.isParentChanged = true;
						}
					}
				}
				else
				//in another container
				{
					if (form.getStContSelection() == 1)
					{
						if (this.locatedAtPosition != null
								&& this.locatedAtPosition.getParentContainer() != null
								&& this.locatedAtPosition.parentContainer.getId().longValue() != form
										.getParentContainerId())
						{
							this.isParentChanged = true;
						}
					}
					else if (this.locatedAtPosition != null
							&& this.locatedAtPosition.parentContainer != null
							&& this.locatedAtPosition.parentContainer.getId().equals(
									form.getContainerId()))
					{
						this.isParentChanged = true;
					}
	
					// code to check if the position of the container is changed
	
					if (form.getStContSelection() == 1)
					{
						if ((this.locatedAtPosition != null && this.locatedAtPosition.positionDimensionOne
								.intValue() != form.getPositionDimensionOne())
								|| (this.locatedAtPosition != null && this.locatedAtPosition.positionDimensionTwo
										.intValue() != form.getPositionDimensionOne()))
						{
							this.positionChanged = true;
						}
					}
					else
					{
						if (this.locatedAtPosition != null
								&& this.locatedAtPosition.positionDimensionOne.equals(form.getPos1())
								|| this.locatedAtPosition.positionDimensionTwo.equals(form.getPos2()))
						{
							this.positionChanged = true;
						}
					}
	
				}
			}
	
			logger.debug("isParentChanged " + this.isParentChanged);
			if (!Constants.SITE.equals(form.getParentContainerSelected()))
			{
				if (this.locatedAtPosition == null)
				{
					this.locatedAtPosition = new ContainerPosition();
				}
				if ("Auto".equals(form.getParentContainerSelected()))
				{
	
					this.locatedAtPosition.parentContainer = new StorageContainer();
					this.locatedAtPosition.parentContainer.setId(new Long(form.getParentContainerId()));
	
					this.locatedAtPosition.setPositionDimensionOne(new Integer(form
							.getPositionDimensionOne()));
					this.locatedAtPosition.setPositionDimensionTwo(new Integer(form
							.getPositionDimensionTwo()));
					this.locatedAtPosition.occupiedContainer = this;
				}
				else
				{
					this.locatedAtPosition.parentContainer = new StorageContainer();
					if (form.getContainerId() != null && !form.getContainerId().trim().equals(""))
					{
						this.locatedAtPosition.parentContainer.setId(new Long(form.getContainerId()));
					}
					else
					{
						this.locatedAtPosition.parentContainer.setName(form.getSelectedContainerName());
					}
					if (form.getPos1() != null && !form.getPos1().trim().equals("")
							&& form.getPos2() != null && !form.getPos2().trim().equals(""))
					{
						if (this.locatedAtPosition == null)
						{
							this.locatedAtPosition = new ContainerPosition();
						}
						this.locatedAtPosition.setPositionDimensionOne(new Integer(form.getPos1()
								.trim()));
						this.locatedAtPosition.setPositionDimensionTwo(new Integer(form.getPos2()
								.trim()));
						this.locatedAtPosition.occupiedContainer = this;
					}
				}
			}
			else
			{
				this.locatedAtPosition = null;
				this.site = new Site();
				this.site.setId(new Long(form.getSiteId()));
				this.site.setName(form.getSiteName());
			}
			this.collectionProtocolCollection = new HashSet();
			final long[] collectionProtocolArr = form.getCollectionIds();
			if (collectionProtocolArr != null)
			{
				for (final long element : collectionProtocolArr)
				{
					if (element != -1)
					{
						final CollectionProtocol collectionProtocol = new CollectionProtocol();
						collectionProtocol.setId(new Long(element));
						this.collectionProtocolCollection.add(collectionProtocol);
					}
				}
			}
			this.holdsStorageTypeCollection = new HashSet();
			final long[] storageTypeArr = form.getHoldsStorageTypeIds();
			if (storageTypeArr != null)
			{
				for (final long element : storageTypeArr)
				{
					logger.debug("Storage Type Id :" + element);
					if (element != -1)
					{
						final StorageType storageType = new StorageType();
						storageType.setId(new Long(element));
						this.holdsStorageTypeCollection.add(storageType);
					}
				}
			}
			setClassColl(form);
			setSpTypeColl(form);
			//		holdsSpArrayTypeCollection.clear();
			this.holdsSpecimenArrayTypeCollection = new HashSet();
			if (form.getSpecimenOrArrayType().equals("SpecimenArray"))
			{
				final long[] specimenArrayTypeArr = form.getHoldsSpecimenArrTypeIds();
				if (specimenArrayTypeArr != null)
				{
					for (final long element : specimenArrayTypeArr)
					{
						Logger.out.debug("specimen array type Id :" + element);
						if (element != -1)
						{
							final SpecimenArrayType spArrayType = new SpecimenArrayType();
							spArrayType.setId(new Long(element));
							this.holdsSpecimenArrayTypeCollection.add(spArrayType);
						}
					}
				}
			}
			if (this.noOfContainers.intValue() > 1)
			{
				Logger.out.info("--------------------------:" + form.getSimilarContainersMap());
				this.similarContainerMap = form.getSimilarContainersMap();
			}
		}
		catch (final Exception excp)
		{
			StorageContainer.logger.error(excp.getMessage(),excp);
			excp.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "StorageContainer.java :");
		}

	}
	/**
	 * @param form StorageContainerForm
	 * @throws ApplicationException ApplicationException
	 */
	private void setSpTypeColl(final StorageContainerForm form) throws ApplicationException
	{
		this.holdsSpecimenTypeCollection = new HashSet<String>();
		if (form.getSpecimenOrArrayType().equals("Specimen"))
		{
			StorageContainerUtil.setSpTypeColl(form, this);
		}
	}
	/**
	 * @param form StorageContainerForm
	 */
	private void setClassColl(final StorageContainerForm form)
	{
		this.holdsSpecimenClassCollection = new HashSet();
		if (form.getSpecimenOrArrayType().equals("Specimen"))
		{
			final String[] specimenClassArr = form.getHoldsSpecimenClassTypes();
			if (specimenClassArr != null)
			{
				for (final String element : specimenClassArr)
				{
					logger.debug("type Id :" + element);
					if (element.equals("-1"))
					{
						this.holdsSpecimenClassCollection
								.addAll(AppUtility.getSpecimenClassTypes());
						break;
					}
					else
					{
						this.holdsSpecimenClassCollection.add(element);
					}
				}
			}
		}
	}

	/**
	 * @return the specimenPositionCollection
	 */
	public Collection<SpecimenPosition> getSpecimenPositionCollection()
	{
		return this.specimenPositionCollection;
	}

	/**
	 * @param specimenPositionCollection the specimenPositionCollection to set
	 */
	public void setSpecimenPositionCollection(
			Collection<SpecimenPosition> specimenPositionCollection)
	{
		this.specimenPositionCollection = specimenPositionCollection;
	}

	//	public Collection getSpecimenCollection() {
	//		return specimenCollection;
	//	}
	//
	//	public void setSpecimenCollection(Collection specimenCollection) {
	//		this.specimenCollection = specimenCollection;
	//	}
}