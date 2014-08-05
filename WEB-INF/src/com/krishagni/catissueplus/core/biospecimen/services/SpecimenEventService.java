package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenEventFormData;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenEventFormDataEvent;


public interface SpecimenEventService {
	
	public SpecimenEventFormDataEvent getSpecimenEventFormData(ReqSpecimenEventFormData req);

}
