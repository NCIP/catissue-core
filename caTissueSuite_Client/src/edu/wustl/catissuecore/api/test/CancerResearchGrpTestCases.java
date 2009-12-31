package edu.wustl.catissuecore.api.test;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;

public class CancerResearchGrpTestCases extends CaTissueBaseTestCase {
	
	AbstractDomainObject domainObject = null;
	
	public void testAddCancerResearchGrp()
	{
		try{
			CancerResearchGroup crg = BaseTestCaseUtility.initCancerResearchGrp();			
			System.out.println(crg);
			crg = (CancerResearchGroup) appService.createObject(crg); 
			TestCaseUtility.setObjectMap(crg,CancerResearchGroup.class);
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testSearchCancerResearchGrp()
	{
			CancerResearchGroup crg = new CancerResearchGroup();
	     	Logger.out.info(" searching domain object");
	    	crg.setId(new Long(1));
	   
	         try {
	        	 List resultList = appService.search(CancerResearchGroup.class,crg);
	        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
	        		 CancerResearchGroup returnedInst = (CancerResearchGroup) resultsIterator.next();
	        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedInst.getName());
	        		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
	             }
	          } 
	          catch (Exception e) {
	           	Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse("Does not find Domain Object", true);
		 		
	          }
	}
	
	public void testUpdateCancerResearchGrp()
	{

		CancerResearchGroup crg =  BaseTestCaseUtility.initCancerResearchGrp();
    	Logger.out.info("updating domain object------->"+crg);
	    try 
		{
	    	crg = (CancerResearchGroup) appService.createObject(crg);
	    	BaseTestCaseUtility.updateCancerResearchGrp(crg);	    	
	    	CancerResearchGroup updatedCRG = (CancerResearchGroup) appService.updateObject(crg);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedCRG);
	       	assertTrue("Domain object successfully updated ---->"+updatedCRG, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
	
	
	public void testWithEmptyCRGName()
	{
		try{
			CancerResearchGroup crg = BaseTestCaseUtility.initCancerResearchGrp();			
			crg.setName("");
			System.out.println(crg);
			crg = (CancerResearchGroup) appService.createObject(crg); 
			assertFalse("Test Failed. Duplicate CRG name should throw exception", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertTrue("CRG Name is required", true);
		 }
	}
	public void testWithDuplicateCRGName()
	{
		try{
			CancerResearchGroup crg = BaseTestCaseUtility.initCancerResearchGrp();
			CancerResearchGroup dupCRGName = BaseTestCaseUtility.initCancerResearchGrp();
			dupCRGName.setName(crg.getName());
			crg = (CancerResearchGroup) appService.createObject(crg); 
			dupCRGName = (CancerResearchGroup) appService.createObject(dupCRGName); 
			assertFalse("Test Failed. Duplicate CRG name should throw exception", true);
		}
		 catch(Exception e){
			 e.printStackTrace();
			 assertTrue("Submission failed since a CRG with the same NAME already exists" , true);
			 
		 }
	}
	
	
//	public void testNullDomainObjectInInsert_CancerResearchGroup()
//	{
//		domainObject = new CancerResearchGroup(); 
//		testNullDomainObjectInInsert(domainObject);
//	}
//	
//	public void testNullSessionDataBeanInInsert_CancerResearchGroup()
//	{
//		domainObject = new CancerResearchGroup();
//		testNullSessionDataBeanInInsert(domainObject);
//	}
//		
//	public void testWrongDaoTypeInInsert_CancerResearchGroup()
//	{
//		domainObject = new CancerResearchGroup();
//		testWrongDaoTypeInInsert(domainObject);
//	}
//	public void testNullSessionDataBeanInUpdate_CancerResearchGroup()
//	{
//		domainObject = new CancerResearchGroup();
//		testNullSessionDataBeanInUpdate(domainObject);
//	}
//	
//	public void testNullOldDomainObjectInUpdate_CancerResearchGroup()
//	{
//		domainObject = new CancerResearchGroup();
//		testNullOldDomainObjectInUpdate(domainObject);
//	}
//	
//		
//	public void testNullCurrentDomainObjectInUpdate_CancerResearchGroup()
//	{
//		domainObject = new CancerResearchGroup();
//		testNullCurrentDomainObjectInUpdate(domainObject);
//	}
//	
//	public void testEmptyCurrentDomainObjectInUpdate_CancerResearchGroup()
//	{
//		domainObject = new CancerResearchGroup();
//		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initCancerResearchGrp();
//		testEmptyCurrentDomainObjectInUpdate(domainObject, initialisedDomainObject);
//	}
//	
//	public void testEmptyOldDomainObjectInUpdate_CancerResearchGroup()
//	{
//		domainObject = new CancerResearchGroup();
//		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initCancerResearchGrp();
//		testEmptyOldDomainObjectInUpdate(domainObject,initialisedDomainObject);
//	}
//	public void testNullDomainObjectInRetrieve_CancerResearchGroup()
//	{
//		domainObject = new CancerResearchGroup();
//		testNullCurrentDomainObjectInRetrieve(domainObject);
//	}
//
//	
}
