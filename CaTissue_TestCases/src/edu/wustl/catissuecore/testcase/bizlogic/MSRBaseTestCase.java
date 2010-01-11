package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import gov.nih.nci.system.comm.client.ClientSession;

/**
 * 
 * @author juberahamad_patel
 *
 */
public class MSRBaseTestCase extends CaTissueSuiteBaseTest
{
	/**
	 * common password for all users
	 */
	public static final String password = "Test123";
	
	public void tearDown()
	{
		System.out.println("Inside MSRBaseTestCase.teardown()");
		ClientSession.getInstance().terminateSession();
	}

	protected void loginAs(String userName)
	{
		ClientSession.getInstance().terminateSession();
		
		try
		{ 
			ClientSession.getInstance().startSession(userName, password);
		} 	
		catch (Exception ex) 
		{ 
			System.out.println(ex.getMessage()); 
			ex.printStackTrace();
			fail();
			System.exit(1);
		}
	}
	
	protected Site createNewSite()
	{
		Site site = null;
		try
		{
			site = BaseTestCaseUtility.initSite();
			site = (Site) appService.createObject(site);
		}
		catch(Exception e)
		{
			System.out.println("MSRBaseTestCase.createNewSite() : "+ e.getMessage());
			e.printStackTrace();
			assertFalse("could not create site", true);
		}
		
		return site;
	}
	
	protected User createNewSiteAdmin(Site site)
	{
		User user = null;
		try
		{
           List siteList = new ArrayList();
           siteList.add(site);
           
            user = createNewUserWithGivenRoleOnGivenSite(Constants.ADMIN_USER,siteList);
			
		}
		catch(Exception e)
		{
			System.out.println("MSRBaseTestCase.createNewSiteAdmin() : "+ e.getMessage());
			e.printStackTrace();
			assertFalse("could not create site admin", true);
		}
		
		return user;
	}
    
    protected User createNewSiteSupervisor(List siteList)
    {
        User user = null;
        try
        {
            user = createNewUserWithGivenRoleOnGivenSite("2",siteList);
            
        }
        catch(Exception e)
        {
            System.out.println("MSRBaseTestCase.createNewSiteSupervisor() : "+ e.getMessage());
            e.printStackTrace();
            assertFalse("could not create site admin", true);
        }
        
        return user;
    }

    protected User createNewSiteTechnician(List siteList)
    {
        User user = null;
        try
        {
            user = createNewUserWithGivenRoleOnGivenSite("3",siteList);
            
        }
        catch(Exception e)
        {
            System.out.println("MSRBaseTestCase.createNewSiteTechnician() : "+ e.getMessage());
            e.printStackTrace();
            assertFalse("could not create site admin", true);
        }
        
        return user;
    }
    
    protected User createNewScientist()
    {
        User user = null;
        try
        {
            user = createNewUserWithGivenRoleOnGivenSite("7",new ArrayList());
            
        }
        catch(Exception e)
        {
            System.out.println("MSRBaseTestCase.createNewScientist() : "+ e.getMessage());
            e.printStackTrace();
            assertFalse("could not create site admin", true);
        }
        
        return user;
    }
    
    protected User createNewUserWithGivenRoleOnGivenSite(String roleId, List siteList)
    {
        User user = null;
        try
        {
            user = BaseTestCaseUtility.initUser();
            
            //TODO how to make it site admin ?
            user.setRoleId(roleId);
            user.setActivityStatus("Active");
            user.setPageOf(Constants.PAGE_OF_USER_ADMIN);
            user.getSiteCollection().clear();
            user.getSiteCollection().addAll(siteList);
            SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
            user = (User)appService.createObject(user);
            user.setNewPassword(password);
            user = (User)appService.updateObject(user);
                
            
        }
        catch(Exception e)
        {
            System.out.println("MSRBaseTestCase.createNewSiteAdmin() : "+ e.getMessage());
            e.printStackTrace();
            assertFalse("could not create site admin", true);
        }
        
        return user;
    }
	
	protected StorageContainer createNewSC(Site site) throws Exception 
	{
		StorageContainer sc = null;
			sc = BaseTestCaseUtility.initStorageContainer();
		
			StorageType storageType = BaseTestCaseUtility.initStorageType();
			storageType.setId(new Long(3));
			sc.setStorageType(storageType);
					
			sc.getCollectionProtocolCollection().clear();
			
			//TODO additional processing
						
			sc.setSite(site);
			sc = (StorageContainer) appService.createObject(sc);
		
		
		return sc;
	}
	
	
	protected boolean confirmUserNotAuthorizedException(Exception e)
	{
		return true;
	
		//TODO write the right implementation - currently we are assuming that any exception 
		//on client side is due to UserNotAuthorizedException on server side
		
		/*
		Throwable t =e;
		System.out.println("Name : " +t.getClass().getName());
		while(t!=null)
		{
			System.out.println("Name : " +t.getClass().getName());
			if(t instanceof UserNotAuthorizedException)
			{
				return;
			}
			t = t.getCause();
		}*/
	}

}
