package edu.wustl.catissuecore.bizlogic.test;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class BioHazardTestCases extends CaTissueBaseTestCase {

	AbstractDomainObject domainObject = null;
	public void testAddBioHazard()
	{
		try{
			Biohazard biohazard= BaseTestCaseUtility.initBioHazard();			
			System.out.println(biohazard);
			biohazard = (Biohazard) appService.createObject(biohazard); 
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testSearchBioHazard()
	{
		Biohazard biohazard = new Biohazard();
    	Logger.out.info(" searching domain object");
    	biohazard.setId(new Long(1));
   
         try {
        	 List resultList = appService.search(Biohazard.class,biohazard);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Biohazard returnedBiohazard = (Biohazard) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedBiohazard.getName());
        		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
             }
          } 
          catch (Exception e) {
           	Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Does not find Domain Object", true);
	 		
          }
	}
	
	public void testUpdateBioHazard()
	{
		Biohazard biohazard =  BaseTestCaseUtility.initBioHazard();
    	Logger.out.info("updating domain object------->"+biohazard);
	    try 
		{
	    	biohazard = (Biohazard) appService.createObject(biohazard);
	    	BaseTestCaseUtility.updateBiohazard(biohazard);	
	    	Biohazard updatedBiohazard = (Biohazard) appService.updateObject(biohazard);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedBiohazard);
	       	assertTrue("Domain object successfully updated ---->"+updatedBiohazard, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
	
	public void testWithEmptyBioHazardName()
	{
		try{
			Biohazard biohazard =  BaseTestCaseUtility.initBioHazard();	
			biohazard.setName("");
			System.out.println(biohazard);
			biohazard = (Biohazard) appService.createObject(biohazard);
			assertFalse("Empty biohazard name should thorw Exception", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Name is required", true);
		 }
	}
	
	public void testWithDuplicateBioHazardName()
	{
		try{
			Biohazard biohazard =  BaseTestCaseUtility.initBioHazard();	
			Biohazard dupBiohazard =  BaseTestCaseUtility.initBioHazard();	
			//te.setId(new Long("1"));
			dupBiohazard.setName(biohazard.getName());
			System.out.println(biohazard);
			biohazard = (Biohazard) appService.createObject(biohazard);
			dupBiohazard = (Biohazard) appService.createObject(dupBiohazard); 
			assertFalse("Test Failed. Duplicate biohazard name should throw exception", true);
		}
		 catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Submission failed since a Site with the same NAME already exists" , true);
			 
		 }
	}
	
	public void testInvalidBioHazardType()
	{
		try{
			Biohazard biohazard =  BaseTestCaseUtility.initBioHazard();
			biohazard.setType("Invalid");
			System.out.println(biohazard);
			biohazard = (Biohazard) appService.createObject(biohazard);
			assertFalse("Test Failed. Invalid biohazard type should throw exception", true);
		}
		 catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Invalid Biohazard type " , true);
			 
		 }
		
	}
	
	public void testNullDomainObjectInInsert_BioHazard()
	{
		domainObject = new Biohazard(); 
		testNullDomainObjectInInsert(domainObject);
	}
	
	public void testNullSessionDataBeanInInsert_BioHazard()
	{
		domainObject = new Biohazard();
		testNullSessionDataBeanInInsert(domainObject);
	}
		
	public void testWrongDaoTypeInInsert_BioHazard()
	{
		domainObject = new Biohazard();
		testWrongDaoTypeInInsert(domainObject);
	}
	public void testNullSessionDataBeanInUpdate_BioHazard()
	{
		domainObject = new Biohazard();
		testNullSessionDataBeanInUpdate(domainObject);
	}
	
	public void testNullOldDomainObjectInUpdate_BioHazard()
	{
		domainObject = new Biohazard();
		testNullOldDomainObjectInUpdate(domainObject);
	}
	
		
	public void testNullCurrentDomainObjectInUpdate_BioHazard()
	{
		domainObject = new Biohazard();
		testNullCurrentDomainObjectInUpdate(domainObject);
	}
	
	public void testEmptyCurrentDomainObjectInUpdate_BioHazard()
	{
		domainObject = new Biohazard();
		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initDistributionProtocol();
		testEmptyCurrentDomainObjectInUpdate(domainObject, initialisedDomainObject);
	}
	
	public void testEmptyOldDomainObjectInUpdate_BioHazard()
	{
		domainObject = new Biohazard();
		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initDistributionProtocol();
		testEmptyOldDomainObjectInUpdate(domainObject,initialisedDomainObject);
	}
	
	public void testNullDomainObjectInRetrieve_BioHazard()
	{
		domainObject = new Biohazard();
		testNullCurrentDomainObjectInRetrieve(domainObject);
	}
	}
