//package com.krishagni.catissueplus.core.administrative.repository.impl;
//
//import java.util.List;
//
//import org.apache.commons.lang.StringUtils;
//import org.hibernate.Criteria;
//import org.hibernate.criterion.Junction;
//import org.hibernate.criterion.MatchMode;
//import org.hibernate.criterion.Restrictions;
//
//import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
//import com.krishagni.catissueplus.core.administrative.repository.DistributionListCriteria;
//import com.krishagni.catissueplus.core.administrative.repository.DistributionOrderDao;
//import com.krishagni.catissueplus.core.common.repository.AbstractDao;
//
//public class DistributionOrderDaoImpl extends AbstractDao<DistributionOrder> implements DistributionOrderDao {
//	public static final String FQN  = DistributionOrder.class.getName();
//	
//	public static final String GET_DO_BY_NAME = FQN + ".getDistributionOrderByName";
//	
//	@Override
//	@SuppressWarnings("unchecked")
//	public DistributionOrder getDistributionOrder(String name) {
//		List<DistributionOrder> result = sessionFactory.getCurrentSession().getNamedQuery(GET_DO_BY_NAME)
//				.setString("name", name)
//				.list();
//				
//		return result.isEmpty() ? null : result.iterator().next();
//	}
//	
//	@Override
//	@SuppressWarnings("unchecked")
//	public List<DistributionOrder> getDistributionOrders(DistributionListCriteria criteria) {
//		Criteria query = sessionFactory.getCurrentSession().createCriteria(DistributionOrder.class)
//				.setFirstResult(criteria.startAt() < 0 ? 0 : criteria.startAt())
//				.setMaxResults(criteria.maxResults() < 0 || criteria.maxResults() > 100 ? 100 : criteria.maxResults())
//				.add(Restrictions.ne("activityStatus", "Disabled"));
//		
//		String searchTerm = criteria.query();
//		if (!StringUtils.isBlank(searchTerm)) {
//			Junction searchCrit = Restrictions.disjunction()
//					.add(Restrictions.ilike("name", searchTerm, MatchMode.ANYWHERE));
//			query.add(searchCrit);
//		}
//		
//		return query.list();
//	}	
//	
//	@Override
//	public Class getType() {
//		return DistributionOrder.class;
//	}
//}
