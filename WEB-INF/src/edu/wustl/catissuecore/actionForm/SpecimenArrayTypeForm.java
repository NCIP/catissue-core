/*
 * <p>Title: SpecimenArrayTypeForm</p>
 * <p>Description:This class initializes the fields of ArrayType form which is associated with Array Type Action
 *  & asociated request parameters with form. </p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on July 14,2006
 */

package edu.wustl.catissuecore.actionForm;

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
	
	/**
	 * default serial version ID 
	 */
	private static final long serialVersionUID = -4486179745074687647L;

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
	protected String[] specimenType;

    /**
     * one dimension capacity for array type 
     */
	protected String oneDimensionCapacity;

    /**
     * two dimension capacity for array type 
     */
	protected String twoDimensionCapacity;

    /**
     * comments to be put for array type 
     */
	protected String comments;

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
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
    public String getOneDimensionCapacity()
    {
        return oneDimensionCapacity;
    }

    /**
     * @param oneDimensionCapacity The oneDimensionCapacity to set.
     */
    public void setOneDimensionCapacity(String oneDimensionCapacity)
    {
        this.oneDimensionCapacity = oneDimensionCapacity;
    }

    /**
     * @return Returns the specimenClass.
     */
    public String getSpecimenClass()
    {
        return specimenClass;
    }

    /**
     * @param specimenClass The specimenClass to set.
     */
    public void setSpecimenClass(String specimenClass)
    {
        this.specimenClass = specimenClass;
    }

    /**
     * @return Returns the specimenType.
     */
    public String[] getSpecimenType()
    {
        return specimenType;
    }

    /**
     * @param specimenType The specimenType to set.
     */
    public void setSpecimenType(String specimenType[])
    {
        this.specimenType = specimenType;
    }

    /**
     * @return Returns the twoDimensionCapacity.
     */
    public String getTwoDimensionCapacity()
    {
        return twoDimensionCapacity;
    }

    /**
     * @param twoDimensionCapacity The twoDimensionCapacity to set.
     */
    public void setTwoDimensionCapacity(String twoDimensionCapacity)
    {
        this.twoDimensionCapacity = twoDimensionCapacity;
    }

    /**
     * @return Returns the comments.
     */
    public String getComments()
    {
        return comments;
    }

    /**
     * @param comments The comments to set.
     */
    public void setComments(String comments)
    {
        this.comments = comments;
    }

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#setAllValues(edu.wustl.common.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject domainObject) {
		if (domainObject instanceof SpecimenArrayType) {
			SpecimenArrayType arrayType = (SpecimenArrayType) domainObject;
	        this.id = arrayType.getId().longValue();
	        this.name = arrayType.getName();
	        //this.specimenClass = arrayType.getSpecimenClass().getName();
	        this.oneDimensionCapacity = arrayType.getCapacity().getOneDimensionCapacity().toString();
	        this.twoDimensionCapacity = arrayType.getCapacity().getTwoDimensionCapacity().toString();
	        this.comments = arrayType.getComment();
	        // To Do code for specimen array type page
		}
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#reset()
	 */
	protected void reset() {
	}
	
	/**
	 * validate specimen array type form level fields & return appropriate message.
	 * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        try {
            if (operation.equals(Constants.ADD)
                    || operation.equals(Constants.EDIT))
            {
//            	validate name of array type
                if (validator.isEmpty(name))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.required", ApplicationProperties
                                    .getValue("arrayType.name")));
                }
//            	 validate specimen class of array type
                if (!validator.isValidOption(specimenClass))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.required", ApplicationProperties
                                    .getValue("arrayType.specimenClass")));
                }
//              validate specimen type of array type
                if (!validator.isValidOption(specimenType[0]))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.required", ApplicationProperties
                                    .getValue("arrayType.specimenType")));
                }
                
//              validate one dimension capacity of array type is empty or not numeric
                if(validator.isEmpty(oneDimensionCapacity))
                {
	                  errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
	                  "errors.item.required", ApplicationProperties
	                          .getValue("arrayType.oneDimensionCapacity")));
                } else if(!validator.isNumeric(oneDimensionCapacity,1) || !validator.isNumeric(twoDimensionCapacity,1)) {
	                  errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
	                  "errors.item.format", ApplicationProperties
	                          .getValue("arrayType.capacity")));
                }
            }
		} catch (Exception e) {
			Logger.out.error(e.getMessage());
		}
		return errors;
	}
	
	
}