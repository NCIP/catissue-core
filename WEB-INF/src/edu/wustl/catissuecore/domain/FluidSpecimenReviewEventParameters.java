/**
 * <p>Title: FluidSpecimenReviewEventParameters Class
 * <p>Description:  Attributes associated with a review event of a fluid specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.FluidSpecimenReviewEventParametersForm;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with a review event of a fluid specimen.
 * @hibernate.joined-subclass table="CATISSUE_FLUID_SPE_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author Aniruddha Phadnis
 */
public class FluidSpecimenReviewEventParameters extends ReviewEventParameters
		implements
			java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(FluidSpecimenReviewEventParameters.class);

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Cell Count.
	 */
	protected Double cellCount;

	/**
	 * Returns the cell count.
	 * @return The cell count.
	 * @see #setCellCount(Double)
	 * @hibernate.property name="cellCount" type="double"
	 * column="CELL_COUNT" length="30"
	 */
	public Double getCellCount()
	{
		return this.cellCount;
	}

	/**
	 * Sets the cell count.
	 * @param cellCount the cell count.
	 * @see #getCellCount()
	 */
	public void setCellCount(Double cellCount)
	{
		this.cellCount = cellCount;
	}

	/**
	 * Default Constructor.
	 * NOTE: Do not delete this constructor. Hibernate uses this by reflection API.
	 */
	public FluidSpecimenReviewEventParameters()
	{
		super();
	}

	/**
	 * Parameterized constructor.
	 * @param abstractForm AbstractActionForm.
	 * @throws AssignDataException : AssignDataException
	 */
	public FluidSpecimenReviewEventParameters(AbstractActionForm abstractForm)
			throws AssignDataException
	{
		super();
		this.setAllValues(abstractForm);
	}

	/**
	 * This function Copies the data from an FluidSpecimenReviewEventParametersForm
	 * object to a FluidSpecimenReviewEventParameters object.
	 * @param abstractForm An FluidSpecimenReviewEventParametersForm object
	 * containing the information about the fluidSpecimenReviewEventParameters.
	 * @throws AssignDataException : AssignDataException
	 * */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			final FluidSpecimenReviewEventParametersForm form = (FluidSpecimenReviewEventParametersForm) abstractForm;
			logger.debug("############DomainObject################## : ");
			logger.debug(form.getCellCount());
			logger.debug("############################## ");
			if (form.getCellCount() != null && form.getCellCount().trim().length() > 0)
			{
				this.cellCount = new Double(form.getCellCount());
			}
			super.setAllValues(form);
		}
		catch (final Exception excp)
		{
			FluidSpecimenReviewEventParameters.logger.error(excp.getMessage(),excp);
			excp.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null,
					"FluidSpecimenReviewEventParameters.java :");
		}
	}

	/**
	 * Do the round off for the required attributes (if any)
	 */
	@Override
	public void doRoundOff() {
		if (cellCount != null) {
			cellCount = AppUtility.RoundOff(cellCount, 5);
		}
	}
}