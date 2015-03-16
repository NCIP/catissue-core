
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.repository.DistributionProtocolDao;
import com.krishagni.catissueplus.core.administrative.repository.DpListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class DistributionProtocolDaoImpl extends AbstractDao<DistributionProtocol> implements DistributionProtocolDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<DistributionProtocol> getDistributionProtocols(DpListCriteria dpCriteria) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(DistributionProtocol.class)
				.setFirstResult(dpCriteria.startAt())
				.setMaxResults(dpCriteria.maxResults())
				.add(Restrictions.ne("activityStatus", "Disabled"));

		addSearchConditions(query, dpCriteria);
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public DistributionProtocol getByShortTitle(String shortTitle) {
		List<DistributionProtocol> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_DP_BY_SHORT_TITLE)
				.setString("shortTitle", shortTitle)
				.list();
		
		return result.isEmpty() ? null : result.iterator().next();
	}

	@SuppressWarnings("unchecked")
	@Override
	public DistributionProtocol getDistributionProtocol(String title) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_DP_BY_TITLE);
		query.setString("title", title);
		List<DistributionProtocol> list = query.list();
		return list.isEmpty() ? null : list.get(0);
	}
	
	public Class getType() {
		return DistributionProtocol.class;
	}
	
	private void addSearchConditions(Criteria query, DpListCriteria dpCriteria) {
		String searchTerm = dpCriteria.query();
		
		if (StringUtils.isBlank(searchTerm)) {
			addTitleCondition(query, dpCriteria);
		}
		else {
			Junction searchCond = Restrictions.disjunction()
					.add(Restrictions.ilike("title", searchTerm, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("shortTitle", searchTerm, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("irbId", searchTerm, MatchMode.ANYWHERE));
			query.add(searchCond);
		}
		
		addPICondition(query, dpCriteria);
	}
	
	private void addTitleCondition(Criteria query, DpListCriteria dpCriteria) {
		String title = dpCriteria.title();
		if (StringUtils.isBlank(title)) {
			return;
		}
		
		Junction titleCond = Restrictions.disjunction()
				.add(Restrictions.ilike("title", title, MatchMode.ANYWHERE))
				.add(Restrictions.ilike("shortTitle", title, MatchMode.ANYWHERE));
		query.add(titleCond);
		
	}
	
	private void addPICondition(Criteria criteria, DpListCriteria dpCriteria) {
		Long piId = dpCriteria.piId();
		if (piId == null) {
			return;
		}
		
		criteria.add(Restrictions.eq("principalInvestigator.id", piId));
	}
	
	private static final String FQN = DistributionProtocol.class.getName();

	private static final String GET_DP_BY_TITLE = FQN + ".getDistributionProtocolByTitle";

	private static final String GET_DP_BY_SHORT_TITLE = FQN + ".getDistributionProtocolByShortTitle";

}
