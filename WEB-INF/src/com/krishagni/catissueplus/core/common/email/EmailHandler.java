
package com.krishagni.catissueplus.core.common.email;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.events.UserDetails;

import edu.wustl.common.util.XMLPropertyHandler;

public class EmailHandler {

	private static String USER_CREATED_EMAIL_TMPL = "user.createdTemplate";

	private static String USER_UPDATED_EMAIL_TMPL = "user.updatedTemplate";

	private static final String KEY_EMAIL_ADMIN_EMAIL_ADDRESS = "email.administrative.emailAddress";

	private static final String adminEmailAddress = XMLPropertyHandler.getValue(KEY_EMAIL_ADMIN_EMAIL_ADDRESS);

	public static void sendUserCreatedEmail(final UserDetails user) {
		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("user", user);
		boolean emailStatus = EmailClient.getInstance().sendEmail(USER_CREATED_EMAIL_TMPL,
				new String[]{user.getEmailAddress()}, new String[]{adminEmailAddress}, null, contextMap, null);
	}

	public static void sendUserUpdatedEmail(final UserDetails user) {
		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("user", user);
		boolean emailStatus = EmailClient.getInstance().sendEmail(USER_UPDATED_EMAIL_TMPL,
				new String[]{user.getEmailAddress()}, new String[]{adminEmailAddress}, null, contextMap, null);
	}

}
