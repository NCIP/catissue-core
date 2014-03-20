
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenGroupsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDeletedEvent;

public interface SpecimenCollGroupService {

	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent event);

	public ScgDeletedEvent delete(DeleteSpecimenGroupsEvent event);

	public ScgCreatedEvent createScg(CreateScgEvent scgEvent);
}
