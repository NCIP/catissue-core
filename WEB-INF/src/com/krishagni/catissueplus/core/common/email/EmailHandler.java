
package com.krishagni.catissueplus.core.common.email;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.de.domain.QueryFolder;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;

import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;

public class EmailHandler {

	private static String USER_CREATED_EMAIL_TMPL = "user.createdTemplate";
	
	private static String USER_SIGNUP_EMAIL_TMPL = "user.signupTemplate";

	private static String FORGOT_PASSWORD_EMAIL_TMPL = "user.forgotPasswordTemplate";
	
	private static String QUERY_DATA_EXPORTED_EMAIL_TMPL = "query.exportData";
	
	private static String SHARE_QUERY_FOLDER_EMAIL_TMPL = "query.shareQueryFolder";
	
	private static String JOB_COMPLETED_EMAIL_TMPL = "job.completed";
	
	private static String JOB_FAILED_EMAIL_TMPL = "job.failed";

	private static final String KEY_EMAIL_ADMIN_EMAIL_ADDRESS = "email.administrative.emailAddress";

	private static final String adminEmailAddress = XMLPropertyHandler.getValue(KEY_EMAIL_ADMIN_EMAIL_ADDRESS);

	public static Boolean sendUserCreatedEmail(final User user) {
		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("user", user);
		contextMap.put("appUrl", CommonServiceLocator.getInstance().getAppURL()); //TODO need remove and co-ordinate with new code
		return EmailClient.getInstance().sendEmail(USER_CREATED_EMAIL_TMPL, new String[]{user.getEmailAddress()},
				new String[]{adminEmailAddress}, null, contextMap, null);
	}
	
	public static void sendUserSignupEmail(final User user) {
		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("user", user);
		contextMap.put("appUrl", CommonServiceLocator.getInstance().getAppURL()); //TODO need remove and co-ordinate with new code
		
		EmailClient.getInstance().sendEmail(
				USER_SIGNUP_EMAIL_TMPL,
				new String[]{user.getEmailAddress()},
				new String[]{adminEmailAddress},
				null,
				contextMap, 
				null);
	}

	public static Boolean sendForgotPasswordEmail(final User user, final String token) {
		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("user", user);
		contextMap.put("token", token);
		contextMap.put("appUrl", getAppUrl()); //TODO need remove and co-ordinate with new code
		return EmailClient.getInstance().sendEmail(FORGOT_PASSWORD_EMAIL_TMPL, new String[]{user.getEmailAddress()},
				new String[]{adminEmailAddress}, null, contextMap, null);
	}
	
	public static void sendQueryDataExportedEmail(User user, SavedQuery query, String filename, Set<String> additionalRecipients) {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("user", user);
		vars.put("query", query);
		vars.put("filename", filename);
		vars.put("appUrl", CommonServiceLocator.getInstance().getAppURL());
		
		String [] recipients = new String[1 + additionalRecipients.size()];
		recipients[0] = user.getEmailAddress();
		int index = 1;
		for (String recipient : additionalRecipients) {
			recipients[index] = recipient;
			index++;
		}
		
		EmailClient.getInstance().sendEmail(
				QUERY_DATA_EXPORTED_EMAIL_TMPL, 
				recipients, 
				new String[] {adminEmailAddress}, 
				null, 
				vars, 
				query != null ? query.getTitle() : "Unsaved query");		
	}
	
	public static void sendQueryFolderSharedEmail(User user, QueryFolder folder, User sharedWith) {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("user", user);
		vars.put("folder", folder);
		vars.put("sharedWith", sharedWith);
		vars.put("appUrl", CommonServiceLocator.getInstance().getAppURL());
		
		EmailClient.getInstance().sendEmail(
				SHARE_QUERY_FOLDER_EMAIL_TMPL, 
				new String[] {sharedWith.getEmailAddress()}, 
				new String[] {adminEmailAddress}, 
				null, 
				vars, 
				folder.getName());
	}
	
	public static void sendJobCompletedEmail(User user, String filename, Set<String> additionalRecipients, String jobName, String subject, boolean isSuccessful) {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("user", user);
		vars.put("filename", filename);
		vars.put("appUrl", CommonServiceLocator.getInstance().getAppURL());
		vars.put("jobName", jobName);
		
		String [] recipients = new String[1 + additionalRecipients.size()];
		recipients[0] = user.getEmailAddress();
		int index = 1;
		for (String recipient : additionalRecipients) {
			recipients[index] = recipient;
			index++;
		}

		String template = isSuccessful ? JOB_COMPLETED_EMAIL_TMPL : JOB_FAILED_EMAIL_TMPL;
		
		EmailClient.getInstance().sendEmail(
				template, 
				recipients, 
				new String[] {adminEmailAddress}, 
				null, 
				vars, 
				subject);
	}
	
	private static String getAppUrl() {
		HttpServletRequest request=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
