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

import com.krishagni.catissueplus.core.administrative.events.CloseUserEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserClosedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.administrative.events.UserUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/users")
public class UserController {

		private String ACTIVITY_STATUS_CLOSED = "Closed";
		
		@Autowired
		private UserService userService;

		@Autowired
		private HttpServletRequest httpServletRequest;
		
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
			
		@RequestMapping(method = RequestMethod.PUT, value="/{id}/close")
		@ResponseBody
		@ResponseStatus(HttpStatus.OK)
		public String closeUser(@PathVariable Long id) {
			CloseUserEvent event= new CloseUserEvent();
            event.setId(id);
            event.setSessionDataBean(getSession());
			UserClosedEvent resp = userService.closeUser(event);
			if (resp.getStatus() == EventStatus.OK) {
				return resp.getMessage();
			}
			return null;
		}
		
		private SessionDataBean getSession() {
			 return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
		}
}
