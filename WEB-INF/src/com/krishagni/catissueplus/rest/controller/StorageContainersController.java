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

import com.krishagni.catissueplus.core.administrative.events.ContainerOccupiedPositionsEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqContainerOccupiedPositionsEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerPositionDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/storage-containers")
public class StorageContainersController {
	
	@Autowired
	private StorageContainerService storageContainerSvc;
	
	@Autowired
	private HttpServletRequest httpReq;	
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<StorageContainerSummary> getStorageContainers(
			@RequestParam(value = "name", required = false) 
			String name,
			
			@RequestParam(value = "site", required = false)
			String siteName,
			
			@RequestParam(value = "onlyFreeContainers", required = false, defaultValue = "false")
			boolean onlyFreeContainers,
			
			@RequestParam(value = "startAt", required = false, defaultValue = "0")
			int startAt,
			
			@RequestParam(value = "maxRecords", required = false, defaultValue = "100")
			int maxRecords,
			
			@RequestParam(value = "parentContainerId", required = false)
			Long parentContainerId,
			
			@RequestParam(value = "anyLevelContainers", required = false, defaultValue = "false")
			boolean anyLevelContainers
			) {
		
		ReqStorageContainersEvent req = new ReqStorageContainersEvent();
		req.setName(name);
		req.setSiteName(siteName);		
		req.setOnlyFreeContainers(onlyFreeContainers);
		req.setStartAt(startAt);
		req.setMaxRecords(maxRecords);
		req.setSessionDataBean(getSession());
		req.setParentContainerId(parentContainerId);
		req.setAnyLevelContainers(anyLevelContainers);
		
		StorageContainersEvent resp = storageContainerSvc.getStorageContainers(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getContainers();
	}

	@RequestMapping(method = RequestMethod.GET, value="{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public StorageContainerDetail getStorageContainer(@PathVariable("id") Long containerId) {
		ReqStorageContainerEvent req = new ReqStorageContainerEvent();
		req.setContainerId(containerId);
		req.setSessionDataBean(getSession());
		
		StorageContainerEvent resp = storageContainerSvc.getStorageContainer(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getContainer();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="{id}/occupied-positions")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<StorageContainerPositionDetail> getStorageContainerOccupiedPositions(@PathVariable("id") Long containerId) {
		ReqContainerOccupiedPositionsEvent req = new ReqContainerOccupiedPositionsEvent();
		req.setContainerId(containerId);
		req.setSessionDataBean(getSession());
		
		ContainerOccupiedPositionsEvent resp = storageContainerSvc.getOccupiedPositions(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getOccupiedPositions();
	}
		
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public StorageContainerDetail createStorageContainer(@RequestBody StorageContainerDetail detail) {
		CreateStorageContainerEvent req = new CreateStorageContainerEvent();
		req.setContainer(detail);
		req.setSessionDataBean(getSession());
		
		StorageContainerCreatedEvent resp = storageContainerSvc.createStorageContainer(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getContainer();
	}

	@RequestMapping(method = RequestMethod.PUT, value="{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public StorageContainerDetail updateStorageContainer(
			@PathVariable("id") 
			Long containerId,
			
			@RequestBody 
			StorageContainerDetail detail) {
		
		detail.setId(containerId);
		
		UpdateStorageContainerEvent req = new UpdateStorageContainerEvent();
		req.setContainer(detail);
		req.setSessionDataBean(getSession());
		
		StorageContainerUpdatedEvent resp = storageContainerSvc.updateStorageContainer(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getContainer();
	}
	
	private SessionDataBean getSession() {
		return (SessionDataBean) httpReq.getSession().getAttribute(Constants.SESSION_DATA);
	}	
}
