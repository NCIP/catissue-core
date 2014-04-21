
package com.krishagni.catissueplus.core.common.email;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.krishagni.catissueplus.core.common.email.domain.EmailDetails;

import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

public class SendEmail {

	private static String HOST_CANNOTBE_NULL = "Host can't be null";

	private static String SUCCESSFUL_EMAIL_SENT_MSG = "Email Sent Successfully...";

	private static String EMAIL_NOT_SENT_MSG = "Email Not Sent Successfully...";

	private static final Logger LOGGER = Logger.getCommonLogger(SendEmail.class);

	private String from;

	private String host;

	private String emailPassword;

	private String port;

	private String isAuthEnabled;

	private String isStartTLSEnabled;

	public SendEmail(String host, String from) throws MessagingException {
		if (null == host) {
			LOGGER.fatal(HOST_CANNOTBE_NULL);
			throw new MessagingException(HOST_CANNOTBE_NULL);
		}
		this.host = host;
		this.from = from;
	}

	/**
	 * Constructor with host and from e-mail address and password.
	 * @param emailPassword Sender email address password 
	 * @param from host address of man server.
	 * @param host Sender email address.
	 */
	public SendEmail(String host, String from, String emailPassword, String port, String isAuthEnabled,
			String isStartTLSEnabled) throws MessagingException {
		if (null == host) {
			LOGGER.fatal(HOST_CANNOTBE_NULL);
			throw new MessagingException(HOST_CANNOTBE_NULL);
		}
		this.host = host;
		this.from = from;
		this.emailPassword = emailPassword;
		this.port = port;
		this.isAuthEnabled = isAuthEnabled;
		this.isStartTLSEnabled = isStartTLSEnabled;
	}

	/**
	 * Used to send the mail with given parameters.
	 *
	 * @param emailDetails EmailDetails object which contains email subject, body recipient address.
	 * @return true if mail was successfully sent, false if it fails
	 */
	public boolean sendMail(EmailDetails emailDetails) {
		boolean sendStatus;
		Session session = getEmailSession();
		try {
			MimeMessage msg = setEmailInfo(emailDetails, session);
			Transport.send(msg);
			sendStatus = true;
		}
		catch (MessagingException mex) {
			LOGGER.warn(EMAIL_NOT_SENT_MSG + emailDetails.getToAddress(), mex);
			sendStatus = false;
			System.out.println(mex.getMessage());
		}
		return sendStatus;
	}

	/**
	 * @param emailDetails EmailDetails object which contains email subject, body recipient address.
	 * @param session Email Session.
	 * @return message with updated
	 * @throws MessagingException This exception thrown if email parameters are wrongly formatted.
	 */
	private MimeMessage setEmailInfo(EmailDetails emailDetails, Session session) throws MessagingException {
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		msg.setRecipients(Message.RecipientType.CC, emailDetails.getCcInternetAddrArray());
		msg.setRecipients(Message.RecipientType.BCC, emailDetails.getBccInternetAddrArray());
		msg.setRecipients(Message.RecipientType.TO, emailDetails.getToInternetAddrArray());
		msg.setSubject(emailDetails.getSubject());
		msg.setSentDate(new Date());
		Multipart multiPart = new MimeMultipart();
		if (emailDetails.getAttachments() != null) {
			int index = 0;
			for (File attachment : emailDetails.getAttachments()) {
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(attachment);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(source.getName());
				multiPart.addBodyPart(messageBodyPart, index);
				index++;
			}
		}
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		if (emailDetails.isHtmlBody()) {
			messageBodyPart.setContent(emailDetails.getBody(), "text/html");
		}
		else {
			messageBodyPart.setText(emailDetails.getBody());
		}
		multiPart.addBodyPart(messageBodyPart);
		msg.setContent(multiPart);
		return msg;
	}

	/**
	 * Provided email password for authentication.
	 * @return Default mail Session.
	 */
	private Session getEmailSession() {
		Session session = null;
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		if ((this.emailPassword == null) || ("".equals(this.emailPassword))) {
			session = Session.getDefaultInstance(props, null);
		}
		else {
			props.put("mail.smtp.auth", isAuthEnabled);
			props.put("mail.smtp.starttls.enable", isStartTLSEnabled);
			props.put("mail.smtp.port", port);
			Authenticator auth = new MailAuthenticator(this.from, this.emailPassword);
			session = Session.getInstance(props, auth);
		}
		session.setDebug(false);
		return session;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the emailPassword
	 */
	public String getEmailPassword() {
		return emailPassword;
	}

	/**
	 * @param emailPassword the emailPassword to set
	 */
	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getIsAuthEnabled() {
		return isAuthEnabled;
	}

	public void setIsAuthEnabled(String isAuthEnabled) {
		this.isAuthEnabled = isAuthEnabled;
	}

	public String getIsStartTLSEnabled() {
		return isStartTLSEnabled;
	}

	public void setIsStartTLSEnabled(String isStartTLSEnabled) {
		this.isStartTLSEnabled = isStartTLSEnabled;
	}

	/**
	 * This is the private class which performs SMTP Mail Authentication
	 * This mail authentication is Password Authentication to check the sender of 
	 * the mail is valid user of the mail server.
	 * @author shrishail_kalshetty
	 *
	 */
	private class MailAuthenticator extends Authenticator {

		/**
		 * Sender Email Address.
		 */
		private String emailSenderId;

		/**
		 * Password of Sender Email Address.
		 */
		private String emailPassword;

		/**
		 * @param emailPassword Password of the Senders email Address.
		 * @param emailSenderId Email Address of the Sender.
		 */
		public MailAuthenticator(String emailSenderId, String emailPassword) {
			this.emailSenderId = emailSenderId;
			this.emailPassword = emailPassword;
		}

		/**
		 * @return returns the password authentication.
		 */
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(this.emailSenderId, this.emailPassword);
		}
	}

	/**
	 * This is the main method to check whether Email sent successfully or not.
	 * @param args Arguments that are passed to the main method.
	 */
	public static void main(String[] args) {
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
		EmailDetails emailDetails = new EmailDetails();
		String[] toAddress = {args[3], args[4]};
		emailDetails.setToAddress(toAddress);
		emailDetails.setBody("Body of the Mail");
		emailDetails.setSubject("Test Mail");

		try {
			String host = args[0];
			String from = args[1];
			String passwd = args[2];
			String port = args[3];
			String isAuthEnabled = args[4];
			String isStartTLSEnabled = args[5];
			SendEmail sendEmail = new SendEmail(host, from, passwd, port, isAuthEnabled, isStartTLSEnabled);
			if (sendEmail.sendMail(emailDetails)) {
				LOGGER.info(SUCCESSFUL_EMAIL_SENT_MSG);
			}
			else {
				LOGGER.info(EMAIL_NOT_SENT_MSG);
			}
		}
		catch (MessagingException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}