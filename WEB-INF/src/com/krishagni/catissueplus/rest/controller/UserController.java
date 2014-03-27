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
import com.krishagni.catissueplus.core.administrative.events.ForgotPasswordEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordDetails;
import com.krishagni.catissueplus.core.administrative.events.PasswordForgottenEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdatePasswordEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserClosedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.administrative.events.UserUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.common.email.EmailHandler;
import com.krishagni.catissueplus.core.common.events.EventStatus;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import gov.nih.nci.semantic.util.Email;

@Controller
@RequestMapping("/users")
public class UserController {

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
				EmailHandler.sendUserCreatedEmail(resp.getUserDetails());
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
				EmailHandler.sendUserUpdatedEmail(resp.getUserDetails());
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
		
		@RequestMapping(method = RequestMethod.PUT, value="/{id}/setPassword/{token}")
		@ResponseBody
		@ResponseStatus(HttpStatus.OK)
		public PasswordDetails setPassword(@PathVariable Long id, @PathVariable String token, @RequestBody PasswordDetails passwordDetails) {
			PasswordUpdatedEvent resp = userService.setPassword(new UpdatePasswordEvent(passwordDetails, token, id));
			if (resp.getStatus() == EventStatus.OK) {
				return resp.getPasswordDetails();
			}
			return null;
		}
		
		@RequestMapping(method = RequestMethod.PUT, value="/{id}/resetPassword/{token}")
		@ResponseBody
		@ResponseStatus(HttpStatus.OK)
		public PasswordDetails resetPassword(@PathVariable Long id, @PathVariable String token, @RequestBody PasswordDetails passwordDetails) {
			PasswordUpdatedEvent resp = userService.resetPassword(new UpdatePasswordEvent(passwordDetails, token, id));
			if (resp.getStatus() == EventStatus.OK) {
				return resp.getPasswordDetails();
			}
			return null;
		}
		
		@RequestMapping(method = RequestMethod.PUT, value="/{id}/forgotPassword")
		@ResponseBody
		@ResponseStatus(HttpStatus.OK)
		public String forgotPassword(@PathVariable Long id) {
			ForgotPasswordEvent event = new ForgotPasswordEvent();
			event.setId(id);
			PasswordForgottenEvent resp = userService.forgotPassword(event);
			if (resp.getStatus() == EventStatus.OK) {
				EmailHandler.sendForgotPasswordEmail(resp.getUserDetails());
				return resp.getMessage();
			}
			return null;
		}
		
		private SessionDataBean getSession() {
			 return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
		}
}
