package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;


public interface SpecimenFactory {
public Specimen createSpecimen(SpecimenDetail specimenDetail);

public Specimen patch(Specimen oldSpecimen, Map<String, Object> specimenProps);
}
