package com.krishagni.catissueplus.rest.controller;

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

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.ListQueryAuditLogsCriteria;
import com.krishagni.catissueplus.core.de.events.ListQueryAuditLogsCriteria.Type;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogDetail;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogsList;
import com.krishagni.catissueplus.core.de.services.QueryService;

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
	public QueryAuditLogsList getAuditLogs(
			@RequestParam(value = "type", required = false, defaultValue = "LAST_24") 
			String type,
			
			@RequestParam(value = "queryId", required = false, defaultValue = "-1") 
			Long queryId,
			
			@RequestParam(value = "start", required = false, defaultValue = "0") 
			int start,
			
			@RequestParam(value = "max", required = false, defaultValue = "25") 
			int max,
			
			@RequestParam(value = "countReq", required = false, defaultValue = "false") 
			boolean countReq) {
		
		ListQueryAuditLogsCriteria crit = new ListQueryAuditLogsCriteria()
			.type(Type.valueOf(type))
			.savedQueryId(queryId)
			.startAt(start)
			.maxResults(max)
			.countReq(countReq);
		
		return response(querySvc.getAuditLogs(getRequest(crit)));
	}

	@RequestMapping(method = RequestMethod.GET, value="{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public QueryAuditLogDetail getAuditLog(@PathVariable Long id) {
		return response(querySvc.getAuditLog(getRequest(id)));
	}

	private <T> T response(ResponseEvent<T> resp) {
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);				
	}	
}
