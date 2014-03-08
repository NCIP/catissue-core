
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenCollectionGroupDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.util.global.Status;

@Repository("specimenCollectionGroupDao")
public class SpecimenCollectionGroupDaoImpl extends AbstractDao<SpecimenCollectionGroup>
		implements
			SpecimenCollectionGroupDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<SpecimenInfo> getSpecimensList(Long scgId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SCG_BY_ID);
		query.setLong("scgId", scgId);
		
		List<SpecimenCollectionGroup> scgs = query.list();
		if (scgs == null || scgs.isEmpty()) {
			return Collections.emptyList();
		}
		
		SpecimenCollectionGroup scg = scgs.get(0);				
		return getSpecimensList(scg.getSpecimenCollection());
	}

	@Override
	public void deleteByParticipant(Long participantId) {
		String hql = "update "+SpecimenCollectionGroup.class.getName()+" scg set scg.activityStatus = '"+Status.ACTIVITY_STATUS_DISABLED.toString()+"' where scg.collectionProtocolRegistration.participant.id = :participantId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setLong("participantId", participantId);
		query.executeUpdate();
	}

	@Override
	public void deleteByRegistration(Long registrationId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long collectionGroupId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkActivechildrenForParticipant(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkActiveChildrenForRegistration(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkActiveChildren(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	private List<SpecimenInfo> getSpecimensList(Collection<Specimen> specimens) {
		Map<Long, List<SpecimenInfo>> specimensMap = new HashMap<Long, List<SpecimenInfo>>();
		for (Specimen specimen : specimens) {
			Specimen parentSpecimen = (Specimen)specimen.getParentSpecimen();
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
	
	private String hql = "select sp.id,sp.label,sp.activityStatus,sp.specimenType,sp.specimenClass,sp.collectionStatus, "
			+ "spr.specimenRequirementLabel from " + Specimen.class.getName()
			+ " as sp left outer join sp.specimenRequirement as spr " + " where sp.specimenCollectionGroup.id = :scgId"
			+ " and sp.activityStatus <> '" + Status.ACTIVITY_STATUS_DISABLED.toString()
			+ "' and sp.parentSpecimen.id is null order by sp.id";

	private static final String FQN = SpecimenCollectionGroup.class.getName();
	
	private static final String GET_SCG_BY_ID = FQN + ".getScgById"; 	
}
