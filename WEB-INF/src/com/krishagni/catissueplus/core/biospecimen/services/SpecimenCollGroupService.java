
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenGroupsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgEvent;

public interface SpecimenCollGroupService {

	AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent event);

	ScgDeletedEvent delete(DeleteSpecimenGroupsEvent event);

	ScgCreatedEvent createScg(CreateScgEvent scgEvent);

	ScgUpdatedEvent updateScg(UpdateScgEvent scgEvent);
}
