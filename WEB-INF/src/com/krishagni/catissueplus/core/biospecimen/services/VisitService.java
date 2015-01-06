
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionGroupsDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AddVisitEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenGroupsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.GetScgReportEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitAddedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimensInfoEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgReportEvent;

public interface VisitService {

	VisitAddedEvent addVisit(AddVisitEvent req);
	
	SpecimensInfoEvent getSpecimensList(ReqSpecimenSummaryEvent event);

	ScgDeletedEvent delete(DeleteSpecimenGroupsEvent event);

	ScgUpdatedEvent updateScg(UpdateScgEvent scgEvent);
	
//	ScgUpdatedEvent patchScg(PatchScgEvent scgEvent);

	ScgReportUpdatedEvent updateScgReport(UpdateScgReportEvent event);

	ScgReportUpdatedEvent getScgReport(GetScgReportEvent event);

	AllCollectionGroupsDetailEvent getAllCollectionGroups(ReqAllScgEvent req);
}
