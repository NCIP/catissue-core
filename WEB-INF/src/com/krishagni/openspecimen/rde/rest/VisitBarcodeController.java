package com.krishagni.openspecimen.rde.rest;

import java.util.List;

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
import com.krishagni.openspecimen.rde.services.VisitRegistrationService;

@Controller
@RequestMapping("/visit-barcodes")
public class VisitBarcodeController {
	
	@Autowired
	private VisitRegistrationService visitRegSvc;
		
	@RequestMapping(method = RequestMethod.POST, value="/validate")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public List<BarcodeDetail> validate(@RequestBody List<String> barcodes) {
		RequestEvent<List<String>> req = new RequestEvent<List<String>>(barcodes);
		ResponseEvent<List<BarcodeDetail>> resp = visitRegSvc.validateVisits(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
		
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public List<VisitSpecimenDetail> registerVisitBarcodes(@RequestBody List<VisitRegDetail> visitRegs) {
		RequestEvent<List<VisitRegDetail>> req = new RequestEvent<List<VisitRegDetail>>(visitRegs);
		ResponseEvent<List<VisitSpecimenDetail>> resp = visitRegSvc.registerVisitBarcodes(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST, value="/participants")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ParticipantRegDetail> registerParticipants(@RequestBody List<ParticipantRegDetail> participants)
	throws Throwable {
		RequestEvent<List<ParticipantRegDetail>> req = new RequestEvent<List<ParticipantRegDetail>>(participants);
		ResponseEvent<List<ParticipantRegDetail>> resp = visitRegSvc.registerParticipants(req);

		if (resp.getError() != null && resp.getError().getException() != null) {
			throw resp.getError().getException();
		}

		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/visits")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public List<VisitSpecimenDetail> getVisits(
			@RequestParam(value = "aliquotLabels", required = true)
			List<String> aliquotLabels) {
		RequestEvent<List<String>> req = new RequestEvent<List<String>>(aliquotLabels);
		
		ResponseEvent<List<VisitSpecimenDetail>> resp = visitRegSvc.getVisits(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST, value="/visits")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<VisitSpecimenDetail> registerVisits(@RequestBody List<VisitDetail> visits) {
		RequestEvent<List<VisitDetail>> req = new RequestEvent<List<VisitDetail>>(visits);
		ResponseEvent<List<VisitSpecimenDetail>> resp = visitRegSvc.registerVisits(req);
		resp.throwErrorIfUnsuccessful();

		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST, value="/primary-specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenDetail> savePrimarySpecimens(@RequestBody List<SpecimenDetail> primarySpmns) {
		RequestEvent<List<SpecimenDetail>> req = new RequestEvent<List<SpecimenDetail>>(primarySpmns);
		ResponseEvent<List<SpecimenDetail>> resp = visitRegSvc.savePrimarySpecimens(req);
		resp.throwErrorIfUnsuccessful();

		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST, value="/specimen-labels-print-jobs")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public LabelPrintJobSummary printSpecimenLabels(@RequestBody List<SpecimenPrintDetail> spmns) {
		RequestEvent<List<SpecimenPrintDetail>> req = new RequestEvent<List<SpecimenPrintDetail>>(spmns);
		ResponseEvent<LabelPrintJobSummary> resp = visitRegSvc.printSpecimenLabels(req);
		resp.throwErrorIfUnsuccessful();

		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/validate-specimens-occupancy")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody		
	public ContainerOccupancyDetail validateSpecimensOccupany(
			@RequestBody
			ContainerOccupancyDetail occupancyDetail) {
		RequestEvent<ContainerOccupancyDetail> req = new RequestEvent<ContainerOccupancyDetail>(occupancyDetail);
		ResponseEvent<ContainerOccupancyDetail> resp = visitRegSvc.validateOccupancyEligibility(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();		
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/child-specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenAndFrozenEventDetail collectChildSpecimens(@RequestBody SpecimenAndFrozenEventDetail input) {
		RequestEvent<SpecimenAndFrozenEventDetail> req = new RequestEvent<SpecimenAndFrozenEventDetail>(input);
		ResponseEvent<SpecimenAndFrozenEventDetail> resp = visitRegSvc.collectChildSpecimens(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();		
	}	
}
