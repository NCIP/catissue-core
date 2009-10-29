/**
 * <p>Title: DisposalEventParameters Class</p>
 * <p>Description: Attributes related to disposal event of a specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.DisposalEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes related to disposal event of a specimen.
 * @hibernate.joined-subclass table="CATISSUE_DISPOSAL_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class DisposalEventParameters extends SpecimenEventParameters
		implements
			java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(DisposalEventParameters.class);

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * The reason for which the specimen is disposed.
	 */
	protected String reason;

	/**
	 * activityStatus.
	 */
	protected String activityStatus;

	/**
	 * Returns the reason of disposal.
	 * @hibernate.property name="reason" type="string" column="REASON" length="255"
	 * @return reason of disposal.
	 */
	public String getReason()
	{
		return this.reason;
	}

	/**
	 * Sets the reason.
	 * @param reason reason of disposal.
	 */
	public void setReason(String reason)
	{
		this.reason = reason;
	}

	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public DisposalEventParameters()
	{
		super();
	}

	/**
	 * Parameterised constructor.
	 * @param abstractForm AbstractActionForm.
	 * @throws AssignDataException : AssignDataException
	 */
	public DisposalEventParameters(AbstractActionForm abstractForm) throws AssignDataException
	{
		super();
		this.setAllValues(abstractForm);
	}

	/**
	 * This function Copies the data from an DisposalEventParametersForm object to
	 * a DisposalEventParameters object.
	 * @param abstractForm An DisposalEventParametersForm object containing the
	 * information about the DisposalEventParameters.
	 * @throws AssignDataException : AssignDataException
	 * */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			final DisposalEventParametersForm form = (DisposalEventParametersForm) abstractForm;
			this.reason = form.getReason();
			this.activityStatus = form.getActivityStatus();
			super.setAllValues(form);
		}
		catch (final Exception excp)
		{
			DisposalEventParameters.logger.error(excp.getMessage(),excp);
			excp.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "DisposalEventParameters.java :");
		}
	}

	/**
	 * Get Activity Status.
	 * @return String.
	 */
	public String getActivityStatus()
	{
		return this.activityStatus;
	}

	/**
	 * Set Activity Status.
	 * @param activityStatus String.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}
}