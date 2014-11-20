package com.krishagni.catissueplus.bulkoperator.repository.impl;

import java.util.List;

import com.krishagni.catissueplus.bulkoperator.domain.BulkOperation;
import com.krishagni.catissueplus.bulkoperator.repository.BulkOperationDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class BulkOperationDaoImpl extends AbstractDao<BulkOperation> implements BulkOperationDao {
	private static final String FQN = BulkOperation.class.getName();
	
	private static final String GET_BULK_OPERATION_BY_NAME = FQN + ".getBulkOperationByName";
	
	private static final String GET_ALL_BULK_OPERATIONS = FQN + ".getAllBulkOperations";
			
	@Override
	@SuppressWarnings("unchecked")
	public BulkOperation getBulkOperation(String operationName) {
		List<BulkOperation> operations = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_BULK_OPERATION_BY_NAME)
				.setString("operationName", operationName)
				.list();

		return operations.isEmpty() ? null : operations.iterator().next();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BulkOperation> getBulkOperations() {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_ALL_BULK_OPERATIONS)
				.list();
	}

}
