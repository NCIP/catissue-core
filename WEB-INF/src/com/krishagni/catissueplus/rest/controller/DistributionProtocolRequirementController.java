package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolRequirementDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolRequirementListCriteria;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolRequirementService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/distribution-protocol-requirement")
public class DistributionProtocolRequirementController {
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private DistributionProtocolRequirementService dprSvc;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DistributionProtocolRequirementDetail> getAllRequirements(
			@RequestParam(value = "dpId", required = true)
			Long dpId,
			
			@RequestParam(value = "startAt", required = false, defaultValue = "0")
			int startAt,
			
			@RequestParam(value = "maxResult", required = false, defaultValue = "50")
			int maxResult,
			
			@RequestParam(value = "includeDistQty", required = false, defaultValue = "false")
			boolean includeDistQty) {
		
		DistributionProtocolRequirementListCriteria crit = new DistributionProtocolRequirementListCriteria()
			.dpId(dpId)
			.startAt(startAt)
			.maxResults(maxResult)
			.includeDistQty(includeDistQty);
		
		ResponseEvent<List<DistributionProtocolRequirementDetail>> resp = dprSvc.getRequirements(getRequest(crit));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionProtocolRequirementDetail getRequirement(@PathVariable("id") Long reqId) {
		ResponseEvent<DistributionProtocolRequirementDetail> resp = dprSvc.getRequirement(getRequest(reqId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionProtocolRequirementDetail createRequirement(
			@RequestBody
			DistributionProtocolRequirementDetail detail) {
		
		ResponseEvent<DistributionProtocolRequirementDetail> resp = dprSvc.createRequirement(getRequest(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionProtocolRequirementDetail updateRequirement(
			@PathVariable("id")
			Long dprId,
			
			@RequestBody
			DistributionProtocolRequirementDetail detail) {
		
		detail.setId(dprId);
		ResponseEvent<DistributionProtocolRequirementDetail> resp = dprSvc.updateRequirement(getRequest(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionProtocolRequirementDetail deleteRequirement(@PathVariable("id") Long dprId) {
		ResponseEvent<DistributionProtocolRequirementDetail> resp = dprSvc.deleteRequirement(getRequest(dprId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}
}
