package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.AliquotSpecimensRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.DerivedSpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;

public interface SpecimenRequirementFactory {
	public SpecimenRequirement createSpecimenRequirement(SpecimenRequirementDetail detail);
	
	public SpecimenRequirement createDerived(DerivedSpecimenRequirement req);
	
	public List<SpecimenRequirement> createAliquots(AliquotSpecimensRequirement req);
}
