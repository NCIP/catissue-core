package com.krishagni.catissueplus.core.support.services.impl;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.support.events.FeedbackDetail;
import com.krishagni.catissueplus.core.support.services.SupportService;

public class SupportServiceImpl implements SupportService {
	private static final String FEEDBACK_EMAIL_TMPL = "feedback";
	
	private EmailService emailService;
	
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	@Override
	public ResponseEvent<Boolean> sendFeedback(RequestEvent<FeedbackDetail> req) {
		FeedbackDetail detail = req.getPayload();
		return ResponseEvent.response(sendFeedbackEmail(AuthUtil.getCurrentUser(), detail));
	}
	
	private boolean sendFeedbackEmail(User user, FeedbackDetail detail) {
		String[] rcpt = new String[]{ConfigUtil.getInstance().getStrSetting("common", "support_email", "")};
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("user", user);
		props.put("feedback", detail);
		
		return emailService.sendEmail(FEEDBACK_EMAIL_TMPL, rcpt, props);
	}	
}
