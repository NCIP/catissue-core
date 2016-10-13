package com.krishagni.catissueplus.core.common.service.impl;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.krishagni.catissueplus.core.common.domain.Email;
import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.service.TemplateService;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.MessageUtil;

public class EmailServiceImpl implements EmailService, ConfigChangeListener, InitializingBean {
	private static final Log logger = LogFactory.getLog(EmailServiceImpl.class);
	
	private static final String MODULE = "email";
	
	private static final String TEMPLATE_SOURCE = "email-templates/";
	
	private static final String BASE_TMPL = "baseTemplate";
	
	private static final String FOOTER_TMPL = "footer";
	
	private JavaMailSender mailSender;
	
	private TemplateService templateService;
	
	private ThreadPoolTaskExecutor taskExecutor;
	
	private ConfigurationService cfgSvc;

	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}

	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}

	@Override
	public void onConfigChange(String name, String value) {
		initializeMailSender();		
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		initializeMailSender();
		cfgSvc.registerChangeListener(MODULE, this);		
	}
	
	private void initializeMailSender() {
		try {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setUsername(getAccountId());
			mailSender.setPassword(getAccountPassword());
			mailSender.setHost(getMailServerHost());
			mailSender.setPort(getMailServerPort());

			String startTlsEnabled = getStartTlsEnabled();
			String authEnabled = getAuthEnabled();
			if (StringUtils.isNotBlank(startTlsEnabled) && StringUtils.isNotBlank(authEnabled)) {
				Properties props = new Properties();
				props.put("mail.smtp.starttls.enable", startTlsEnabled);
				props.put("mail.smtp.auth", authEnabled);
				mailSender.setJavaMailProperties(props);
			}
			
			this.mailSender = mailSender;
		} catch (Exception e) {
			logger.error("Error initialising e-mail sender", e);
		}
	}
	
	@Override
	public boolean sendEmail(String emailTmplKey, String[] to, Map<String, Object> props) {
		return sendEmail(emailTmplKey, to, null, props);
	}

	@Override
	public boolean sendEmail(String emailTmplKey, String[] to, File[] attachments, Map<String, Object> props) {
		return sendEmail(emailTmplKey, to, null, attachments, props);
	}

	@Override
	public boolean sendEmail(String emailTmplKey, String[] to, String[] bcc, File[] attachments, Map<String, Object> props) {
		return sendEmail(emailTmplKey, null, to, bcc, attachments, props);
	}

	@Override
	public boolean sendEmail(String emailTmplKey, String emailTmpl, String[] to, Map<String, Object> props) {
		return sendEmail(emailTmplKey, emailTmpl, to, null, null, props);
	}

	@Override
	public boolean sendEmail(Email mail) {
		try {
			final MimeMessage mimeMessage = mailSender.createMimeMessage();
			final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
			message.setSubject(mail.getSubject());
			message.setTo(mail.getToAddress());
			
			if (mail.getBccAddress() != null) {
				message.setBcc(mail.getBccAddress());
			}
			
			if (mail.getCcAddress() != null) {
				message.setCc(mail.getCcAddress());
			}
			
			message.setText(mail.getBody(), true); // true = isHtml
			message.setFrom(getAccountId());
			
			if (mail.getAttachments() != null) {
				for (File attachment: mail.getAttachments()) {
					FileSystemResource file = new FileSystemResource(attachment);
					message.addAttachment(file.getFilename(), file);
				}
			}
			
			taskExecutor.submit(new SendMailTask(mimeMessage));
			return true;
		} catch (Exception e) {
			logger.error("Error sending e-mail", e);
			return false;
		}
		
	}

	private boolean sendEmail(String tmplKey, String tmplContent, String[] to, String[] bcc, File[] attachments, Map<String, Object> props) {
		boolean emailEnabled = cfgSvc.getBoolSetting("notifications", "email_" + tmplKey, true);
		if (!emailEnabled) {
			return false;
		}

		String adminEmailId = getAdminEmailId();

		if (StringUtils.isNotBlank(tmplContent)) {
			props.put("templateContent", tmplContent);
		} else {
			props.put("template", getTemplate(tmplKey));
		}

		props.put("footer", getFooterTmpl());
		props.put("appUrl", getAppUrl());
		props.put("adminEmailAddress", adminEmailId);
		props.put("adminPhone", "1234567890");//TODO: will be replaced by property file
		props.put("dateFmt", new SimpleDateFormat(ConfigUtil.getInstance().getDateTimeFmt()));
		String subject = getSubject(tmplKey, (String[]) props.get("$subject"));
		String content = templateService.render(getBaseTmpl(), props);

		Email email = new Email();
		email.setSubject(subject);
		email.setBody(content);
		email.setToAddress(to);
		email.setCcAddress(new String[] {adminEmailId});
		email.setBccAddress(bcc);
		email.setAttachments(attachments);

		return sendEmail(email);
	}
	
	private String getSubject(String subjKey, String[] subjParams) {
		return getSubjectPrefix() + MessageUtil.getInstance().getMessage(subjKey.toLowerCase() + "_subj", subjParams);
	}

	private String getSubjectPrefix() {
		String subjectPrefix = MessageUtil.getInstance().getMessage("email_subject_prefix");
		String deployEnv = ConfigUtil.getInstance().getStrSetting("common", "deploy_env", "");

		subjectPrefix += " " + StringUtils.substring(deployEnv, 0, 10);
		return "[" + subjectPrefix.trim() + "]: ";
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
				logger.error("Error sending e-mail", e);
			}
		}
	}
	
	private String getTemplate(String tmplKey) {
		String localeTmpl = TEMPLATE_SOURCE + Locale.getDefault().toString() + "/" + tmplKey + ".vm";
		URL url = this.getClass().getClassLoader().getResource(localeTmpl);
		if (url == null) {
			localeTmpl = TEMPLATE_SOURCE + "default/" + tmplKey + ".vm";			
		}
		
		return localeTmpl;
	}
	
	private String getBaseTmpl() {
		return getTemplate(BASE_TMPL);
	}
	
	private String getFooterTmpl() {
		return getTemplate(FOOTER_TMPL);
	}
	
	/**
	 *  Config helper methods
	 */
	private String getAccountId() {
		return cfgSvc.getStrSetting(MODULE, "account_id");
	}
	
	private String getAccountPassword() {
		return cfgSvc.getStrSetting(MODULE, "account_password");
	}
	
	private String getMailServerHost() {
		return cfgSvc.getStrSetting(MODULE, "server_host");
	}
	
	private Integer getMailServerPort() {
		return cfgSvc.getIntSetting(MODULE, "server_port", 25);
	}
	
	private String getStartTlsEnabled() {
		return cfgSvc.getStrSetting(MODULE, "starttls_enabled");
	}
	
	private String getAuthEnabled() {
		return cfgSvc.getStrSetting(MODULE, "auth_enabled");
	}
	
	private String getAdminEmailId() {
		return cfgSvc.getStrSetting(MODULE, "admin_email_id");
	}	
	
	private String getAppUrl() {
		return cfgSvc.getStrSetting("common", "app_url");
	}
}
