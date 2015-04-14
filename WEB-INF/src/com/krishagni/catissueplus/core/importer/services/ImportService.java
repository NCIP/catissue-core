package com.krishagni.catissueplus.core.importer.services;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportDetail;
import com.krishagni.catissueplus.core.importer.events.ImportJobDetail;

public interface ImportService {
	public ResponseEvent<ImportJobDetail> importObjects(RequestEvent<ImportDetail> req);
}
