package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
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
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.common.util.HQLCriteria;


public class ParticipantTestCases extends CaTissueBaseTestCase {

	AbstractDomainObject domainObject = null;
	
	public void testAddParticipantWithCPR()
	{
		try{
			Participant participant= BaseTestCaseUtility.initParticipantWithCPR();			
			System.out.println(participant);
			participant = (Participant) appService.createObject(participant); 
			Collection collectionProtocolRegistrationCollection = participant.getCollectionProtocolRegistrationCollection();
			Iterator cprItr = collectionProtocolRegistrationCollection.iterator();
			CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration)cprItr.next();
			
			TestCaseUtility.setObjectMap(collectionProtocolRegistration, CollectionProtocolRegistration.class);
			TestCaseUtility.setObjectMap(participant, Participant.class);
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testUpdateCPRAssociatedCPWithDeleteCollectionProtocolEvent()
	{
	    try 
		{
	    	CollectionProtocolRegistration cpr = (CollectionProtocolRegistration)TestCaseUtility.getObjectMap(CollectionProtocolRegistration.class);
	    	CollectionProtocol collectionProtocol = cpr.getCollectionProtocol();
	    	Collection cpeCollection = collectionProtocol.getCollectionProtocolEventCollection();
	    	Iterator itr = cpeCollection.iterator();
	    	Collection collectionProtocolEventList = new LinkedHashSet();
	    	collectionProtocolEventList.add(itr.next());
	    	collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventList);
	    	CollectionProtocol updatedCollectionProtocol = (CollectionProtocol)appService.updateObject(collectionProtocol);
	    	Collection updatedCPECollection = updatedCollectionProtocol.getCollectionProtocolEventCollection();
	    	if(updatedCPECollection.size()!= cpeCollection.size())
	    	{
	    		fail("User cannot not add/edit events");
	    	}
	    	assertTrue("User cannot not add/edit events", true);
	    } 
	    catch (Exception e)
	    {
	    	System.out.println("TestCaseTesting.testdeleteCollectionProtocolEvent()"+ e.getMessage());
	    	e.printStackTrace();
	    	fail("User cannot not add/edit events");
	    }
	}
	
	public void testSearchParticipant()
	{
		Participant participant = new Participant();
    	Logger.out.info(" searching domain object");
    	participant.setId(new Long(1));
   
         try {
        	 List resultList = appService.search(Participant.class,participant);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Participant returnedParticipant = (Participant) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedParticipant.getFirstName() +" "+returnedParticipant.getLastName());
        		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
             }
          } 
          catch (Exception e) {
           	Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Does not find Domain Object", true);
	 		
          }
	}
	
	public void testMatchingParticipant() {
		List<?> resultList1 = null;
		Participant  participant=new Participant();
		try {
			resultList1=appService.getParticipantMatchingObects(participant);
			for(int i=0;i<resultList1.size();i++)
			{
				System.out.println(resultList1.get(i));
			}
			assertTrue("Macthing participant list is retrieved using API", true);
			
		}catch(Exception e)
		{
			assertFalse("Not able to retrieve mating participant list using API", true);
		}
		
	}
	
	
	public void testUpdateParticipant()
	{
		Participant participant =  BaseTestCaseUtility.initParticipantWithCPR();
    	Logger.out.info("updating domain object------->"+participant);
	    try 
		{
	    	participant = (Participant) appService.createObject(participant);
	    	BaseTestCaseUtility.updateParticipant(participant);
	    	Participant updatedParticipant = (Participant) appService.updateObject(participant);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedParticipant);
	       	assertTrue("Domain object successfully updated ---->"+updatedParticipant, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
	
	
	public void testCollectedSCGAndNoOfChildSpecimens()
	{
		Participant participant =  BaseTestCaseUtility.initParticipantWithCPR();
		CollectionProtocolRegistration cpr = (CollectionProtocolRegistration)participant.getCollectionProtocolRegistrationCollection().iterator().next();
		System.out.println(cpr.getCollectionProtocol().getId());
		try
		{
			participant = (Participant) appService.createObject(participant);
			Collection cprCollection = participant.getCollectionProtocolRegistrationCollection();
			Iterator cprCPR = cprCollection.iterator();
			CollectionProtocolRegistration cprObj  = null;
			while(cprCPR.hasNext())
			{
				cprObj = (CollectionProtocolRegistration)cprCPR.next();
			}
			System.out.println("Befor");
			Collection scgCollection  = cprObj.getSpecimenCollectionGroupCollection();
			Iterator scgItr = scgCollection.iterator();
			SpecimenCollectionGroup sprObj  = null;
			while(scgItr.hasNext())
			{
				sprObj = (SpecimenCollectionGroup)scgItr.next();
			}
			
			
			TestCaseUtility.setObjectMap(sprObj, SpecimenCollectionGroup.class);
			TestCaseUtility.setObjectMap(participant, Participant.class);
			
			String hqlQuery2="select elements(scg.specimenCollection)" +
			" from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg" +
	    	" where scg.id="+sprObj.getId();
        	
            System.out.println("-------------------");
        	HQLCriteria hqlCriteria2= new HQLCriteria(hqlQuery2);
        	List specimenCollection = appService.query(hqlCriteria2, SpecimenCollectionGroup.class.getName());
        	System.out.println("-------------------"+specimenCollection);
        	
        	System.out.println("Criteria executed");
			
			//Iterator itr = sprObj.getSpecimenCollection().iterator();
        	Iterator itr =specimenCollection.iterator();
			Specimen sp = null;
			boolean specimenAdded=false;
			while(itr.hasNext())
			{
				sp = (Specimen)itr.next();
				if(sp.getLineage().equals("New")&&!specimenAdded)
				{
					TestCaseUtility.setObjectMap(sp, Specimen.class);
					System.out.println("This is Specimen"+sp.getId());
					specimenAdded=true;
				}
			}
			
			if(sp==null)
			{
				assertFalse("No Specimens created under SCG", true);
			}
			
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
	 		System.out.println("ParticipantTestCases.testCollectedSCGAndNoOfChildSpecimens() "+e.getMessage());
			e.printStackTrace();
	 		assertFalse("Error occured in executing test case", true);
		}
	}
	
	/*public void testAddParticipantWithUniquePMI()
	{
		try{
			Participant participant= BaseTestCaseUtility.initParticipant();
			Collection participantMedicalIdentifierCollection = new HashSet();
			ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
			Site site =(Site)  TestCaseUtility.getObjectMap(Site.class);
			pmi.setSite(site);
			pmi.setMedicalRecordNumber("1234");
			pmi.setParticipant(participant);
			participantMedicalIdentifierCollection.add(pmi);						
			participant.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);	
			participant = (Participant) appService.createObject(participant); 
			assertTrue("Object created successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testAddParticipantWithDuplicatePMI()
	{
		try{
			Participant participant= BaseTestCaseUtility.initParticipant();
			Collection participantMedicalIdentifierCollection = new HashSet();
			ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
			Site site =(Site)  TestCaseUtility.getObjectMap(Site.class);
			pmi.setSite(site);
			pmi.setMedicalRecordNumber("1234");
			pmi.setParticipant(participant);
			participantMedicalIdentifierCollection.add(pmi);						
			participant.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);				
			participant = (Participant) appService.createObject(participant); 
			assertFalse("Object created successfully", true);			
		 }
		 catch(Exception e){
			 e.printStackTrace();			 
			 assertTrue("Submission failed because Participant with same PMI is already exist", true);
		 }
	}*/
	
	public void testAddParticipantRegistrationWithUniquePMI()
	{
		
		Participant participant= BaseTestCaseUtility.initParticipant();		
		Collection participantMedicalIdentifierCollection = new HashSet();
		ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
		Site site =(Site)  TestCaseUtility.getObjectMap(Site.class);
		pmi.setSite(site);
		pmi.setMedicalRecordNumber("1234");
		pmi.setParticipant(participant);
		participantMedicalIdentifierCollection.add(pmi);						
		participant.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);
		try{	
			participant = (Participant) appService.createObject(participant);
		 }
		 	catch(Exception e){
			 e.printStackTrace();			 
			 assertFalse("Unable to create participant", true);
		 }
			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

			CollectionProtocol collectionProtocol =(CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class); 
			collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);

			collectionProtocolRegistration.setParticipant(participant);

			collectionProtocolRegistration.setProtocolParticipantIdentifier("");
					
			collectionProtocolRegistration.setActivityStatus("Active");
			try
			{
				collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975",
						Utility.datePattern("08/15/1975")));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			//Setting Consent Tier Responses.
			try
			{
				collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006",Utility.datePattern("11/23/2006")));
			}
			catch (ParseException e)
			{			
				e.printStackTrace();
			}
			collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
			User user = (User)TestCaseUtility.getObjectMap(User.class);
			collectionProtocolRegistration.setConsentWitness(user);
			
			Collection consentTierResponseCollection = new HashSet();
			Collection consentTierCollection = collectionProtocol.getConsentTierCollection();
			Iterator consentTierItr = consentTierCollection.iterator();
			while(consentTierItr.hasNext())
			{
				ConsentTier consentTier = (ConsentTier)consentTierItr.next();
				ConsentTierResponse consentResponse = new ConsentTierResponse();
				consentResponse.setConsentTier(consentTier);
				consentResponse.setResponse("Yes");
				consentTierResponseCollection.add(consentResponse);
			}
			collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
		try
		  {		
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
			assertTrue("Participant Registration successfully created",true);
		 }
		 catch(Exception e)
		 {
		 	 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testParticipantRegistrationWithDuplicatePMI()
	{
		
		Participant participant= BaseTestCaseUtility.initParticipant();		
		Collection participantMedicalIdentifierCollection = new HashSet();
		ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
		Site site =(Site)  TestCaseUtility.getObjectMap(Site.class);
		pmi.setSite(site);
		pmi.setMedicalRecordNumber("1234");
		pmi.setParticipant(participant);
		participantMedicalIdentifierCollection.add(pmi);						
		participant.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);
		try{	
			participant = (Participant) appService.createObject(participant);
		 }
		 	catch(Exception e){
			 e.printStackTrace();			 
			 assertTrue("Unable to create participant", true);
		 }
			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

			CollectionProtocol collectionProtocol =(CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class); 
			collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);

			collectionProtocolRegistration.setParticipant(participant);

			collectionProtocolRegistration.setProtocolParticipantIdentifier("");
			
			collectionProtocolRegistration.setActivityStatus("Active");
			try
			{
				collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975",
						Utility.datePattern("08/15/1975")));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			//Setting Consent Tier Responses.
			try
			{
				collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006",Utility.datePattern("11/23/2006")));
			}
			catch (ParseException e)
			{			
				e.printStackTrace();
			}
			collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
			User user = (User)TestCaseUtility.getObjectMap(User.class);
			collectionProtocolRegistration.setConsentWitness(user);
			
			Collection consentTierResponseCollection = new HashSet();
			Collection consentTierCollection = collectionProtocol.getConsentTierCollection();
			Iterator consentTierItr = consentTierCollection.iterator();
			while(consentTierItr.hasNext())
			{
				ConsentTier consentTier = (ConsentTier)consentTierItr.next();
				ConsentTierResponse consentResponse = new ConsentTierResponse();
				consentResponse.setConsentTier(consentTier);
				consentResponse.setResponse("Yes");
				consentTierResponseCollection.add(consentResponse);
			}
			collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
		try
		  {		
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
			assertFalse("Shouldnot create object", true);
			
		 }
		 catch(Exception e)
		 {
		 	 e.printStackTrace();
			 assertTrue("Participant with same medical identifier ia already present", true);
		 }
	}
	
	public void testParticipantRegistrationWithUniquePPI()
	{
		
		Participant participant= BaseTestCaseUtility.initParticipant();		
		try{	
			participant = (Participant) appService.createObject(participant);
		 }
		 	catch(Exception e){
			 e.printStackTrace();			 
			 assertFalse("Unable to create participant", true);
		 }
			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

			CollectionProtocol collectionProtocol =(CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class); 
			collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);

			collectionProtocolRegistration.setParticipant(participant);

			collectionProtocolRegistration.setProtocolParticipantIdentifier("123");
			collectionProtocolRegistration.setActivityStatus("Active");
			try
			{
				collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975",
						Utility.datePattern("08/15/1975")));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			//Setting Consent Tier Responses.
			try
			{
				collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006",Utility.datePattern("11/23/2006")));
			}
			catch (ParseException e)
			{			
				e.printStackTrace();
			}
			collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
			User user = (User)TestCaseUtility.getObjectMap(User.class);
			collectionProtocolRegistration.setConsentWitness(user);
			
			Collection consentTierResponseCollection = new HashSet();
			Collection consentTierCollection = collectionProtocol.getConsentTierCollection();
			Iterator consentTierItr = consentTierCollection.iterator();
			while(consentTierItr.hasNext())
			{
				ConsentTier consentTier = (ConsentTier)consentTierItr.next();
				ConsentTierResponse consentResponse = new ConsentTierResponse();
				consentResponse.setConsentTier(consentTier);
				consentResponse.setResponse("Yes");
				consentTierResponseCollection.add(consentResponse);
			}
			collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
		try
		  {		
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
			
		 }
		 catch(Exception e)
		 {
		 	 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testParticipantRegistrationWithDuplicatePPI()
	{
		
		Participant participant= BaseTestCaseUtility.initParticipant();		
		try{	
			participant = (Participant) appService.createObject(participant);
		 }
		 	catch(Exception e){
			 e.printStackTrace();			 
			 assertFalse("Unable to create participant", true);
		 }
			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

			CollectionProtocol collectionProtocol =(CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class); 
			collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);

			collectionProtocolRegistration.setParticipant(participant);

			collectionProtocolRegistration.setProtocolParticipantIdentifier("123");
			collectionProtocolRegistration.setActivityStatus("Active");
			try
			{
				collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975",
						Utility.datePattern("08/15/1975")));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			//Setting Consent Tier Responses.
			try
			{
				collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006",Utility.datePattern("11/23/2006")));
			}
			catch (ParseException e)
			{			
				e.printStackTrace();
			}
			collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
			User user = (User)TestCaseUtility.getObjectMap(User.class);
			collectionProtocolRegistration.setConsentWitness(user);
			
			Collection consentTierResponseCollection = new HashSet();
			Collection consentTierCollection = collectionProtocol.getConsentTierCollection();
			Iterator consentTierItr = consentTierCollection.iterator();
			while(consentTierItr.hasNext())
			{
				ConsentTier consentTier = (ConsentTier)consentTierItr.next();
				ConsentTierResponse consentResponse = new ConsentTierResponse();
				consentResponse.setConsentTier(consentTier);
				consentResponse.setResponse("Yes");
				consentTierResponseCollection.add(consentResponse);
			}
			collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
		try
		  {		
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
			assertFalse("Failed to create CPR with duplicate PPI", true);
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
		 	 e.printStackTrace();
			 assertTrue("should not not create object", true);
		 }
	}
	
	
	
	
/*	public void testInvalidParticipantActivityStatus()
	{
		try{
			Participant participant = BaseTestCaseUtility.initParticipant();		
			participant.setActivityStatus("Invalid");
			System.out.println(participant);
			participant = (Participant) appService.createObject(participant); 
			assertFalse("Test Failed.Invalid Activity status should thorw Exception", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Name is required", true);
		 }
	}

*/		
	
}
