
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;

public interface SpecimenCollectionGroupDao extends Dao<SpecimenCollectionGroup> {

	public List<SpecimenInfo> getSpecimensList(Long scgId);

	public void deleteByParticipant(Long participantId);

	public void deleteByRegistration(Long registrationId);

	public void delete(Long collectionGroupId);

	public boolean checkActivechildrenForParticipant(long id);

	public boolean checkActiveChildrenForRegistration(long id);

	public boolean checkActiveChildren(long id);
}
