
package com.krishagni.catissueplus.rest.controller;

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

import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
import com.krishagni.catissueplus.core.administrative.events.SiteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.repository.SiteListCriteria;
import com.krishagni.catissueplus.core.administrative.services.SiteService;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/sites")
public class SitesController {

	@Autowired
	private SiteService siteService;

	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SiteDetail> getSites(
			@RequestParam(value = "name", required= false)
			String name,
			
			@RequestParam(value = "exactMatch", required= false, defaultValue = "false")
			boolean exactMatch,
			
			@RequestParam(value = "startAt", required = false, defaultValue = "0")
			int startAt,
			
			@RequestParam(value = "maxResults", required = false, defaultValue = "100") 
			int maxResults
			) {
		
		SiteListCriteria crit = new SiteListCriteria()
			.query(name)
			.exactMatch(exactMatch)
			.startAt(startAt)
			.maxResults(maxResults);
		
		RequestEvent<SiteListCriteria> req = new RequestEvent<SiteListCriteria>(getSession(), crit);
		ResponseEvent<List<SiteDetail>> resp = siteService.getSites(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SiteDetail getSite(@PathVariable Long id) {
		SiteQueryCriteria crit = new SiteQueryCriteria();
		crit.setId(id);
		
		RequestEvent<SiteQueryCriteria> req = new RequestEvent<SiteQueryCriteria>(getSession(), crit);
		ResponseEvent<SiteDetail> resp = siteService.getSite(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}


	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SiteDetail createSite(@RequestBody SiteDetail siteDetail) {
		RequestEvent<SiteDetail> req = new RequestEvent<SiteDetail>(getSession(), siteDetail);
		ResponseEvent<SiteDetail> resp = siteService.createSite(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SiteDetail updateSite(@PathVariable Long id, @RequestBody SiteDetail siteDetail) {
		siteDetail.setId(id);
		
		RequestEvent<SiteDetail> req = new RequestEvent<SiteDetail>(getSession(), siteDetail);
		ResponseEvent<SiteDetail> resp = siteService.updateSite(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/dependent-entities")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<Map<String, Object>> getSiteDependentEntities(@PathVariable Long id) {
		RequestEvent<Long> req = new RequestEvent<Long>(null, id);
		ResponseEvent<List<Map<String, Object>>> resp = siteService.getSiteDependentEntities(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SiteDetail deleteSite(@PathVariable Long id, 
			@RequestParam(value="close", required=false, defaultValue="false") boolean close) {
		DeleteEntityOp deleteOp = new DeleteEntityOp(id, close);
		RequestEvent<DeleteEntityOp> req = new RequestEvent<DeleteEntityOp>(null, deleteOp);
		ResponseEvent<SiteDetail> resp = siteService.deleteSite(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
