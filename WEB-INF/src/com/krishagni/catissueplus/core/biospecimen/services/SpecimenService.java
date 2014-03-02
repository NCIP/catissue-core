package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;


public interface SpecimenService {
	
	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent event);

	public void deleteSpecimens(Long participantId);
}
