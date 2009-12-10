package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class SiteBizTestCases extends CaTissueSuiteBaseTest {

	AbstractDomainObject domainObject = null;
 	public void testAddSite()
	{
		try{
			Site site= BaseTestCaseUtility.initSite();			
			System.out.println(site);
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			site = (Site) appService.createObject(site,bean); 
			TestCaseUtility.setObjectMap(site, Site.class);
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testSearchSite()
	{
		Site site = new Site();
    	Logger.out.info(" searching domain object");
    	site.setId(new Long(1));
   
         try {
        	 String query = "from edu.wustl.catissuecore.domain.Site as site where "
 				+ "site.id= 1";	
        	 List resultList = appService.search(query);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Site returnedSite = (Site) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedSite.getName());
        		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
             }
          } 
          catch (Exception e) {
           	Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Does not find Domain Object", true);
	 		
          }
	}
	
	public void testUpdateSite()
	{
		Site site =  BaseTestCaseUtility.initSite();
    	Logger.out.info("updating domain object------->"+site);
	    try 
		{
	    	SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	    	site = (Site) appService.createObject(site,bean);
	    	BaseTestCaseUtility.updateSite(site);	
	    	Site updatedSite = (Site) appService.updateObject(site,bean);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedSite);
	       	assertTrue("Domain object successfully updated ---->"+updatedSite, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
	public void testWithEmptySiteName()
	{
		try{
			Site site = BaseTestCaseUtility.initSite();		
			//te.setId(new Long("1"));
			site.setName("");
			System.out.println(site);
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			site = (Site) appService.createObject(site,bean); 
			assertFalse("Empty site name should thorw Exception", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Name is required", true);
		 }
	}
	
	public void testWithDuplicateSiteName()
	{
		try{
			Site site = BaseTestCaseUtility.initSite();	
			Site dupSiteName = BaseTestCaseUtility.initSite();
			dupSiteName.setName(site.getName());
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			site = (Site) appService.createObject(site, bean); 
			dupSiteName = (Site) appService.createObject(dupSiteName,bean); 
			assertFalse("Test Failed. Duplicate site name should throw exception", true);
		}
		 catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Submission failed since a Site with the same NAME already exists" , true);
			 
		 }
	}
	
	public void testWithInvalidSiteType()
	{
		try{
			Site site = BaseTestCaseUtility.initSite();		
			//te.setId(new Long("1"));
			site.setType("xyz");
			System.out.println(site);
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			site = (Site) appService.createObject(site, bean); 
			assertFalse("Invalid site type should thorw Exception", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Type is wrong", true);
		 }
	}
	
	public void testWithInvalidSiteActivityStatus()
	{
		try{
			Site site = BaseTestCaseUtility.initSite();		
			//te.setId(new Long("1"));
			site.setActivityStatus("Invalid");
			System.out.println(site);
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			site = (Site) appService.createObject(site,bean); 
			assertFalse("Invalid site type should thorw Exception", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Type is wrong", true);
		 }
	}
	
	public void testWithInvalidCordinatorInSite()
	{
		try{
			Site site = BaseTestCaseUtility.initSite();		
			User user= new User();
			user = BaseTestCaseUtility.initUser(); 
			site.setCoordinator(user);
			System.out.println(site);
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			site = (Site) appService.createObject(site,bean); 
			assertFalse("It should throw exception", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cordinator is required", true);
		 }
	}
	public void testUpdateSiteToClosedActivityStatus()
	{
		try{
			Site site = BaseTestCaseUtility.initSite();
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			site = (Site) appService.createObject(site,bean); 
			site.setActivityStatus("Closed");
			System.out.println(site);
			Site updatedSite = (Site) appService.updateObject(site,bean); 
			assertTrue("Site updated successfully", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse("Failed to update site", true);
		 }
	}
	
	public void testAddSiteWithInvalidPhoneNumber()
	{
		try{
			Site site = BaseTestCaseUtility.initSite();
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			site = (Site) appService.createObject(site,bean);
			Address address = new Address();
			address.setPhoneNumber("2242241111");
			site.setAddress(address);
			System.out.println(site);
			Site updatedSite = (Site) appService.updateObject(site,bean);
			Logger.out.info("Invalid phone number entered, it should throw exception");
			fail("Invalid phone number entered, it should throw exception");
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Invalid phone number entered, correct format is xxx-xxx-xxxx", true);
		}		
	}
	
	public void testAddSiteWithInvalidFaxNumber()
	{
		try{
			Site site = BaseTestCaseUtility.initSite();	
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			site = (Site) appService.createObject(site,bean);
			Address address = new Address();
			address.setFaxNumber("2242241111");
			site.setAddress(address);
			System.out.println(site);
			Site updatedSite = (Site) appService.updateObject(site,bean);
			Logger.out.info("Invalid fax number entered, it should throw exception");
			fail("Invalid fax number entered, it should throw exception");
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Invalid fax number entered, correct format is xxx-xxx-xxxx", true);
		}		
	} 

	public void testEditSiteUserCPAssociation()
	{
    	Logger.out.info("updating domain object site ------->");
	    try 
		{
			new ExcelTestCaseUtility().siteAssociation();
			assertTrue("Domain object successfully updated ---->", true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
}