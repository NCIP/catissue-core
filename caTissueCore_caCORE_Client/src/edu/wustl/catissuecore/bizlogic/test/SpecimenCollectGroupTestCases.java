package edu.wustl.catissuecore.bizlogic.test;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.util.logger.Logger;


public class SpecimenCollectGroupTestCases extends CaTissueBaseTestCase
{
	
	public void testUpdateSpecimenCollectionGroupWithConsents()
	{
		try
		{
			SpecimenCollectionGroup specimenCollGroup = (SpecimenCollectionGroup)TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			Participant participant = (Participant)TestCaseUtility.getObjectMap(Participant.class);
			updateSCG(specimenCollGroup, participant);
			assertTrue("Specimen Collection Group Updated", true);
		}
		catch(Exception e)
		{
			assertFalse("Specimen Collection Group Not Updated", true);
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			fail("Failed to add Domain Object");
		}
	}
	
	private void updateSCG(SpecimenCollectionGroup sprObj, Participant participant)
	{
		System.out.println("After");
		System.out.println(sprObj+": sprObj");
		System.out.println(participant+": participant");
		System.out.println("Before Update");
		sprObj.setCollectionStatus("Complete");
		
		Collection consentTierStatusCollection = new HashSet();
		ConsentTierStatus r1 = new ConsentTierStatus();
		ConsentTier consentTier = new ConsentTier();
		consentTier.setId(new Long(1));
		r1.setConsentTier(consentTier);
		r1.setStatus("Yes");
		consentTierStatusCollection.add(r1);
		
		ConsentTierStatus r2 = new ConsentTierStatus();
		ConsentTier consentTier2 = new ConsentTier();
		consentTier2.setId(new Long(2));
		r2.setConsentTier(consentTier2);
		r2.setStatus("No");
		consentTierStatusCollection.add(r2);
		
		ConsentTierStatus r3 = new ConsentTierStatus();
		ConsentTier consentTier3 = new ConsentTier();
		consentTier3.setId(new Long(3));
		r3.setConsentTier(consentTier3);
		r3.setStatus("Yes");
		consentTierStatusCollection.add(r3);
		
		sprObj.setConsentTierStatusCollection(consentTierStatusCollection);
		sprObj.getCollectionProtocolRegistration().getCollectionProtocol().setId(new Long(1));
		sprObj.getCollectionProtocolRegistration().setParticipant(participant);
		Collection collectionProtocolEventList = new LinkedHashSet();
		
		Site site = new Site();
		site.setId(new Long(1));
		sprObj.setSpecimenCollectionSite(site);
		setEventParameters(sprObj);
		try
		{
			System.out.println("Before Update");
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup)appService.updateObject(sprObj);
			System.out.println(scg.getCollectionStatus().equals("Complete"));
			if(scg.getCollectionStatus().equals("Complete"))
			{
				assertTrue("Specimen Collected ---->", true);
			}
			else
			{
				assertFalse("Anticipatory Specimen", true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	private void setEventParameters(SpecimenCollectionGroup sprObj)
	{
		System.out.println("Inside Event Parameters");
		Collection specimenEventParametersCollection = new HashSet();
		CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
		ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
		collectionEventParameters.setCollectionProcedure("Not Specified");
		collectionEventParameters.setComment("");
		collectionEventParameters.setContainer("Not Specified");		
		Date timestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
		collectionEventParameters.setTimestamp(timestamp);
		User user = new User();
		user.setId(new Long(1));
		collectionEventParameters.setUser(user);	
		collectionEventParameters.setSpecimenCollectionGroup(sprObj);	
		
		//Received Events		
		receivedEventParameters.setComment("");
		User receivedUser = new User();
		receivedUser.setId(new Long(1));
		receivedEventParameters.setUser(receivedUser);
		receivedEventParameters.setReceivedQuality("Not Specified");		
		Date receivedTimestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
		receivedEventParameters.setTimestamp(receivedTimestamp);		
		receivedEventParameters.setSpecimenCollectionGroup(sprObj);
		specimenEventParametersCollection.add(collectionEventParameters);
		specimenEventParametersCollection.add(receivedEventParameters);
		sprObj.setSpecimenEventParametersCollection(specimenEventParametersCollection);
	}
	
	/*public void testAddSpecimenCollectionGroup()
	{
		try
		{
			SpecimenCollectionGroup specimenCollectionGroupObj =  BaseTestCaseUtility.initSpecimenCollectionGroup();
	    	Logger.out.info("Inserting domain object------->"+specimenCollectionGroupObj);
			specimenCollectionGroupObj =  (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroupObj);
			assertTrue("Domain Object is successfully added" , true);
			Logger.out.info(" Specimen Collection Group is successfully added ---->    ID:: " + specimenCollectionGroupObj.getId().toString());
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			fail("Failed to add Domain Object");
		}
	}
	
	 public void testSearchSpecimenCollectionGroup()
	 {
    	try 
    	{
	       	SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
	     	Logger.out.info(" searching domain object");		    	
	    	 	    List resultList = appService.search(SpecimenCollectionGroup.class,specimenCollectionGroup);
	        	for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
	        		 SpecimenCollectionGroup returnedspecimencollectiongroup = (SpecimenCollectionGroup) resultsIterator.next();
	        		 assertTrue("Specimen Collection Group is successfully Found" , true);
	        		 Logger.out.info(" Specimen Collection Group is successfully Found ---->  :: " + returnedspecimencollectiongroup.getName());
	             }
          } 
          catch (Exception e) {
        	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		fail("Failed to search Domain Object");
          }
	 }
	*/
	/**
	 * Negative Testcases for SpecimenCollectionGroup Object
	 */
	
	/*public void testAddSpecimenCollectionGroupWithEmptySCGName()
	{
 		SpecimenCollectionGroup specimenCollectionGroupObj = null;
		try
		{
			specimenCollectionGroupObj =  BaseTestCaseUtility.initSpecimenCollectionGroup();
	    	Logger.out.info("Inserting domain object------->"+specimenCollectionGroupObj);
	    	specimenCollectionGroupObj.setName("");
			specimenCollectionGroupObj =  (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroupObj);
			fail("Empty SCG name should throw exception");
			
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Empty SCG name throws exception" , true);
		}
	}
	
	public void testAddSpecimenCollectionGroupWithWrongClinicalDiagnosis()
	{
 		SpecimenCollectionGroup specimenCollectionGroupObj = null;
		try
		{
			specimenCollectionGroupObj =  BaseTestCaseUtility.initSpecimenCollectionGroup();
	    	Logger.out.info("Inserting domain object------->"+specimenCollectionGroupObj);
	    	specimenCollectionGroupObj.setClinicalDiagnosis("Wrong Clinical Diagnosis");
	    	specimenCollectionGroupObj =  (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroupObj);
			fail("Wrong Clinical Diagnosis should throw exception");
			
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Wrong Clinical Diagnosis value throws exception" , true);
		}
	}
	
	public void testAddSpecimenCollectionGroupWithWrongClinicalStatus()
	{
 		SpecimenCollectionGroup specimenCollectionGroupObj = null;
		try
		{
			specimenCollectionGroupObj =  BaseTestCaseUtility.initSpecimenCollectionGroup();
	    	Logger.out.info("Inserting domain object------->"+specimenCollectionGroupObj);
	    	specimenCollectionGroupObj.setClinicalStatus("wrong Clinical status");
	    	specimenCollectionGroupObj =  (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroupObj);
			fail("Wrong Clinical Status should throw exception");
			
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Wrong Clinical Status value throws exception" , true);
		}
	}
	
	public void testAddSpecimenCollectionGroupWithInvalidActivityStatus()
	{
 		SpecimenCollectionGroup specimenCollectionGroupObj = null;
		try
		{
			specimenCollectionGroupObj =  BaseTestCaseUtility.initSpecimenCollectionGroup();
	    	Logger.out.info("Inserting domain object------->"+specimenCollectionGroupObj);
	    	specimenCollectionGroupObj.setActivityStatus("Wrong Activity status");
	    	specimenCollectionGroupObj =  (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroupObj);
			fail("Wrong Clinical Status should throw exception");
			
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Wrong Clinical Status value throws exception" , true);
		}
	}
	
	public void testAddSpecimeColeectionGroupWithWrongCollectionEvents()
 	{
 		SpecimenCollectionGroup specimenCollectionGroupObj = null;
		try
		{
			specimenCollectionGroupObj = BaseTestCaseUtility.initSpecimenCollectionGroup();
	    	Logger.out.info("Inserting domain object------->"+specimenCollectionGroupObj);	    	
	    	CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
	    	(specimenCollectionGroupObj.getSpecimenEventParametersCollection()).add(collectionEventParameters);	    
	    	specimenCollectionGroupObj =  (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroupObj);
	    	fail("Empty collection events should throw exception");
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Empty collection events throws exception", true);
		}
 	}*/
	
//	public void testUpdateSpecimenCollectionGroupWithDisabledActivityStatus()
//	{
// 		
//		try
//		{
//			SpecimenCollectionGroup specimenCollectionGroupObj = new SpecimenCollectionGroup();
//			specimenCollectionGroupObj.setId((Long) dataModelObjectMap.get("updatedSpecimenCollectionGroup"));
//			BaseTestCaseUtility.updateSpecimenCollectionGroup(specimenCollectionGroupObj);
//			specimenCollectionGroupObj.setActivityStatus("Disabled");
//			Logger.out.info("Updating domain object------->"+specimenCollectionGroupObj);
//	       	specimenCollectionGroupObj = (SpecimenCollectionGroup) appService.updateObject(specimenCollectionGroupObj);
//	    	assertTrue("Successfully updated activity status" , true);  	
//		}
//		catch(Exception e)
//		{
//			Logger.out.error(e.getMessage(),e);
//			e.printStackTrace();
//			fail("Failed to update activity status");
//		}
//	}
	
	
	/**
	 * Testcases using Mock Objects
	 * 
	 *//*
		
	public void testEmptyObjectInInsertInSpecimenCollectionGroup(){
		domainObject = new SpecimenCollectionGroup();
		testEmptyDomainObjectInInsert(domainObject);
	}
	
	public void testNullObjectInInsertInSpecimenCollectionGroup(){
		domainObject = new SpecimenCollectionGroup();
		testNullDomainObjectInInsert(domainObject);
	}
	
	public void testNullSessionDatBeanInInsertInSpecimenCollectionGroup(){
		domainObject = new SpecimenCollectionGroup();
		testNullSessionDataBeanInInsert(domainObject);
	}
	
	public void testNullSessionDataBeanInUpdateSpecimenInCollectionGroup(){
		domainObject = new SpecimenCollectionGroup();
		testNullSessionDataBeanInUpdate(domainObject);
	}
	
	public void testNullOldDomainObjectInUpdateSInpecimenCollectionGroup(){
		domainObject = new SpecimenCollectionGroup();
		testNullOldDomainObjectInUpdate(domainObject);
	}
	
	public void testNullCurrentDomainObjectInUpdateInSpecimenCollectionGroup(){
		domainObject = new SpecimenCollectionGroup();
		testNullCurrentDomainObjectInUpdate(domainObject);
	}
	
	public void testWrongDaoTypeInUpdateInSpecimenCollectionGroup(){
		domainObject = new SpecimenCollectionGroup();
		testNullCurrentDomainObjectInUpdate(domainObject);
	}
	
	public void testEmptyCurrentDomainObjectInUpdateInCollectionProtocolRegistration(){
		domainObject = new SpecimenCollectionGroup();
		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initSpecimenCollectionGroup();
		testEmptyCurrentDomainObjectInUpdate(domainObject, initialisedDomainObject);
	}
	
	public void testEmptyOldDomainObjectInUpdateInCollectionProtocolRegistration(){
		domainObject = new SpecimenCollectionGroup();
		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initSpecimenCollectionGroup();
		testEmptyOldDomainObjectInUpdate(domainObject, initialisedDomainObject);
	}
*/
}
