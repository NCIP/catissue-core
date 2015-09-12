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

import com.krishagni.catissueplus.core.administrative.events.RequirementDetail;
import com.krishagni.catissueplus.core.administrative.services.DistributionOrderRequirementService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/distribution-order-requirements")
public class DistributionOrderRequirementController {
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private DistributionOrderRequirementService reqSvc;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<RequirementDetail> getAllRequirements(
			@RequestParam(value = "dpId", required = false)
			Long dpId,
			
			@RequestParam(value = "startAt", required = false, defaultValue = "0")
			int startAt,
			
			@RequestParam(value = "maxResult", required = false, defaultValue = "50")
			int maxResult,
			
			@RequestParam(value = "includeStats", required = false, defaultValue = "false")
			boolean includeStats) {
		
		ResponseEvent<List<RequirementDetail>> resp = reqSvc.getRequirements(new RequestEvent<Long>(dpId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public RequirementDetail getRequirement(@PathVariable("id") Long reqId) {
		ResponseEvent<RequirementDetail> resp = reqSvc.getRequirementById(new RequestEvent<Long>(reqId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public RequirementDetail addNewRequirement(@RequestBody RequirementDetail detail) {
		ResponseEvent<RequirementDetail> resp = reqSvc.addRequirement(new RequestEvent<RequirementDetail>(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public RequirementDetail editRequirement(
			@PathVariable("id")
			Long reqId,
			
			@RequestBody RequirementDetail detail) {
		
		detail.setId(reqId);
		ResponseEvent<RequirementDetail> resp = reqSvc.updateRequirement(new RequestEvent<RequirementDetail>(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Long deleteRequirement(@PathVariable("id") Long reqId) {
		ResponseEvent<Long> resp = reqSvc.deleteRequirement(new RequestEvent<Long>(reqId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}