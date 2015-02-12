
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

import com.krishagni.catissueplus.core.administrative.events.ListSiteCriteria;
import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
import com.krishagni.catissueplus.core.administrative.events.SiteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.services.SiteService;
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

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SiteDetail> getSites(
			@RequestParam(value = "maxResults", required = false, defaultValue = "1000") 
			int maxResults) {
		
		ListSiteCriteria crit = new ListSiteCriteria().maxResults(maxResults);		
		
		RequestEvent<ListSiteCriteria> req = new RequestEvent<ListSiteCriteria>(getSession(), crit);
		ResponseEvent<List<SiteDetail>> resp = siteService.getSites(req);
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

	@RequestMapping(method = RequestMethod.PUT, value = "/{name}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SiteDetail updateSite(@PathVariable String name, @RequestBody SiteDetail siteDetail) {
		siteDetail.setName(name);
		
		RequestEvent<SiteDetail> req = new RequestEvent<SiteDetail>(getSession(), siteDetail);
		ResponseEvent<SiteDetail> resp = siteService.updateSite(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SiteDetail deleteSite(@PathVariable Long id) {
		SiteQueryCriteria crit = new SiteQueryCriteria();
		crit.setId(id);
		
		RequestEvent<SiteQueryCriteria> req = new RequestEvent<SiteQueryCriteria>(getSession(), crit);
		ResponseEvent<SiteDetail> resp = siteService.deleteSite(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
