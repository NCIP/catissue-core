package edu.wustl.catissuecore.bizlogic.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
			sp.setId(sp.getId());
			List spCollection = appService.getObjects(sp);
			sp = (Specimen)spCollection.get(0);
			sp.setCollectionStatus("Collected");
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
			sp.setExternalIdentifierCollection(null);
			
			Collection consentTierStatusCollection = new HashSet();
			ConsentTierStatus r1 = new ConsentTierStatus();
			ConsentTier consentTier = new ConsentTier();
			consentTier.setId(new Long(1));
			r1.setConsentTier(consentTier);
			r1.setStatus("Yes");
			consentTierStatusCollection.add(r1);
			
			ConsentTierStatus r2 = new ConsentTierStatus();
			ConsentTier consentTier2 = new ConsentTier();
			consentTier2.setId(new Long(2));
			r2.setConsentTier(consentTier2);
			r2.setStatus("Yes");
			consentTierStatusCollection.add(r2);
			
			ConsentTierStatus r3 = new ConsentTierStatus();
			ConsentTier consentTier3 = new ConsentTier();
			consentTier3.setId(new Long(3));
			r3.setConsentTier(consentTier3);
			r3.setStatus("Yes");
			consentTierStatusCollection.add(r3);
			
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
