
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.CollectSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimensCollectedEvent;

public interface SpecimenService {
	public SpecimenCreatedEvent createSpecimen(CreateSpecimenEvent req);
	
	public SpecimensCollectedEvent collectSpecimens(CollectSpecimensEvent req);
	
	public boolean doesSpecimenExists(String label);
}
