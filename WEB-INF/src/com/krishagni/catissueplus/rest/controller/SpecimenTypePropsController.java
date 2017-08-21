
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenTypeProps;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenTypePropsService;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/specimen-type-props")
public class SpecimenTypePropsController {

	@Autowired
	private SpecimenTypePropsService spmnTypePropsSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenTypeProps> getProps() {
		ResponseEvent<List<SpecimenTypeProps>> resp = spmnTypePropsSvc.getProps();
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}
