
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
import com.krishagni.catissueplus.service.SpecimenCollGroupService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/ng/specimen-collection-groups")
public class SpecimenCollectionGroupController {

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private SpecimenCollGroupService specimenCollGroupService;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/child-specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenInfo> getSpecimensList(@PathVariable("id") Long scgId) {
		ReqSpecimenSummaryEvent event = new ReqSpecimenSummaryEvent();
		event.setSessionDataBean((SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA));
		event.setParentId(scgId);
		return specimenCollGroupService.getSpecimensList(event).getSpecimensInfo();
	}

}
