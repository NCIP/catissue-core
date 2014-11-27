package com.krishagni.catissueplus.bulkoperator.events;

import java.io.OutputStream;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqLogFileContentEvent extends RequestEvent {
	private Long jobId;
	
	private OutputStream out;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public OutputStream getOut() {
		return out;
	}

	public void setOut(OutputStream out) {
		this.out = out;
	}
}
