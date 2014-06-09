		
package com.krishagni.catissueplus.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.privileges.events.CreateRoleEvent;
import com.krishagni.catissueplus.core.privileges.events.RoleCreatedEvent;
import com.krishagni.catissueplus.core.privileges.events.RoleDetails;
import com.krishagni.catissueplus.core.privileges.events.RoleUpdatedEvent;
import com.krishagni.catissueplus.core.privileges.events.UpdateRoleEvent;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;
import com.krishagni.catissueplus.core.privileges.services.RoleService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/roles")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PrivilegeService privService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public RoleDetails createRole(@RequestBody RoleDetails roleDetails) {
		CreateRoleEvent event = new CreateRoleEvent();
		event.setSessionDataBean(getSession());
		event.setRoleDetails(roleDetails);
		//privService.getCpList(115l, PrivilegeType.USER_PROVISIONING.value());		
		RoleCreatedEvent resp = roleService.createRole(event);
		if (resp.getStatus() == EventStatus.OK) {
			
			return resp.getRoleDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{roleId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public RoleDetails updateRole(@PathVariable Long roleId, @RequestBody RoleDetails roleDetails) {
		UpdateRoleEvent event = new UpdateRoleEvent();
		event.setRoleDetails(roleDetails);
		roleDetails.setId(roleId);
		RoleUpdatedEvent resp = roleService.updateRole(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getRoleDetails();
		}
		return null;
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
