package com.krishagni.catissueplus.bulkoperator.services;

import com.krishagni.catissueplus.bulkoperator.events.BulkImportRecordsEvent;
import com.krishagni.catissueplus.bulkoperator.events.BulkOperationsEvent;
import com.krishagni.catissueplus.bulkoperator.events.BulkRecordsImportedEvent;
import com.krishagni.catissueplus.bulkoperator.events.JobsDetailEvent;
import com.krishagni.catissueplus.bulkoperator.events.LogFileContentEvent;
import com.krishagni.catissueplus.bulkoperator.events.ReqJobsDetailEvent;
import com.krishagni.catissueplus.bulkoperator.events.ReqLogFileContentEvent;
import com.krishagni.catissueplus.core.common.events.RequestEvent;

public interface BulkOperationService {
	public BulkOperationsEvent getBulkOperations(RequestEvent event);
	
	public JobsDetailEvent getJobs(ReqJobsDetailEvent req);
	
	public BulkRecordsImportedEvent bulkImportRecords(BulkImportRecordsEvent req);
			
	public LogFileContentEvent getLogFileContent(ReqLogFileContentEvent req);	
}
