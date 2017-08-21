package com.krishagni.catissueplus.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.support.events.FeedbackDetail;
import com.krishagni.catissueplus.core.support.services.SupportService;

@Controller
@RequestMapping("/support")
public class SupportController {
	@Autowired
	private SupportService supportService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/user-feedback")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public boolean sendFeedback(@RequestBody FeedbackDetail feedbackDetail) {
		ResponseEvent<Boolean> resp = supportService.sendFeedback(new RequestEvent<FeedbackDetail>(feedbackDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}
