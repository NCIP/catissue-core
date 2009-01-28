/**
 * <p>Title: FrozenEventParametersForm Class</p>
 * <p>Description:  This Class handles the frozen event parameters..
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

import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh
 *
 * Description:  This Class handles the frozen event parameters..
 */
public class FrozenEventParametersForm extends SpecimenEventParametersForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(FrozenEventParametersForm.class);
	
	/**
     * Method applied on specimen to freeze it.
     */
	private String method = (String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_METHOD);

	/**
	 * @return Returns the method applied on specimen to freeze it.
	 */
	public String getMethod()
	{
		return method;
	}
	
	/**
	 * @param method The method to set.
	 */
	public void setMethod(String method)
	{
		this.method = method;
	}

	// ----- SUPERCLASS METHODS
	/**
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 * @return FROZEN_EVENT_PARAMETERS_FORM_ID
	 */
	public int getFormId()
	{
		return Constants.FROZEN_EVENT_PARAMETERS_FORM_ID;
	}

	/**
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 * @param abstractDomain An AbstractDomain Object
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		FrozenEventParameters frozenEventParametersObject = (FrozenEventParameters)abstractDomain ;
		this.method = Utility.toString(frozenEventParametersObject.getMethod());
	}
	
	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
     {
     	ActionErrors errors = super.validate(mapping, request);
         Validator validator = new Validator();
         
         try
         {
//         	// checks the method
           	if (!validator.isValidOption(method))
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("frozeneventparameters.method")));
            }
         }
         catch(Exception excp)
         {
             logger.error(excp.getMessage());
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
//         this.method = null;
     }
       
   
}