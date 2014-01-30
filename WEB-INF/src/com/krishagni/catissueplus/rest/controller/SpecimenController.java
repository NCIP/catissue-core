
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.events.specimens.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.events.specimens.SpecimenInfo;
import com.krishagni.catissueplus.service.SpecimenService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/ng/specimens")
public class SpecimenController {

	@Autowired
	private SpecimenService specimenService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/child-specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenInfo> getchildSpecimenList(@PathVariable("id") Long parentId) {
		ReqSpecimenSummaryEvent event = new ReqSpecimenSummaryEvent();
		event.setParentId(parentId);
		event.setSessionDataBean((SessionDataBean)httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA));
		return specimenService.getSpecimensList(event).getSpecimensInfo();
	}
}
