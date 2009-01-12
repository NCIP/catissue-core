package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * This class is used to store the PathologyReportReviewParameter object to database
 * @author vijay_pande
 * 
 */
public class PathologyReportReviewParameterBizLogic extends DefaultBizLogic
{
	/**
	 * Saves the Pathology Report Review Parameter object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		PathologyReportReviewParameter reviewParam = (PathologyReportReviewParameter) obj;
		
		String className;
		className=User.class.getName();
		Object object = dao.retrieve(className, sessionDataBean.getUserId());
		reviewParam.setUser((User)object);
		String reviewerRole;
		SecurityManager securityManager=SecurityManager.getInstance(this.getClass());
		try
		{
			Role role=securityManager.getUserRole(new Long(sessionDataBean.getCsmUserId()).longValue());
			reviewerRole=role.getName();
			reviewParam.setReviewerRole(reviewerRole);
		}
		catch(SMException ex)
		{
			Logger.out.info("Review Role not found!");
		}
		dao.insert(reviewParam, sessionDataBean, true, false);
		
		// Since  PathologyReportReviewParameter is in PUBLIC_DATA_GROUP protection objects not required
		/*Set protectionObjects = new HashSet();
		protectionObjects.add(reviewParam);
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
	 * @throws Exception 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) 
	{
		try
		{
			PathologyReportReviewParameter oldreviewParam = (PathologyReportReviewParameter) oldObj;
			PathologyReportReviewParameter newreviewParam = (PathologyReportReviewParameter) obj;
			oldreviewParam.setStatus(Constants.COMMENT_STATUS_REVIEWED);
			dao.update(oldreviewParam, sessionDataBean, true, false, false);
			newreviewParam.setStatus(Constants.COMMENT_STATUS_REPLIED);
			dao.insert(newreviewParam, sessionDataBean, false, false);
		}
		catch(Exception ex)
		{
			Logger.out.error("Error occured while updating object of PathologyReportReviewParameter"+ex);
		}
	}
}
