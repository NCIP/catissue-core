package com.krishagni.catissueplus.core.bo.factory;

import com.krishagni.catissueplus.core.bo.events.BulkOperationRequest;
import com.krishagni.catissueplus.core.bo.events.BulkOperationResponse;

public interface BulkOperationController {
	public BulkOperationResponse saveObject(BulkOperationRequest req);
	
	public BulkOperationResponse updateObject(BulkOperationRequest req);
	
	public BulkOperationResponse deleteObject(BulkOperationRequest req);
}
