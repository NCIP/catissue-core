
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

import com.krishagni.catissueplus.events.specimencollectiongroups.ReqSpecimenCollGroupSummaryEvent;
import com.krishagni.catissueplus.events.specimencollectiongroups.SpecimenCollectionGroupInfo;
import com.krishagni.catissueplus.service.CollectionProtocolRegistrationService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/ng/collection-protocol-registrations")
public class CollectionProtocolRegistrationController {

	@Autowired
	private CollectionProtocolRegistrationService registrationService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/specimen-collection-groups")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenCollectionGroupInfo> getCollectionGroupsList(@PathVariable("id") Long cprId) {
		ReqSpecimenCollGroupSummaryEvent event = new ReqSpecimenCollGroupSummaryEvent();
		event.setCollectionProtocolRegistrationId(cprId);
		event.setSessionDataBean((SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA));
		return registrationService.getSpecimenCollGroupsList(event).getSpecimenCollectionGroupsInfo();
	}
}
