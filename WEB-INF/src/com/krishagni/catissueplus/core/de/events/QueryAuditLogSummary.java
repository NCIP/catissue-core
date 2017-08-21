package com.krishagni.catissueplus.core.de.events;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.krishagni.catissueplus.core.common.events.UserSummary;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class QueryAuditLogSummary {	
	private Long id;
	
	private Long queryId;
	
	private String queryTitle;
	
	private UserSummary runBy;
	
	private Date timeOfExecution;
	
	private Long timeToFinish;
	
	private String runType;
	
	private Long recordCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public String getQueryTitle() {
		return queryTitle;
	}

	public void setQueryTitle(String queryTitle) {
		this.queryTitle = queryTitle;
	}

	public UserSummary getRunBy() {
		return runBy;
	}

	public void setRunBy(UserSummary runBy) {
		this.runBy = runBy;
	}

	public Date getTimeOfExecution() {
		return timeOfExecution;
	}

	public void setTimeOfExecution(Date runtime) {
		this.timeOfExecution = runtime;
	}

	public Long getTimeToFinish() {
		return timeToFinish;
	}

	public void setTimeToFinish(Long timeToFinish) {
		this.timeToFinish = timeToFinish;
	}

	public String getRunType() {
		return runType;
	}

	public void setRunType(String runType) {
		this.runType = runType;
	}

	public Long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}	
}
