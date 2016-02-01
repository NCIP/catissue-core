
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsDao;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class VisitsDaoImpl extends AbstractDao<Visit> implements VisitsDao {
	
	@Override
	public Class<Visit> getType() {
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

		Date regDate = null;
		int minEventPoint = 0;
		for (Object[] row : rows) {
			Long visitId = (Long)row[0];
			String eventStatus = (String)row[3];
			if (visitId == null && StringUtils.isNotBlank(eventStatus) && eventStatus.equals("Disabled")) {
				continue;
			}
			
			VisitSummary visit = new VisitSummary();
			visit.setId(visitId);
			visit.setEventId((Long)row[1]);
			visit.setName((String)row[2]);
			visit.setEventLabel((String)row[4]);
			visit.setEventPoint((Integer)row[5]);
			visit.setStatus((String)row[6]);
			visit.setVisitDate((Date)row[7]);
			regDate = (Date)row[8];
			visit.setMissedVisitReason((String)row[9]);
			visits.add(visit);

			if (crit.includeStat()) {				
				visitsMap.put(getVisitKey(visit.getId(), visit.getEventId()), visit);
			}

			if (visit.getEventPoint() != null && visit.getEventPoint() < minEventPoint) {
				minEventPoint = visit.getEventPoint();
			}
		}

		Calendar cal = Calendar.getInstance();
		for (VisitSummary visit : visits) {
			if (visit.getEventPoint() == null) {
				continue;
			}

			cal.setTime(regDate);
			cal.add(Calendar.DAY_OF_YEAR, visit.getEventPoint() - minEventPoint);
			visit.setAnticipatedVisitDate(cal.getTime());
		}
						
		if (crit.includeStat() && !visitsMap.isEmpty()) {
			getVisitsCollectionStatus(crit.cprId(), visitsMap);
		}
	
		Collections.sort(visits);
		return visits;
	}
	
	@Override
	public Visit getByName(String name) {
		List<Visit> visits = getByName(Collections.singleton(name));
		return !visits.isEmpty() ? visits.iterator().next() : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Visit> getByName(Collection<String> names) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_VISIT_BY_NAME)
				.setParameterList("names", names)
				.list();
	}

	public List<Visit> getBySpr(String sprNumber) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_VISIT_BY_SPR)
				.setString("sprNumber", sprNumber)
				.list();
	}

	private String getVisitKey(Long visitId, Long cpeId) {
		String key = "";
		if (visitId != null) {
			key += visitId;
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

	private static final String GET_VISIT_BY_NAME = FQN + ".getVisitByName";

	private static final String GET_VISIT_BY_SPR = FQN + ".getVisitBySpr";
}

