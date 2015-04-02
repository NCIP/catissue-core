package com.krishagni.openspecimen.custom.le.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.custom.le.services.SpecimenCollectionService;

@Controller("leSpecimenCollectionController")
@RequestMapping("/le/specimens")
public class SpecimenCollectionController {
	
	@Autowired
	private SpecimenCollectionService specimenCollSvc;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public List<VisitSpecimenDetail> collectPrimarySpecimens(@RequestBody List<VisitSpecimenDetail> input) {
		RequestEvent<List<VisitSpecimenDetail>> req = new RequestEvent<List<VisitSpecimenDetail>>(input);
		ResponseEvent<List<VisitSpecimenDetail>> resp = specimenCollSvc.collectVisitsAndSpecimens(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

}
