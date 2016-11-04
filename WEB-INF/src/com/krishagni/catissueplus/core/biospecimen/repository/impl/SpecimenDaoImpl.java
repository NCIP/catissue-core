
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListCriteria;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SpecimenDaoImpl extends AbstractDao<Specimen> implements SpecimenDao {
	public Class<?> getType() {
		return Specimen.class;
	}

	@SuppressWarnings("unchecked")
	public List<Specimen> getSpecimens(SpecimenListCriteria crit) {
		Criteria query = getSessionFactory().getCurrentSession()
			.createCriteria(Specimen.class, "specimen")
			.addOrder(Order.asc("specimen.id"))
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (CollectionUtils.isNotEmpty(crit.ids())) {
			addIdsCond(query, crit.ids());
		} else if (CollectionUtils.isNotEmpty(crit.labels())) {
			if (crit.labels().size() == 1) {
				addLabelCond(query, crit.labels().iterator().next(), crit.matchMode());
			} else {
				addLabelsCond(query, crit.labels());
			}
		}


		addLineageCond(query, crit);
		addCollectionStatusCond(query, crit);
		addSiteCpsCond(query, crit);
		addCpCond(query, crit);
		addSpecimenListCond(query, crit);
		addStorageLocationSiteCond(query, crit);

		if (crit.limitItems()) {
			query.setFirstResult(crit.startAt()).setMaxResults(crit.maxResults());
		}

		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Specimen getByLabel(String label) {
		List<Specimen> specimens = sessionFactory.getCurrentSession()
			.getNamedQuery(GET_BY_LABEL)
			.setString("label", label)
			.list();
		return specimens.isEmpty() ? null : specimens.iterator().next();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Specimen getByLabelAndCp(String cpShortTitle, String label) {
		List<Specimen> specimens = getCurrentSession().getNamedQuery(GET_BY_LABEL_AND_CP)
			.setString("label", label)
			.setString("cpShortTitle", cpShortTitle)
			.list();

		return specimens.isEmpty() ? null : specimens.iterator().next();
	}
	
	@Override
	public Specimen getSpecimenByVisitAndSr(Long visitId, Long srId) {
		return getByVisitAndSrId(GET_BY_VISIT_AND_SR, visitId, srId);
	}

	@Override
	public Specimen getParentSpecimenByVisitAndSr(Long visitId, Long srId) {
		return getByVisitAndSrId(GET_PARENT_BY_VISIT_AND_SR, visitId, srId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Specimen getByBarcode(String barcode) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Specimen.class);
		query.add(Restrictions.eq("barcode", barcode));
		List<Specimen> specimens = query.list();
		
		return specimens.isEmpty() ? null : specimens.iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Specimen> getSpecimensByIds(List<Long> specimenIds) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_BY_IDS)
				.setParameterList("specimenIds", specimenIds)
				.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Specimen> getSpecimensByVisitId(Long visitId) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_BY_VISIT_ID)
				.setLong("visitId", visitId)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Specimen> getSpecimensByVisitName(String visitName) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_BY_VISIT_NAME)
				.setString("visitName", visitName)
				.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getCprAndVisitIds(String key, Object value) {
		List<Object[]> rows = getCurrentSession().createCriteria(Specimen.class)
			.createAlias("visit", "visit")
			.createAlias("visit.registration", "cpr")
			.createAlias("cpr.collectionProtocol", "cp")
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("cp.id"))
					.add(Projections.property("cpr.id"))
					.add(Projections.property("visit.id"))
					.add(Projections.property("id")))
			.add(Restrictions.eq(key, value))
			.list();

		if (CollectionUtils.isEmpty(rows)) {
			return null;
		}
		
		Map<String, Object> result = new HashMap<>();
		Object[] row = rows.iterator().next();
		result.put("cpId",       row[0]);
		result.put("cprId",      row[1]);
		result.put("visitId",    row[2]);
		result.put("specimenId", row[3]);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, Set<Long>> getSpecimenSites(Set<Long> specimenIds) {
		Criteria query = getSessionFactory().getCurrentSession().createCriteria(Specimen.class)
				.createAlias("visit", "visit")
				.createAlias("visit.registration", "cpr")
				.createAlias("cpr.collectionProtocol", "cp")
				.createAlias("cp.sites", "cpSite")
				.createAlias("cpSite.site", "site");
		
		ProjectionList projs = Projections.projectionList();
		query.setProjection(projs);
		projs.add(Projections.property("id"));
		projs.add(Projections.property("site.id"));
		query.add(Restrictions.in("id", specimenIds));
		
		List<Object []> rows = query.list();
		Map<Long, Set<Long>> results = new HashMap<>();
		for (Object[] row: rows) {
			Long id = (Long)row[0];
			Long siteId = (Long)row[1];
			Set<Long> siteIds = results.get(id);
			if (siteIds == null) {
				siteIds = new HashSet<Long>();
				results.put(id, siteIds);
			}
			
			siteIds.add(siteId);
		}
		
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, String> getDistributionStatus(List<Long> specimenIds) {
		List<Object[]> rows = getSessionFactory().getCurrentSession()
			.getNamedQuery(GET_LATEST_DISTRIBUTION_AND_RETURN_DATES)
			.setParameterList("specimenIds", specimenIds)
			.list();

		return rows.stream().collect(
			Collectors.toMap(
				row -> (Long)row[0],
				row -> getDistributionStatus((Date)row[1], (Date)row[2])
			));
	}

	@Override
	public String getDistributionStatus(Long specimenId) {
		Map<Long, String> statuses = getDistributionStatus(Collections.singletonList(specimenId));
		return statuses.get(specimenId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Visit> getSpecimenVisits(SpecimenListCriteria crit) {
		boolean noLabels = CollectionUtils.isEmpty(crit.labels());
		boolean noIds = CollectionUtils.isEmpty(crit.ids());

		if (noLabels && noIds && crit.specimenListId() == null) {
			throw new IllegalArgumentException("No limiting condition on specimens");
		}

		Criteria query = getSessionFactory().getCurrentSession()
			.createCriteria(Visit.class, "visit")
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.createAlias("visit.specimens", "specimen");

		if (!noIds) {
			addIdsCond(query, crit.ids());
		} else if (!noLabels) {
			addLabelsCond(query, crit.labels());
		}

		addSiteCpsCond(query, crit);
		addSpecimenListCond(query, crit);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean areDuplicateLabelsPresent() {
		List<Object[]> rows = getSessionFactory().getCurrentSession()
			.getNamedQuery(GET_DUPLICATE_LABEL_COUNT)
			.list();

		return rows.size() > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, Long> getSpecimenStorageSite(Set<Long> specimenIds) {
		List<Object[]> rows = getCurrentSession().getNamedQuery(GET_STORAGE_SITE)
			.setParameterList("specimenIds", specimenIds)
			.list();

		// null value for site means virtual specimen
		HashMap<Long, Long> result = new HashMap<>();
		rows.forEach((row) -> result.put((Long)row[0], (Long)row[1]));
		return result;
	}

	private void addIdsCond(Criteria query, List<Long> ids) {
		addInCond(query, "specimen.id", ids);
	}

	private void addLabelCond(Criteria query, String label, MatchMode matchMode) {
		if (matchMode == MatchMode.EXACT) {
			query.add(Restrictions.eq("specimen.label", label));
		} else {
			query.add(Restrictions.ilike("specimen.label", label, matchMode));
		}
	}

	private void addLabelsCond(Criteria query, List<String> labels) {
		addInCond(query, "specimen.label", labels);
	}

	private <T> void addInCond(Criteria query, String property, List<T> values) {
		int numValues = values.size();
		Disjunction labelIn = Restrictions.disjunction();

		for (int i = 0; i < numValues; i += 500) {
			List<T> params = values.subList(i, i + 500 > numValues ? numValues : i + 500);
			labelIn.add(Restrictions.in(property, params));
			i += 500;
		}

		query.add(labelIn);
	}

	private void addLineageCond(Criteria query, SpecimenListCriteria crit) {
		if (crit.lineages() == null || crit.lineages().length == 0) {
			return;
		}

		query.add(Restrictions.in("lineage", crit.lineages()));
	}

	private void addCollectionStatusCond(Criteria query, SpecimenListCriteria crit) {
		if (crit.collectionStatuses() == null || crit.collectionStatuses().length == 0) {
			return;
		}

		query.add(Restrictions.in("collectionStatus", crit.collectionStatuses()));
	}

	private void addSiteCpsCond(Criteria query, SpecimenListCriteria crit) {
		if (CollectionUtils.isEmpty(crit.siteCps())) {
			return;
		}

		if (!query.getAlias().equals("visit")) {
			query.createAlias("specimen.visit", "visit");
		}

		query.createAlias("visit.registration", "cpr")
			.createAlias("cpr.collectionProtocol", "cp")
			.createAlias("cp.sites", "cpSite")
			.createAlias("cpSite.site", "site")
			.createAlias("cpr.participant", "participant")
			.createAlias("participant.pmis", "pmi", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pmi.site", "mrnSite", JoinType.LEFT_OUTER_JOIN);

		Disjunction cpSitesCond = Restrictions.disjunction();
		for (Pair<Long, Long> siteCp : crit.siteCps()) {
			Long siteId = siteCp.first();
			Long cpId = siteCp.second();


			Junction siteCond = Restrictions.disjunction();
			if (crit.useMrnSites()) {
				//
				// When MRNs exist, site ID should be one of the MRN site
				//
				Junction mrnSite = Restrictions.conjunction()
					.add(Restrictions.isNotEmpty("participant.pmis"))
					.add(Restrictions.eq("mrnSite.id", siteId));

				//
				// When no MRNs exist, site ID should be one of CP site
				//
				Junction cpSite = Restrictions.conjunction()
					.add(Restrictions.isEmpty("participant.pmis"))
					.add(Restrictions.eq("site.id", siteId));

				siteCond.add(mrnSite).add(cpSite);
			} else {
				//
				// Site ID should be either MRN site or CP site
				//
				siteCond
					.add(Restrictions.eq("mrnSite.id", siteId))
					.add(Restrictions.eq("site.id", siteId));
			}

			Junction cond = Restrictions.conjunction().add(siteCond);
			if (cpId != null) {
				cond.add(Restrictions.eq("cp.id", cpId));
			}
		
			cpSitesCond.add(cond);
		}
		
		query.add(cpSitesCond);
	}

	private void addCpCond(Criteria query, SpecimenListCriteria crit) {
		if (crit.cpId() == null) {
			return;
		}

		if (CollectionUtils.isEmpty(crit.siteCps())) {
			if (!query.getAlias().equals("visit")) {
				query.createAlias("specimen.visit", "visit");
			}

			query.createAlias("visit.registration", "cpr")
				.createAlias("cpr.collectionProtocol", "cp");
		}

		query.add(Restrictions.eq("cp.id", crit.cpId()));
	}

	private void addSpecimenListCond(Criteria query, SpecimenListCriteria crit) {
		if (crit.specimenListId() == null) {
			return;
		}

		query.createAlias("specimen.specimenLists", "list").add(Restrictions.eq("list.id", crit.specimenListId()));
	}

	private void addStorageLocationSiteCond(Criteria query, SpecimenListCriteria crit) {
		if (StringUtils.isBlank(crit.storageLocationSite())) {
			return;
		}

		query.createAlias("specimen.position", "pos", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pos.container", "cont", JoinType.LEFT_OUTER_JOIN)
			.createAlias("cont.site", "contSite", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.or(
				Restrictions.isNull("pos.id"),
				Restrictions.eq("contSite.name", crit.storageLocationSite())
			));
	}

	@SuppressWarnings("unchecked")
	private Specimen getByVisitAndSrId(String hql, Long visitId, Long srId) {
		List<Specimen> specimens = sessionFactory.getCurrentSession()
				.getNamedQuery(hql)
				.setLong("visitId", visitId)
				.setLong("srId", srId)
				.list();
		return specimens.isEmpty() ? null : specimens.iterator().next();
	}

	private String getDistributionStatus(Date execDate, Date returnDate) {
		return (returnDate == null || execDate.after(returnDate)) ? "Distributed" : "Returned";
	}

	private static final String FQN = Specimen.class.getName();
	
	private static final String GET_BY_LABEL = FQN + ".getByLabel";

	private static final String GET_BY_LABEL_AND_CP = FQN + ".getByLabelAndCp";
	
	private static final String GET_BY_VISIT_AND_SR = FQN + ".getByVisitAndReq";

	private static final String GET_PARENT_BY_VISIT_AND_SR = FQN + ".getParentByVisitAndReq";
	
	private static final String GET_BY_IDS = FQN + ".getByIds";
	
	private static final String GET_BY_VISIT_ID = FQN + ".getByVisitId";
	
	private static final String GET_BY_VISIT_NAME = FQN + ".getByVisitName";
	
	private static final String GET_LATEST_DISTRIBUTION_AND_RETURN_DATES = FQN + ".getLatestDistributionAndReturnDates";

	private static final String GET_DUPLICATE_LABEL_COUNT = FQN + ".getDuplicateLabelCount";

	private static final String GET_STORAGE_SITE = FQN + ".getStorageSite";
}
