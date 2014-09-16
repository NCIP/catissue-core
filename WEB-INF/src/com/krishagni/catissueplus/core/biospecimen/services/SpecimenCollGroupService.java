
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionGroupsDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenGroupsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.GetScgReportEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimensInfoEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgReportEvent;

public interface SpecimenCollGroupService {

	SpecimensInfoEvent getSpecimensList(ReqSpecimenSummaryEvent event);

	ScgDeletedEvent delete(DeleteSpecimenGroupsEvent event);

	ScgCreatedEvent createScg(CreateScgEvent scgEvent);

	ScgUpdatedEvent updateScg(UpdateScgEvent scgEvent);
	
	ScgUpdatedEvent patchScg(PatchScgEvent scgEvent);

	ScgReportUpdatedEvent updateScgReport(UpdateScgReportEvent event);

	ScgReportUpdatedEvent getScgReport(GetScgReportEvent event);

	AllCollectionGroupsDetailEvent getAllCollectionGroups(ReqAllScgEvent req);
}
