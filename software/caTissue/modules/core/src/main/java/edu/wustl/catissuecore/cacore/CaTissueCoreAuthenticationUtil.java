package edu.wustl.catissuecore.cacore;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;

public class CaTissueCoreAuthenticationUtil {

	public static String retreiveUsernameFromSecurityContext() {
		String userName = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null) {
			Authentication auth = context.getAuthentication();
			if (auth != null) {
				Object details = auth.getDetails();
				if (details != null) {
					userName = (String) details;
				}
			}
		}
		return userName;
	}

}
