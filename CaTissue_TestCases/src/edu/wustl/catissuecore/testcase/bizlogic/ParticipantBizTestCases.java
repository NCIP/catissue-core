package edu.wustl.catissuecore.testcase.bizlogic;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.common.util.HQLCriteria;


public class ParticipantBizTestCases extends CaTissueSuiteBaseTest {

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
			 System.out
					.println("ParticipantTestCases.testAddParticipantWithCPR()"+e.getMessage());
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testUpdateCPAssociatedCPRWithDeleteCollectionProtocolEvent()
	{
	    try 
		{
	    	CollectionProtocolRegistration cpr = (CollectionProtocolRegistration)TestCaseUtility.getObjectMap(CollectionProtocolRegistration.class);
	    	CollectionProtocol collectionProtocol = cpr.getCollectionProtocol();
	    	Collection cpeCollection = collectionProtocol.getCollectionProtocolEventCollection();
	    	Iterator itr = cpeCollection.iterator();
	    	Collection collectionProtocolEventList = new LinkedHashSet();
	    	while(itr.hasNext())
	    	{
	    		collectionProtocolEventList.add(itr.next());
	    	}
			CollectionProtocolEvent collectionProtocolEvent = null;
			for(int specimenEventCount = 0 ;specimenEventCount<2 ;specimenEventCount++)
			{
				collectionProtocolEvent= new CollectionProtocolEvent();
				BaseTestCaseUtility.setCollectionProtocolEvent(collectionProtocolEvent);
				collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
				collectionProtocolEventList.add(collectionProtocolEvent);
			}
			collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventList);
	    	CollectionProtocol updatedCollectionProtocol = (CollectionProtocol)appService.updateObject(collectionProtocol);
	    	Collection updatedCPECollection = updatedCollectionProtocol.getCollectionProtocolEventCollection();
	    	if(updatedCPECollection.size() > cpeCollection.size())
	    	{
	    		assertTrue("User can add events after participant regestered to CP", true);
	    		
	    	}
	    	else
	    	{
	    		fail("User can add events after participant regestered to CP");	
			}
	    	
	    } 
	    catch (Exception e)
	    {
	    	System.out
	    	.println("ParticipantTestCases.testUpdateCPAssociatedCPRWithDeleteCollectionProtocolEvent()" + e.getMessage());
	    	e.printStackTrace();
	    	fail("User can add events after participant regestered to CP");
	    }
	}
	
	/*public void testSearchParticipant()
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
           	System.out.println("ParticipantTestCases.testSearchParticipant()"+e.getMessage());
           	e.printStackTrace();
           	assertFalse("Does not find Domain Object", true);
	 		
          }
	}
	*/
	public void testMatchingParticipant() {
		List<?> resultList1 = null;
		Participant  participant=new Participant();
		try {
			resultList1=appService.getParticipantMatchingObects(participant);
			System.out.println("testMatchingParticipant resultList1 "+ resultList1);
			for(int i=0;i<resultList1.size();i++)
			{
				System.out.println(resultList1.get(i));
			}
			assertTrue("Macthing participant list is retrieved using API", true);
			
		}catch(Exception e)
		{
			System.out.println("testMatchingParticipant"+ e.getMessage());
			e.printStackTrace();
			assertFalse("Not able to retrieve matching participant list using API", true);
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
	       	System.out.println("ParticipantTestCases.testUpdateParticipant()"+e.getMessage());
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
	

	public void testUpdateCPRWithBarcode()
	{
		Participant participant =  BaseTestCaseUtility.initParticipantWithCPR();
		

    	Logger.out.info("updating domain object------->"+participant);
	    try 
		{
	    	participant = (Participant) appService.createObject(participant);
	    	BaseTestCaseUtility.updateParticipant(participant);
			Collection collectionProtocolRegistrationCollection = new HashSet();
			CollectionProtocolRegistration collectionProtocolRegistration=(CollectionProtocolRegistration)participant.getCollectionProtocolRegistrationCollection().iterator().next();
			collectionProtocolRegistrationCollection.add(collectionProtocolRegistration);
			collectionProtocolRegistration.setBarcode("PATICIPANT"+UniqueKeyGeneratorUtil.getUniqueKey());
			participant.setCollectionProtocolRegistrationCollection(collectionProtocolRegistrationCollection);
	    	Participant updatedParticipant = (Participant) appService.updateObject(participant);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedParticipant);
	       	assertTrue("Domain object successfully updated ---->"+updatedParticipant, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	       	System.out
					.println("ParticipantTestCases.testUpdateCPRWithBarcode()"+e.getMessage());
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
	

	public void testUpdateCPRWithCaseSensitiveBarcode()
	{
		String uniqueKey=UniqueKeyGeneratorUtil.getUniqueKey();
		Participant participant1 =  BaseTestCaseUtility.initParticipantWithCPR();
		
    	Logger.out.info("updating domain object------->"+participant1);
	    try 
		{
	    	participant1 = (Participant) appService.createObject(participant1);
	    	BaseTestCaseUtility.updateParticipant(participant1);
			Collection collectionProtocolRegistrationCollection = new HashSet();
			CollectionProtocolRegistration collectionProtocolRegistration=(CollectionProtocolRegistration)participant1.getCollectionProtocolRegistrationCollection().iterator().next();
			collectionProtocolRegistration.setBarcode("CPR"+uniqueKey);
			collectionProtocolRegistrationCollection.add(collectionProtocolRegistration);
			participant1.setCollectionProtocolRegistrationCollection(collectionProtocolRegistrationCollection);
	    	Participant updatedParticipant1 = (Participant) appService.updateObject(participant1);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedParticipant1);
	       	assertTrue("Domain object successfully updated ---->"+updatedParticipant1, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	       	System.out
					.println("ParticipantTestCases.testUpdateCPRWithCaseSensitiveBarcode()"+e.getMessage());
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	    
	    Participant participant2 =  BaseTestCaseUtility.initParticipantWithCPR();
    	Logger.out.info("updating domain object------->"+participant2);
	    try 
		{
	    	participant2 = (Participant) appService.createObject(participant2);
	    	BaseTestCaseUtility.updateParticipant(participant2);
			Collection collectionProtocolRegistrationCollection2 = new HashSet();
			CollectionProtocolRegistration collectionProtocolRegistration2=(CollectionProtocolRegistration)participant2.getCollectionProtocolRegistrationCollection().iterator().next();
			collectionProtocolRegistrationCollection2.add(collectionProtocolRegistration2);
			collectionProtocolRegistration2.setBarcode("cpr"+uniqueKey);
			participant2.setCollectionProtocolRegistrationCollection(collectionProtocolRegistrationCollection2);
	    	Participant updatedParticipant2 = (Participant) appService.updateObject(participant2);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedParticipant2);
	       	assertTrue("Domain object successfully updated ---->"+updatedParticipant2, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	       	System.out
					.println("ParticipantTestCases.testUpdateCPRWithCaseSensitiveBarcode()"+e.getMessage());
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
        	//List specimenCollection = appService.query(hqlCriteria2, SpecimenCollectionGroup.class.getName());
        	List specimenCollection = appService.search(hqlQuery2);
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
			 System.out
					.println("ParticipantTestCases.testAddParticipantRegistrationWithUniquePMI() 1"+e.getMessage());
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
		 	 System.out
					.println("ParticipantTestCases.testAddParticipantRegistrationWithUniquePMI()"+e.getMessage());
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
		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
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
	
	
	public void testUpdateParticipantWithoutCollectionProtocol()
	{
		try
		{
			Participant participant=new Participant();
			participant.setFirstName("fisrtname"+UniqueKeyGeneratorUtil.getUniqueKey());
			participant.setLastName("lastName"+UniqueKeyGeneratorUtil.getUniqueKey());
			participant.setActivityStatus("Active");
			
			Collection participantMedicalIdentifierCollection = new HashSet();
			participant.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);
			participant=(Participant)appService.createObject(participant);
			participant.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);
			participant=(Participant) appService.updateObject(participant);
			assertTrue("Participant wihtout CPR updated successfully ", true);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			assertFalse("Can not update ", true);
		}
		
	}
	
	public void testUpdateParticipantWithPMI()
	{
		try
		{
			Participant participant=new Participant();
			participant.setFirstName("fisrtname"+UniqueKeyGeneratorUtil.getUniqueKey());
			participant.setLastName("lastName"+UniqueKeyGeneratorUtil.getUniqueKey());
			participant.setActivityStatus("Active");
			
			participant.setParticipantMedicalIdentifierCollection(new HashSet());
			participant=(Participant)appService.createObject(participant);
			
			Participant participant2=new Participant();
			participant2.setId(participant.getId());
			
			String query = "from edu.wustl.catissuecore.domain.Participant as participant where "
 				+ "participant.id="+participant.getId();	
        	 List resultList = appService.search(query);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		  participant2 = (Participant) resultsIterator.next();
        	 }
        	 
			//participant2=(Participant)appService.search(Participant.class, participant2).iterator().next();
			
			Collection participantMedicalIdentifierCollection = new HashSet();
			ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
			Site site =(Site)  TestCaseUtility.getObjectMap(Site.class);
			pmi.setSite(site);
			
			System.out.println("Site is "+site.getName());
			String mrn="mrn"+UniqueKeyGeneratorUtil.getUniqueKey();
			pmi.setMedicalRecordNumber(mrn);
			pmi.setParticipant(participant2);
			pmi.setParticipant(participant2);
			participantMedicalIdentifierCollection.add(pmi);
			participant2.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);	
			participant2=(Participant) appService.updateObject(participant2);
			
			Participant participant3=new Participant();
			participant3.setId(participant.getId());
			
			 query = "from edu.wustl.catissuecore.domain.Participant as participant where "
 				+ "participant.id="+participant.getId();	
        	  resultList = appService.search(query);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		  participant3 = (Participant) resultsIterator.next();
        	 }
        	 
			//participant3=(Participant)appService.search(Participant.class, participant3).iterator().next();
			
			Collection pmiCollection=participant3.getParticipantMedicalIdentifierCollection();
			if(!pmiCollection.isEmpty())
			{
				Iterator<ParticipantMedicalIdentifier> pmiIterator=  pmiCollection.iterator();
				
				while(pmiIterator.hasNext())
				{
					ParticipantMedicalIdentifier pmIdentifier=pmiIterator.next();
				
					if(pmIdentifier.getMedicalRecordNumber()!=null&&pmIdentifier.getSite().getId()!=null)
					{
						if(pmIdentifier.getMedicalRecordNumber().equals(mrn)&&pmIdentifier.getSite().getId().equals(site.getId()))
						{
								
							System.out.println("Succecfully updated");
							assertTrue("Participant wiht medical identifier updated successfully ", true);
									
						}
					}
				}
			}
		
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			assertFalse("Can not update ", true);
			
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
