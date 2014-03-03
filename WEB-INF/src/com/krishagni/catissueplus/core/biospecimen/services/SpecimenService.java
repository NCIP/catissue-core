
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenGroupsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;

public interface SpecimenService {

	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent event);

	public void delete(DeleteParticipantEvent event);

	public void delete(DeleteRegistrationEvent event);

	public void delete(DeleteSpecimenGroupsEvent event);

	public void delete(DeleteSpecimenEvent event);
}
