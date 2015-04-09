
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.CprSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CprListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class CollectionProtocolRegistrationDaoImpl 
	extends AbstractDao<CollectionProtocolRegistration> 
	implements CollectionProtocolRegistrationDao {
	
	@Override
	public Class<CollectionProtocolRegistration> getType() {
		return CollectionProtocolRegistration.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CprSummary> getCprList(CprListCriteria cprCrit) {
		Criteria query = getCprListQuery(cprCrit);
		query.setProjection(getCprSummaryFields(cprCrit));
		
		List<CprSummary> cprs = new ArrayList<CprSummary>();
		Map<Long, CprSummary> cprMap = new HashMap<Long, CprSummary>();
		
		List<Object[]> rows = query.list();				
		for (Object[] row : rows) {
			CprSummary cpr = getCprSummary(row);
			if (cprCrit.includeStat()) {
				cprMap.put(cpr.getCprId(), cpr);
			}
			
			cprs.add(cpr);
		}
		
		if (!cprCrit.includeStat()) {
			return cprs;
		}
		
		List<Object[]> countRows = getScgAndSpecimenCounts(cprCrit);
		for (Object[] row : countRows) {
			CprSummary cpr = cprMap.get((Long)row[0]);
			cpr.setScgCount((Long)row[1]);
			cpr.setSpecimenCount((Long)row[2]);
		}
		
		return cprs;
	}

	@Override
	@SuppressWarnings("unchecked")
	public CollectionProtocolRegistration getCprByBarcode(String barcode) {
		List<CollectionProtocolRegistration> result = sessionFactory.getCurrentSession()
				.createCriteria(CollectionProtocolRegistration.class)
				.add(Restrictions.eq("barcode", barcode))
				.list();
		
		return result.isEmpty() ? null : result.iterator().next();
	}

	@Override
	@SuppressWarnings("unchecked")
	public CollectionProtocolRegistration getCprByPpId(Long cpId, String ppid) {
		List<CollectionProtocolRegistration> result = sessionFactory.getCurrentSession()
			.getNamedQuery(GET_BY_CP_ID_AND_PPID)
			.setLong("cpId", cpId)
			.setString("ppid", ppid)
			.list();
		
		return result != null && !result.isEmpty() ? result.iterator().next() : null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public CollectionProtocolRegistration getCprByParticipantId(Long cpId, Long participantId) {		
		List<CollectionProtocolRegistration> result =  sessionFactory.getCurrentSession()
				.getNamedQuery(GET_BY_CP_ID_AND_PID)
				.setLong("cpId", cpId)
				.setLong("participantId", participantId)
				.list();
		
		return result.isEmpty() ? null : result.iterator().next();
	}
	
	
	private Criteria getCprListQuery(CprListCriteria cprCrit) {
		Criteria query = getSessionFactory().getCurrentSession()
				.createCriteria(CollectionProtocolRegistration.class)
				.createAlias("collectionProtocol", "cp")
				.createAlias("participant", "participant")
				.add(Restrictions.eq("cp.id", cprCrit.cpId()))
				.add(Restrictions.ne("activityStatus", "Disabled"))
				.add(Restrictions.ne("cp.activityStatus", "Disabled"))
				.add(Restrictions.ne("participant.activityStatus", "Disabled"))
				.addOrder(Order.asc("id"))
				.setFirstResult(cprCrit.startAt() < 0 ? 0 : cprCrit.startAt())
				.setMaxResults(cprCrit.maxResults() < 0 || cprCrit.maxResults() > 100 ? 100 : cprCrit.maxResults());
		
		addMrnSiteCondition(query, cprCrit.siteIds());
		
		String searchTerm = cprCrit.query();
		boolean isSearchTermSpecified = !StringUtils.isBlank(searchTerm);
		if (!isSearchTermSpecified) {
			return query;
		}
		
		Junction searchCrit = Restrictions.disjunction()
					.add(Restrictions.ilike("ppid", searchTerm, MatchMode.ANYWHERE));			
		if (cprCrit.includePhi()) {				
			searchCrit.add(Restrictions.ilike("participant.firstName", searchTerm, MatchMode.ANYWHERE));
			searchCrit.add(Restrictions.ilike("participant.lastName", searchTerm, MatchMode.ANYWHERE));
		}
			
		query.add(searchCrit);
		return query;		
	}
	
	private void addMrnSiteCondition(Criteria query, Set<Long> siteIds) {
		if (CollectionUtils.isEmpty(siteIds)) {
			return;
		}
		
		query.createAlias("participant.pmis", "pmi", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pmi.site", "site", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.disjunction()
					.add(Restrictions.isNull("site.id"))
					.add(Restrictions.in("site.id", siteIds)));
	}
	
	private ProjectionList getCprSummaryFields(CprListCriteria cprCrit) {
		ProjectionList projs = Projections.projectionList()
				.add(Projections.property("id"))
				.add(Projections.property("ppid"))
				.add(Projections.property("registrationDate"))
				.add(Projections.property("participant.id"));
		
		if (cprCrit.includePhi()) {
			projs.add(Projections.property("participant.firstName"))
				.add(Projections.property("participant.lastName"));				
		}
		
		return projs;		
	}
	
	private CprSummary getCprSummary(Object[] row) {
		CprSummary cpr = new CprSummary();
		cpr.setCprId((Long)row[0]);
		cpr.setPpid((String)row[1]);
		cpr.setRegistrationDate((Date)row[2]);
		
		ParticipantSummary participant = new ParticipantSummary();
		cpr.setParticipant(participant);			
		participant.setId((Long)row[3]);
		if (row.length > 4) {
			participant.setFirstName((String)row[4]);
			participant.setLastName((String)row[5]);
		}
		
		return cpr;
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getScgAndSpecimenCounts(CprListCriteria cprCrit) {
		Criteria countQuery = getCprListQuery(cprCrit)
				.createAlias("visits", "visit",
						CriteriaSpecification.LEFT_JOIN, Restrictions.eq("visit.status", "Complete"))
				.createAlias("visit.specimens", "specimen",
						CriteriaSpecification.LEFT_JOIN, Restrictions.eq("specimen.collectionStatus", "Collected"));
		
		return countQuery.setProjection(Projections.projectionList()
				.add(Projections.property("id"))
				.add(Projections.countDistinct("visit.id"))
				.add(Projections.countDistinct("specimen.id"))
				.add(Projections.groupProperty("id")))
				.list();
	}
	
	private static final String FQN = CollectionProtocolRegistration.class.getName();
	
	private static final String GET_BY_CP_ID_AND_PPID = FQN + ".getCprByCpIdAndPpid";
	
	private static final String GET_BY_CP_ID_AND_PID = FQN + ".getCprByCpIdAndPid";
}
