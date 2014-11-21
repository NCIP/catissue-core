package com.krishagni.catissueplus.bulkoperator.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqLogFileContentEvent extends RequestEvent {
	private Long jobId;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
}
