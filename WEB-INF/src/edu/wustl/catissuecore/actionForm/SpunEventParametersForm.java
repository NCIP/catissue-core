/**
 * <p>Title:SpunEventParametersForm Class</p>
 * <p>Description:  This Class handles the Spun event parameters..
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

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.SpunEventParameters;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Jyoti_Singh
 *
 * Description:  This Class handles the Spun event parameters..
 */
public class SpunEventParametersForm extends EventParametersForm
{
	
	private double gForce;
	private int durationInMinutes;


	
	
	
	/**
	 * @return Returns the durationInMinutes.
	 */
	public int getDurationInMinutes()
	{
		return durationInMinutes;
	}
	/**
	 * @param durationInMinutes The durationInMinutes to set.
	 */
	public void setDurationInMinutes(int durationInMinutes)
	{
		this.durationInMinutes = durationInMinutes;
	}
	/**
	 * @return Returns the gForce.
	 */
	public double getGForce()
	{
		return gForce;
	}
	/**
	 * @param force The gForce to set.
	 */
	public void setGForce(double gForce)
	{
		this.gForce = gForce;
	}
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.SPUN_EVENT_PARAMETERS_FORM_ID;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		try
		{
			super.setAllValues(abstractDomain);
			SpunEventParameters SpunEventParametersObject = (SpunEventParameters)abstractDomain ;
			this.gForce = SpunEventParametersObject.getGForce().doubleValue() ;
			this.durationInMinutes = SpunEventParametersObject.getDurationInMinutes().intValue();
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

            //	 checks the durationInMinutes
         	if (durationInMinutes  <= 0)
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("spuneventparameters.durationinminutes")));
            }

         	if (gForce <= 0 || Double.isNaN(gForce))
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("spuneventparameters.gforce")));
            }
         	
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
     public void reset(ActionMapping mapping, HttpServletRequest request)
     {
         reset();
         this.gForce = 0.0;
         this.durationInMinutes = 0;
     }
     
}