/**
 * <p>Title: TissueSpecimenReviewEventParameters Class>
 * <p>Description:  Attributes associated with a review event of a tissue specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.TissueSpecimenReviewEventParametersForm;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with a review event of a tissue specimen.
 * @hibernate.joined-subclass table="CATISSUE_TIS_SPE_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author Aniruddha Phadnis
 */
public class TissueSpecimenReviewEventParameters extends ReviewEventParameters
		implements
			java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(TissueSpecimenReviewEventParameters.class);
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Percentage of histologically evident neoplastic cells present in the tissue specimen.
	 */
	protected Double neoplasticCellularityPercentage;

	/**
	 * Percentage of specimen that is histologically necrotic.
	 */
	protected Double necrosisPercentage;

	/**
	 * Percentage of histologically evident lymphocytes in the tissue specimen.
	 */
	protected Double lymphocyticPercentage;

	/**
	 * Percentage of total cellularity of the specimen. Note that TOTCELL-NEOCELL-LYMPHCELL= % cellularity
	 * of other stromal, etc. cell types. Also Note that 100-TOTCELL-NECROSIS= % of tissue containing a
	 * cellular material.
	 */
	protected Double totalCellularityPercentage;

	/**
	 * Histological Quality of the specimen.
	 */
	protected String histologicalQuality;

	/**
	 * Returns the percentage of histologically evident neoplastic cells present in the tissue specimen.
	 * @return The percentage of histologically evident neoplastic cells present in the tissue specimen.
	 * @see #setNeoplasticCellularityPercentage(Double)
	 * @hibernate.property name="neoplasticCellularityPercentage" type="double"
	 * column="NEOPLASTIC_CELLULARITY_PER" length="30"
	 */
	public Double getNeoplasticCellularityPercentage()
	{
		return this.neoplasticCellularityPercentage;
	}

	/**
	 * Sets the percentage of histologically evident neoplastic cells present in the specimen.
	 * @param neoplasticCellularityPercentage the percentage of histologically evident
	 * neoplastic cells present in the specimen.
	 * @see #getNeoplasticCellularityPercentage()
	 */
	public void setNeoplasticCellularityPercentage(Double neoplasticCellularityPercentage)
	{
		this.neoplasticCellularityPercentage = neoplasticCellularityPercentage;
	}

	/**
	 * Returns the percentage of specimen that is histologically necrotic.
	 * @return The percentage of specimen that is histologically necrotic.
	 * @see #setNecrosisPercentage(Double)
	 * @hibernate.property name="necrosisPercentage" type="double"
	 * column="NECROSIS_PERCENTAGE" length="30"
	 */
	public Double getNecrosisPercentage()
	{
		return this.necrosisPercentage;
	}

	/**
	 * Sets the percentage of specimen that is histologically necrotic.
	 * @param necrosisPercentage the percentage of specimen that is histologically necrotic.
	 * @see #getNecrosisPercentage()
	 */
	public void setNecrosisPercentage(Double necrosisPercentage)
	{
		this.necrosisPercentage = necrosisPercentage;
	}

	/**
	 * Returns the percentage of histologically evident lymphocytes in the tissue specimen.
	 * @return The percentage of histologically evident lymphocytes in the tissue specimen.
	 * @see #setLymphocyticPercentage(Double)
	 * @hibernate.property name="lymphocyticPercentage" type="double"
	 * column="LYMPHOCYTIC_PERCENTAGE" length="30"
	 */
	public Double getLymphocyticPercentage()
	{
		return this.lymphocyticPercentage;
	}

	/**
	 * Sets the percentage of histologically evident lymphocytes in the tissue specimen.
	 * @param lymphocyticPercentage the percentage of histologically evident
	 * lymphocytes in the tissue specimen.
	 * @see #getLymphocyticPercentage()
	 */
	public void setLymphocyticPercentage(Double lymphocyticPercentage)
	{
		this.lymphocyticPercentage = lymphocyticPercentage;
	}

	/**
	 * Returns the total cellularity percentage.
	 * @return The total cellularity percentage.
	 * @see #setTotalCellularityPercentage(Double)
	 * @hibernate.property name="totalCellularityPercentage" type="double"
	 * column="TOTAL_CELLULARITY_PERCENTAGE" length="30"
	 */
	public Double getTotalCellularityPercentage()
	{
		return this.totalCellularityPercentage;
	}

	/**
	 * Sets the total cellularity percentage.
	 * @param totalCellularityPercentage the total cellularity percentage.
	 * @see #getTotalCellularityPercentage()
	 */
	public void setTotalCellularityPercentage(Double totalCellularityPercentage)
	{
		this.totalCellularityPercentage = totalCellularityPercentage;
	}

	/**
	 * Returns Histological Quality of the specimen.
	 * @return Histological Quality of the specimen.
	 * @see #setHistologicalQuality(String)
	 * @hibernate.property name="histologicalQuality" type="string"
	 * column="HISTOLOGICAL_QUALITY" length="50"
	 */
	public String getHistologicalQuality()
	{
		return this.histologicalQuality;
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

	/**
	 * Default Constructor.
	 */
	public TissueSpecimenReviewEventParameters()
	{
		super();
	}

	/**
	 * Parameterized constructor.
	 * @param abstractForm of AbstractActionForm type.
	 * @throws AssignDataException : AssignDataException
	 */
	public TissueSpecimenReviewEventParameters(AbstractActionForm abstractForm)
			throws AssignDataException
	{
		super();
		this.setAllValues(abstractForm);
	}

	/**
	 * This function Copies the data from an TissueSpecimenReviewEventParametersForm
	 * object to a TissueSpecimenReviewEventParameters object.
	 * @param abstractForm - tissueSpecimenReviewEventParametersForm AnTissueSpecimenReviewEventParametersForm
	 * object containing the information about the TissueSpecimenReviewEventParameters.
	 * @throws AssignDataException : AssignDataException
	 */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		final String nullString = null;
		try
		{
			final TissueSpecimenReviewEventParametersForm form = (TissueSpecimenReviewEventParametersForm) abstractForm;

			if (form.getNeoplasticCellularityPercentage() != null
					&& form.getNeoplasticCellularityPercentage().trim().length() > 0)
			{
				this.neoplasticCellularityPercentage = new Double(form
						.getNeoplasticCellularityPercentage());
			}
			if (form.getNecrosisPercentage() != null
					&& form.getNecrosisPercentage().trim().length() > 0)
			{
				this.necrosisPercentage = new Double(form.getNecrosisPercentage());
			}
			if (form.getTotalCellularityPercentage() != null
					&& form.getTotalCellularityPercentage().trim().length() > 0)
			{
				this.totalCellularityPercentage = new Double(form.getTotalCellularityPercentage());
			}
			if (form.getLymphocyticPercentage() != null
					&& form.getLymphocyticPercentage().trim().length() > 0)
			{
				this.lymphocyticPercentage = new Double(form.getLymphocyticPercentage());
			}

			final Validator validator = new Validator();
			if (validator.isValidOption(form.getHistologicalQuality()))
			{
				this.histologicalQuality = form.getHistologicalQuality();
			}
			else
			{
				this.histologicalQuality = nullString; //Purposefully set null for Edit Mode
			}

			super.setAllValues(form);
		}
		catch (final Exception excp)
		{
			TissueSpecimenReviewEventParameters.logger.error(excp.getMessage(),excp);
			excp.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null,
					"TissueSpecimenReviewEventParameters.java :");
		}
	}
	/**
	 * Do the round off for the required attributes (if any)
	 */
	@Override
	public void doRoundOff() {
		if (neoplasticCellularityPercentage != null) {
			neoplasticCellularityPercentage = AppUtility.RoundOff(neoplasticCellularityPercentage, 5);
		}
		if (necrosisPercentage != null) {
			necrosisPercentage = AppUtility.RoundOff(necrosisPercentage, 5);
		}
		if (lymphocyticPercentage != null) {
			lymphocyticPercentage = AppUtility.RoundOff(lymphocyticPercentage, 5);
		}
		if (totalCellularityPercentage != null) {
			totalCellularityPercentage = AppUtility.RoundOff(totalCellularityPercentage, 5);
		}
	}
}