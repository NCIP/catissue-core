/**
 * <p>Title: TissueSpecimenReviewEventParametersForm Class</p>
 * <p>Description:  This Class handles the Tissue Specimen Review event parameters.
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

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


/**
 * @author mandar_deshmukh
 * This Class handles the Tissue Specimen Review event parameters.
 */
public class TissueSpecimenReviewEventParametersForm extends EventParametersForm
{
	/**
     * Percentage of histologically evident neoplastic cells present in the tissue specimen.
     */
	protected double neoplasticCellularityPercentage;
	
	/**
     * Percentage of specimen that is histologically necrotic.
     */
	protected double necrosisPercentage;
	
	/**
     * Percentage of histologically evident lymphocytes in the tissue specimen.
     */
	protected double lymphocyticPercentage;
	
	/**
     * Percentage of total cellularity of the specimen.  Note that TOTCELL-NEOCELL-LYMPHCELL= % cellularity 
     * of other stromal, etc. cell types.  Also Note that 100-TOTCELL-NECROSIS= % of tissue containing a 
     * cellular material.
     */
	protected double totalCellularityPercentage;
	
	/**
     * Histological Quality of the specimen.
     */
	protected String histologicalQuality;

	/**
     * Returns the percentage of histologically evident neoplastic cells present in the tissue specimen. 
     * @return The percentage of histologically evident neoplastic cells present in the tissue specimen.
     * @see #setNeoplasticCellularityPercentage(double)
     */
	public double getNeoplasticCellularityPercentage()
	{
		return neoplasticCellularityPercentage;
	}

	/**
     * Sets the percentage of histologically evident neoplastic cells present in the specimen.
     * @param neoplasticCellularityPercentage the percentage of histologically evident neoplastic cells present in the specimen.
     * @see #getNeoplasticCellularityPercentage()
     */
	public void setNeoplasticCellularityPercentage(double neoplasticCellularityPercentage)
	{
		this.neoplasticCellularityPercentage = neoplasticCellularityPercentage;
	}

	/**
     * Returns the percentage of specimen that is histologically necrotic. 
     * @return The percentage of specimen that is histologically necrotic.
     * @see #setNecrosisPercentage(double)
     */
	public double getNecrosisPercentage()
	{
		return necrosisPercentage;
	}

	/**
     * Sets the percentage of specimen that is histologically necrotic.
     * @param necrosisPercentage the percentage of specimen that is histologically necrotic.
     * @see #getNecrosisPercentage()
     */
	public void setNecrosisPercentage(double necrosisPercentage)
	{
		this.necrosisPercentage = necrosisPercentage;
	}

	/**
     * Returns the percentage of histologically evident lymphocytes in the tissue specimen.
     * @return The percentage of histologically evident lymphocytes in the tissue specimen.
     * @see #setLymphocyticPercentage(double)
     */
	public double getLymphocyticPercentage()
	{
		return lymphocyticPercentage;
	}

	/**
     * Sets the percentage of histologically evident lymphocytes in the tissue specimen.
     * @param lymphocyticPercentage the percentage of histologically evident lymphocytes in the tissue specimen.
     * @see #getLymphocyticPercentage()
     */
	public void setLymphocyticPercentage(double lymphocyticPercentage)
	{
		this.lymphocyticPercentage = lymphocyticPercentage;
	}

	/**
     * Returns the total cellularity percentage.
     * @return The total cellularity percentage.
     * @see #setTotalCellularityPercentage(double)
     */
	public double getTotalCellularityPercentage()
	{
		return totalCellularityPercentage;
	}

	/**
     * Sets the total cellularity percentage.
     * @param totalCellularityPercentage the total cellularity percentage.
     * @see #getTotalCellularityPercentage()
     */
	public void setTotalCellularityPercentage(double totalCellularityPercentage)
	{
		this.totalCellularityPercentage = totalCellularityPercentage;
	}

	/**
     * Returns Histological Quality of the specimen. 
     * @return Histological Quality of the specimen.
     * @see #setHistologicalQuality(String)
     */
	public String getHistologicalQuality()
	{
		return histologicalQuality;
	}

	/**
     * Sets Histological Quality of the specimen.
     * @param histologicalQuality Histological Quality of the specimen.
     * @see #getHistologicalQuality()
     */
	public void setHistologicalQuality(String histologicalQuality)
	{
		this.histologicalQuality = histologicalQuality;
	}
	
	
//	 ----- SUPERCLASS METHODS
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		TissueSpecimenReviewEventParameters tissueSpecimenReviewParametersObject = (TissueSpecimenReviewEventParameters)abstractDomain ;
		
		this.neoplasticCellularityPercentage = tissueSpecimenReviewParametersObject.getNeoplasticCellularityPercentage().doubleValue() ;
		this.necrosisPercentage = tissueSpecimenReviewParametersObject.getNecrosisPercentage().doubleValue() ;
		this.lymphocyticPercentage = tissueSpecimenReviewParametersObject.getLymphocyticPercentage().doubleValue();
		this.totalCellularityPercentage =tissueSpecimenReviewParametersObject.getTotalCellularityPercentage().doubleValue();
		this.histologicalQuality = tissueSpecimenReviewParametersObject.getHistologicalQuality();
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
 
//         	// checks the neoplasticCellularityPercentage
           	if (neoplasticCellularityPercentage <= 0  || Double.isNaN(neoplasticCellularityPercentage) )
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("tissuespecimenrevieweventparameters.neoplasticcellularitypercentage")));
            }

 
//         	// checks the necrosisPercentage
           	if (necrosisPercentage <= 0  || Double.isNaN(necrosisPercentage) )
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("tissuespecimenrevieweventparameters.necrosispercentage")));
            }

 
//         	// checks the lymphocyticPercentage
           	if (lymphocyticPercentage <= 0  || Double.isNaN(lymphocyticPercentage) )
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("tissuespecimenrevieweventparameters.lymphocyticpercentage")));
            }

 
//         	// checks the totalCellularityPercentage
           	if (totalCellularityPercentage <= 0  || Double.isNaN(totalCellularityPercentage) )
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("tissuespecimenrevieweventparameters.totalcellularitypercentage")));
            }

 
//         	// checks the histologicalQuality
           	if (validator.isEmpty(histologicalQuality) )
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("tissuespecimenrevieweventparameters.histologicalquality")));
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
       
     }
	 protected void reset()
	 {
	 	super.reset();
        this.necrosisPercentage = 0.0;
        this.neoplasticCellularityPercentage = 0.0;
        this.lymphocyticPercentage = 0.0;
        this.totalCellularityPercentage = 0.0;
        this.histologicalQuality = null;

	 }

    
	
}
