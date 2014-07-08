
package com.krishagni.catissueplus.rest.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.krishagni.catissueplus.core.audit.events.AuditReportCreatedEvent;
import com.krishagni.catissueplus.core.audit.events.AuditReportDetail;
import com.krishagni.catissueplus.core.audit.events.CreateAuditReportEvent;
import com.krishagni.catissueplus.core.audit.events.GetObjectNameEvent;
import com.krishagni.catissueplus.core.audit.events.GetOperationEvent;
import com.krishagni.catissueplus.core.audit.events.GetUsersInfoEvent;
import com.krishagni.catissueplus.core.audit.events.UserInfo;
import com.krishagni.catissueplus.core.audit.services.AuditReportService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.ApplicationProperties;

@Controller
@RequestMapping("/auditreport")
@ResponseStatus(HttpStatus.OK)
public class AuditReportController {

	private final String DATE_FORMAT = ApplicationProperties.getValue("date.pattern");

	@Autowired
	private AuditReportService auditReportService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@GET
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@Produces("text/csv")
	public HttpServletResponse downloadAuditReport(
			@RequestParam(value = "json", required = false, defaultValue = "") String json, HttpServletResponse response)
			throws IOException {

		ServletOutputStream out;

		try {
			Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();

			AuditReportDetail auditReportDetail = gson.fromJson(json, AuditReportDetail.class);

			CreateAuditReportEvent auditReportEvent = new CreateAuditReportEvent();
			auditReportEvent.setSessionDataBean((SessionDataBean) httpServletRequest.getSession().getAttribute(
					Constants.SESSION_DATA));
			auditReportEvent.setAuditReportDetail(auditReportDetail);
			AuditReportCreatedEvent createdEvent = auditReportService.getAuditReport(auditReportEvent);

			if (createdEvent.getStatus().equals(EventStatus.OK)) {
				String reportData = createdEvent.getReportData();
				response.setHeader("Content-Disposition", "attachment; filename=audit_report.csv");
				response.setContentType("application/download");
				response.setContentLength(reportData.length());
				byte[] reportArray = String.valueOf(reportData).getBytes();

				out = response.getOutputStream();
				out.write(reportArray);
				out.flush();
				out.close();
			}
			else {

				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
		catch (Exception exception) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		return null;

	}

	//this method get User Details and send to UI
	@RequestMapping(method = RequestMethod.POST, value = "/users")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<UserInfo> getUsersInfo() {

		GetUsersInfoEvent event = auditReportService.getUserDetails();
		return event.getUsersInfo();
	}

	//this method get ObjectTypes and send to UI
	@RequestMapping(method = RequestMethod.POST, value = "/objects")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, String> getObjectTypes() {

		GetObjectNameEvent event = auditReportService.getObjectTypes();
		if (event.getStatus().equals(EventStatus.OK)) {
			return event.getObjectNameMap();
		}
		else {
			return null;
		}
	}

	//this method get Event types and send to UI
	@RequestMapping(method = RequestMethod.POST, value = "/events")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, String> getOperations() {

		GetOperationEvent eventTypeDetailsEvent = auditReportService.getEventsTypes();
		if (eventTypeDetailsEvent.getStatus().equals(EventStatus.OK)) {
			return eventTypeDetailsEvent.getOperationMap();
		}
		else
			return null;
	}

}
