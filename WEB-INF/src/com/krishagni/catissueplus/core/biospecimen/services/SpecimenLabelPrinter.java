package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenLabelPrintJob;

public interface SpecimenLabelPrinter {
	public SpecimenLabelPrintJob print(List<Specimen> specimens);
}
