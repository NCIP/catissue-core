
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsDao;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;

@Repository("visitsDao")
public class VisitsDaoImpl extends AbstractDao<Visit> implements VisitsDao {
	
	@Override
	public Class getType() {
		return Visit.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VisitSummary> getVisits(VisitsListCriteria crit) {
		List<Object[]> rows = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_VISITS_SUMMARY_BY_CPR_ID)
				.setLong("cprId", crit.cprId())
				.list();
		
		List<VisitSummary> visits = new ArrayList<VisitSummary>();
		Map<String, VisitSummary> visitsMap = new HashMap<String, VisitSummary>();
		
		for (Object[] row : rows) {
			VisitSummary visit = new VisitSummary();
			visit.setId((Long)row[0]);
			visit.setEventId((Long)row[1]);
			visit.setName((String)row[2]);
			visit.setLabel((String)row[3]);
			visit.setCalendarPoint((Integer)row[4]);
			visit.setStatus((String)row[5]);
			visit.setVisitDate((Date)row[6]);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime((Date)row[7]);
			cal.add(Calendar.DAY_OF_YEAR, visit.getCalendarPoint());
			visit.setAnticipatedVisitDate(cal.getTime());
			
			visits.add(visit);
			if (crit.includeStat()) {				
				visitsMap.put(getVisitKey(visit.getId(), visit.getEventId()), visit);
			}
		}
						
		if (crit.includeStat() && !visitsMap.isEmpty()) {
			getVisitsCollectionStatus(crit.cprId(), visitsMap);
		}
	
		Collections.sort(visits);
		return visits;
	}
	
	@Override
	public Visit getVisit(Long visitId) {
		return (Visit)sessionFactory.getCurrentSession()
				.get(Visit.class, visitId);
	}
	
	
	private String getVisitKey(Long scgId, Long cpeId) {
		String key = "";
		if (scgId != null) {
			key += scgId;
		}
		
		key += "_" + cpeId;
		return key;
	}
	
	
	private void getVisitsCollectionStatus(Long cprId, Map<String, VisitSummary> visitsMap) {
		Set<Long> eventIds = new HashSet<Long>();
		for (VisitSummary visit : visitsMap.values()) {
			eventIds.add(visit.getEventId());
		}
		
		getPlannedCollectionStatus(cprId, eventIds, visitsMap);
		getUnplannedCollectionStatus(cprId, eventIds, visitsMap);
	}
	
	@SuppressWarnings("unchecked")
	private void getPlannedCollectionStatus(Long cprId, Set<Long> eventIds, Map<String, VisitSummary> visitsMap) {		
		List<Object[]> rows = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_VISITS_COLLECTION_STATUS)
				.setLong("cprId", cprId)
				.setParameterList("eventIds", eventIds)
				.list();
		
		for (Object[] row : rows) {
			Long scgId = (Long)row[0];
			Long eventId = (Long)row[1];
			
			VisitSummary visit = visitsMap.get(getVisitKey(scgId, eventId));
			visit.setAnticipatedSpecimens((Integer)row[2]);
			visit.setCollectedSpecimens((Integer)row[3]);
			visit.setUncollectedSpecimens((Integer)row[4]);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void getUnplannedCollectionStatus(Long cprId, Set<Long> eventIds, Map<String, VisitSummary> visitsMap) {		
		List<Object[]> rows = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_VISITS_UNPLANNED_SPECIMENS_STAT)
				.setLong("cprId", cprId)
				.setParameterList("eventIds", eventIds)
				.list();
		
		for (Object[] row : rows) {
			Long scgId = (Long)row[0];	
			Long eventId = (Long)row[1];
			
			VisitSummary visit = visitsMap.get(getVisitKey(scgId, eventId));
			visit.setUnplannedSpecimens((Integer)row[2]);
		}				
	}
		
	private static final String FQN = Visit.class.getName();
	
	private static final String GET_VISITS_SUMMARY_BY_CPR_ID = FQN + ".getVisitsSummaryByCprId";
	
	private static final String GET_VISITS_COLLECTION_STATUS = FQN + ".getVisitsCollectionStatus";
	
	private static final String GET_VISITS_UNPLANNED_SPECIMENS_STAT = FQN + ".getVisitsUnplannedSpecimenCount";

	//
	// TODO: Requires review
	//
	@Override
	public List<Visit> getAllScgs(int startAt, int maxRecords, String... searchString) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Visit.class, "scg")
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
				.createCriteria(Visit.class, "scg")
				.add(Restrictions.or(
						Restrictions.eq("scg.activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()),
						Restrictions.eq("scg.activityStatus", Status.ACTIVITY_STATUS_CLOSED.getStatus())))
				.setProjection(Projections.countDistinct("scg.id"));
		
		addSearchConditions(criteria, searchString);
		return ((Number)criteria.uniqueResult()).longValue();
	}
	
	@Override
	public List<Specimen> getSpecimensList(Long scgId) {
		Object object = sessionFactory.getCurrentSession().get(Visit.class.getName(), scgId);
		if (object == null) {
			return Collections.emptyList();
		}

		Visit scg = (Visit) object;
		return new ArrayList<Specimen>(scg.getSpecimens());
	}

	@Override
	@SuppressWarnings("unchecked")
	public Visit getScgByName(String name) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Visit.class);
		query.add(Restrictions.eq("name", name));
		List<Visit> scgs = query.list();
		
		return scgs.size() > 0 ? scgs.get(0) : null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Visit getScgByBarcode(String barcode) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Visit.class);
		query.add(Restrictions.eq("barcode", barcode));
		List<Visit> scgs = query.list();
		
		return scgs.size() > 0 ? scgs.get(0) : null;
	}

	@Override
	public Visit getscg(Long id) {
		return (Visit)sessionFactory.getCurrentSession().get(Visit.class, id);
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
	private List<Visit> getScgs(Criteria criteria) {
		List<Visit> result = criteria.list();
		return result;		
	}
}
