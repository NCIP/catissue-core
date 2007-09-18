package edu.wustl.catissuecore.bizlogic.test;


import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

public class CollectionProtocolTestCases extends CaTissueBaseTestCase {

	AbstractDomainObject domainObject = null;
	
	public void testAddCollectionProtocol()
	{
		try {
			CollectionProtocol collectionProtocol = BaseTestCaseUtility.initCollectionProtocol();			
			System.out.println(collectionProtocol);
			collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol); 
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
//	 Failing TestCases
 public void testSearchCollectionProtocol()
	{
    	CollectionProtocol collectionProtocol = BaseTestCaseUtility.initCollectionProtocol();
    	collectionProtocol.setId(new Long(18));
       	Logger.out.info(" searching domain object");
    	
         try {
        	// collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
        	 List resultList = appService.search(CollectionProtocol.class,collectionProtocol);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
        	 {
        		 CollectionProtocol returnedcollectionprotocol = (CollectionProtocol) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedcollectionprotocol.getTitle());
             }
          } 
          catch (Exception e) {
        	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("Doesnot found collection protocol", true);
          }
	}

	public void testUpdateCollectionProtocol()
	{
		CollectionProtocol collectionProtocol = BaseTestCaseUtility.initCollectionProtocol();
		//CollectionProtocol collectionProtocol = new CollectionProtocol();
		//collectionProtocol.setId(new Long(8));
    	Logger.out.info("updating domain object------->"+collectionProtocol);
	    try 
		{
	    	collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
	    	BaseTestCaseUtility.updateCollectionProtocol(collectionProtocol);
	    	CollectionProtocol updatedCollectionProtocol = (CollectionProtocol) appService.updateObject(collectionProtocol);
	    	Logger.out.info("Domain object successfully updated ---->"+updatedCollectionProtocol);
	    	assertTrue("Domain object updated successfully", true);
	    } 
	    catch (Exception e)
	    {
	    	Logger.out.error(e.getMessage(),e);
	    	e.printStackTrace();
	    	assertFalse("Failed to update object",true);
	 		
	    }
	}
	
	
	public void testCollectionProtocolWithEmptyTitle()
	{
		    	
	    try 
	  	{
	    	CollectionProtocol collectionProtocol =  BaseTestCaseUtility.initCollectionProtocol();
	    	collectionProtocol.setTitle("");
	    	Logger.out.info("updating domain object------->"+collectionProtocol);
	    	collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
	    	Logger.out.info("Collection Protocol object eith empty title ---->"+collectionProtocol);
	       	assertFalse("Collection Protocol should throw exception ---->"+collectionProtocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Distribution Protocol object", true);
	    }
	}
	
	public void testCollectionProtocolWithDuplicateCollectionProtocolTitle()
	{
		
		try{
			CollectionProtocol collectionProtocol = BaseTestCaseUtility.initCollectionProtocol();	
			CollectionProtocol dupCollectionProtocol = BaseTestCaseUtility.initCollectionProtocol();
			dupCollectionProtocol.setTitle(collectionProtocol.getTitle());
			collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol); 
			dupCollectionProtocol = (CollectionProtocol) appService.createObject(dupCollectionProtocol); 
			assertFalse("Test Failed. Duplicate Collection Protocol name should throw exception", true);
		}
		 catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Submission failed since a Collection Protocol with the same NAME already exists" , true);
			 
		 }
    	
	}
	
	public void testCollectionProtocolWithEmptyShortTitle()
	{
		    	
	    try 
	  	{
	    	CollectionProtocol collectionProtocol =  BaseTestCaseUtility.initCollectionProtocol();
	    	collectionProtocol.setShortTitle("");
	    	Logger.out.info("updating domain object------->"+collectionProtocol);
	    	collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
	    	Logger.out.info("Collection Protocol object with empty short title ---->"+collectionProtocol);
	       	assertFalse("Collection Protocol should throw exception ---->"+collectionProtocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("Failed to create Collection Protocol object", true);
	    }
	}
	
	public void testCollectionProtocolWithEmptyStartDate()
	{
		    	
	    try 
	  	{
	    	CollectionProtocol collectionProtocol =  BaseTestCaseUtility.initCollectionProtocol();
	    	
	    	collectionProtocol.setStartDate(Utility.parseDate("", Utility
						.datePattern("08/15/1975")));
	    		    	
	    	Logger.out.info("updating domain object------->"+collectionProtocol);
	    	collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
	    	Logger.out.info("Collection Protocol object with empty date ---->"+collectionProtocol);
	       	assertFalse("Collection should throw exception ---->"+collectionProtocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Collection Protocol object", true);
	    }
	}
	
	public void testCollectionProtocolWithClosedActivityStatus()
	{
		    	
	    try 
	  	{
	    	CollectionProtocol collectionProtocol =  BaseTestCaseUtility.initCollectionProtocol();
	    	collectionProtocol.setActivityStatus("Closed");
	    		    	
	    	Logger.out.info("updating domain object------->"+collectionProtocol);
	    	collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
	    	Logger.out.info("Collection Protocol object with closed activity status ---->"+collectionProtocol);
	       	assertFalse("Collection Protocol should throw exception ---->"+collectionProtocol, true);
	    } 
	    catch (Exception e)
	    {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Collection Protocol object", true);
	    }
	}
	
	public void testCollectionProtocolWithInvalidActivityStatus()
	{
		    	
	    try 
	  	{
	    	CollectionProtocol collectionProtocol =  BaseTestCaseUtility.initCollectionProtocol();
	    	collectionProtocol.setActivityStatus("Invalid");
	    		    	
	    	Logger.out.info("updating domain object------->"+collectionProtocol);
	    	collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
	    	Logger.out.info("Collection Protocol object with invalid activity status ---->"+collectionProtocol);
	       	assertFalse("Collection Protocol should throw exception ---->"+collectionProtocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Collection Protocol object", true);
	    }
	}  
	
	
	
	
	
	
	
	public void testCollectionProtocolWithInvalidSpecimenClass()
	{
		CollectionProtocol collectionProtocol = BaseTestCaseUtility.initCollectionProtocol();

		Collection collectionProtocolEventCollectionSet = new HashSet();
	 
		CollectionProtocolEvent collectionProtocolEvent = BaseTestCaseUtility.initCollectionProtocolEvent();		
			 

		Collection specimenRequirementCollection = new HashSet();
		SpecimenRequirement specimenRequirement = BaseTestCaseUtility.initSpecimenRequirement();
		specimenRequirement.setSpecimenClass("Invalid class");
		specimenRequirementCollection.add(specimenRequirement);
		
	//	collectionProtocolEvent.setSpecimenRequirementCollection(specimenRequirementCollection);

		//Setting collection point label
		collectionProtocolEvent.setCollectionPointLabel("User entered value");
		
		collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
		collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);
    	
	    try 
	  	{

			collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
	    	Logger.out.info("Collection Protocol object with invalid specimen class ---->"+collectionProtocol);
	       	assertFalse("Collection Protocol should throw exception ---->"+collectionProtocol, true); 
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Collection Protocol object", true);
	    }	  	
		
	}
	
	
	
	public void testCollectionProtocolWithInvalidSpecimenType()
	{
		    	
		CollectionProtocol collectionProtocol = BaseTestCaseUtility.initCollectionProtocol();

		Collection collectionProtocolEventCollectionSet = new HashSet();
	 
		CollectionProtocolEvent collectionProtocolEvent = BaseTestCaseUtility.initCollectionProtocolEvent();		
			 

		Collection specimenRequirementCollection = new HashSet();
		SpecimenRequirement specimenRequirement = BaseTestCaseUtility.initSpecimenRequirement();
		specimenRequirement.setSpecimenClass("Molecular");
		specimenRequirement.setSpecimenType("Invalid Type");
		specimenRequirementCollection.add(specimenRequirement);
		
	///	collectionProtocolEvent.setSpecimenRequirementCollection(specimenRequirementCollection);

		//Setting collection point label
		collectionProtocolEvent.setCollectionPointLabel("User entered value");
		
		collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
		collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);
    	
	    try 
	  	{

			collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
	    	Logger.out.info("Collection Protocol object with invalid specimen class ---->"+collectionProtocol);
	       	assertFalse("Collection Protocol should throw exception ---->"+collectionProtocol, true); 
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Collection Protocol object", true);
	    }
	}
	
	public void testCollectionProtocolWithInvalidTissueSite()
	{
    	
		CollectionProtocol collectionProtocol = BaseTestCaseUtility.initCollectionProtocol();

		Collection collectionProtocolEventCollectionSet = new HashSet();
	 
		CollectionProtocolEvent collectionProtocolEvent = BaseTestCaseUtility.initCollectionProtocolEvent();		
			 

		Collection specimenRequirementCollection = new HashSet();
		SpecimenRequirement specimenRequirement = BaseTestCaseUtility.initSpecimenRequirement();
		specimenRequirement.setTissueSite("Invalid Tissue site");
		
		specimenRequirementCollection.add(specimenRequirement);
		
	//	collectionProtocolEvent.setSpecimenRequirementCollection(specimenRequirementCollection);

		//Setting collection point label
		collectionProtocolEvent.setCollectionPointLabel("User entered value");
		
		collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
		collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);
    	
	    try 
	  	{

			collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
	    	Logger.out.info("Collection Protocol object with invalid specimen class ---->"+collectionProtocol);
	       	assertFalse("Collection Protocol should throw exception ---->"+collectionProtocol, true); 
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Collection Protocol object", true);
	    }
	}
	
	public void testCollectionProtocolWithInvalidPathologicalStatus()
	{
		CollectionProtocol collectionProtocol = BaseTestCaseUtility.initCollectionProtocol();

		Collection collectionProtocolEventCollectionSet = new HashSet();
	 
		CollectionProtocolEvent collectionProtocolEvent = BaseTestCaseUtility.initCollectionProtocolEvent();		
			 

		Collection specimenRequirementCollection = new HashSet();
		SpecimenRequirement specimenRequirement = BaseTestCaseUtility.initSpecimenRequirement();
		specimenRequirement.setPathologyStatus("Invalid Pathelogical Status");
		
		specimenRequirementCollection.add(specimenRequirement);
		
	//	collectionProtocolEvent.setSpecimenRequirementCollection(specimenRequirementCollection);

		//Setting collection point label
		collectionProtocolEvent.setCollectionPointLabel("User entered value");
		
		collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
		collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);
    	
	    try 
	  	{

			collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
	    	Logger.out.info("Collection Protocol object with invalid specimen class ---->"+collectionProtocol);
	       	assertFalse("Collection Protocol should throw exception ---->"+collectionProtocol, true); 
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Collection Protocol object", true);
	    }
	}
	
	
	public void testNullObjectInInsert_CollectionProtocol(){
		domainObject = new CollectionProtocol(); 
		testNullDomainObjectInInsert(domainObject);
	}
	
	public void testNullSessionDatBeanInInsert_CollectionProtocol(){
		domainObject = new CollectionProtocol();
		testNullSessionDataBeanInInsert(domainObject);
	}
	
	public void testNullSessionDataBeanInUpdate_CollectionProtocol(){
		domainObject = new CollectionProtocol();
		testNullSessionDataBeanInUpdate(domainObject);
	}
	
	public void testNullOldDomainObjectInUpdate_CollectionProtocol(){
		domainObject = new CollectionProtocol();
		testNullOldDomainObjectInUpdate(domainObject);
	}
	
	public void testNullCurrentDomainObjectInUpdate_CollectionProtocol(){
		domainObject = new CollectionProtocol();
		testNullCurrentDomainObjectInUpdate(domainObject);
	}
	
	public void testWrongDaoTypeInUpdate_CollectionProtocol(){
		domainObject = new CollectionProtocol();
		testNullCurrentDomainObjectInUpdate(domainObject);
	}
	
	public void testEmptyCurrentDomainObjectInUpdate_CollectionProtocol(){
		domainObject = new CollectionProtocol();
		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initCollectionProtocol();
		testEmptyCurrentDomainObjectInUpdate(domainObject, initialisedDomainObject);
	}
	
	public void testEmptyOldDomainObjectInUpdate_CollectionProtocol(){
		domainObject = new CollectionProtocol();
		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initCollectionProtocol();
		testEmptyOldDomainObjectInUpdate(domainObject, initialisedDomainObject);
	}
	
	
	
	
}
