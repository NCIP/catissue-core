/**
 * <p>Title: EmbeddedEventParametersForm Class</p>
 * <p>Description:  This Class handles the embedded event parameters..
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Jyoti Singh
 * @version 1.00
 * Created on Aug 2, 2005
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
  *
 * Description:  This Class handles the embedded event parameters..
 */
public class EmbeddedEventParametersForm extends SpecimenEventParametersForm
{
	
	private String embeddingMedium = Constants.NOTSPECIFIED;

	
	public String getEmbeddingMedium()
	{
		return embeddingMedium;
	}
	
	
	public void setEmbeddingMedium(String embeddingMedium)
	{
		this.embeddingMedium = embeddingMedium;
	}

	// ----- SUPERCLASS METHODS
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.EMBEDDED_EVENT_PARAMETERS_FORM_ID;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		EmbeddedEventParameters embeddedEventParametersObject = (EmbeddedEventParameters)abstractDomain ;
		this.embeddingMedium = Utility.toString(embeddedEventParametersObject.getEmbeddingMedium());
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
			if (!validator.isValidOption(embeddingMedium))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("embeddedeventparameters.embeddingMedium")));
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
      protected void reset()
     {
//         super.reset();
//         this.embeddingMedium = null;
     }
  
}