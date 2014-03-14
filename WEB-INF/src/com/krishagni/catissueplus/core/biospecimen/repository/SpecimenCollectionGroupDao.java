
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SpecimenCollectionGroupDao extends Dao<SpecimenCollectionGroup> {

	public List<SpecimenInfo> getSpecimensList(Long scgId);

}
