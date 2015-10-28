
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
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
		Criteria query = getSessionFactory().getCurrentSession().createCriteria(Specimen.class)
				.addOrder(Order.asc("id"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<String> labels = crit.labels();
		if (CollectionUtils.isNotEmpty(labels)) {
			addLabelsCond(query, labels);
		} else {
			query.setFirstResult(crit.startAt() < 0 ? 0 : crit.startAt())
				.setMaxResults(crit.maxResults() <= 0 ? 100 : crit.maxResults());
		}
		
		if (CollectionUtils.isNotEmpty(crit.siteCps())) {
			addSiteCpsCond(query, crit.siteCps());
		}
		
		if (crit.specimenListId() != null) {
			query.createAlias("specimenLists", "list")
				.add(Restrictions.eq("list.id", crit.specimenListId()));
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
	public Map<String, Long> getCprAndVisitIds(Long specimenId) {
		List<Object[]> rows = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CPR_AND_VISIT_IDS)
				.setLong("specimenId", specimenId)
				.list();
		
		if (CollectionUtils.isEmpty(rows)) {
			return null;
		}
		
		Map<String, Long> result = new HashMap<String, Long>();
		Object[] row = rows.iterator().next();
		result.put("cpId", (Long)row[0]);
		result.put("cprId", (Long)row[1]);
		result.put("visitId", (Long)row[2]);
		result.put("specimenId", specimenId);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Set<Long>> getSpecimenSites(Set<Long> specimenIds) {
		Criteria query = getSessionFactory().getCurrentSession().createCriteria(Specimen.class)
				.createAlias("visit", "visit")
				.createAlias("visit.registration", "cpr")
				.createAlias("cpr.collectionProtocol", "cp")
				.createAlias("cp.sites", "cpSite")
				.createAlias("cpSite.site", "site");
		
		ProjectionList projs = Projections.projectionList();
		query.setProjection(projs);
		projs.add(Projections.property("label"));
		projs.add(Projections.property("site.id"));
		query.add(Restrictions.in("id", specimenIds));
		
		List<Object []> rows = query.list();
		Map<String, Set<Long>> results = new HashMap<String, Set<Long>>();
		for (Object[] row: rows) {
			String label = (String)row[0];
			Long siteId = (Long)row[1];
			Set<Long> siteIds = results.get(label);
			if (siteIds == null) {
				siteIds = new HashSet<Long>();
				results.put(label, siteIds);
			}
			
			siteIds.add(siteId);
		}
		
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getDistributedSpecimens(List<Long> specimenIds) {
		return (List<Long>) getSessionFactory().getCurrentSession()
				.getNamedQuery(GET_DISTRIBUTED_SPECIMENS)
				.setParameterList("specimenIds", specimenIds)
				.list();
	}

	private void addLabelsCond(Criteria query, List<String> labels) {
		int numLabels = labels.size();		
		Disjunction labelIn = Restrictions.disjunction();
		
		for (int i = 0; i < numLabels; i += 500) {
			List<String> params = labels.subList(i, i + 500 > numLabels ? numLabels : i + 500);
			labelIn.add(Restrictions.in("label", params));
			i += 500;
		}
		
		query.add(labelIn);		
	}
	
	private void addSiteCpsCond(Criteria query, List<Pair<Long, Long>> siteCps) {
		query.createAlias("visit", "visit")
			.createAlias("visit.registration", "cpr")
			.createAlias("cpr.collectionProtocol", "cp")
			.createAlias("cp.sites", "cpSite")
			.createAlias("cpSite.site", "site")
			.createAlias("cpr.participant", "participant")
			.createAlias("participant.pmis", "pmi", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pmi.site", "mrnSite", JoinType.LEFT_OUTER_JOIN);
		
		Disjunction cpSitesCond = Restrictions.disjunction();
		for (Pair<Long, Long> siteCp : siteCps) {
			Junction cond = Restrictions.conjunction();
			
			Long siteId = siteCp.first();
			Long cpId = siteCp.second();
		
			cond.add(
				/** mrn site = siteId or cp site = siteId **/
				Restrictions.disjunction()
					.add(Restrictions.eq("mrnSite.id", siteId))
					.add(Restrictions.eq("site.id", siteId))
			);

			if (cpId != null) {
				cond.add(Restrictions.eq("cp.id", cpId));
			}
		
			cpSitesCond.add(cond);
		}
		
		query.add(cpSitesCond);
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

	private static final String FQN = Specimen.class.getName();
	
	private static final String GET_BY_LABEL = FQN + ".getByLabel";
	
	private static final String GET_BY_VISIT_AND_SR = FQN + ".getByVisitAndReq";

	private static final String GET_PARENT_BY_VISIT_AND_SR = FQN + ".getParentByVisitAndReq";
	
	private static final String GET_BY_IDS = FQN + ".getByIds";
	
	private static final String GET_BY_VISIT_ID = FQN + ".getByVisitId";
	
	private static final String GET_BY_VISIT_NAME = FQN + ".getByVisitName";
	
	private static final String GET_CPR_AND_VISIT_IDS = FQN + ".getCprAndVisitIds";

	private static final String GET_DISTRIBUTED_SPECIMENS = FQN + ".getDistributedSpecimens";
}
