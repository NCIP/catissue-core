package edu.wustl.catissuecore.util;

import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.querysuite.security.utility.IValidator;

public class CSMValidator implements IValidator {

	public boolean hasPrivilegeToView(SessionDataBean sessionDataBean, String baseObjectId,
			String privilegeName) 
	{
		boolean hasPrivilege = false;
		hasPrivilege = Utility.checkForAllCurrentAndFutureCPs(null, privilegeName, sessionDataBean, baseObjectId);
		return hasPrivilege;
	}

}
