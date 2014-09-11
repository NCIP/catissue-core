
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.AliquotCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateAliquotEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ValidateSpecimensLabelEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimensLabelValidatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateSpecimenEvent;

public interface SpecimenService {

	public AllSpecimensEvent getAllSpecimens(ReqAllSpecimensEvent event);

	public SpecimenDeletedEvent delete(DeleteSpecimenEvent event);

	public SpecimenCreatedEvent createSpecimen(CreateSpecimenEvent event);

	SpecimenUpdatedEvent updateSpecimen(UpdateSpecimenEvent event);

	public SpecimenUpdatedEvent patchSpecimen(PatchSpecimenEvent event);

	public AliquotCreatedEvent createAliquot(CreateAliquotEvent event);

	public SpecimensLabelValidatedEvent validateSpecimensLabel(ValidateSpecimensLabelEvent event);
}
