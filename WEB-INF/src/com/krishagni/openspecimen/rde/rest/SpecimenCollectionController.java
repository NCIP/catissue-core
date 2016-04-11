package com.krishagni.openspecimen.rde.rest;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.LabelPrintJobSummary;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.rde.events.BarcodeDetail;
import com.krishagni.openspecimen.rde.events.ContainerOccupancyDetail;
import com.krishagni.openspecimen.rde.events.ParticipantRegDetail;
import com.krishagni.openspecimen.rde.events.SpecimenAndFrozenEventDetail;
import com.krishagni.openspecimen.rde.events.SpecimenPrintDetail;
import com.krishagni.openspecimen.rde.events.VisitRegDetail;
import com.krishagni.openspecimen.rde.services.SpecimenCollectionService;

@Controller
@RequestMapping("/rde-workflow")
public class SpecimenCollectionController {
	
	@Autowired
	private SpecimenCollectionService spmnCollSvc;

	@RequestMapping(method = RequestMethod.POST, value="/register-participants")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ParticipantRegDetail> registerParticipants(@RequestBody List<ParticipantRegDetail> participants)
	throws Throwable {
		ResponseEvent<List<ParticipantRegDetail>> resp = spmnCollSvc.registerParticipants(request(participants));
		if (resp.getError() != null && resp.getError().getException() != null) {
			throw resp.getError().getException();
		}

		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/visits")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<VisitSpecimenDetail> getVisits(
		@RequestParam(value = "labels", required = false)
		List<String> labels,

		@RequestParam(value = "visitNames", required = false)
		List<String> visitNames) {

		if (CollectionUtils.isNotEmpty(labels)) {
			return response(spmnCollSvc.getVisitsBySpecimens(request(labels)));
		} else if (CollectionUtils.isNotEmpty(visitNames)) {
			return response(spmnCollSvc.getVisitsByNames(request(visitNames)));
		} else {
			return Collections.emptyList();
		}
	}

	@RequestMapping(method = RequestMethod.POST, value="/validate-visit-names")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<BarcodeDetail> validateVisitNames(@RequestBody List<String> barcodes) {
		return response(spmnCollSvc.validateVisitNames(request(barcodes)));
	}

	@RequestMapping(method = RequestMethod.POST, value="/register-visit-names")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<VisitSpecimenDetail> registerVisitNames(@RequestBody List<VisitRegDetail> visitRegs) {
		return response(spmnCollSvc.registerVisitNames(request(visitRegs)));
	}


	@RequestMapping(method = RequestMethod.POST, value="/register-visits")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<VisitSpecimenDetail> registerVisits(@RequestBody List<VisitDetail> visits) {
		return response(spmnCollSvc.registerVisits(request(visits)));
	}

	@RequestMapping(method = RequestMethod.POST, value="/collect-primary-specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenDetail> collectPrimarySpecimens(@RequestBody List<SpecimenDetail> primarySpmns) {
		return response(spmnCollSvc.collectPrimarySpecimens(request(primarySpmns)));
	}

	@RequestMapping(method = RequestMethod.POST, value="/print-specimen-labels")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public LabelPrintJobSummary printSpecimenLabels(@RequestBody List<SpecimenPrintDetail> spmns) {
		return response(spmnCollSvc.printSpecimenLabels(request(spmns)));
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/validate-occupancy-eligibility")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody		
	public ContainerOccupancyDetail validateOccupancyEligibility(@RequestBody ContainerOccupancyDetail occupancyDetail) {
		return response(spmnCollSvc.validateOccupancyEligibility(request(occupancyDetail)));
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/collect-child-specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenAndFrozenEventDetail collectChildSpecimens(@RequestBody SpecimenAndFrozenEventDetail input) {
		return response(spmnCollSvc.collectChildSpecimens(request(input)));
	}

	private <T> RequestEvent<T> request(T payload) {
		return new RequestEvent<>(payload);
	}

	private <T> T response(ResponseEvent<T> resp) {
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}
