package com.krishagni.openspecimen.custom.le.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.custom.le.services.SpecimenCollectionService;

public class SpecimenCollectionServiceImpl implements SpecimenCollectionService {
	
	private VisitService visitService;
	
	public void setVisitService(VisitService visitService) {
		this.visitService = visitService;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<VisitSpecimenDetail>> collectVisitsAndSpecimens(RequestEvent<List<VisitSpecimenDetail>> req) {
		List<VisitSpecimenDetail> responses = new ArrayList<VisitSpecimenDetail>();
		
		for (VisitSpecimenDetail detail : req.getPayload()) {
			RequestEvent<VisitSpecimenDetail> subReq = new RequestEvent<VisitSpecimenDetail>(detail);
			ResponseEvent<VisitSpecimenDetail> subResp = visitService.collectVisitAndSpecimens(subReq);
			if (!subResp.isSuccessful()) {
				return ResponseEvent.error(subResp.getError());
			}
						
			responses.add(subResp.getPayload());
		}
		
		return new ResponseEvent<List<VisitSpecimenDetail>>(responses);
	}
}
