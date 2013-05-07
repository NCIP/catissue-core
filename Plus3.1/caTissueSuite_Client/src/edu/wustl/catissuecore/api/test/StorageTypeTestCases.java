package edu.wustl.catissuecore.api.test;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class StorageTypeTestCases extends CaTissueBaseTestCase {
	
		AbstractDomainObject domainObject = null;
		public void testAddStorageType()
		{
			try{
				StorageType storagetype = BaseTestCaseUtility.initStorageType();			
				System.out.println(storagetype);
				storagetype = (StorageType) appService.createObject(storagetype);
				TestCaseUtility.setObjectMap(storagetype, StorageType.class);
				System.out.println("Object created successfully");
				assertTrue("Object added successfully", true);
			 }
			 catch(Exception e){
				 e.printStackTrace();
				 assertFalse("could not add object", true);
			 }
		}
		
		public void testSearchStorageType()
		{
			StorageType storagetype = new StorageType();
	    	Logger.out.info(" searching domain object");
	    	storagetype.setId(new Long(1));
	   
	         try {
	        	 List resultList = appService.search(StorageType.class,storagetype);
	        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
	        		 StorageType returnedSite = (StorageType) resultsIterator.next();
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
		
		public void testUpdateStorageType()
		{
			StorageType storagetype =  BaseTestCaseUtility.initStorageType();
	    	Logger.out.info("updating domain object------->"+storagetype);
		    try 
			{
		    	storagetype = (StorageType) appService.createObject(storagetype);
		    	BaseTestCaseUtility.updateStorageType(storagetype);	
		    	StorageType updatedStorageType = (StorageType) appService.updateObject(storagetype);
		       	Logger.out.info("Domain object successfully updated ---->"+updatedStorageType);
		       	assertTrue("Domain object successfully updated ---->"+updatedStorageType, true);
		    } 
		    catch (Exception e) {
		       	Logger.out.error(e.getMessage(),e);
		 		e.printStackTrace();
		 		assertFalse("failed to update Object", true);
		    }
		}
		
		
		
		public void testWithEmptyStorageTypeName()
		{
			try{
				StorageType storagetype = BaseTestCaseUtility.initStorageType();		
				//te.setId(new Long("1"));
				storagetype.setName("");
				System.out.println(storagetype);
				storagetype = (StorageType) appService.createObject(storagetype); 
				assertFalse("Empty storagetype name should thorw Exception", true);
			 }
			 catch(Exception e){
				 Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertTrue("Name is required", true);
			 }
		}
		
		public void testWithDuplicateStorageTypeName()
		{
			try{
				StorageType storagetype = BaseTestCaseUtility.initStorageType();	
				StorageType dupStorageTypeName = BaseTestCaseUtility.initStorageType();
				dupStorageTypeName.setName(storagetype.getName());
				storagetype = (StorageType) appService.createObject(storagetype); 
				dupStorageTypeName = (StorageType) appService.createObject(dupStorageTypeName); 
				assertFalse("Test Failed. Duplicate storagetype name should throw exception", true);
			}
			 catch(Exception e){
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
				assertTrue("Submission failed since a storagetype with the same NAME already exists" , true);
				 
			 }
		}
		
		public void testWithNegativeDimensionCapacity_StorageType()
		{
			try{
				StorageType storagetype = BaseTestCaseUtility.initStorageType();		
				//te.setId(new Long("1"));
				Capacity capacity = new Capacity();
				capacity.setOneDimensionCapacity(new Integer(-1));
				capacity.setTwoDimensionCapacity(new Integer(-1));
				storagetype.setCapacity(capacity);		
				
				System.out.println(storagetype);
				storagetype = (StorageType) appService.createObject(storagetype); 
				assertFalse("Negative Dimension capacity should thorw Exception", true);
			 }
			 catch(Exception e){
				 Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertTrue("Name is required", true);
			 }
		}
		
		public void testWithEmptyDimensionLabel_StorageType()
		{
			try{
				StorageType storagetype = BaseTestCaseUtility.initStorageType();	
				storagetype.setOneDimensionLabel("");
				storagetype.setTwoDimensionLabel("");
				System.out.println(storagetype);
				storagetype = (StorageType) appService.createObject(storagetype); 
				assertFalse("Empty Text Label should thorw Exception", true);
			 }
			 catch(Exception e){
				 Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertTrue("Text Label For Dimension One is required", true);
			 }
		}
		
		/*public void testNullDomainObjectInInsert()
		{
			domainObject = new StorageType(); 
			testNullDomainObjectInInsert(domainObject);
		}*/
		
//		public void testNullSessionDataBeanInInsert_StorageType()
//		{
//			domainObject = new StorageType();
//			testNullSessionDataBeanInInsert(domainObject);
//		}
//			
//		public void testWrongDaoTypeInInsert_StorageType()
//		{
//			domainObject = new StorageType();
//			testWrongDaoTypeInInsert(domainObject);
//		}
//		public void testNullSessionDataBeanInUpdate_StorageType()
//		{
//			domainObject = new StorageType();
//			testNullSessionDataBeanInUpdate(domainObject);
//		}
//		
//		public void testNullOldDomainObjectInUpdate_StorageType()
//		{
//			domainObject = new StorageType();
//			testNullOldDomainObjectInUpdate(domainObject);
//		}
//		
//			
//		/*public void testNullCurrentDomainObjectInUpdate()
//		{
//			domainObject = new StorageType();
//			testNullCurrentDomainObjectInUpdate(domainObject);
//		}*/
//		
//		public void testEmptyCurrentDomainObjectInUpdate_StorageType()
//		{
//			domainObject = new StorageType();
//			AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initDistributionProtocol();
//			testEmptyCurrentDomainObjectInUpdate(domainObject, initialisedDomainObject);
//		}
//		
//		public void testEmptyOldDomainObjectInUpdate_StorageType()
//		{
//			domainObject = new StorageType();
//			AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initDistributionProtocol();
//			testEmptyOldDomainObjectInUpdate(domainObject,initialisedDomainObject);
//		}
//		
//		public void testNullDomainObjectInRetrieve_StorageType()
//		{
//			domainObject = new StorageType();
//			testNullCurrentDomainObjectInRetrieve(domainObject);
//		}
}
