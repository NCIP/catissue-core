
package com.krishagni.catissueplus.rest.controller;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError; 

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetails;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolPatchedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

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

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DistributionProtocolDetails createDistributionProtocol(
			@RequestBody DistributionProtocolDetails distributionProtocolDetails) {
		CreateDistributionProtocolEvent event = new CreateDistributionProtocolEvent();
		event.setDistributionProtocolDetails(distributionProtocolDetails);
		event.setSessionDataBean(getSession());
		DistributionProtocolCreatedEvent response = distributionProtocolSvc.createDistributionProtocol(event);
		if (response.getStatus() == EventStatus.OK) {
			return response.getDetails();
		}
		return null;
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
		DistributionProtocolUpdatedEvent response = distributionProtocolSvc.updateDistributionProtocol(event);
		if (response.getStatus() == EventStatus.OK) {
			return response.getDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public DistributionProtocolDetails patchDistributionProtocol(@PathVariable Long id,
			@RequestBody Map<String, Object> values) {
		PatchDistributionProtocolEvent event = new PatchDistributionProtocolEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());

		DistributionProtocolDetails details = new DistributionProtocolDetails();
		try {
			BeanUtils.populate(details, values);
		}
		catch (Exception e) {
			reportError(DistributionProtocolErrorCode.BAD_REQUEST, PATCH_DISTRIBUTION_PROTOCOL);
		}
		details.setModifiedAttributes(new ArrayList<String>(values.keySet()));
		event.setDetails(details);

		DistributionProtocolPatchedEvent response = distributionProtocolSvc.patchDistributionProtocol(event);
		if (response.getStatus() == EventStatus.OK) {
			return response.getDetails();
		}
		return null;
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
