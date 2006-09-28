/**
 * <p>Title: QuickEventsForm Class</p>
 * <p>Description:  QuickEventsForm Class is used to encapsulate all the request parameters passed 
 * from QuickEvents webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 3, 2006
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * <p>Title: QuickEventsForm Class</p>
 * <p>Description:  QuickEventsForm Class is used to encapsulate all the request parameters passed 
 * from QuickEvents webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 3, 2006
 */
public class QuickEventsForm extends AbstractActionForm {
	/**
	 * Label of Specimen to search.
	 */
	private String specimenLabel;
	/**
	 * Barcode of Specimen to search.
	 */
	private String barCode;

	/**
	 * Event to be selected.
	 */
	private String specimenEventParameter;
	
	/**
	 * Radio button to choose SpecimenID/barcode.
	 */
	private String checkedButton = "1";
	
	
    /**
     * No argument constructor for UserForm class 
     */
    public QuickEventsForm()
    {
        reset();
    }

    /**
     * Empty implementation of setAllValues() method.
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
    }
    
        
    /**
     * Returns the id assigned to form bean
     */
    public int getFormId()
    {
        return Constants.QUICKEVENTS_FORM_ID;
    }
    
    /**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.  
     * */
    protected void reset()
    {
    }
    
    
    

	/**
	 * @return Returns the barCode.
	 */
	public String getBarCode() {
		return barCode;
	}
	/**
	 * @param barCode The barCode to set.
	 */
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	/**
	 * @return Returns the specimenEventParameter.
	 */
	public String getSpecimenEventParameter() {
		return specimenEventParameter;
	}
	/**
	 * @param specimenEventParameter The specimenEventParameter to set.
	 */
	public void setSpecimenEventParameter(String eventName) {
		this.specimenEventParameter = eventName;
	}
	/**
	 * @return Returns the specimenLabel.
	 */
	public String getSpecimenLabel() {
		return specimenLabel;
	}
	/**
	 * @param specimenLabel The specimenLabel to set.
	 */
	public void setSpecimenLabel(String specimenID) {
		this.specimenLabel = specimenID;
	}
	
	/**
	 * @return Returns the checkedButton.
	 */
	public String getCheckedButton() {
		return checkedButton;
	}
	/**
	 * @param checkedButton The checkedButton to set.
	 */
	public void setCheckedButton(String checkedButton) {
		this.checkedButton = checkedButton;
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
            if (!validator.isValidOption(specimenEventParameter))
            {
            	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("quickEvents.eventparameters")));
            }
            if(checkedButton.equals("1" ) && !validator.isValidOption(specimenLabel) )
            {
            	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("quickEvents.specimenLabel")));
            }
            if(checkedButton.equals("2" ) && validator.isEmpty(barCode) )
            {
            	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("quickEvents.barcode")));
            }
        }
        catch(Exception excp)
        {
            Logger.out.error(excp.getMessage(),excp);
        }
        return errors;
     }
 
}
