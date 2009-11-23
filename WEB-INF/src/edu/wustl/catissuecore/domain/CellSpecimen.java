/**
 * <p>Title: CellSpecimen Class>
 * <p>Description:  A biospecimen composed of purified single cells not in the
 * context of a tissue or other biospecimen fluid.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

/**
 * A biospecimen composed of purified single cells not in the
 * context of a tissue or other biospecimen fluid.
 * @hibernate.subclass name="CellSpecimen" discriminator-value = "Cell"
 */
public class CellSpecimen extends Specimen implements Serializable
{

	/**
	 * Serial Version ID of the class.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(CellSpecimen.class);

	/**
	 * Default Constructor.
	 */
	public CellSpecimen()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param form AbstractActionForm.
	 * @throws AssignDataException : AssignDataException
	 */
	public CellSpecimen(final AbstractActionForm form) throws AssignDataException
	{
		super();
		this.setAllValues(form);
	}

	/**
	 * This function Copies the data from an NewSpecimenForm object to a CellSpecimen object.
	 * @param abstractForm - siteForm An SiteForm object containing the information about the site.
	 * @throws AssignDataException : AssignDataException
	 * */
	@Override
	public void setAllValues(final IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			super.setAllValues(abstractForm);
		}
		catch (final Exception excp)
		{
			CellSpecimen.logger.error(excp.getMessage(), excp);
			excp.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "CellSpecimen.java :");
		}
	}

	/**
	 * Parameterized Constructor.
	 * @param cellReqSpecimen of type SpecimenRequirement.
	 */
	public CellSpecimen(final SpecimenRequirement cellReqSpecimen)
	{
		super(cellReqSpecimen);
	}
}