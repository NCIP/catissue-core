package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenRequirement;


public interface SpecimenFactory {
public Specimen createSpecimen(SpecimenRequirement requirement, SpecimenCollectionGroup group);
}
