package com.krishagni.catissueplus.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogDetail;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogEvent;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogsEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryAuditLogEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryAuditLogsEvent;
import com.krishagni.catissueplus.core.de.events.ReqQueryAuditLogsEvent.Type;
import com.krishagni.catissueplus.core.de.services.QueryService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/query-audit-logs")
public class QueryAuditLogsController {

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private QueryService querySvc;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public Map<String, Object> getAuditLogs(
			@RequestParam(value = "type", required = false, defaultValue = "LAST_24") String type,
			@RequestParam(value = "queryId", required = false, defaultValue = "-1") Long queryId,
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "max", required = false, defaultValue = "25") int max,
			@RequestParam(value = "countReq", required = false, defaultValue = "false") boolean countReq) {
		
		ReqQueryAuditLogsEvent req = new ReqQueryAuditLogsEvent();
		req.setType(Type.valueOf(type));
		req.setSessionDataBean(getSession());
		req.setSavedQueryId(queryId);
		req.setStartAt(start < 0 ? 0 : start);
		req.setMaxRecords(max < 0 ? 25 : max);
		req.setCountReq(countReq);
		
		QueryAuditLogsEvent resp = querySvc.getAuditLogs(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		if (resp.getCount() != null) {
			result.put("count", resp.getCount());
		}
		
		result.put("auditLogs", resp.getAuditLogs());
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value="{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public QueryAuditLogDetail getAuditLog(@PathVariable Long id) {

		ReqQueryAuditLogEvent req = new ReqQueryAuditLogEvent();
		req.setAuditLogId(id);
		req.setSessionDataBean(getSession());

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
