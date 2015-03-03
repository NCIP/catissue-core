package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.factory.ScheduledJobErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

public class ScheduledJob extends BaseEntity {
	public enum RepeatSchedule { 
		ONCE,
		MINUTELY,
		HOURLY,
		DAILY,
		WEEKLY,
		MONTHLY,
		YEARLY
	}
	
	private String name;
	
	private User createdBy;
	
	private Date startDate;
	
	private Date endDate;
	
	private String activityStatus;
	
	private RepeatSchedule repeatSchedule;
	
	private String type;
	
	private String command;
	
	private String comments;
	
	private Set<String> recipients = new HashSet<String>();
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public Set<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(Set<String> recipients) {
		this.recipients = recipients;
	}
	
	public boolean isExpired() {
		Date currentDate = new Date();
		
		if (endDate != null && currentDate.after(endDate)) {
			return true;
		}
		
		if (repeatSchedule == RepeatSchedule.ONCE && currentDate.after(startDate)) { 
			return true;
		}
		
		return false;
	}

	public Date getNextRunOn() {
		if (repeatSchedule == RepeatSchedule.ONCE) {
			return startDate;
		} else if (repeatSchedule == RepeatSchedule.HOURLY) {
			return getNextHourlyOccurence();
		} else if (repeatSchedule == RepeatSchedule.DAILY) {
			return getNextDailyOccurence();
		} else if (repeatSchedule == RepeatSchedule.WEEKLY) {
			return getNextWeeklyOccurence();
		} else if (repeatSchedule == RepeatSchedule.MONTHLY) {
			return getNextMonthlyOccurence();
		} else if (repeatSchedule == RepeatSchedule.YEARLY) {
			return getNextYearlyOccurence();
		} else if (repeatSchedule == RepeatSchedule.MINUTELY) {
			return getNextMinutelyOccurence();
		}
		
		throw OpenSpecimenException.userError(ScheduledJobErrorCode.INVALID_REPEAT_SCHEDULE);
	}
	
	public void delete() {
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
		name = Utility.getDisabledValue(name);
	}
	
	private Date getNextMinutelyOccurence() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, 1);
		return calendar.getTime();
	}
	
	private Date getNextHourlyOccurence() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		int minutes = calendar.get(Calendar.MINUTE);
		if (minutes == 0) {
			calendar.add(Calendar.HOUR, 1);
		} else {
			int diff = 60 - minutes;
			calendar.add(Calendar.MINUTE, diff);
		}
		
		return calendar.getTime();
	}
	
	private Date getNextDailyOccurence() {
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		
		Calendar current = Calendar.getInstance();
		current.setTime(new Date());
		
		if (hasTimepassedOver(current, start)) {
			current.add(Calendar.DATE, 1);
		}
		
		current.set(Calendar.HOUR_OF_DAY, start.get(Calendar.HOUR_OF_DAY));
		current.set(Calendar.MINUTE, start.get(Calendar.MINUTE));
		return current.getTime();
	}
	
	private Date getNextWeeklyOccurence() {
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		int startDayOfWeek = start.get(Calendar.DAY_OF_WEEK);
		
		Calendar current = Calendar.getInstance();
		current.setTime(new Date());
		int currentDayOfWeek = current.get(Calendar.DAY_OF_WEEK);
		
		if (currentDayOfWeek < startDayOfWeek) {
			int diff = startDayOfWeek - currentDayOfWeek;
			current.add(Calendar.DATE, diff);
		} else if (currentDayOfWeek == startDayOfWeek) {
			if (hasTimepassedOver(current, start)) {
				current.add(Calendar.DATE, 7);
			}
		} else {
			int diff = (7-currentDayOfWeek) + startDayOfWeek;
			current.add(Calendar.DATE, diff);
		}
		
		current.set(Calendar.HOUR_OF_DAY, start.get(Calendar.HOUR_OF_DAY));
		current.set(Calendar.MINUTE, start.get(Calendar.MINUTE));
		return current.getTime();
	}
	
	private Date getNextMonthlyOccurence() {
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		int startDay = start.get(Calendar.DAY_OF_MONTH);
		
		Calendar current = Calendar.getInstance();
		current.setTime(new Date());
		int currentDay = current.get(Calendar.DAY_OF_MONTH);
		
		if (currentDay < startDay) {
			int diff = startDay - currentDay;
			current.add(Calendar.DATE, diff);
		} else if (currentDay == startDay) {
			if (hasTimepassedOver(current, start)) {
				current.add(Calendar.MONTH, 1);
			}
		} else {
			int numOfDaysInThisMonth = current.getActualMaximum(Calendar.DAY_OF_MONTH);
			int diff = numOfDaysInThisMonth - currentDay + startDay;
			current.add(Calendar.DATE, diff);
		}
		
		current.set(Calendar.HOUR_OF_DAY, start.get(Calendar.HOUR_OF_DAY));
		current.set(Calendar.MINUTE, start.get(Calendar.MINUTE));
		return current.getTime();
	}
	
	private Date getNextYearlyOccurence() {
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		int startDay = start.get(Calendar.DAY_OF_YEAR);
		
		Calendar current = Calendar.getInstance();
		current.setTime(new Date());
		int currentDay = current.get(Calendar.DAY_OF_YEAR);
		
		if (currentDay < startDay) {
			int diff = startDay - currentDay;
			current.add(Calendar.DATE, diff);
		} else if (currentDay == startDay) {
			if (hasTimepassedOver(current, start)) {
				current.add(Calendar.YEAR, 1);
			}
		} else {
			int totalNumOfDaysInThisYear = current.getActualMaximum(Calendar.DAY_OF_YEAR);
			int diff = totalNumOfDaysInThisYear - currentDay + startDay;
			current.add(Calendar.DATE, diff);
		}
 
		current.set(Calendar.HOUR_OF_DAY, start.get(Calendar.HOUR_OF_DAY));
		current.set(Calendar.MINUTE, start.get(Calendar.MINUTE));
		return current.getTime();
	}
	
	private boolean hasTimepassedOver(Calendar current, Calendar other) {
		int currentHour = current.get(Calendar.HOUR_OF_DAY);
		int currentMinutes = current.get(Calendar.MINUTE);
		
		int otherHour = other.get(Calendar.HOUR_OF_DAY);
		int otherMinutes = other.get(Calendar.MINUTE);
		
		if (currentHour > otherHour) {
			return true;
		}
		
		if (currentHour == otherHour && currentMinutes > otherMinutes) {
			return true;
		}
		
		return false;
	}
}
