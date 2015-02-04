
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.krishagni.catissueplus.core.administrative.events.AllDistributionProtocolsEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetailEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetails;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/distribution-protocols")
public class DistributionProtocolController {
	@Autowired
	private DistributionProtocolService distributionProtocolSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DistributionProtocolDetails> getAllDistributionProtocols(
			@RequestParam(value = "startAt", required = false, defaultValue = "0") int startAt,
			@RequestParam(value = "maxResults", required = false, defaultValue = "100") int maxResults) {
		ReqAllDistributionProtocolEvent req = new ReqAllDistributionProtocolEvent();
		
		req.setStartAt(startAt);
		req.setMaxResults(maxResults);
		AllDistributionProtocolsEvent resp = distributionProtocolSvc.getAllDistributionProtocols(req);
		
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getProtocols();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionProtocolDetails getDistributionProtocol(@PathVariable Long id) {
		ReqDistributionProtocolEvent event = new ReqDistributionProtocolEvent();
		event.setId(id);
		DistributionProtocolDetailEvent resp = distributionProtocolSvc.getDistributionProtocol(event);
		
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionProtocolDetails createDistributionProtocol(
			@RequestBody DistributionProtocolDetails distributionProtocolDetails) {
		CreateDistributionProtocolEvent event = new CreateDistributionProtocolEvent();
		event.setDistributionProtocolDetails(distributionProtocolDetails);
		event.setSessionDataBean(getSession());
		DistributionProtocolCreatedEvent resp = distributionProtocolSvc.createDistributionProtocol(event);
		
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public DistributionProtocolDetails updateDistributionProtocol(@PathVariable Long id,
			@RequestBody DistributionProtocolDetails details) {
		UpdateDistributionProtocolEvent event = new UpdateDistributionProtocolEvent();
		event.setId(id);
		event.setDetails(details);
		event.setSessionDataBean(getSession());
		DistributionProtocolUpdatedEvent resp = distributionProtocolSvc.updateDistributionProtocol(event);
		
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public DistributionProtocolDetails deleteDistributionProtocol(@PathVariable Long id) {
		DeleteDistributionProtocolEvent event = new DeleteDistributionProtocolEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());
		DistributionProtocolDeletedEvent resp = distributionProtocolSvc.deleteDistributionProtocol(event);
		
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getDetails();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}