
package com.krishagni.catissueplus.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.krishagni.catissueplus.dao.SpecimenCollectionGroupDao;
import com.krishagni.catissueplus.events.specimens.SpecimenInfo;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.global.Status;

@Repository("specimenCollectionGroupDao")
public class SpecimenCollectionGroupDaoImpl extends AbstractDao<SpecimenCollectionGroupDao>
		implements
			SpecimenCollectionGroupDao {

	String hql = "select sp.id,sp.label,sp.activityStatus,sp.specimenType,sp.specimenClass,sp.collectionStatus, "
			+ "spr.specimenRequirementLabel from " + Specimen.class.getName()
			+ " as sp left outer join sp.specimenRequirement as spr " + " where sp.specimenCollectionGroup.id = :scgId"
			+ " and sp.activityStatus <> '" + Status.ACTIVITY_STATUS_DISABLED.toString()
			+ "' and sp.parentSpecimen.id is null order by sp.id";

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

}
