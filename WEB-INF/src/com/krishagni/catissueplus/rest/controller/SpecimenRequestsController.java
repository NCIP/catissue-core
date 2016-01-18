package com.krishagni.catissueplus.rest.controller;

import java.util.Collections;
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
import com.krishagni.catissueplus.core.common.events.EntityStatusDetail;
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
		@RequestParam(value = "cpId", required = false)
		Long cpId,

		@RequestParam(value = "startAt", required = false, defaultValue = "0")
		int startAt,

		@RequestParam(value = "maxResults", required = false, defaultValue = "100")
		int maxResults,

		@RequestParam(value = "includeStats", required = false, defaultValue = "false")
		boolean includeStats) {

		SpecimenRequestListCriteria listCriteria = new SpecimenRequestListCriteria()
			.cpId(cpId)
			.startAt(startAt)
			.maxResults(maxResults)
			.includeStat(includeStats);
		return response(spmnReqSvc.getRequests(request(listCriteria)));
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenRequestDetail getSpecimenRequest(@PathVariable("id") Long reqId) {
		return response(spmnReqSvc.getRequest(request(reqId)));
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/form-data")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> getSpecimenRequestFormData(@PathVariable("id") Long reqId) {
		return response(spmnReqSvc.getRequestFormData(request(reqId)));
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenRequestSummary> createSpecimenRequest(@RequestBody RequestListSpecimensDetail detail) {
		return response(spmnReqSvc.createRequest(request(detail)));
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{id}/status")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenRequestSummary updateStatus(@PathVariable("id") Long reqId, @RequestBody EntityStatusDetail detail) {
		detail.setId(reqId);
		return response(spmnReqSvc.updateStatus(request(detail)));
	}


	/** Used mostly for UI purpose **/
	@RequestMapping(method = RequestMethod.GET, value="form-ids")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Long> getRequestFormIds(@RequestParam("listId") Long listId) {
		return  response(spmnReqSvc.getFormIds(request(listId)));
	}

	@RequestMapping(method = RequestMethod.GET, value="have-requests")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Boolean> haveRequests(@RequestParam(value = "cpId", required = false) Long cpId) {
		SpecimenRequestListCriteria listCriteria = new SpecimenRequestListCriteria().cpId(cpId);
		Boolean haveRequests = response(spmnReqSvc.haveRequests(request(listCriteria)));
		return Collections.singletonMap("haveRequests", haveRequests);
	}

	private <T> RequestEvent<T> request(T payload) {
		return new RequestEvent<T>(payload);
	}

	private <T> T response(ResponseEvent<T> resp) {
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}