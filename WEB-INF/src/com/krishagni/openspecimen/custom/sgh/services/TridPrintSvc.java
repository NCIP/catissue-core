package com.krishagni.openspecimen.custom.sgh.services;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.custom.sgh.events.BulkTridPrintOpDetail;

public interface TridPrintSvc {
	public ResponseEvent<Boolean> generateAndPrintTrids(RequestEvent<BulkTridPrintOpDetail> req);

}
