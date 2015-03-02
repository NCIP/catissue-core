package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.DistributionOrderDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;

public class DistributionOrderDaoImpl extends AbstractDao<DistributionOrder> implements DistributionOrderDao {
	@Override
	@SuppressWarnings("unchecked")
	public List<DistributionOrder> getDistributionOrders(DistributionOrderListCriteria criteria) {
		Criteria query = sessionFactory.getCurrentSession()
				.createCriteria(DistributionOrder.class)
				.add(Restrictions.eq("activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()))
				.setFirstResult(criteria.startAt() < 0 ? 0 : criteria.startAt())
				.setMaxResults(criteria.maxResults() < 0 || criteria.maxResults() > 100 ? 100 : criteria.maxResults());
		
		String searchTerm = criteria.query();
		if (!StringUtils.isBlank(searchTerm)) {
			query.add(Restrictions.ilike("name", searchTerm, MatchMode.ANYWHERE));
		}
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public DistributionOrder getDistributionOrder(String name) {
		List<DistributionOrder> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_DIST_ORD_BY_NAME)
				.setString("name", name)
				.list();
				
		return result.isEmpty() ? null : result.iterator().next();
	}
	
	@Override
	public Class getType() {
		return DistributionOrder.class;
	}
	
	public static final String FQN  = DistributionOrder.class.getName();
	
	public static final String GET_DIST_ORD_BY_NAME = FQN + ".getDistributionOrderByName";

}
