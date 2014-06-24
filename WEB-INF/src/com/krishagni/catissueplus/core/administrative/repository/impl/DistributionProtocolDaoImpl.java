
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

	@Override
	public DistributionProtocol getDistributionProtocol(Long id) {
		return (DistributionProtocol) sessionFactory.getCurrentSession().get(DistributionProtocol.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean isUniqueTitle(String title) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_DISTRIBUTION_PROTOCOL_TITLE);
		query.setString("title", title);
		List<DistributionProtocol> list = query.list();
		return list.isEmpty() ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean isUniqueShortTitle(String shortTitle) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_DISTRIBUTION_PROTOCOL_SHORT_TITLE);
		query.setString("shortTitle", shortTitle);
		List<DistributionProtocol> list = query.list();
		return list.isEmpty() ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DistributionProtocol getDistributionProtocol(String title) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_DISTRIBUTION_PROTOCOL_TITLE);
		query.setString("title", title);
		List<DistributionProtocol> list = query.list();
		return list.isEmpty() ? null : list.get(0);
	}
}
