package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.ShippingOrder;
import com.krishagni.catissueplus.core.administrative.events.ShippingOrderListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.ShippingOrderDao;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class ShippingOrderDaoImpl extends AbstractDao<ShippingOrder> implements ShippingOrderDao {
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ShippingOrder> getShippingOrders(ShippingOrderListCriteria crit) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(ShippingOrder.class)
				.setFirstResult(crit.startAt() < 0 ? 0 : crit.startAt())
				.setMaxResults(crit.maxResults() < 0 || crit.maxResults() > 100 ? 100 : crit.maxResults())
				.addOrder(Order.desc("id"));
		
		MatchMode matchMode = crit.exactMatch() ? MatchMode.EXACT : MatchMode.ANYWHERE;
		addNameRestrictions(query, crit, matchMode);
		addInstituteRestrictions(query, crit, MatchMode.EXACT);
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public ShippingOrder getOrderByName(String name) {
		List<ShippingOrder> result = sessionFactory.getCurrentSession().getNamedQuery(GET_ORDER_BY_NAME)
				.setString("name", name)
				.list();
		
		return result.isEmpty() ? null : result.iterator().next();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Specimen> getShippedSpecimens(List<String> specimenLabels) {
		return sessionFactory.getCurrentSession().getNamedQuery(GET_SHIPPED_SPECIMENS)
				.setParameterList("labels", specimenLabels)
				.list();
	}
	
	@Override
	public Class<ShippingOrder> getType() {
		return ShippingOrder.class;
	}
	
	private void addNameRestrictions(Criteria query, ShippingOrderListCriteria crit, MatchMode matchMode) {
		if (StringUtils.isBlank(crit.query())) {
			return;
		}
		
		query.add(Restrictions.ilike("name", crit.query(), matchMode));
	}
	
	private void addInstituteRestrictions(Criteria query, ShippingOrderListCriteria crit, MatchMode matchMode) {
		if (StringUtils.isBlank(crit.institute())) {
			return;
		}
		
		query.createAlias("site", "site")
				.createAlias("site.institute", "institute")
				.add(Restrictions.ilike("institute.name", crit.institute(), matchMode));
	}
	
	private static final String FQN = ShippingOrder.class.getName();
	
	private static final String GET_ORDER_BY_NAME = FQN + ".getOrderByName";
	
	private static final String GET_SHIPPED_SPECIMENS = FQN + ".getShippedSpecimens";
}
