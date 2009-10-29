/**
 * <p>Title: EventParametersForm Class</p>
 * <p>Description:  This Class will be the super class for all Event Parameter classes.
 * <p> It contains the common attributes of the Event Parameter classes.   
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 28th, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.util.MultipleSpecimenValidationUtil;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh
 *  This Class will be the super class for all Event Parameter classes.
 */
public abstract class EventParametersForm extends AbstractActionForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7656338598031229913L;
	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(EventParametersForm.class);
	/**
	 * Time in hours for the Event Parameter.
	 * */
	protected String timeInHours;

	/**
	 * Time in minutes for the Event Parameter.
	 * */
	protected String timeInMinutes;

	/**
	 * Date of the Event Parameter.
	 * */
	protected String dateOfEvent;

	/**
	 * Id of the User.   
	 */
	protected long userId;

	/**
	 * Comments on the event parameter.   
	 */
	protected String comments;

	// ----- variable declaration end

	// ------ GET SET methods
	/**
	 * @return Returns the comments.
	 */
	public String getComments()
	{
		return this.comments;
	}

	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
	}

	/**
	 * @return Returns the dateOfEvent.
	 */
	public String getDateOfEvent()
	{
		return this.dateOfEvent;
	}

	/**
	 * @param dateOfEvent The dateOfEvent to set.
	 */
	public void setDateOfEvent(String dateOfEvent)
	{
		this.dateOfEvent = dateOfEvent;
	}

	/**
	 * @return Returns the time_InMinutes.
	 */
	public String getTimeInMinutes()
	{
		return this.timeInMinutes;
	}

	/**
	 * @param timeInMinutes The time_InMinutes to set.
	 */
	public void setTimeInMinutes(String timeInMinutes)
	{
		this.timeInMinutes = timeInMinutes;
	}

	/**
	 * @return Returns the timeStamp.
	 */
	public String getTimeInHours()
	{
		return this.timeInHours;
	}

	/**
	 * @param timeStamp The timeStamp to set.
	 */
	public void setTimeInHours(String timeStamp)
	{
		this.timeInHours = timeStamp;
	}

	/**
	 * @return Returns the userId.
	 */
	public long getUserId()
	{
		return this.userId;
	}

	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	//--------  Super class Methods
	/**
	 * Resets the values of all the fields.
	 */
	@Override
	protected void reset()
	{
	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{

		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();

		try
		{
			MultipleSpecimenValidationUtil.validateDate(errors, validator, this.userId,
					this.dateOfEvent, this.timeInHours, this.timeInMinutes);
		}
		catch (final Exception excp)
		{
			EventParametersForm.logger.error(excp.getMessage(),excp);
			excp.printStackTrace() ;
		}
		return errors;
	}

	/** 
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 * @param abstractDomain An AbstractDomainObject obj
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		final SpecimenEventParameters eventParametersObject = (SpecimenEventParameters) abstractDomain;
		this.comments = CommonUtilities.toString(eventParametersObject.getComment());
		this.setId(eventParametersObject.getId().longValue());

		final Calendar calender = Calendar.getInstance();
		if (eventParametersObject.getTimestamp() != null)
		{
			calender.setTime(eventParametersObject.getTimestamp());
			this.timeInHours = CommonUtilities.toString(Integer.toString(calender
					.get(Calendar.HOUR_OF_DAY)));
			this.timeInMinutes = CommonUtilities.toString(Integer.toString(calender
					.get(Calendar.MINUTE)));
			this.dateOfEvent = CommonUtilities.parseDateToString(eventParametersObject
					.getTimestamp(), CommonServiceLocator.getInstance().getDatePattern());
		}
		this.userId = eventParametersObject.getUser().getId().longValue();
		//this.dateOfEvent = (calender.get(Calendar.MONTH)+1)+"-"+calender.get(Calendar.DAY_OF_MONTH)+"-"+calender.get(Calendar.YEAR) ;
		logger.debug("id:" + this.getId() + " timeInHours:" + this.timeInHours + " timeInMinutes:"
				+ this.timeInMinutes + " userId:" + this.userId + " dateOfEvent:"
				+ this.dateOfEvent);
	}

}