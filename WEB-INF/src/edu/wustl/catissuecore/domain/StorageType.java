/**
 * <p>Title: StorageType Class
 * <p>Description:  Type of the storage container e.g. Freezer, Box etc. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;
import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author gautam_shetty
 * @hibernate.joined-subclass table="CATISSUE_STORAGE_TYPE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class StorageType extends ContainerType
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(StorageType.class);
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = -1398514908633751482L;

	/**
	 * defaultTempratureInCentigrade.
	 */
	protected Double defaultTempratureInCentigrade;

	/**
	 * holdsStorageTypeCollection.
	 */
	protected Collection holdsStorageTypeCollection = new HashSet();

	/**
	 * HashSet holding SpecimenClassCollection.
	 */
	protected Collection holdsSpecimenClassCollection = new HashSet();

	/**
	 * HashSet holding SpecimenArrayTypeCollection.
	 */
	protected Collection holdsSpecimenArrayTypeCollection = new HashSet();

	/**
	 * Default Constructor.
	 */
	public StorageType()
	{
		super();
		// Default Constructor, required for Hibernate
	}

	/**
	 * Parameterized Constructor.
	 * @param abstractActionForm of AbstractActionForm type.
	 * @throws AssignDataException AssignDataException.
	 */
	public StorageType(AbstractActionForm abstractActionForm) throws AssignDataException
	{
		super();
		setAllValues((IValueObject) abstractActionForm);
	}

	/**
	 * @return Returns the defaultTempratureInCentigrade.
	 * @hibernate.property name="defaultTempratureInCentigrade" type="double"
	 * column="DEFAULT_TEMPERATURE" length="30"
	 */
	public Double getDefaultTempratureInCentigrade()
	{
		return defaultTempratureInCentigrade;
	}

	/**
	 * @param defaultTempratureInCentigrade The defaultTempratureInCentigrade to set.
	 */
	public void setDefaultTempratureInCentigrade(Double defaultTempratureInCentigrade)
	{
		this.defaultTempratureInCentigrade = defaultTempratureInCentigrade;
	}

	/**
	 * @return Returns the holdsSpecimenClassCollection.
	 * @hibernate.set name="holdsSpecimenClassCollection" table="CATISSUE_STOR_TYPE_SPEC_CLASS"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="STORAGE_TYPE_ID"
	 * @hibernate.element type="string" column="SPECIMEN_CLASS" length="30"
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
	 * @return Returns the holdsStorageTypeCollection.
	 * Returns the collection of storage types associated with type
	 * @hibernate.set name="holdsStorageTypeCollection" table="CATISSUE_STOR_TYPE_HOLDS_TYPE"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="STORAGE_TYPE_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.StorageType"
	 * column="HOLDS_STORAGE_TYPE_ID"
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
	 * @return Returns the holdsSpecimenArrayTypeCollection.
	 * Returns the collection of specimen array types associated with type
	 * @hibernate.set name="holdsSpecimenArrayTypeCollection" table="CATISSUE_STORTY_HOLDS_SPARRTY"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="STORAGE_TYPE_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.SpecimenArrayType"
	 * column="SPECIMEN_ARRAY_TYPE_ID"
	 */
	public Collection getHoldsSpecimenArrayTypeCollection()
	{
		return holdsSpecimenArrayTypeCollection;
	}

	/**
	 * @param holdsSpecimenArrayTypeCollection The holdsSpecimenArrayTypeCollection to set.
	 */
	public void setHoldsSpecimenArrayTypeCollection(Collection holdsSpecimenArrayTypeCollection)
	{
		this.holdsSpecimenArrayTypeCollection = holdsSpecimenArrayTypeCollection;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.ContainerType#
	 * setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
	 */
	/**
	 * Set all values.
	 * @throws AssignDataException AssignDataException.
	 * @param abstractForm of IValueObject type.
	 */
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			final StorageTypeForm storageTypeForm = (StorageTypeForm) abstractForm;

			this.id = Long.valueOf(storageTypeForm.getId());
			this.name = storageTypeForm.getType().trim();

			if (storageTypeForm.getDefaultTemperature() != null &&
					storageTypeForm.getDefaultTemperature().trim().length() > 0)
			{
				this.defaultTempratureInCentigrade = new Double(
						storageTypeForm.getDefaultTemperature());
			}

			this.oneDimensionLabel = storageTypeForm.getOneDimensionLabel();
			this.twoDimensionLabel = storageTypeForm.getTwoDimensionLabel();

			if (SearchUtil.isNullobject(capacity))
			{
				capacity = new Capacity();
			}

			capacity.setOneDimensionCapacity(Integer.valueOf(
					storageTypeForm.getOneDimensionCapacity()));
			capacity.setTwoDimensionCapacity(Integer.valueOf(
					storageTypeForm.getTwoDimensionCapacity()));

			//holdsStorageTypeCollection.clear();
			holdsStorageTypeCollection = new HashSet();
			long[] storageTypeArr = storageTypeForm.getHoldsStorageTypeIds();
			if (storageTypeArr != null)
			{
				for (int i = 0; i < storageTypeArr.length; i++)
				{
					logger.debug("type Id :" + storageTypeArr[i]);
					if (storageTypeArr[i] != -1)
					{
						StorageType storageType = new StorageType();
						storageType.setId(Long.valueOf(storageTypeArr[i]));
						holdsStorageTypeCollection.add(storageType);
					}
				}
			}

			//holdsSpecimenClassCollection.clear();
			holdsSpecimenClassCollection = new HashSet();
			if (storageTypeForm.getSpecimenOrArrayType().equals("Specimen"))
			{
				String[] specimenClassTypeArr = storageTypeForm.getHoldsSpecimenClassTypes();
				if (specimenClassTypeArr != null)
				{

					for (int i = 0; i < specimenClassTypeArr.length; i++)
					{
						logger.debug("type Id :" + specimenClassTypeArr[i]);
						if (specimenClassTypeArr[i].equals("-1"))
						{
							holdsSpecimenClassCollection.addAll(
									AppUtility.getSpecimenClassTypes());
							break;
						}
						else
						{
							holdsSpecimenClassCollection.add(specimenClassTypeArr[i]);
						}
					}
				}
			}
			//			holdsSpArrayTypeCollection.clear();
			holdsSpecimenArrayTypeCollection = new HashSet();
			populateHoldsSpecimenArrayTypeCollection(storageTypeForm);
			this.activityStatus = "Active";
		}
		catch (Exception excp)
		{
			logger.error(excp.getMessage());
		}
	}

	/**
	 * @param storageTypeForm of StorageTypeForm type.
	 */
	private void populateHoldsSpecimenArrayTypeCollection(final StorageTypeForm storageTypeForm)
	{
		if (storageTypeForm.getSpecimenOrArrayType().equals("SpecimenArray"))
		{
			long[] specimenArrayTypeArr = storageTypeForm.getHoldsSpecimenArrTypeIds();
			if (specimenArrayTypeArr != null)
			{
				for (int i = 0; i < specimenArrayTypeArr.length; i++)
				{
					logger.debug("specimen array type Id :" + specimenArrayTypeArr[i]);
					if (specimenArrayTypeArr[i] != -1)
					{
						SpecimenArrayType spArrayType = new SpecimenArrayType();
						spArrayType.setId(Long.valueOf(specimenArrayTypeArr[i]));
						holdsSpecimenArrayTypeCollection.add(spArrayType);
					}
				}
			}
		}
	}

	/**
	 * @param object Object.
	 * @return Boolean.
	 */
	public boolean equals(Object object)
	{
		boolean equals = false;
		if ((object != null) && object instanceof StorageType)
		{
			StorageType storageType = (StorageType) object;
			if (this.getId().longValue() == storageType.getId().longValue())
			{
				equals = true;
			}
		}
		return equals;
	}

	/**
	 * @return integer.
	 */
	public int hashCode()
	{
		int hashCode = super.hashCode();

		if (this.getId() != null)
		{
			hashCode = this.getId().hashCode();
		}

		return hashCode;
	}
}