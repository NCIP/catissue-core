
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
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

			cps.add(cp);
		}

		return cps;
	}

	@Override
	public CollectionProtocol getCollectionProtocol(Long cpId) {
		return null;
	}

	private static final String FQN = CollectionProtocol.class.getName();

	private static final String GET_ALL_CPS = FQN + ".getAllProtocols";

	private String hql = "select new com.krishagni.catissueplus.core.biospecimen.events.ParticipantInfo(cpr.id, cpr.participant.id , cpr.protocolParticipantIdentifier,"
			+ "case when cpr.participant.firstName is null then '' else cpr.participant.firstName end, "
			+ "case when cpr.participant.lastName is null  then '' else cpr.participant.lastName end) "
			+ " from edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr "
			+ "where  cpr.collectionProtocol.id = :cpId and cpr.activityStatus != :cprActivityStatus and  cpr.participant.activityStatus != :participantStatus "
			+ " and (lower(cpr.participant.lastName) like :lastName or lower(cpr.participant.firstName) like :firstName or "
			+ "lower(cpr.protocolParticipantIdentifier) like :ppId)";

	private int maxParticipantCount = 200;

	@Override
	public Collection<ConsentTier> getConsentTierCollection(Long cpId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ParticipantInfo> getRegisteredParticipants(Long cpId, String searchString) {
		if (searchString != null)
			searchString.toLowerCase();

		//" order by cpr.participant.lastName, cpr.participant.firstName ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);//getNamedQuery(GET_PARTICIPANT_INFO_LIST_BY_QUERY);
		query.setLong("cpId", cpId);
		query.setString("lastName", searchString + "%");
		query.setString("firstName", searchString + "%");
		query.setString("ppId", searchString + "%");
		query.setString("cprActivityStatus", Constants.ACTIVITY_STATUS_DISABLED);
		query.setString("participantStatus", Constants.ACTIVITY_STATUS_DISABLED);
		query.setMaxResults(maxParticipantCount);
		List<ParticipantInfo> participantsList = query.list();
		return participantsList;
	}

	@Override
	public CollectionProtocolRegistration getCpr(Long cpId, String ppid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<CollectionProtocolEvent> getEventCollection(Long cpId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<SpecimenRequirement> getSpecimenRequirements(Long cpeId) {
		// TODO Auto-generated method stub
		return null;
	}

}
