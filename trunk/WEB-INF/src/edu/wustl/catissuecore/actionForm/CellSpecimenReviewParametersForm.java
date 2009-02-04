/**
 * <p>Title: CellSpecimenReviewParametersForm Class</p>
 * <p>Description:  This Class handles the Cell Specimen Review event parameters.
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

import edu.wustl.catissuecore.domain.CellSpecimenReviewParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


/**
 * @author mandar_deshmukh
 * Handles attributes associated with a review event of a cell specimen.
 */
public class CellSpecimenReviewParametersForm extends SpecimenEventParametersForm
{
	/**
     * Percentage of histologically evident neoplastic cells present in the specimen.
     */
	protected String neoplasticCellularityPercentage;
	
	/**
     * Percentage of viable cells in the specimen.
     */
	protected String viableCellPercentage;

	/**
     * Returns the percentage of histologically evident neoplastic cells present in the specimen. 
     * @return The percentage of histologically evident neoplastic cells present in the specimen.
     * @see #setNeoplasticCellularityPercentage(double)
     */
	public String getNeoplasticCellularityPercentage()
	{
		return neoplasticCellularityPercentage;
	}

	/**
     * Sets the percentage of histologically evident neoplastic cells present in the specimen.
     * @param neoplasticCellularityPercentage the percentage of histologically evident neoplastic cells present in the specimen.
     * @see #getNeoplasticCellularityPercentage()
     */
	public void setNeoplasticCellularityPercentage(String neoplasticCellularityPercentage)
	{
		this.neoplasticCellularityPercentage = neoplasticCellularityPercentage;
	}

	/**
     * Returns the percentage of viable cells in the specimen. 
     * @return The percentage of viable cells in the specimen.
     * @see #setViableCellPercentage(Double)
     */
	public String getViableCellPercentage()
	{
		return viableCellPercentage;
	}

	/**
     * Sets the percentage of viable cells in the specimen.
     * @param viableCellPercentage the percentage of viable cells in the specimen.
     * @see #getViableCellPercentage()
     */
	public void setViableCellPercentage(String viableCellPercentage)
	{
		this.viableCellPercentage = viableCellPercentage;
	}
	
//	 ----- SUPERCLASS METHODS
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.CELL_SPECIMEN_REVIEW_PARAMETERS_FORM_ID;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		CellSpecimenReviewParameters cellSpecimenReviewParametersObject = (CellSpecimenReviewParameters)abstractDomain ;
		this.neoplasticCellularityPercentage =Utility.toString(cellSpecimenReviewParametersObject.getNeoplasticCellularityPercentage()) ;
		this.viableCellPercentage = Utility.toString(cellSpecimenReviewParametersObject.getViableCellPercentage()) ; 
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
         	// checks the neoplasticCellularityPercentage
           	if (!validator.isEmpty(neoplasticCellularityPercentage)  &&  !validator.isDouble(neoplasticCellularityPercentage,false) )
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("cellspecimenreviewparameters.neoplasticcellularitypercentage")));
            }


         	// checks the viableCellPercentage
           	if (!validator.isEmpty(viableCellPercentage) && !validator.isDouble(viableCellPercentage,false) )
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("cellspecimenreviewparameters.viablecellpercentage")));
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
     	//super.reset();
//     	this.neoplasticCellularityPercentage = null;
//     	this.viableCellPercentage = null;
 	}
}
