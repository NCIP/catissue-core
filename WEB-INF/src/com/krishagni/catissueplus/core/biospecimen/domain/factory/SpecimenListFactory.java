package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetails;

public interface SpecimenListFactory {
	public SpecimenList createSpecimenList(SpecimenListDetails details);
}

