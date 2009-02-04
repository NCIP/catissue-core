/**
 * <p>Title: DisposalEventParametersForm Class</p>
 * <p>Description:  This Class handles the disposal event parameters..
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Jyoti Singh
 * @version 1.00
 * Created on July 28th, 2005
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Jyoti_Singh
 *
 * Description:  This Class handles the Disposal event parameters..
 */
public class DisposalEventParametersForm extends SpecimenEventParametersForm
{
	/**
     * reason for disposal of specimen it.
     */
	private String reason;
	
	/**
	 * Activity Status of the Specimen
	 */
	private String activityStatus;
	
	public String getActivityStatus()
	{
		return activityStatus;
	}

	
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * @return Returns the reason applied on specimen for its Disposal.
	 */
	public String getReason()
	{
		return reason;
	}
	
	/**
	 * @param reason The reason to set.
	 */
	public void setReason(String reason)
	{
		this.reason = reason;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.DISPOSAL_EVENT_PARAMETERS_FORM_ID;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		DisposalEventParameters disposalEventParametersObject = (DisposalEventParameters)abstractDomain ;
		this.reason = Utility.toString(disposalEventParametersObject.getReason());
		this.activityStatus = disposalEventParametersObject.getSpecimen().getActivityStatus(); 
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
 
         	//Commented due to Bug:- 3106: No need to have Disposal Reason a required field
         	// checks the reason
//           	if (validator.isEmpty(reason ) )
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("disposaleventparameters.reason")));
//            }
         }
         catch(Exception excp)
         {
             Logger.out.error(excp.getMessage());
         }
         return errors;
      }
	
     /**
      * Resets the values of all the fields.
      * This method defined in ActionForm is overridden in this class.
      */
    protected void reset()
    {
//        super.reset();
//        this.reason = null;
    }
	
}