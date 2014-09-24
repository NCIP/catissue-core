package com.krishagni.catissueplus.rest.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.krishagni.catissueplus.core.de.domain.SelectField;
import com.krishagni.catissueplus.core.de.events.*;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.krishagni.catissueplus.core.common.events.EventStatus;
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
	@ResponseBody
	public Map<String, Object> getSavedQueries(
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "max", required = false, defaultValue = "25") int max,
			@RequestParam(value = "countReq", required = false, defaultValue = "false") boolean countReq,
			@RequestParam(value = "searchString", required = false, defaultValue = "") String searchString) {
		ReqSavedQueriesSummaryEvent req = new ReqSavedQueriesSummaryEvent();
		req.setStartAt(start);
		req.setMaxRecords(max);
		req.setCountReq(countReq);
		req.setSearchString(searchString);
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
	
	@RequestMapping(method = RequestMethod.POST, value="/definition-file", produces="text/plain")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody		
	public String importQuery(@PathVariable("file") MultipartFile file) 
	throws IOException {
		String json = new String(file.getBytes());
		SavedQueryDetail detail = new Gson().fromJson(json, SavedQueryDetail.class);
		return new Gson().toJson(saveQuery(detail));
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SavedQueryDetail saveQuery(@RequestBody SavedQueryDetail detail) {
		curateSavedQueryDetail(detail);
		
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
		curateSavedQueryDetail(detail);
		
		UpdateQueryEvent req = new UpdateQueryEvent();
		req.setSavedQueryDetail(detail);
		req.setSessionDataBean(getSession());
		
		QueryUpdatedEvent resp = querySvc.updateQuery(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getSavedQueryDetail();
		}
		
		return null;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Long deleteQuery(@PathVariable("id") Long id) {
		DeleteQueryEvent req = new DeleteQueryEvent();
		req.setQueryId(id);
		req.setSessionDataBean(getSession());

		QueryDeletedEvent resp = querySvc.deleteQuery(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getQueryId();
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
	
	
	private void curateSavedQueryDetail(SavedQueryDetail detail) {
		Object[] selectList = detail.getSelectList();
		if (selectList == null) {
			return;
		}
		
		for (int i = 0; i < selectList.length; ++i) {
			if (!(selectList[i] instanceof Map)) {
				continue;
			}
			
			try {
				selectList[i] = getSelectField(getJson((Map<String, Object>)selectList[i]));
			} catch (Exception e) {
				throw new RuntimeException("Bad select field", e);
			}
		}
	}
	
	private String getJson(Map<String, Object> props) 
	throws Exception {
		return new ObjectMapper().writeValueAsString(props);
	}
	
	private SelectField getSelectField(String json) 
	throws Exception {
		return new ObjectMapper().readValue(json, SelectField.class);
	}
}
