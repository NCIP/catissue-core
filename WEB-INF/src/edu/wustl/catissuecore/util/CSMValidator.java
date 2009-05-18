package edu.wustl.catissuecore.util;

import java.util.Collection;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.YMInterval;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.query.util.global.AQConstants;
import edu.wustl.query.util.querysuite.TemporalColumnMetadata;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.privilege.IValidator;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;
public class CSMValidator implements IValidator {

	private transient Logger logger = Logger.getCommonLogger(CSMValidator.class);
	public boolean hasPrivilegeToView(SessionDataBean sessionDataBean, String baseObjectId,
			String privilegeName) 
	{
		boolean hasPrivilege = false;
		hasPrivilege = AppUtility.checkForAllCurrentAndFutureCPs(privilegeName, sessionDataBean, baseObjectId);
		return hasPrivilege;
	}

	public boolean hasPrivilegeToViewGlobalParticipant(SessionDataBean sessionDataBean) 
	{
		boolean hasPrivilege = false;
		DAO dao = null;
		User user = null;
		Collection<CollectionProtocol> cpCollection = null;
			try 
			{
				PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName());
				dao = DAOConfigFactory.getInstance().getDAOFactory(Constants.APPLICATION_NAME).getDAO();
				dao.openSession(sessionDataBean);
				
				user = (User) dao.retrieveById(User.class.getName(), sessionDataBean.getUserId());
				cpCollection = user.getAssignedProtocolCollection();
				
				if (cpCollection != null && !cpCollection.isEmpty())
		        {
					hasPrivilege = checkePriviliges(sessionDataBean, privilegeCache,
							cpCollection);
		        } 
				else
		        {
		        	hasPrivilege = edu.wustl.catissuecore.util.global.AppUtility.checkForAllCurrentAndFutureCPs(Permissions.REGISTRATION, sessionDataBean, null);
		        }
				dao.closeSession();
			} 
			catch (Exception e1) 
			{
				logger.debug(e1.getMessage(), e1);
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
	 * @throws SMException SM Exception.
	 */
	private boolean checkePriviliges(
			SessionDataBean sessionDataBean, 
			PrivilegeCache privilegeCache,
			Collection<CollectionProtocol> cpCollection) throws SMException 
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
			hasPrivilege = edu.wustl.catissuecore.util.global.AppUtility.checkForAllCurrentAndFutureCPs(Permissions.REGISTRATION, sessionDataBean, null);
		}
		return hasPrivilege;
	} 

	public boolean hasPrivilegeToViewTemporalColumn(List tqColumnMetadataList,
			List<String> row,boolean isAuthorizedUser) 
	{
		boolean removeRow = false;
		for (Object object : tqColumnMetadataList) 
		{ 
			TemporalColumnMetadata tqMetadata = (TemporalColumnMetadata) object;
			String ageString = row.get(tqMetadata.getColumnIndex() - 1);
			long age = 0;
			
			if (tqMetadata.getTermType().equals(TermType.Timestamp) || !isAuthorizedUser) 
			{
				row.set(tqMetadata.getColumnIndex() - 1, AQConstants.HASHED_OUT);
			} 
			else if (tqMetadata.getTermType().equals(TermType.DSInterval)) 
			{
				if(!ageString.equals(edu.wustl.query.util.global.Constants.PHI_AGE))
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
						row.set(tqMetadata.getColumnIndex() - 1, edu.wustl.query.util.global.Constants.PHI_AGE);
					}
				}
			}
		}
		return removeRow;
	}
	
}
