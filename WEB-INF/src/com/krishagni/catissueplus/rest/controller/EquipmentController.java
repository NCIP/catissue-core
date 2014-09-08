
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

import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.events.AllEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.EquipmentCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.EquipmentDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.EquipmentDetails;
import com.krishagni.catissueplus.core.administrative.events.EquipmentPatchDetails;
import com.krishagni.catissueplus.core.administrative.events.EquipmentUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.GetEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.GotEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.services.EquipmentService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/equipments")
public class EquipmentController {

	private static final String PATCH_EQUIPMENT = "patch equipment";

	@Autowired
	private EquipmentService equipmentSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	//@CacheControl(policy = {CachePolicy.NO_STORE, CachePolicy.NO_CACHE})
	@ResponseBody
	public List<EquipmentDetails> getAllEquipments(
			@RequestParam(value = "maxResults", required = false, defaultValue = "1000") String maxResults) {
		ReqAllEquipmentEvent req = new ReqAllEquipmentEvent();
		req.setMaxResults(Integer.parseInt(maxResults));
		AllEquipmentEvent resp = equipmentSvc.getAllEquipments(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getEquipments();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public EquipmentDetails getEquipment(@PathVariable Long id) {
		GetEquipmentEvent event = new GetEquipmentEvent();
		event.setId(id);
		GotEquipmentEvent resp = equipmentSvc.getEquipment(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/displayName={displayName}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public EquipmentDetails getEquipment(@PathVariable String displayName) {
		GetEquipmentEvent event = new GetEquipmentEvent();
		event.setDisplayName(displayName);
		GotEquipmentEvent resp = equipmentSvc.getEquipment(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public EquipmentDetails createEquipment(@RequestBody EquipmentDetails details) {
		CreateEquipmentEvent reqEvent = new CreateEquipmentEvent();
		reqEvent.setDetails(details);
		reqEvent.setSessionDataBean(getSession());
		EquipmentCreatedEvent resp = equipmentSvc.createEquipment(reqEvent);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public EquipmentDetails updateEquipment(@PathVariable Long id, @RequestBody EquipmentDetails details) {
		UpdateEquipmentEvent reqEvent = new UpdateEquipmentEvent();
		reqEvent.setDetails(details);
		reqEvent.setId(id);
		reqEvent.setSessionDataBean(getSession());
		EquipmentUpdatedEvent resp = equipmentSvc.updateEquipment(reqEvent);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public EquipmentDetails patchEquipment(@PathVariable Long id, @RequestBody Map<String, Object> values) {
		PatchEquipmentEvent reqEvent = new PatchEquipmentEvent();
		reqEvent.setId(id);
		reqEvent.setSessionDataBean(getSession());
		EquipmentPatchDetails details = new EquipmentPatchDetails();
		try {
			BeanUtils.populate(details, values);
		}
		catch (Exception e) {
			reportError(SiteErrorCode.BAD_REQUEST, PATCH_EQUIPMENT);
		}
		details.setModifiedAttributes(new ArrayList<String>(values.keySet()));
		reqEvent.setDetails(details);

		EquipmentUpdatedEvent resp = equipmentSvc.patchEquipment(reqEvent);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String deleteEquipment(@PathVariable Long id) {
		DeleteEquipmentEvent reqEvent = new DeleteEquipmentEvent();
		reqEvent.setId(id);
		reqEvent.setSessionDataBean(getSession());
		EquipmentDeletedEvent resp = equipmentSvc.deleteEquipment(reqEvent);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/displayName={displayName}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String deleteEquipment(@PathVariable String displayName) {
		DeleteEquipmentEvent reqEvent = new DeleteEquipmentEvent();
		reqEvent.setDisplayName(displayName);
		reqEvent.setSessionDataBean(getSession());
		EquipmentDeletedEvent resp = equipmentSvc.deleteEquipment(reqEvent);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
