package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

public class DisableFunctionalityTestCases extends CaTissueBaseTestCase {
	public void testCreateStorageContainerUnderClosedSite()
	{
		try{
			Site site= BaseTestCaseUtility.initSite();			
			site = (Site) appService.createObject(site); 
			
			site.setActivityStatus("Closed");
			site = (Site) appService.updateObject(site); 
			
			CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
			cp = (CollectionProtocol) appService.createObject(cp);
			
			StorageType storageType = BaseTestCaseUtility.initStorageType();
			storageType = (StorageType) appService.createObject(storageType);
			
			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			storageContainer.setStorageType(storageType);
			storageContainer.setSite(site);
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 			
			
		}catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("could not add object", true);
		 }
		
	}
	
	public void testUseDisabledStorageContainerToAddSpecimen()
	{
		try{
			Site site= BaseTestCaseUtility.initSite();			
			site = (Site) appService.createObject(site); 
			TestCaseUtility.setObjectMap(site, Site.class);
			
			StorageType storageType = BaseTestCaseUtility.initStorageType();
			storageType = (StorageType) appService.createObject(storageType); 
			TestCaseUtility.setObjectMap(storageType, StorageType.class);
			
			CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
			cp = (CollectionProtocol) appService.createObject(cp);
			TestCaseUtility.setObjectMap(cp, CollectionProtocol.class);
			System.out.println("CP:"+cp.getTitle());
			System.out.println("CP short title:"+cp.getShortTitle());
			
			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			storageContainer.setStorageType(storageType);
			storageContainer.setSite(site);
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 
			TestCaseUtility.setObjectMap(storageContainer, StorageContainer.class);
			System.out.println("SC name:"+storageContainer.getName());
			System.out.println("SC id:"+storageContainer.getId());
			
			storageContainer.setActivityStatus("Disabled");
			storageContainer = (StorageContainer) appService.updateObject(storageContainer);
			System.out.println("SC id after disabling:"+storageContainer.getId());
			
			StorageContainer sc = new StorageContainer();
			sc.setId(storageContainer.getId());
			List resultList = appService.search(StorageContainer.class,sc);
			
			System.out.println("List size:"+resultList.size());
			StorageContainer retSC = (StorageContainer) resultList.get(0);
			System.out.println("Storage Container:"+retSC.getName());
		
			SpecimenCollectionGroup scg = createSCGWithConsents(cp);
			
			TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			ts.setStorageContainer(storageContainer);
			ts.setPositionDimensionOne(new Integer(1));
			ts.setPositionDimensionTwo(new Integer(2));
			ts.setSpecimenCollectionGroup(scg);
			ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
			ts.setAvailable(new Boolean("true"));
			ts = (TissueSpecimen) appService.createObject(ts);
			System.out.println("Tissue Specimen:"+ts.getLabel());
			assertFalse("Successfully created specimen using disabled storage Container", true);
			
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Could not create specimen", true);
			 
		 }
	} 
	
	public void testDisabledStorageContainerHavingSubContainers()
	{
		try{
			
			Site site= BaseTestCaseUtility.initSite();			
			site = (Site) appService.createObject(site); 
			
			StorageType storageType = BaseTestCaseUtility.initStorageType();
			storageType = (StorageType) appService.createObject(storageType); 
			
			
			StorageContainer parentContainer = new StorageContainer();
			parentContainer.setStorageType(storageType);
			parentContainer.setSite(site);

			Integer conts = new Integer(2);
			parentContainer.setNoOfContainers(conts);
			parentContainer.setTempratureInCentigrade(new Double(-30));
			parentContainer.setBarcode("barc" + UniqueKeyGeneratorUtil.getUniqueKey());

			Capacity capacity = new Capacity();
			capacity.setOneDimensionCapacity(new Integer(5));
			capacity.setTwoDimensionCapacity(new Integer(5));
			parentContainer.setCapacity(capacity);
			
			parentContainer.setNoOfContainers(new Integer(3));
			
			Collection holdsStorageTypeCollection = new HashSet();
			holdsStorageTypeCollection.add(storageType);
			parentContainer.setHoldsStorageTypeCollection(holdsStorageTypeCollection);
			parentContainer.setActivityStatus("Active");
			parentContainer.setFull(Boolean.valueOf(false));
			
			parentContainer = (StorageContainer) appService.createObject(parentContainer); 
			System.out.println("Parent:"+parentContainer.getId());
			TestCaseUtility.setObjectMap(parentContainer, StorageContainer.class);
		
			StorageContainer subStorageContainer = new StorageContainer();
			subStorageContainer.setStorageType(storageType);
			subStorageContainer.setParent(parentContainer);
			subStorageContainer.setPositionDimensionOne(new Integer(1));
			subStorageContainer.setPositionDimensionOne(new Integer(1));
			subStorageContainer.setNoOfContainers(new Integer(1));
			subStorageContainer.setActivityStatus("Active");
			Capacity capacity1 = new Capacity();
			capacity1.setOneDimensionCapacity(new Integer(5));
			capacity1.setTwoDimensionCapacity(new Integer(5));
			subStorageContainer.setCapacity(capacity1);
			subStorageContainer.setFull(Boolean.valueOf(false));
			subStorageContainer.setHoldsStorageTypeCollection(holdsStorageTypeCollection);
			
			subStorageContainer = (StorageContainer) appService.createObject(subStorageContainer);
			System.out.println("Child container:"+subStorageContainer.getId());
			System.out.println("ParentC:"+subStorageContainer.getParent().getName());
			
			
			parentContainer.setActivityStatus("Disabled");
		
			StorageContainer updatedparentContainer = (StorageContainer) appService.updateObject(parentContainer);
			System.out.println("disabled Parent Container Name :"+updatedparentContainer.getId());
			System.out.println("Parent Disabled:"+updatedparentContainer.getName());
		
			assertEquals("Disabled", subStorageContainer.getActivityStatus());
			
	 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	 
	
	public void testCreateSpecimenInClosedStorageContainer()
	{
		try{
			Site site= BaseTestCaseUtility.initSite();			
			site = (Site) appService.createObject(site); 
			TestCaseUtility.setObjectMap(site, Site.class);
			
			StorageType storageType = BaseTestCaseUtility.initStorageType();
			storageType = (StorageType) appService.createObject(storageType); 
			
			
			CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
			cp = (CollectionProtocol) appService.createObject(cp);
			TestCaseUtility.setObjectMap(cp, CollectionProtocol.class);
			
			StorageContainer storageContainer = (StorageContainer) BaseTestCaseUtility.initStorageContainer();
			
			storageContainer = (StorageContainer) appService.createObject(storageContainer);
			storageContainer.setStorageType(storageType);
			
			storageContainer.setActivityStatus("Closed");
			storageContainer = (StorageContainer) appService.updateObject(storageContainer);
			
			SpecimenCollectionGroup scg = createSCGWithConsents(cp);
			
			TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			ts.setStorageContainer(storageContainer);
			ts.setPositionDimensionOne(new Integer(1));
			ts.setPositionDimensionTwo(new Integer(2));
			ts.setSpecimenCollectionGroup(scg);
			ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
			ts.setAvailable(new Boolean("true"));
			ts = (TissueSpecimen) appService.createObject(ts);
			
		}catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("StorageContainer is already marked as closed", true);
		}
		
	} 
	
	public void testDisableCPhavingSpecimens()
	{
		try{
			Site site= BaseTestCaseUtility.initSite();			
			site = (Site) appService.createObject(site); 
			TestCaseUtility.setObjectMap(site, Site.class);
			
			StorageType storageType = BaseTestCaseUtility.initStorageType();
			storageType = (StorageType) appService.createObject(storageType); 
			
			CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
			cp = (CollectionProtocol) appService.createObject(cp);
			TestCaseUtility.setObjectMap(cp, CollectionProtocol.class);
			
			StorageContainer storageContainer = (StorageContainer) BaseTestCaseUtility.initStorageContainer();			
			storageContainer = (StorageContainer) appService.createObject(storageContainer);
			storageContainer.setStorageType(storageType);
								
			SpecimenCollectionGroup scg = createSCGWithConsents(cp);
			
			TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			ts.setStorageContainer(storageContainer);
			ts.setPositionDimensionOne(new Integer(1));
			ts.setPositionDimensionTwo(new Integer(2));
			ts.setSpecimenCollectionGroup(scg);
			ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
			ts.setAvailable(new Boolean("true"));
			ts = (TissueSpecimen) appService.createObject(ts);
			
			cp.setActivityStatus("Disabled");
			cp = (CollectionProtocol) appService.updateObject(cp);
			
		}catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("StorageContainer is already marked as closed", true);
		}
		
	}  
	
	public void testParticipantRegistrationUnderClosedCP()
	{
		try{			
			CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
			cp = (CollectionProtocol) appService.createObject(cp);
			
			cp.setActivityStatus("Closed");
			cp = (CollectionProtocol) appService.updateObject(cp);
			
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
			
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
			
					
		}catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("CollectionProtocol is already marked as closed", true);
		}
		
	} 
	
  public void testParticipantRegistrationUnderDisabledCP()
	{
		try{			
			CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
			cp = (CollectionProtocol) appService.createObject(cp);
			
			cp.setActivityStatus("Disabled");
			cp = (CollectionProtocol) appService.updateObject(cp);
			
			System.out.println("CP title:"+ cp.getTitle());
			System.out.println("CP short title:"+ cp.getShortTitle());
			System.out.println("CP id:"+ cp.getId());
			
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
			
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
			System.out.println("Participant Fname:"+ collectionProtocolRegistration.getParticipant().getFirstName());
			assertFalse("Successfully registered participant to disabled CollectionProtocol", true);
					
		}catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("CollectionProtocol is already marked as closed", true);
		}
			
		
	} 
	
	public void testDisableParticipantHavingSpecimens()
	{
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		try{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to register participant", true);
		}
		
		
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
		User user = new User();
		user.setId(new Long(1));
		collectionProtocolRegistration.setConsentWitness(user);
		
		Collection consentTierResponseCollection = new LinkedHashSet();
		Collection consentTierCollection = new LinkedHashSet();
		consentTierCollection = cp.getConsentTierCollection();
		
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
		Site site= BaseTestCaseUtility.initSite();
		try{
			site = (Site) appService.createObject(site);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to register participant", true);
		}
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
		
		TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		ts.setStorageContainer(null);
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
		ts.setAvailable(new Boolean("true"));
		try{
			ts = (TissueSpecimen) appService.createObject(ts);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to register participant", true);
		}
		
		participant.setActivityStatus("Disabled");
		try{
			participant = (Participant) appService.updateObject(participant);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertTrue("Unable to disable Participant : Before disabling it,dispose all the associated Specimens.", true);
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
}

