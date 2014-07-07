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

import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenListEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ListSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ListSpecimensUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqListSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenListDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ShareSpecimenListEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetails;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListSharedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListSummary;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenSummary;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateSpecimenListEvent;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenListService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/specimen-lists")
public class SpecimenListsController {
	
	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private SpecimenListService specimenListSvc;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenListSummary> getSpecimenListsForUser(){
		RequestEvent req = new RequestEvent();
		req.setSessionDataBean(getSession());
		SpecimenListsEvent resp = specimenListSvc.getUserSpecimenLists(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getLists();
		}
		
		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{listId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenListDetails getSpecimenList(@PathVariable Long listId) {
		ReqSpecimenListDetailEvent req = new ReqSpecimenListDetailEvent();
		req.setListId(listId);
		req.setSessionDataBean(getSession());
		
		SpecimenListDetailEvent resp = specimenListSvc.getSpecimenList(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getDetails();
		}
		
		return null;
	}
		
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenListDetails createSpecimenList(@RequestBody SpecimenListDetails details) {
		CreateSpecimenListEvent req = new CreateSpecimenListEvent();
		req.setListDetails(details);
		req.setSessionDataBean(getSession());
		
		SpecimenListCreatedEvent resp = specimenListSvc.createSpecimenList(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getListDetails();
		}
		
		return null;
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{listId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenListDetails updateSpecimenList(@PathVariable Long listId, @RequestBody SpecimenListDetails details) {
		UpdateSpecimenListEvent req = new UpdateSpecimenListEvent();
		
		details.setId(listId);
		req.setListDetails(details);
		req.setSessionDataBean(getSession());
		
		SpecimenListUpdatedEvent resp = specimenListSvc.updateSpecimenList(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getListDetails();
		}
		
		return null;
	}
	
			
	@RequestMapping(method = RequestMethod.GET, value="/{listId}/specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenSummary> getListSpecimens(@PathVariable("listId") Long listId) {
		ReqListSpecimensEvent req = new ReqListSpecimensEvent(listId);
		req.setSessionDataBean(getSession());
		
		ListSpecimensEvent resp = specimenListSvc.getListSpecimens(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getSpecimens();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{listId}/specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenSummary> updateListSpecimens(
			@PathVariable("listId") Long listId,
			@RequestParam(value = "operation", required = false, defaultValue = "UPDATE") String operation,
			@RequestBody List<String> specimenLabels) {
		
		UpdateListSpecimensEvent req = new UpdateListSpecimensEvent();
		req.setListId(listId);
		req.setSpecimens(specimenLabels);
		req.setOp(com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensEvent.Operation.valueOf(operation));
		req.setSessionDataBean(getSession());

		ListSpecimensUpdatedEvent resp = specimenListSvc.updateListSpecimens(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getSpecimens();
		}
		
		return null;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{listId}/users")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<UserSummary> shareSpecimenList(
			@PathVariable("listId") Long listId,
			@RequestParam(value = "operation", required = false, defaultValue = "UPDATE") String operation,
			@RequestBody List<Long> userIds) {
		ShareSpecimenListEvent req = new ShareSpecimenListEvent();
		req.setListId(listId);
		req.setOp(com.krishagni.catissueplus.core.biospecimen.events.ShareSpecimenListEvent.Operation.valueOf(operation));
		req.setUserIds(userIds);
		req.setSessionDataBean(getSession());
		
		SpecimenListSharedEvent resp = specimenListSvc.shareSpecimenList(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getUsers();
		}
		
		return null;
	}
		
	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
