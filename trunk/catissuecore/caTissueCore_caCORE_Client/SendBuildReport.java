import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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
 * <p>Program is used to send report via email.</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SendBuildReport
{

	/**
	 * Specify the report field 
	 */
	private static SendBuildReport report;
	
	/**
	 * get instance of mail report
	 * @return mapil report instance
	 */
	public static SendBuildReport getInstance()
	{
		if (report == null)
		{
			report = new SendBuildReport();
		}
		return report;
	}
	
    /**
     * Used to send the mail with given parameters & report attachment.
     * @param to "To" Address for sending the mail
     * @param cc "CC" Address for sending the mail
     * @param bcc "BCC" Address for sending the mail
     * @param from "From" Address for sending the mail
     * @param host "Host" from where to send the mail
     * @param subject "Subject" of the mail
     * @param body "Body" of the mail
     * @param filePath path of file to be send as attachment
     * @return true if mail was successfully sent, false if it fails
     */
    public void sendmail(String to, String cc, String bcc, String from, String host,
            String subject, String body, String filePath)
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
    		messageBodyPart = new MimeBodyPart();
    		DataSource source = new FileDataSource(filePath);
    		messageBodyPart.setDataHandler(new DataHandler(source));
    		messageBodyPart.setFileName("catissuecoreclient.log");
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
        }
        catch (Exception ex)
        {
            Logger.out.warn("Unable to send mail to: " + to);
            Logger.out.warn("Exception= " + ex.getMessage());
        }
    }

}
