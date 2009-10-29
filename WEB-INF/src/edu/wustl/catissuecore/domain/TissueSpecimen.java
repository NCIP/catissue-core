/**
 * <p>Title: TissueSpecimen Class>
 * <p>Description:  A single unit of tissue specimen
 * that is collected or created from a Participant.</p>
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
 * A single unit of tissue specimen.
 * that is collected or created from a Participant.
 * @hibernate.subclass name="TissueSpecimen" discriminator-value="Tissue"
 */
public class TissueSpecimen extends Specimen implements Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(TissueSpecimen.class);
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Default Constructor.
	 */
	public TissueSpecimen()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param form of AbstractActionForm type.
	 * @throws AssignDataException : AssignDataException
	 */
	public TissueSpecimen(AbstractActionForm form) throws AssignDataException
	{
		super();
		this.setAllValues(form);
	}

	/**
	 * This function Copies the data from an NewSpecimenForm object to a TissueSpecimen object.
	 * @param abstractForm - An IValueObject object containing the information about the site.
	 * @throws AssignDataException : AssignDataException
	 * */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			super.setAllValues(abstractForm);
		}
		catch (final Exception excp)
		{
			TissueSpecimen.logger.error(excp.getMessage(),excp);
			excp.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "TissueSpecimen.java :");
		}
	}

	/**
	 * Parameterized Constructor.
	 * @param tissueReqSpecimen of SpecimenRequirement type.
	 */
	public TissueSpecimen(SpecimenRequirement tissueReqSpecimen)
	{
		super(tissueReqSpecimen);
	}
}