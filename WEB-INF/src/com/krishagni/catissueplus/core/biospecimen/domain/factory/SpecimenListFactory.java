package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetails;

public interface SpecimenListFactory {
	public SpecimenList createSpecimenList(SpecimenListDetails details);
	
	public SpecimenList createSpecimenList(SpecimenList existing, SpecimenListDetails details);

	public SpecimenList createDefaultSpecimenList(User user);
}

