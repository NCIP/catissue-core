
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

import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolsEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetailEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqDistributionProtocolsEvent;
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
	public List<DistributionProtocolDetail> getAllDistributionProtocols(
			@RequestParam(value = "query", required = false, defaultValue = "") String searchStr,
			@RequestParam(value = "startAt", required = false, defaultValue = "0") int startAt,
			@RequestParam(value = "maxResults", required = false, defaultValue = "100") int maxResults) {
		ReqDistributionProtocolsEvent req = new ReqDistributionProtocolsEvent();
		req.setStartAt(startAt);
		req.setMaxResults(maxResults);
		req.setSearchString(searchStr);
		
		DistributionProtocolsEvent resp = distributionProtocolSvc.getDistributionProtocols(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getProtocols();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionProtocolDetail getDistributionProtocol(@PathVariable Long id) {
		ReqDistributionProtocolEvent req = new ReqDistributionProtocolEvent();
		req.setId(id);
		
		DistributionProtocolDetailEvent resp = distributionProtocolSvc.getDistributionProtocol(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getProtocol();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionProtocolDetail createDistributionProtocol(
			@RequestBody DistributionProtocolDetail distributionProtocolDetails) {
		CreateDistributionProtocolEvent req = new CreateDistributionProtocolEvent();
		req.setProtocol(distributionProtocolDetails);
		req.setSessionDataBean(getSession());
		
		DistributionProtocolCreatedEvent resp = distributionProtocolSvc.createDistributionProtocol(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getProtocol();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public DistributionProtocolDetail updateDistributionProtocol(@PathVariable Long id,
			@RequestBody DistributionProtocolDetail details) {
		UpdateDistributionProtocolEvent req = new UpdateDistributionProtocolEvent();
		req.setId(id);
		req.setProtocol(details);
		req.setSessionDataBean(getSession());

		DistributionProtocolUpdatedEvent resp = distributionProtocolSvc.updateDistributionProtocol(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getProtocol();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public DistributionProtocolDetail deleteDistributionProtocol(@PathVariable Long id) {
		DeleteDistributionProtocolEvent req = new DeleteDistributionProtocolEvent();
		req.setId(id);
		req.setSessionDataBean(getSession());
	
		DistributionProtocolDeletedEvent resp = distributionProtocolSvc.deleteDistributionProtocol(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getProtocol();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}