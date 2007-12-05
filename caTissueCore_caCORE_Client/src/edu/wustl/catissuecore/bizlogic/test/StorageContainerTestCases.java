package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;


public class StorageContainerTestCases extends CaTissueBaseTestCase{
	AbstractDomainObject domainObject = null;
	
	public void testAddStorageContainer()
	{
		try{
			StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();			
			System.out.println(storageContainer);
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testSearchStorageContainer()
	{
		StorageContainer storageContainer = new StorageContainer();
    	Logger.out.info(" searching domain object");
    	storageContainer.setId(new Long(1));
   
         try {
        	 List resultList = appService.search(StorageContainer.class,storageContainer);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 StorageContainer returnedStorageContainer = (StorageContainer) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " 
        				 + returnedStorageContainer.getName());
        		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
             }
          } 
          catch (Exception e) {
           	Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Does not find Domain Object", true);
	 		
          }
	}
	
	public void testUpdateStorageContainer()
	{
		StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
		System.out.println("Before Update");
    	Logger.out.info("updating domain object------->"+storageContainer);
	    try 
		{
	    	storageContainer = (StorageContainer) appService.createObject(storageContainer);
	    	BaseTestCaseUtility.updateStorageContainer(storageContainer);
	    	System.out.println("After Update");
	    	StorageContainer updatedStorageContainer = (StorageContainer) appService.updateObject(storageContainer);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedStorageContainer);
	       	assertTrue("Domain object successfully updated ---->"+updatedStorageContainer, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
	
	public void testUpdateStorageContainerToClosedActivityStatus()
	{
		try{
			StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();			
			System.out.println(storageContainer);
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 
			System.out.println("Object created successfully");
			storageContainer.setActivityStatus("Closed");
			StorageContainer updatedStorageContainer = (StorageContainer) appService.updateObject(storageContainer);
			assertTrue("Object updated successfully", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse("Could notclose Storage Container", true);
		 }
	}
	
	public void testUpdateStorageContainerToDisabledActivityStatus()
	{
		try{
			StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();			
			System.out.println(storageContainer);
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 
			System.out.println("Object created successfully");
			storageContainer.setActivityStatus("Disabled");
			StorageContainer updatedStorageContainer = (StorageContainer) appService.updateObject(storageContainer);
			assertTrue("Object updated successfully", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse("Could not disable Storage Container", true);
		 }
	}
	
	public void testAddStorageContainerToClosedSite()
	{
	try{
		Site site= BaseTestCaseUtility.initSite(); 	
		System.out.println(site);
		try{
			site = (Site) appService.createObject(site); 
		}catch(Exception e){
			Logger.out.error(e.getMessage(),e);	
			e.printStackTrace();	
			assertFalse("Failed to create site ", true);
		}
		
		site.setActivityStatus("Closed");
		
		try{
			site =(Site)appService.updateObject(site);
		 }catch(Exception e){
			Logger.out.error(e.getMessage(),e);	
			e.printStackTrace();	
			assertFalse("Failed to close the site ", true);
		 }
		
		StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer(); 
		System.out.println(storageContainer);
		storageContainer.setSite(site);
		
		try{
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 
			assertFalse("Storage Container successfully created", true);
	
		}catch(Exception e){
			Logger.out.error(e.getMessage(),e);	
			e.printStackTrace();	
			assertTrue("Could not add Storage Container to close site ", true);
		 }
			
	}catch(Exception e){
		Logger.out.error(e.getMessage(),e);	
		e.printStackTrace();	
		assertFalse("Test Failed", true);
	}
  }	

}
