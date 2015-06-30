
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
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
	
	private static final String FQN = DistributionProtocol.class.getName();

	private static final String GET_DP_BY_TITLE = FQN + ".getDistributionProtocolByTitle";

	private static final String GET_DP_BY_SHORT_TITLE = FQN + ".getDistributionProtocolByShortTitle";
	
	private static final String GET_SPMN_COUNT_BY_DPS = FQN + ".getSpmnCountByDps";
}
