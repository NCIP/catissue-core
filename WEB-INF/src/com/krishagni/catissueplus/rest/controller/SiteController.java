
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
import com.krishagni.catissueplus.core.administrative.events.AllSitesEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.GetSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteDetails;
import com.krishagni.catissueplus.core.administrative.events.SiteGotEvent;
import com.krishagni.catissueplus.core.administrative.events.SitePatchDetails;
import com.krishagni.catissueplus.core.administrative.events.SiteUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateSiteEvent;
import com.krishagni.catissueplus.core.administrative.services.SiteService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/sites")
public class SiteController {

	@Autowired
	private SiteService siteService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	private static String PATCH_SITE = "patch site";

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SiteDetails getSite(@PathVariable Long id) {
		GetSiteEvent reqEvent = new GetSiteEvent();
		reqEvent.setId(id);
		SiteGotEvent resp = siteService.getSite(reqEvent);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/name={name}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SiteDetails getSite(@PathVariable String name) {
		GetSiteEvent reqEvent = new GetSiteEvent();
		reqEvent.setName(name);
		SiteGotEvent resp = siteService.getSite(reqEvent);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	//@CacheControl(policy = {CachePolicy.NO_STORE, CachePolicy.NO_CACHE})
	@ResponseBody
	public List<SiteDetails> getAllSites(
			@RequestParam(value = "maxResults", required = false, defaultValue = "1000") String maxResults) {
		ReqAllSiteEvent req = new ReqAllSiteEvent();
		req.setMaxResults(Integer.parseInt(maxResults));
		AllSitesEvent resp = siteService.getAllSites(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getSites();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SiteDetails createSite(@RequestBody SiteDetails siteDetails) {
		CreateSiteEvent event = new CreateSiteEvent();
		event.setSiteDetails(siteDetails);
		event.setSessionDataBean(getSession());
		SiteCreatedEvent resp = siteService.createSite(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getSiteDetails();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SiteDetails updateSite(@PathVariable Long id, @RequestBody SiteDetails siteDetails) {
		UpdateSiteEvent event = new UpdateSiteEvent(siteDetails, id);
		event.setSessionDataBean(getSession());
		SiteUpdatedEvent resp = siteService.updateSite(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getSiteDetails();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/name={name}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SiteDetails updateSite(@PathVariable String name, @RequestBody SiteDetails siteDetails) {
		UpdateSiteEvent event = new UpdateSiteEvent(siteDetails, name);
		event.setSessionDataBean(getSession());
		SiteUpdatedEvent resp = siteService.updateSite(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getSiteDetails();
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SiteDetails PatchSite(@PathVariable Long id, @RequestBody Map<String, Object> values) {
		PatchSiteEvent event = new PatchSiteEvent();
		event.setSiteId(id);
		event.setSessionDataBean(getSession());

		SitePatchDetails details = new SitePatchDetails();
		try {
			BeanUtils.populate(details, values);
		}
		catch (Exception e) {
			reportError(SiteErrorCode.BAD_REQUEST, PATCH_SITE);
		}
		details.setModifiedAttributes(new ArrayList<String>(values.keySet()));
		event.setDetails(details);

		SiteUpdatedEvent resp = siteService.patchSite(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getSiteDetails();
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/name={name}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SiteDetails PatchSite(@PathVariable String name, @RequestBody Map<String, Object> values) {
		PatchSiteEvent event = new PatchSiteEvent();
		event.setSiteName(name);
		event.setSessionDataBean(getSession());

		SitePatchDetails details = new SitePatchDetails();
		try {
			BeanUtils.populate(details, values);
		}
		catch (Exception e) {
			reportError(SiteErrorCode.BAD_REQUEST, PATCH_SITE);
		}
		details.setModifiedAttributes(new ArrayList<String>(values.keySet()));
		event.setDetails(details);

		SiteUpdatedEvent resp = siteService.patchSite(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getSiteDetails();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String deleteSite(@PathVariable Long id) {
		DeleteSiteEvent reqEvent = new DeleteSiteEvent();
		reqEvent.setId(id);
		reqEvent.setSessionDataBean(getSession());
		SiteDeletedEvent resp = siteService.deleteSite(reqEvent);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/name={name}")
	@ResponseStatus(HttpStatus.OK)
	public String deleteSite(@PathVariable String name) {
		DeleteSiteEvent reqEvent = new DeleteSiteEvent();
		reqEvent.setName(name);
		reqEvent.setSessionDataBean(getSession());
		SiteDeletedEvent resp = siteService.deleteSite(reqEvent);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
