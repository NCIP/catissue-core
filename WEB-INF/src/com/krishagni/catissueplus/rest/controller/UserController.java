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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.events.AllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteUserEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.administrative.events.UserUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/users")
public class UserController {

		private String ACTIVITY_STATUS_DISABLED = "Disabled";
		
		@Autowired
		private UserService userService;

		@Autowired
		private HttpServletRequest httpServletRequest;

		@RequestMapping(method = RequestMethod.GET)
		@ResponseStatus(HttpStatus.OK)
		@ResponseBody
		public List<User> getUserList() {
			ReqAllUsersEvent req = new ReqAllUsersEvent();
			req.setSessionDataBean(getSession());
			AllUsersEvent result = userService.getAllUsers(req);
			return result.getUserList();
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
		
		@RequestMapping(method = RequestMethod.PUT, value="/{id}")
		@ResponseBody
		@ResponseStatus(HttpStatus.OK)
		public UserDetails updateUser(@PathVariable Long id, @RequestBody UserDetails userDetails) {
			UserUpdatedEvent resp = userService.updateUser(new UpdateUserEvent(userDetails, id));
			if (resp.getStatus() == EventStatus.OK) {
				return resp.getUserDetails();
			}
			return null;
		}
		
		@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
		@ResponseStatus(HttpStatus.OK)
		@ResponseBody
		public String deleteUser(@PathVariable Long id) {
			DeleteUserEvent event = new DeleteUserEvent();
			event.setId(id);
			UserDeletedEvent resp = userService.delete(event);;
			if (resp.getStatus() == EventStatus.OK) {
				return resp.getMessage();
			}
			return null;
		}

		private SessionDataBean getSession() {
			return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
		}
}
