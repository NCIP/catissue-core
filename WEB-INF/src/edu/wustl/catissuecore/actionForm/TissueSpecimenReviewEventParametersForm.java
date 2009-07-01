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
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh
 * This Class handles the Tissue Specimen Review event parameters.
 */
public class TissueSpecimenReviewEventParametersForm extends SpecimenEventParametersForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger
			.getLogger(TissueSpecimenReviewEventParametersForm.class);
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
	 * Name : Virender Mehta
	 * Reviewer: Sachin Lale
	 * Bug ID: defaultValueConfiguration_BugID
	 * Patch ID:defaultValueConfiguration_BugID_16
	 * Description: Configuration for default value for Histological Quality
	 */
	/**
	 * Histological Quality of the specimen.
	 */
	protected String histologicalQuality = (String) DefaultValueManager
			.getDefaultValue(Constants.DEFAULT_HISTOLOGICAL_QUALITY);

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
	/**
	 * @return TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID
	 */
	public int getFormId()
	{
		return Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID;
	}

	/**
	 * Populates all the fields from the domain object to the form bean.
	 * @param abstractDomain An AbstractDomain Object  
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		TissueSpecimenReviewEventParameters tissueSpecimenReviewParametersObject = (TissueSpecimenReviewEventParameters) abstractDomain;

		this.neoplasticCellularityPercentage = Utility
				.toString(tissueSpecimenReviewParametersObject.getNeoplasticCellularityPercentage());
		this.necrosisPercentage = Utility.toString(tissueSpecimenReviewParametersObject
				.getNecrosisPercentage());
		this.lymphocyticPercentage = Utility.toString(tissueSpecimenReviewParametersObject
				.getLymphocyticPercentage());
		this.totalCellularityPercentage = Utility.toString(tissueSpecimenReviewParametersObject
				.getTotalCellularityPercentage());
		this.histologicalQuality = Utility.toString(tissueSpecimenReviewParametersObject
				.getHistologicalQuality());
		logger.debug("this.neoplasticCellularityPercentage : "
				+ this.neoplasticCellularityPercentage);
		logger.debug("this.necrosisPercentage : " + this.necrosisPercentage);
		logger.debug("this.lymphocyticPercentage : " + this.lymphocyticPercentage);
		logger.debug("this.totalCellularityPercentage : " + this.totalCellularityPercentage);
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

			//         	// checks the neoplasticCellularityPercentage
			if (!validator.isEmpty(neoplasticCellularityPercentage)
					&& !validator.isDouble(neoplasticCellularityPercentage, false))
			{
				errors
						.add(
								ActionErrors.GLOBAL_ERROR,
								new ActionError(
										"errors.item.format",
										ApplicationProperties
												.getValue("tissuespecimenrevieweventparameters.neoplasticcellularitypercentage")));
			}
			//

			//         	// checks the necrosisPercentage
			if (!validator.isEmpty(necrosisPercentage)
					&& !validator.isDouble(necrosisPercentage, false))
			{
				errors
						.add(
								ActionErrors.GLOBAL_ERROR,
								new ActionError(
										"errors.item.format",
										ApplicationProperties
												.getValue("tissuespecimenrevieweventparameters.necrosispercentage")));
			}
			//

			//         	// checks the lymphocyticPercentage
			if (!validator.isEmpty(lymphocyticPercentage)
					&& !validator.isDouble(lymphocyticPercentage, false))

			{
				errors
						.add(
								ActionErrors.GLOBAL_ERROR,
								new ActionError(
										"errors.item.format",
										ApplicationProperties
												.getValue("tissuespecimenrevieweventparameters.lymphocyticpercentage")));
			}
			//

			//         	// checks the totalCellularityPercentage
			if (!validator.isEmpty(totalCellularityPercentage)
					&& !validator.isDouble(totalCellularityPercentage, false))
			{
				errors
						.add(
								ActionErrors.GLOBAL_ERROR,
								new ActionError(
										"errors.item.format",
										ApplicationProperties
												.getValue("tissuespecimenrevieweventparameters.totalcellularitypercentage")));
			}
			//

			//         	// checks the histologicalQuality
			//           	if (!validator.isValidOption(histologicalQuality) )
			//            {
			//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("tissuespecimenrevieweventparameters.histologicalquality")));
			//            }
			//        
		}
		catch (Exception excp)
		{
			logger.error(excp.getMessage());
		}
		return errors;
	}

	/**
	 * Resets the values of all the fields.
	 */
	protected void reset()
	{
		//	 	super.reset();
		//        this.necrosisPercentage = null;
		//        this.neoplasticCellularityPercentage = null;
		//        this.lymphocyticPercentage = null;
		//        this.totalCellularityPercentage = null;
		//        this.histologicalQuality = null;

	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}
