
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderSpecReqDetails;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderSpecReqListCriteria;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolSummary;
import com.krishagni.catissueplus.core.administrative.repository.DistributionProtocolDao;
import com.krishagni.catissueplus.core.administrative.repository.DpListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class DistributionProtocolDaoImpl extends AbstractDao<DistributionProtocol> implements DistributionProtocolDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<DistributionProtocol> getDistributionProtocols(DpListCriteria crit) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(DistributionProtocol.class)
				.setFirstResult(crit.startAt())
				.setMaxResults(crit.maxResults())
				.add(Restrictions.ne("activityStatus", "Disabled"))
				.addOrder(Order.asc("title"));

		addSearchConditions(query, crit);		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public DistributionProtocol getByShortTitle(String shortTitle) {
		List<DistributionProtocol> dps = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_DP_BY_SHORT_TITLE)
				.setString("shortTitle", shortTitle)
				.list();		
		return CollectionUtils.isNotEmpty(dps) ? dps.iterator().next() : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DistributionProtocol getDistributionProtocol(String title) {
		List<DistributionProtocol> dps = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_DP_BY_TITLE)
				.setString("title", title)
				.list();
		return CollectionUtils.isNotEmpty(dps) ? dps.iterator().next() : null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, Integer> getSpecimensCountByDpIds(Collection<Long> dpIds) {
		List<Object[]> rows = getSessionFactory().getCurrentSession()
				.getNamedQuery(GET_SPMN_COUNT_BY_DPS)
				.setParameterList("dpIds", dpIds)
				.list();
		
		Map<Long, Integer> countMap = new HashMap<Long, Integer>();
		for (Object[] row : rows) {
			countMap.put((Long)row[0], ((Long)row[1]).intValue());
		}
		
		return countMap;
	}
	
	public Class<DistributionProtocol> getType() {
		return DistributionProtocol.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DistributionOrderSpecReqDetails> getOrderSpecifications(DistributionOrderSpecReqListCriteria listCrit) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(DistributionOrder.class)
				.createAlias("orderItems", "item")
				.createAlias("item.specimen", "specimen");

		query.add(Restrictions.eq("status", DistributionOrder.Status.EXECUTED));
		if (listCrit.dpId() != null) {
			query.add(Restrictions.eq("distributionProtocol.id", listCrit.dpId()));
		}
		else {
			if (CollectionUtils.isNotEmpty(listCrit.siteIds())) {
				query.createAlias("distributionProtocol", "dp")
					.createAlias("dp.distributingSites", "distSite")
					.add(Restrictions.in("distSite.id", listCrit.siteIds()));
			}
		}
		
		addOrderSpecProjections(query, listCrit);
		
		List<Object []> rows = query.list();
		List<DistributionOrderSpecReqDetails> result = new ArrayList<DistributionOrderSpecReqDetails>();
		for (Object[] row: rows) {
			DistributionOrderSpecReqDetails detail = getDOSpecDetail(row, listCrit);
			result.add(detail);
		}
		
		return result;
	}

	private void addSearchConditions(Criteria query, DpListCriteria crit) {
		String searchTerm = crit.query();
		
		if (StringUtils.isBlank(searchTerm)) {
			searchTerm = crit.title();
		}
		
		if (StringUtils.isNotBlank(searchTerm)) {
			Junction searchCond = Restrictions.disjunction()
					.add(Restrictions.ilike("title", searchTerm, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("shortTitle", searchTerm, MatchMode.ANYWHERE));
			
			if (StringUtils.isNotBlank(crit.query())) {
				searchCond.add(Restrictions.ilike("irbId", searchTerm, MatchMode.ANYWHERE));
			}
			
			query.add(searchCond);
		}
		
		addPiCondition(query, crit);
		addInstCondition(query, crit);
		addDistSitesCondition(query, crit);
		addActivityStatusCondition(query, crit);
	}
	
	private void addPiCondition(Criteria query, DpListCriteria crit) {
		Long piId = crit.piId();
		if (piId == null) {
			return;
		}
		
		query.add(Restrictions.eq("principalInvestigator.id", piId));
	}
	
	private void addInstCondition(Criteria query, DpListCriteria crit) {
		Long instituteId = crit.instituteId();
		if (instituteId == null) {
			return;
		}
		
		query.add(Restrictions.eq("institute.id", instituteId));
	}
	
	private void addDistSitesCondition(Criteria query, DpListCriteria crit) {
		Set<Long> siteIds = crit.siteIds();
		if (CollectionUtils.isEmpty(siteIds)) {
			return;
		}
		
		query.createAlias("distributingSites", "distSite")
			.add(Restrictions.in("distSite.id", siteIds));
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	}
	
	private void addActivityStatusCondition(Criteria query, DpListCriteria crit) {
		String activityStatus = crit.activityStatus();
		if (StringUtils.isBlank(activityStatus)) {
			return;
		}
		
		query.add(Restrictions.eq("activityStatus", activityStatus));
	}
	
	private void addOrderSpecProjections(Criteria query, DistributionOrderSpecReqListCriteria crit) {
		ProjectionList projs = Projections.projectionList();
		
		projs.add(Projections.property("id"));
		projs.add(Projections.property("name"));
		projs.add(Projections.property("distributionProtocol"));
		projs.add(Projections.property("executionDate"));
		projs.add(Projections.property("specimen.specimenType"));
		projs.add(Projections.property("specimen.tissueSite"));
		projs.add(Projections.property("specimen.pathologicalStatus"));
		projs.add(Projections.count("specimen.specimenType"));
		
		projs.add(Projections.groupProperty("id"));
		if (crit.specimenType()) {
			projs.add(Projections.groupProperty("specimen.specimenType"));
		}
		
		if (crit.anatomicSite()) {
			projs.add(Projections.groupProperty("specimen.tissueSite"));
		}
		
		if (crit.pathologyStatus()) {
			projs.add(Projections.groupProperty("specimen.pathologicalStatus"));
		}
		
		query.setProjection(projs);
	}
	
	private DistributionOrderSpecReqDetails getDOSpecDetail(Object[] row, DistributionOrderSpecReqListCriteria crit) {
		DistributionOrderSpecReqDetails detail = new DistributionOrderSpecReqDetails();
		
		detail.setId((Long)row[0]);
		detail.setName((String)row[1]);
		detail.setDistributionProtocol(DistributionProtocolSummary.from((DistributionProtocol)row[2]));
		detail.setExecutionDate((Date)row[3]);
		if (crit.specimenType()) {
			detail.setSpecimenType((String)row[4]);
		}
		
		if (crit.anatomicSite()) {
			detail.setAnatomicSite((String)row[5]);
		}
		
		if (crit.pathologyStatus()) {
			detail.setPathologyStatus((String)row[6]);
		}
		
		detail.setDistributedSpecimenCount((Long)row[7]);
		
		return detail;
	}
	
	private static final String FQN = DistributionProtocol.class.getName();

	private static final String GET_DP_BY_TITLE = FQN + ".getDistributionProtocolByTitle";

	private static final String GET_DP_BY_SHORT_TITLE = FQN + ".getDistributionProtocolByShortTitle";
	
	private static final String GET_SPMN_COUNT_BY_DPS = FQN + ".getSpmnCountByDps";
}
