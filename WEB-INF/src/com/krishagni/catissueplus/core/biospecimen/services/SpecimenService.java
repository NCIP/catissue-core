
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.AliquotCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateAliquotEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateSpecimenEvent;

public interface SpecimenService {

	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent event);

	public SpecimenDeletedEvent delete(DeleteSpecimenEvent event);

	public SpecimenCreatedEvent createSpecimen(CreateSpecimenEvent event);

	SpecimenUpdatedEvent updateSpecimen(UpdateSpecimenEvent event);

	public SpecimenUpdatedEvent patchSpecimen(PatchSpecimenEvent event);

	public AliquotCreatedEvent createAliquot(CreateAliquotEvent event);

	public SpecimensSummaryEvent getSpecimensByLabels(ReqSpecimensSummaryEvent event);
}
