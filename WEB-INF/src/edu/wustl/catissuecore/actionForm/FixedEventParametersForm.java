/**
 * <p>Title: FixedEventParametersForm Class</p>
 * <p>Description:  This Class handles the Fixed Event Parameters.
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

import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


/**
 * @author mandar_deshmukh
 *
 * This Class handles the Fixed Event Parameters.
 */
public class FixedEventParametersForm extends SpecimenEventParametersForm
{
	/**
	 * Type of the fixation.
	 */
	protected String fixationType = Constants.NOTSPECIFIED;

	/**
	 * Duration, measured in minutes, for which fixation is performed on specimen.
	 */
	protected int durationInMinutes;

	/**
	 * Returns the Type of the fixation.
	 * @return fixationType.
	 */
	public String getFixationType()
	{
		return fixationType;
	}

	/**
	 * Sets the fixationType. 
	 * @param fixationType fixationType of specimen.
	 */
	public void setFixationType(String fixationType)
	{
		this.fixationType = fixationType;
	}

	/**
	 * Returns the  duration In Minutes.
	 * @return durationInMinutes.
	 */
	public int getDurationInMinutes()
	{
		return durationInMinutes;
	}

	/**
	 * Sets the durationInMinutes. 
	 * @param durationInMinutes durationInMinutes required for fixation.
	 */
	public void setDurationInMinutes(int durationInMinutes)
	{
		this.durationInMinutes = durationInMinutes;
	}
	
//	 ----- SUPERCLASS METHODS
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.FIXED_EVENT_PARAMETERS_FORM_ID;
	}

	public void setAllValues(AbstractDomainObject abstractDomain)
	{
	    try
        {
			super.setAllValues(abstractDomain);
			FixedEventParameters fixedEventParametersObject = (FixedEventParameters)abstractDomain ;
			this.fixationType = Utility.toString(fixedEventParametersObject.getFixationType());
			this.durationInMinutes = fixedEventParametersObject.getDurationInMinutes().intValue();  
	    }
	    catch(Exception excp)
	    {
	        Logger.out.error(excp.getMessage());
	    }
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

         //	 checks the fixationType
          	if (!validator.isValidOption(fixationType ))
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("fixedeventparameters.fixationtype")));
            }
          	
//       	 checks the durationInMinutes
//         	if (!validator.isNumeric(String.valueOf(durationInMinutes)))
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("fixedeventparameters.durationinminutes")));
//            }
//          	           //	 checks the durationInMinutes
//         	else if (durationInMinutes <= 0 )
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("fixedeventparameters.durationinminutes")));
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
//         super.reset();
//         this.fixationType = null;
//         this.durationInMinutes = 0;
     }
    
	
}
