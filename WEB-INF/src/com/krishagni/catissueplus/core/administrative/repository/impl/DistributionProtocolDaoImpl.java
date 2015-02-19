
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.events.DpListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.DistributionProtocolDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class DistributionProtocolDaoImpl extends AbstractDao<DistributionProtocol> implements DistributionProtocolDao {

	private static final String FQN = DistributionProtocol.class.getName();

	private static final String GET_DP_BY_TITLE = FQN + ".getDistributionProtocolByTitle";

	private static final String GET_DP_BY_SHORT_TITLE = FQN + ".getDistributionProtocolByShortTitle";

	@SuppressWarnings("unchecked")
	@Override
	public List<DistributionProtocol> getDistributionProtocols(DpListCriteria dpCriteria) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(DistributionProtocol.class)
				.setFirstResult(dpCriteria.startAt() < 0 ? 0 : dpCriteria.startAt())
				.setMaxResults(dpCriteria.maxResults() < 0 || dpCriteria.maxResults() > 100 ? 100 : dpCriteria.maxResults())
				.add(Restrictions.ne("activityStatus", "Disabled"));
		
		String searchTerm = dpCriteria.query();
		if (!StringUtils.isBlank(searchTerm)) {
			Junction searchCrit = Restrictions.disjunction()
					.add(Restrictions.ilike("title", searchTerm, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("shortTitle", searchTerm, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("irbId", searchTerm, MatchMode.ANYWHERE));
			query.add(searchCrit);
		}
		
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
}
