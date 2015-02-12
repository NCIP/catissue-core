
package com.krishagni.catissueplus.rest.controller;

import java.util.HashMap;
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

import com.krishagni.catissueplus.core.administrative.events.ListUserCriteria;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.events.UserDetail;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
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
	public List<UserSummary> getAllUsers(
			@RequestParam(value = "start", required = false, defaultValue = "0") 
			int start,
			
			@RequestParam(value = "max", required = false, defaultValue = "100") 
			int max,
			
			@RequestParam(value = "searchString", required = false, defaultValue = "") 
			String searchString){
		
		ListUserCriteria crit = new ListUserCriteria()
			.startAt(start)
			.maxResults(max)
			.query(searchString);
		
		RequestEvent<ListUserCriteria> req = new RequestEvent<ListUserCriteria>(getSession(), crit);
		ResponseEvent<List<UserSummary>> resp = userService.getAllUsers(req);
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/signed-in-user")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public UserDetail getSignedInUser() {
		return getUser(getSession().getUserId());
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

	@RequestMapping(method = RequestMethod.DELETE, value = "close/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public UserDetail closeUser(@PathVariable Long id) {
		RequestEvent<Long> req = new RequestEvent<Long>(getSession(), id);
		ResponseEvent<UserDetail> resp = userService.closeUser(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public UserDetail disableUser(@PathVariable Long id) {
		RequestEvent<Long> req = new RequestEvent<Long>(getSession(), id);
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

	@RequestMapping(method = RequestMethod.POST, value = "/forgot-password-token")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Boolean forgotPassword(@RequestBody HashMap<String, String>  data) {
		RequestEvent<String> req = new RequestEvent<String>(getSession(), data.get("loginName"));
		ResponseEvent<Boolean> resp = userService.forgotPassword(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/validatePassword/{password}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Boolean validatePassword(@PathVariable String password) {
		RequestEvent<String> req = new RequestEvent<String>(getSession(), password);
		ResponseEvent<Boolean> resp = userService.validatePassword(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
