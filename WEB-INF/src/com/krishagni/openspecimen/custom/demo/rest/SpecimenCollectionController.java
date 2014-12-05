package com.krishagni.openspecimen.custom.demo.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.openspecimen.custom.demo.events.CollectSpecimensEvent;
import com.krishagni.openspecimen.custom.demo.events.SpecimenCollectionDetail;
import com.krishagni.openspecimen.custom.demo.events.SpecimensCollectedEvent;
import com.krishagni.openspecimen.custom.demo.services.SpecimenCollectionService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/demo/participant-specimens")
public class SpecimenCollectionController {
	
	@Autowired
	private SpecimenCollectionService specimenCollectionSvc;
	
	@Autowired
	private HttpServletRequest httpReq;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenCollectionDetail collectSpecimens(@RequestBody SpecimenCollectionDetail input) {
		CollectSpecimensEvent req = new CollectSpecimensEvent();
		req.setCollectionDetail(input);
		req.setSessionDataBean(getSession());
		
		SpecimensCollectedEvent resp = specimenCollectionSvc.collect(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getCollectionDetails();
	}
	
	private SessionDataBean getSession() {
		return (SessionDataBean) httpReq.getSession().getAttribute(Constants.SESSION_DATA);
	}	
}
