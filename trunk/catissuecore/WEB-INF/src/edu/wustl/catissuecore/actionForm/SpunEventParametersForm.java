/**
 * <p>Title:SpunEventParametersForm Class</p>
 * <p>Description:  This Class handles the Spun event parameters..
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on August 8th, 2005
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.SpunEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Mandar Deshmukh
 *
 * Description:  This Class handles the Spun event parameters..
 */
public class SpunEventParametersForm extends SpecimenEventParametersForm
{
	/**
     * Rotational force applied to specimen.
     */
	protected double gravityForce;
	
	/**
     * Duration for which specimen is spun.
     */
	protected int durationInMinutes;

//	/**
//     * Returns the rotational force applied to specimen. 
//     * @return The rotational force applied to specimen.
//     * @see #setGForce(double)
//     */
//	public double getGForce()
//	{
//		return gForce;
//	}
//
//	/**
//     * Sets the rotational force applied to specimen.
//     * @param gForce the rotational force applied to specimen.
//     * @see #getGForce()
//     */
//	public void setGForce(double gForce)
//	{
//		this.gForce = gForce;
//	}

	/**
     * Returns duration for which specimen is spun. 
     * @return Duration for which specimen is spun.
     * @see #setDurationInMinutes(int)
     */
	public int getDurationInMinutes()
	{
		return durationInMinutes;
	}

	/**
     * Sets the duration for which specimen is spun.
     * @param durationInMinutes duration for which specimen is spun.
     * @see #getDurationInMinutes()
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
		return Constants.SPUN_EVENT_PARAMETERS_FORM_ID ;
	}

	public void setAllValues(AbstractDomainObject abstractDomain)
	{
	    try
       {
			super.setAllValues(abstractDomain);
			SpunEventParameters spunEventParametersObject = (SpunEventParameters)abstractDomain ;
			this.gravityForce = spunEventParametersObject.getGravityForce().doubleValue() ;
			this.durationInMinutes = spunEventParametersObject.getDurationInMinutes().intValue() ; 
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
        	
           //	 checks the gForce
//        	if (!validator.isDouble(""+gravityForce ))
//           {
//          		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("spuneventparameters.gforce")));
//           }
        	
        	Logger.out.info("durationInMinutes: "+ durationInMinutes  );
            //	 checks the durationInMinutes
//         	if (!validator.isNumeric(String.valueOf(durationInMinutes),1) )
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("spuneventparameters.durationinminutes")));
//            }
        	
        }
        catch(Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
        return errors;
     }
	

   
	 protected void reset()
	 {
//	 	super.reset();
//        this.gravityForce = 0;
//        this.durationInMinutes = 0;
	 }

	
    
	/**
	 * @return Returns the gravityForce.
	 */
	public double getGravityForce()
	{
		return gravityForce;
	}
	/**
	 * @param gravityForce The gravityForce to set.
	 */
	public void setGravityForce(double gravityForce)
	{
		this.gravityForce = gravityForce;
	}
}