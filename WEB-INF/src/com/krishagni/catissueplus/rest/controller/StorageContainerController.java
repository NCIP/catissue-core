
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

import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.GetAllStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.GetStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetails;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerGotEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerPatchDetails;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/storage-containers")
public class StorageContainerController {

	private static final String PATCH_CONTAINER = "patch container";

	@Autowired
	private StorageContainerService storageContainerSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public StorageContainerDetails createStorageContainer(@RequestBody StorageContainerDetails details) {
		CreateStorageContainerEvent event = new CreateStorageContainerEvent(details);
		event.setSessionDataBean(getSession());
		StorageContainerCreatedEvent resp = storageContainerSvc.createStorageContainer(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getStorageContainerDetails();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public StorageContainerDetails updateStorageContainer(@PathVariable Long id,
			@RequestBody StorageContainerDetails details) {
		UpdateStorageContainerEvent event = new UpdateStorageContainerEvent(details, id);
		event.setSessionDataBean(getSession());

		StorageContainerUpdatedEvent resp = storageContainerSvc.updateStorageContainer(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getStorageContainerDetails();
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public StorageContainerDetails patchStorageContainer(@PathVariable Long id, @RequestBody Map<String, Object> values) {
		PatchStorageContainerEvent event = new PatchStorageContainerEvent();
		event.setStorageContainerId(id);
		event.setSessionDataBean(getSession());

		StorageContainerPatchDetails details = new StorageContainerPatchDetails();
		try {
			BeanUtils.populate(details, values);
		}
		catch (Exception e) {
			reportError(StorageContainerErrorCode.BAD_REQUEST, PATCH_CONTAINER);
		}
		details.setModifiedAttributes(new ArrayList<String>(values.keySet()));
		event.setStorageContainerDetails(details);

		StorageContainerUpdatedEvent resp = storageContainerSvc.patchStorageContainer(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getStorageContainerDetails();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String disableStorageContainer(@PathVariable Long id) {
		DisableStorageContainerEvent event = new DisableStorageContainerEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());
		StorageContainerDisabledEvent resp = storageContainerSvc.disableStorageContainer(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getMessage();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/name={name}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String disableStorageContainer(@PathVariable String name) {
		DisableStorageContainerEvent event = new DisableStorageContainerEvent();
		event.setName(name);
		event.setSessionDataBean(getSession());
		StorageContainerDisabledEvent resp = storageContainerSvc.disableStorageContainer(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getMessage();
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<StorageContainerSummary> getStorageContainers(
			@RequestParam(value = "maxResults", required = false, defaultValue = "1000") String maxResults) {
		ReqAllStorageContainersEvent event = new ReqAllStorageContainersEvent();
		event.setMaxResults(Integer.parseInt(maxResults));
		GetAllStorageContainersEvent resp = storageContainerSvc.getStorageContainers(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getSummary();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public StorageContainerDetails getStorageContainerById(@PathVariable Long id) {
		GetStorageContainerEvent event = new GetStorageContainerEvent();
		event.setId(id);
		StorageContainerGotEvent resp = storageContainerSvc.getStorageContainer(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getStorageContainerDetails();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/name={name}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public StorageContainerDetails getStorageContainerById(@PathVariable String name) {
		GetStorageContainerEvent event = new GetStorageContainerEvent();
		event.setName(name);
		StorageContainerGotEvent resp = storageContainerSvc.getStorageContainer(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getStorageContainerDetails();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
