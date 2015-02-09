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

import com.krishagni.catissueplus.core.administrative.events.AllDistributionOrdersEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateDistributionOrderEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllDistributionOrdersEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDistributionOrderEvent;
import com.krishagni.catissueplus.core.administrative.services.DistributionOrderService;

@Controller
@RequestMapping("/distribution-orders")
public class DistributionOrderController {
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private DistributionOrderService distributionService;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionOrderDetail createDistribution(
			@RequestBody DistributionOrderDetail detail) {
		CreateDistributionOrderEvent req = new CreateDistributionOrderEvent();
		req.setDetail(detail);
		
		DistributionOrderCreatedEvent resp = distributionService.createDistribution(req);
		
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getDetail();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionOrderDetail updateDistribution(@PathVariable("id") Long distributionId, 
			@RequestBody DistributionOrderDetail detail) {
		UpdateDistributionOrderEvent req = new UpdateDistributionOrderEvent();
		req.setDetail(detail);
		detail.setId(distributionId);
		
		DistributionOrderUpdatedEvent resp = distributionService.updateDistribution(req);
		
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getDetail();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DistributionOrderDetail> getDistributions(
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "max", required = false, defaultValue = "50") int max) {
		ReqAllDistributionOrdersEvent req = new ReqAllDistributionOrdersEvent();
		req.setStartAt(start);
		req.setMaxResults(max);
		
		AllDistributionOrdersEvent resp = distributionService.getAllDistributionOrders(req);
		
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getOrders();
	}
}