/*
 * Created on May 9, 2005 TODO To change the template for this generated file go
 * to Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * @author gautam_shetty TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class ReportedProblemBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger.getCommonLogger(ReportedProblemBizLogic.class);

	/**
	 * (non-Javadoc)
	 * @see edu.wustl.common.dao.HibernateDAO#add(java.lang.Object)
	 * @param obj : obj
	 * @param dao : dao
	 * @param sessionDataBean : sessionDataBean
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final ReportedProblem reportedProblem = (ReportedProblem) obj;

			/**
			 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of
			 * Api Search, previoulsy it was failing since there was default
			 * class level initialization on domain object. For example in User
			 * object, it was initialized as protected String lastName=""; So we
			 * removed default class level initialization on domain object and
			 * are initializing in method setAllValues() of domain object. But
			 * in case of Api Search, default values will not get set since
			 * setAllValues() method of domainObject will not get called. To
			 * avoid null pointer exception, we are setting the default values
			 * same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setReportedProblemDefault(reportedProblem);
			// End:- Change for API Search

			dao.insert(obj);
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			auditManager.insertAudit(dao, obj);

			// Send the reported problem to the administrator and the user who
			// reported it.
			final EmailHandler emailHandler = new EmailHandler();
			emailHandler.sendReportedProblemEmail(reportedProblem);
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException auditExp)
		{
			this.logger.error(auditExp.getMessage(), auditExp);
			auditExp.printStackTrace();
			throw this.getBizLogicException(auditExp, auditExp.getErrorKeyName(), auditExp
					.getMsgValues());
		}
	}

	/**
	 * (non-Javadoc)
	 * @see IBizLogic#update(java.lang.Object)
	 * @param dao : dao
	 * @param obj : obj
	 * @param oldObj : oldObj
	 * @param sessionDataBean : sessionDataBean
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final ReportedProblem reportedProblem = (ReportedProblem) obj;

			/**
			 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of
			 * Api Search, previoulsy it was failing since there was default
			 * class level initialization on domain object. For example in User
			 * object, it was initialized as protected String lastName=""; So we
			 * removed default class level initialization on domain object and
			 * are initializing in method setAllValues() of domain object. But
			 * in case of Api Search, default values will not get set since
			 * setAllValues() method of domainObject will not get called. To
			 * avoid null pointer exception, we are setting the default values
			 * same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setReportedProblemDefault(reportedProblem);
			// End:- Change for API Search

			dao.update(obj);

			// Called audit manager to audit updates. 
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			auditManager.updateAudit(dao, obj, oldObj);
		}
		catch (final AuditException auditException)
		{
			this.logger.error(auditException.getMessage(), auditException);
			auditException.printStackTrace();
			throw this.getBizLogicException(auditException, auditException.getErrorKeyName(),
					auditException.getMsgValues());
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}
}