package com.krishagni.catissueplus.bulkoperator.services;

import java.io.File;

import com.krishagni.catissueplus.bulkoperator.domain.BulkOperation;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;

import edu.wustl.common.beans.SessionDataBean;

public interface BulkOperationManager {
	public Long importRecords(SessionDataBean session, BulkOperation bulkOperation, File fileIn)
	throws BulkOperationException;
}
