package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.CreateInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateInstituteEvent;


public interface InstituteService {

	public InstituteCreatedEvent createInstitute(CreateInstituteEvent event);

	public InstituteUpdatedEvent updateInstitute(UpdateInstituteEvent event);
}
