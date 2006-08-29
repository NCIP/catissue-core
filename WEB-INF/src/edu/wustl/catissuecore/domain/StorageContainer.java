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
import java.util.Map;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author gautam_shetty
 * @hibernate.joined-subclass table="CATISSUE_STORAGE_CONTAINER"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class StorageContainer extends Container
{
	protected Double tempratureInCentigrade;

	protected StorageType storageType;

	protected Site site;

	protected Collection collectionProtocolCollection = new HashSet();

	protected Collection holdsStorageTypeCollection = new HashSet();

	protected Collection holdsSpecimenClassCollection = new HashSet();

	/**
	 * Number of containers
	 */
	protected transient Integer startNo;

	/**
	 * Number of containers
	 */
	protected transient Integer noOfContainers;

	protected transient Map similarContainerMap;

	protected transient boolean isParentChanged = false;

	// -------- To check for changed position in the same container.
	private boolean positionChanged = false;

	public StorageContainer()
	{
	}

	public StorageContainer(StorageContainer oldContainer)
	{
		this.setId(oldContainer.getId());
		this.setActivityStatus(oldContainer.getActivityStatus());
		this.setParent(oldContainer.getParent());
		//this.setNumber(oldContainer.getNumber());
		this.setName(oldContainer.getName());
		this.setFull(oldContainer.isFull());
		if (parent != null)
		{
			parent.getChildren().add(this);
		}
		this.setSite(oldContainer.getSite());
		capacity = new Capacity(oldContainer.getCapacity());

		this.setStorageType(oldContainer.getStorageType());
		this.setTempratureInCentigrade(oldContainer.getTempratureInCentigrade());
		this.setCollectionProtocolCollection(oldContainer.getCollectionProtocolCollection());
		this.setHoldsStorageTypeCollection(oldContainer.getHoldsStorageTypeCollection());
		this.setHoldsSpecimenClassCollection(oldContainer.getHoldsSpecimenClassCollection());
	}

	public StorageContainer(AbstractActionForm abstractActionForm) throws AssignDataException
	{
		setAllValues(abstractActionForm);
	}

	/**
	 * @return Returns the collectionProtocolCollection.
	 * @hibernate.set name="collectionProtocolCollection" table="CATISSUE_STORAGE_CONT_COLL_PROT_REL" 
	 * cascade="none" inverse="false" lazy="false"
	 * @hibernate.collection-key column="STORAGE_CONTAINER_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.CollectionProtocol" column="COLLECTION_PROTOCOL_ID"
	 */
	public Collection getCollectionProtocolCollection()
	{
		return collectionProtocolCollection;
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
	public Collection getHoldsSpecimenClassCollection()
	{
		return holdsSpecimenClassCollection;
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
		return isParentChanged;
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
		return noOfContainers;
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
		return positionChanged;
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
		return similarContainerMap;
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
		return startNo;
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
	 * @hibernate.set name="holdsStorageTypeCollection" table="CATISSUE_STOR_CONT_STOR_TYPE_REL" 
	 * cascade="none" inverse="false" lazy="false"
	 * @hibernate.collection-key column="STORAGE_CONTAINER_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.StorageType" column="STORAGE_TYPE_ID"
	 */
	public Collection getHoldsStorageTypeCollection()
	{
		return holdsStorageTypeCollection;
	}

	/**
	 * @param holdsStorageTypeCollection The holdsStorageTypeCollection to set.
	 */
	public void setHoldsStorageTypeCollection(Collection holdsStorageTypeCollection)
	{
		this.holdsStorageTypeCollection = holdsStorageTypeCollection;
	}

	/**
	 * @return Returns the site.
	 * @hibernate.many-to-one column="SITE_ID" class="edu.wustl.catissuecore.domain.Site"
	 * constrained="true"
	 */
	public Site getSite()
	{
		return site;
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
		return tempratureInCentigrade;
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
		return storageType;
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
	 * @param storageTypeForm A StorageTypeForm object containing the information about the StorageType.  
	 * */
	public void setAllValues(AbstractActionForm abstractForm)
	{
		try
		{
			StorageContainerForm form = (StorageContainerForm) abstractForm;
			this.name = form.getContainerName();
			this.noOfContainers = new Integer(form.getNoOfContainers());
			if (Utility.toString(form.getDefaultTemperature()).trim().length() > 0)
			{
				this.tempratureInCentigrade = new Double(form.getDefaultTemperature());
			}
			if (this.noOfContainers.intValue() == 1)
			{
				if (!form.getBarcode().trim().equals(""))
					this.barcode = form.getBarcode();
				else
					this.barcode = null;
			}
			this.full = new Boolean(form.getIsFull());
			Logger.out.debug("SC Domain : " + this.full + " :-: form.getIsFull() : "
					+ form.getIsFull());
			this.activityStatus = form.getActivityStatus();
			this.noOfContainers = new Integer(form.getNoOfContainers());

			storageType = new StorageType();
			storageType.setId(new Long(form.getTypeId()));
			storageType.setOneDimensionLabel(form.getOneDimensionLabel());
			storageType.setTwoDimensionLabel(form.getTwoDimensionLabel());
			capacity.setOneDimensionCapacity(new Integer(form.getOneDimensionCapacity()));
			capacity.setTwoDimensionCapacity(new Integer(form.getTwoDimensionCapacity()));

			//in case of edit
			if (!form.isAddOperation())
			{
				//Previously Container was in a site
				if (parent == null)
				{
					//Put this in another container
					if (form.getParentContainerId() > 0)
					{
						isParentChanged = true;
					}
				}
				else
				//in another container
				{
					if (parent.getId().longValue() != form.getParentContainerId())
					{
						isParentChanged = true;
					}
					// code to check if the position of the container is changed
					else
					{
						if (positionDimensionOne.intValue() != form.getPositionDimensionOne()
								|| positionDimensionTwo.intValue() != form
										.getPositionDimensionOne())
						{
							positionChanged = true;
						}
					}
				}
			}

			Logger.out.debug("isParentChanged " + isParentChanged);
			if (form.getCheckedButton() == Constants.CONTAINER_IN_ANOTHER_CONTAINER)
			{
				parent = new StorageContainer();
				parent.setId(new Long(form.getParentContainerId()));
				this.setPositionDimensionOne(new Integer(form.getPositionDimensionOne()));
				this.setPositionDimensionTwo(new Integer(form.getPositionDimensionTwo()));
			}
			else
			{
				parent = null;
				this.setPositionDimensionOne(null);
				this.setPositionDimensionTwo(null);
				site = new Site();
				site.setId(new Long(form.getSiteId()));
			}

			collectionProtocolCollection.clear();
			long[] collectionProtocolArr = form.getCollectionIds();
			if (collectionProtocolArr != null)
			{
				for (int i = 0; i < collectionProtocolArr.length; i++)
				{
					Logger.out.debug("Collection prtocoo Id :" + collectionProtocolArr[i]);
					if (collectionProtocolArr[i] != -1)
					{
						CollectionProtocol collectionProtocol = new CollectionProtocol();
						collectionProtocol.setId(new Long(collectionProtocolArr[i]));
						collectionProtocolCollection.add(collectionProtocol);
					}
				}
			}
			holdsStorageTypeCollection.clear();
			long[] storageTypeArr = form.getHoldsStorageTypeIds();
			if (storageTypeArr != null)
			{
				for (int i = 0; i < storageTypeArr.length; i++)
				{
					Logger.out.debug("Storage Type Id :" + storageTypeArr[i]);
					if (storageTypeArr[i] != -1)
					{
						StorageType storageType = new StorageType();
						storageType.setId(new Long(storageTypeArr[i]));
						holdsStorageTypeCollection.add(storageType);
					}
				}
			}

			holdsSpecimenClassCollection.clear();
			String[] specimenClassArr = form.getHoldsSpecimenClassTypes();
			if (specimenClassArr != null)
			{
				for (int i = 0; i < specimenClassArr.length; i++)
				{
					Logger.out.debug("Specimen class Id :" + specimenClassArr[i]);
					//if(!specimenClassArr[i].equals("-1"))
					//{
					//TODO : Vaishali
					//		        		SpecimenClass specimenClass = new SpecimenClass();
					//		        		specimenClass.setId(new Long(specimenClassArr[i]));
					//		        		holdsSpecimenClassCollection.add(specimenClass);
					//String specimenClassType=specimenClassArr[i];
					holdsSpecimenClassCollection.add(specimenClassArr[i]);
					//}
				}
			}
			if (this.noOfContainers.intValue() > 1)
			{
				Logger.out.info("--------------------------:" + form.getSimilarContainersMap());
				this.similarContainerMap = form.getSimilarContainersMap();
			}

		}
		catch (Exception excp)
		{
			Logger.out.error(excp.getMessage(), excp);
		}
	}
}