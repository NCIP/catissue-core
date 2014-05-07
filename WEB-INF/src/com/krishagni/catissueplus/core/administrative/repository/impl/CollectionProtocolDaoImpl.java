
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.util.global.Constants;

public class CollectionProtocolDaoImpl extends AbstractDao<CollectionProtocol> implements CollectionProtocolDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<CollectionProtocolSummary> getAllCollectionProtocols() {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_ALL_CPS);
		query.setString("activityStatus", Constants.ACTIVITY_STATUS_ACTIVE);

		List<Object[]> rows = query.list();
		List<CollectionProtocolSummary> cps = new ArrayList<CollectionProtocolSummary>();
		for (Object[] row : rows) {
			CollectionProtocolSummary cp = new CollectionProtocolSummary();
			cp.setId((Long) row[0]);
			cp.setShortTitle((String) row[1]);
			cp.setTitle((String) row[2]);
			cp.setPpidFormat((String)row[3]);

			cps.add(cp);
		}

		return cps;
	}

	@Override
	public CollectionProtocol getCollectionProtocol(Long cpId) {
		return (CollectionProtocol) sessionFactory.getCurrentSession().get(CollectionProtocol.class.getName(), cpId);
	}

	@Override
	public CollectionProtocolEvent getCpe(Long cpeId) {
		return (CollectionProtocolEvent) sessionFactory.getCurrentSession().get(CollectionProtocolEvent.class, cpeId);
	}

	private static final String FQN = CollectionProtocol.class.getName();

	private static final String GET_ALL_CPS = FQN + ".getAllProtocols";

	@Override
	public SpecimenRequirement getSpecimenRequirement(Long requirementId) {
		return (SpecimenRequirement) sessionFactory.getCurrentSession().get(SpecimenRequirement.class, requirementId);
	}

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol getCPByTitle(String cpTitle) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CP_BY_NAME);
		query.setString("cpTitle", cpTitle);
		List<com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol> cpList = query.list();
		return cpList.isEmpty() ? null : cpList.get(0);
	}
	
	private static final String GET_CP_BY_NAME = 
			com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol.class.getName() + ".getCpByTitle";

}
