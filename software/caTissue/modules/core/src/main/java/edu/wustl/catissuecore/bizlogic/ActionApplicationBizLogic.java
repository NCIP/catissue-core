
package edu.wustl.catissuecore.bizlogic;

import edu.common.dynamicextensions.domain.integration.AbstractFormContext;
import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

public class ActionApplicationBizLogic extends CatissueDefaultBizLogic
{

	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final ActionApplication actionApplication = (ActionApplication) obj;
			dao.insert(actionApplication);
		}
		catch (final DAOException daoExp)
		{
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	@Override
	protected void insert(Object obj, DAO dao) throws BizLogicException
	{
		try
		{
			dao.insert(obj);
			dao.commit();
		}
		catch (DAOException daoEx)
		{
			throw getBizLogicException(daoEx, daoEx.getErrorKeyName(), daoEx.getMsgValues());
		}
	}

	/**
	 * Insert action application record entry.
	 *
	 * @param formContext the form context
	 *
	 * @return the action application record entry
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	public ActionApplicationRecordEntry insertActionApplicationRecordEntry(
			AbstractFormContext formContext) throws BizLogicException
	{
		ActionApplicationRecordEntry actionAppRecordEntry = new ActionApplicationRecordEntry();
		actionAppRecordEntry.setFormContext(formContext);
		actionAppRecordEntry.setActivityStatus("Active");
		insert(actionAppRecordEntry);
		return actionAppRecordEntry;
	}
}
