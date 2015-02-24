package com.krishagni.rbac.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.rbac.domain.Operation;

public interface OperationDao extends Dao<Operation> {
	public Operation getOperationByName(String opName);
	
	public Operation getOperation(Long id);
	
	public List<Operation> getOperations(OperationListCriteria listCriteria);
}
