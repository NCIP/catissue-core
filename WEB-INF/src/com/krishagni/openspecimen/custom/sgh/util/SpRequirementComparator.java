package com.krishagni.openspecimen.custom.sgh.util;

import java.util.Comparator;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;


public class SpRequirementComparator implements Comparator<SpecimenRequirement>{

	@Override
	public int compare(SpecimenRequirement o1, SpecimenRequirement o2) {
		
		return o1.getId().compareTo(o2.getId());
	}

}

