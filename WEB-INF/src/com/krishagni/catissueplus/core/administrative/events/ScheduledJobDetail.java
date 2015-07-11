package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob.RepeatSchedule;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class ScheduledJobDetail {
	private Long id;

	private String name;
	
	private UserSummary createdBy;
	
	private Date startDate;
	
	private Date endDate;
	
	private Integer scheduledHour;
	
	private Integer scheduledMinute;
	
	private String scheduledDayOfWeek;
	
	private Integer scheduledDayOfMonth;
	
	private Date nextRunOn;
	
	private String activityStatus;
	
	private String repeatSchedule;
	
	private String type;
	
	private String taskImplFqn;
	
	private String command;
	
	private List<UserSummary> recipients = new ArrayList<UserSummary>();
	
	private Boolean isActiveJob;

	private Date lastRunOn;
	
	private Boolean rtArgsProvided;
	
	private String rtArgsHelpText;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserSummary getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserSummary createdBy) {
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

	public String getScheduledDayOfWeek() {
		return scheduledDayOfWeek;
	}

	public void setScheduledDayOfWeek(String dayOfWeek) {
		this.scheduledDayOfWeek = dayOfWeek;
	}

	public Integer getScheduledDayOfMonth() {
		return scheduledDayOfMonth;
	}

	public void setScheduledDayOfMonth(Integer scheduledDayOfMonth) {
		this.scheduledDayOfMonth = scheduledDayOfMonth;
	}

	public Date getNextRunOn() {
		return nextRunOn;
	}

	public void setNextRunOn(Date nextRunOn) {
		this.nextRunOn = nextRunOn;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getRepeatSchedule() {
		return repeatSchedule;
	}

	public void setRepeatSchedule(String repeatSchedule) {
		this.repeatSchedule = repeatSchedule;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTaskImplFqn() {
		return taskImplFqn;
	}

	public void setTaskImplFqn(String fqn) {
		this.taskImplFqn = fqn;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<UserSummary> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<UserSummary> recipients) {
		this.recipients = recipients;
	}

	public Boolean getIsActiveJob() {
		return isActiveJob;
	}

	public void setIsActiveJob(Boolean isActiveJob) {
		this.isActiveJob = isActiveJob;
	}

	public Date getLastRunOn() {
		return lastRunOn;
	}

	public void setLastRunOn(Date lastRunOn) {
		this.lastRunOn = lastRunOn;
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

	public static ScheduledJobDetail from(ScheduledJob job) {
		ScheduledJobDetail detail = new ScheduledJobDetail();
	
		detail.setId(job.getId());
		detail.setName(job.getName());
		detail.setCreatedBy(UserSummary.from(job.getCreatedBy()));
		detail.setActivityStatus(job.getActivityStatus());
		detail.setRepeatSchedule(job.getRepeatSchedule().toString());
		detail.setType(job.getType().toString());
		detail.setCommand(job.getCommand());
		detail.setIsActiveJob(job.isActiveJob());
		detail.setTaskImplFqn(job.getTaskImplfqn());
		detail.setRecipients(UserSummary.from(job.getRecipients()));

		detail.setRtArgsProvided(job.getRtArgsProvided());
		detail.setRtArgsHelpText(job.getRtArgsHelpText());		
		
		if (job.getRepeatSchedule().equals(RepeatSchedule.ONDEMAND)) {
			return detail;
		}
		
		detail.setStartDate(job.getStartDate());
		detail.setEndDate(job.getEndDate());
		detail.setNextRunOn(job.getNextRunOn());

		detail.setScheduledMinute(job.getScheduledMinute());
		detail.setScheduledHour(job.getScheduledHour());
		detail.setScheduledDayOfMonth(job.getScheduledDayOfMonth());

		if (job.getScheduledDayOfWeek() != null) {
			detail.setScheduledDayOfWeek(job.getScheduledDayOfWeek().toString());
		}
		
		return detail;
	}
	
	public static List<ScheduledJobDetail> from(List<ScheduledJob> jobs) {
		List<ScheduledJobDetail> list = new ArrayList<ScheduledJobDetail>();
		
		for (ScheduledJob job : jobs) {
			list.add(from(job));
		}
		
		return list;
	}
}
