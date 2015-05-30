
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenQuantityUnitDetail;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenQuantityUnitService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/specimen-quantity-units")
public class SpecimenQuantityUnitsController {

	@Autowired
	private SpecimenQuantityUnitService qtyUnitSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenQuantityUnitDetail> getUnits() {
		ResponseEvent<List<SpecimenQuantityUnitDetail>> resp = qtyUnitSvc.getQuantityUnits();
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();		
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenQuantityUnitDetail saveUnit(@RequestBody SpecimenQuantityUnitDetail detail) {
		RequestEvent<SpecimenQuantityUnitDetail> req = new RequestEvent<SpecimenQuantityUnitDetail>(detail);
		ResponseEvent<SpecimenQuantityUnitDetail> resp = qtyUnitSvc.saveOrUpdate(req);
		resp.throwErrorIfUnsuccessful();		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenQuantityUnitDetail updateUnit(@PathVariable("id") Long id, @RequestBody SpecimenQuantityUnitDetail detail) {
		detail.setId(id);
		
		RequestEvent<SpecimenQuantityUnitDetail> req = new RequestEvent<SpecimenQuantityUnitDetail>(detail);
		ResponseEvent<SpecimenQuantityUnitDetail> resp = qtyUnitSvc.saveOrUpdate(req);
		resp.throwErrorIfUnsuccessful();		
		return resp.getPayload();
	}
}
