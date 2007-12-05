package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;


public class SpecimenTestCases extends CaTissueBaseTestCase {
	
	AbstractDomainObject domainObject = null;
	public void testUpdateCollectionStatusOfSpecimen()
	{
		try
		{
			Specimen sp = new Specimen();
			sp = (Specimen)TestCaseUtility.getObjectMap(Specimen.class);
			System.out.println("Get Object Sp"+sp.getId());
			List spCollection = appService.getObjects(sp);
			sp = (Specimen)spCollection.get(0);
			System.out.println("Get Object Sp");
			sp.setCollectionStatus("Collected");
			sp.setAvailable(true);
			sp.setExternalIdentifierCollection(null);
			System.out.println(sp+": sp");
			sp =  (Specimen) appService.updateObject(sp);
			System.out.println(sp+": sp After Update");
			assertTrue(" Domain Object is successfully added ---->    Name:: " + sp, true);
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}
	public void testUpdateSpecimenWithConsents()
	{
		try
		{
			Specimen sp = new Specimen();
			sp = (Specimen)TestCaseUtility.getObjectMap(Specimen.class);
			sp.setId(sp.getId());
			List spCollection = appService.getObjects(sp);
			sp = (Specimen)spCollection.get(0);
			sp.setCollectionStatus("Collected");
			sp.setAvailable(true);
			sp.setExternalIdentifierCollection(null);
			
			Collection consentTierStatusCollection = new HashSet();
			
			CollectionProtocol collectionProtocol = (CollectionProtocol)TestCaseUtility.getObjectMap(CollectionProtocol.class);
			Collection consentTierCollection = collectionProtocol.getConsentTierCollection();
			Iterator consentTierItr = consentTierCollection.iterator();
			while(consentTierItr.hasNext())
			{
				ConsentTier consentTier = (ConsentTier)consentTierItr.next();
				ConsentTierStatus consentStatus = new ConsentTierStatus();
				consentStatus.setConsentTier(consentTier);
				consentStatus.setStatus("Yes");
				consentTierStatusCollection.add(consentStatus);
			}
			sp.setConsentTierStatusCollection(consentTierStatusCollection);
			System.out.println(sp+": sp");
			sp =  (Specimen) appService.updateObject(sp);
			System.out.println(sp+": sp After Update");
			assertTrue(" Domain Object is successfully added ---->    Name:: " + sp, true);
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}
	
	public void testAddTissueSpecimen()
	{
	   try {
		    System.out.println("Before Creating SCG");
		    CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) TestCaseUtility.getObjectMap(CollectionProtocolRegistration.class);
		    
		    SpecimenCollectionGroup scg =(SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(cpr);
		    Site site = (Site) TestCaseUtility.getObjectMap(Site.class);
		    scg.setSpecimenCollectionSite(site);
		    scg.setName("SCG1"+UniqueKeyGeneratorUtil.getUniqueKey());		    
		    scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);		    
		    scg = (SpecimenCollectionGroup)appService.createObject(scg);	
		    TestCaseUtility.setObjectMap(scg, SpecimenCollectionGroup.class);
		    System.out.println("After Creating SCG");
		    
			TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();				
			specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->"+specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj =  (TissueSpecimen) appService.createObject(specimenObj);
			TestCaseUtility.setObjectMap(specimenObj, TissueSpecimen.class);
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
			assertTrue(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);			
		}
		catch(Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}
	
	
	
	public void testAddMolecularSpecimen()
	{
	   try {
		    MolecularSpecimen specimenObj = (MolecularSpecimen) BaseTestCaseUtility.initMolecularSpecimen();
		    SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
		    specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->"+specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj =  (MolecularSpecimen) appService.createObject(specimenObj);
			TestCaseUtility.setObjectMap(specimenObj, MolecularSpecimen.class);
			System.out.println("Afer Creating Tissue Specimen");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
			assertTrue("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);		
	   
	   }
		catch(Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}
	
	public void testAddCellSpecimen()
	{
	   try {
		    CellSpecimen specimenObj = (CellSpecimen) BaseTestCaseUtility.initCellSpecimen();
		    SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
		    specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->"+specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj =  (CellSpecimen) appService.createObject(specimenObj);
			TestCaseUtility.setObjectMap(specimenObj, CellSpecimen.class);
			System.out.println("Afer Creating Tissue Specimen");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
			assertTrue("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);		
	   
	   }
		catch(Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}
	public void testAddFluidSpecimen()
	{
	   try {
		    FluidSpecimen specimenObj = (FluidSpecimen) BaseTestCaseUtility.initFluidSpecimen();
		    SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
		    specimenObj.setSpecimenCollectionGroup(scg);
			Logger.out.info("Inserting domain object------->"+specimenObj);
			System.out.println("Before Creating Tissue Specimen");
			specimenObj =  (FluidSpecimen) appService.createObject(specimenObj);
			TestCaseUtility.setObjectMap(specimenObj, FluidSpecimen.class);
			System.out.println("Afer Creating Tissue Specimen");
			Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
			Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
			assertTrue("Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);		
	   
	   }
		catch(Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}
	
	public void testSearchSpecimen()
    {
    	Specimen specimen = new TissueSpecimen();
    	Specimen cachedSpecimen = (TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
    	specimen.setId(cachedSpecimen.getId());
     	Logger.out.info(" searching domain object");
    	 try {
        	 List resultList = appService.search(Specimen.class,specimen);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Specimen returnedspecimen = (Specimen) resultsIterator.next();
        		 System.out.println("here-->" + returnedspecimen.getLabel() +"Id:"+returnedspecimen.getId());
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedspecimen.getLabel());
             }
        	 assertTrue("Specimen found", true);
          } 
          catch (Exception e) {
        	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("Couldnot found Specimen", true);  
          }

    }
	
	
	/*public void testUpdateTissueSpecimen()
	{
	   try {
		   TissueSpecimen ts = (TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
		   System.out.println("Specimen from map"+ts.getLabel());
		   ts.setLabel("upadated TS"+UniqueKeyGeneratorUtil.getUniqueKey());
		   ts.setAvailable(new Boolean(true));
		   ts.setCollectionStatus("Collected");
		   ts = (TissueSpecimen) appService.updateObject(ts);
		   Logger.out.info(" Domain Object is successfully updated ---->  :: " + ts.getLabel());		   
		   assertTrue(" Domain Object is successfully added ---->    Name:: " + ts.getLabel(), true);			
		}
		catch(Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	  }
	
	public void testUpdateMolecularSpecimen()
	{
	   try {
		   MolecularSpecimen ts = (MolecularSpecimen) TestCaseUtility.getObjectMap(MolecularSpecimen.class);
		   System.out.println("Specimen from map"+ts.getLabel());
		   ts.setLabel("upadated TS"+UniqueKeyGeneratorUtil.getUniqueKey());
		   ts.setAvailable(new Boolean(true));
		   ts.setCollectionStatus("Collected");
		   Collection externalIdentifierCollection = new HashSet();
		   ExternalIdentifier externalIdentifier = new ExternalIdentifier();
		   externalIdentifier.setName("eid" + UniqueKeyGeneratorUtil.getUniqueKey());
		   externalIdentifier.setValue(""+ UniqueKeyGeneratorUtil.getUniqueKey());
		   externalIdentifier.setSpecimen(ts);
		   externalIdentifierCollection.add(externalIdentifier);
		   ts.setExternalIdentifierCollection(externalIdentifierCollection);
		   ts = (MolecularSpecimen) appService.updateObject(ts);
		   Logger.out.info(" Domain Object is successfully updated ---->  :: " + ts.getLabel());		   
		   assertTrue(" Domain Object is successfully added ---->    Name:: " + ts.getLabel(), true);			
		}
		catch(Exception e)
		{
			System.out.println("Exception thrown");
			System.out.println(e);
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	  }
	*/
	
	public void testAddTissueSpecimenInStorageContainerWithClosedSite()
	{ 
		Site site = BaseTestCaseUtility.initSite();
		try{
			site = (Site) appService.createObject(site);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse("Failed to create site", true);
		}		
		
			
		CollectionProtocol cp = BaseTestCaseUtility.initCollectionProtocol();
		try{
			cp = (CollectionProtocol) appService.createObject(cp);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Failed to create collection protocol", true);
		}
		StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();			
		storageContainer.setSite(site);
		Collection cpCollection = new HashSet();
		cpCollection.add(cp);
		storageContainer.setCollectionProtocolCollection(cpCollection);
		try{			
			storageContainer = (StorageContainer) appService.createObject(storageContainer); 			
		}catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse("Failed create Storage Container", true);
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
			assertFalse("Failed to add registration date", true);
		}
		collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
		User user = (User)TestCaseUtility.getObjectMap(User.class);
		collectionProtocolRegistration.setConsentWitness(user);
		
		Collection consentTierResponseCollection = new HashSet();
		Collection consentTierCollection = cp.getConsentTierCollection();
		
		Iterator ConsentierItr = consentTierCollection.iterator();
		Iterator ConsentierResponseItr = consentTierResponseCollection.iterator();
		
		while(ConsentierItr.hasNext())
		{
			ConsentTier consentTier = (ConsentTier)ConsentierItr.next();
			ConsentTierResponse consentResponse = new ConsentTierResponse();
			consentResponse.setResponse("Yes");
			consentResponse.setConsentTier(consentTier);		
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
		
		site.setActivityStatus("Closed");
		
		try{
			site = (Site) appService.updateObject(site);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse("Could not update site", true);
		}
		
		TissueSpecimen ts =(TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		ts.setStorageContainer(storageContainer);
		ts.setPositionDimensionOne(new Integer(1));
		ts.setPositionDimensionTwo(new Integer(2));
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Befor creating Tissue Specimen");
		
		try{
			ts = (TissueSpecimen) appService.createObject(ts);
			System.out.println("TissueSpec:"+ts.getLabel());
			assertFalse("Successfully created specimen", true);
		}
		catch(Exception e){
			assertTrue("Failed to add Specimen in container with closed site", true);
		}	
	
	
	}
	
	

	

}
