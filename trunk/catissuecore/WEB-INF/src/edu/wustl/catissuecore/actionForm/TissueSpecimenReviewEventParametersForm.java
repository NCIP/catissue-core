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

import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


/**
 * @author mandar_deshmukh
 * This Class handles the Tissue Specimen Review event parameters.
 */
public class TissueSpecimenReviewEventParametersForm extends SpecimenEventParametersForm
{
	/**
     * Percentage of histologically evident neoplastic cells present in the tissue specimen.
     */
	protected String neoplasticCellularityPercentage;
	
	/**
     * Percentage of specimen that is histologically necrotic.
     */
	protected String necrosisPercentage;
	
	/**
     * Percentage of histologically evident lymphocytes in the tissue specimen.
     */
	protected String lymphocyticPercentage;
	
	/**
     * Percentage of total cellularity of the specimen.  Note that TOTCELL-NEOCELL-LYMPHCELL= % cellularity 
     * of other stromal, etc. cell types.  Also Note that 100-TOTCELL-NECROSIS= % of tissue containing a 
     * cellular material.
     */
	protected String totalCellularityPercentage;
	
	/**
     * Histological Quality of the specimen.
     */
	protected String histologicalQuality = Constants.NOTSPECIFIED;

	/**
     * Returns the percentage of histologically evident neoplastic cells present in the tissue specimen. 
     * @return The percentage of histologically evident neoplastic cells present in the tissue specimen.
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
     * Returns the percentage of specimen that is histologically necrotic. 
     * @return The percentage of specimen that is histologically necrotic.
     * @see #setNecrosisPercentage(double)
     */
	public String getNecrosisPercentage()
	{
		return necrosisPercentage;
	}

	/**
     * Sets the percentage of specimen that is histologically necrotic.
     * @param necrosisPercentage the percentage of specimen that is histologically necrotic.
     * @see #getNecrosisPercentage()
     */
	public void setNecrosisPercentage(String necrosisPercentage)
	{
		this.necrosisPercentage = necrosisPercentage;
	}

	/**
     * Returns the percentage of histologically evident lymphocytes in the tissue specimen.
     * @return The percentage of histologically evident lymphocytes in the tissue specimen.
     * @see #setLymphocyticPercentage(double)
     */
	public String getLymphocyticPercentage()
	{
		return lymphocyticPercentage;
	}

	/**
     * Sets the percentage of histologically evident lymphocytes in the tissue specimen.
     * @param lymphocyticPercentage the percentage of histologically evident lymphocytes in the tissue specimen.
     * @see #getLymphocyticPercentage()
     */
	public void setLymphocyticPercentage(String lymphocyticPercentage)
	{
		this.lymphocyticPercentage = lymphocyticPercentage;
	}

	/**
     * Returns the total cellularity percentage.
     * @return The total cellularity percentage.
     * @see #setTotalCellularityPercentage(double)
     */
	public String getTotalCellularityPercentage()
	{
		return totalCellularityPercentage;
	}

	/**
     * Sets the total cellularity percentage.
     * @param totalCellularityPercentage the total cellularity percentage.
     * @see #getTotalCellularityPercentage()
     */
	public void setTotalCellularityPercentage(String totalCellularityPercentage)
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
		
		this.neoplasticCellularityPercentage = Utility.toString(tissueSpecimenReviewParametersObject.getNeoplasticCellularityPercentage() );
		this.necrosisPercentage = Utility.toString( tissueSpecimenReviewParametersObject.getNecrosisPercentage() );
		this.lymphocyticPercentage =Utility.toString( tissueSpecimenReviewParametersObject.getLymphocyticPercentage());
		this.totalCellularityPercentage =Utility.toString( tissueSpecimenReviewParametersObject.getTotalCellularityPercentage());
		this.histologicalQuality = Utility.toString(tissueSpecimenReviewParametersObject.getHistologicalQuality());
		Logger.out.debug("this.neoplasticCellularityPercentage : "+ this.neoplasticCellularityPercentage );
		Logger.out.debug("this.necrosisPercentage : " + this.necrosisPercentage);
		Logger.out.debug("this.lymphocyticPercentage : " + this.lymphocyticPercentage);
		Logger.out.debug("this.totalCellularityPercentage : " + this.totalCellularityPercentage);
						
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
           	if (!validator.isEmpty( neoplasticCellularityPercentage) && !validator.isDouble( neoplasticCellularityPercentage,false ) )
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("tissuespecimenrevieweventparameters.neoplasticcellularitypercentage")));
            }
//
 
//         	// checks the necrosisPercentage
           	if (!validator.isEmpty(necrosisPercentage) && !validator.isDouble(necrosisPercentage,false) )
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("tissuespecimenrevieweventparameters.necrosispercentage")));
            }
//
 
//         	// checks the lymphocyticPercentage
           	if (!validator.isEmpty(lymphocyticPercentage) && !validator.isDouble(lymphocyticPercentage,false) )

            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("tissuespecimenrevieweventparameters.lymphocyticpercentage")));
            }
//
 
//         	// checks the totalCellularityPercentage
       		if (!validator.isEmpty(totalCellularityPercentage) && !validator.isDouble(totalCellularityPercentage,false) )
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("tissuespecimenrevieweventparameters.totalcellularitypercentage")));
            }
//
 
//         	// checks the histologicalQuality
//           	if (!validator.isValidOption(histologicalQuality) )
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("tissuespecimenrevieweventparameters.histologicalquality")));
//            }
//        
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
//        this.necrosisPercentage = null;
//        this.neoplasticCellularityPercentage = null;
//        this.lymphocyticPercentage = null;
//        this.totalCellularityPercentage = null;
//        this.histologicalQuality = null;

	 }

    
	
}
