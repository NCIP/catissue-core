package edu.wustl.catissuecore.bizlogic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * Used to store deidentified pathology report to the database 
 *
 */
public class DeidentifiedSurgicalPathologyReportBizLogic extends IntegrationBizLogic
{	
	/**
	 * Saves the Deidentified pathology reportobject in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		try{
		DeidentifiedSurgicalPathologyReport report = (DeidentifiedSurgicalPathologyReport) obj;
		dao.insert(report, sessionDataBean, true, false);
		Set protectionObjects = new HashSet();
		protectionObjects.add(report);
		try
		{
			SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, null);
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		try{
			DeidentifiedSurgicalPathologyReport report = (DeidentifiedSurgicalPathologyReport) obj;
			if(report.getTextContent().getId()==null){
				dao.insert(report.getTextContent(), sessionDataBean, false, false);	
			}
			dao.update(report, sessionDataBean, true, false, false);

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * @return a collection of all deidentified reports
	 * @throws Exception
	 */
	public Map getAllIdentifiedReports() throws Exception
	{
		// Initialising instance of IBizLogic
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String sourceObjectName = DeidentifiedSurgicalPathologyReport.class.getName();

		// getting all the Deidentified reports from the database 
		List listOfReports = bizLogic.retrieve(sourceObjectName);
		Map mapOfReports = new HashMap();
		for (int i = 0; i < listOfReports.size(); i++)
		{
			DeidentifiedSurgicalPathologyReport report = (DeidentifiedSurgicalPathologyReport) listOfReports.get(i);
			mapOfReports.put(report.getId(), report);
		}
		return  mapOfReports;
	}
	

	/**
	 * This function takes identifier as parameter and returns corresponding DeidentifiedSurgicalPathologyReport
	 * @param identifier system generated unique id for report
	 * @return Deidentified pathology report of given identifier
	 * @throws Exception
	 */
	public DeidentifiedSurgicalPathologyReport getReportById(Long identifier) throws Exception
	{
		// Initialising instance of IBizLogic
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String sourceObjectName = DeidentifiedSurgicalPathologyReport.class.getName();

		// getting all the deidentified reports from the database 
		List reportList = bizLogic.retrieve(sourceObjectName, Constants.ID, identifier);
		DeidentifiedSurgicalPathologyReport report = (DeidentifiedSurgicalPathologyReport) reportList.get(0);
		return report;

	}
	
}
