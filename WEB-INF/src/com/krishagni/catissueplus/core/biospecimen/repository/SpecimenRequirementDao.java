package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SpecimenRequirementDao extends Dao<SpecimenRequirement> {
	public SpecimenRequirement getSpecimenRequirement(Long id);
	
	public SpecimenRequirement getByCpEventLabelAndSrCode(String cpShortTitle, String eventLabel, String code);
	
	public int getSpecimensCount(Long srId);
}
