package edu.wustl.catissuecore.bizlogic.test;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class SiteTestCases extends CaTissueBaseTestCase {

	AbstractDomainObject domainObject = null;
 	public void testAddSite()
	{
		try{
			Site site= BaseTestCaseUtility.initSite();			
			System.out.println(site);
			site = (Site) appService.createObject(site); 
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
        	 List resultList = appService.search(Site.class,site);
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
	    	site = (Site) appService.createObject(site);
	    	BaseTestCaseUtility.updateSite(site);	
	    	Site updatedSite = (Site) appService.updateObject(site);
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
			site = (Site) appService.createObject(site); 
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
			site = (Site) appService.createObject(site); 
			dupSiteName = (Site) appService.createObject(dupSiteName); 
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
			site = (Site) appService.createObject(site); 
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
			site = (Site) appService.createObject(site); 
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
			//te.setId(new Long("1"));
			User user= new User();
			user = BaseTestCaseUtility.initUser(); 
			site.setCoordinator(user);
			System.out.println(site);
			site = (Site) appService.createObject(site); 
			assertFalse("It should throw exception", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cordinator is required", true);
		 }
	}
	
	
//	
//	public void testNullDomainObjectInInsert_Site()
//	{
//		domainObject = new Site(); 
//		testNullDomainObjectInInsert(domainObject);
//	}
//	
//	public void testNullSessionDataBeanInInsert_Site()
//	{
//		domainObject = new Site();
//		testNullSessionDataBeanInInsert(domainObject);
//	}
//		
//	public void testWrongDaoTypeInInsert_Site()
//	{
//		domainObject = new Site();
//		testWrongDaoTypeInInsert(domainObject);
//	}
//	public void testNullSessionDataBeanInUpdate_Site()
//	{
//		domainObject = new Site();
//		testNullSessionDataBeanInUpdate(domainObject);
//	}
//	
//	public void testNullOldDomainObjectInUpdate_Site()
//	{
//		domainObject = new Site();
//		testNullOldDomainObjectInUpdate(domainObject);
//	}
//	
//		
//	public void testNullCurrentDomainObjectInUpdate_Site()
//	{
//		domainObject = new Site();
//		testNullCurrentDomainObjectInUpdate(domainObject);
//	}
//	
//	public void testEmptyCurrentDomainObjectInUpdate_Site()
//	{
//		domainObject = new Site();
//		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initDistributionProtocol();
//		testEmptyCurrentDomainObjectInUpdate(domainObject, initialisedDomainObject);
//	}
//	
//	public void testEmptyOldDomainObjectInUpdate_Site()
//	{
//		domainObject = new Site();
//		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initDistributionProtocol();
//		testEmptyOldDomainObjectInUpdate(domainObject,initialisedDomainObject);
//	}
//	
//	public void testNullDomainObjectInRetrieve_Site()
//	{
//		domainObject = new Site();
//		testNullCurrentDomainObjectInRetrieve(domainObject);
//	}
}
