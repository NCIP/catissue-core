/*
 * <p>Title: SpecimenArrayTypeForm Class </p>
 * <p>Description:This class initializes the fields of ArrayType form which is associated with Array Type Action
 *  & asociated request parameters with form. </p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on July 24,2006
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields of ArrayType form which is associated with Array Type Action
 * & asociated request parameters with form.
 * @author Ashwin Gupta
 * @author gautam_shetty
 * @see edu.wustl.common.actionForm.AbstractActionForm
 */
public class SpecimenArrayTypeForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(SpecimenArrayTypeForm.class);
	/**
	 * name of array type
	 */
	protected String name;

	/**
	 * Specimen class to which array type be associated
	 */
	protected String specimenClass;

	/**
	 * Specimen type to which array type be associated
	 */
	protected String[] specimenTypes;

	/**
	 * one dimension capacity for array type
	 */
	protected int oneDimensionCapacity;

	/**
	 * two dimension capacity for array type
	 */
	protected int twoDimensionCapacity;

	/**
	 * comments to be put for array type
	 */
	protected String comment;

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return Returns the oneDimensionCapacity.
	 */
	public int getOneDimensionCapacity()
	{
		return this.oneDimensionCapacity;
	}

	/**
	 * @param oneDimensionCapacity The oneDimensionCapacity to set.
	 */
	public void setOneDimensionCapacity(int oneDimensionCapacity)
	{
		this.oneDimensionCapacity = oneDimensionCapacity;
	}

	/**
	 * @return Returns the specimenClass.
	 */
	public String getSpecimenClass()
	{
		return this.specimenClass;
	}

	/**
	 * @param specimenClass The specimenClass to set.
	 */
	public void setSpecimenClass(String specimenClass)
	{
		this.specimenClass = specimenClass;
	}

	/**
	 * @return Returns the specimenTypes.
	 */
	public String[] getSpecimenTypes()
	{
		return this.specimenTypes;
	}

	/**
	 * @param specimenType The specimenTypes to set.
	 */
	public void setSpecimenTypes(String[] specimenType)
	{
		this.specimenTypes = specimenType;
	}

	/**
	 * @return Returns the twoDimensionCapacity.
	 */
	public int getTwoDimensionCapacity()
	{
		return this.twoDimensionCapacity;
	}

	/**
	 * @param twoDimensionCapacity The twoDimensionCapacity to set.
	 */
	public void setTwoDimensionCapacity(int twoDimensionCapacity)
	{
		this.twoDimensionCapacity = twoDimensionCapacity;
	}

	/**
	 * @return Returns the comment.
	 */
	public String getComment()
	{
		return this.comment;
	}

	/**
	 * @param comment The comments to set.
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * @return SPECIMEN_ARRAY_TYPE_FORM_ID
	 */
	@Override
	public int getFormId()
	{
		return Constants.SPECIMEN_ARRAY_TYPE_FORM_ID;
	}

	/**
	 * Populates all the fields from the domain object to the form bean.
	 * @param abstractDomain An AbstractDomain Object
	 */
	public void setAllValues(AbstractDomainObject domainObject)
	{
		if (domainObject instanceof SpecimenArrayType)
		{
			final SpecimenArrayType arrayType = (SpecimenArrayType) domainObject;
			this.setId(arrayType.getId().longValue());
			this.name = arrayType.getName();
			this.specimenClass = arrayType.getSpecimenClass();
			this.oneDimensionCapacity = arrayType.getCapacity().getOneDimensionCapacity()
					.intValue();
			this.twoDimensionCapacity = arrayType.getCapacity().getTwoDimensionCapacity()
					.intValue();
			this.comment = arrayType.getComment();
			final Collection<String> specimenTypeCollection = arrayType.getSpecimenTypeCollection();

			if ((specimenTypeCollection != null) && (!specimenTypeCollection.isEmpty()))
			{
				this.specimenTypes = new String[specimenTypeCollection.size()];
				String specimenTypeStr = null;
				int i = 0;
				for (final Iterator<String> iter = specimenTypeCollection.iterator(); iter.hasNext(); i++)
				{
					specimenTypeStr = iter.next();
					this.specimenTypes[i] = specimenTypeStr;
				}
			}
		}
	}

	/**
	 * Resets the values of all the fields.
	 */
	@Override
	protected void reset()
	{
	}

	/**
	 * validate specimen array type form level fields & return appropriate message.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();
		try
		{
			if (this.getOperation().equals(Constants.ADD)
					|| this.getOperation().equals(Constants.EDIT))
			{
				//            	validate name of array type
				if (Validator.isEmpty(this.name))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("arrayType.name")));
				}
				//            	 validate specimen class of array type
				if (!validator.isValidOption(this.specimenClass))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("arrayType.specimenClass")));
				}
				//              validate specimen type in array type
				if ((this.specimenTypes != null) && (this.specimenTypes.length > 0))
				{
					for (int i = 0; i < this.specimenTypes.length; i++)
					{
						if (this.specimenTypes[i] != null)
						{
							if (!validator.isValidOption(this.specimenTypes[i]))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.item.selected", ApplicationProperties
												.getValue("arrayType.specimenType")));
								break;
							}
						}
					}
				}
				else
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("arrayType.specimenType")));
				}

				//              validate one dimension capacity of array type is empty or not numeric
				/*                if(validator.isEmpty(oneDimensionCapacity))
				                {
					                  errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					                  "errors.item.required", ApplicationProperties
					                          .getValue("arrayType.oneDimensionCapacity")));
				                } else
				*/
				if (!validator.isNumeric(String.valueOf(this.oneDimensionCapacity), 1)
						|| !validator.isNumeric(String.valueOf(this.twoDimensionCapacity), 1))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
							ApplicationProperties.getValue("arrayType.capacity")));
				}
			}
		}
		catch (final Exception e)
		{
			SpecimenArrayTypeForm.logger.error(e.getMessage(),e);
			e.printStackTrace() ;
		}
		return errors;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}