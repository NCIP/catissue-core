package com.krishagni.catissueplus.core.common.service.impl;

import java.io.File;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.krishagni.catissueplus.core.common.domain.Email;
import com.krishagni.catissueplus.core.common.service.EmailService;

import edu.wustl.common.util.XMLPropertyHandler;

public class EmailServiceImpl implements EmailService {
	private String fromAddress;
	
	private JavaMailSender mailSender;
	
	private ThreadPoolTaskExecutor taskExecutor;

	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	private void initJavaMailSenderInstance() {
		try {
			String fromAddress = XMLPropertyHandler.getValue("email.sendEmailFrom.emailAddress");
			String fromPassword = XMLPropertyHandler.getValue("email.sendEmailFrom.emailPassword");
			String mailServer = XMLPropertyHandler.getValue("email.mailServer");
			String mailServerPort = XMLPropertyHandler.getValue("email.mailServer.port");
			String isStartTLSEnabled = XMLPropertyHandler.getValue("email.smtp.starttls.enabled");
			String isSMTPAuthEnabled = XMLPropertyHandler.getValue("email.smtp.auth.enabled");
			
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setUsername(fromAddress);
			mailSender.setPassword(fromPassword); 
			mailSender.setHost(mailServer);
			mailSender.setPort(new Integer(mailServerPort));
			
			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable", isStartTLSEnabled);
			props.put("mail.smtp.auth", isSMTPAuthEnabled);
			mailSender.setJavaMailProperties(props);
			
			this.mailSender = mailSender;
			this.fromAddress = fromAddress;
		} catch (Exception e) {
			
		}
	}

	@Override
	public boolean sendMail(Email mail) throws MessagingException {
		if (mailSender == null) {
			initJavaMailSenderInstance();
		}
		
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
