
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenCollectionGroupDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

@Repository("specimenCollectionGroupDao")
public class SpecimenCollectionGroupDaoImpl extends AbstractDao<SpecimenCollectionGroup>
		implements
			SpecimenCollectionGroupDao {

	@Override
	public List<SpecimenInfo> getSpecimensList(Long scgId) {
		Object object = sessionFactory.getCurrentSession().get(SpecimenCollectionGroup.class.getName(), scgId);
		if (object == null) {
			return Collections.emptyList();
		}

		SpecimenCollectionGroup scg = (SpecimenCollectionGroup) object;
		return getSpecimensList(scg.getSpecimenCollection());
	}

	@Override
	public boolean isNameUnique(String name) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SCG_ID_BY_NAME);
		query.setString("name", name);
		return query.list().isEmpty() ? true : false;
	}

	@Override
	public boolean isBarcodeUnique(String barcode) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SCG_ID_BY_BARCODE);
		query.setString("barcode", barcode);
		return query.list().isEmpty() ? true : false;
	}

	private List<SpecimenInfo> getSpecimensList(Collection<Specimen> specimens) {
		Map<Long, List<SpecimenInfo>> specimensMap = new HashMap<Long, List<SpecimenInfo>>();
		for (Specimen specimen : specimens) {
			Specimen parentSpecimen = (Specimen) specimen.getParentSpecimen();
			Long parentId = parentSpecimen != null ? parentSpecimen.getId() : -1L;

			List<SpecimenInfo> specimenInfoList = specimensMap.get(parentId);
			if (specimenInfoList == null) {
				specimenInfoList = new ArrayList<SpecimenInfo>();
				specimensMap.put(parentId, specimenInfoList);
			}

			specimenInfoList.add(SpecimenInfo.from(specimen));
		}

		List<SpecimenInfo> specimensList = specimensMap.get(-1L);
		linkParentChildSpecimens(specimensMap, specimensList);
		return specimensList;
	}

	private void linkParentChildSpecimens(Map<Long, List<SpecimenInfo>> specimensMap, List<SpecimenInfo> specimens) {
		if (specimens == null || specimens.isEmpty()) {
			return;
		}

		for (SpecimenInfo specimen : specimens) {
			List<SpecimenInfo> childSpecimens = specimensMap.get(specimen.getId());
			specimen.setChildren(childSpecimens);
			linkParentChildSpecimens(specimensMap, childSpecimens);
		}
	}

	//	private String hql = "select sp.id,sp.label,sp.activityStatus,sp.specimenType,sp.specimenClass,sp.collectionStatus, "
	//			+ "spr.specimenRequirementLabel from " + Specimen.class.getName()
	//			+ " as sp left outer join sp.specimenRequirement as spr " + " where sp.specimenCollectionGroup.id = :scgId"
	//			+ " and sp.activityStatus <> '" + Status.ACTIVITY_STATUS_DISABLED.toString()
	//			+ "' and sp.parentSpecimen.id is null order by sp.id";

	private static final String FQN = SpecimenCollectionGroup.class.getName();

	private static final String GET_SCG_BY_ID = FQN + ".getScgById";

	private static final String GET_SCG_ID_BY_BARCODE = FQN + ".getScgIdByBarcode";

	private static final String GET_SCG_ID_BY_NAME = FQN + ".getScgIdByName";

}
