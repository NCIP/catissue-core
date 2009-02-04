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
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to encapsulate all the request parameters passed from StorageType.jsp page.
 * @author aniruddha_phadnis
 * */
public class StorageTypeForm extends AbstractActionForm
{

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
	 * List of storage types Ids  that Storage Type can hold
	 */
	private long holdsStorageTypeIds[];

	/**
	 * List of Specimen Types that Storage Type can hold
	 */
	private String holdsSpecimenClassTypes[];

	private long holdsSpecimenArrTypeIds[];

	private String specimenOrArrayType;

	/**
	 * No argument constructor for StorageTypeForm class 
	 */
	public StorageTypeForm()
	{
		reset();
	}

	/**
	 * This function Copies the data from an storage type object to a StorageTypeForm object.
	 * @param storageType A StorageType object containing the information about storage type of the container.  
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		StorageType storageType = (StorageType) abstractDomain;
		Logger.out.info("in storege type form :"
				+ storageType.getHoldsSpecimenClassCollection().size());
		this.id = storageType.getId().longValue();
		this.type = storageType.getName();
		this.defaultTemperature = Utility.toString(storageType.getDefaultTempratureInCentigrade());
		this.oneDimensionCapacity = storageType.getCapacity().getOneDimensionCapacity().intValue();
		this.twoDimensionCapacity = storageType.getCapacity().getTwoDimensionCapacity().intValue();
		this.oneDimensionLabel = storageType.getOneDimensionLabel();
		this.twoDimensionLabel = storageType.getTwoDimensionLabel();
		//      Populating the storage type-id array
		Collection storageTypeCollection = storageType.getHoldsStorageTypeCollection();

		if (storageTypeCollection != null)
		{
			holdsStorageTypeIds = new long[storageTypeCollection.size()];
			int i = 0;

			Iterator it = storageTypeCollection.iterator();
			while (it.hasNext())
			{
				StorageType holdStorageType = (StorageType) it.next();
				holdsStorageTypeIds[i] = holdStorageType.getId().longValue();
				i++;
			}
		}
		//      Populating the specimen class type-id array
		Collection specimenClassTypeCollection = storageType.getHoldsSpecimenClassCollection();

		if (specimenClassTypeCollection != null)
		{
			if (specimenClassTypeCollection.size() == Utility.getSpecimenClassTypes().size())
			{
				holdsSpecimenClassTypes = new String[1];
				holdsSpecimenClassTypes[0] = "-1";
				this.specimenOrArrayType = "Specimen";
			}
			else
			{
				holdsSpecimenClassTypes = new String[specimenClassTypeCollection.size()];
				int i = 0;

				Iterator it = specimenClassTypeCollection.iterator();
				while (it.hasNext())
				{
					String specimenClass = (String) it.next();
					holdsSpecimenClassTypes[i] = specimenClass;
					i++;
					this.specimenOrArrayType = "Specimen";
					
				}
			}
		}
		//      Populating the specimen array type-id array
		Collection specimenArrayTypeCollection = storageType.getHoldsSpecimenArrayTypeCollection();

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
		Logger.out.info("in form bean:----------------"+this.specimenOrArrayType);
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
	 * Sets the default temperature of the storage container
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
		return holdsStorageTypeIds;
	}

	/**
	 * Sets the Storage Type Holds List.
	 * @param holdStorageTypeList the list of storage type Ids to be set.
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
		return holdsSpecimenClassTypes;
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
	 * Sets the Specimen Class torage Type Holds List.
	 * @param holdSpecimenClassTypeList the list of specimen class type Ids to be set.
	 * @see #getHoldsSpecimenClassList()
	 */
	public void setHoldsSpecimenClassTypes(String[] holdsSpecimenClassTypes)
	{
		this.holdsSpecimenClassTypes = holdsSpecimenClassTypes;
	}

	/**
	 * Returns the id assigned to form bean
	 */
	public int getFormId()
	{
		return Constants.STORAGE_TYPE_FORM_ID;
	}

	/**
	 * Resets the values of all the fields.
	 * Is called by the overridden reset method defined in ActionForm.  
	 * */
	protected void reset()
	{
		/*this.oneDimensionLabel = null;
		this.twoDimensionLabel = null;
		this.type = null;
		this.defaultTemperature = null;*/
		
	}

	/**
	 * Overrides the validate method of ActionForm.
	 * */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();

		try
		{
			setRedirectValue(validator);
			if (validator.isEmpty(type))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("storageType.type")));
			}
			else
			{
				String s = new String("- _");
				String delimitedString = validator.delimiterExcludingGiven(s);
				if (validator.containsSpecialCharacters(type, delimitedString))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.valid.data",
							ApplicationProperties.getValue("storageType.type")));
				}

			}
			if (validator.isEmpty(String.valueOf(oneDimensionCapacity)))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("storageType.oneDimensionCapacity")));
			}
			else
			{
				if (!validator.isNumeric(String.valueOf(oneDimensionCapacity)))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.valid.number",
							ApplicationProperties.getValue("storageType.oneDimensionCapacity")));
				}
			}
			// Mandar 10-apr-06 : bugid :353 
			// Error messages should be in the same sequence as the sequence of fields on the page.

			if (validator.isEmpty(oneDimensionLabel))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("storageType.oneDimensionLabel")));
			}

			if(holdsStorageTypeIds != null) 
			{
			checkValidSelectionForAny(holdsStorageTypeIds, "storageType.holdsStorageType", errors);
			}
			//checkValidSelectionForAny(holdsSpecimenClassTypeIds,"storageType.holdsSpecimenClass",errors);
			if (validator.isEmpty(String.valueOf(twoDimensionCapacity)))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("storageType.twoDimensionCapacity")));
			}
			else
			{
				if (!validator.isNumeric(String.valueOf(twoDimensionCapacity)))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.valid.number",
							ApplicationProperties.getValue("storageType.twoDimensionCapacity")));
				}
			}

			if (validator.isEmpty(twoDimensionLabel) && (twoDimensionCapacity > 1))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.labelRequired",
						ApplicationProperties.getValue("storageType.twoDimensionLabel")));
			}
			// code as per bug id 233 
			// checking for double value if present
			if (!validator.isEmpty(defaultTemperature)
					&& !validator.isDouble(defaultTemperature, false))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
						ApplicationProperties.getValue("storageType.defaultTemperature")));
			}
		}
		catch (Exception excp)
		{
			Logger.out.error(excp.getMessage());
		}
		return errors;
	}

	void checkValidSelectionForAny(long[] Ids, String message, ActionErrors errors)
	{
		if (Ids.length > 1)
		{
			for (int i = 0; i < Ids.length; i++)
			{
				if (Ids[i] == 1)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
							ApplicationProperties.getValue(message)));
					break;
				}
			}
		}
	}

	public String getSpecimenOrArrayType()
	{
		return specimenOrArrayType;
	}

	public void setSpecimenOrArrayType(String specimenOrArrayType)
	{
		this.specimenOrArrayType = specimenOrArrayType;
	}

}