package edu.wustl.catissuecore.bizlogic.test;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
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
		
		CollectionProtocol collectionProtocol = (CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class);
		Collection consentTierCollection = collectionProtocol.getConsentTierCollection();
		Iterator consentTierItr = consentTierCollection.iterator();
		Collection consentTierStatusCollection = new HashSet();
		while(consentTierItr.hasNext())
		{
			ConsentTier consentTier = (ConsentTier)consentTierItr.next();
			ConsentTierStatus consentStatus = new ConsentTierStatus();
			consentStatus.setConsentTier(consentTier);
			consentStatus.setStatus("No");
			consentTierStatusCollection.add(consentStatus);
		}
		sprObj.setConsentTierStatusCollection(consentTierStatusCollection);
		sprObj.getCollectionProtocolRegistration().getCollectionProtocol().setId(new Long(1));
		sprObj.getCollectionProtocolRegistration().setParticipant(participant);
		Collection collectionProtocolEventList = new LinkedHashSet();
		
		
		Site site = (Site)TestCaseUtility.getObjectMap(Site.class); 
		//new Site();
		//site.setId(new Long(1));
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
	
	public void testSearchSpecimenCollectionGroup()
    {
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
    	SpecimenCollectionGroup cachedSCG = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
    	scg.setId((Long) cachedSCG.getId());
     	Logger.out.info(" searching domain object");
    	 try {
        	 List resultList = appService.search(SpecimenCollectionGroup.class, scg);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 SpecimenCollectionGroup returnedSCG = (SpecimenCollectionGroup) resultsIterator.next();
        		 System.out.println("here-->" + returnedSCG.getName() +"Id:"+returnedSCG.getId());
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedSCG.getName());
             }
        	 assertTrue("SCG found", true);
          } 
          catch (Exception e) {
        	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("Couldnot found Specimen", true);  
          }

    }
	
	public void testAddSCGWithDuplicateName()
	{
		
		try{
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup)BaseTestCaseUtility.initSCG();		    
		    	
		  //  TestCaseUtility.setObjectMap(scg, SpecimenCollectionGroup.class);
		    SpecimenCollectionGroup duplicateSCG = (SpecimenCollectionGroup)BaseTestCaseUtility.initSCG();
		    duplicateSCG.setName(scg.getName());
		    scg = (SpecimenCollectionGroup)appService.createObject(scg);
		    duplicateSCG = (SpecimenCollectionGroup)appService.createObject(duplicateSCG);
		    System.out.println("After Creating SCG");
			fail("Test Failed. Duplicate SCG name should throw exception");
		}
		 catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Submission failed since a Collection Protocol with the same NAME already exists" , true);
			 
		 }
    	
	}
	
	public void testUpdateSCGWithClosedActivityStatus()
	{
		
		try{
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup)BaseTestCaseUtility.initSCG();	
			//scg.setActivityStatus("Closed");
		    scg = (SpecimenCollectionGroup)appService.createObject(scg);
		    Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
		    scg.setSpecimenCollectionSite(site);
		    CollectionProtocol collectionProtocol = (CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class);
		    Participant participant = (Participant)TestCaseUtility.getObjectMap(Participant.class);
		    scg.getCollectionProtocolRegistration().getCollectionProtocol().setId(new Long(1));
		    scg.getCollectionProtocolRegistration().setParticipant(participant);
		    scg.setActivityStatus("Closed");
		    scg = (SpecimenCollectionGroup)appService.updateObject(scg);
		    assertTrue("Should throw Exception", true);
			
		}
		 catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();			
			assertFalse("While adding SCG Activity status should be Active" , true);
		 }
    	
	}
	
	public void testUpdateSCGWithDisabledActivityStatus()
	{
		
		try{
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup)BaseTestCaseUtility.initSCG();	
			//scg.setActivityStatus("Closed");
		    scg = (SpecimenCollectionGroup)appService.createObject(scg);
		    Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
		    scg.setSpecimenCollectionSite(site);
		    CollectionProtocol collectionProtocol = (CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class);
		    Participant participant = (Participant)TestCaseUtility.getObjectMap(Participant.class);
		    scg.getCollectionProtocolRegistration().getCollectionProtocol().setId(new Long(1));
		    scg.getCollectionProtocolRegistration().setParticipant(participant);
		    scg.setActivityStatus("Disabled");
		    scg = (SpecimenCollectionGroup)appService.updateObject(scg);
		    assertTrue("Should throw Exception", true);
			
		}
		 catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			System.out.println(e);
			e.printStackTrace();
			assertFalse("While adding SCG Activity status should be Active", true);
			 
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
	
	
}
	