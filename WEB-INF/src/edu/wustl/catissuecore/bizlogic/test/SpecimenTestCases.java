package edu.wustl.catissuecore.bizlogic.test;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class SpecimenTestCases extends CaTissueBaseTestCase {
	
	AbstractDomainObject domainObject = null;
	public void testAddSpecimen(){
	
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

    }
	
	
	
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
