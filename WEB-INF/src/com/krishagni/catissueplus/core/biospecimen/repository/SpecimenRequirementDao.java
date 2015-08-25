package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SpecimenRequirementDao extends Dao<SpecimenRequirement> {
	public SpecimenRequirement getSpecimenRequirement(Long id);
	
	public int getSpecimensCount(Long srId);
}
