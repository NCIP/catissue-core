
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderStat;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderStatListCriteria;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolSummary;
import com.krishagni.catissueplus.core.administrative.repository.DistributionProtocolDao;
import com.krishagni.catissueplus.core.administrative.repository.DpListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Utility;

public class DistributionProtocolDaoImpl extends AbstractDao<DistributionProtocol> implements DistributionProtocolDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<DistributionProtocol> getDistributionProtocols(DpListCriteria crit) {
		Criteria query = getDpListQuery(crit)
				.setFirstResult(crit.startAt())
				.setMaxResults(crit.maxResults())
				.addOrder(Order.asc("title"));

		return query.list();
	}

	@Override
	public Long getDistributionProtocolsCount(DpListCriteria criteria) {
		Number count = (Number) getDpListQuery(criteria)
				.setProjection(Projections.rowCount())
				.uniqueResult();
		return count.longValue();
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
	public List<DistributionProtocol> getExpiringDps(Date fromDate, Date toDate) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_EXPIRING_DPS)
				.setDate("fromDate", fromDate)
				.setDate("toDate", toDate)
				.list();
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
	public List<DistributionOrderStat> getOrderStats(DistributionOrderStatListCriteria listCrit) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(DistributionOrder.class)
				.createAlias("orderItems", "item")
				.createAlias("item.specimen", "specimen");

		query.add(Restrictions.eq("status", DistributionOrder.Status.EXECUTED));
		if (listCrit.dpId() != null) {
			query.add(Restrictions.eq("distributionProtocol.id", listCrit.dpId()));
		} else if (CollectionUtils.isNotEmpty(listCrit.siteIds())) {
			query.createAlias("distributionProtocol", "dp")
				.createAlias("dp.distributingSites", "distSites");
			
			addSitesCondition(query, listCrit.siteIds());
		}
		
		addOrderStatProjections(query, listCrit);
		
		List<Object []> rows = query.list();
		List<DistributionOrderStat> result = new ArrayList<DistributionOrderStat>();
		for (Object[] row : rows) {
			DistributionOrderStat detail = getDOStats(row, listCrit);
			result.add(detail);
		}
		
		return result;
	}

	@Override
	public Map<String, Object> getDpIds(String key, Object value) {
		return getObjectIds("dpId", key, value);
	}

	private Criteria getDpListQuery(DpListCriteria crit) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(DistributionProtocol.class)
			.add(Restrictions.ne("activityStatus", "Disabled"));

		return addSearchConditions(query, crit);
	}

	private Criteria addSearchConditions(Criteria query, DpListCriteria crit) {
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
		addExpiredDpsCondition(query, crit);
		addActivityStatusCondition(query, crit);
		return query;
	}
	
	private void addPiCondition(Criteria query, DpListCriteria crit) {
		Long piId = crit.piId();
		if (piId == null) {
			return;
		}
		
		query.add(Restrictions.eq("principalInvestigator.id", piId));
	}
	
	private void addInstCondition(Criteria query, DpListCriteria crit) {
		if (StringUtils.isBlank(crit.receivingInstitute())) {
			return;
		}

		query.createAlias("institute", "institute")
			.add(Restrictions.eq("institute.name", crit.receivingInstitute().trim()));
	}
	
	private void addDistSitesCondition(Criteria query, DpListCriteria crit) {
		Set<Long> siteIds = crit.siteIds();
		if (CollectionUtils.isEmpty(siteIds)) {
			return;
		}
		
		query.createAlias("distributingSites", "distSites");
		addSitesCondition(query, siteIds);
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	}

	private void addExpiredDpsCondition(Criteria query, DpListCriteria crit) {
		if (!crit.excludeExpiredDps()) {
			return;
		}

		Date today = Utility.chopTime(Calendar.getInstance().getTime());
		query.add(Restrictions.or(
			Restrictions.isNull("endDate"),
			Restrictions.ge("endDate", today)));
	}
	
	private void addActivityStatusCondition(Criteria query, DpListCriteria crit) {
		String activityStatus = crit.activityStatus();
		if (StringUtils.isBlank(activityStatus)) {
			return;
		}
		
		query.add(Restrictions.eq("activityStatus", activityStatus));
	}
	
	private void addOrderStatProjections(Criteria query, DistributionOrderStatListCriteria crit) {
		ProjectionList projs = Projections.projectionList();
		
		projs.add(Projections.groupProperty("id"));
		projs.add(Projections.groupProperty("name"));
		projs.add(Projections.groupProperty("distributionProtocol"));
		projs.add(Projections.groupProperty("executionDate"));
		projs.add(Projections.count("specimen.specimenType"));
		
		Map<String, String> props = getProps();
		
		for (String attr : crit.groupByAttrs()) {
			String prop = props.get(attr);
			projs.add(Projections.groupProperty(prop));
		}
		
		query.setProjection(projs);
	}
	
	private Map<String, String> getProps() {
		Map<String, String> props = new HashMap<String, String>();
		props.put("specimenType", "specimen.specimenType");
		props.put("anatomicSite", "specimen.tissueSite");
		props.put("pathologyStatus", "specimen.pathologicalStatus");

		return props;
	}
	
	private DistributionOrderStat getDOStats(Object[] row, DistributionOrderStatListCriteria crit) {
		DistributionOrderStat stat = new DistributionOrderStat();
		int index = 0;
		
		stat.setId((Long)row[index++]);
		stat.setName((String)row[index++]);
		stat.setDistributionProtocol(DistributionProtocolSummary.from((DistributionProtocol)row[index++]));
		stat.setExecutionDate((Date)row[index++]);
		stat.setDistributedSpecimenCount((Long)row[index++]);
		
		for (String attr : crit.groupByAttrs()) {
			stat.getGroupByAttrVals().put(attr, row[index++]);
		}
		
		return stat;
	}
	
	private void addSitesCondition(Criteria query, Set<Long> siteIds) {
		query.createAlias("distSites.site", "distSite", JoinType.LEFT_OUTER_JOIN)
			.createAlias("distSites.institute", "distInst")
			.createAlias("distInst.sites", "instSite")
			.add(Restrictions.or(
				Restrictions.and(Restrictions.isNull("distSites.site"), Restrictions.in("instSite.id", siteIds)),
				Restrictions.and(Restrictions.isNotNull("distSites.site"),Restrictions.in("distSite.id", siteIds))
			));
	}
	
	private static final String FQN = DistributionProtocol.class.getName();

	private static final String GET_DP_BY_TITLE = FQN + ".getDistributionProtocolByTitle";

	private static final String GET_DP_BY_SHORT_TITLE = FQN + ".getDistributionProtocolByShortTitle";
	
	private static final String GET_EXPIRING_DPS = FQN + ".getExpiringDps";
	
	private static final String GET_SPMN_COUNT_BY_DPS = FQN + ".getSpmnCountByDps";
}
