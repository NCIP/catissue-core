package com.krishagni.catissueplus.rest.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.java.common.CacheControl;
import com.java.common.CachePolicy;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogSummary;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogsEvent;
import com.krishagni.catissueplus.core.de.events.QueryDefEvent;
import com.krishagni.catissueplus.core.de.events.QuerySavedEvent;
import com.krishagni.catissueplus.core.de.events.QueryUpdatedEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryAuditLogsEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryDefEvent;
import com.krishagni.catissueplus.core.de.events.ReqSavedQueriesSummaryEvent;
import com.krishagni.catissueplus.core.de.events.ReqSavedQueryDetailEvent;
import com.krishagni.catissueplus.core.de.events.SaveQueryEvent;
import com.krishagni.catissueplus.core.de.events.SavedQueriesSummaryEvent;
import com.krishagni.catissueplus.core.de.events.SavedQueryDetail;
import com.krishagni.catissueplus.core.de.events.SavedQueryDetailEvent;
import com.krishagni.catissueplus.core.de.events.UpdateQueryEvent;
import com.krishagni.catissueplus.core.de.services.QueryService;

import edu.common.dynamicextensions.nutility.IoUtil;
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
	@CacheControl(policy = {CachePolicy.NO_STORE, CachePolicy.NO_CACHE})
	@ResponseBody
	public Map<String, Object> getSavedQueries(
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "max", required = false, defaultValue = "25") int max,
			@RequestParam(value = "countReq", required = false, defaultValue = "false") boolean countReq) {
		ReqSavedQueriesSummaryEvent req = new ReqSavedQueriesSummaryEvent();
		req.setStartAt(start);
		req.setMaxRecords(max);
		req.setCountReq(countReq);
		req.setSessionDataBean(getSession());
		
		SavedQueriesSummaryEvent resp = querySvc.getSavedQueries(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		if (resp.getCount() != null) {
			result.put("count", resp.getCount());
		}
		result.put("queries", resp.getSavedQueries());
		return result;		
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@CacheControl(policy = {CachePolicy.NO_STORE, CachePolicy.NO_CACHE})
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

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/definition-file")
	@ResponseStatus(HttpStatus.OK)	
	@CacheControl(policy = {CachePolicy.NO_STORE, CachePolicy.NO_CACHE})
	public void getQueryDefFile(@PathVariable("id") Long queryId, HttpServletResponse response) {
		ReqQueryDefEvent req = new ReqQueryDefEvent();
		req.setQueryId(queryId);
		req.setSessionDataBean(getSession());
		
		QueryDefEvent resp = querySvc.getQueryDef(req);
		if (resp.getStatus() != EventStatus.OK) {
			return;
		}
		
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment;filename=QueryDef_" + queryId + ".json");
			
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(resp.getQueryDef().getBytes());
			IoUtil.copy(in, response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException("Error sending file", e);
		} finally {
			IoUtil.close(in);
		}				
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/definition-file")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public SavedQueryDetail importQuery(@PathVariable("file") MultipartFile file) 
	throws IOException {
		String json = new String(file.getBytes());
		SavedQueryDetail detail = new Gson().fromJson(json, SavedQueryDetail.class);
		return saveQuery(detail);
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
	@CacheControl(policy = {CachePolicy.NO_STORE, CachePolicy.NO_CACHE})
	@ResponseBody
	public List<QueryAuditLogSummary> getQueryAuditLogs(
			@PathVariable("id") Long savedQueryId,
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "max", required = false, defaultValue = "25") int max) {
		ReqQueryAuditLogsEvent req = new ReqQueryAuditLogsEvent();
		req.setStartAt(start);
		req.setMaxRecords(max);
		req.setSavedQueryId(savedQueryId);
		req.setSessionDataBean(getSession());
		
		QueryAuditLogsEvent resp = querySvc.getAuditLogs(req);
		if(resp.getStatus() != EventStatus.OK) {
			return null;			
		}
		
		return resp.getAuditLogs();
	}
	
	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(
				Constants.SESSION_DATA);
	}
}
