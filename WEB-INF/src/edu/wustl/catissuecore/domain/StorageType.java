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
import edu.wustl.common.exception.ErrorKey;
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
	private static Logger logger = Logger.getCommonLogger(StorageType.class);
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
	protected Collection<StorageType> holdsStorageTypeCollection = new HashSet<StorageType>();

	/**
	 * HashSet holding SpecimenClassCollection.
	 */
	protected Collection<String> holdsSpecimenClassCollection = new HashSet<String>();
	/**
	 * HashSet holding SpecimenClassCollection.
	 */
	protected Collection<String> holdsSpecimenTypeCollection = new HashSet<String>();

	/**
	 * HashSet holding SpecimenArrayTypeCollection.
	 */
	protected Collection<SpecimenArrayType> holdsSpecimenArrayTypeCollection = new HashSet<SpecimenArrayType>();

	/**
	 * Default Constructor.
	 */
	public StorageType()
	{
		super();
	}

	/**
	 * Returns SpecimenType Collection.
	 * @return holdsSpecimenTypeCollection
	 */
	public Collection<String> getHoldsSpecimenTypeCollection()
	{
		return holdsSpecimenTypeCollection;
	}

	/**
	 * Holds SpecimenType Collection.
	 * @param holdsSpecimenTypeCollection
	 */
	public void setHoldsSpecimenTypeCollection(Collection<String> holdsSpecimenTypeCollection)
	{
		this.holdsSpecimenTypeCollection = holdsSpecimenTypeCollection;
	}

	/**
	 * Parameterized Constructor.
	 * @param abstractActionForm of AbstractActionForm type.
	 * @throws AssignDataException AssignDataException.
	 */
	public StorageType(AbstractActionForm abstractActionForm) throws AssignDataException
	{
		super();
		this.setAllValues(abstractActionForm);
	}

	/**
	 * @return Returns the defaultTempratureInCentigrade.
	 * @hibernate.property name="defaultTempratureInCentigrade" type="double"
	 * column="DEFAULT_TEMPERATURE" length="30"
	 */
	public Double getDefaultTempratureInCentigrade()
	{
		return this.defaultTempratureInCentigrade;
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
	public Collection<String> getHoldsSpecimenClassCollection()
	{
		return this.holdsSpecimenClassCollection;
	}

	/**
	 * @param holdsSpecimenClassCollection The holdsSpecimenClassCollection to set.
	 */
	public void setHoldsSpecimenClassCollection(Collection<String> holdsSpecimenClassCollection)
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
	public Collection<StorageType> getHoldsStorageTypeCollection()
	{
		return this.holdsStorageTypeCollection;
	}

	/**
	 * @param holdsStorageTypeCollection The holdsStorageTypeCollection to set.
	 */
	public void setHoldsStorageTypeCollection(Collection<StorageType> holdsStorageTypeCollection)
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
	public Collection<SpecimenArrayType> getHoldsSpecimenArrayTypeCollection()
	{
		return this.holdsSpecimenArrayTypeCollection;
	}

	/**
	 * @param holdsSpecimenArrayTypeCollection The holdsSpecimenArrayTypeCollection to set.
	 */
	public void setHoldsSpecimenArrayTypeCollection(Collection<SpecimenArrayType> holdsSpecimenArrayTypeCollection)
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
	 * @throws AssignDataException : AssignDataException
	 */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			final StorageTypeForm storageTypeForm = (StorageTypeForm) abstractForm;

			this.id = Long.valueOf(storageTypeForm.getId());
			this.name = storageTypeForm.getType().trim();

			if (storageTypeForm.getDefaultTemperature() != null
					&& storageTypeForm.getDefaultTemperature().trim().length() > 0)
			{
				this.defaultTempratureInCentigrade = new Double(storageTypeForm
						.getDefaultTemperature());
			}

			this.oneDimensionLabel = storageTypeForm.getOneDimensionLabel();
			this.twoDimensionLabel = storageTypeForm.getTwoDimensionLabel();

			if (SearchUtil.isNullobject(this.capacity))
			{
				this.capacity = new Capacity();
			}

			this.capacity.setOneDimensionCapacity(Integer.valueOf(storageTypeForm
					.getOneDimensionCapacity()));
			this.capacity.setTwoDimensionCapacity(Integer.valueOf(storageTypeForm
					.getTwoDimensionCapacity()));

			//holdsStorageTypeCollection.clear();
			this.holdsStorageTypeCollection = new HashSet<StorageType>();
			final long[] storageTypeArr = storageTypeForm.getHoldsStorageTypeIds();
			if (storageTypeArr != null)
			{
				for (final long element : storageTypeArr)
				{
					logger.debug("type Id :" + element);
					if (element != -1)
					{
						final StorageType storageType = new StorageType();
						storageType.setId(Long.valueOf(element));
						this.holdsStorageTypeCollection.add(storageType);
					}
				}
			}

			//holdsSpecimenClassCollection.clear();
			this.holdsSpecimenClassCollection = new HashSet<String>();
			if (storageTypeForm.getSpecimenOrArrayType().equals("Specimen"))
			{
				final String[] specimenClassTypeArr = storageTypeForm.getHoldsSpecimenClassTypes();
				if (specimenClassTypeArr != null)
				{

					for (final String element : specimenClassTypeArr)
					{
						logger.debug("type Id :" + element);
						if (element.equals("-1"))
						{
							this.holdsSpecimenClassCollection.addAll(AppUtility
									.getSpecimenClassTypes());
							break;
						}
						else
						{
							this.holdsSpecimenClassCollection.add(element);
						}
					}
				}
			}
			
			//holdsSpecimenClassCollection.clear();
			this.holdsSpecimenTypeCollection = new HashSet<String>();
			if (storageTypeForm.getSpecimenOrArrayType().equals("Specimen"))
			{
				final String[] specimenTypeArr = storageTypeForm.getHoldsSpecimenType();
				if (specimenTypeArr != null)
				{
					for (final String element : specimenTypeArr)
					{
						logger.debug("type Id :" + element);
						if (element.equals("-1"))
						{
							this.holdsSpecimenTypeCollection.addAll(AppUtility.getSpecimenTypes());
							break;
						}
						else
						{
							this.holdsSpecimenTypeCollection.add(element);
						}
					}
				}
			}
			//			holdsSpArrayTypeCollection.clear();
			this.holdsSpecimenArrayTypeCollection = new HashSet<SpecimenArrayType>();
			this.populateHoldsSpecimenArrayTypeCollection(storageTypeForm);
			this.activityStatus = "Active";
		}
		catch (final Exception excp)
		{
			StorageType.logger.error(excp.getMessage(),excp);
			excp.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "Specimen.java :");
		}
	}

	/**
	 * @param storageTypeForm of StorageTypeForm type.
	 */
	private void populateHoldsSpecimenArrayTypeCollection(final StorageTypeForm storageTypeForm)
	{
		if (storageTypeForm.getSpecimenOrArrayType().equals("SpecimenArray"))
		{
			final long[] specimenArrayTypeArr = storageTypeForm.getHoldsSpecimenArrTypeIds();
			if (specimenArrayTypeArr != null)
			{
				for (final long element : specimenArrayTypeArr)
				{
					logger.debug("specimen array type Id :" + element);
					if (element != -1)
					{
						final SpecimenArrayType spArrayType = new SpecimenArrayType();
						spArrayType.setId(Long.valueOf(element));
						this.holdsSpecimenArrayTypeCollection.add(spArrayType);
					}
				}
			}
		}
	}

	/**
	 * @param object Object.
	 * @return Boolean.
	 */
	@Override
	public boolean equals(Object object)
	{
		boolean equals = false;
		if ((object != null) && object instanceof StorageType)
		{
			final StorageType storageType = (StorageType) object;
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
	@Override
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