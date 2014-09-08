
package com.krishagni.catissueplus.rest.controller;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
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

import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.events.AllDistributionProtocolsEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetails;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolPatchDetails;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolPatchedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.GetDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.GotDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/distribution-protocols")
public class DistributionProtocolController {

	private static final String PATCH_DISTRIBUTION_PROTOCOL = "patch distribution protocol";

	@Autowired
	private DistributionProtocolService distributionProtocolSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DistributionProtocolDetails> getAllDistributionProtocols(
			@RequestParam(value = "maxResults", required = false, defaultValue = "1000") String maxResults) {
		ReqAllDistributionProtocolEvent req = new ReqAllDistributionProtocolEvent();
		req.setMaxResults(Integer.parseInt(maxResults));
		AllDistributionProtocolsEvent resp = distributionProtocolSvc.getAllDistributionProtocols(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getProtocols();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionProtocolDetails getBiohazard(@PathVariable Long id) {
		GetDistributionProtocolEvent event = new GetDistributionProtocolEvent();
		event.setId(id);
		GotDistributionProtocolEvent resp = distributionProtocolSvc.getDistributionProtocol(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/title={title}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionProtocolDetails getBiohazard(@PathVariable String title) {
		GetDistributionProtocolEvent event = new GetDistributionProtocolEvent();
		event.setTitle(title);
		GotDistributionProtocolEvent resp = distributionProtocolSvc.getDistributionProtocol(event);
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

	@RequestMapping(method = RequestMethod.PUT, value = "/title={title}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public DistributionProtocolDetails updateDistributionProtocol(@PathVariable String title,
			@RequestBody DistributionProtocolDetails details) {
		UpdateDistributionProtocolEvent event = new UpdateDistributionProtocolEvent();
		event.setTitle(title);
		event.setDetails(details);
		event.setSessionDataBean(getSession());
		DistributionProtocolUpdatedEvent resp = distributionProtocolSvc.updateDistributionProtocol(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public DistributionProtocolDetails patchDistributionProtocol(@PathVariable Long id,
			@RequestBody Map<String, Object> values) {
		PatchDistributionProtocolEvent event = new PatchDistributionProtocolEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());

		DistributionProtocolPatchDetails details = new DistributionProtocolPatchDetails();
		try {
			BeanUtils.populate(details, values);
		}
		catch (Exception e) {
			reportError(DistributionProtocolErrorCode.BAD_REQUEST, PATCH_DISTRIBUTION_PROTOCOL);
		}
		details.setModifiedAttributes(new ArrayList<String>(values.keySet()));
		event.setDetails(details);

		DistributionProtocolPatchedEvent resp = distributionProtocolSvc.patchDistributionProtocol(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/title={title}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public DistributionProtocolDetails patchDistributionProtocolByTitle(@PathVariable String title,
			@RequestBody Map<String, Object> values) {
		PatchDistributionProtocolEvent event = new PatchDistributionProtocolEvent();
		event.setTitle(title);
		event.setSessionDataBean(getSession());

		DistributionProtocolPatchDetails details = new DistributionProtocolPatchDetails();
		try {
			BeanUtils.populate(details, values);
		}
		catch (Exception e) {
			reportError(DistributionProtocolErrorCode.BAD_REQUEST, PATCH_DISTRIBUTION_PROTOCOL);
		}
		details.setModifiedAttributes(new ArrayList<String>(values.keySet()));
		event.setDetails(details);

		DistributionProtocolPatchedEvent resp = distributionProtocolSvc.patchDistributionProtocol(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String deleteDistributionProtocol(@PathVariable Long id) {
		DeleteDistributionProtocolEvent event = new DeleteDistributionProtocolEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());
		DistributionProtocolDeletedEvent resp = distributionProtocolSvc.deleteDistributionProtocol(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/title={title}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String deleteDistributionProtocol(@PathVariable String title) {
		DeleteDistributionProtocolEvent event = new DeleteDistributionProtocolEvent();
		event.setTitle(title);
		event.setSessionDataBean(getSession());
		DistributionProtocolDeletedEvent resp = distributionProtocolSvc.deleteDistributionProtocol(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}