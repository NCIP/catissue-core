
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.administrative.events.ListPvCriteria;
import com.krishagni.catissueplus.core.administrative.events.PvDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.PermissibleValueService;

@Controller
@RequestMapping("/permissible-values")
public class PermissibleValueController {

	@Autowired
	private PermissibleValueService pvSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<PvDetail> getPermissibleValue(
			@RequestParam(value = "attribute", required = false) 
			String attribute,
			
			@RequestParam(value = "searchString", required = false) 
			String searchStr,
			
			@RequestParam(value = "includeParentValue", required = false, defaultValue="false")
			boolean includeParentValue,

			@RequestParam(value = "parentAttribute", required = false)
			String parentAttribute,
			
			@RequestParam(value = "parentValue", required = false) 
			String parentValue,
			
			@RequestParam(value = "includeOnlyLeafValue", required = false, defaultValue="false")
			boolean includeOnlyLeafValue,
						
			@RequestParam(value = "maxResults", required = false, defaultValue = "1000") 
			int maxResults) {
		
		ListPvCriteria crit = new ListPvCriteria()
			.attribute(attribute)
			.query(searchStr)
			.parentValue(parentValue)
			.includeParentValue(includeParentValue)
			.parentAttribute(parentAttribute)
			.includeOnlyLeafValue(includeOnlyLeafValue)
			.maxResults(maxResults);
		
		RequestEvent<ListPvCriteria> req = new RequestEvent<ListPvCriteria>(crit);
		ResponseEvent<List<PvDetail>> resp = pvSvc.getPermissibleValues(req);
		resp.throwErrorIfUnsuccessful();
				
		return resp.getPayload();
	}
}
