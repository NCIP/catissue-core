
package com.krishagni.catissueplus.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.krishagni.catissueplus.dao.ParticipantDao;
import com.krishagni.catissueplus.events.participants.ParticipantInfo;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.patientLookUp.util.Constants;

@Repository("participantDao")
public class ParticipantDaoImpl extends AbstractDao<Participant> implements ParticipantDao {

	private String GET_PARTICIPANT_INFO_LISTBY_CP_ID = "getParticipantInfoListByCPID";

	private String GET_PARTICIPANT_INFO_LIST_BY_QUERY = "getParticipantInfoListByQuery";

	private int maxParticipantCount = 200;

	@Override
	@SuppressWarnings("unchecked")
	public List<ParticipantInfo> getParticipantsInfoList(Long cpId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PARTICIPANT_INFO_LISTBY_CP_ID);
		query.setLong("cpId", cpId);
		query.setString("cprActivityStatus", Constants.ACTIVITY_STATUS_DISABLED);
		query.setString("participantStatus", Constants.ACTIVITY_STATUS_DISABLED);
		query.setMaxResults(maxParticipantCount);
		List<ParticipantInfo> participantInfos = new ArrayList<ParticipantInfo>();
		List<Object[]> result = query.list();
		for (Object[] object : result) {
			ParticipantInfo participantInfo = new ParticipantInfo();
			participantInfo.setCollectionProtocolRegistrationId(Long.valueOf(object[0].toString()));
			participantInfo.setId(Long.valueOf(object[1].toString()));
			participantInfo.setLastName(object[2].toString());
			participantInfo.setFirstName(object[3].toString());
			participantInfo.setProtocolParticipantIdentifier(object[4].toString());
			participantInfos.add(participantInfo);
		}
		return participantInfos;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ParticipantInfo> getParticipantsInfoList(Long cpId, String participantQuery) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PARTICIPANT_INFO_LIST_BY_QUERY);
		query.setLong("cpId", cpId);
		query.setString("lastName", participantQuery);
		query.setString("firstName", participantQuery);
		query.setString("ppId", participantQuery);
		query.setString("cprActivityStatus", Constants.ACTIVITY_STATUS_DISABLED);
		query.setString("participantStatus", Constants.ACTIVITY_STATUS_DISABLED);
		query.setMaxResults(maxParticipantCount);
		List<ParticipantInfo> participantInfos = new ArrayList<ParticipantInfo>();
		List<Object[]> result = query.list();
		for (Object[] object : result) {
			ParticipantInfo participantInfo = new ParticipantInfo();
			participantInfo.setCollectionProtocolRegistrationId(Long.valueOf(object[0].toString()));
			participantInfo.setId(Long.valueOf(object[1].toString()));
			participantInfo.setLastName(object[2].toString());
			participantInfo.setFirstName(object[3].toString());
			participantInfo.setProtocolParticipantIdentifier(object[4].toString());
			participantInfos.add(participantInfo);
		}
		return participantInfos;
	}

	@Override
	public Participant getParticipant(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
