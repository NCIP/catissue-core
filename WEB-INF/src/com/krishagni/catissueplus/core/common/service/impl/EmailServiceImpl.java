package com.krishagni.catissueplus.core.common.service.impl;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.krishagni.catissueplus.core.common.domain.Email;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.service.TemplateService;
import com.krishagni.catissueplus.core.common.util.Utility;

import edu.wustl.common.util.XMLPropertyHandler;

public class EmailServiceImpl implements EmailService {
	private static final String locale = Locale.getDefault().toString();
	
	private static final String TEMPLATE_SOURCE = "email-templates/" + locale;
	
	private static final String BASE_TMPL = TEMPLATE_SOURCE + "/baseTemplate.vm";
	
	private static final String FOOTER_TMPL = TEMPLATE_SOURCE + "/footer.vm";
	
	private static String adminEmailAddress;
	
	private static String fromAddress;
	
	private static String subjectPrefix;
	
	private JavaMailSender mailSender;
	
	private TemplateService templateService;
	
	private ResourceBundleMessageSource resourceBundle;
	
	private ThreadPoolTaskExecutor taskExecutor;

	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}

	public void setResourceBundle(ResourceBundleMessageSource resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	public EmailServiceImpl(){
		initJavaMailSenderInstance();
	}
	
	private void initJavaMailSenderInstance() {
		try {
			adminEmailAddress = XMLPropertyHandler.getValue("email.administrative.emailAddress");
			fromAddress = XMLPropertyHandler.getValue("email.sendEmailFrom.emailAddress");
			
			String fromPassword = XMLPropertyHandler.getValue("email.sendEmailFrom.emailPassword");
			String host = XMLPropertyHandler.getValue("email.mailServer");
			String port = XMLPropertyHandler.getValue("email.mailServer.port");
			String isStartTLSEnabled = XMLPropertyHandler.getValue("email.smtp.starttls.enabled");
			String isSMTPAuthEnabled = XMLPropertyHandler.getValue("email.smtp.auth.enabled");
			
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setUsername(fromAddress);
			mailSender.setPassword(fromPassword);
			mailSender.setHost(host);
			
			if (StringUtils.isNotBlank(port)) {
				mailSender.setPort(new Integer(port));
			}
			
			if (StringUtils.isNotBlank(isStartTLSEnabled) && StringUtils.isNotBlank(isSMTPAuthEnabled)) {
				Properties props = new Properties();
				props.put("mail.smtp.starttls.enable", isStartTLSEnabled);
				props.put("mail.smtp.auth", isSMTPAuthEnabled);
				mailSender.setJavaMailProperties(props);
			}
			
			this.mailSender = mailSender;
		} catch (Exception e) {
			new RuntimeException("Error while initialising java mail sender", e);
		}
	}
	
	@Override
	public boolean sendEmail(String emailTmplKey, String[] to, Map<String, Object> props) {
		return sendEmail(emailTmplKey, to, null, props);
	}
	
	@Override
	public boolean sendEmail(String emailTmplKey, String[] to, File[] attachments, Map<String, Object> props) {
		props.put("template", getEmailTmpl(emailTmplKey));
		props.put("footer", FOOTER_TMPL);
		props.put("appUrl", Utility.getAppUrl());
		props.put("adminEmailAddress", adminEmailAddress);
		props.put("adminPhone", "1234567890");//TODO: will be replaced by property file
		String subject = getSubject(emailTmplKey, (String[]) props.get("$subject"));
		String content = templateService.render(BASE_TMPL, props);
		
		Email email = new Email();
		email.setSubject(subject);
		email.setBody(content);
		email.setToAddress(to);
		email.setCcAddress(new String[] {adminEmailAddress});
		email.setAttachments(attachments);
		
		return sendEmail(email);
	}

	@Override
	public boolean sendEmail(Email mail) {
		try {
			final MimeMessage mimeMessage = mailSender.createMimeMessage();
			final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8"); // true = multipart
			message.setSubject(mail.getSubject());
			message.setTo(mail.getToAddress());
			
			if (mail.getBccAddress() != null) {
				message.setBcc(mail.getBccAddress());
			}
			
			if (mail.getCcAddress() != null) {
				message.setCc(mail.getCcAddress());
			}
			
			message.setText(mail.getBody(), true); // true = isHtml
			message.setFrom(fromAddress);
			
			if (mail.getAttachments() != null) {
				for (File attachment: mail.getAttachments()) {
					FileSystemResource file = new FileSystemResource(attachment);
					message.addAttachment(file.getFilename(), file);
				}
			}
			
			taskExecutor.submit(new SendMailTask(mimeMessage));
			return true;
		} catch (MessagingException e) {
			 throw OpenSpecimenException.serverError(e);
		}
	}
	
	private String getSubject(String emailTmplKey, String[] subjParams) {
		if (subjectPrefix == null) {
			subjectPrefix = resourceBundle.getMessage("email_subject_prefix", null, Locale.getDefault());
		}
		
		return subjectPrefix + resourceBundle.getMessage(emailTmplKey.toLowerCase() + "_subj", subjParams, Locale.getDefault());
	}
	
	private String getEmailTmpl(String emailTmplKey) {
		return TEMPLATE_SOURCE + "/" + emailTmplKey + ".vm";
	}
	
	private class SendMailTask implements Runnable {
		private MimeMessage mimeMessage;
		
		public SendMailTask(MimeMessage mimeMessage) {
			this.mimeMessage = mimeMessage;
		}
		
		public void run() {
			try{
				mailSender.send(mimeMessage);
			} catch(Exception e) {
				new RuntimeException("Error while sending Email", e);
			}
		}
	}
}
