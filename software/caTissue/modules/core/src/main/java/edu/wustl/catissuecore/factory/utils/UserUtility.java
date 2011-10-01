package edu.wustl.catissuecore.factory.utils;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.SecurityManagerFactory;
import gov.nih.nci.security.authorization.domainobjects.Role;

public class UserUtility {

	public static String getRoleId(User user) {
		String roleId = null;
		if (user != null) {
			Long userId = user.getId();
			Long csmUserId = user.getCsmUserId();
			try {
				if ((userId != null && userId != 0) && csmUserId != null) {
					final Role role = SecurityManagerFactory
							.getSecurityManager().getUserRole(csmUserId);
					if (role != null && role.getId() != null) {
						roleId = role.getId().toString();
					}
				}
			} catch (final SMException e) {
				e.printStackTrace();
			}
		}
		return roleId;
	}

	public static void setRoleId(String roleId) {
		if (roleId != null && roleId.equalsIgnoreCase("-1")) {
			roleId = Constants.NON_ADMIN_USER;
		} else if (roleId != null
				&& roleId.equalsIgnoreCase(Constants.ADMIN_USER)) {
			roleId = Constants.SUPER_ADMIN_USER;
		}
	}

}
