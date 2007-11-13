package edu.wustl.catissuecore.bizlogic.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Specimen;
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
