/**
 * <p>Title: ThawEventParameters Class>
 * <p>Description:  Attributes associated with a thawing event of a specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.ThawEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with a thawing event of a specimen.
 * @hibernate.joined-subclass table="CATISSUE_THAW_EVENT_PARAMETERS"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author Aniruddha Phadnis
 */
public class ThawEventParameters extends SpecimenEventParameters implements java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(ThawEventParameters.class);
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Default Constructor.
	 * NOTE: Do not delete this constructor. Hibernate uses this by reflection API.
	 */
	public ThawEventParameters()
	{
		super();
	}

	/**
	 *  Parameterised constructor.
	 * @param abstractForm of AbstractActionForm type.
	 * @throws AssignDataException : AssignDataException
	 */
	public ThawEventParameters(AbstractActionForm abstractForm) throws AssignDataException
	{
		super();
		this.setAllValues(abstractForm);
	}

	/**
	 * This function Copies the data from an ThawEventParameters object.
	 * @param abstractForm - ThawEventParametersForm An ThawEventParametersForm object
	 * containing the information about the ThawEventParameters.
	 * @throws AssignDataException : AssignDataException
	 * */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			final ThawEventParametersForm form = (ThawEventParametersForm) abstractForm;

			super.setAllValues(form);
		}
		catch (final Exception excp)
		{
			logger.error(excp.getMessage());
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "ThawEventParameters.java :");
		}
	}
}