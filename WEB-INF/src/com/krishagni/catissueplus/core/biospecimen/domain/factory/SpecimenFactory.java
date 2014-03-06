package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;

import edu.wustl.catissuecore.domain.SpecimenRequirement;


public interface SpecimenFactory {
public Specimen createSpecimen(SpecimenRequirement requirement, SpecimenCollectionGroup group);
}
