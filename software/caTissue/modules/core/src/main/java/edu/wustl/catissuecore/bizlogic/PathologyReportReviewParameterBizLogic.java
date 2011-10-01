
package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
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

	private transient final Logger logger = Logger
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
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final PathologyReportReviewParameter reviewParam = (PathologyReportReviewParameter) obj;

			String className;
			className = User.class.getName();
			final Object object = dao.retrieveById(className, sessionDataBean.getUserId());
			reviewParam.setUser((User) object);
			String reviewerRole;
			final ISecurityManager securityManager = SecurityManagerFactory.getSecurityManager();

			final Role role = securityManager.getUserRole(new Long(sessionDataBean.getCsmUserId())
					.longValue());
			reviewerRole = role.getName();
			reviewParam.setReviewerRole(reviewerRole);

			dao.insert(reviewParam);

		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final SMException ex)
		{
			this.logger.error("Review Role not found!"+ex.getMessage(),ex);
			ex.printStackTrace();
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
	@Override
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
	{
		try
		{
			final PathologyReportReviewParameter oldRevParam = (PathologyReportReviewParameter) oldObj;
			final PathologyReportReviewParameter oldreviewParam = (PathologyReportReviewParameter) oldObj;
			final PathologyReportReviewParameter newreviewParam = (PathologyReportReviewParameter) obj;
			oldreviewParam.setStatus(Constants.COMMENT_STATUS_REVIEWED);
			dao.update(oldreviewParam,oldRevParam);
			newreviewParam.setStatus(Constants.COMMENT_STATUS_REPLIED);
			dao.insert(newreviewParam);
		}
		catch (final Exception ex)
		{
			this.logger
					.error("Error occured while updating object of PathologyReportReviewParameter"
							+ ex.getMessage(),ex);
			ex.printStackTrace();
		}
	}
}
