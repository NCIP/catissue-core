package edu.wustl.catissuecore.caties.util;

import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

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
	
	public static void initialize()
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
				System.out.println(ex.getMessage()); 
				ex.printStackTrace();
				return;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Logger.out.error("Test client throws Exception = "+ ex);
		}
	}
}
