package edu.wustl.catissuecore.bizlogic;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.locator.CSMGroupLocator;
import edu.wustl.security.manager.SecurityManagerFactory;

/**
 * Used to store identified pathology report to the database 
 *
 */

public class IdentifiedSurgicalPathologyReportBizLogic  extends CatissueDefaultBizLogic
{
	
	/**
	 * Saves the Identified pathology reportobject in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) obj;
			dao.insert(report, false);
			Set protectionObjects = new HashSet();
			protectionObjects.add(report);
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
		
	}

	/**
	 * Method to get dynamicGroup for given Report
	 * @param obj IdentifiedSurgicalPathologyReport object
	 * @return Array of dynamicGroup
	 * @throws SMException Security manager exception
	 * @throws DAOException 
	 *//*
	private String[] getDynamicGroups(DAO dao, AbstractDomainObject obj) throws ApplicationException, ClassNotFoundException
	{
		IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport= (IdentifiedSurgicalPathologyReport)obj;
		CollectionProtocolRegistration collectionProtocolRegistration=identifiedSurgicalPathologyReport.getSpecimenCollectionGroup().getCollectionProtocolRegistration();
		String[] dynamicGroups = new String[1];

		dynamicGroups[0] = SecurityManagerFactory.getSecurityManager().getProtectionGroupByName(
				collectionProtocolRegistration, CSMGroupLocator.getInstance().getPGName(null,Class.forName("edu.wustl.catissuecore.domain.CollectionProtocol")));
		Logger.out.debug("Dynamic Group name: " + dynamicGroups[0]);
		return dynamicGroups;
	}*/
	
	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) obj;
			if(report.getTextContent().getId()==null)
			{
				dao.insert(report.getTextContent(), false);	
			}
			dao.update(report);
			
		}
		catch(Exception ex)
		{
			Logger.out.error("Error occured while updating IdentifiedSurgicalPathologyReport domain object"+ex);
		}
	}

	/**
	 * @return a collection of all identified reports
	 * @throws Exception
	 */
	public Map getAllIdentifiedReports() throws Exception
	{
		// Initialising instance of IBizLogic
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String sourceObjectName = IdentifiedSurgicalPathologyReport.class.getName();

		// getting all the Identified reports from the database 
		List listOfReports = bizLogic.retrieve(sourceObjectName);
		Map mapOfReports = new HashMap();
		for (int i = 0; i < listOfReports.size(); i++)
		{
			IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) listOfReports.get(i);
			mapOfReports.put(report.getId(), report);
		}
		return  mapOfReports;

	}

	/**
	 * This function takes identifier as parameter and returns corresponding IdentifiedSurgicalPathologyReport
	 * @param identifier system generated unique id for report
	 * @return Identified pathology report of given identifier
	 * @throws Exception Generic exception
	 */
	public IdentifiedSurgicalPathologyReport getReportById(Long identifier) throws Exception
	{
		// Initialising instance of IBizLogic
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String sourceObjectName = IdentifiedSurgicalPathologyReport.class.getName();

		// getting all the identified reports from the database 
		Object object = bizLogic.retrieve(sourceObjectName, identifier);
		IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) object;
		return report;

	}
	
	@Override
	public boolean isReadDeniedTobeChecked() {
		return true;
	}
	
	@Override
	public String getReadDeniedPrivilegeName()
	{
		return Permissions.REGISTRATION+","+Permissions.READ_DENIED;
	}
	
	public boolean hasPrivilegeToView(String objName, Long identifier, SessionDataBean sessionDataBean)
	{
		return AppUtility.hasPrivilegeToView(objName, identifier, sessionDataBean, getReadDeniedPrivilegeName());
	}
}

