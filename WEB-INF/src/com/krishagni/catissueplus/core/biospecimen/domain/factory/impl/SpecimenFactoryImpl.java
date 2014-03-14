
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.global.Status;

public class SpecimenFactoryImpl implements SpecimenFactory {

	private String COLLECTION_STATUS_PENDING = "Pending";

	private String ACTIVITY_STATUS_ACTIVE = "Active";

	@Override
	public Set<Specimen> createSpecimens(Collection<SpecimenRequirement> requirements, SpecimenCollectionGroup group) {
		Set<Specimen> specimenCollection = new HashSet<Specimen>();
		for (SpecimenRequirement specimenRequirement : requirements) {
			
			if (Constants.NEW_SPECIMEN.equals(specimenRequirement.getLineage())
					&& !Status.ACTIVITY_STATUS_DISABLED.toString().equalsIgnoreCase(specimenRequirement.getActivityStatus())) {
				
				specimenCollection.add(this.getCorrespondingSpecimen(specimenRequirement, null, group));
			}
		}
		return specimenCollection;
	}

	private void populateSpecimen(SpecimenRequirement requirement, Specimen specimen) {
		specimen.setSpecimenType(requirement.getSpecimenType());
		specimen.setInitialQuantity(requirement.getInitialQuantity());
		specimen.setAvailableQuantity(0.0);
		specimen.setSpecimenRequirement(requirement);
		specimen.setSpecimenClass(requirement.getSpecimenClass());
		specimen.setPathologicalStatus(requirement.getPathologicalStatus());
		SpecimenCharacteristics characteristics = requirement.getSpecimenCharacteristics();
		if (characteristics != null) {
			specimen.setTissueSide(characteristics.getTissueSide());
			specimen.setTissueSite(characteristics.getTissueSite());
			specimen.setCharacteristics(characteristics);
		}
		specimen.setLineage(requirement.getLineage());
		specimen.setConcentrationInMicrogramPerMicroliter(requirement.getConcentrationInMicrogramPerMicroliter());
		specimen.setIsAvailable(false);
		specimen.setCollectionStatus(COLLECTION_STATUS_PENDING);
		specimen.setActivityStatus(ACTIVITY_STATUS_ACTIVE);
	}

	private Specimen getCorrespondingSpecimen(SpecimenRequirement reqSpecimen, Specimen pSpecimen,
			SpecimenCollectionGroup specimenCollectionGroup) {

		Specimen newSpecimen = new Specimen();
		
		newSpecimen.setParentSpecimen(pSpecimen);
		populateSpecimen(reqSpecimen, newSpecimen);
		newSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
		Collection<AbstractSpecimen> childrenSpecimenCollection = reqSpecimen.getChildSpecimenCollection();
		
		if (childrenSpecimenCollection != null && !childrenSpecimenCollection.isEmpty()) {
			
			Set<Specimen> childrenSpecimen = new HashSet<Specimen>();
			Iterator<AbstractSpecimen> iterator = childrenSpecimenCollection.iterator();
			while (iterator.hasNext()) {
				
				SpecimenRequirement childSpecimen = (SpecimenRequirement) iterator.next();
				if (!Status.ACTIVITY_STATUS_DISABLED.toString().equalsIgnoreCase(childSpecimen.getActivityStatus())) {
					
					Specimen newchildSpecimen = this
							.getCorrespondingSpecimen(childSpecimen, newSpecimen, specimenCollectionGroup);
					childrenSpecimen.add(newchildSpecimen);
				}
			}
			newSpecimen.setChildCollection(childrenSpecimen);
		}

		return newSpecimen;
	}

}
