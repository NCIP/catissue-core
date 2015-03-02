package com.krishagni.catissueplus.core.common.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.domain.Email;
import com.krishagni.catissueplus.core.common.service.EmailHandler;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.service.TemplateService;

import edu.wustl.common.util.XMLPropertyHandler;

public class EmailHandlerImpl implements EmailHandler {
	private static String FORGOT_PASSWORD_EMAIL_TMPL = "user.forgotPasswordTemplate";
	
	private static String CHANGED_PASSWORD_EMAIL_TMPL = "user.changedPasswordTemplate";
	
	private static String BASE_TMPL = "baseTemplate";
	
	private static final String adminEmailAddress = XMLPropertyHandler.getValue("email.administrative.emailAddress");
	
	private static Properties emailTemplates = null;

	private static Properties subjectProperties = null;
	
	private static String subjectPrefix = null;
	
	private EmailService emailService;
	
	private TemplateService templateService;

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public TemplateService getTemplateService() {
		return templateService;
	}

	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}
	
	public EmailHandlerImpl() {
		init();
	}
	
	private void init() {
		String subPropFile = "../resources/ng-email-templates/emailSubjects.properties";
		String tmplPropFile = "../resources/ng-email-templates/emailTemplates.properties";
		
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			
			subjectProperties = new Properties();
			subjectProperties.load(classLoader.getResourceAsStream(subPropFile));

			emailTemplates = new Properties();
			emailTemplates.load(classLoader.getResourceAsStream(tmplPropFile));
			
			if (subjectProperties.getProperty("subject.property") != null) {
				subjectPrefix = subjectProperties.getProperty("subject.property");
			}

		}
		catch (Exception e) {
			throw new RuntimeException("Error while initializing Email handler", e);
		}
	}

	@Override
	public Boolean sendForgotPasswordEmail(final User user, final String token) throws MessagingException {
		Map<String, String> props = new HashMap<String, String>();
		props.put("template", "ng-email-templates/html/user.forgotPasswordTemplate.vm");
		props.put("loginName", user.getLoginName());
		props.put("token", token);
		props.put("appUrl", getAppUrl());

		Email email = createEmail(FORGOT_PASSWORD_EMAIL_TMPL, new String[]{user.getEmailAddress()}, new String[]{adminEmailAddress}, 
				null, null, props);
		return emailService.sendMail(email);
	}
	
	public Boolean sendChangedPasswordEmail(final User user) throws MessagingException {
		Map<String, String> props = new HashMap<String, String>();
		props.put("lastName", user.getLastName());
		props.put("firstName", user.getFirstName());
		props.put("appUrl", getAppUrl());
		Email email = createEmail(CHANGED_PASSWORD_EMAIL_TMPL, new String[]{user.getEmailAddress()}, new String[]{adminEmailAddress}, 
				null, null, props);
		return emailService.sendMail(email);
	}
	
	private Email createEmail(String tmplKey, String[] to, String[] cc, String[] bcc,
			File[] attachments, Map<String, String> props) {
		try {
			String emailTemplate = emailTemplates.getProperty(BASE_TMPL);
			props.put("template", emailTemplates.getProperty(tmplKey));
			
			Email email = new Email();
			email.setSubject(subjectPrefix + "" + subjectProperties.getProperty(tmplKey));
			email.setBody(templateService.render(emailTemplate, props));
			email.setToAddress(to);
			email.setCcAddress(cc);
			email.setBccAddress(bcc);
			email.setAttachments(attachments);
			
			return email;
		} catch (Exception e) {
			throw new RuntimeException("Exception got while creating mail", e);
		}
	}
	
	private static String getAppUrl() {
		HttpServletRequest request=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
	
}
