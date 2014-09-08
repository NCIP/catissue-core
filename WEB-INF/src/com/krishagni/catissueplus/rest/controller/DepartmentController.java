
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

import com.krishagni.catissueplus.core.administrative.events.AllDepartmentsEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDetails;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentGotEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.GetDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.services.DepartmentService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentSvc;

	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DepartmentDetails> getAllDepatments(
			@RequestParam(value = "maxResults", required = false, defaultValue = "1000") String maxResults) {
		ReqAllDepartmentEvent req = new ReqAllDepartmentEvent();
		req.setMaxResults(Integer.parseInt(maxResults));
		AllDepartmentsEvent resp = departmentSvc.getAllDepartments(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDepartments();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DepartmentDetails createDepartment(@RequestBody DepartmentDetails departmentDetails) {
		CreateDepartmentEvent event = new CreateDepartmentEvent();
		event.setSessionDataBean(getSession());
		event.setDepartmentDetails(departmentDetails);
		DepartmentCreatedEvent resp = departmentSvc.createDepartment(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDepartmentDetails();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{departmentId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public DepartmentDetails updateDepartment(@PathVariable Long departmentId,
			@RequestBody DepartmentDetails departmentDetails) {
		UpdateDepartmentEvent event = new UpdateDepartmentEvent();
		event.setDepartmentDetails(departmentDetails);
		departmentDetails.setId(departmentId);
		DepartmentUpdatedEvent resp = departmentSvc.updateDepartment(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDepartmentDetails();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public String disableDepartment(@PathVariable Long id) {
		DisableDepartmentEvent event = new DisableDepartmentEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());
		DepartmentDisabledEvent resp = departmentSvc.deleteDepartment(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/name={name}")
	@ResponseStatus(HttpStatus.OK)
	public String disableDepartment(@PathVariable String name) {
		DisableDepartmentEvent event = new DisableDepartmentEvent();
		event.setName(name);
		event.setSessionDataBean(getSession());
		DepartmentDisabledEvent resp = departmentSvc.deleteDepartment(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public String getDepartment(@PathVariable Long id) {
		GetDepartmentEvent event = new GetDepartmentEvent();
		event.setId(id);
		DepartmentGotEvent resp = departmentSvc.getDepartment(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/name={name}")
	@ResponseStatus(HttpStatus.OK)
	public String getDepartment(@PathVariable String name) {
		GetDepartmentEvent event = new GetDepartmentEvent();
		event.setName(name);
		DepartmentGotEvent resp = departmentSvc.getDepartment(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
