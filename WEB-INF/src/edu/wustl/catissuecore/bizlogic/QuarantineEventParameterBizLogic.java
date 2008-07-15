package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


/**
 * This class is used to store the QuarantineEventParameter object to database
 * @author vijay_pande
 * 
 */
public class QuarantineEventParameterBizLogic extends DefaultBizLogic
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
		DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport =(DeidentifiedSurgicalPathologyReport)dao.retrieve(DeidentifiedSurgicalPathologyReport.class.getName(), quarantineParam.getDeIdentifiedSurgicalPathologyReport().getId()); 
		deidentifiedSurgicalPathologyReport.setIsQuarantined(Constants.QUARANTINE_REQUEST);
		deidentifiedSurgicalPathologyReport.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
		dao.update(deidentifiedSurgicalPathologyReport, sessionDataBean, true, false, false);
		String className;
		className=User.class.getName();
		Object object = dao.retrieve(className, sessionDataBean.getUserId());
		quarantineParam.setUser((User)object);
		dao.insert(quarantineParam, sessionDataBean, true, false);
		
		// Since  QuarantineEventParameter is in PUBLIC_DATA_GROUP protection objects not required
		/*
		Set protectionObjects = new HashSet();
		protectionObjects.add(quarantineParam);
		try
		{
			SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, null);
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}*/
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
			DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport = oldquarantineParam.getDeIdentifiedSurgicalPathologyReport();
			oldquarantineParam.setStatus(newquarantineParam.getStatus());
			if(oldquarantineParam.getStatus().equalsIgnoreCase(Constants.COMMENT_STATUS_QUARANTINED))
			{
				deidentifiedSurgicalPathologyReport.setIsQuarantined(Constants.QUARANTINE);
				deidentifiedSurgicalPathologyReport.setIsQuarantined(Constants.ACTIVITY_STATUS_DISABLED);
			}
			else
			{
				deidentifiedSurgicalPathologyReport.setIsQuarantined(Constants.ACTIVITY_STATUS_ACTIVE);
			}
			newquarantineParam.setStatus(Constants.COMMENT_STATUS_REPLIED);
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
