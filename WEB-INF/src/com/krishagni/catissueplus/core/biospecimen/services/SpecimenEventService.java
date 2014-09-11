package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.SaveSpecimenEventsDataEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenEventsSavedEvent;

public interface SpecimenEventService {
	
	public SpecimenEventsSavedEvent saveSpecimenEvents(SaveSpecimenEventsDataEvent event);
	
//	public SpecimenEventFormDataEvent getSpecimenEventFormData(ReqSpecimenEventFormData req);
	

}
