package edu.wustl.catissuecore.caties.util;

import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class CaCoreAPIService 
{
	private static ApplicationService appService;
	private static ClientSession cs;
	
	/**
	 * Default constructor declared as private to make is SingleTone
	 */
	private CaCoreAPIService()
	{
		
	}
	
	public static ApplicationService getAppServiceInstance()
	{
		if(appService==null)
		{
			appService=ApplicationServiceProvider.getRemoteInstance();
		}
		return appService;
	}
	
	public static void initialize() throws Exception
	{
		try
		{
			appService = ApplicationServiceProvider.getRemoteInstance();
			cs = ClientSession.getInstance();
			try
			{ 
				cs.startSession(CaTIESProperties.getValue(CaTIESConstants.USER_NAME), CaTIESProperties.getValue(CaTIESConstants.PASSWORD));
			} 
			catch (Exception ex) 
			{ 
				Logger.out.error("Error in initializing CaCoreAPIService "+ex); 
				throw ex;
			}
		}
		catch(Exception ex)
		{
			Logger.out.error("Test client throws Exception = "+ ex);
			throw ex;
		}
	}
	
	public static Object getObject(Class targertClass, String columnName, Object columnValue)
	{
		try 
		{
			List resultList = getList(targertClass, columnName, columnValue);
			if(resultList!=null && resultList.size()>0)
			{
				return resultList.get(0);
			}
		} 
		catch (Exception e) 
		{
			Logger.out.error("Error while retrieving object "+ targertClass+e);
		}
		return null;
	}
	
	public static List getList(Class targertClass, String columnName, Object columnValue)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(targertClass);
		criteria.add(Restrictions.eq(columnName, columnValue));
		try 
		{
			List resultList = CaCoreAPIService.getAppServiceInstance().query(criteria, targertClass.getName());
			return resultList;
		} 
		catch (ApplicationException e) 
		{
			Logger.out.error("Error while retrieving List for "+ targertClass+e);
		}
		return null;
	}
	
	public static Collection executeQuery(String hqlQuery, String targetClassName)
	{
		List resultList=null;
		HQLCriteria hqlCriteria = new HQLCriteria(hqlQuery); 
		
		try 
		{
			ApplicationService appService=getAppServiceInstance();
			resultList =appService.query(hqlCriteria, targetClassName);
			return resultList;
		}
		catch (ApplicationException ex) 
		{
			Logger.out.error("Error while executing query "+hqlQuery+ex);
		}
		Logger.out.info("Query result:" +resultList);
		return null;
	}
}
