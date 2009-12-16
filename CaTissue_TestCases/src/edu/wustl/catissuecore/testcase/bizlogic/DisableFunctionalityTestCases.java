package edu.wustl.catissuecore.testcase.bizlogic;

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
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.common.util.HQLCriteria;

public class DisableFunctionalityTestCases extends CaTissueSuiteBaseTest {
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
			String query = "from edu.wustl.catissuecore.domain.StorageContainer as storageContainer where "
				+ "storageContainer.id="+storageContainer;	
			List resultList = appService.search(query);
			
			System.out.println("List size:"+resultList.size());
			StorageContainer retSC = (StorageContainer) resultList.get(0);
			System.out.println("Storage Container:"+retSC.getName());
		
			SpecimenCollectionGroup scg = createSCGWithConsents(cp);
			
			TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			
			SpecimenPosition cpos = new SpecimenPosition();
			cpos.setStorageContainer(storageContainer);
			cpos.setPositionDimensionOne(new Integer(1));
			cpos.setPositionDimensionTwo(new Integer(2));
			cpos.setSpecimen(ts);
			ts.setSpecimenPosition(cpos);
			ts.setSpecimenCollectionGroup(scg);
			ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
			ts.setIsAvailable(new Boolean("true"));
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
	
	public void testDisableStorageContainerHavingChildContainer()
    {
        try
        {
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
            
            CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
            cp = (CollectionProtocol) appService.createObject(cp);
            
            Collection collectionProtocolCollection = new HashSet();
            collectionProtocolCollection.add(cp);
           
            parentContainer.setCollectionProtocolCollection(collectionProtocolCollection);
            
            SpecimenArrayType sat = BaseTestCaseUtility.initSpecimenSpecimenArrayType();
            sat = (SpecimenArrayType) appService.createObject(sat);
            Collection holdsSpecimenArrayTypeCollection = new HashSet();
            holdsSpecimenArrayTypeCollection.add(sat);
           
            parentContainer.setHoldsSpecimenArrayTypeCollection(holdsSpecimenArrayTypeCollection);
            
            parentContainer.setActivityStatus("Active");
            parentContainer.setFull(Boolean.valueOf(false));
            
            parentContainer = (StorageContainer) appService.createObject(parentContainer);
            System.out.println("Parent:"+parentContainer.getId());
           
           // System.out.println("Parent:"+StorageContainerUtil.getChildren(dao, containerId).getChildren().size());
            TestCaseUtility.setObjectMap(parentContainer,StorageContainer.class);
        
            StorageContainer subStorageContainer = new StorageContainer();
            subStorageContainer.setStorageType(storageType);
            
            ContainerPosition cPos = new ContainerPosition();
            cPos.setParentContainer(parentContainer);
            cPos.setPositionDimensionOne(new Integer(1));
            cPos.setPositionDimensionOne(new Integer(2));
            cPos.setOccupiedContainer(subStorageContainer);
            subStorageContainer.setLocatedAtPosition(cPos);
            
            subStorageContainer.setNoOfContainers(new Integer(1));
            subStorageContainer.setActivityStatus("Active");
            Capacity capacity1 = new Capacity();
            capacity1.setOneDimensionCapacity(new Integer(5));
            capacity1.setTwoDimensionCapacity(new Integer(5));
            subStorageContainer.setCapacity(capacity1);
            subStorageContainer.setFull(Boolean.valueOf(false));
           
            subStorageContainer.setHoldsStorageTypeCollection(holdsStorageTypeCollection);
            
            subStorageContainer = (StorageContainer) appService.createObject(subStorageContainer);
            
            System.out.println("Childcontainer:"+subStorageContainer.getId());
           
 //           System.out.println("ParentC:"+subStorageContainer.getParent().getName());
           
    //        System.out.println("Parent:"+parentContainer.getChildren().size());
            
            StorageContainer parentConatiertoUpdate = getStorageContainer(parentContainer.getId());
            
            parentConatiertoUpdate.setActivityStatus("Disabled");
            StorageContainer updatedparentContainer = (StorageContainer) appService.updateObject(parentConatiertoUpdate);
            
     }catch(Exception e){
		 Logger.out.error(e.getMessage(),e);
		 e.printStackTrace();
		 assertFalse("Failed to disable storage Container", true);
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
			
			SpecimenPosition spPos = new SpecimenPosition();
			spPos.setStorageContainer(storageContainer);
			spPos.setPositionDimensionOne(new Integer(1));
			spPos.setPositionDimensionTwo(new Integer(2));
			spPos.setSpecimen(ts);
			ts.setSpecimenPosition(spPos);
			ts.setSpecimenCollectionGroup(scg);
			ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
			ts.setIsAvailable(new Boolean("true"));
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
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
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
			SpecimenPosition spPos = new SpecimenPosition();
			spPos.setStorageContainer(storageContainer);
			spPos.setSpecimen(ts);
			spPos.setPositionDimensionOne(new Integer(1));
			spPos.setPositionDimensionTwo(new Integer(2));
			ts.setSpecimenPosition(spPos);
			ts.setSpecimenCollectionGroup(scg);
			ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
			ts.setIsAvailable(new Boolean("true"));
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
	
/*  public void testParticipantRegistrationUnderDisabledCP()
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
			
		
	} */
	
	public void testDisableParticipantHavingSpecimens()
	{
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		try{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to create Collection Protocol", true);
		}
		
		
        Participant participant = BaseTestCaseUtility.initParticipant();
		
		try{
			participant = (Participant) appService.createObject(participant);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to create participant", true);
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
           	assertFalse("Failed to create site", true);
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
           	assertFalse("Failed to create SCG", true);
		}
		
		TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		
//		ts.setStorageContainer(null);
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
		ts.setIsAvailable(new Boolean("true"));
		try{
			ts = (TissueSpecimen) appService.createObject(ts);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to create specimen", true);
		}
		
		
		try{
			participant.setActivityStatus("Disabled");
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
 private StorageContainer getStorageContainer(Long id) throws Exception
    {
        StorageContainer stContainer = new StorageContainer ();
        stContainer.setId(id);
        String query = "from edu.wustl.catissuecore.domain.StorageContainer as storageContainer where "
			+ "storageContainer.id= "+id;	
        List l = (List)appService.search(query);
        StorageContainer storageContainer = (StorageContainer)l.get(0);
        
        Collection collectionProtocolCollection = new HashSet();
        String hql = "select elements(sc.collectionProtocolCollection)from edu.wustl.catissuecore.domain.StorageContainer as sc where sc.id = "+ id;
        HQLCriteria criteria = new HQLCriteria(hql);
        List collectionProtocolCollectionOld = (List)appService.search(hql);
//        List collectionProtocolCollectionOld = (List)appService.query(criteria, CollectionProtocol.class.getName());
        Iterator itCollectionProtocolCollectionOld = collectionProtocolCollectionOld.iterator();
        while(itCollectionProtocolCollectionOld.hasNext())
        {
            CollectionProtocol cp = (CollectionProtocol)itCollectionProtocolCollectionOld.next();
            CollectionProtocol cpN = new CollectionProtocol();
            cpN.setId(cp.getId());
            //List list =(List)appService.search(CollectionProtocol.class, cpN);
            collectionProtocolCollection.add(cpN);
        }
       
        storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
        
        Collection holdsStorageTypeCollection =  new HashSet();
        String hql1 = "select elements(sc.holdsStorageTypeCollection)from edu.wustl.catissuecore.domain.StorageContainer as sc where sc.id = "+ id;
        HQLCriteria criteria1 = new HQLCriteria(hql1);
        List holdsStorageTypeCollectionOld = (List)appService.search(hql1);
        Iterator itHoldsStorageTypeCollectionOld = holdsStorageTypeCollectionOld.iterator();
        while(itHoldsStorageTypeCollectionOld.hasNext())
        {
            StorageType st = (StorageType)itHoldsStorageTypeCollectionOld.next();
            StorageType stN = new StorageType();
            stN.setId(st.getId());
            //List list = (List)appService.search(StorageType.class, stN);
            holdsStorageTypeCollection.add(stN);
        }
       
        storageContainer.setHoldsStorageTypeCollection(holdsStorageTypeCollection);
        
        
        Collection holdsSpecimenArrayTypeCollection = new HashSet();
        String hql2 = "select elements(sc.holdsSpecimenArrayTypeCollection) from edu.wustl.catissuecore.domain.StorageContainer as sc where sc.id = "+ id; 
        HQLCriteria criteria2 = new HQLCriteria(hql2);
        List holdsSpecimenArrayTypeCollectionOld = (List)appService.search(hql2);
//      List holdsSpecimenArrayTypeCollectionOld = (List)appService.query(criteria2, SpecimenArrayType.class.getName());
        Iterator itHoldsSpecimenArrayTypeCollectionOld = holdsSpecimenArrayTypeCollectionOld.iterator();
        while(itHoldsSpecimenArrayTypeCollectionOld.hasNext())
        {
            SpecimenArrayType sat = (SpecimenArrayType)itHoldsSpecimenArrayTypeCollectionOld.next();
            SpecimenArrayType satN = new SpecimenArrayType();
            satN.setId(sat.getId());
            //List list =(List)appService.search(CollectionProtocol.class, satN);
            holdsSpecimenArrayTypeCollection.add(satN);
        }
       
        storageContainer.setHoldsSpecimenArrayTypeCollection(holdsSpecimenArrayTypeCollection);
        
        return storageContainer;
    }
}

