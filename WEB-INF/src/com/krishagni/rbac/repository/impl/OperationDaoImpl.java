package com.krishagni.rbac.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.rbac.domain.Operation;
import com.krishagni.rbac.repository.OperationDao;
import com.krishagni.rbac.repository.OperationListCriteria;

public class OperationDaoImpl extends AbstractDao<Operation> implements OperationDao {
	private static final String FQN = Operation.class.getName();
	
	private static final String GET_OPERATION_BY_NAME = FQN + ".getOperationByName";
	
	private static final String GET_ALL_OPERATIONS = FQN + ".getAllOperations";
	
	@Override
	@SuppressWarnings("unchecked")
	public Operation getOperationByName(String opName) {
		List<Operation> operations = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_OPERATION_BY_NAME)
				.setString("name", opName)
				.list();
		return operations.isEmpty() ? null : operations.get(0);
	}

	@Override
	public Operation getOperation(Long id) {
		return (Operation)sessionFactory.getCurrentSession()
				.get(Operation.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Operation> getOperations(OperationListCriteria listCriteria) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Operation.class)
				.setFirstResult(listCriteria.startAt())
				.setMaxResults(listCriteria.maxResults());
		
		if (StringUtils.isNotBlank(listCriteria.query())) {
			query.add(Restrictions.ilike("name", listCriteria.query(), MatchMode.ANYWHERE));
		}
		
		return query.list();
	}
}
