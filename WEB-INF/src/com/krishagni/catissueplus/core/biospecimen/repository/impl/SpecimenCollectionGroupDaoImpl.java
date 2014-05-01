
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		return getSpecimensList(scg);
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

	private List<SpecimenInfo> getSpecimensList(SpecimenCollectionGroup scg) {
		Collection<Specimen> specimens = scg.getSpecimenCollection(); 
		Collection<SpecimenRequirement> requirementColl = scg.getCollectionProtocolEvent().getSpecimenRequirementCollection();
		Map<String, Set<SpecimenInfo>> specimensMap = new HashMap<String, Set<SpecimenInfo>>();
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
				if(psr==null){  
				parentKey = parentSpecimen.getId()+"_-1";
				} 
				else{ 
					parentKey = parentSpecimen.getId()+"_"+psr.getId();
				}
			}			
			Set<SpecimenInfo> specimenInfoList = specimensMap.get(parentKey);
			if (specimenInfoList == null) {
				specimenInfoList = new HashSet<SpecimenInfo>();
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
				Set<SpecimenInfo> specimenInfoList = specimensMap.get(parentKey);
				if (specimenInfoList == null) {
					specimenInfoList = new HashSet<SpecimenInfo>();
					specimensMap.put(parentKey, specimenInfoList);
				}
				SpecimenInfo sr = SpecimenInfo.fromRequirement(requirement);
				if(scg!=null)
				{
					sr.setScgId(scg.getId());
				}
				specimenInfoList.add(sr);
			}

		}

		Set<SpecimenInfo> specimensList = specimensMap.get("-1");
		linkParentChildSpecimens(specimensMap, specimensList);
		List<SpecimenInfo> result = new ArrayList<SpecimenInfo>(specimensList);
		if(specimensList!=null){
		Collections.sort(result);
		}
		return result;
	}

	private void linkParentChildSpecimens(Map<String, Set<SpecimenInfo>> specimensMap, Set<SpecimenInfo> specimens) {
		if (specimens == null || specimens.isEmpty()) {
			return;
		}

		for (SpecimenInfo specimen : specimens) {
			Set<SpecimenInfo> childSpecimens = specimensMap.get(specimen.getId()+"_"+specimen.getRequirementId());
			List<SpecimenInfo> childList = Collections.emptyList();
			if(childSpecimens != null){
				childList = new ArrayList<SpecimenInfo>(childSpecimens);
				Collections.sort(childList);
			}
			
			specimen.setChildren(childList);
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
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setCollectionProtocolEvent(cpe);
		scg.setSpecimenCollection(Collections.<Specimen>emptySet());
		return getSpecimensList(scg);
	}

	

}
