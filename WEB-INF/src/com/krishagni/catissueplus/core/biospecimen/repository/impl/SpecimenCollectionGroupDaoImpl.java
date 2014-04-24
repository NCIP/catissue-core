
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

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenRequirement;

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
//		CollectionProtocolEvent cpe = scg.getCollectionProtocolEvent().getSpecimenRequirementCollection();
		return getSpecimensList(scg.getSpecimenCollection(),scg.getCollectionProtocolEvent().getSpecimenRequirementCollection());
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

	private List<SpecimenInfo> getSpecimensList(Collection<Specimen> specimens, Collection<SpecimenRequirement> requirementColl) {
		Map<String, List<SpecimenInfo>> specimensMap = new HashMap<String, List<SpecimenInfo>>();
		Map<Long,Long> specimenReqMapping = new HashMap<Long, Long>();
		
		for (Specimen specimen : specimens) {
			
			SpecimenRequirement sr = specimen.getSpecimenRequirement();
			if(sr!=null){
				specimenReqMapping.put(sr.getId(), specimen.getId());
			}
			Specimen parentSpecimen = (Specimen) specimen.getParentSpecimen();
			String parentKey = "-1";
			if(parentSpecimen != null){
				SpecimenRequirement psr = parentSpecimen.getSpecimenRequirement();
				parentKey = parentSpecimen.getId()+"_"+psr.getId();
			}			
			List<SpecimenInfo> specimenInfoList = specimensMap.get(parentKey);
			if (specimenInfoList == null) {
				specimenInfoList = new ArrayList<SpecimenInfo>();
				specimensMap.put(parentKey, specimenInfoList);
			}

			specimenInfoList.add(SpecimenInfo.fromSpecimen(specimen));
		}
		
		for (SpecimenRequirement requirement : requirementColl) {
			SpecimenRequirement parentSpecimenReq = (SpecimenRequirement) requirement.getParentSpecimen();
			
			String parentKey = "-1";
			if(parentSpecimenReq != null)
			{
				parentKey = specimenReqMapping.get(parentSpecimenReq.getId())+"_"+parentSpecimenReq.getId();
			}
			if(specimenReqMapping.get(requirement.getId()) == null)
			{
				List<SpecimenInfo> specimenInfoList = specimensMap.get(parentKey);
				if (specimenInfoList == null) {
					specimenInfoList = new ArrayList<SpecimenInfo>();
					specimensMap.put(parentKey, specimenInfoList);
				}
				specimenInfoList.add(SpecimenInfo.fromRequirement(requirement));
			}

		}

		List<SpecimenInfo> specimensList = specimensMap.get("-1");
		linkParentChildSpecimens(specimensMap, specimensList);
		return specimensList;
	}

	private void linkParentChildSpecimens(Map<String, List<SpecimenInfo>> specimensMap, List<SpecimenInfo> specimens) {
		if (specimens == null || specimens.isEmpty()) {
			return;
		}

		for (SpecimenInfo specimen : specimens) {
			List<SpecimenInfo> childSpecimens = specimensMap.get(specimen.getId()+"_"+specimen.getRequirementId());
			specimen.setChildren(childSpecimens);
			linkParentChildSpecimens(specimensMap, childSpecimens);
		}
	}
	
	@Override
	public SpecimenCollectionGroup getscg(Long id) {
		return (SpecimenCollectionGroup)sessionFactory.getCurrentSession().get(SpecimenCollectionGroup.class, id);
	}

	private static final String FQN = SpecimenCollectionGroup.class.getName();

	private static final String GET_SCG_ID_BY_BARCODE = FQN + ".getScgIdByBarcode";

	private static final String GET_SCG_ID_BY_NAME = FQN + ".getScgIdByName";

	@Override
	public List<SpecimenInfo> getSpecimensListFromCpe(Long cpeId) {
		Object object = sessionFactory.getCurrentSession().get(CollectionProtocolEvent.class.getName(), cpeId);
		if (object == null) {
			return Collections.emptyList();
		}
		CollectionProtocolEvent cpe = (CollectionProtocolEvent) object;
		return getSpecimensList(Collections.<Specimen>emptyList(),cpe.getSpecimenRequirementCollection());
	}

	

}
