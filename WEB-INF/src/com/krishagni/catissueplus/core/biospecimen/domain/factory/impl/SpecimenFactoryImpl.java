
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;

import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenRequirement;

public class SpecimenFactoryImpl implements SpecimenFactory {

	private String COLLECTION_STATUS_PENDING = "Pending";

	private String ACTIVITY_STATUS_ACTIVE = "Active";

	@Override
	public Specimen createSpecimen(SpecimenRequirement requirement, SpecimenCollectionGroup group) {
		//TODO: yet to handle the parent child relationship
		Specimen specimen = new Specimen();
		//		this.availableQuantity = new Double(0);
		specimen.setSpecimenType(requirement.getSpecimenType());
		specimen.setSpecimenRequirement(requirement);
		specimen.setSpecimenClass(requirement.getSpecimenClass());
		specimen.setPathologicalStatus(requirement.getPathologicalStatus());
		SpecimenCharacteristics characteristics = requirement.getSpecimenCharacteristics();
		if (characteristics != null) {
			specimen.setTissueSide(characteristics.getTissueSide());
			specimen.setTissueSite(characteristics.getTissueSite());
		}
		specimen.setLineage(requirement.getLineage());
		specimen.setConcentrationInMicrogramPerMicroliter(requirement.getConcentrationInMicrogramPerMicroliter());
		specimen.setIsAvailable(false);
		specimen.setCollectionStatus(COLLECTION_STATUS_PENDING);
		specimen.setActivityStatus(ACTIVITY_STATUS_ACTIVE);
		//		specimen.setParentSpecimen(parentSpecimen)
		//		specimen.setChildSpecimenCollection(/)
		specimen.setSpecimenCollectionGroup(group);
		//setConsents(specimen,group);
		return specimen;
	}

}
