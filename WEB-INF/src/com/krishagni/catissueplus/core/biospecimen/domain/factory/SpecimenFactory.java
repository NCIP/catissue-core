package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;


public interface SpecimenFactory {
public Specimen createSpecimen(SpecimenDetail specimenDetail, ObjectCreationException exceptionHandler);
}
