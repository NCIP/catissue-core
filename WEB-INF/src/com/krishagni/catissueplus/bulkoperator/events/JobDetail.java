package com.krishagni.catissueplus.bulkoperator.events;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.bulkoperator.domain.BulkOperationJob;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class JobDetail {
	private Long id;
	
	private String name;
	
	private String status;
	
	private Timestamp startTime;
	
	private Long timeTaken;

	private Long failedRecordsCount;

	private Long totalRecordsCount;

	private Long processedRecords;
	
	private UserSummary startedBy;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	//
	// TODO: Changed these accessor and mutator methods from getJobName to getName etc
	//
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Long getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(Long timeTaken) {
		this.timeTaken = timeTaken;
	}

	public Long getFailedRecordsCount() {
		return failedRecordsCount;
	}

	public void setFailedRecordsCount(Long failedRecordsCount) {
		this.failedRecordsCount = failedRecordsCount;
	}

	public Long getTotalRecordsCount() {
		return totalRecordsCount;
	}

	public void setTotalRecordsCount(Long totalRecordsCount) {
		this.totalRecordsCount = totalRecordsCount;
	}

	public Long getProcessedRecords() {
		return processedRecords;
	}

	public void setProcessedRecords(Long processedRecords) {
		this.processedRecords = processedRecords;
	}

	public UserSummary getStartedBy() {
		return startedBy;
	}

	public void setStartedBy(UserSummary startedBy) {
		this.startedBy = startedBy;
	}

	public static JobDetail from(BulkOperationJob job) {
		JobDetail detail = new JobDetail();
		detail.setId(job.getId());
		detail.setName(job.getName());
		detail.setStatus(job.getStatus());
		detail.setStartTime(job.getStartTime());
		detail.setTimeTaken(job.getTimeTaken());
		detail.setFailedRecordsCount(job.getFailedRecordsCount());
		detail.setTotalRecordsCount(job.getTotalRecordsCount());
		detail.setProcessedRecords(job.getProcessedRecords());
		detail.setStartedBy(UserSummary.fromUser(job.getStartedBy()));
		return detail;
	}
	
	public static List<JobDetail> from(List<BulkOperationJob> jobs) {
		List<JobDetail> jobsList = new ArrayList<JobDetail>();
		
		for (BulkOperationJob job : jobs) {
			jobsList.add(from(job));
		}
		
		return jobsList;
	}
}


