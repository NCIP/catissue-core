
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

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenUnitDetail;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenUnitService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/specimen-units")
public class SpecimenUnitsController {

	@Autowired
	private SpecimenUnitService specUnitsSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenUnitDetail> getUnits() {
		ResponseEvent<List<SpecimenUnitDetail>> resp = specUnitsSvc.getUnits();
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();		
	}
}
