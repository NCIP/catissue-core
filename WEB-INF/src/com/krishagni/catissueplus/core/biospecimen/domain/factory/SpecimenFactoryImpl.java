package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenRequirement;


public class SpecimenFactoryImpl implements SpecimenFactory{


	private String COLLECTION_STATUS_PENDING = "Pending";
	private String ACTIVITY_STATUS_ACTIVE = "Active";
	@Override
	public Specimen createSpecimen(SpecimenRequirement requirement, SpecimenCollectionGroup group) {
		
		Specimen specimen = new Specimen();
//		this.availableQuantity = new Double(0);
		specimen.setSpecimenType(requirement.getSpecimenType());
		specimen.setSpecimenRequirement(requirement);
		specimen.setSpecimenClass(requirement.getSpecimenClass());
		specimen.setPathologicalStatus(requirement.getPathologicalStatus());
		specimen.setSpecimenCharacteristics(requirement.getSpecimenCharacteristics());
		specimen.setLineage(requirement.getLineage());
		specimen.setConcentrationInMicrogramPerMicroliter(requirement.getConcentrationInMicrogramPerMicroliter());
		specimen.setIsAvailable(false);
		specimen.setCollectionStatus(COLLECTION_STATUS_PENDING);
		specimen.setActivityStatus(ACTIVITY_STATUS_ACTIVE);
//		specimen.setParentSpecimen(parentSpecimen)
//		specimen.setChildSpecimenCollection(/)
		specimen.setSpecimenCollectionGroup(group);
		setConsents(specimen,group);
		return specimen;
	}

	private void setConsents(Specimen specimen, SpecimenCollectionGroup group) {
		Collection<ConsentTierStatus> consentTierStatusCollectionN = new HashSet<ConsentTierStatus>();

		final Collection<ConsentTierStatus> consentTierStatusCollection = group
				.getConsentTierStatusCollection();
		if (consentTierStatusCollection != null && !consentTierStatusCollection.isEmpty())
		{
			final Iterator<ConsentTierStatus> iterator = consentTierStatusCollection.iterator();
			while (iterator.hasNext())
			{
				ConsentTierStatus consentTierstatusN = new ConsentTierStatus((ConsentTierStatus)iterator.next());
				consentTierStatusCollectionN.add(consentTierstatusN);
			}
		}
		specimen.setConsentTierStatusCollection(consentTierStatusCollectionN);
	}

}
