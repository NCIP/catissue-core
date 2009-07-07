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
	private static org.apache.log4j.Logger logger = Logger.getLogger(DisposalEventParameters.class);

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
	 */
	public DisposalEventParameters(AbstractActionForm abstractForm)
	{
		super();
		this.setAllValues(abstractForm);
	}

	/**
	 * This function Copies the data from an DisposalEventParametersForm object to
	 * a DisposalEventParameters object.
	 * @param abstractForm An DisposalEventParametersForm object containing the
	 * information about the DisposalEventParameters.
	 * */
	@Override
	public void setAllValues(IValueObject abstractForm)
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
			logger.error(excp.getMessage());
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