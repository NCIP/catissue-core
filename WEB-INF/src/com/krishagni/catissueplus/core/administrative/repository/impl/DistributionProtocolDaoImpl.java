
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.repository.DistributionProtocolDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class DistributionProtocolDaoImpl extends AbstractDao<DistributionProtocol> implements DistributionProtocolDao {

	private static final String FQN = DistributionProtocol.class.getName();

	private static final String GET_DISTRIBUTION_PROTOCOL_TITLE = FQN + ".getDistributionProtocolByTitle";

	private static final String GET_DISTRIBUTION_PROTOCOL_SHORT_TITLE = FQN + ".getDistributionProtocolByShortTitle";

	private static final String GET_ALL_DPS = FQN + ".getAllDistributionProtocols";

	@SuppressWarnings("unchecked")
	@Override
	public List<DistributionProtocol> getAllDistributionProtocol(int startAt, int maxResults) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_ALL_DPS)
				.setFirstResult(startAt <= 0 ? 0 : startAt);
		
		if (maxResults > 0 ) {
			query.setMaxResults(maxResults);
		}
		return query.setMaxResults(maxResults).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public DistributionProtocol getByShortTitle(String shortTitle) {
		List<DistributionProtocol> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_DISTRIBUTION_PROTOCOL_SHORT_TITLE)
				.setString("shortTitle", shortTitle)
				.list();
		
		return result.isEmpty() ? null : result.iterator().next();
	}

	@SuppressWarnings("unchecked")
	@Override
	public DistributionProtocol getDistributionProtocol(String title) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_DISTRIBUTION_PROTOCOL_TITLE);
		query.setString("title", title);
		List<DistributionProtocol> list = query.list();
		return list.isEmpty() ? null : list.get(0);
	}
	
	public Class getType() {
		return DistributionProtocol.class;
	}
}
