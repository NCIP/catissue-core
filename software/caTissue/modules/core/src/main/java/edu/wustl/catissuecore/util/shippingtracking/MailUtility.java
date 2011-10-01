/**
 * <p>Title: MailUtility </p>
 * <p>Description: Utility contains mail activities.</p>
 * Copyright:    Copyright (c) year
 * Company:
 * @author nilesh_ghone
 * @version 1.00
 */

package edu.wustl.catissuecore.util.shippingtracking;

import javax.mail.MessagingException;

import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.EmailDetails;
import edu.wustl.common.util.global.SendEmail;
import edu.wustl.common.util.logger.Logger;

/**
 * This is a utility class for mail notification functionality.
 */
public class MailUtility
{

	/**
	 * Sends email to the user with the email address passed.
	 * Returns true if the email is successfully sent else returns false.
	 * @param toUserEmailAddress mail address of the users to whom mail is to be sent.
	 * @param ccUserMailAddress mail address of the users who are to be kept in cc.
	 * @param subject The subject of the email.
	 * @param body The body of the email.
	 * @return true if the email is successfully sent else returns false.
	 */
	public static boolean sendEmailToUser(String[] toUserEmailAddress, String[] ccUserMailAddress,
			String subject, String body)
	{
		final Logger logger = Logger.getCommonLogger(MailUtility.class);
		final String mailServer = XMLPropertyHandler.getValue("email.mailServer");
		final String sendFromEmailAddress = XMLPropertyHandler
				.getValue("email.sendEmailFrom.emailAddress");
		final EmailDetails emailDetails = new EmailDetails();
		emailDetails.setToAddress(toUserEmailAddress);
		emailDetails.setBody(body);
		emailDetails.setSubject(subject);
		SendEmail email = null;
		try
		{
			email = new SendEmail(mailServer, sendFromEmailAddress);
		}
		catch (final MessagingException e)
		{
			logger.error(e.getMessage(), e);
		}
		final boolean emailStatus = email.sendMail(emailDetails);
		return emailStatus;
	}
}
