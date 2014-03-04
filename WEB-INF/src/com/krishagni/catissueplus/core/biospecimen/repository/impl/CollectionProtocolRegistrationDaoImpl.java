
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCollectionGroupInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.util.global.Status;

@Repository("collectionProtocolRegistrationDao")
public class CollectionProtocolRegistrationDaoImpl extends AbstractDao<CollectionProtocolRegistration>
		implements
			CollectionProtocolRegistrationDao {

	private String ACTIVITY_STATUS_DISABLED = "Disabled";

	private final String hql = "select scg.id,scg.name,scg.collectionStatus, scg.receivedTimestamp, "
			+ "scg.collectionProtocolEvent.id,scg.collectionProtocolEvent.studyCalendarEventPoint,"
			+ "scg.collectionProtocolEvent.collectionPointLabel, scg.collectionProtocolRegistration.registrationDate from "
			+ SpecimenCollectionGroup.class.getName() + " as scg where scg.collectionProtocolRegistration.id = :cprId"
			+ " and scg.activityStatus <> '" + Status.ACTIVITY_STATUS_DISABLED.toString()
			+ "' order by scg.collectionProtocolEvent.studyCalendarEventPoint";

	@Override
	public List<SpecimenCollectionGroupInfo> getSpecimenCollectiongroupsList(Long cprId) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setLong("cprId", cprId);
		List<Object[]> results = query.list();
		List<SpecimenCollectionGroupInfo> collectionGroupsInfo = new ArrayList<SpecimenCollectionGroupInfo>();
		for (Object[] object : results) {
			SpecimenCollectionGroupInfo info = new SpecimenCollectionGroupInfo();
			info.setId(Long.valueOf(object[0].toString()));
			info.setName(object[1].toString());
			info.setCollectionStatus(object[2].toString());
			if (object[3] != null) {
				info.setReceivedDate((Date) object[7]);
			}
			info.setEventPoint(Double.parseDouble(object[5].toString()));
			info.setCollectionPointLabel(object[6].toString());
			info.setRegistrationDate((Date) object[7]);
			collectionGroupsInfo.add(info);
		}

		return collectionGroupsInfo;
	}

	@Override
	public void deleteByParticipant(Long participantId) {
		String hql = "update " + CollectionProtocolRegistration.class.getName() + " cpr set cpr.activityStatus = '"
				+ ACTIVITY_STATUS_DISABLED + "' where cpr.participant.id = :participantId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setLong("participantId", participantId);
		query.executeUpdate();
	}

	@Override
	public void deleteByRegistration(Long registrationId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Long id) {

	}

	@Override
	public boolean checkActiveChildren(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkActiveChildrenForParticipant(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CollectionProtocolRegistration getCpr(Long cprId) {
		// TODO Auto-generated method stub
		return null;
	}

}
