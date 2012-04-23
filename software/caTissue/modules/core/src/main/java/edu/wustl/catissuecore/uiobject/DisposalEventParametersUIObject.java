package edu.wustl.catissuecore.uiobject;

import edu.wustl.common.domain.UIObject;

public class DisposalEventParametersUIObject implements UIObject
{
	/**
	 * activityStatus.
	 */
	protected String activityStatus;

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
	
	protected boolean createEvent=Boolean.TRUE;

	
	public boolean getCreateEvent()
	{
		return createEvent;
	}

	
	public void setCreateEvent(boolean createEvent)
	{
		this.createEvent = createEvent;
	}
}
