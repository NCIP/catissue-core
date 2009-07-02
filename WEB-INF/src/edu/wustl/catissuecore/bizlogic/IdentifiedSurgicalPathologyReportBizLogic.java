
package edu.wustl.catissuecore.bizlogic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.global.Permissions;

/**
 * Used to store identified pathology report to the database
 */

public class IdentifiedSurgicalPathologyReportBizLogic extends CatissueDefaultBizLogic
{

	private transient Logger logger = Logger
			.getCommonLogger(IdentifiedSurgicalPathologyReportBizLogic.class);

	/**
	 * Saves the Identified pathology reportobject in the database.
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
			IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) obj;
			dao.insert(report);
			Set protectionObjects = new HashSet();
			protectionObjects.add(report);
		}
		catch (DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	/**
	 * Method to get dynamicGroup for given Report
	 * @param obj
	 *            IdentifiedSurgicalPathologyReport object
	 * @return Array of dynamicGroup
	 * @throws SMException
	 *             Security manager exception
	 * @throws DAOException
	 */
	/*
	 * private String[] getDynamicGroups(DAO dao, AbstractDomainObject obj)
	 * throws ApplicationException, ClassNotFoundException {
	 * IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport=
	 * (IdentifiedSurgicalPathologyReport)obj; CollectionProtocolRegistration
	 * collectionProtocolRegistration
	 * =identifiedSurgicalPathologyReport.getSpecimenCollectionGroup
	 * ().getCollectionProtocolRegistration(); String[] dynamicGroups = new
	 * String[1]; dynamicGroups[0] =
	 * SecurityManagerFactory.getSecurityManager().getProtectionGroupByName(
	 * collectionProtocolRegistration,
	 * CSMGroupLocator.getInstance().getPGName(null
	 * ,Class.forName("edu.wustl.catissuecore.domain.CollectionProtocol")));
	 * Logger.out.debug("Dynamic Group name: " + dynamicGroups[0]); return
	 * dynamicGroups; }
	 */

	/**
	 * Updates the persistent object in the database.
	 * @param dao : dao
	 * @param oldObj : oldObj
	 * @param obj
	 *            The object to be updated.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) obj;
			if (report.getTextContent().getId() == null)
			{
				dao.insert(report.getTextContent());
			}
			dao.update(report);

		}
		catch (Exception ex)
		{
			logger
					.error("Error occured while updating IdentifiedSurgicalPathologyReport domain object"
							+ ex);
		}
	}

	/**
	 * @return a collection of all identified reports
	 * @throws Exception : Exception
	 */
	public Map getAllIdentifiedReports() throws Exception
	{
		// Initialising instance of IBizLogic
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String sourceObjectName = IdentifiedSurgicalPathologyReport.class.getName();

		// getting all the Identified reports from the database
		List listOfReports = bizLogic.retrieve(sourceObjectName);
		Map mapOfReports = new HashMap();
		for (int i = 0; i < listOfReports.size(); i++)
		{
			IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) listOfReports
					.get(i);
			mapOfReports.put(report.getId(), report);
		}
		return mapOfReports;

	}

	/**
	 * This function takes identifier as parameter and returns corresponding
	 * IdentifiedSurgicalPathologyReport
	 * @param identifier
	 *            system generated unique id for report
	 * @return Identified pathology report of given identifier
	 * @throws Exception
	 *             Generic exception
	 */
	public IdentifiedSurgicalPathologyReport getReportById(Long identifier) throws Exception
	{
		// Initialising instance of IBizLogic
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String sourceObjectName = IdentifiedSurgicalPathologyReport.class.getName();

		// getting all the identified reports from the database
		Object object = bizLogic.retrieve(sourceObjectName, identifier);
		IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) object;
		return report;

	}

	@Override
	public boolean isReadDeniedTobeChecked()
	{
		return true;
	}

	@Override
	public String getReadDeniedPrivilegeName()
	{
		return Permissions.REGISTRATION + "," + Permissions.READ_DENIED;
	}
    /**
     * @param objName : objName
     * @param sessionDataBean : sessionDataBean
     * @param identifier : identifier
     * @return boolean
     */
	public boolean hasPrivilegeToView(String objName, Long identifier,
			SessionDataBean sessionDataBean)
	{
		return AppUtility.hasPrivilegeToView(objName, identifier, sessionDataBean,
				getReadDeniedPrivilegeName());
	}
}
