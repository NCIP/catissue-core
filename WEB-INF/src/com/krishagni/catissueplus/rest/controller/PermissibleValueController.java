
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.administrative.events.ListPvCriteria;
import com.krishagni.catissueplus.core.administrative.events.PvInfo;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/permissible-values")
public class PermissibleValueController {

	@Autowired
	private PermissibleValueService pvSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET, value = "/attribute={attribute}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<PvInfo> getPermissibleValue(
			@PathVariable 
			String attribute,
			
			@RequestParam(value = "parentValue", required = false, defaultValue = "") 
			String parentValue,
			
			@RequestParam(value = "searchString", required = false, defaultValue = "") 
			String searchStr,
			
			@RequestParam(value = "maxResults", required = false, defaultValue = "1000") 
			int maxResults) {
		
		ListPvCriteria crit = new ListPvCriteria()
			.attribute(attribute)
			.query(searchStr)
			.parentValue(parentValue)
			.maxResults(maxResults);
		
		RequestEvent<ListPvCriteria> req = new RequestEvent<ListPvCriteria>(getSession(), crit);
		ResponseEvent<List<PvInfo>> resp = pvSvc.getPermissibleValues(req);
		resp.throwErrorIfUnsuccessful();
				
		return resp.getPayload();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
