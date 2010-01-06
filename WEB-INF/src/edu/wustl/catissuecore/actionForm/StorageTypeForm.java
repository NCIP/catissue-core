/**
 * <p>Title: StorageTypeForm Class>
 * <p>Description:  This Class is used to encapsulate all the request parameters passed 
 * from StorageType.jsp page. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 15, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to encapsulate all the request parameters passed from StorageType.jsp page.
 * @author aniruddha_phadnis
 * */
public class StorageTypeForm extends AbstractActionForm implements ISpecimenType
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(StorageTypeForm.class);
	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = 1234567890L;
	/**
	 * A string containing the type of the storage.
	 */
	private String type = null;

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
	private int twoDimensionCapacity;

	/**
	 * Text label for dimension one.
	 */
	private String oneDimensionLabel = null;

	/**
	 * Text label for dimension two.
	 */
	private String twoDimensionLabel = null;

	/**
	 * List of storage types Ids  that Storage Type can hold.
	 */
	private long[] holdsStorageTypeIds;

	/**
	 * List of Specimen Types that Storage Type can hold.
	 */
	private String[] holdsSpecimenClassTypes;
	/**
	 * List of Tissue Specimen Types that Storage Type can hold.
	 */
	private String[] holdsTissueSpType;
	/**
	 * List of Fluid Specimen Types that Storage Type can hold.
	 */
	private String[] holdsFluidSpType;
	/**
	 * List of Cell Specimen Types that Storage Type can hold.
	 */
	private String[] holdsCellSpType;
	/**
	 * List of Molecular Specimen Types that Storage Type can hold.
	 */
	private String[] holdsMolSpType;
	/**
	 * @return holdsTissueSpType
	 */
	public String[] getHoldsTissueSpType()
	{
		return holdsTissueSpType;
	}
	/**
	 * @param holdsTissueSpType Tissue Specimen Types
	 */
	public void setHoldsTissueSpType(String[] holdsTissueSpType)
	{
		this.holdsTissueSpType = holdsTissueSpType;
	}
	/**
	 * @return Tissue Specimen Types
	 */
	public String[] getHoldsFluidSpType()
	{
		return holdsFluidSpType;
	}
	/**
	 * @param holdsFluidSpType Fluid Specimen Types
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
	 * @param holdsCellSpType Cell Specimen Types
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
	 * @param holdsMolSpType Molecular Specimen Types
	 */
	public void setHoldsMolSpType(String[] holdsMolSpType)
	{
		this.holdsMolSpType = holdsMolSpType;
	}
	/**
	 * Array of SpecimenArray Id's.
	 */
	private long[] holdsSpecimenArrTypeIds;
	/**
	 * String to check Specimen or SpecimenArray type.
	 */
	private String specimenOrArrayType;

	/**
	 * No argument constructor for StorageTypeForm class.
	 */
	public StorageTypeForm()
	{
		this.reset();
	}

	/**
	 * This function Copies the data from an storage type object to a StorageTypeForm object.
	 * @param abstractDomain StorageType domain Object
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		final StorageType storageType = (StorageType) abstractDomain;
		logger
				.info("in storege type form :"
						+ storageType.getHoldsSpecimenClassCollection().size());
		this.setId(storageType.getId().longValue());
		this.type = storageType.getName();
		this.defaultTemperature = CommonUtilities.toString(storageType
				.getDefaultTempratureInCentigrade());
		this.oneDimensionCapacity = storageType.getCapacity().getOneDimensionCapacity().intValue();
		this.twoDimensionCapacity = storageType.getCapacity().getTwoDimensionCapacity().intValue();
		this.oneDimensionLabel = storageType.getOneDimensionLabel();
		this.twoDimensionLabel = storageType.getTwoDimensionLabel();
		final Collection storageTypeCollection = storageType.getHoldsStorageTypeCollection();
		if (storageTypeCollection != null)
		{
			this.holdsStorageTypeIds = new long[storageTypeCollection.size()];
			int i = 0;
			final Iterator it = storageTypeCollection.iterator();
			while (it.hasNext())
			{
				final StorageType holdStorageType = (StorageType) it.next();
				this.holdsStorageTypeIds[i] = holdStorageType.getId().longValue();
				i++;
			}
		}
		final Collection spClassTypeCollection = setSpClass(storageType);
		final Collection spTypeCollection = storageType.getHoldsSpecimenTypeCollection();
		if(spTypeCollection!=null)
		{
			StorageContainerUtil.populateSpType(spClassTypeCollection, spTypeCollection, this);
		}
		final Collection specimenArrayTypeCollection = storageType
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
	 * @param storageType StorageType
	 * @return specimenClassTypeCollection
	 */
	private Collection setSpClass(final StorageType storageType)
	{
		final Collection specimenClassTypeCollection = storageType
				.getHoldsSpecimenClassCollection();

		if (specimenClassTypeCollection != null)
		{
			if (specimenClassTypeCollection.size() == AppUtility.getSpecimenClassTypes().size())
			{
				this.holdsSpecimenClassTypes = new String[1];
				this.holdsSpecimenClassTypes[0] = "-1";
				this.specimenOrArrayType = "Specimen";
			}
			else
			{
				this.holdsSpecimenClassTypes = new String[specimenClassTypeCollection.size()];
				int i = 0;

				final Iterator it = specimenClassTypeCollection.iterator();
				while (it.hasNext())
				{
					final String specimenClass = (String) it.next();
					this.holdsSpecimenClassTypes[i] = specimenClass;
					i++;
					this.specimenOrArrayType = "Specimen";

				}
				setHoldsSpecimenClassTypes(holdsSpecimenClassTypes);
			}
		}
		return specimenClassTypeCollection;
	}

	/**
	 * Returns the type of the storage.
	 * @return String the type of the storage.
	 * @see #setType(String)
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * Sets the type of the storage.
	 * @param type String type of the storage to be set.
	 * @see #getType()
	 */
	public void setType(String type)
	{
		this.type = type;
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
	 * Returns the list of storage type Ids that this Storage Type can hold.
	 * @return long[] the list of storage type Ids.
	 * @see #getHoldsStorageTypeList(long[])
	 */
	public long[] getHoldsStorageTypeIds()
	{
		return this.holdsStorageTypeIds;
	}

	/**
	 * Sets the Storage Type Holds List.
	 * @param holdsStorageTypeIds the list of storage type Ids to be set.
	 * @see #getHoldsStorageTypeList()
	 */
	public void setHoldsStorageTypeIds(long[] holdsStorageTypeIds)
	{
		this.holdsStorageTypeIds = holdsStorageTypeIds;
	}

	/**
	 * Returns the list of specimen class type Ids that this Storage Type can hold.
	 * @return long[] the list of specimen class type Ids.
	 * @see #getHoldsSpecimenClassList(long[])
	 */
	public String[] getHoldsSpecimenClassTypes()
	{
		return this.holdsSpecimenClassTypes;
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
	 * Sets the Specimen Class torage Type Holds List.
	 * @param holdsSpecimenClassTypes the list of specimen class type Ids to be set.
	 * @see #getHoldsSpecimenClassList()
	 */
	public void setHoldsSpecimenClassTypes(String[] holdsSpecimenClassTypes)
	{
		this.holdsSpecimenClassTypes = holdsSpecimenClassTypes;
	}

	/**
	 * @return Returns the id assigned to form bean
	 */
	@Override
	public int getFormId()
	{
		return Constants.STORAGE_TYPE_FORM_ID;
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
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Action mapping instance
	 * @param request HttpServletRequest instance
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();

		try
		{
			this.setRedirectValue(validator);
			if(this.specimenOrArrayType.equals("Specimen") && this.holdsSpecimenClassTypes==null)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("storageContainer.specimenClass")));
			}
			if (Validator.isEmpty(this.type))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("storageType.type")));
			}
			else
			{
				final String s = new String("- _");
				final String delimitedString = validator.delimiterExcludingGiven(s);
				if (validator.containsSpecialCharacters(this.type, delimitedString))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.valid.data",
							ApplicationProperties.getValue("storageType.type")));
				}

			}
			if (Validator.isEmpty(String.valueOf(this.oneDimensionCapacity)))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("storageType.oneDimensionCapacity")));
			}
			else
			{
				if (!validator.isNumeric(String.valueOf(this.oneDimensionCapacity)))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.valid.number",
							ApplicationProperties.getValue("storageType.oneDimensionCapacity")));
				}
			}
			// Mandar 10-apr-06 : bugid :353 
			// Error messages should be in the same sequence as the sequence of fields on the page.

			if (Validator.isEmpty(this.oneDimensionLabel))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("storageType.oneDimensionLabel")));
			}

			if (this.holdsStorageTypeIds != null)
			{
				this.checkValidSelectionForAny(this.holdsStorageTypeIds,
						"storageType.holdsStorageType", errors);
			}
			//checkValidSelectionForAny(holdsSpecimenClassTypeIds,"storageType.holdsSpecimenClass",errors);
			if (Validator.isEmpty(String.valueOf(this.twoDimensionCapacity)))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("storageType.twoDimensionCapacity")));
			}
			else
			{
				if (!validator.isNumeric(String.valueOf(this.twoDimensionCapacity)))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.valid.number",
							ApplicationProperties.getValue("storageType.twoDimensionCapacity")));
				}
			}

			if (Validator.isEmpty(this.twoDimensionLabel) && (this.twoDimensionCapacity > 1))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.labelRequired",
						ApplicationProperties.getValue("storageType.twoDimensionLabel")));
			}
			if (!Validator.isEmpty(this.defaultTemperature)
					&& !validator.isDouble(this.defaultTemperature, false))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
						ApplicationProperties.getValue("storageType.defaultTemperature")));
			}
		}
		catch (final Exception excp)
		{
			StorageTypeForm.logger.error(excp.getMessage(),excp);
			excp.printStackTrace();
		}
		return errors;
	}

	/**
	 * @param Ids Array of long ids
	 * @param message message for  ApplicationProperties
	 * @param errors Action Errors
	 */
	void checkValidSelectionForAny(long[] Ids, String message, ActionErrors errors)
	{
		if (Ids.length > 1)
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

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}