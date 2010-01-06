package edu.wustl.catissuecore.api.test;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.util.Utility;
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
			System.out
					.println("SpecimenCollectGroupTestCases.testUpdateSpecimenCollectionGroupWithConsents()"+e.getMessage());
			e.printStackTrace();
			fail("Failed to add Domain Object");
		}
	}
	
	public void testAddSpecimenCollectionGroupWithPPID()
	{
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		System.out.println("Before Creating SCG");
	    CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) TestCaseUtility.getObjectMap(CollectionProtocolRegistration.class);
	    System.out.println("cpr.getProtocolParticipantIdentifier() !!!"+cpr.getProtocolParticipantIdentifier());
	    scg =(SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(cpr);
	    try
		{
	    	 cpr.setId(null);
	    	 Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
	 	     scg.setSpecimenCollectionSite(site);
	 	     scg.setName("SCG1"+UniqueKeyGeneratorUtil.getUniqueKey());		    
	 	     scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);
	 	     scg = (SpecimenCollectionGroup)appService.createObject(scg);
	    	 System.out.println("After Creating SCG with PPID!!!!!! "+scg.getId());
	    	 assertTrue("Add SpecimenCollectionGroupWithPPID having CPR id as null.", true);    
		}
		catch(Exception e)
		{
			System.out.println("Error Creating SCG with PPID!!!!!!");
			e.printStackTrace();
			fail("Failed to add SpecimenCollectionGroupWithPPID having CPR id as null.");
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
		sprObj.getCollectionProtocolRegistration().getCollectionProtocol();
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
        	System.out
					.println("SpecimenCollectGroupTestCases.testSearchSpecimenCollectionGroup()"+e.getMessage());
	 		e.printStackTrace();
	 		assertFalse("Couldnot found Specimen", true);
          }

    }

	public void testUpdateSpecimenCollectionGroupWithBarcode()
	{
    	SpecimenCollectionGroup cachedSCG = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
    	cachedSCG.setBarcode("SCG"+UniqueKeyGeneratorUtil.getUniqueKey());
     	Logger.out.info(" searching domain object");
    	 try {
    		 SpecimenCollectionGroup scg = (SpecimenCollectionGroup)appService.updateObject(cachedSCG);
    		 assertTrue("SCG found", true);
          }
          catch (Exception e) {
        	Logger.out.error(e.getMessage(),e);
        	System.out
					.println("SpecimenCollectGroupTestCases.testUpdateSpecimenCollectionGroupWithBarcode()"+e.getMessage());
	 		e.printStackTrace();
	 		assertFalse("Couldnot found Specimen", true);
          }
	}
	public void testUpdateSCGWithCaseSensitiveBarcode()
	{
		String uniqueKey=UniqueKeyGeneratorUtil.getUniqueKey();
		SpecimenCollectionGroup cachedSCG = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
    	cachedSCG.setBarcode("SCG"+uniqueKey);
     	Logger.out.info(" searching domain object");
    	 try {
    		 	cachedSCG= (SpecimenCollectionGroup)appService.updateObject(cachedSCG);
    		 	assertTrue("SCG  updated", true);

    		 	SpecimenCollectionGroup scg = (SpecimenCollectionGroup)BaseTestCaseUtility.initSCG();
    		 	scg = (SpecimenCollectionGroup)appService.createObject(scg);
    		 	scg.setBarcode("scg"+uniqueKey);
  		   	    scg = (SpecimenCollectionGroup)appService.updateObject(scg);

  		}
  		 catch(Exception e){
  			Logger.out.error(e.getMessage(),e);
  			System.out
					.println("SpecimenCollectGroupTestCases.testUpdateSCGWithCaseSensitiveBarcode()"+e.getMessage());
  			e.printStackTrace();
  			assertTrue("Can not update case sensitive scg" , true);


  		 }
	}

	public void testSearchScgWithBarcode()
	{
		SpecimenCollectionGroup specimenCollectionGroup=new SpecimenCollectionGroup();
		specimenCollectionGroup.setBarcode(((SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class)).getBarcode());
		 try {
        	 List resultList = appService.search(SpecimenCollectionGroup.class, specimenCollectionGroup);
        	 if(resultList!=null)
        	 {
        		 if(resultList.size()==1)
        		 {

        			 assertFalse("SCG found with case sensitive barcode", true);
        		 }
        		 else{
        			 assertTrue("All the matches find for the given barcode" , true);
        		 }
        	 }
          }
          catch (Exception e) {
        	Logger.out.error(e.getMessage(),e);
        	System.out
					.println("SpecimenCollectGroupTestCases.testSearchScgWithBarcode()"+e.getMessage());
	 		e.printStackTrace();
	 		assertFalse("Could not found SCG", true);
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
		    assertTrue("Submission doe not fail since label generator already present" , true);
		    TestCaseUtility.setObjectMap(scg, SpecimenCollectionGroup.class);
		}
		 catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			System.out
					.println("SpecimenCollectGroupTestCases.testAddSCGWithDuplicateName()"+e.getMessage());
			e.printStackTrace();
			fail("Test Failed. Duplicate SCG name should not throw exception"+e.getMessage());


		 }

	}

	public void testUpdateSCGWithDuplicateName()
	{

		try{
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup)BaseTestCaseUtility.initSCG();

		  //  TestCaseUtility.setObjectMap(scg, SpecimenCollectionGroup.class);
		    SpecimenCollectionGroup duplicateSCG = (SpecimenCollectionGroup)BaseTestCaseUtility.initSCG();
		    duplicateSCG.setName(scg.getName());
		    scg = (SpecimenCollectionGroup)appService.createObject(scg);
		    duplicateSCG = (SpecimenCollectionGroup)appService.createObject(duplicateSCG);
		    duplicateSCG.setName(((SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class)).getName());
		    duplicateSCG=(SpecimenCollectionGroup)appService.updateObject(duplicateSCG);
		    fail("Update should fails since same name already exist");
		}
		 catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			System.out
					.println("SpecimenCollectGroupTestCases.testUpdateSCGWithDuplicateName()"+e.getMessage());
			assertTrue("Should throw Exception", true);

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
		    scg.getCollectionProtocolRegistration().getCollectionProtocol();
		    scg.getCollectionProtocolRegistration().setParticipant(participant);
		    scg.setActivityStatus("Closed");
		    scg = (SpecimenCollectionGroup)appService.updateObject(scg);
		    assertTrue("Should throw Exception", true);

		}
		 catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			System.out
					.println("SpecimenCollectGroupTestCases.testUpdateSCGWithClosedActivityStatus()"+e.getMessage());
			e.printStackTrace();
			assertFalse("While adding SCG Activity status should be Active"+e.getMessage() , true);
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
		    scg.getCollectionProtocolRegistration().getCollectionProtocol();
		    scg.getCollectionProtocolRegistration().setParticipant(participant);
		    scg.setActivityStatus("Disabled");
		    scg = (SpecimenCollectionGroup)appService.updateObject(scg);
		    assertTrue("SCG contains specimen so should fail", true);
		}
		 catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			System.out.println(e);
			e.printStackTrace();
			assertFalse("Should not throw Exception", true);
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

	public void testAddSCGWithNameAndEvents()
	{
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		try{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			System.out
					.println("SpecimenCollectGroupTestCases.testAddSCGWithNameAndEvents() 1"+e.getMessage());
           	e.printStackTrace();
           	assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("CP:"+cp.getTitle());
		Participant participant = BaseTestCaseUtility.initParticipant();

		try{
			participant = (Participant) appService.createObject(participant);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			System.out
					.println("SpecimenCollectGroupTestCases.testAddSCGWithNameAndEvents() 2"+e.getMessage());
           	e.printStackTrace();
           	assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("Participant:"+participant.getFirstName());
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		collectionProtocolRegistration.setCollectionProtocol(cp);
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		collectionProtocolRegistration.setActivityStatus("Active");
		try
		{
			collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975",
					Utility.datePattern("08/15/1975")));
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
		Collection consentTierCollection = cp.getConsentTierCollection();
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

		collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
		System.out.println("Creating CPR");
		try{
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to register participant", true);
		}

		SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
		specimenCollectionGroup =(SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(collectionProtocolRegistration);
		Site site = (Site)TestCaseUtility.getObjectMap(Site.class);
		specimenCollectionGroup.setSpecimenCollectionSite(site);
		String scgName="scg added through api"+UniqueKeyGeneratorUtil.getUniqueKey();
		specimenCollectionGroup.setName(scgName);

		CollectionEventParameters collectionEventParameters = new CollectionEventParameters();

		collectionEventParameters.setUser(user);
		collectionEventParameters.setTimestamp(new Date(00,02,04));
		collectionEventParameters.setCollectionProcedure("Lavage");
		collectionEventParameters.setComment("collected");
		collectionEventParameters.setContainer("ACD Vacutainer");
		collectionEventParameters.setSpecimenCollectionGroup(specimenCollectionGroup);

		//Adding RECEIVED  Event PARAMETERS
		ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
		receivedEventParameters.setComment("received");
		receivedEventParameters.setReceivedQuality("Acceptable");
		receivedEventParameters.setTimestamp(new Date(00,02,04));
		receivedEventParameters.setUser(user);
		receivedEventParameters.setSpecimenCollectionGroup(specimenCollectionGroup);

		//COLLECTION OF SPCIMEN EVENT PARAMETERS COLLECTION
		Collection<SpecimenEventParameters> specimenEventParamsColl = new HashSet<SpecimenEventParameters>();
		specimenEventParamsColl.add(collectionEventParameters);
		specimenEventParamsColl.add(receivedEventParameters);
		specimenCollectionGroup.setSpecimenEventParametersCollection(specimenEventParamsColl);

		//specimenCollectionGroup = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(specimenCollectionGroup);
		try{
			int count=0;
			SpecimenCollectionGroup	specimenCollectionGroup1 = (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroup);

			Iterator iter=specimenCollectionGroup1.getSpecimenEventParametersCollection().iterator();
			while(iter.hasNext())
			{
				SpecimenEventParameters parameters=(SpecimenEventParameters) iter.next();
				if(parameters instanceof ReceivedEventParameters)
				{
					ReceivedEventParameters receivedEventParameters2=(ReceivedEventParameters)parameters;

					if(receivedEventParameters2.getTimestamp().equals(receivedEventParameters.getTimestamp())&&receivedEventParameters2.getComment().equals(receivedEventParameters.getComment())
							&&receivedEventParameters.getReceivedQuality().equals(receivedEventParameters2.getReceivedQuality())&&(receivedEventParameters2.getUser().getId()).equals(receivedEventParameters.getUser().getId())
							)
					{
						count++;
					}
					else
					{
						System.out.println("Received event parameter not retained");
					}
				}
				if(parameters instanceof CollectionEventParameters)
				{
					CollectionEventParameters collectionEventParameters2=(CollectionEventParameters)parameters;
					if(collectionEventParameters2.getCollectionProcedure().equals(collectionEventParameters.getCollectionProcedure())
							&&collectionEventParameters2.getComment().equals(collectionEventParameters.getComment())
							&&collectionEventParameters2.getContainer().equals(collectionEventParameters.getContainer())
							&&collectionEventParameters2.getTimestamp().equals(collectionEventParameters.getTimestamp())
							&&collectionEventParameters2.getUser().getId().equals(collectionEventParameters.getUser().getId()))
					{
						count++;
					}
					else
					{
						System.out.println("Collection event parameter not retained");
					}
				}
			}
			if(!specimenCollectionGroup1.getGroupName().equals(specimenCollectionGroup.getGroupName()))
			{
				count++;
			}
			if(count==3)
			{

				System.out.println("all the parameters retained"+ count);
				assertTrue("all the parameters retained", true);
			}

		}catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			System.out
					.println("SpecimenCollectGroupTestCases.testAddSCGWithNameAndEvents()"+e.getMessage());
			e.printStackTrace();
			assertFalse("SCG Name and Other parameters are not retained", true);
		}


	}

	public void testEditeCP() {
		try {
			ExcelTestCaseUtility.addAnticipatedSCGInParticipant();
			assertTrue("Domain object updated successfully", true);
		} catch (Exception e) {
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
			// assertFalse("Failed to update object",true);
			fail("Failed to update SCG object");
		}
	}


	/*public void testVerifyConsentResponseAndConsentStatusAtSCG()
	{
        System.out.println("Inside ConsentsVerificationTestCases:");
        CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		try{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("CP:"+cp.getTitle());
		TestCaseUtility.setObjectMap(cp, CollectionProtocol.class);

		SpecimenCollectionGroup scg = (SpecimenCollectionGroup) createSCGWithConsents(cp);
		CollectionProtocolRegistration collectionProtocolRegistration =
			(CollectionProtocolRegistration) TestCaseUtility.getObjectMap(CollectionProtocolRegistration.class);
		Collection consStatusCol = scg.getConsentTierStatusCollection();
		Collection consResponseCol = collectionProtocolRegistration.getConsentTierResponseCollection();

		Iterator consResItr = consResponseCol.iterator();
		Iterator consStatusItr = consStatusCol.iterator();

		ConsentTierStatus cs[]= new ConsentTierStatus[consStatusCol.size()];
		ConsentTierResponse rs[] = new ConsentTierResponse[consResponseCol.size()];
		int i = 0;
		System.out.println("Reached up to while");
		while(consStatusItr.hasNext())
		{
			cs[i] = (ConsentTierStatus) consStatusItr.next();
			rs[i] = (ConsentTierResponse) consResItr.next();
			i++;
		}

		for(int j = 0; j<cs.length; j++)
		{
			for(int k = 0; k<cs.length; k++)
			{
				if(cs[k].getConsentTier().getStatement().equals(rs[j].getConsentTier().getStatement()))
				{
					System.out.println("Statement:"+cs[k].getConsentTier().getStatement());
					assertEquals(cs[k].getStatus(), rs[j].getResponse());
				}
			}
		}

		TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		ts.setStorageContainer(null);
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
		ts.setAvailable(new Boolean("true"));
		System.out.println("Befor creating Tissue Specimen");
		try{
			ts = (TissueSpecimen) appService.createObject(ts);
			System.out.println("Spec:"+ts.getLabel());
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
			assertFalse("Failed to create", true);
		}
	}
	public void testVerifyConsentResopnseAndConsentStatusForUpadatedCP(){
		System.out.println("Inside ConsentsVerificationTestCases:");
        CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		try{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("CP:"+cp.getTitle());
		TestCaseUtility.setObjectMap(cp, CollectionProtocol.class);

		SpecimenCollectionGroup scg = (SpecimenCollectionGroup) createSCGWithConsents(cp);

		TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		ts.setStorageContainer(null);
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
		ts.setAvailable(new Boolean("true"));
		System.out.println("Befor creating Tissue Specimen");

		try{
			ts = (TissueSpecimen) appService.createObject(ts);
			System.out.println("Spec:"+ts.getLabel());
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
			assertFalse("Failed to create", true);
		}

		CollectionProtocol updatedCP = (CollectionProtocol) updateCP(cp);

		SpecimenCollectionGroup newSCG = (SpecimenCollectionGroup) createSCGWithConsents(updatedCP);

		TissueSpecimen ts1 =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		ts1.setStorageContainer(null);
		ts1.setSpecimenCollectionGroup(newSCG);
		ts1.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
		ts1.setAvailable(new Boolean("true"));
		System.out.println("Befor creating Tissue Specimen");

		try{
			ts = (TissueSpecimen) appService.createObject(ts1);
			System.out.println("Spec:"+ts.getLabel());
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
			assertFalse("Failed to create", true);
		}

		Collection consStatusCol = newSCG.getConsentTierStatusCollection();
		CollectionProtocolRegistration collectionProtocolRegistration =
			(CollectionProtocolRegistration) TestCaseUtility.getObjectMap(CollectionProtocolRegistration.class);
		Collection consResponseCol = collectionProtocolRegistration.getConsentTierResponseCollection();

		Iterator consResItr = consResponseCol.iterator();
		Iterator consStatusItr = consStatusCol.iterator();

		ConsentTierStatus cs[]= new ConsentTierStatus[consStatusCol.size()];
		ConsentTierResponse rs[] = new ConsentTierResponse[consResponseCol.size()];
		int i = 0;
		System.out.println("Reached up to while");
		while(consStatusItr.hasNext())
		{
			cs[i] = (ConsentTierStatus) consStatusItr.next();
			rs[i] = (ConsentTierResponse) consResItr.next();
			i++;
		}

		for(int j = 0; j<cs.length; j++)
		{
			for(int k = 0; k<cs.length; k++)
			{
				if(cs[k].getConsentTier().getStatement().equals(rs[j].getConsentTier().getStatement()))
				{
					System.out.println("Statements:"+cs[k].getConsentTier().getStatement());
					assertEquals(cs[k].getStatus(), rs[j].getResponse());
				}
			}
		}

	}

	public SpecimenCollectionGroup createSCGWithConsents(CollectionProtocol cp){

			Participant participant = BaseTestCaseUtility.initParticipant();

			try{
				participant = (Participant) appService.createObject(participant);
			}
			catch(Exception e){
				Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse("Failed to create collection protocol", true);
			}
			System.out.println("Participant:"+participant.getFirstName());
			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
			collectionProtocolRegistration.setCollectionProtocol(cp);
			collectionProtocolRegistration.setParticipant(participant);
			collectionProtocolRegistration.setProtocolParticipantIdentifier("");
			collectionProtocolRegistration.setActivityStatus("Active");
			try
			{
				collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975",
						Utility.datePattern("08/15/1975")));
				collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006",Utility.datePattern("11/23/2006")));

			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
			collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
			User user = (User)TestCaseUtility.getObjectMap(User.class);
			collectionProtocolRegistration.setConsentWitness(user);

			Collection consentTierResponseCollection = new LinkedHashSet();
			Collection consentTierCollection = new LinkedHashSet();
			consentTierCollection = cp.getConsentTierCollection();

//			Iterator ConsentierItr = consentTierCollection.iterator();
//
//			ConsentTier c1= (ConsentTier) ConsentierItr.next();
//			ConsentTierResponse r1 = new ConsentTierResponse();
//			r1.setResponse("Yes");
//			r1.setConsentTier(c1);
//			consentTierResponseCollection.add(r1);
//			ConsentTier c2= (ConsentTier) ConsentierItr.next();
//			ConsentTierResponse r2 = new ConsentTierResponse();
//			r2.setResponse("No");
//			consentTierResponseCollection.add(r2);
//			r2.setConsentTier(c2);
//			ConsentTier c3= (ConsentTier) ConsentierItr.next();
//			ConsentTierResponse r3 = new ConsentTierResponse();
//			r3.setResponse("Yes");
//			r3.setConsentTier(c3);
//			consentTierResponseCollection.add(r3);
			Iterator consentTierItr = consentTierCollection.iterator();
			 while(consentTierItr.hasNext())
			 {
				 ConsentTier consent= (ConsentTier) consentTierItr.next();
				 ConsentTierResponse response= new ConsentTierResponse();
				 response.setResponse("Yes");
				 response.setConsentTier(consent);
				 consentTierResponseCollection.add(response);
			 }

			collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);

			System.out.println("Creating CPR");
			try{
				collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
			}
			catch(Exception e){
				Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse("Failed to register participant", true);
			}
			TestCaseUtility.setObjectMap(collectionProtocolRegistration, CollectionProtocolRegistration.class);

			SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
			scg =(SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(collectionProtocolRegistration);
			Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
			scg.setSpecimenCollectionSite(site);
			scg.setName("New SCG"+UniqueKeyGeneratorUtil.getUniqueKey());
			scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);
			System.out.println("Creating SCG");
			try{
				scg = (SpecimenCollectionGroup) appService.createObject(scg);
			}
			catch(Exception e){
				Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse("Failed to register participant", true);
			}
			return scg;
	}



	public CollectionProtocol updateCP(CollectionProtocol collectionProtocol)
	{

		try
		{
			collectionProtocol = (CollectionProtocol) TestCaseUtility.getObjectMap(CollectionProtocol.class);
		   	Logger.out.info("updating domain object------->"+collectionProtocol);
		   	Collection ConCollection = collectionProtocol.getConsentTierCollection();
		   	ConsentTier c4 = new ConsentTier();
		   	c4.setStatement("consent for any research" );
		   	ConCollection.add(c4);
		   	collectionProtocol.setConsentTierCollection(ConCollection);
	    	collectionProtocol = (CollectionProtocol)appService.updateObject(collectionProtocol);
	    	System.out.println("after updation"+collectionProtocol.getTitle());
	    	System.out.println("after updation"+collectionProtocol.getShortTitle());
	    	assertTrue("Domain object updated successfully", true);
	    }
	    catch (Exception e)
	    {
	    	Logger.out.error(e.getMessage(),e);
	    	e.printStackTrace();
	    	//assertFalse("Failed to update object",true);
	    	fail("Failed to update object");
	    }
	    return collectionProtocol;
	}


	public void testVerifyConsentWithdrawnWithDiscardOption(){
		System.out.println("Inside ConsentsVerificationTestCases:");
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		try{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("CP:"+cp.getTitle());
		Participant participant = BaseTestCaseUtility.initParticipant();

		try{
			participant = (Participant) appService.createObject(participant);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("Participant:"+participant.getFirstName());
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		collectionProtocolRegistration.setCollectionProtocol(cp);
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		collectionProtocolRegistration.setActivityStatus("Active");
		try
		{
			collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975",
					Utility.datePattern("08/15/1975")));
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
		Collection consentTierCollection = cp.getConsentTierCollection();
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

		collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
		System.out.println("Creating CPR");
		try{
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to register participant", true);
		}

		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg =(SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(collectionProtocolRegistration);
		Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
		scg.setSpecimenCollectionSite(site);
		scg.setName("New SCG"+UniqueKeyGeneratorUtil.getUniqueKey());
		scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);
		System.out.println("Creating SCG");


		try{
			scg = (SpecimenCollectionGroup) appService.createObject(scg);

		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to rcreate SCG", true);
		}

		TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		ts.setStorageContainer(null);
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");

		try{
			ts = (TissueSpecimen) appService.createObject(ts);
		}
		catch(Exception e){
			assertFalse("Failed to create specimen", true);
		}

		Collection consentTierCollection1 = cp.getConsentTierCollection();
		Iterator consentTierItr1 = consentTierCollection1.iterator();
		Collection newConStatusCol = new HashSet();
		Collection consentTierStatusCollection = scg.getConsentTierStatusCollection();

		Iterator conStatusItr =  consentTierStatusCollection.iterator();
		ConsentTier c1 = (ConsentTier)consentTierItr1.next();
		ConsentTierStatus consentStatus1 = new ConsentTierStatus();
		consentStatus1.setStatus("Withdrawn");
		consentStatus1.setConsentTier(c1);
		newConStatusCol.add(consentStatus1);
		ConsentTier c2 = (ConsentTier)consentTierItr1.next();
		ConsentTierStatus consentStatus2 = new ConsentTierStatus();
		consentStatus2.setStatus("Withdrawn");
		consentStatus2.setConsentTier(c2);
		newConStatusCol.add(consentStatus2);
		ConsentTier c3 = (ConsentTier)consentTierItr1.next();
		ConsentTierStatus consentStatus3 = new ConsentTierStatus();
		consentStatus3.setStatus("Withdrawn");
		consentStatus3.setConsentTier(c3);
		newConStatusCol.add(consentStatus3);

	    scg.setConsentTierStatusCollection(newConStatusCol);
 		scg.setConsentWithdrawalOption("Discard");
		scg.getCollectionProtocolRegistration().getCollectionProtocol().setId(collectionProtocolRegistration.getId());
		scg.getCollectionProtocolRegistration().setParticipant(participant);
		try{
			scg = (SpecimenCollectionGroup) appService.updateObject(scg);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to update SCG", true);
		}

	}

	public void testVerifyConsentsWithdrawnWithReturnOption(){
		System.out.println("Inside ConsentsVerificationTestCases:");
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		try{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("CP:"+cp.getTitle());
		Participant participant = BaseTestCaseUtility.initParticipant();

		try{
			participant = (Participant) appService.createObject(participant);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to create collection protocol", true);
		}
		System.out.println("Participant:"+participant.getFirstName());
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		collectionProtocolRegistration.setCollectionProtocol(cp);
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		collectionProtocolRegistration.setActivityStatus("Active");
		try
		{
			collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975",
					Utility.datePattern("08/15/1975")));
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
		Collection consentTierCollection = cp.getConsentTierCollection();
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

		collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
		System.out.println("Creating CPR");
		try{
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to register participant", true);
		}

		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg =(SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(collectionProtocolRegistration);
		Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
		scg.setSpecimenCollectionSite(site);
		scg.setName("New SCG"+UniqueKeyGeneratorUtil.getUniqueKey());
		scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);
		System.out.println("Creating SCG");


		try{
			scg = (SpecimenCollectionGroup) appService.createObject(scg);

		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to rcreate SCG", true);
		}

		TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		ts.setStorageContainer(null);
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");

		try{
			ts = (TissueSpecimen) appService.createObject(ts);
		}
		catch(Exception e){
			assertFalse("Failed to create specimen", true);
		}

		Collection consentTierCollection1 = cp.getConsentTierCollection();
		Iterator consentTierItr1 = consentTierCollection1.iterator();
		Collection newConStatusCol = new HashSet();
		Collection consentTierStatusCollection = scg.getConsentTierStatusCollection();

		Iterator conStatusItr =  consentTierStatusCollection.iterator();
		ConsentTier c1 = (ConsentTier)consentTierItr1.next();
		ConsentTierStatus consentStatus1 = new ConsentTierStatus();
		consentStatus1.setStatus("Withdrawn");
		consentStatus1.setConsentTier(c1);
		newConStatusCol.add(consentStatus1);
		ConsentTier c2 = (ConsentTier)consentTierItr1.next();
		ConsentTierStatus consentStatus2 = new ConsentTierStatus();
		consentStatus2.setStatus("Withdrawn");
		consentStatus2.setConsentTier(c2);
		newConStatusCol.add(consentStatus2);
		ConsentTier c3 = (ConsentTier)consentTierItr1.next();
		ConsentTierStatus consentStatus3 = new ConsentTierStatus();
		consentStatus3.setStatus("Withdrawn");
		consentStatus3.setConsentTier(c3);
		newConStatusCol.add(consentStatus3);

	    scg.setConsentTierStatusCollection(newConStatusCol);
 		scg.setConsentWithdrawalOption("Return");
		scg.getCollectionProtocolRegistration().getCollectionProtocol().setId(collectionProtocolRegistration.getId());
		scg.getCollectionProtocolRegistration().setParticipant(participant);
		try{
			scg = (SpecimenCollectionGroup) appService.updateObject(scg);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to update SCG", true);
		}

	}	*/


}
