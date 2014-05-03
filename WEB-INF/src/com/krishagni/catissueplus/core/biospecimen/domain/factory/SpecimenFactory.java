
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.AliquotDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;

public interface SpecimenFactory {

	public Specimen createSpecimen(SpecimenDetail specimenDetail);

	public Specimen patch(Specimen oldSpecimen, SpecimenDetail specimenDetail);

	public Set<Specimen> createAliquots(Specimen specimen, AliquotDetail aliquotDetail);
}
