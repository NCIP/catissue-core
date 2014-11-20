package com.krishagni.catissueplus.bulkoperator.events;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class BulkRecordsImportedEvent extends ResponseEvent {
	private Long jobId;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	
	public static BulkRecordsImportedEvent ok(Long jobId) {
		BulkRecordsImportedEvent event = new BulkRecordsImportedEvent();
		event.setJobId(jobId);
		event.setStatus(EventStatus.OK);
		return event;
	}
	
	public static BulkRecordsImportedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		BulkRecordsImportedEvent resp = new BulkRecordsImportedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
	
	public static BulkRecordsImportedEvent invalidRequest(CatissueErrorCode error, Throwable t) {
		BulkRecordsImportedEvent resp = new BulkRecordsImportedEvent();
		resp.setupResponseEvent(EventStatus.BAD_REQUEST, error, t);
		return resp;
	}
}
