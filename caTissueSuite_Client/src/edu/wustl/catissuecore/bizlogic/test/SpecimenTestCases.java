package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
import edu.wustl.catissuecore.domain.SpecimenPosition;
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
			System.out.println("testUpdateCollectionStatusOfSpecimen Get Object Sp"+sp.getId());
			List spCollection = appService.getObjects(sp);
			sp = (Specimen)spCollection.get(0);
			System.out.println("Get Object Sp");
			sp.setCollectionStatus("Collected");
			sp.setIsAvailable(true);
			sp.setExternalIdentifierCollection(null);
			System.out.println(sp+": sp");
			sp =  (Specimen) appService.updateObject(sp);
			System.out.println(sp+": sp After Update");
			assertTrue(" Domain Object is successfully added ---->    Name:: " + sp, true);
		}
		catch(Exception e)
		{
			Logger.out.error("testUpdateCollectionStatusOfSpecimen"+ e.getMessage(),e);
			System.out.println("SpecimenTestCases.testUpdateCollectionStatusOfSpecimen():"+e.getMessage());
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
			sp.setIsAvailable(true);
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
			Logger.out.error("testUpdateSpecimenWithConsents"+e.getMessage(),e);
			System.out.println("SpecimenTestCases.testUpdateSpecimenWithConsents():"+e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}
	
	public void testAddTissueSpecimen()
	{
	   try {
		   TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
		   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
		   System.out.println("SpecimenTestCases.testAddTissueSpecimen(): "+scg);
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
		    System.out.println("SpecimenTestCases.testAddMolecularSpecimen(): "+scg);
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
	public void testUpdateSpecimenWithBarcode()
	{
		String uniqueKey=UniqueKeyGeneratorUtil.getUniqueKey();
		MolecularSpecimen specimen = (MolecularSpecimen) TestCaseUtility.getObjectMap(MolecularSpecimen.class);
    	specimen.setBarcode("barcode"+uniqueKey);
    	specimen.setExternalIdentifierCollection(null);
    	try
    	{
    		specimen =  (MolecularSpecimen) appService.updateObject(specimen);
    		System.out.println(specimen+": specimen After Update");
    		assertTrue(" Domain Object is successfully added ---->    Name:: " + specimen, true);
		}
		catch(Exception e)
		{
			Logger.out.error("testUpdateCollectionStatusOfSpecimen"+ e.getMessage(),e);
			System.out.println("SpecimenTestCases.testUpdateCollectionStatusOfSpecimen():"+e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
    	
	}
	public void testUpdateSpecimenWithCaseSensitiveBarcode()
	{
		String uniqueKey=UniqueKeyGeneratorUtil.getUniqueKey();
		MolecularSpecimen cellSpecimen = (MolecularSpecimen) TestCaseUtility.getObjectMap(MolecularSpecimen.class);
		cellSpecimen.setBarcode("specimen with barcode"+uniqueKey);
		cellSpecimen.setExternalIdentifierCollection(null);
    	try
    	{
    		cellSpecimen =  (MolecularSpecimen) appService.updateObject(cellSpecimen);
    		System.out.println(cellSpecimen+": specimen After Update");
    		assertTrue(" Domain Object is successfully added ---->    Name:: " + cellSpecimen, true);
		}
		catch(Exception e)
		{
			Logger.out.error("testUpdateCollectionStatusOfSpecimen"+ e.getMessage(),e);
			System.out.println("SpecimenTestCases.testUpdateCollectionStatusOfSpecimen():"+e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
		TissueSpecimen specimen = (TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
    	specimen.setBarcode("SPECIMEN WITH BARCODE"+uniqueKey);
    	specimen.setExternalIdentifierCollection(null);
    	try
    	{
    		specimen =  (TissueSpecimen) appService.updateObject(specimen);
    		System.out.println(specimen+": specimen After Update");
    		assertTrue(" Domain Object is successfully added ---->    Name:: " + specimen, true);
		}
		catch(Exception e)
		{
			Logger.out.error("testUpdateCollectionStatusOfSpecimen"+ e.getMessage(),e);
			System.out.println("SpecimenTestCases.testUpdateCollectionStatusOfSpecimen():"+e.getMessage());
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	}
	public void testSearchSpecimenWithBarcode()
	{
		Specimen specimen =new Specimen();
    	TissueSpecimen cachedSpecimen = (TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
    	specimen.setBarcode(cachedSpecimen.getBarcode());
     	Logger.out.info("searching domain object");
    	 try {
        	 List resultList = appService.search(Specimen.class,specimen);
        	 if(resultList!=null)
        	 {	 
        		 if(resultList.size()==1)
        		 {
        			 System.out.println(resultList.size());
        		 	System.out.println("Case sensitive match is found of barcode is for Specimen");
        		 	assertFalse("Case sensitive match is found of barcode is for Specimen", true);
        		 }
        		 else
        		 {
        			 assertTrue("All the  Specimen matched barcode found", true);  
        		 }
        	 }
          } 
          catch (Exception e) {
        	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("Couldnot found Specimen", true);  
          }
	}

	public void testSearchTissueSpecimen()
    {
    	TissueSpecimen specimen = new TissueSpecimen();
    	TissueSpecimen cachedSpecimen = (TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
    	specimen.setId(cachedSpecimen.getId());
     	Logger.out.info(" searching domain object");
    	 try {
        	 List resultList = appService.search(TissueSpecimen.class,specimen);
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
	public void testSearchSpecimen()
    {
    	Specimen specimen = new Specimen();
    	TissueSpecimen cachedSpecimen = (TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
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
        	  System.out.println("SpecimenTestCases.testSearchSpecimen()"+ e.getMessage());
        	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("Couldnot found Specimen", true);  
          }

    }
	public void testUpdateTissueSpecimen()
	{
	   try {
		   TissueSpecimen ts = (TissueSpecimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
		   System.out.println("Specimen from map"+ts.getLabel());
		   ts.setLabel("upadated TS"+UniqueKeyGeneratorUtil.getUniqueKey());
		   ts.setIsAvailable(new Boolean(true));
		   ts.setCollectionStatus("Collected");
		   Collection externalIdentifierCollection = new HashSet();
		   ExternalIdentifier externalIdentifier = new ExternalIdentifier();
		   externalIdentifier.setName("eid" + UniqueKeyGeneratorUtil.getUniqueKey());
		   externalIdentifier.setValue(""+ UniqueKeyGeneratorUtil.getUniqueKey());
		   externalIdentifier.setSpecimen(ts);
		   externalIdentifierCollection.add(externalIdentifier);
		   ts.setExternalIdentifierCollection(externalIdentifierCollection);
		   ts = (TissueSpecimen) appService.updateObject(ts);
		   Logger.out.info(" Domain Object is successfully updated ---->  :: " + ts.getLabel());		   
		   assertTrue(" Domain Object is successfully added ---->    Name:: " + ts.getLabel(), true);			
		}
		catch(Exception e)
		{
			System.out.println("SpecimenTestCases.testUpdateTissueSpecimen():"+e.getMessage());
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
		   ts.setIsAvailable(new Boolean(true));
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
			System.out
					.println("SpecimenTestCases.testUpdateMolecularSpecimen(): "+e.getMessage());
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse("Failed to create Domain Object", true);
		}
	  }
	
	
	public void testVerifyConsentResponseAndConsentStatusAtSCG()
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
//		ts.setStorageContainer(null);
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
		ts.setIsAvailable(new Boolean("true"));
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
//		ts.setStorageContainer(null);
		ts.setSpecimenCollectionGroup(scg);
		ts.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
		ts.setIsAvailable(new Boolean("true"));
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
//		ts1.setStorageContainer(null);
		ts1.setSpecimenCollectionGroup(newSCG);
		ts1.setLabel("TisSpec"+UniqueKeyGeneratorUtil.getUniqueKey());
		ts1.setIsAvailable(new Boolean("true"));
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
//		ts.setStorageContainer(null);
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
//		ts.setStorageContainer(null);
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
	
}
