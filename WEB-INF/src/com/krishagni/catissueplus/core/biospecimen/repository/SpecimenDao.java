
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.Specimen;

public interface SpecimenDao extends Dao<Specimen> {

	public List<SpecimenInfo> getSpecimensList(Long scgId);

	public void deleteByParticipant(Long participantId);

	public void deleteByRegistration(Long registrationId);

	public void deleteBycollectionGroup(Long collecionGroupId);

	public void delete(Long id);

	public boolean checkActiveChildrenForParticipant(long id);

	public boolean checkActiveChildrenForRegistration(long id);

	public boolean checkActiveChildrenForCollGroup(long id);

	public boolean checkActiveChildren(long id);
}
