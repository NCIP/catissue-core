
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.CprSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CprListCriteria;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Utility;

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
		Criteria query = getCprListQuery(cprCrit)
			.addOrder(Order.asc("id"))
			.setFirstResult(cprCrit.startAt())
			.setMaxResults(cprCrit.maxResults())
			.setProjection(getCprSummaryFields(cprCrit));
		
		List<CprSummary> cprs = new ArrayList<>();
		Map<Long, CprSummary> cprMap = new HashMap<>();
		Set<Long> phiCps = cprCrit.phiCps();
		boolean allCpsAccess = CollectionUtils.isEmpty(cprCrit.siteCps());
		for (Object[] row : (List<Object[]>)query.list()) {
			CprSummary cpr = getCprSummary(row, allCpsAccess, phiCps);
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
	public Long getCprCount(CprListCriteria cprCrit) {
		Number count = (Number) getCprListQuery(cprCrit)
			.setProjection(Projections.rowCount())
			.uniqueResult();
		return count.longValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CollectionProtocolRegistration> getCprsByCpId(Long cpId, int startAt, int maxResults) {
		return sessionFactory.getCurrentSession()
			.getNamedQuery(GET_BY_CP_ID)
			.setLong("cpId", cpId)
			.setFirstResult(startAt < 0 ? 0 : startAt)
			.setMaxResults(maxResults < 0 ? 100 : maxResults)
			.list();
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
	public CollectionProtocolRegistration getCprByPpid(Long cpId, String ppid) {
		List<CollectionProtocolRegistration> result = sessionFactory.getCurrentSession()
			.getNamedQuery(GET_BY_CP_ID_AND_PPID)
			.setLong("cpId", cpId)
			.setString("ppid", ppid)
			.list();
		
		return CollectionUtils.isEmpty(result) ? null : result.iterator().next();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public CollectionProtocolRegistration getCprByPpid(String cpTitle, String ppid) {
		List<CollectionProtocolRegistration> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_BY_CP_TITLE_AND_PPID)
				.setString("title", cpTitle)
				.setString("ppid", ppid)
				.list();
		
		return CollectionUtils.isEmpty(result) ? null : result.iterator().next();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public CollectionProtocolRegistration getCprByCpShortTitleAndPpid(String cpShortTitle, String ppid) {
		List<CollectionProtocolRegistration> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_BY_CP_SHORT_TITLE_AND_PPID)
				.setString("shortTitle", cpShortTitle)
				.setString("ppid", ppid)
				.list();
		return CollectionUtils.isEmpty(result) ? null : result.iterator().next();
	}

	@Override
	public CollectionProtocolRegistration getCprByCpShortTitleAndEmpi(String cpShortTitle, String empi) {
		List<CollectionProtocolRegistration> result = getCurrentSession()
				.getNamedQuery(GET_BY_CP_SHORT_TITLE_AND_EMPI)
				.setString("shortTitle", cpShortTitle)
				.setString("empi", empi)
				.list();
		return CollectionUtils.isEmpty(result) ? null : result.iterator().next();
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

	@Override
	public Map<String, Object> getCprIds(String key, Object value) {
		List<Object[]> rows = getCurrentSession().createCriteria(CollectionProtocolRegistration.class)
			.createAlias("collectionProtocol", "cp")
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("id"))
					.add(Projections.property("cp.id")))
			.add(Restrictions.eq(key, value))
			.list();

		if (CollectionUtils.isEmpty(rows)) {
			return Collections.emptyMap();
		}

		Object[] row = rows.iterator().next();
		Map<String, Object> ids = new HashMap<>();
		ids.put("cprId", row[0]);
		ids.put("cpId", row[1]);
		return ids;
	}

	private Criteria getCprListQuery(CprListCriteria cprCrit) {
		Criteria query = getSessionFactory().getCurrentSession()
			.createCriteria(CollectionProtocolRegistration.class)
			.createAlias("collectionProtocol", "cp")
			.createAlias("participant", "participant")
			.add(Restrictions.ne("activityStatus", "Disabled"))
			.add(Restrictions.ne("cp.activityStatus", "Disabled"))
			.add(Restrictions.ne("participant.activityStatus", "Disabled"));

		addCpRestrictions(query, cprCrit);
		addRegDateCondition(query, cprCrit);
		addMrnEmpiUidCondition(query, cprCrit);
		addNamePpidAndUidCondition(query, cprCrit);
		addDobCondition(query, cprCrit);
		addSpecimenCondition(query, cprCrit);
		addSiteCpsCond(query, cprCrit);
		return query;		
	}

	private void addCpRestrictions(Criteria query, CprListCriteria cprCrit) {
		if (cprCrit.cpId() == null || cprCrit.cpId() == -1) {
			return;
		}

		query.add(Restrictions.eq("cp.id", cprCrit.cpId()));
	}

	private void addRegDateCondition(Criteria query, CprListCriteria crit) {
		if (crit.registrationDate() == null) {
			return;
		}

		Date from = Utility.chopTime(crit.registrationDate());
		Date to = Utility.getEndOfDay(crit.registrationDate());
		query.add(Restrictions.between("registrationDate", from, to));
	}
	
	private void addMrnEmpiUidCondition(Criteria query, CprListCriteria crit) {
		if (!crit.includePhi() || StringUtils.isBlank(crit.participantId())) {
			return;
		}

		Junction cond = Restrictions.disjunction()
			.add(Restrictions.ilike("pmi.medicalRecordNumber", crit.participantId(), MatchMode.ANYWHERE))
			.add(Restrictions.ilike("participant.empi", crit.participantId(), MatchMode.ANYWHERE))
			.add(Restrictions.ilike("participant.uid", crit.participantId(), MatchMode.ANYWHERE));

		query.createAlias("participant.pmis", "pmi", JoinType.LEFT_OUTER_JOIN).add(cond);
	}
	
	private void addNamePpidAndUidCondition(Criteria query, CprListCriteria crit) {
		if (StringUtils.isNotBlank(crit.query())) {
			Junction cond = Restrictions.disjunction()
				.add(Restrictions.ilike("ppid", crit.query(), MatchMode.ANYWHERE));

			if (crit.includePhi()) {
				cond.add(Restrictions.ilike("participant.firstName", crit.query(), MatchMode.ANYWHERE));
				cond.add(Restrictions.ilike("participant.lastName", crit.query(), MatchMode.ANYWHERE));
				cond.add(Restrictions.ilike("participant.uid", crit.query(), MatchMode.ANYWHERE));
			}
			
			query.add(cond);
			return;
		}

		if (StringUtils.isNotBlank(crit.ppid())) {
			query.add(Restrictions.ilike("ppid", crit.ppid(), crit.matchMode()));
		}

		if (!crit.includePhi()) {
			return;
		}
		
		if (StringUtils.isNotBlank(crit.uid())) {
			query.add(Restrictions.ilike("participant.uid", crit.uid(), crit.matchMode()));
		}
		
		if (StringUtils.isNotBlank(crit.name())) {
			query.add(Restrictions.disjunction()
				.add(Restrictions.ilike("participant.firstName", crit.name(), MatchMode.ANYWHERE))
				.add(Restrictions.ilike("participant.lastName", crit.name(), MatchMode.ANYWHERE))
			);
		}
	}
	
	private void addDobCondition(Criteria query, CprListCriteria crit) {
		if (!crit.includePhi() || crit.dob() == null) {
			return;
		}

		Date from = Utility.chopTime(crit.dob());
		Date to = Utility.getEndOfDay(crit.dob());
		query.add(Restrictions.between("participant.birthDate", from, to));
	}
	
	private void addSpecimenCondition(Criteria query, CprListCriteria crit) {
		if (StringUtils.isBlank(crit.specimen())) {
			return;
		}
		
		query.createAlias("visits", "visit")
			.createAlias("visit.specimens", "specimen")
			.add(Restrictions.disjunction()
					.add(Restrictions.ilike("specimen.label", crit.specimen(), MatchMode.ANYWHERE))
					.add(Restrictions.ilike("specimen.barcode", crit.specimen(), MatchMode.ANYWHERE)))
			.add(Restrictions.ne("specimen.activityStatus", "Disabled"))
			.add(Restrictions.ne("visit.activityStatus", "Disabled"));
	}

	private void addSiteCpsCond(Criteria query, CprListCriteria crit) {
		if (CollectionUtils.isEmpty(crit.siteCps())) {
			return;
		}

		Set<Pair<Set<Long>, Long>> siteCps;
		if (crit.includePhi() && crit.hasPhiFields()) {
			//
			// User has phi access and list search criteria is based on one or
			// more phi fields
			//
			siteCps = crit.phiSiteCps();
		} else {
			siteCps = crit.siteCps();
		}

		query.createAlias("cp.sites", "cpSite").createAlias("cpSite.site", "site");
		if (crit.useMrnSites()) {
			if (StringUtils.isBlank(crit.participantId())) {
				query.createAlias("participant.pmis", "pmi", JoinType.LEFT_OUTER_JOIN);
			}

			query.createAlias("pmi.site", "mrnSite", JoinType.LEFT_OUTER_JOIN);
		}

		Disjunction cpSitesCond = Restrictions.disjunction();
		for (Pair<Set<Long>, Long> siteCp : siteCps) {
			Set<Long> siteIds = siteCp.first();
			Long cpId = siteCp.second();

			Junction siteCond = Restrictions.disjunction();
			if (crit.useMrnSites()) {
				//
				// When MRNs exist, site ID should be one of the MRN site
				//
				Junction mrnSite = Restrictions.conjunction()
					.add(Restrictions.isNotNull("pmi.id"))
					.add(Restrictions.in("mrnSite.id", siteIds));

				//
				// When no MRNs exist, site ID should be one of CP site
				//
				Junction cpSite = Restrictions.conjunction()
					.add(Restrictions.isNull("pmi.id"))
					.add(Restrictions.in("site.id", siteIds));

				siteCond.add(mrnSite).add(cpSite);
			} else {
				//
				// Site ID should be CP site
				//
				siteCond.add(Restrictions.in("site.id", siteIds));
			}

			Junction cond = Restrictions.conjunction().add(siteCond);
			if (cpId != null && !cpId.equals(crit.cpId())) {
				cond.add(Restrictions.eq("cp.id", cpId));
			}

			cpSitesCond.add(cond);
		}

		query.add(cpSitesCond);
	}

	private Projection getCprSummaryFields(CprListCriteria cprCrit) {
		ProjectionList projs = Projections.projectionList()
				.add(Projections.property("id"))
				.add(Projections.property("ppid"))
				.add(Projections.property("registrationDate"))
				.add(Projections.property("cp.id"))
				.add(Projections.property("cp.shortTitle"))
				.add(Projections.property("participant.id"));

		if (cprCrit.includePhi()) {
			projs.add(Projections.property("participant.firstName"))
				.add(Projections.property("participant.lastName"))
				.add(Projections.property("participant.empi"))
				.add(Projections.property("participant.uid"));
		}
		return Projections.distinct(projs);
	}
	
	private CprSummary getCprSummary(Object[] row, boolean allCpsAccess, Set<Long> phiCps) {
		int idx = 0;
		CprSummary cpr = new CprSummary();
		cpr.setCprId((Long)row[idx++]);
		cpr.setPpid((String)row[idx++]);
		cpr.setRegistrationDate((Date)row[idx++]);
		cpr.setCpId((Long)row[idx++]);
		cpr.setCpShortTitle((String)row[idx++]);

		ParticipantSummary participant = new ParticipantSummary();
		cpr.setParticipant(participant);
		participant.setId((Long)row[idx++]);
		if (row.length > idx && (allCpsAccess || (phiCps != null && phiCps.contains(cpr.getCpId())))) {
			participant.setFirstName((String)row[idx++]);
			participant.setLastName((String) row[idx++]);
			participant.setEmpi((String) row[idx++]);
			participant.setUid((String) row[idx++]);
		}
		
		return cpr;
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getScgAndSpecimenCounts(CprListCriteria cprCrit) {
		Criteria countQuery = getCprListQuery(cprCrit)
				.addOrder(Order.asc("id"))
				.setFirstResult(cprCrit.startAt())
				.setMaxResults(cprCrit.maxResults());
		
		if (StringUtils.isBlank(cprCrit.specimen())) {
			countQuery
				.createAlias("visits", "visit",
					JoinType.LEFT_OUTER_JOIN, Restrictions.eq("visit.status", "Complete"))
				.createAlias("visit.specimens", "specimen",
					JoinType.LEFT_OUTER_JOIN, Restrictions.eq("specimen.collectionStatus", "Collected"));
		}
		
		return countQuery.setProjection(Projections.projectionList()
				.add(Projections.property("id"))
				.add(Projections.countDistinct("visit.id"))
				.add(Projections.countDistinct("specimen.id"))
				.add(Projections.groupProperty("id")))
				.list();
	}
	
	private static final String FQN = CollectionProtocolRegistration.class.getName();
	
	private static final String GET_BY_CP_ID_AND_PPID = FQN + ".getCprByCpIdAndPpid";
	
	private static final String GET_BY_CP_TITLE_AND_PPID = FQN + ".getCprByCpTitleAndPpid";
	
	private static final String GET_BY_CP_SHORT_TITLE_AND_PPID = FQN + ".getCprByCpShortTitleAndPpid";

	private static final String GET_BY_CP_SHORT_TITLE_AND_EMPI = FQN + ".getCprByCpShortTitleAndEmpi";
	
	private static final String GET_BY_CP_ID_AND_PID = FQN + ".getCprByCpIdAndPid";

	private static final String GET_BY_CP_ID = FQN + ".getCprsByCpId";
}
