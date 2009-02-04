package edu.wustl.catissuecore.bizlogic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


/**
 * This class is used to store the QuarantineEventParameter object to database
 * @author vijay_pande
 * 
 */
public class QuarantineEventParameterBizLogic extends IntegrationBizLogic
{
	/**
	 * Saves the Pathology Report Quarantine Event Parameter object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 * @throws UserNotAuthorizedException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		QuarantineEventParameter quarantineParam = (QuarantineEventParameter) obj;
		DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport = quarantineParam.getDeidentifiedSurgicalPathologyReport(); 
		deidentifiedSurgicalPathologyReport.setIsQuanrantined(Constants.QUARANTINE_REQUEST);
		dao.update(deidentifiedSurgicalPathologyReport, sessionDataBean, true, false, false);
		String className;
		String colName=new String(Constants.SYSTEM_IDENTIFIER);
		className=User.class.getName();
		List userList=dao.retrieve(className, colName, sessionDataBean.getUserId());
		quarantineParam.setUser((User)userList.get(0));
		dao.insert(quarantineParam, sessionDataBean, true, true);
		Set protectionObjects = new HashSet();
		protectionObjects.add(quarantineParam);
		try
		{
			SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, null);
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		try
		{
			QuarantineEventParameter oldquarantineParam = (QuarantineEventParameter) oldObj;
			QuarantineEventParameter newquarantineParam = (QuarantineEventParameter) obj;
			if(newquarantineParam.getUser().getId()==null){
				dao.insert(newquarantineParam.getUser(), sessionDataBean, false, false);	
			}
			DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport = oldquarantineParam.getDeidentifiedSurgicalPathologyReport();
			oldquarantineParam.setStatus(newquarantineParam.getStatus());
			if(oldquarantineParam.getStatus().equalsIgnoreCase(Constants.COMMENT_STATUS_QUARANTINED))
			{
				deidentifiedSurgicalPathologyReport.setIsQuanrantined(Constants.QUARANTINE);
			}
			else
			{
				deidentifiedSurgicalPathologyReport.setIsQuanrantined(Constants.ACTIVITY_STATUS_ACTIVE);
			}
			dao.update(deidentifiedSurgicalPathologyReport, sessionDataBean, true, false, false);
			dao.update(oldquarantineParam, sessionDataBean, true, false, false);
			newquarantineParam.setStatus(Constants.COMMENT_STATUS_REPLIED);
			dao.insert(newquarantineParam, sessionDataBean, false, false);
		}
		catch(Exception ex)
		{
			Logger.out.error("Error occured while updating object of QuarantineEventParameter"+ex);
		}
	}
}
