/**
 * <p>Title: FluidSpecimenReviewEventParametersForm Class</p>
 * <p>Description:  This Class handles the Fluid Specimen Review event parameters.
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

import edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


/**
 * @author mandar_deshmukh
 *   This Class handles the Fluid Specimen Review event parameters.
 */
public class FluidSpecimenReviewEventParametersForm extends SpecimenEventParametersForm
{
	/**
     * Cell Count.
     */
	protected String cellCount;

	/**
     * Returns the cell count. 
     * @return The cell count.
     * @see #setCellCount(double)
     */
	public String getCellCount()
	{
		return cellCount;
	}

	/**
     * Sets the cell count.
     * @param cellCount the cell count.
     * @see #getCellCount()
     */
	public void setCellCount(String cellCount)
	{
		this.cellCount = cellCount;
	}

	
//	 ----- SUPERCLASS METHODS
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		FluidSpecimenReviewEventParameters fluidSpecimenReviewEventParametersObject = (FluidSpecimenReviewEventParameters)abstractDomain ;
		this.cellCount = Utility.toString(fluidSpecimenReviewEventParametersObject.getCellCount()) ; 
		Logger.out.debug("############FormObject################## : ");
		Logger.out.debug(this.cellCount);
		Logger.out.debug("############################## : ");
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
 
//         	// checks the cellCount
           	if (!validator.isEmpty(cellCount) && !validator.isDouble(cellCount,false) )
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("fluidspecimenrevieweventparameters.cellcount")));
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
       //  this.cellCount = null;
      }
       

	
}
