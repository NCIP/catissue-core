package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.events.PrintSpecimenLabelDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenLabelPrintJobSummary;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenLabelPrintService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenLabelPrintServiceImpl implements SpecimenLabelPrintService {

	@Override
	public ResponseEvent<SpecimenLabelPrintJobSummary> print(
			RequestEvent<PrintSpecimenLabelDetail> req) {
		// TODO Auto-generated method stub
		return null;
	}

}
