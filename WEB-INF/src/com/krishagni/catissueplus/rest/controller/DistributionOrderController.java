package com.krishagni.catissueplus.rest.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderItemDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderListCriteria;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderSummary;
import com.krishagni.catissueplus.core.administrative.events.ReturnedSpecimenDetail;
import com.krishagni.catissueplus.core.administrative.services.DistributionOrderService;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.QueryDataExportResult;

@Controller
@RequestMapping("/distribution-orders")
public class DistributionOrderController {
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private DistributionOrderService distributionService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DistributionOrderSummary> getDistributionOrders(			
		@RequestParam(value = "query", required = false, defaultValue = "") 
		String searchTerm,
		
		@RequestParam(value = "dpShortTitle", required = false, defaultValue = "")
		String dpShortTitle,
		
		@RequestParam(value = "dpId", required = false)
		Long dpId,
		
		@RequestParam(value = "requestor", required = false, defaultValue = "")
		String requestor,
		
		@RequestParam(value = "requestorId", required = false)
		Long requestorId,
		
		@RequestParam(value = "executionDate", required = false) 
		@DateTimeFormat(pattern="yyyy-MM-dd")
		Date executionDate,
		
		@RequestParam(value = "receivingSite", required = false, defaultValue = "")
		String receivingSite,
		
		@RequestParam(value = "receivingInstitute", required = false, defaultValue = "")
		String receivingInstitute,
		
		@RequestParam(value = "startAt", required = false, defaultValue = "0") 
		int startAt,
			
		@RequestParam(value = "maxResults", required = false, defaultValue = "50") 
		int maxResults,

		@RequestParam(value = "includeStats", required = false, defaultValue = "false") 
		boolean includeStats) {
		
		
		DistributionOrderListCriteria listCrit = new DistributionOrderListCriteria()
			.query(searchTerm)
			.dpShortTitle(dpShortTitle)
			.dpId(dpId)
			.requestor(requestor)
			.requestorId(requestorId)
			.executionDate(executionDate)
			.receivingSite(receivingSite)
			.receivingInstitute(receivingInstitute)
			.startAt(startAt)
			.maxResults(maxResults)
			.includeStat(includeStats);
			
		ResponseEvent<List<DistributionOrderSummary>> resp = distributionService.getOrders(getRequest(listCrit));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/count")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Long> getDistributionOrdersCount(
		@RequestParam(value = "query", required = false, defaultValue = "")
		String searchTerm,
		
		@RequestParam(value = "dpShortTitle", required = false, defaultValue = "")
		String dpShortTitle,
		
		@RequestParam(value = "dpId", required = false)
		Long dpId,
		
		@RequestParam(value = "requestor", required = false, defaultValue = "")
		String requestor,
		
		@RequestParam(value = "requestorId", required = false)
		Long requestorId,
		
		@RequestParam(value = "executionDate", required = false)
		@DateTimeFormat(pattern="yyyy-MM-dd")
		Date executionDate,
		
		@RequestParam(value = "receivingSite", required = false, defaultValue = "")
		String receivingSite,
		
		@RequestParam(value = "receivingInstitute", required = false, defaultValue = "")
		String receivingInstitute) {
		
		
		DistributionOrderListCriteria listCrit = new DistributionOrderListCriteria()
			.query(searchTerm)
			.dpShortTitle(dpShortTitle)
			.dpId(dpId)
			.requestor(requestor)
			.requestorId(requestorId)
			.executionDate(executionDate)
			.receivingSite(receivingSite)
			.receivingInstitute(receivingInstitute);
			
		ResponseEvent<Long> resp = distributionService.getOrdersCount(getRequest(listCrit));
		resp.throwErrorIfUnsuccessful();
		return Collections.singletonMap("count", resp.getPayload());
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionOrderDetail getDistribution(@PathVariable("id") Long id) {
		ResponseEvent<DistributionOrderDetail> resp = distributionService.getOrder(getRequest(id));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionOrderDetail createDistribution(@RequestBody DistributionOrderDetail order) {
		order.setId(null);
		ResponseEvent<DistributionOrderDetail> resp = distributionService.createOrder(getRequest(order));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionOrderDetail updateDistribution(
		@PathVariable("id") 
		Long distributionId,
		
		@RequestBody 
		DistributionOrderDetail order) {
		
		order.setId(distributionId);
		ResponseEvent<DistributionOrderDetail> resp = distributionService.updateOrder(getRequest(order));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionOrderDetail deleteOrder(@PathVariable("id") Long orderId) {
		ResponseEvent<DistributionOrderDetail> resp = distributionService.deleteOrder(getRequest(orderId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/report")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public QueryDataExportResult exportDistributionReport(@PathVariable("id") Long orderId) {
		ResponseEvent<QueryDataExportResult> resp = distributionService.exportReport(getRequest(orderId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DistributionOrderItemDetail> getDistributedSpecimens(
		@RequestParam(value = "label", required = false)
		List<String> labels,

		@RequestParam(value = "barcode", required = false)
		List<String> barcodes) {

		if (CollectionUtils.isEmpty(labels) && CollectionUtils.isEmpty(barcodes)) {
			return Collections.emptyList();
		}

		SpecimenListCriteria crit = new SpecimenListCriteria()
			.labels(labels)
			.barcodes(barcodes);

		RequestEvent<SpecimenListCriteria> req = getRequest(crit);
		ResponseEvent<List<DistributionOrderItemDetail>> resp = distributionService.getDistributedSpecimens(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/return-specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenInfo> returnSpecimens(@RequestBody List<ReturnedSpecimenDetail> returnedSpecimens) {
		ResponseEvent<List<SpecimenInfo>> resp = distributionService.returnSpecimens(getRequest(returnedSpecimens));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}
}
