
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

import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchSiteEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.SiteDetails;
import com.krishagni.catissueplus.core.administrative.events.SitePatchDetails;
import com.krishagni.catissueplus.core.administrative.events.SiteUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateSiteEvent;
import com.krishagni.catissueplus.core.administrative.services.SiteService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

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

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SiteDetails createSite(@RequestBody SiteDetails siteDetails) {
		CreateSiteEvent event = new CreateSiteEvent();
		event.setSiteDetails(siteDetails);
		event.setSessionDataBean(getSession());
		SiteCreatedEvent resp = siteService.createSite(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getSiteDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SiteDetails updateSite(@PathVariable Long id, @RequestBody SiteDetails siteDetails) {
		UpdateSiteEvent event = new UpdateSiteEvent(siteDetails, id);
		event.setSessionDataBean(getSession());
		SiteUpdatedEvent resp = siteService.updateSite(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getSiteDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/name={name}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SiteDetails updateSite(@PathVariable String name, @RequestBody SiteDetails siteDetails) {
		UpdateSiteEvent event = new UpdateSiteEvent(siteDetails, name);
		event.setSessionDataBean(getSession());
		SiteUpdatedEvent resp = siteService.updateSite(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getSiteDetails();
		}
		return null;
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

		SiteUpdatedEvent response = siteService.patchSite(event);
		if (response.getStatus() == EventStatus.OK) {
			return response.getSiteDetails();
		}
		return null;
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

		SiteUpdatedEvent response = siteService.patchSite(event);
		if (response.getStatus() == EventStatus.OK) {
			return response.getSiteDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String deleteSite(@PathVariable Long id) {
		DeleteSiteEvent reqEvent = new DeleteSiteEvent();
		reqEvent.setId(id);
		reqEvent.setSessionDataBean(getSession());
		SiteDeletedEvent response = siteService.deleteSite(reqEvent);
		if (response.getStatus() == EventStatus.OK) {
			return response.getMessage();
		}
		return null;
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
