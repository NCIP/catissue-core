package com.krishagni.catissueplus.bulkoperator.repository;

import java.util.List;

import com.krishagni.catissueplus.bulkoperator.domain.BulkOperation;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface BulkOperationDao extends Dao<BulkOperation>{
	public BulkOperation getBulkOperation(String operationName);
	
	//
	// TODO: Changed this to getBulkOperations()
	// 
	public List<BulkOperation> getBulkOperations();
}
