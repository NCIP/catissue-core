
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;

public interface SpecimenFactory {
	public Specimen createSpecimen(SpecimenDetail specimenDetail, Specimen parent);
}
