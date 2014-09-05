
package com.krishagni.catissueplus.rest.controller;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.java.common.CacheControl;
import com.java.common.CachePolicy;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.AllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.CloseUserEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableUserEvent;
import com.krishagni.catissueplus.core.administrative.events.ForgotPasswordEvent;
import com.krishagni.catissueplus.core.administrative.events.GetUserEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.events.PasswordForgottenEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordValidatedEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchUserEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdatePasswordEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserClosedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.administrative.events.UserDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.UserPatchDetails;
import com.krishagni.catissueplus.core.administrative.events.UserUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ValidatePasswordEvent;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.auth.domain.factory.PasswordActionType;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.UserSummary;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/users")
public class UserController {

	private static String PATCH_USER = "patch user";

	@Autowired
	private UserService userService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> getAllUsers(
			@RequestParam(value = "start", required = false, defaultValue = "0") int startAt,
			@RequestParam(value = "max", required = false, defaultValue = "100") int maxRecords,
			@RequestParam(value = "countReq", required = false, defaultValue = "false") boolean countReq,
            @RequestParam(value = "searchString", required = false, defaultValue = "") String searchString) {
		
		ReqAllUsersEvent req = new ReqAllUsersEvent();
		req.setStartAt(startAt);
		req.setMaxRecords(maxRecords);
		req.setCountReq(countReq);
		req.setSearchString(searchString);
		
		AllUsersEvent resp = userService.getAllUsers(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		if (resp.getCount() != null) {
			result.put("count", resp.getCount());
		}
		
		result.put("users", resp.getUsers());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public UserDetails getUser(@PathVariable Long id) {	
		GetUserEvent resp = userService.getUser(id);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getUserDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public UserDetails createUser(@RequestBody UserDetails userDetails) {
		UserCreatedEvent resp = userService.createUser(new CreateUserEvent(userDetails));
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getUserDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public UserDetails updateUser(@PathVariable Long id, @RequestBody UserDetails userDetails) {
		UserUpdatedEvent resp = userService.updateUser(new UpdateUserEvent(userDetails, id));
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getUserDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public UserDetails patchUser(@PathVariable Long id, @RequestBody Map<String, Object> values) {
		PatchUserEvent event = new PatchUserEvent();
		event.setUserId(id);
		event.setSessionDataBean(getSession());

		UserPatchDetails details = new UserPatchDetails();
		try {
			BeanUtils.populate(details, values);
		}
		catch (Exception e) {
			reportError(UserErrorCode.BAD_REQUEST, PATCH_USER);
		}
		details.setModifiedAttributes(new ArrayList<String>(values.keySet()));
		event.setUserDetails(details);

		UserUpdatedEvent response = userService.patchUser(event);
		if (response.getStatus() == EventStatus.OK) {
			return response.getUserDetails();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "close/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String closeUser(@PathVariable Long id) {
		CloseUserEvent event = new CloseUserEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());
		UserClosedEvent resp = userService.closeUser(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getMessage();
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public String disableUser(@PathVariable Long id) {
		DisableUserEvent event = new DisableUserEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());
		UserDisabledEvent resp = userService.deleteUser(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getMessage();
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/name={name}")
	@ResponseStatus(HttpStatus.OK)
	public String disableUser(@PathVariable String name) {
		DisableUserEvent event = new DisableUserEvent();
		event.setName(name);
		event.setSessionDataBean(getSession());
		UserDisabledEvent resp = userService.deleteUser(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getMessage();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/password")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String setPassword(@PathVariable Long id,
			@RequestParam(value = "token", required = false, defaultValue = "") String token,
			@RequestParam(value = "type", required = false, defaultValue = "") String type,
			@RequestBody PasswordDetails passwordDetails) {
		UpdatePasswordEvent event = new UpdatePasswordEvent(passwordDetails, token, id);
		PasswordUpdatedEvent resp = null;
		if (PasswordActionType.CHANGE.value().equalsIgnoreCase(type)) {
			resp = userService.changePassword(event);
		}
		else {
			resp = userService.setPassword(event);
		}

		if (resp.getStatus() == EventStatus.OK) {
			return resp.getMessage();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{loginName}/forgotPassword")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String forgotPassword(@PathVariable String loginName) {
		ForgotPasswordEvent event = new ForgotPasswordEvent();
		event.setName(loginName);
		PasswordForgottenEvent resp = userService.forgotPassword(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getMessage();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/validatePassword/{password}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Boolean validatePassword(@PathVariable String password) {
		ValidatePasswordEvent event = new ValidatePasswordEvent(password);
		PasswordValidatedEvent resp = userService.validatePassword(event);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.isValid();
		}
		return false;
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
