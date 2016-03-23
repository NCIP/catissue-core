
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

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

import com.krishagni.catissueplus.core.administrative.events.ContainerTypeDetail;
import com.krishagni.catissueplus.core.administrative.events.ContainerTypeSummary;
import com.krishagni.catissueplus.core.administrative.repository.ContainerTypeListCriteria;
import com.krishagni.catissueplus.core.administrative.services.ContainerTypeService;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/container-types")
public class ContainerTypesController {

	@Autowired
	private ContainerTypeService containerTypeSvc;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ContainerTypeSummary> getContainerTypes(
		@RequestParam(value="name", required = false)
		String name,
		
		@RequestParam(value="canHold", required = false)
		String canHold,
			
		@RequestParam(value = "exactMatch", required= false, defaultValue = "false")
		boolean exactMatch,
			
		@RequestParam(value = "startAt", required = false, defaultValue = "0")
		int startAt,
			
		@RequestParam(value = "maxResults", required = false, defaultValue = "100")
		int maxResults) {

		ContainerTypeListCriteria crit = new ContainerTypeListCriteria()
				.query(name)
				.canHold(canHold)
				.exactMatch(exactMatch)
				.startAt(startAt)
				.maxResults(maxResults);
		
		RequestEvent<ContainerTypeListCriteria> req = new RequestEvent<ContainerTypeListCriteria>(crit);
		ResponseEvent<List<ContainerTypeSummary>> resp = containerTypeSvc.getContainerTypes(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ContainerTypeDetail getContainerType(@PathVariable("id") Long typeId) {
		return getContainerType(new EntityQueryCriteria(typeId));
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/byname")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ContainerTypeDetail getContainerType(@RequestParam(value = "name", required = true) String name) {
		return getContainerType(new EntityQueryCriteria(name));
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ContainerTypeDetail createContainerType(@RequestBody ContainerTypeDetail detail) {
		RequestEvent<ContainerTypeDetail> req = new RequestEvent<ContainerTypeDetail>(detail);
		ResponseEvent<ContainerTypeDetail> resp = containerTypeSvc.createContainerType(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ContainerTypeDetail updateContainerType(
		@PathVariable("id")
		Long id,
			
		@RequestBody
		ContainerTypeDetail detail) {

		detail.setId(id);
		
		RequestEvent<ContainerTypeDetail> req = new RequestEvent<ContainerTypeDetail>(detail);
		ResponseEvent<ContainerTypeDetail> resp = containerTypeSvc.updateContainerType(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value= "/{id}/dependent-entities")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DependentEntityDetail> getDependentEntities(@PathVariable Long id) {
		RequestEvent<Long> req = new RequestEvent<Long>(id);
		ResponseEvent<List<DependentEntityDetail>> resp = containerTypeSvc.getDependentEntities(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ContainerTypeDetail deleteContainerType(@PathVariable Long id) {
		RequestEvent<Long> req = new RequestEvent<Long>(id);
		ResponseEvent<ContainerTypeDetail> resp = containerTypeSvc.deleteContainerType(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	private ContainerTypeDetail getContainerType(EntityQueryCriteria crit) {
		RequestEvent<EntityQueryCriteria> req = new RequestEvent<EntityQueryCriteria>(crit);
		ResponseEvent<ContainerTypeDetail> resp = containerTypeSvc.getContainerType(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}	

}
