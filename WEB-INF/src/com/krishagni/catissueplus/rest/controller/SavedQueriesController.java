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

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogSummary;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogsEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryAuditLogsEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryAuditLogEvent;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogDetail;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogEvent;
import com.krishagni.catissueplus.core.de.events.SavedQueryDetail;
import com.krishagni.catissueplus.core.de.events.SavedQueryDetailEvent;
import com.krishagni.catissueplus.core.de.events.QuerySavedEvent;
import com.krishagni.catissueplus.core.de.events.QueryUpdatedEvent;
import com.krishagni.catissueplus.core.de.events.ReqSavedQueryDetailEvent;
import com.krishagni.catissueplus.core.de.events.ReqSavedQueriesSummaryEvent;
import com.krishagni.catissueplus.core.de.events.SaveQueryEvent;
import com.krishagni.catissueplus.core.de.events.SavedQueriesSummaryEvent;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;
import com.krishagni.catissueplus.core.de.events.UpdateQueryEvent;
import com.krishagni.catissueplus.core.de.services.QueryService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/saved-queries")
public class SavedQueriesController {

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private QueryService querySvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SavedQuerySummary> getSavedQueries(
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "max", required = false, defaultValue = "25") int max) {
		ReqSavedQueriesSummaryEvent req = new ReqSavedQueriesSummaryEvent();
		req.setStartAt(start);
		req.setMaxRecords(max);
		req.setSessionDataBean(getSession());
		
		SavedQueriesSummaryEvent resp = querySvc.getSavedQueries(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getSavedQueries();
		}
		
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SavedQueryDetail getQueryDetails(@PathVariable("id") Long queryId) {
		ReqSavedQueryDetailEvent req = new ReqSavedQueryDetailEvent();
		req.setQueryId(queryId);
		
		SavedQueryDetailEvent resp = querySvc.getSavedQuery(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getSavedQueryDetail();
		}
		
		return null;
	}

	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SavedQueryDetail saveQuery(@RequestBody SavedQueryDetail detail) {
		SaveQueryEvent req = new SaveQueryEvent();
		req.setSavedQueryDetail(detail);
		req.setSessionDataBean(getSession());
		
		QuerySavedEvent resp = querySvc.saveQuery(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getSavedQueryDetail();
		}
		
		return null;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SavedQueryDetail updateQuery(@RequestBody SavedQueryDetail detail) {
		UpdateQueryEvent req = new UpdateQueryEvent();
		req.setSavedQueryDetail(detail);
		req.setSessionDataBean(getSession());
		
		QueryUpdatedEvent resp = querySvc.updateQuery(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getSavedQueryDetail();
		}
		
		return null;
	}

	@RequestMapping(method = RequestMethod.GET,  value = "/{id}/audit-logs")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<QueryAuditLogSummary> getQueryAuditLogs(
			@PathVariable("id") Long savedQueryId,
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "max", required = false, defaultValue = "25") int max) {
		ReqQueryAuditLogsEvent req = new ReqQueryAuditLogsEvent();
		req.setStartAt(start);
		req.setMaxRecords(max);
		req.setSavedQueryId(savedQueryId);
		
		QueryAuditLogsEvent resp = querySvc.getAuditLogs(req);
		if(resp.getStatus() != EventStatus.OK) {
			return null;			
		}
		
		return resp.getAuditLogs();
	}
	
	@RequestMapping(method = RequestMethod.GET,  value = "/{id}//audit-logs/{auditLogId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public QueryAuditLogDetail getQueryAuditLog(
			@PathVariable("id") Long savedQueryId,
			@PathVariable("auditLogId") Long auditLogId){
		ReqQueryAuditLogEvent req = new ReqQueryAuditLogEvent();
		req.setSavedQueryId(savedQueryId);
		req.setAuditLogId(auditLogId);
		
		QueryAuditLogEvent resp = querySvc.getAuditLog(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getAuditLog();
	}
	
	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(
				Constants.SESSION_DATA);
	}
}
