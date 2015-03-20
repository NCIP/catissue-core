package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.PrintSpecimenLabelDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenLabelPrintJobSummary;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface SpecimenLabelPrintService {
	public ResponseEvent<SpecimenLabelPrintJobSummary> print(RequestEvent<PrintSpecimenLabelDetail> req);
}
