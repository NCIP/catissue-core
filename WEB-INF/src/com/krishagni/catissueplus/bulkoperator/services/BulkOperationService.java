package com.krishagni.catissueplus.bulkoperator.services;

import java.util.List;

import com.krishagni.catissueplus.bulkoperator.events.BulkOperationDetail;
import com.krishagni.catissueplus.bulkoperator.events.ImportRecordsOpDetail;
import com.krishagni.catissueplus.bulkoperator.events.JobDetail;
import com.krishagni.catissueplus.bulkoperator.events.ListJobsCriteria;
import com.krishagni.catissueplus.bulkoperator.events.LogFileContent;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface BulkOperationService {
	public ResponseEvent<List<BulkOperationDetail>> getBulkOperations(RequestEvent<?> req);
	
	public ResponseEvent<List<JobDetail>> getJobs(RequestEvent<ListJobsCriteria> req);
	
	public ResponseEvent<String> bulkImportRecords(RequestEvent<ImportRecordsOpDetail> req);
			
	public ResponseEvent<LogFileContent> getLogFileContent(RequestEvent<Long> req);	
}
