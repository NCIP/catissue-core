package com.krishagni.catissueplus.rest.controller;

import java.util.List;

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

import com.krishagni.catissueplus.core.administrative.events.ShippingOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.ShippingOrderListCriteria;
import com.krishagni.catissueplus.core.administrative.services.ShippingOrderService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/shipping-orders")
public class ShippingOrderController {

	@Autowired
	private ShippingOrderService shipOrderSvc;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ShippingOrderDetail> getOrders(
			@RequestParam(value = "query", required = false, defaultValue = "")
			String searchTerm,
			
			@RequestParam(value = "institute", required = false, defaultValue = "")
			String institute,
			
			@RequestParam(value = "startAt", required = false, defaultValue = "0")
			int startAt,
				
			@RequestParam(value = "maxResults", required = false, defaultValue = "50")
			int maxResults) {
		
		ShippingOrderListCriteria listCrit = new ShippingOrderListCriteria()
				.query(searchTerm)
				.institute(institute)
				.startAt(startAt)
				.maxResults(maxResults);
		
		ResponseEvent<List<ShippingOrderDetail>> resp = shipOrderSvc.getOrders(getRequest(listCrit));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ShippingOrderDetail getOrder(@PathVariable("id") Long id) {
		ResponseEvent<ShippingOrderDetail> resp = shipOrderSvc.getOrder(getRequest(id));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ShippingOrderDetail createOrder(
			@RequestBody
			ShippingOrderDetail detail) {
		ResponseEvent<ShippingOrderDetail> resp = shipOrderSvc.createOrder(getRequest(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ShippingOrderDetail updateOrder(
			@PathVariable
			Long id,
			
			@RequestBody
			ShippingOrderDetail detail) {
		detail.setId(id);
		ResponseEvent<ShippingOrderDetail> resp = shipOrderSvc.updateOrder(getRequest(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}
}
