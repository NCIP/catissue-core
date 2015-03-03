package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class ScheduledJobDetail {
	private Long id;
	
	private String name;
	
	private UserSummary createdBy;
	
	private Date startDate;
	
	private Date endDate;
	
	private Date nextRunOn;
	
	private String activityStatus;
	
	private String repeatSchedule;
	
	private String type;
	
	private String command;
	
	private String comments;
	
	private Set<String> recipients = new HashSet<String>();
	
	private Boolean expired;

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

	public Boolean isExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}

	public static ScheduledJobDetail from(ScheduledJob job) {
		ScheduledJobDetail detail = new ScheduledJobDetail();
	
		detail.setId(job.getId());
		detail.setName(job.getName());
		detail.setCreatedBy(UserSummary.from(job.getCreatedBy()));
		detail.setActivityStatus(job.getActivityStatus());
		detail.setRepeatSchedule(job.getRepeatSchedule().toString());
		detail.setType(job.getType());
		detail.setCommand(job.getCommand());
		detail.setComments(job.getComments());
		detail.setNextRunOn(job.getNextRunOn());
		detail.setStartDate(job.getStartDate());
		detail.setEndDate(job.getEndDate());
		detail.setExpired(job.isExpired());
		
		Set<String> recipients = job.getRecipients();
		for (String recipient : recipients) {
			detail.recipients.add(recipient);
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
