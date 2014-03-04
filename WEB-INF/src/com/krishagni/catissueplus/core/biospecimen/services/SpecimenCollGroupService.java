
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenGroupsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;

public interface SpecimenCollGroupService {

	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent event);

	public void delete(DeleteParticipantEvent event);

	public void delete(DeleteRegistrationEvent event);

	public void delete(DeleteSpecimenGroupsEvent event);
}
