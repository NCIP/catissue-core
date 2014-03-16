
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import gov.nih.nci.logging.api.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantInfo;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCollectionGroupInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

@Repository("collectionProtocolRegistrationDao")
public class CollectionProtocolRegistrationDaoImpl extends AbstractDao<CollectionProtocolRegistration>
		implements
			CollectionProtocolRegistrationDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ParticipantInfo> getParticipants(Long cpId, String searchString) {
		boolean isSearchTermSpecified = !StringUtils.isBlank(searchString);

		String queryName = GET_PARTICIPANTS_BY_CP_ID;
		if (isSearchTermSpecified) {
			queryName = GET_PARTICIPANTS_BY_CP_ID_AND_SEARCH_TERM;
		}

		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
		query.setLong("cpId", cpId);

		if (isSearchTermSpecified) {
			query.setString("searchTerm", searchString.toLowerCase() + "%");
		}

		List<ParticipantInfo> result = new ArrayList<ParticipantInfo>();
		List<Object[]> rows = query.list();
		for (Object[] row : rows) {
			ParticipantInfo participant = new ParticipantInfo();
			participant.setCprId((Long) row[0]);
			participant.setId((Long) row[1]);
			participant.setPpId((String) row[2]);
			participant.setFirstName((String) row[3]);
			participant.setLastName((String) row[4]);

			result.add(participant);
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SpecimenCollectionGroupInfo> getScgList(Long cprId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_COLLECTION_GROUPSBY_CPR_ID);
		query.setLong("cprId", cprId);
		List<Object[]> results = query.list();

		List<SpecimenCollectionGroupInfo> scgs = new ArrayList<SpecimenCollectionGroupInfo>();
		for (Object[] object : results) {
			SpecimenCollectionGroupInfo scg = new SpecimenCollectionGroupInfo();
			scg.setId(Long.valueOf(object[0].toString()));
			scg.setName(object[1] == null ? "" : object[1].toString());
			scg.setCollectionStatus(object[2].toString());
			if (object[3] != null) {
				scg.setReceivedDate((Date) object[7]);
			}
			scg.setEventPoint(Double.parseDouble(object[5].toString()));
			scg.setCollectionPointLabel(object[6].toString());
			scg.setRegistrationDate((Date) object[7]);
			scgs.add(scg);
		}

		return scgs;
	}

	@Override
	public CollectionProtocolRegistration getCpr(Long cprId) {

		return (CollectionProtocolRegistration) sessionFactory.getCurrentSession().get(
				CollectionProtocolRegistration.class, cprId);
	}

	private static final String FQN = CollectionProtocolRegistration.class.getName();

	private static final String GET_PARTICIPANTS_BY_CP_ID = FQN + ".getParticipantsByCpId";

	private static final String GET_PARTICIPANTS_BY_CP_ID_AND_SEARCH_TERM = FQN + ".getParticipantsByCpIdAndSearchTerm";

	private static final String GET_COLLECTION_GROUPSBY_CPR_ID = FQN + ".getCollectionGroupsByCprId";

	//	private final String hql = "select scg.id,scg.name,scg.collectionStatus, scg.receivedTimestamp, "
	//			+ "scg.collectionProtocolEvent.id,scg.collectionProtocolEvent.studyCalendarEventPoint,"
	//			+ "scg.collectionProtocolEvent.collectionPointLabel, scg.collectionProtocolRegistration.registrationDate from "
	//			+ SpecimenCollectionGroup.class.getName() + " as scg where scg.collectionProtocolRegistration.id = :cprId"
	//			+ " and scg.activityStatus <> '" + Status.ACTIVITY_STATUS_DISABLED.toString()
	//			+ "' order by scg.collectionProtocolEvent.studyCalendarEventPoint";
}
