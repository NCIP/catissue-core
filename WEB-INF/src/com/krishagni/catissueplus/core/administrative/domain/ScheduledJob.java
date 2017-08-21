package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.administrative.domain.factory.ScheduledJobErrorCode;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.administrative.services.impl.ExternalScheduledTask;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

public class ScheduledJob extends BaseEntity {
	private static final Log logger = LogFactory.getLog(ScheduledJob.class);

	public enum RepeatSchedule { 
		MINUTELY,
		HOURLY,
		DAILY,
		WEEKLY,
		MONTHLY,
		ONDEMAND
	}
	
	public enum DayOfWeek {
		SUNDAY(1),
		MONDAY(2),
		TUESDAY(3),
		WEDNESDAY(4),
		THURSDAY(5),
		FRIDAY(6),
		SATRUDAY(7);
		
		private final int value;
		
		private DayOfWeek(int newValue) {
			value = newValue;
		}
		
		public int value() {
			return value;
		}
	}
	
	public enum Type {
		INTERNAL,
		EXTERNAL
	}
	
	private String name;
	
	private User createdBy;
	
	private Date startDate;
	
	private Date endDate;
	
	private Integer scheduledHour;
	
	private Integer scheduledMinute;
	
	private DayOfWeek scheduledDayOfWeek;
	
	private Integer scheduledDayOfMonth;
	
	private String activityStatus;
	
	private RepeatSchedule repeatSchedule;
	
	private Type type;
	
	private String taskImplfqn;
	
	private String command;
	
	private Set<User> recipients = new HashSet<User>();
	
	//
	// UI purpose
	//
	private Boolean rtArgsProvided;
	
	private String rtArgsHelpText;
		
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getScheduledHour() {
		return scheduledHour;
	}

	public void setScheduledHour(Integer scheduledHour) {
		this.scheduledHour = scheduledHour;
	}

	public Integer getScheduledMinute() {
		return scheduledMinute;
	}

	public void setScheduledMinute(Integer scheduledMinute) {
		this.scheduledMinute = scheduledMinute;
	}

	public DayOfWeek getScheduledDayOfWeek() {
		return scheduledDayOfWeek;
	}

	public void setScheduledDayOfWeek(DayOfWeek scheduledDayOfWeek) {
		this.scheduledDayOfWeek = scheduledDayOfWeek;
	}

	public Integer getScheduledDayOfMonth() {
		return scheduledDayOfMonth;
	}

	public void setScheduledDayOfMonth(Integer scheduledDayOfMonth) {
		this.scheduledDayOfMonth = scheduledDayOfMonth;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public RepeatSchedule getRepeatSchedule() {
		return repeatSchedule;
	}

	public void setRepeatSchedule(RepeatSchedule repeatSchedule) {
		this.repeatSchedule = repeatSchedule;
	}


	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTaskImplfqn() {
		return taskImplfqn;
	}

	public void setTaskImplfqn(String fqn) {
		this.taskImplfqn = fqn;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Set<User> getRecipients() {
		return recipients;
	}

	public void setRecipients(Set<User> recipients) {
		this.recipients = recipients;
	}
	
	public Boolean getRtArgsProvided() {
		return rtArgsProvided;
	}

	public void setRtArgsProvided(Boolean rtArgsProvided) {
		this.rtArgsProvided = rtArgsProvided;
	}

	public String getRtArgsHelpText() {
		return rtArgsHelpText;
	}

	public void setRtArgsHelpText(String rtArgsHelpText) {
		this.rtArgsHelpText = rtArgsHelpText;
	}

	public void update(ScheduledJob other) {
		BeanUtils.copyProperties(other, this, JOB_UPDATE_IGN_PROPS);
		CollectionUpdater.update(this.getRecipients(), other.getRecipients());
	}

	public boolean isOnDemand() {
		return repeatSchedule.equals(RepeatSchedule.ONDEMAND);
	}
	
	public boolean isActiveJob() {
		if (!Status.ACTIVITY_STATUS_ACTIVE.getStatus().equals(activityStatus)) {
			return false;
		}

		if (isOnDemand()) {
			return true;
		}
		
		Date current = new Date();
		if (current.before(startDate)) {
			return false;
		}
		
		if (endDate != null && current.after(endDate)) {
			return false;
		}
		
		return true;
	}

	public Date getNextRunOn() {
		switch (repeatSchedule) {
			case MINUTELY:
				return getNextMinutelyOccurence();
				
			case HOURLY:
				return getNextHourlyOccurence();
			
			case DAILY:
				return getNextDailyOccurence();

			case MONTHLY:
				return getNextMonthlyOccurence();
			
			case WEEKLY:
				return getNextWeeklyOccurence();
				
			default:
				return null;
		}
	}
	
	public void delete() {
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
		setName(Utility.getDisabledValue(name, 255));
	}
	
	private Date getNextMinutelyOccurence() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 1);
		return calendar.getTime();
	}
	
	private Date getNextHourlyOccurence() {
		Calendar calendar = Calendar.getInstance();
		int minutes = calendar.get(Calendar.MINUTE);
		if (minutes >= scheduledMinute) {
			calendar.add(Calendar.HOUR, 1);
		} 
		
		calendar.set(Calendar.MINUTE, scheduledMinute);
		return calendar.getTime();
	}
	
	private Date getNextDailyOccurence() {
		Calendar current = Calendar.getInstance();
		if (isTimeAfterScheduledTime(current)) {
			current.add(Calendar.DATE, 1);
		}
		
		current.set(Calendar.HOUR_OF_DAY, scheduledHour);
		current.set(Calendar.MINUTE, scheduledMinute);
		return current.getTime();
	}
	
	private Date getNextWeeklyOccurence() {
		int startDayOfWeek = scheduledDayOfWeek.value();
		Calendar current = Calendar.getInstance();
		int currentDayOfWeek = current.get(Calendar.DAY_OF_WEEK);
		
		if (currentDayOfWeek < startDayOfWeek) {
			int diff = startDayOfWeek - currentDayOfWeek;
			current.add(Calendar.DATE, diff);
		} else if (currentDayOfWeek == startDayOfWeek) {
			if (isTimeAfterScheduledTime(current)) {
				current.add(Calendar.DATE, 7);
			}
		} else {
			int diff = (7-currentDayOfWeek) + startDayOfWeek;
			current.add(Calendar.DATE, diff);
		}
		
		current.set(Calendar.HOUR_OF_DAY, scheduledHour);
		current.set(Calendar.MINUTE, scheduledMinute);
		return current.getTime();
	}
	
	private Date getNextMonthlyOccurence() {
		Calendar current = Calendar.getInstance();
		int currentDay = current.get(Calendar.DAY_OF_MONTH);
		
		if (currentDay < scheduledDayOfMonth) {
			int diff = scheduledDayOfMonth.intValue() - currentDay;
			current.add(Calendar.DATE, diff);
		} else if (currentDay == scheduledDayOfMonth.intValue()) {
			if (isTimeAfterScheduledTime(current)) {
				current.add(Calendar.MONTH, 1);
			}
		} else {
			int numOfDaysInThisMonth = current.getActualMaximum(Calendar.DAY_OF_MONTH);
			int diff = numOfDaysInThisMonth - currentDay + scheduledDayOfMonth.intValue();
			current.add(Calendar.DATE, diff);
		}
		
		current.set(Calendar.HOUR_OF_DAY, scheduledHour);
		current.set(Calendar.MINUTE, scheduledMinute);
		return current.getTime();
	}
	
	private boolean isTimeAfterScheduledTime(Calendar current) {
		int currentHour = current.get(Calendar.HOUR_OF_DAY);
		int currentMinutes = current.get(Calendar.MINUTE);
		
		if (currentHour > scheduledHour) {
			return true;
		}
		
		if (currentHour == scheduledHour && currentMinutes >= scheduledMinute) {
			return true;
		}
		
		return false;
	}
	
	public ScheduledTask newTask() {
		if (getType() == Type.EXTERNAL) {
			return new ExternalScheduledTask();
		}
		
		try {
			return (ScheduledTask)Class.forName(getTaskImplfqn()).newInstance();
		} catch (Exception e) {
			logger.error("Invalid scheduled job class: " + getTaskImplfqn(), e);
			throw OpenSpecimenException.userError(ScheduledJobErrorCode.INVALID_TYPE);
		}
	}
	
	private static final String[] JOB_UPDATE_IGN_PROPS = new String[] {
		"id", 
		"createdBy",
		"recipients"
	};
}
