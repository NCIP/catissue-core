package com.krishagni.catissueplus.rest.controller;

import java.util.List;
import java.util.Map;

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
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.events.UserDetail;
import com.krishagni.catissueplus.core.administrative.repository.UserListCriteria;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.auth.services.UserAuthenticationService;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserAuthenticationService userAuthService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<UserSummary> getUsers(
			@RequestParam(value = "start", required = false, defaultValue = "0") 
			int start,
			
			@RequestParam(value = "max", required = false, defaultValue = "100") 
			int max,
			
			@RequestParam(value = "searchString", required = false) 
			String searchString,
			
			@RequestParam(value = "name", required = false)
			String name,
			
			@RequestParam(value = "loginName", required = false)
			String loginName,
			
			@RequestParam(value = "activityStatus", required = false)
			String activityStatus
			){
		
		UserListCriteria crit = new UserListCriteria()
			.startAt(start)
			.maxResults(max)
			.query(searchString)
			.name(name)
			.loginName(loginName)
			.activityStatus(activityStatus);
		
		
		RequestEvent<UserListCriteria> req = new RequestEvent<UserListCriteria>(getSession(), crit);
		ResponseEvent<List<UserSummary>> resp = userService.getUsers(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();		
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public UserDetail getUser(@PathVariable Long id) {
		ResponseEvent<UserDetail> resp = userService.getUser(new RequestEvent<Long>(getSession(), id));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public UserDetail createUser(@RequestBody UserDetail userDetails) {
		RequestEvent<UserDetail> req = new RequestEvent<UserDetail>(getSession(), userDetails);
		ResponseEvent<UserDetail> resp = userService.createUser(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/sign-up")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public UserDetail signupUser(@RequestBody UserDetail detail) {
		RequestEvent<UserDetail> req = new RequestEvent<UserDetail>(getSession(), detail);
		ResponseEvent<UserDetail> resp = userService.createUser(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public UserDetail updateUser(@PathVariable Long id, @RequestBody UserDetail userDetails) {
		userDetails.setId(id);
		
		RequestEvent<UserDetail> req = new RequestEvent<UserDetail>(getSession(), userDetails);
		ResponseEvent<UserDetail> resp = userService.updateUser(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/activity-status")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public UserDetail activateUser(@PathVariable Long id) {
		RequestEvent<Long> req = new RequestEvent<Long>(null, id);
		ResponseEvent<UserDetail> resp = userService.activateUser(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/dependency-stat")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<Map<String, Object>> getUserDependencyStat(@PathVariable Long id) {
		RequestEvent<Long> req = new RequestEvent<Long>(null, id);
		ResponseEvent<List<Map<String, Object>>> resp = userService.getUserDependencyStat(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public UserDetail deleteUser(@PathVariable Long id,
			@RequestParam(value = "close", required = false, defaultValue = "false") boolean close) {
		DeleteEntityOp deleteEntityOp = new DeleteEntityOp(id, close);
		RequestEvent<DeleteEntityOp> req = new RequestEvent<DeleteEntityOp>(null, deleteEntityOp);
		ResponseEvent<UserDetail> resp = userService.deleteUser(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/update-password")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Boolean changePassword(@RequestBody PasswordDetails passwordDetails) {
		RequestEvent<PasswordDetails> req = new RequestEvent<PasswordDetails>(getSession(), passwordDetails);
		ResponseEvent<Boolean> resp = userService.changePassword(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/reset-password")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Boolean resetPassword(@RequestBody PasswordDetails passwordDetails) {
		RequestEvent<PasswordDetails> req = new RequestEvent<PasswordDetails>(getSession(), passwordDetails);
		ResponseEvent<Boolean> resp = userService.resetPassword(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/forgot-password")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Boolean forgotPassword(@RequestBody Map<String, String>  data) {
		RequestEvent<String> req = new RequestEvent<String>(getSession(), data.get("loginName"));
		ResponseEvent<Boolean> resp = userService.forgotPassword(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/current-user")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public UserSummary getCurrentUser() {
		ResponseEvent<UserSummary> resp = userAuthService.getCurrentLoggedInUser();
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
 	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
