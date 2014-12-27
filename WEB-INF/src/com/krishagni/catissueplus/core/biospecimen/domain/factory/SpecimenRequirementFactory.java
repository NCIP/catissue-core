package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;

public interface SpecimenRequirementFactory {
	public SpecimenRequirement createSpecimenRequirement(SpecimenRequirementDetail detail);
}
