package com.krishagni.catissueplus.rest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.administrative.events.RequestListSpecimensDetail;
import com.krishagni.catissueplus.core.administrative.events.SpecimenRequestDetail;
import com.krishagni.catissueplus.core.administrative.events.SpecimenRequestSummary;
import com.krishagni.catissueplus.core.administrative.repository.SpecimenRequestListCriteria;
import com.krishagni.catissueplus.core.administrative.services.SpecimenRequestService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/specimen-requests")
public class SpecimenRequestsController {

	@Autowired
	private SpecimenRequestService spmnReqSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenRequestSummary> getSpecimenRequests(
		@RequestParam(value = "startAt", required = false, defaultValue = "0")
		int startAt,

		@RequestParam(value = "maxResults", required = false, defaultValue = "100")
		int maxResults) {

		SpecimenRequestListCriteria listCriteria = new SpecimenRequestListCriteria()
			.startAt(startAt)
			.maxResults(maxResults);

		ResponseEvent<List<SpecimenRequestSummary>> resp = spmnReqSvc.getRequests(getRequest(listCriteria));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenRequestDetail getSpecimenRequests(@PathVariable("id") Long reqId) {
		ResponseEvent<SpecimenRequestDetail> resp = spmnReqSvc.getRequest(getRequest(reqId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/form-data")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> getSpecimenRequestFormData(@PathVariable("id") Long reqId) {
		ResponseEvent<Map<String, Object>> resp = spmnReqSvc.getRequestFormData(getRequest(reqId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenRequestSummary> createSpecimenRequest(@RequestBody RequestListSpecimensDetail detail) {
		ResponseEvent<List<SpecimenRequestSummary>> resp = spmnReqSvc.createRequest(getRequest(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}
}
