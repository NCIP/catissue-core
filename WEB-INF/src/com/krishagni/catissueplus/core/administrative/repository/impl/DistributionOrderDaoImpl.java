package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.repository.DistributionOrderDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class DistributionOrderDaoImpl extends AbstractDao<DistributionOrder> implements DistributionOrderDao {
	public static final String FQN  = DistributionOrder.class.getName();
	
	public static final String GET_DO_BY_NAME = FQN + ".getDistributionOrderByName";
	
	public static final String GET_ALL_DO = FQN + ".getAllDistributionOrders";
	
	@Override
	@SuppressWarnings("unchecked")
	public DistributionOrder getDistributionOrder(String name) {
		List<DistributionOrder> result = sessionFactory.getCurrentSession().getNamedQuery(GET_DO_BY_NAME)
				.setString("name", name)
				.list();
				
		return result.isEmpty() ? null : result.iterator().next();
	}
	
	@Override
	public List<DistributionOrder> getDistributionOrders(int startAt, int maxResults) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_ALL_DO)
				.setFirstResult(startAt <= 0 ? 0 : startAt);
		
		if (maxResults > 0 ) {
			query.setMaxResults(maxResults);
		}
		return query.setMaxResults(maxResults).list();
		
	}	
	
	@Override
	public Class getType() {
		return DistributionOrder.class;
	}
}
