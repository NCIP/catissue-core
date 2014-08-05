/**
 * <p>Title: EmailHandler Class>
 * <p>Description:	EmailHandler is used to send emails during user signup, creation, forgot password.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.util;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.EmailClient;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.EmailDetails;
import edu.wustl.common.util.global.SendEmail;
import edu.wustl.common.util.logger.Logger;


/**
 * EmailHandler is used to send emails during user signup, creation, forgot password.
 * @author gautam_shetty
 */
public class EmailHandler
{
	private static final Logger logger = Logger.getCommonLogger(EmailHandler.class);
    private static final String KEY_EMAIL_ADMIN_EMAIL_ADDRESS = "email.administrative.emailAddress";
    private static final String adminEmailAddress = XMLPropertyHandler.getValue(KEY_EMAIL_ADMIN_EMAIL_ADDRESS);
    /**
     * Creates and sends the user registration approval emails to user and the administrator.
     * @param user The user whose registration is approved.
     * @param roleOfUser Role of the user.
     * @throws Exception 
     */
    public void sendApprovalEmail(final User user)
    {
    	Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("user", user);
		contextMap.put("url", CommonServiceLocator.getInstance().getAppURL());
		
		boolean emailStatus = EmailClient.getInstance().sendEmail(
				Constants.USER_APPROVAL_EMAIL_TMPL,
				new String[] { user.getEmailAddress(), adminEmailAddress },
				contextMap);
		logEmailStatus(user, emailStatus);
    }

    /**
     * Creates and sends the user registration rejection emails to user and the administrator.
     * @param user The user whose registration is rejected.
     */
    public void sendRejectionEmail(final User user)
    {
    	Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("user", user);
		boolean emailStatus = EmailClient.getInstance().sendEmail(
				Constants.USER_REJECTION_EMAIL_TMPL,
				new String[]{ user.getEmailAddress() },
				new String[]{ adminEmailAddress },
				null, contextMap, null);
		logEmailStatus(user, emailStatus);
    }

    /**
     * Creates and sends the user signup request received email to the user and the administrator.
     * @param user The user registered for the membership.
     */
    public void sendUserSignUpEmail(final User user)
    {
    	Map<String, Object> contextMap = new HashMap<String, Object>();
    	contextMap.put("user", user);
    	boolean emailStatus = EmailClient.getInstance().sendEmail(
    			Constants.USER_SIGNUP_EMAIL_TMPL,
    			new String[]{ user.getEmailAddress() },
				new String[]{ adminEmailAddress },
				null, contextMap, null);
    	logEmailStatus(user, emailStatus);
    }
    
    
    public boolean sendForgotPasswordEmail(final User user, final String userToken) throws ApplicationException
    {
    	Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("user", user);
		contextMap.put("url", CommonServiceLocator.getInstance().getAppURL());
		contextMap.put("userToken", userToken);

		boolean emailStatus = EmailClient.getInstance().sendEmail(
				  Constants.USER_FORGOT_PASSWORD_EMAIL_TMPL,
				  new String[]{ user.getEmailAddress() },
				  contextMap);
		logEmailStatus(user, emailStatus);
        return emailStatus;
    }


	/**
	 * method logs email status
	 * @param user
	 * @param emailStatus
	 */
	private void logEmailStatus(final User user, final boolean emailStatus)
	{
		if (emailStatus){
			logger.info(ApplicationProperties
			    .getValue("user.loginDetails.email.success")
			    + user.getLastName() + " " + user.getFirstName());
		} else {
			logger.info(ApplicationProperties
			    .getValue("user.loginDetails.email.failure")
			    + user.getLastName() + " " + user.getFirstName());
		}
	}

    /**
     * Sends email to the administrator and the user who reported the problem.
     * @param reportedProblem The problem reported.
     */
    public void sendReportedProblemEmail(final ReportedProblem reportedProblem)
    {
        // Send the reported problem to administrator and the user who reported it.
    	Map<String, Object> contextMap = new HashMap<String, Object>();
    	contextMap.put("reportedProblem", reportedProblem);
    	
        boolean emailStatus = EmailClient.getInstance().sendEmail(
        		 Constants.USER_REPORTEDPROB_EMAIL_TMPL,
        		 new String[]{ reportedProblem.getFrom() },
        		 new String[]{ adminEmailAddress },
        		 null, contextMap, null);
        
  
        if (emailStatus)
		{
			logger.info(ApplicationProperties
			    .getValue("reportedProblem.email.success"));
		}
		else
		{
			logger.info(ApplicationProperties
			    .getValue("reportedProblem.email.failure"));
		}
    }

	/**
     * Sends email to Administrator and CC to Scientist on successful placement of order.
     *
     * Returns true if mail is sent successfully.
	 * @param orderPlacementTempl 
	 * @param contextMap 
     * @param none
     * @return boolean indicating true/false
     */

    public boolean sendEmailForOrderPlacement(User user, OrderDetails order)
    {
    	Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("user", user);
		contextMap.put("order",order);
		contextMap.put("distributionTitle",order.getDistributionProtocol().getTitle());
		
		boolean emailStatus = EmailClient.getInstance().sendEmail(
				 Constants.ORDER_PLACEMENT_EMAIL_TMPL,
				 new String[]{ adminEmailAddress },
				 new String[]{ user.getEmailAddress() },
				 null, contextMap, order.getName());
    	return emailStatus;
    }
    /**
     * Sends email to Scientist and cc to Admin on distribution of the order.
     * @return
     */

    public boolean sendEmailForOrderDistribution(final String body,final String toEmailAddress,final String fromEmailAddress, final String ccEmailAddress, final String bccEmailAddress,  final String subject)
    {
    	final String sendFromEmailPassword = XMLPropertyHandler.getValue("email.sendEmailFrom.emailPassword");
    	final String mailServer = XMLPropertyHandler.getValue("email.mailServer");
    	final String mailServerPort = XMLPropertyHandler.getValue("email.mailServer.port");
    	final String isSMTPAuthEnabled = XMLPropertyHandler.getValue("email.smtp.auth.enabled");
 		final String isStartTLSEnabled = XMLPropertyHandler.getValue("email.smtp.starttls.enabled");
     
        final EmailDetails emailDetails= new EmailDetails();
        emailDetails.setToAddress(new String[]{toEmailAddress});
        emailDetails.setCcAddress(new String[]{ccEmailAddress});
        emailDetails.setBccAddress(new String[]{bccEmailAddress});
        emailDetails.setSubject(subject);
        emailDetails.setBody(body);
        SendEmail email;
        boolean emailStatus;
		try
		{
			email = new SendEmail(mailServer,fromEmailAddress,sendFromEmailPassword,mailServerPort,isSMTPAuthEnabled,isStartTLSEnabled);
			emailStatus=email.sendMail(emailDetails);
		}
		catch (final MessagingException messExcp)
		{
			emailStatus=false;
			logger.error(messExcp.getMessage(),messExcp);
		}
    	logger.info("EmailStatus  "  + emailStatus);
        return emailStatus;
    }

    public void sendEMPIAdminUserNotExitsEmail(){
    	Map<String, Object> contextMap = new HashMap<String, Object>();
		boolean emailStatus = EmailClient.getInstance()
				.sendEmail(
						Constants.EMPI_ADMINUSER_NOTEXISTS_EMAIL_TMPL,
						new String[] { adminEmailAddress },
						contextMap);
		 
		if (!emailStatus) {
			Logger.out.info(ApplicationProperties.getValue("empi.adminuser.notexists.email.failure") + XMLPropertyHandler.getValue(KEY_EMAIL_ADMIN_EMAIL_ADDRESS));
		}
	}
    
    public void sendEMPIAdminUserClosedEmail(User eMPIAdminUser){
    	Map<String, Object> contextMap = new HashMap<String, Object>();
    	contextMap.put("user", eMPIAdminUser);
		boolean emailStatus = EmailClient.getInstance().sendEmail(
				 Constants.EMPI_ADMINUSER_CLOSED_EMAIL_TMPL,
				 new String[]{ adminEmailAddress }, 
				 contextMap);
			
		if (!emailStatus) {
			Logger.out.info(ApplicationProperties.getValue("empi.adminuser.closed.email.failure")+XMLPropertyHandler.getValue(KEY_EMAIL_ADMIN_EMAIL_ADDRESS));
		}
	}   

	public boolean sendOrderUpdateEmail(String requestorFirstName, String requestorLastName,
			String toEmailAddress, String ccEmailAddress,String bccEmailAddress,
			String orderName, String updaterFirstName, String updaterLastName, String orderStatus, Long orderId,List<File> attachmentOrderCsv)
    {
    	Map<String, Object> contextMap = new HashMap<String, Object>();
    	contextMap.put("requestorFirstName", requestorFirstName);
    	contextMap.put("requestorLastName", requestorLastName);
    	contextMap.put("order", orderName);
    	contextMap.put("updaterFirstName", updaterFirstName);
    	contextMap.put("updaterLastName", updaterLastName);
    	contextMap.put("date", new Date().toString());
    	contextMap.put("status", orderStatus);
    	contextMap.put("url", CommonServiceLocator.getInstance().getAppURL());
    	contextMap.put("orderId",orderId.toString());
    	
		boolean emailStatus = EmailClient.getInstance().sendEmailWithAttachment(
				 Constants.ORDER_DISTRIBTION_EMAIL_TMPL,
				 new String[]{ toEmailAddress },
				 new String[]{ ccEmailAddress },
				 new String[]{ bccEmailAddress },
				 attachmentOrderCsv,contextMap,orderName);
			
		if (!emailStatus) {
			Logger.out.info(ApplicationProperties.getValue("empi.adminuser.closed.email.failure")+XMLPropertyHandler.getValue(KEY_EMAIL_ADMIN_EMAIL_ADDRESS));
		}
    	
    	
        return emailStatus;
    }
	public boolean reuestShipmentEmail(String creatorName,
			String[] toEmailAddress, String[] ccEmailAddress,String bccEmailAddress,
			String shipmentName, String siteAdmin,String siteName,Long id)
    {
    	Map<String, Object> contextMap = new HashMap<String, Object>();
    	contextMap.put("creator", creatorName);
    	contextMap.put("name", shipmentName);
    	contextMap.put("siteadmin", siteAdmin);
    	contextMap.put("date", Utility.parseDateToString(Calendar.getInstance()
				.getTime(), CommonServiceLocator.getInstance().getDatePattern()));
    	contextMap.put("siteName", siteName);
    	contextMap.put("url", CommonServiceLocator.getInstance().getAppURL());
    	contextMap.put("id", id);
    	
		boolean emailStatus = EmailClient.getInstance().sendEmail(
				 Constants.SHIPMENT_REQUEST,
				  toEmailAddress,ccEmailAddress ,
				 new String[]{ },contextMap,shipmentName);
			
		if (!emailStatus) {
			Logger.out.info(ApplicationProperties.getValue("empi.adminuser.closed.email.failure")+XMLPropertyHandler.getValue(KEY_EMAIL_ADMIN_EMAIL_ADDRESS));
		}
    	
    	
        return emailStatus;
    }
	public boolean acceptShipmentCreatedEmail(String creatorName,
			String[] toEmailAddress, String[] ccEmailAddress,String bccEmailAddress,
			String shipmentName, String siteAdmin,String requestSiteName,String senderSiteName,String activityStatus)
    {
    	Map<String, Object> contextMap = new HashMap<String, Object>();
    	contextMap.put("creator", creatorName);
    	contextMap.put("name", shipmentName);
    	contextMap.put("siteadmin", siteAdmin);
    	contextMap.put("requestSiteName", requestSiteName);
    	contextMap.put("senderSiteName", senderSiteName);
    	contextMap.put("date", Utility.parseDateToString(Calendar.getInstance()
				.getTime(), CommonServiceLocator.getInstance().getDatePattern()));
    	contextMap.put("activityStatus", activityStatus);
    	
		boolean emailStatus = EmailClient.getInstance().sendEmail(
				 Constants.SHIPMENT_ACCEPTED,
				 toEmailAddress,ccEmailAddress,
				 new String[]{},contextMap,shipmentName+" "+activityStatus);
			
		if (!emailStatus) {
			Logger.out.info(ApplicationProperties.getValue("empi.adminuser.closed.email.failure")+XMLPropertyHandler.getValue(KEY_EMAIL_ADMIN_EMAIL_ADDRESS));
		}
    	
    	
        return emailStatus;
    }
	
	public boolean createShipmentEmail(String creatorName,
			String[] toEmailAddress, String[] ccEmailAddress,String bccEmailAddress,
			String shipmentName, String siteAdmin,String requestSiteName,String senderSiteName,Date createdDate,Long id,String activityStatus)
    {
    	Map<String, Object> contextMap = new HashMap<String, Object>();
    	contextMap.put("creator", creatorName);
    	contextMap.put("name", shipmentName);
    	contextMap.put("siteadmin", siteAdmin);
    	contextMap.put("date", Utility.parseDateToString(createdDate, CommonServiceLocator.getInstance().getDatePattern()));
    	contextMap.put("requestSiteName", requestSiteName);
    	contextMap.put("senderSiteName", senderSiteName);
    	contextMap.put("url", CommonServiceLocator.getInstance().getAppURL());
    	contextMap.put("id", id);
    	contextMap.put("activityStatus", activityStatus);
		boolean emailStatus = EmailClient.getInstance().sendEmail(
				 Constants.SHIPMENT_CREATED,
				 toEmailAddress,ccEmailAddress,
				 new String[]{},contextMap,shipmentName+" "+activityStatus);
			
		if (!emailStatus) {
			Logger.out.info(ApplicationProperties.getValue("empi.adminuser.closed.email.failure")+XMLPropertyHandler.getValue(KEY_EMAIL_ADMIN_EMAIL_ADDRESS));
		}
    	
    	
        return emailStatus;
    }
}