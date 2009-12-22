/**
 * <p>Title: CellSpecimenReviewParameters Class>
 * <p>Description:  Attributes associated with a review event of a cell specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.CellSpecimenReviewParametersForm;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with a review event of a cell specimen.
 * @hibernate.joined-subclass table="CATISSUE_CELL_SPE_REVIEW_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author Aniruddha Phadnis
 */
public class CellSpecimenReviewParameters extends ReviewEventParameters
		implements
			java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(CellSpecimenReviewParameters.class);
	/**
	 * Serial Version Id for the class.
	 */
	private static final long serialVersionUID = 1234567890L;
	/**
	 * Percentage of histologically evident neoplastic cells present in the specimen.
	 */
	protected Double neoplasticCellularityPercentage;
	/**
	 * Percentage of viable cells in the specimen.
	 */
	protected Double viableCellPercentage;

	/**
	 * Returns the percentage of histologically evident neoplastic cells present in the specimen.
	 * @return The percentage of histologically evident neoplastic cells present in the specimen.
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
	 * @param neoplasticCellularityPercentage the percentage of histologically evident neoplastic
	 * cells present in the specimen.
	 * @see #getNeoplasticCellularityPercentage()
	 */
	public void setNeoplasticCellularityPercentage(Double neoplasticCellularityPercentage)
	{
		this.neoplasticCellularityPercentage = neoplasticCellularityPercentage;
	}

	/**
	 * Returns the percentage of viable cells in the specimen.
	 * @return The percentage of viable cells in the specimen.
	 * @see #setViableCellPercentage(Double)
	 * @hibernate.property name="viableCellPercentage" type="double"
	 * column="VIABLE_CELL_PERCENTAGE" length="30"
	 */
	public Double getViableCellPercentage()
	{
		return this.viableCellPercentage;
	}

	/**
	 * Sets the percentage of viable cells in the specimen.
	 * @param viableCellPercentage the percentage of viable cells in the specimen.
	 * @see #getViableCellPercentage()
	 */
	public void setViableCellPercentage(Double viableCellPercentage)
	{
		this.viableCellPercentage = viableCellPercentage;
	}

	/**
	 * Default Constructor.
	 */
	public CellSpecimenReviewParameters()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param abstractForm AbstractActionForm.
	 */
	public CellSpecimenReviewParameters(AbstractActionForm abstractForm)
	{
		super();
		this.setAllValues(abstractForm);
	}

	/**
	 * This function Copies the data from an CellSpecimenReviewParametersForm
	 * object to a CellSpecimenReviewParameters object.
	 * @param abstractForm - cellSpecimenReviewParametersForm An CellSpecimenReviewParametersForm
	 * object containing the information about the cellSpecimenReviewParameters.
	 */
	@Override
	public void setAllValues(IValueObject abstractForm)
	{
		try
		{
			final CellSpecimenReviewParametersForm form = (CellSpecimenReviewParametersForm) abstractForm;
			this.setNeoplasticCellularityPercentage(form);
			if (form.getViableCellPercentage() != null
					&& form.getViableCellPercentage().trim().length() > 0)
			{
				this.viableCellPercentage = new Double(form.getViableCellPercentage());
			}
			super.setAllValues(form);
		}
		catch (final Exception excp)
		{
			CellSpecimenReviewParameters.logger.error(excp.getMessage(),excp);
			excp.printStackTrace();
		}
	}

	/**
	 * Compares the NeoplasticCellularityPercentage data.
	 * @param form CellSpecimenReviewParametersForm.
	 */
	private void setNeoplasticCellularityPercentage(CellSpecimenReviewParametersForm form)
	{
		if (form.getNeoplasticCellularityPercentage() != null
				&& form.getNeoplasticCellularityPercentage().trim().length() > 0)
		{
			this.neoplasticCellularityPercentage = new Double(form
					.getNeoplasticCellularityPercentage());
		}
	}
	
	/**
	 * Do the round off for the required attributes (if any)
	 */
	@Override
	public void doRoundOff() {
		if (neoplasticCellularityPercentage != null) {
			neoplasticCellularityPercentage = AppUtility.RoundOff(neoplasticCellularityPercentage, 2);
		}
		if (viableCellPercentage != null) {
			viableCellPercentage = AppUtility.RoundOff(viableCellPercentage, 2);
		}
	}
}