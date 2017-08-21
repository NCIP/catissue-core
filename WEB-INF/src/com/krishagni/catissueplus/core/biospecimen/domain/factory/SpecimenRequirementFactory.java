package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.AliquotSpecimensRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.DerivedSpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenPoolRequirements;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;

public interface SpecimenRequirementFactory {
	public SpecimenRequirement createSpecimenRequirement(SpecimenRequirementDetail detail);
	
	public SpecimenRequirement createDerived(DerivedSpecimenRequirement req);
	
	public SpecimenRequirement createForUpdate(String lineage, SpecimenRequirementDetail req);
	
	public List<SpecimenRequirement> createAliquots(AliquotSpecimensRequirement req);
	
	public List<SpecimenRequirement> createSpecimenPoolReqs(SpecimenPoolRequirements req);
}
