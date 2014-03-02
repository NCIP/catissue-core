
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.Specimen;

public interface SpecimenDao extends Dao<Specimen> {

	public List<SpecimenInfo> getSpecimensList(Long scgId);

	public void deleteSpecimens(Long participantId);
}
