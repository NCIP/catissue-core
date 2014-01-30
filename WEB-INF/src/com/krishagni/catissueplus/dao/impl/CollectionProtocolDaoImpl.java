
package com.krishagni.catissueplus.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.krishagni.catissueplus.dao.CollectionProtocolDao;
import com.krishagni.catissueplus.events.collectionprotocols.CollectionProtocolInfo;
import com.krishagni.catissueplus.events.participants.ParticipantInfo;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.global.Constants;

@Repository("collectionProtocolDao")
public class CollectionProtocolDaoImpl extends AbstractDao<CollectionProtocol> implements CollectionProtocolDao {

	private String GET_COLLECTION_PROTOCOL_LIST = "getCollectionProtocolList";

	private String GET_PARTICIPANT_INFO_LIST_BY_QUERY = "getParticipantInfoListByQuery";
	
	private String hql = "select new com.krishagni.catissueplus.events.participants.ParticipantInfo(cpr.id, cpr.participant.id , cpr.protocolParticipantIdentifier,"+ 
      "case when cpr.participant.firstName is null then '' else cpr.participant.firstName end, "+
      "case when cpr.participant.lastName is null  then '' else cpr.participant.lastName end) "+
       " from edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr "+  
       "where  cpr.collectionProtocol.id = :cpId and cpr.activityStatus != :cprActivityStatus and  cpr.participant.activityStatus != :participantStatus "+
       " and (lower(cpr.participant.lastName) like :lastName or lower(cpr.participant.firstName) like :firstName or " +
       "lower(cpr.protocolParticipantIdentifier) like :ppId)";

	private int maxParticipantCount = 200;

	//TODO need to modify the hbm's to set the lazy false, so that DAO will return the domain objects instead of DTO's.

	@Override
	@SuppressWarnings("unchecked")
	public List<CollectionProtocolInfo> getCollectionProtocolsList() {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_COLLECTION_PROTOCOL_LIST);
		query.setString("activityStatus", Constants.ACTIVITY_STATUS_ACTIVE);
		return query.list();

	}

	@Override
	public CollectionProtocol getCollectionProtocol(Long id) {
		return (CollectionProtocol) sessionFactory.getCurrentSession().get(CollectionProtocol.class, id);
	}

	@Override
	public List<ParticipantInfo> getRegisteredParticipants(Long cpId, String searchString) {
		if(searchString != null)
			searchString.toLowerCase();
		
         //" order by cpr.participant.lastName, cpr.participant.firstName ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);//getNamedQuery(GET_PARTICIPANT_INFO_LIST_BY_QUERY);
		query.setLong("cpId", cpId);
		query.setString("lastName", searchString+"%");
		query.setString("firstName", searchString+"%");
		query.setString("ppId", searchString+"%");
		query.setString("cprActivityStatus", Constants.ACTIVITY_STATUS_DISABLED);
		query.setString("participantStatus", Constants.ACTIVITY_STATUS_DISABLED);
		query.setMaxResults(maxParticipantCount);
		List<ParticipantInfo> participantsList = query.list();
		return participantsList;
	}

}
