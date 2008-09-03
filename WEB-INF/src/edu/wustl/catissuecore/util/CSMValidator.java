package edu.wustl.catissuecore.util;

import java.util.Collection;

import org.hibernate.Session;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.querysuite.security.utility.IValidator;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.logger.Logger;

public class CSMValidator implements IValidator {

	public boolean hasPrivilegeToView(SessionDataBean sessionDataBean, String baseObjectId,
			String privilegeName) 
	{
		boolean hasPrivilege = false;
		hasPrivilege = Utility.checkForAllCurrentAndFutureCPs(null, privilegeName, sessionDataBean, baseObjectId);
		return hasPrivilege;
	}

	public boolean hasPrivilegeToViewGlobalParticipant(SessionDataBean sessionDataBean) 
	{
		boolean hasPrivilege = false;
		Session session = null;
		PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName());
		
		User user = null;
		Collection<CollectionProtocol> cpCollection = null;
		try 
		{
			session = DBUtil.getCleanSession();
			user = (User) session.load(User.class.getName(), sessionDataBean.getUserId());
			cpCollection = user.getAssignedProtocolCollection();
			
			if (cpCollection != null && !cpCollection.isEmpty())
	        {
				for(CollectionProtocol cp : cpCollection)
				{
					if(privilegeCache.hasPrivilege(CollectionProtocol.class.getName()+"_"+cp.getId(), Permissions.REGISTRATION))
					{
						hasPrivilege = true;
						break;
					}
				}
	        } else
	        {
	        	hasPrivilege = edu.wustl.catissuecore.util.global.Utility.checkForAllCurrentAndFutureCPs(null,Permissions.REGISTRATION, sessionDataBean, null);
	        }
		} 
		catch (BizLogicException e1) 
		{
			Logger.out.debug(e1.getMessage(), e1);
		}
		finally
		{
			session.close();
		}
		
		return hasPrivilege;
	}

}
