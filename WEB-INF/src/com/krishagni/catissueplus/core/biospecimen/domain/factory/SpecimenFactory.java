package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.util.Collection;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;

import edu.wustl.catissuecore.domain.SpecimenRequirement;


public interface SpecimenFactory {
public Set<Specimen> createSpecimens(Collection<SpecimenRequirement> specimenRequirements, SpecimenCollectionGroup group);
}
