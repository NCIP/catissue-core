package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.DAO;

public interface ICaTissueDefaultBizLogic {

	public boolean isAuthorized(DAO dao, Object domainObject,
			SessionDataBean sessionDataBean) throws BizLogicException;

	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#isAuthorized.
	 * @param dao The dao object.
	 * @param domainObject domain Object
	 * @param sessionDataBean session specific Data
	 * @return isAuthorized
	 * @throws BizLogicException User Not BizLogicException
	 * @ generic DAOException
	 */
	public boolean isAuthorized(DAO dao, Object domainObject,
			SessionDataBean sessionDataBean, Object uiObject)
			throws BizLogicException;

	/**
	 * This method is called to get actual class name.
	 * @param name name.
	 * @return String name.
	 */
	public String getActualClassName(String name);

	/**
	 * This method called to view privilege.
	 * @param objName objName
	 * @param identifier identifier
	 * @param sessionDataBean sessionDataBean
	 * @throws BizLogicException BizLogicException
	 */
	public boolean hasPrivilegeToView(String objName, Long identifier,
			SessionDataBean sessionDataBean) throws BizLogicException;

	/**
	 * This method called to refreshTitliSearchIndexMultiple.
	 * @param objCollection objCollection
	 * @param  operation operation
	 * @throws BizLogicException BizLogicException
	 */
	public void refreshTitliSearchIndexMultiple(
			Collection<AbstractDomainObject> objCollection, String operation)
			throws BizLogicException;

	/**
	 * Deletes an object from the database.
	 * @param obj The object to be deleted.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public void delete(Object obj) throws BizLogicException;

}