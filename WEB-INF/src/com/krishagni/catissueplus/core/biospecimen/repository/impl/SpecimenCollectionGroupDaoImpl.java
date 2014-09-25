
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenCollectionGroupDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenRequirement;

@Repository("specimenCollectionGroupDao")
public class SpecimenCollectionGroupDaoImpl extends AbstractDao<SpecimenCollectionGroup>
		implements
			SpecimenCollectionGroupDao {

	@Override
	public List<SpecimenCollectionGroup> getAllScgs(int startAt, int maxRecords, String... searchString) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(SpecimenCollectionGroup.class, "scg")
				.add(Restrictions.or(
						Restrictions.eq("scg.activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()),
						Restrictions.eq("scg.activityStatus", Status.ACTIVITY_STATUS_CLOSED.getStatus())));
		
		addSearchConditions(criteria, searchString);
		criteria.addOrder(Order.asc("scg.name"));
		addLimits(criteria, startAt, maxRecords);
		return getScgs(criteria);
	}
	
	@Override
	public Long getScgsCount(String... searchString) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(SpecimenCollectionGroup.class, "scg")
				.add(Restrictions.or(
						Restrictions.eq("scg.activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()),
						Restrictions.eq("scg.activityStatus", Status.ACTIVITY_STATUS_CLOSED.getStatus())))
				.setProjection(Projections.countDistinct("scg.id"));
		
		addSearchConditions(criteria, searchString);
		return ((Number)criteria.uniqueResult()).longValue();
	}
	
	@Override
	public List<Specimen> getSpecimensList(Long scgId) {
		Object object = sessionFactory.getCurrentSession().get(SpecimenCollectionGroup.class.getName(), scgId);
		if (object == null) {
			return Collections.emptyList();
		}

		SpecimenCollectionGroup scg = (SpecimenCollectionGroup) object;
		return new ArrayList<Specimen>(scg.getSpecimenCollection());
	}

	@Override
	public boolean isNameUnique(String name) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SCG_ID_BY_NAME);
		query.setString("name", name);
		return query.list().isEmpty() ? true : false;
	}

	@Override
	public boolean isBarcodeUnique(String barcode) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SCG_ID_BY_BARCODE);
		query.setString("barcode", barcode);
		return query.list().isEmpty() ? true : false;
	}

	@Override
	public SpecimenCollectionGroup getscg(Long id) {
		return (SpecimenCollectionGroup)sessionFactory.getCurrentSession().get(SpecimenCollectionGroup.class, id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<SpecimenRequirement> getSpecimenRequirments(Long scgId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CPE_BY_SCG_ID);
		query.setLong("scgId", scgId);
		List<CollectionProtocolEvent> cpeList = query.list();
		if(cpeList.isEmpty()){
			return Collections.emptyList();
		}
		CollectionProtocolEvent cpe = (CollectionProtocolEvent) cpeList.get(0);
		return new ArrayList<SpecimenRequirement>(cpe.getSpecimenRequirementCollection());
	}
	
	private void addSearchConditions(Criteria criteria, String[] searchString) {
		if (searchString == null || searchString.length == 0 || StringUtils.isBlank(searchString[0])) {
			return;
		}
		
		Disjunction srchCond = Restrictions.disjunction();
		srchCond.add(Restrictions.or(
				Restrictions.ilike("scg.name", searchString[0], MatchMode.ANYWHERE),
				Restrictions.ilike("scg.barcode", searchString[0], MatchMode.ANYWHERE)
				));
		criteria.add(srchCond);
	}
	
	
	private void addLimits(Criteria criteria, int start, int maxRecords) {
		criteria.setFirstResult(start <= 0 ? 0 : start);
		if (maxRecords > 0) {
			criteria.setMaxResults(maxRecords);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<SpecimenCollectionGroup> getScgs(Criteria criteria) {
		List<SpecimenCollectionGroup> result = criteria.list();
		return result;		
	}

	private static final String FQN = SpecimenCollectionGroup.class.getName();

	private static final String GET_SCG_ID_BY_BARCODE = FQN + ".getScgIdByBarcode";

	private static final String GET_SCG_ID_BY_NAME = FQN + ".getScgIdByName";
	
	private static final String GET_CPE_BY_SCG_ID = FQN + ".getCpeByScgId";

}
