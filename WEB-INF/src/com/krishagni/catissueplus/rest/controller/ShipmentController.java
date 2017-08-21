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

import com.krishagni.catissueplus.core.administrative.events.ShipmentDetail;
import com.krishagni.catissueplus.core.administrative.events.ShipmentListCriteria;
import com.krishagni.catissueplus.core.administrative.services.ShipmentService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.QueryDataExportResult;

@Controller
@RequestMapping("/shipments")
public class ShipmentController {

	@Autowired
	private ShipmentService shipmentSvc;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ShipmentDetail> getShipments(
			@RequestParam(value = "name", required = false, defaultValue = "")
			String name,
			
			@RequestParam(value = "recvInstitute", required = false, defaultValue = "")
			String recvInstitute,
			
			@RequestParam(value = "recvSite", required = false, defaultValue = "")
			String recvSite,
			
			@RequestParam(value = "startAt", required = false, defaultValue = "0")
			int startAt,
				
			@RequestParam(value = "maxResults", required = false, defaultValue = "50")
			int maxResults) {
		
		ShipmentListCriteria listCrit = new ShipmentListCriteria()
				.name(name)
				.recvInstitute(recvInstitute)
				.recvSite(recvSite)
				.startAt(startAt)
				.maxResults(maxResults);
		
		ResponseEvent<List<ShipmentDetail>> resp = shipmentSvc.getShipments(getRequest(listCrit));
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/count")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Long> getShipmentsCount(
			@RequestParam(value = "name", required = false, defaultValue = "")
			String name,
			
			@RequestParam(value = "recvInstitute", required = false, defaultValue = "")
			String recvInstitute,
			
			@RequestParam(value = "recvSite", required = false, defaultValue = "")
			String recvSite) {
		
		ShipmentListCriteria listCrit = new ShipmentListCriteria()
				.name(name)
				.recvInstitute(recvInstitute)
				.recvSite(recvSite);
		
		ResponseEvent<Long> resp = shipmentSvc.getShipmentsCount(getRequest(listCrit));
		resp.throwErrorIfUnsuccessful();
		
		return Collections.singletonMap("count", resp.getPayload());
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ShipmentDetail getShipment(@PathVariable("id") Long id) {
		ResponseEvent<ShipmentDetail> resp = shipmentSvc.getShipment(getRequest(id));
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ShipmentDetail createShipment(
			@RequestBody
			ShipmentDetail detail) {
		ResponseEvent<ShipmentDetail> resp = shipmentSvc.createShipment(getRequest(detail));
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ShipmentDetail updateShipment(
			@PathVariable
			Long id,
			
			@RequestBody
			ShipmentDetail detail) {
		detail.setId(id);
		ResponseEvent<ShipmentDetail> resp = shipmentSvc.updateShipment(getRequest(detail));
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/report")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public QueryDataExportResult exportReport(@PathVariable Long id) {
		ResponseEvent<QueryDataExportResult> resp = shipmentSvc.exportReport(getRequest(id));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}
}
