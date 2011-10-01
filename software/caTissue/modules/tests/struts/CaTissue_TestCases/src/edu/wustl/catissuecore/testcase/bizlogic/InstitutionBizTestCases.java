package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;

public class InstitutionBizTestCases extends CaTissueSuiteBaseTest{
	AbstractDomainObject domainObject = null;
	
	public void testAddInstitution()
	{
		try{
			Institution institution = BaseTestCaseUtility.initInstitution();
			System.out.println(institution);
			institution = (Institution) appService.createObject(institution); 
			BizTestCaseUtility.setObjectMap(institution, Institution.class);
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse(e.getMessage(), true);
		 }
	}
	
	public void testUpdateInstitution()
	{
		Institution institution =  BaseTestCaseUtility.initInstitution();
    	Logger.out.info("updating domain object------->"+institution);
	    try 
		{
	    	institution = (Institution) appService.createObject(institution);
	    	BaseTestCaseUtility.updateInstitution(institution);	    	
	    	Institution updatedInst = (Institution) appService.updateObject(institution);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedInst);
	       	assertTrue("Domain object successfully updated ---->"+updatedInst, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse(e.getMessage(), true);
	    }
	}

	public void testWithEmptyInstitutionName()
	{
		try{
			Institution inst = BaseTestCaseUtility.initInstitution();			
			inst.setName("");
			System.out.println(inst);
			inst = (Institution) appService.createObject(inst); 
			assertFalse("Test Failed. Duplicate institution name should throw exception", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertTrue("Institution Name is required", true);
		 }
	}
	public void testWithDuplicateInstitutionName()
	{
		try{
			Institution inst = BaseTestCaseUtility.initInstitution();
			Institution dupInstName = BaseTestCaseUtility.initInstitution();
			dupInstName.setName(inst.getName());
			inst = (Institution) appService.createObject(inst); 
			dupInstName = (Institution) appService.createObject(dupInstName); 
			assertFalse("Test Failed. Duplicate dept name should throw exception", true);
		}
		 catch(Exception e){
			 e.printStackTrace();
			 assertTrue("Submission failed since a Department with the same NAME already exists" , true);
			 
		 }
	}

	public void testSearchInstitution()
	{
		Institution institution = new Institution();
    	Logger.out.info(" searching domain object");
    	institution.setId(new Long(1));
   
         try {
        	 String query = "from edu.wustl.catissuecore.domain.Institution as institution where "
 				+ "institution.id= 1";	
        	 List resultList = appService.search(query);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Institution returnedInst = (Institution) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedInst.getName());
        		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
             }
          } 
          catch (Exception e) {
           	Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse(e.getMessage(), true);
	 		
          }
	}
	
//	public void testNullDomainObjectInInsert_Institution()
//	{
//		domainObject = new Institution(); 
//		testNullDomainObjectInInsert(domainObject);
//	}
//	
//	public void testNullSessionDataBeanInInsert_Institution()
//	{
//		domainObject = new Institution();
//		testNullSessionDataBeanInInsert(domainObject);
//	}
//		
//	public void testWrongDaoTypeInInsert_Institution()
//	{
//		domainObject = new Institution();
//		testWrongDaoTypeInInsert(domainObject);
//	}
//	public void testNullSessionDataBeanInUpdate_Institution()
//	{
//		domainObject = new Institution();
//		testNullSessionDataBeanInUpdate(domainObject);
//	}
//	
//	public void testNullOldDomainObjectInUpdate_Institution()
//	{
//		domainObject = new Institution();
//		testNullOldDomainObjectInUpdate(domainObject);
//	}
//	
//		
//	public void testNullCurrentDomainObjectInUpdate_Institution()
//	{
//		domainObject = new Institution();
//		testNullCurrentDomainObjectInUpdate(domainObject);
//	}
//	
//	public void testEmptyCurrentDomainObjectInUpdate_Institution()
//	{
//		domainObject = new Institution();
//		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initInstitution();
//		testEmptyCurrentDomainObjectInUpdate(domainObject, initialisedDomainObject);
//	}
//	
//	public void testEmptyOldDomainObjectInUpdate_Institution()
//	{
//		domainObject = new Institution();
//		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initInstitution();
//		testEmptyOldDomainObjectInUpdate(domainObject, initialisedDomainObject);
//	}
//	
//	public void testNullDomainObjectInRetrieve_Institution()
//	{
//		domainObject = new Institution();
//		testNullCurrentDomainObjectInRetrieve(domainObject);
//	}
	
}
