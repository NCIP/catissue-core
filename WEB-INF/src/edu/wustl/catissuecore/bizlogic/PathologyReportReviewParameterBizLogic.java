
package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.ISecurityManager;
import edu.wustl.security.manager.SecurityManagerFactory;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * This class is used to store the PathologyReportReviewParameter object to
 * database
 *
 * @author vijay_pande
 */
public class PathologyReportReviewParameterBizLogic extends CatissueDefaultBizLogic
{

	private transient Logger logger = Logger
			.getCommonLogger(PathologyReportReviewParameterBizLogic.class);

	/**
	 * Saves the Pathology Report Review Parameter object in the database.
	 * @param dao : dao
	 * @param obj
	 *            The storageType object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			PathologyReportReviewParameter reviewParam = (PathologyReportReviewParameter) obj;

			String className;
			className = User.class.getName();
			Object object = dao.retrieveById(className, sessionDataBean.getUserId());
			reviewParam.setUser((User) object);
			String reviewerRole;
			ISecurityManager securityManager = SecurityManagerFactory.getSecurityManager();

			Role role = securityManager.getUserRole(new Long(sessionDataBean.getCsmUserId())
					.longValue());
			reviewerRole = role.getName();
			reviewParam.setReviewerRole(reviewerRole);

			dao.insert(reviewParam);
			AuditManager auditManager = getAuditManager(sessionDataBean);
			auditManager.insertAudit(dao, reviewParam);

		}
		catch (DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (SMException ex)
		{
			logger.info("Review Role not found!");
		}
		// Since PathologyReportReviewParameter is in PUBLIC_DATA_GROUP
		// protection objects not required
		/*
		 * Set protectionObjects = new HashSet();
		 * protectionObjects.add(reviewParam); try {
		 * SecurityManager.getInstance(
		 * this.getClass()).insertAuthorizationData(null, protectionObjects,
		 * null); } catch (SMException e) { throw handleSMException(e); }
		 */catch (AuditException e)
		{
			logger.debug(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * Updates the persistent object in the database.
	 * @param dao : dao
	 * @param oldObj : oldObj
	 * @param obj
	 *            The object to be updated.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
	{
		try
		{
			PathologyReportReviewParameter oldreviewParam = (PathologyReportReviewParameter) oldObj;
			PathologyReportReviewParameter newreviewParam = (PathologyReportReviewParameter) obj;
			oldreviewParam.setStatus(Constants.COMMENT_STATUS_REVIEWED);
			dao.update(oldreviewParam);
			newreviewParam.setStatus(Constants.COMMENT_STATUS_REPLIED);
			dao.insert(newreviewParam);
		}
		catch (Exception ex)
		{
			logger.error("Error occured while updating object of PathologyReportReviewParameter"
					+ ex);
		}
	}
}
