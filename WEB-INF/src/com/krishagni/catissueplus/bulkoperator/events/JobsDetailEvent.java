package com.krishagni.catissueplus.bulkoperator.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class JobsDetailEvent extends ResponseEvent {
	private List<JobDetail> jobs = new ArrayList<JobDetail>();

	public List<JobDetail> getJobs() {
		return jobs;
	}

	public void setJobs(List<JobDetail> jobs) {
		this.jobs = jobs;
	}
	
	public static JobsDetailEvent ok(List<JobDetail> jobs) {
		JobsDetailEvent resp = new JobsDetailEvent();
		resp.setStatus(EventStatus.OK);
		resp.setJobs(jobs);
		return resp;
	}
	
	public static JobsDetailEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		JobsDetailEvent resp = new JobsDetailEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
	
	public static JobsDetailEvent invalidRequest(CatissueErrorCode error, Throwable t) {
		JobsDetailEvent resp = new JobsDetailEvent();
		resp.setupResponseEvent(EventStatus.BAD_REQUEST, error, t);
		return resp;
	}
}
