package edu.wustl.catissuecore.bizlogic;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * Used to store identified pathology report to the database 
 *
 */

public class IdentifiedSurgicalPathologyReportBizLogic  extends IntegrationBizLogic
{
	
		/**
		 * Saves the Identified pathology reportobject in the database.
		 * @param obj The storageType object to be saved.
		 * @param session The session in which the object is saved.
		 * @throws DAOException 
		 */
		protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
		{
			IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) obj;
			dao.insert(report, sessionDataBean, true, true);
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
				IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) obj;
				if(report.getTextContent().getId()==null){
					dao.insert(report.getTextContent(), sessionDataBean, false, false);	
				}
				dao.update(report, sessionDataBean, true, false, false);
				
			}catch(Exception ex){
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
			List reportList = bizLogic.retrieve(sourceObjectName, Constants.ID, identifier);
			IdentifiedSurgicalPathologyReport report = (IdentifiedSurgicalPathologyReport) reportList.get(0);
			return report;

		}
	}

