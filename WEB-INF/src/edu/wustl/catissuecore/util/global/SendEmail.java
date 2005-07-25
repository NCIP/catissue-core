/**
 * <p>Title: SendEmail Class>
 * <p>Description:	This Class is used to send emails.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 18, 2005
 */

package edu.wustl.catissuecore.util.global;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import edu.wustl.common.util.logger.Logger;


/**
 * This Class is used to send emails.
 * @author gautam_shetty
 */
public class SendEmail
{
    
    /**
     * Used to send the mail with given parameters.
     * @param to "To" Address for sending the mail
     * @param from "From" Address for sending the mail
     * @param host "Host" from where to send the mail
     * @param subject "Subject" of the mail
     * @param body "Body" of the mail
     * @return true if mail was successfully sent, false if it fails
     */
    public boolean sendmail(String to, String from, String host,
            String subject, String body)
    {
        return sendmail(to,null,null,from,host,subject,body);
    }
    
    /**
     * Used to send the mail with given parameters.
     * @param to "To" Address for sending the mail
     * @param cc "CC" Address for sending the mail
     * @param bcc "BCC" Address for sending the mail
     * @param from "From" Address for sending the mail
     * @param host "Host" from where to send the mail
     * @param subject "Subject" of the mail
     * @param body "Body" of the mail
     * @return true if mail was successfully sent, false if it fails
     */
    public boolean sendmail(String to, String cc, String bcc, String from, String host,
            String subject, String body)
    {
        
        //create some properties and get the default Session
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
 
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(false);

        try
        {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = {new InternetAddress(to)};
            
            if (cc != null)
            {
                InternetAddress[] ccAddress = {new InternetAddress(cc)};
                msg.setRecipients(Message.RecipientType.CC,ccAddress);
            }

            if (bcc != null)
            {
                InternetAddress[] bccAddress = {new InternetAddress(bcc)};
                msg.setRecipients(Message.RecipientType.BCC,bccAddress);
            }
            //set TO
            msg.setRecipients(Message.RecipientType.TO, toAddress);

            //Set Subject
            msg.setSubject(subject);
            //set Date
            msg.setSentDate(new Date());
            // create and fill the first message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
          
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(messageBodyPart);
            // add the Multipart to the message
            msg.setContent(mp);
            // send the message
            Transport.send(msg);
        }
        catch (MessagingException mex)
        {
            Logger.out.warn("Unable to send mail to: " + to);
            Logger.out.warn("Exception= " + mex.getMessage());
            Exception ex = null;
            if ((ex = mex.getNextException()) != null)
            {
                Logger.out.warn("Exception= " + ex.getMessage());
            }
            return false;
        }
        catch (Exception ex)
        {
            Logger.out.warn("Unable to send mail to: " + to);
            Logger.out.warn("Exception= " + ex.getMessage());
            return false;
        }

        return true;
    }
}
