package edu.wustl.catissuecore.util;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.querysuite.QueryModuleConstants;
import edu.wustl.catissuecore.util.querysuite.TemporalColumnMetada;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.YMInterval;
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
				hasPrivilege = checkePriviliges(sessionDataBean, privilegeCache,
						cpCollection);
	        } 
			else
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

	/**
	 * method checks priviliges and returns hasPrivilege.
	 * @param sessionDataBean
	 * @param hasPrivilege
	 * @param privilegeCache
	 * @param cpCollection
	 * @return
	 */
	private boolean checkePriviliges(
			SessionDataBean sessionDataBean, 
			PrivilegeCache privilegeCache,
			Collection<CollectionProtocol> cpCollection) 
	{
		boolean hasPrivilege = false;
		for(CollectionProtocol cp : cpCollection)
		{
			if(privilegeCache.hasPrivilege(CollectionProtocol.class.getName()+"_"+cp.getId(), Permissions.REGISTRATION))
			{
				hasPrivilege = true;
				break;
			}
		}
		if(!hasPrivilege)
		{
			hasPrivilege = edu.wustl.catissuecore.util.global.Utility.checkForAllCurrentAndFutureCPs(null,Permissions.REGISTRATION, sessionDataBean, null);
		}
		return hasPrivilege;
	} 

	public boolean hasPrivilegeToViewTemporalColumn(List tqColumnMetadataList,
			List<String> row,boolean isAuthorizedUser) 
	{
		boolean removeRow = false;
		for (Object object : tqColumnMetadataList) 
		{ 
			TemporalColumnMetada tqMetadata = (TemporalColumnMetada) object;
			String ageString = row.get(tqMetadata.getColumnIndex() - 1);
			long age = 0;
			
			if (tqMetadata.getTermType().equals(TermType.Timestamp) || !isAuthorizedUser) 
			{
				row.set(tqMetadata.getColumnIndex() - 1, QueryModuleConstants.HASHED_OUT);
			} 
			else if (tqMetadata.getTermType().equals(TermType.DSInterval)) 
			{
				if(!ageString.equals(QueryModuleConstants.PHI_AGE))
				{
				    age = Long.parseLong(ageString);
				
				    if (tqMetadata.getPHIDate() != null && tqMetadata.isBirthDate()) 
					{
						java.util.Date todaysDate = new java.util.Date();
						int year = tqMetadata.getPHIDate().getDate().getYear();
						age = todaysDate.getYear()
								- year
								+ age;
					} 
					
					if (!tqMetadata.getTimeInterval().name().equals(
								YMInterval.Year.name())) 
					{
							age = Math.round((age * tqMetadata.getTimeInterval()
									.numSeconds())
									/ YMInterval.Year.numSeconds());
					}
					if (Math.abs(age) > 89)
					{
						row.set(tqMetadata.getColumnIndex() - 1, QueryModuleConstants.PHI_AGE);
					}
				}
			}
		}
		return removeRow;
	}
	
}
