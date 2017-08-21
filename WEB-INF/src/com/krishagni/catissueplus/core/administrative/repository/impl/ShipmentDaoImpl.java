package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.Shipment;
import com.krishagni.catissueplus.core.administrative.domain.Shipment.Status;
import com.krishagni.catissueplus.core.administrative.events.ShipmentListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.ShipmentDao;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class ShipmentDaoImpl extends AbstractDao<Shipment> implements ShipmentDao {

	@Override
	public Class<Shipment> getType() {
		return Shipment.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Shipment> getShipments(ShipmentListCriteria crit) {
		Criteria query = getShipmentsQuery(crit)
				.setFirstResult(crit.startAt())
				.setMaxResults(crit.maxResults())
				.addOrder(Order.desc("id"));
		
		return query.list();
	}

	@Override
	public Long getShipmentsCount(ShipmentListCriteria crit) {
		Number count = (Number) getShipmentsQuery(crit)
				.setProjection(Projections.rowCount())
				.uniqueResult();
		return count.longValue();
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
	public List<Specimen> getShippedSpecimensByIds(List<Long> specimenIds) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_SHIPPED_SPECIMENS_BY_IDS)
				.setParameterList("ids", specimenIds)
				.list();
	}

	@Override
	public Map<String, Object> getShipmentIds(String key, Object value) {
		return getObjectIds("shipmentId", key, value);
	}

	private Criteria getShipmentsQuery(ShipmentListCriteria crit) {
		Criteria query = sessionFactory.getCurrentSession()
				.createCriteria(Shipment.class)
				.createAlias("receivingSite", "recvSite");
		
		addNameRestrictions(query, crit);
		addInstituteRestrictions(query, crit);
		addSiteRestrictions(query, crit);
		return query;
	}

	private void addNameRestrictions(Criteria query, ShipmentListCriteria crit) {
		if (StringUtils.isBlank(crit.name())) {
			return;
		}
		
		query.add(Restrictions.ilike("name", crit.name(), crit.matchMode()));
	}
	
	private void addInstituteRestrictions(Criteria query, ShipmentListCriteria crit) {
		if (StringUtils.isBlank(crit.recvInstitute())) {
			return;
		}
		
		query.createAlias("recvSite.institute", "institute")
			.add(Restrictions.eq("institute.name", crit.recvInstitute()));
	}
	
	private void addSiteRestrictions(Criteria query, ShipmentListCriteria crit) {
		if (StringUtils.isNotBlank(crit.recvSite())) {
			query.add(Restrictions.eq("recvSite.name", crit.recvSite()));
		}
		
		if (CollectionUtils.isEmpty(crit.siteIds())) {
			return;
		}
		
		query.createAlias("sendingSite", "sendSite")
			.add(
				Restrictions.or(
					Restrictions.and(
						Restrictions.in("recvSite.id", crit.siteIds()),
						Restrictions.ne("status", Status.PENDING)
					),/* end of AND */
					Restrictions.in("sendSite.id", crit.siteIds())
				)/* end of OR */
			);
	}
	
	private static final String FQN = Shipment.class.getName();
	
	private static final String GET_SHIPMENT_BY_NAME = FQN + ".getShipmentByName";
	
	private static final String GET_SHIPPED_SPECIMENS_BY_IDS = FQN + ".getShippedSpecimensByIds";
}
