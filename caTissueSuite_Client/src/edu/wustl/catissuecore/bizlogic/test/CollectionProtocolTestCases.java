package edu.wustl.catissuecore.bizlogic.test;


import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;

public class CollectionProtocolTestCases extends CaTissueBaseTestCase 
{
	AbstractDomainObject domainObject = null;
	
	public void testAddCollectionProtocol()
	{
		try
		 {
			CollectionProtocol collectionProtocol = BaseTestCaseUtility.initCollectionProtocol();			
			collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
			TestCaseUtility.setObjectMap(collectionProtocol, CollectionProtocol.class);
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 fail("could not add object");
		 }
	}
	
	public void testUpdateCollectionProtocol()
	{
	    try 
		{
	    	CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
		   	Logger.out.info("updating domain object------->"+collectionProtocol);
	    	BaseTestCaseUtility.updateCollectionProtocol(collectionProtocol);
	    	//System.out.println("befor");
	    	//System.out.println(collectionProtocol.getId()+">>>>");
	    	CollectionProtocol updatedCollectionProtocol = (CollectionProtocol)appService.updateObject(collectionProtocol);
	    	//System.out.println("after");
	    	Logger.out.info("Domain object successfully updated ---->"+updatedCollectionProtocol);
	    	assertTrue("Domain object updated successfully", true);
	    } 
	    catch (Exception e)
	    {
	    	Logger.out.error(e.getMessage(),e);
	    	e.printStackTrace();
	    	//assertFalse("Failed to update object",true);
	    	fail("Failed to update object");
	    }
	}
	public void testUpdateCollectionProtocolDeleteCollectionProtocolEvent()
	{
	    try 
		{
	    	CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
	    	Collection cpeCollection = collectionProtocol.getCollectionProtocolEventCollection();
	    	Iterator itr = cpeCollection.iterator();
	    	Collection collectionProtocolEventList = new LinkedHashSet();
	    	collectionProtocolEventList.add(itr.next());
	    	System.out.println(collectionProtocolEventList.size() + " : Collection Size");
	    	collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventList);
	    	CollectionProtocol updatedCollectionProtocol = (CollectionProtocol)appService.updateObject(collectionProtocol);
	    	Logger.out.info("Domain object successfully updated ---->"+updatedCollectionProtocol);
	    	assertTrue("Domain object updated successfully", true);
	    } 
	    catch (Exception e)
	    {
	    	Logger.out.error(e.getMessage(),e);
	    	System.out.println("TestCaseTesting.testdeleteCollectionProtocolEvent()"+ e.getMessage());
	    	e.printStackTrace();
	    	fail("Failed to update object");
	    }
	}
	
	public void testUpdateCollectionProtocolDeleteChildSpecimenRequirement()
	{
	    try 
		{
	    	CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
	    	Collection cpeCollection = collectionProtocol.getCollectionProtocolEventCollection();
	    	Iterator itr = cpeCollection.iterator();
	    	while(itr.hasNext())
	    	{
	    		CollectionProtocolEvent cpe = (CollectionProtocolEvent)itr.next();
	    		Collection reqSpecimenCollection = cpe.getSpecimenRequirementCollection();
	    		Iterator spReqItr = reqSpecimenCollection.iterator();
	    		while(spReqItr.hasNext())
	    		{
	    			SpecimenRequirement spReq = (SpecimenRequirement)spReqItr.next();
	    			if("Aliquot".equals(spReq.getLineage()))
	    			{
	    				spReqItr.remove();
	    			}
	    		}
	    	}
	    	
	    	CollectionProtocol updatedCollectionProtocol = (CollectionProtocol)appService.updateObject(collectionProtocol);
	    	Logger.out.info("Domain object successfully updated ---->"+updatedCollectionProtocol);
	    	assertTrue("Domain object updated successfully", true);
	    } 
	    catch (Exception e)
	    {
	    	System.out.println("TestCaseTesting.testdeleteChildSpecimenRequirement() : " + e.getMessage());
	    	e.printStackTrace();
	    	fail("Failed to update object");
	    }
	}
	
	public void testUpdateCollectionProtocolAddCollectionProtocolEvent()
	{
	    try 
		{
	    	CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
	    	Collection cpeCollection = collectionProtocol.getCollectionProtocolEventCollection();
	    	CollectionProtocolEvent collectionProtocolEvent = new CollectionProtocolEvent();
			BaseTestCaseUtility.setCollectionProtocolEvent(collectionProtocolEvent);
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			cpeCollection.add(collectionProtocolEvent);
	    	CollectionProtocol updatedCollectionProtocol = (CollectionProtocol)appService.updateObject(collectionProtocol);
	    	Logger.out.info("Domain object successfully updated ---->"+updatedCollectionProtocol);
	    	assertTrue("Domain object updated successfully", true);
	    } 
	    catch (Exception e)
	    {
	    	Logger.out.error(e.getMessage(),e);
	        System.out.println("TestCaseTesting.testAddCollectionProtocolEvent() : "+ e.getMessage());
	    	e.printStackTrace();
	    	fail("Failed to update object");
	    }
	}
	
	public void testUpdateCollectionProtocolDeleteConsent()
	{
	    try 
		{
	    	CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
	    	Collection consentCollection = collectionProtocol.getConsentTierCollection();
	    	Iterator<ConsentTier> itr = consentCollection.iterator();
	    	itr.next();
	    	itr.remove();
	    	System.out.println(consentCollection.size() + " : Collection Size");
	    	CollectionProtocol updatedCollectionProtocol = (CollectionProtocol)appService.updateObject(collectionProtocol);
	    	Logger.out.info("Domain object successfully updated ---->"+updatedCollectionProtocol);
	    	assertTrue("Domain object updated successfully", true);
	    } 
	    catch (Exception e)
	    {
	    	Logger.out.error(e.getMessage(),e);
	    	System.out.println("TestCaseTesting.testdeleteCollectionProtocolEvent()"+ e.getMessage());
	    	e.printStackTrace();
	    	fail("Failed to update object");
	    }
	}
	public void testSearchCollectionProtocol()
	{
    	CollectionProtocol collectionProtocol = new CollectionProtocol();
    	CollectionProtocol cachedCollectionProtocol = (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
    	cachedCollectionProtocol.setId((Long) cachedCollectionProtocol.getId());
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
	 		//assertFalse("Doesnot found collection protocol", true);
	 		fail("Doesnot found collection protocol");
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
	       	//assertFalse("Collection Protocol should throw exception ---->"+collectionProtocol, true);
	       	fail("Collection Protocol should throw exception");
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Collection Protocol object", true);
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
			fail("Test Failed. Duplicate Collection Protocol name should throw exception");
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
	       	//assertFalse("Collection Protocol should throw exception ---->"+collectionProtocol, true);
	    	fail("Collection Protocol should throw exception");
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
	       	fail("Collection Protocol should throw exception");
	    } 
	    catch (Exception e) {
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
	       	fail("Collection Protocol should throw exception");
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
	
		//Setting collection point label
		collectionProtocolEvent.setCollectionPointLabel("User entered value");
		
		collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
		collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);
    	
	    try 
	  	{

			collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
	    	Logger.out.info("Collection Protocol object with invalid specimen class ---->"+collectionProtocol);
	       //assertFalse("Collection Protocol should throw exception ---->"+collectionProtocol, true);
	    	fail("Collection Protocol should throw exception");
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
			 
		//Setting collection point label
		collectionProtocolEvent.setCollectionPointLabel("User entered value");
		
		collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
		collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);
    	
	    try 
	  	{

			collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
	    	Logger.out.info("Collection Protocol object with invalid specimen class ---->"+collectionProtocol);
	       	//assertFalse("Collection Protocol should throw exception ---->"+collectionProtocol, true); 
	       	fail("Collection Protocol should throw exception");
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
			 
		//Setting collection point label
		collectionProtocolEvent.setCollectionPointLabel("User entered value");
		
		collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
		collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);
    	
	    try 
	  	{

			collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
	    	Logger.out.info("Collection Protocol object with invalid specimen class ---->"+collectionProtocol);
	      // 	assertFalse("Collection Protocol should throw exception ---->"+collectionProtocol, true); 
	       	fail("Collection Protocol should throw exception");
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
			 
		//Setting collection point label
		collectionProtocolEvent.setCollectionPointLabel("User entered value");
		
		collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
		collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);
    	
	    try 
	  	{
			collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
	    	Logger.out.info("Collection Protocol object with invalid specimen class ---->"+collectionProtocol);
	       	//assertFalse("Collection Protocol should throw exception ---->"+collectionProtocol, true);
	    	fail("Collection Protocol should throw exception");
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("Failed to create Collection Protocol object", true);
	    }
	}
	

}
