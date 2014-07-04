
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

import com.krishagni.catissueplus.core.administrative.domain.factory.BiohazardErrorCode;
import com.krishagni.catissueplus.core.administrative.events.BiohazardCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.BiohazardDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.BiohazardDetails;
import com.krishagni.catissueplus.core.administrative.events.BiohazardPatchDetails;
import com.krishagni.catissueplus.core.administrative.events.BiohazardUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateBiohazardEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteBiohazardEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchBiohazardEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateBiohazardEvent;
import com.krishagni.catissueplus.core.administrative.services.BiohazardService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/biohazards")
public class BiohazardController {

	private static final String PATCH_BIOHAZARD = "patch biohazard";

	@Autowired
	private BiohazardService biohazardSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public BiohazardDetails createBiohazard(@RequestBody BiohazardDetails biohazardDetails) {
		CreateBiohazardEvent biohazardEvent = new CreateBiohazardEvent();
		biohazardEvent.setBiohazardDetails(biohazardDetails);
		biohazardEvent.setSessionDataBean(getSession());
		BiohazardCreatedEvent response = biohazardSvc.createBiohazard(biohazardEvent);

		if (response.getStatus().equals(EventStatus.OK)) {
			return response.getBiohazardDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public BiohazardDetails updateBiohazard(@PathVariable Long id, @RequestBody BiohazardDetails details) {
		UpdateBiohazardEvent event = new UpdateBiohazardEvent();
		event.setBiohazardDetails(details);
		event.setId(id);
		event.setSessionDataBean(getSession());
		BiohazardUpdatedEvent response = biohazardSvc.updateBiohazard(event);
		if (response != null) {
			return response.getBiohazardDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/name={name}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public BiohazardDetails updateBiohazard(@PathVariable String name, @RequestBody BiohazardDetails details) {
		UpdateBiohazardEvent event = new UpdateBiohazardEvent();
		event.setBiohazardDetails(details);
		event.setName(name);
		event.setSessionDataBean(getSession());
		BiohazardUpdatedEvent response = biohazardSvc.updateBiohazard(event);
		if (response != null) {
			return response.getBiohazardDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public BiohazardDetails patchBiohazard(@PathVariable long id, @RequestBody Map<String, Object> values) {
		PatchBiohazardEvent event = new PatchBiohazardEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());
		BiohazardPatchDetails details = new BiohazardPatchDetails();
		try {
			BeanUtils.populate(details, values);
		}
		catch (Exception e) {
			reportError(BiohazardErrorCode.BAD_REQUEST, PATCH_BIOHAZARD);
		}
		details.setModifiedAttributes(new ArrayList<String>(values.keySet()));
		event.setDetails(details);

		BiohazardUpdatedEvent response = biohazardSvc.patchBiohazard(event);
		if (response.getStatus() == EventStatus.OK) {
			return response.getBiohazardDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/name={name}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public BiohazardDetails patchBiohazard(@PathVariable String name, @RequestBody Map<String, Object> values) {
		PatchBiohazardEvent event = new PatchBiohazardEvent();
		event.setName(name);
		event.setSessionDataBean(getSession());
		BiohazardPatchDetails details = new BiohazardPatchDetails();
		try {
			BeanUtils.populate(details, values);
		}
		catch (Exception e) {
			reportError(BiohazardErrorCode.BAD_REQUEST, PATCH_BIOHAZARD);
		}
		details.setModifiedAttributes(new ArrayList<String>(values.keySet()));
		event.setDetails(details);

		BiohazardUpdatedEvent response = biohazardSvc.patchBiohazard(event);
		if (response.getStatus() == EventStatus.OK) {
			return response.getBiohazardDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String deleteBiohazard(@PathVariable Long id) {
		DeleteBiohazardEvent reqEvent = new DeleteBiohazardEvent();
		reqEvent.setId(id);
		reqEvent.setSessionDataBean(getSession());
		BiohazardDeletedEvent response = biohazardSvc.deteteBiohazard(reqEvent);
		if (response.getStatus() == EventStatus.OK) {
			return response.getMessage();
		}
		return null;
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}

}
