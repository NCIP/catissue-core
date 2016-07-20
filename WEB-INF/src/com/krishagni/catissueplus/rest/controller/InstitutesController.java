package com.krishagni.catissueplus.rest.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.events.InstituteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.repository.InstituteListCriteria;
import com.krishagni.catissueplus.core.administrative.services.InstituteService;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/institutes")
public class InstitutesController {

	@Autowired
	private InstituteService instituteSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<InstituteDetail> getInstitutes(
			@RequestParam(value = "startAt", required = false, defaultValue = "0")
			int startAt,
			
			@RequestParam(value = "maxResults", required = false, defaultValue = "100") 
			int maxResults,
			
			@RequestParam(value = "name", required = false)
			String name,
			
			@RequestParam(value = "exactMatch", required = false, defaultValue = "false")
			boolean exactMatch,

			@RequestParam(value = "includeStats", required = false, defaultValue = "false")
			boolean includeStats) {
		
		InstituteListCriteria crit  = new InstituteListCriteria()
				.query(name)
				.exactMatch(exactMatch)
				.startAt(startAt)
				.maxResults(maxResults)
				.includeStat(includeStats);
		
		RequestEvent<InstituteListCriteria> req = new RequestEvent<InstituteListCriteria>(crit);
		ResponseEvent<List<InstituteDetail>> resp = instituteSvc.getInstitutes(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/count")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Long> getInstitutesCount(
			@RequestParam(value = "name", required = false)
			String name) {

		RequestEvent<InstituteListCriteria> req = new RequestEvent<>(new InstituteListCriteria().query(name));
		ResponseEvent<Long> resp = instituteSvc.getInstitutesCount(req);
		resp.throwErrorIfUnsuccessful();

		return Collections.singletonMap("count", resp.getPayload());
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public InstituteDetail getInstitute(@PathVariable Long id) {
		InstituteQueryCriteria crit = new InstituteQueryCriteria(); 
		crit.setId(id);
		
		RequestEvent<InstituteQueryCriteria> req = new RequestEvent<InstituteQueryCriteria>(crit);		
		ResponseEvent<InstituteDetail> resp = instituteSvc.getInstitute(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/byname")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public InstituteDetail getInstituteByName(@RequestParam(value = "name", required = true) String name) {		
		InstituteQueryCriteria crit = new InstituteQueryCriteria();
		crit.setName(name);

		RequestEvent<InstituteQueryCriteria> req = new RequestEvent<InstituteQueryCriteria>(crit);
		ResponseEvent<InstituteDetail> resp = instituteSvc.getInstitute(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public InstituteDetail createInstitute(@RequestBody InstituteDetail detail) {
		RequestEvent<InstituteDetail> req = new RequestEvent<InstituteDetail>(detail);		
		ResponseEvent<InstituteDetail> resp = instituteSvc.createInstitute(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{instituteId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public InstituteDetail updateInstitute(@PathVariable Long instituteId, @RequestBody InstituteDetail instituteDetail) {
		instituteDetail.setId(instituteId);
		
		RequestEvent<InstituteDetail> req = new RequestEvent<InstituteDetail>(instituteDetail);
		ResponseEvent<InstituteDetail> resp = instituteSvc.updateInstitute(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/dependent-entities")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<DependentEntityDetail> getDependentEntities(@PathVariable Long id) {
		RequestEvent<Long> req = new RequestEvent<Long>(id);
		ResponseEvent<List<DependentEntityDetail>> resp = instituteSvc.getDependentEntities(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public InstituteDetail deleteInstitute(@PathVariable Long id,
			@RequestParam(value="close", required=false, defaultValue="false") boolean close) {
		DeleteEntityOp deleteEntityOp = new DeleteEntityOp(id, close);
		RequestEvent<DeleteEntityOp> req = new RequestEvent<DeleteEntityOp>(deleteEntityOp);
		ResponseEvent<InstituteDetail> resp = instituteSvc.deleteInstitute(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
}