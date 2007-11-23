package edu.wustl.catissuecore.bizlogic.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.common.domain.AbstractDomainObject;
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
	
	
	
/*	public void testAddSpecimen()
	{
	
	try
	{
		TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();				
		//setLogger(specimenObj);
		Logger.out.info("Inserting domain object------->"+specimenObj);
		specimenObj =  (TissueSpecimen) appService.createObject(specimenObj);
		Logger.out.info(" Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
		Logger.out.info(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());
		assertTrue(" Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel(), true);
		
	}
	catch(Exception e)
	{
		Logger.out.error(e.getMessage(),e);
		e.printStackTrace();
		assertFalse("Failed to create Domain Object", true);
	}
	}
	
	public void testSearchSpecimen()
    {
    	Specimen specimen = new TissueSpecimen();
    	specimen.setId(new Long(8));
     	Logger.out.info(" searching domain object");
    	 try {
        	 List resultList = appService.search(Specimen.class,specimen);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Specimen returnedspecimen = (Specimen) resultsIterator.next();
        		// System.out.println("here-->" + returnedspecimen.getSpecimenEventCollection());
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedspecimen.getLabel());
             }
        	// assertTrue("Specimen found", true);
          } 
          catch (Exception e) {
        	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("Couldnot found Specimen", true);  
          }

    }*/
	
	
	/*public void testEmptyObjectInInsert(){
		domainObject = new MolecularSpecimen();
		testEmptyDomainObjectInInsert(domainObject);
	}
	
	public void testNullObjectInInsert(){
		domainObject = new MolecularSpecimen();
		testNullDomainObjectInInsert(domainObject);
	}
	
	public void testNullSessionDatBeanInInsert(){
		domainObject = new MolecularSpecimen();
		testNullSessionDataBeanInInsert(domainObject);
	}
	
	public void testNullSessionDataBeanInUpdate(){
		domainObject = new MolecularSpecimen();
		testNullSessionDataBeanInUpdate(domainObject);
	}
	
	public void testNullOldDomainObjectInUpdate(){
		domainObject = new MolecularSpecimen();
		testNullOldDomainObjectInUpdate(domainObject);
	}
	
	public void testNullCurrentDomainObjectInUpdate(){
		domainObject = new MolecularSpecimen();
		testNullCurrentDomainObjectInUpdate(domainObject);
	}
	
	public void testWrongDaoTypeInUpdate(){
		domainObject = new MolecularSpecimen();
		testNullCurrentDomainObjectInUpdate(domainObject);
	}
	
	public void testEmptyCurrentDomainObjectInUpdate(){
		domainObject = new MolecularSpecimen();
		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initCollectionProtocol();
		testEmptyCurrentDomainObjectInUpdate(domainObject, initialisedDomainObject);
	}
	
	public void testEmptyOldDomainObjectInUpdate(){
		domainObject = new MolecularSpecimen();
		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initCollectionProtocol();
		testEmptyOldDomainObjectInUpdate(domainObject, initialisedDomainObject);
	}*/

}
