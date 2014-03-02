
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenCollectionGroupDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.util.global.Status;

@Repository("specimenCollectionGroupDao")
public class SpecimenCollectionGroupDaoImpl extends AbstractDao<SpecimenCollectionGroup>
		implements
			SpecimenCollectionGroupDao {

	private String hql = "select sp.id,sp.label,sp.activityStatus,sp.specimenType,sp.specimenClass,sp.collectionStatus, "
			+ "spr.specimenRequirementLabel from " + Specimen.class.getName()
			+ " as sp left outer join sp.specimenRequirement as spr " + " where sp.specimenCollectionGroup.id = :scgId"
			+ " and sp.activityStatus <> '" + Status.ACTIVITY_STATUS_DISABLED.toString()
			+ "' and sp.parentSpecimen.id is null order by sp.id";
	private String ACTIVITY_STATUS_DISABLED = "Disabled";

	@Override
	public List<SpecimenInfo> getSpecimensList(Long scgId) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setLong("scgId", scgId);
		List<SpecimenInfo> specimensInfo = new ArrayList<SpecimenInfo>();
		List<Object[]> result = query.list();
		for (Object[] object : result) {
			SpecimenInfo info = new SpecimenInfo();
			info.setId(Long.valueOf(object[0].toString()));
			if (object[1] != null) {
				info.setLabel(object[1].toString());
			}
			info.setSpecimenType(object[3].toString());
			info.setSpecimenClass(object[4].toString());
			info.setCollectionStatus(object[5].toString());
			if (object[6] != null) {
				info.setRequirementLabel(object[6].toString());
			}

			specimensInfo.add(info);
		}

		return specimensInfo;
	}

	@Override
	public void deleteGroups(Long participantId) {
		String hql = "update "+SpecimenCollectionGroup.class.getName()+" scg set scg.activityStatus = '"+ACTIVITY_STATUS_DISABLED+"' where scg.collectionProtocolRegistration.participant.id = :participantId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setLong("participantId", participantId);
		query.executeUpdate();
	}

}
