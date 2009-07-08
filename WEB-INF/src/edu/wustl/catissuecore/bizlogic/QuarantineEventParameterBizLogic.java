
package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * This class is used to store the QuarantineEventParameter object to database
 * 
 * @author vijay_pande
 */
public class QuarantineEventParameterBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger
			.getCommonLogger(QuarantineEventParameterBizLogic.class);

	/**
	 * Saves the Pathology Report Quarantine Event Parameter object in the
	 * database.
	 * 
	 * @param obj
	 *            The storageType object to be saved.
	 * @param session
	 *            The session in which the object is saved.
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final QuarantineEventParameter quarantineParam = (QuarantineEventParameter) obj;
			final DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport = (DeidentifiedSurgicalPathologyReport) dao
					.retrieveById(DeidentifiedSurgicalPathologyReport.class.getName(),
							quarantineParam.getDeIdentifiedSurgicalPathologyReport().getId());
			deidentifiedSurgicalPathologyReport.setIsQuarantined(Constants.QUARANTINE_REQUEST);
			deidentifiedSurgicalPathologyReport.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED
					.toString());
			dao.update(deidentifiedSurgicalPathologyReport);
			String className;
			className = User.class.getName();
			final Object object = dao.retrieveById(className, sessionDataBean.getUserId());
			quarantineParam.setUser((User) object);
			dao.insert(quarantineParam);

			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			auditManager.insertAudit(dao, quarantineParam);
			// Since QuarantineEventParameter is in PUBLIC_DATA_GROUP protection
			// objects not required
			/*
			 * Set protectionObjects = new HashSet();
			 * protectionObjects.add(quarantineParam); try {
			 * SecurityManager.getInstance
			 * (this.getClass()).insertAuthorizationData(null,
			 * protectionObjects, null); } catch (SMException e) { throw
			 * handleSMException(e); }
			 */
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * Updates the persistent object in the database.
	 * 
	 * @param obj
	 *            The object to be updated.
	 * @param session
	 *            The session in which the object is saved.
	 * @throws BizLogicException
	 */
	@Override
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final QuarantineEventParameter oldquarantineParam = (QuarantineEventParameter) oldObj;
			final QuarantineEventParameter newquarantineParam = (QuarantineEventParameter) obj;
			final DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport = oldquarantineParam
					.getDeIdentifiedSurgicalPathologyReport();
			oldquarantineParam.setStatus(newquarantineParam.getStatus());
			if (oldquarantineParam.getStatus().equalsIgnoreCase(
					Constants.COMMENT_STATUS_QUARANTINED))
			{
				deidentifiedSurgicalPathologyReport.setIsQuarantined(Constants.QUARANTINE);
				deidentifiedSurgicalPathologyReport
						.setIsQuarantined(Status.ACTIVITY_STATUS_DISABLED.toString());
			}
			else
			{
				deidentifiedSurgicalPathologyReport.setIsQuarantined(Status.ACTIVITY_STATUS_ACTIVE
						.toString());
			}
			newquarantineParam.setStatus(Constants.COMMENT_STATUS_REPLIED);
			dao.update(deidentifiedSurgicalPathologyReport);
			dao.update(oldquarantineParam);
			newquarantineParam.setStatus(Constants.COMMENT_STATUS_REPLIED);
			dao.insert(newquarantineParam);
		}
		catch (final Exception ex)
		{
			this.logger.error("Error occured while updating object of QuarantineEventParameter"
					+ ex);
		}
	}
}
