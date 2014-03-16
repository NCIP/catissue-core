
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SpecimenDaoImpl extends AbstractDao<Specimen> implements SpecimenDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<SpecimenInfo> getSpecimensList(Long parentSpecimenId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SPECIMEN_DETAILS_DY_SCG_ID);
		query.setLong("parentSpecimenId", parentSpecimenId);
		List<Object[]> result = query.list();
		List<SpecimenInfo> specimensInfo = new ArrayList<SpecimenInfo>();
		for (Object[] object : result) {
			SpecimenInfo info = new SpecimenInfo();
			info.setId(Long.valueOf(object[0].toString()));
			info.setLabel(object[1].toString());
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

	@SuppressWarnings("unchecked")
	@Override
	public Long getScgId(Long specimenId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SCG_ID_BY_SPECIMEN_ID);
		query.setLong("specimenId", specimenId);

		List<Long> rows = query.list();
		if (rows != null && !rows.isEmpty()) {
			return rows.iterator().next();
		}
		else {
			return null;
		}
	}

	//	private String ACTIVITY_STATUS_DISABLED = "Disabled";
	//
	//	private String hql = "select sp.id,sp.label,sp.activityStatus,sp.specimenType,sp.specimenClass,sp.collectionStatus, "
	//			+ "spr.specimenRequirementLabel from " + Specimen.class.getName()
	//			+ " as sp left outer join sp.specimenRequirement as spr " + " where sp.parentSpecimen.id = :parentSpecimenId"
	//			+ " and sp.activityStatus <> '" + Status.ACTIVITY_STATUS_DISABLED.toString() + "' order by sp.id";

	private static String FQN = Specimen.class.getName();

	private static String GET_SCG_ID_BY_SPECIMEN_ID = FQN + ".getScgIdBySpecimenId";

	private static String GET_SPECIMEN_DETAILS_DY_SCG_ID = FQN + ".getSpecimenDetailsByScgId";
}
