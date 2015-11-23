package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.Shipment;
import com.krishagni.catissueplus.core.administrative.events.ShipmentListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.ShipmentDao;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class ShipmentDaoImpl extends AbstractDao<Shipment> implements ShipmentDao {

	@Override
	public Class<Shipment> getType() {
		return Shipment.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Shipment> getShipments(ShipmentListCriteria crit) {
		Criteria query = sessionFactory.getCurrentSession()
				.createCriteria(Shipment.class)
				.createAlias("site", "site")
				.setFirstResult(crit.startAt() < 0 ? 0 : crit.startAt())
				.setMaxResults(crit.maxResults() < 0 || crit.maxResults() > 100 ? 100 : crit.maxResults())
				.addOrder(Order.desc("id"));
		
		if (CollectionUtils.isNotEmpty(crit.siteIds())) {
			query.createAlias("sender", "sender")
				.add(Restrictions.or(
					Restrictions.in("site.id", crit.siteIds()),
					Restrictions.eq("sender.id", AuthUtil.getCurrentUser().getId())));
		}
		
		MatchMode matchMode = crit.exactMatch() ? MatchMode.EXACT : MatchMode.ANYWHERE;
		addNameRestrictions(query, crit, matchMode);
		addInstituteRestrictions(query, crit, MatchMode.EXACT);
		addSiteRestrictions(query, crit, MatchMode.EXACT);
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Shipment getShipmentByName(String name) {
		List<Shipment> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_SHIPMENT_BY_NAME)
				.setString("name", name)
				.list();
		
		return result.isEmpty() ? null : result.iterator().next();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Specimen> getShippedSpecimensByLabels(List<String> specimenLabels) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_SHIPPED_SPECIMENS_BY_LABELS)
				.setParameterList("labels", specimenLabels)
				.list();
	}
	
	private void addNameRestrictions(Criteria query, ShipmentListCriteria crit, MatchMode matchMode) {
		if (StringUtils.isBlank(crit.name())) {
			return;
		}
		
		query.add(Restrictions.ilike("name", crit.name(), matchMode));
	}
	
	private void addInstituteRestrictions(Criteria query, ShipmentListCriteria crit, MatchMode matchMode) {
		if (StringUtils.isBlank(crit.institute())) {
			return;
		}
		
		query.createAlias("site.institute", "institute")
			.add(Restrictions.ilike("institute.name", crit.institute(), matchMode));
	}
	
	private void addSiteRestrictions(Criteria query, ShipmentListCriteria crit, MatchMode matchMode) {
		if (StringUtils.isBlank(crit.site())) {
			return;
		}
		
		query.add(Restrictions.ilike("site.name", crit.site(), matchMode));
	}
	
	private static final String FQN = Shipment.class.getName();
	
	private static final String GET_SHIPMENT_BY_NAME = FQN + ".getShipmentByName";
	
	private static final String GET_SHIPPED_SPECIMENS_BY_LABELS = FQN + ".getShippedSpecimensByLabels";
}
