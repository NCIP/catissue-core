/**
 * <p>Title: CheckInCheckOutEventParametersForm Class</p>
 * <p>Description:  This Class handles the CheckIn CheckOut Event Parameters. 
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 28th, 2005
 */
package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
* This Class handles the CheckIn CheckOut Event Parameters.
*/
public class CheckInCheckOutEventParametersForm extends SpecimenEventParametersForm
{
	/**
	 * Type of the movement e.g. Check-in or Check-out.
	 */
	protected String storageStatus;
	
	
	
	/**
	 * @return Returns the storageStatus.
	 * Type of the movement e.g. Check-in or Check-out.
	 */
	public String getStorageStatus()
	{
		return storageStatus;
	}
	/**
	 * @param storageStatus The storageStatus to set.
	 */
	public void setStorageStatus(String storageStatus)
	{
		this.storageStatus = storageStatus;
	}
	
	
// ---- super class methods
	// ----- SUPERCLASS METHODS
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		CheckInCheckOutEventParameter checkInCheckOutEventParameterObject = (CheckInCheckOutEventParameter)abstractDomain ;
		this.storageStatus = Utility.toString(checkInCheckOutEventParameterObject.getStorageStatus()); 
	}
	
	/**
	 * Overrides the validate method of ActionForm.
     * */
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
     {
     	 
         ActionErrors errors = super.validate(mapping, request);
         Validator validator = new Validator();
         
         try
         {
         	// checks the storageStatus
         	// changed from isEmpty to isValidOption as per bug 294 textbox to dropdown
           	if ( !validator.isValidOption( storageStatus  ))
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("checkincheckouteventparameter.storagestatus")));
            }
         }
         catch(Exception excp)
         {
             Logger.out.error(excp.getMessage());
         }
         return errors;
      }
	
     protected void reset()
  	{
//      	super.reset();
//        this.storageStatus = null;
  	}


	
}
