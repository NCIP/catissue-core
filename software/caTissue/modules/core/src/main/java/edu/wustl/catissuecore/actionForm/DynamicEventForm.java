package edu.wustl.catissuecore.actionForm;

import java.util.Calendar;

import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;

public class DynamicEventForm extends AbstractActionForm{

	private long userId;

	private String eventName;

	private String reasonDeviation;
	/** Time in hours for the Event Parameter. */
	protected String timeInHours;

	/** Time in minutes for the Event Parameter. */
	protected String timeInMinutes;

	/** Date of the Event Parameter. */
	protected String dateOfEvent;
	private int formId;

	private long recordEntry;
	private long recordIdentifier;
	private long contId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(final long userId) {
		this.userId = userId;
	}

	public String getReasonDeviation() {
		return reasonDeviation;
	}

	public void setReasonDeviation(final String reasonDeviation) {
		this.reasonDeviation = reasonDeviation;
	}

	@Override
	public int getFormId() {
		// TODO Auto-generated method stub
		return Constants.ACTION_APP_FORM_ID;
	}

	public void setFormId(int formId) {
		this.formId = formId;
	}

	@Override
	protected void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAddNewObjectIdentifier(final String arg0, final Long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAllValues(final AbstractDomainObject abstractDomainObject) {
		final ActionApplication actionApp = (ActionApplication) abstractDomainObject;
		this.userId=actionApp.getPerformedBy().getId();
		this.reasonDeviation=actionApp.getReasonDeviation();

		final Calendar calender = Calendar.getInstance();
		if (actionApp.getTimestamp() != null)
		{
			calender.setTime(actionApp.getTimestamp());
			this.timeInHours = CommonUtilities.toString(Integer.toString(calender
					.get(Calendar.HOUR_OF_DAY)));
			this.timeInMinutes = CommonUtilities.toString(Integer.toString(calender
					.get(Calendar.MINUTE)));
			this.dateOfEvent = CommonUtilities.parseDateToString(actionApp
					.getTimestamp(), CommonServiceLocator.getInstance().getDatePattern());
		}
		this.recordEntry=actionApp.getApplicationRecordEntry().getId();
		this.contId=actionApp.getApplicationRecordEntry().getFormContext().getContainerId();
	}

	public String getTimeInHours() {
		return timeInHours;
	}

	public void setTimeInHours(String timeInHours) {
		this.timeInHours = timeInHours;
	}

	public String getTimeInMinutes() {
		return timeInMinutes;
	}

	public void setTimeInMinutes(String timeInMinutes) {
		this.timeInMinutes = timeInMinutes;
	}

	public String getDateOfEvent() {
		return dateOfEvent;
	}

	public void setDateOfEvent(String dateOfEvent) {
		this.dateOfEvent = dateOfEvent;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public long getRecordEntry() {
		return recordEntry;
	}

	public void setRecordEntry(long recordEntry) {
		this.recordEntry = recordEntry;
	}

	public long getContId() {
		return contId;
	}

	public void setContId(long contId) {
		this.contId = contId;
	}

	public long getRecordIdentifier() {
		return recordIdentifier;
	}

	public void setRecordIdentifier(long recordIdentifier) {
		this.recordIdentifier = recordIdentifier;
	}


}
