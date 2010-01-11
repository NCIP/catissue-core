package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class DepartmentBizTestCases extends CaTissueSuiteBaseTest {

	AbstractDomainObject domainObject = null;
	
	public void testAddDepartment()
	{
		try{
			Department dept = BaseTestCaseUtility.initDepartment();			
			System.out.println(dept);
			dept = (Department) appService.createObject(dept);
			TestCaseUtility.setObjectMap(dept, Department.class);
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testSearchDepartmet()
	{
		Department dept = new Department();
      	Logger.out.info(" searching domain object");
    	dept.setId(new Long(1));
          try {
        	  String query = "from edu.wustl.catissuecore.domain.Department as department where "
  				+ "department.id= 1";
          	 List resultList = appService.search(query);

//         	 List resultList = appService.search(Department.class,dept);
         	 //List resultList = appService.search(Department.class,dept);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
        	 {
        		 Department returnedDepartment = (Department) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
        	 }
          } 
          catch (Exception e) {
           	Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Does not find Domain Object", true);	 		
          }
	}
	
	public void testUpdateDepartment()
	{
		Department department =  BaseTestCaseUtility.initDepartment();
    	Logger.out.info("updating domain object------->"+department);
	    try 
		{
	    	department = (Department) appService.createObject(department);
	    	BaseTestCaseUtility.updateDepartment(department);	    	
	    	Department updatedDepartment = (Department) appService.updateObject(department);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedDepartment);
	       	assertTrue("Domain object successfully updated ---->"+updatedDepartment, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
	
	public void testWithEmptyDepartmentName()
	{
		try{
			Department dept = BaseTestCaseUtility.initDepartment();			
			dept.setName("");
			System.out.println(dept);
			dept = (Department) appService.createObject(dept); 
			assertFalse("Empty Department name added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertTrue("Name is required", true);
		 }
	}
	public void testWithDuplicateDepartmentName()
	{
		try{
			Department dept = BaseTestCaseUtility.initDepartment();
			Department dupDept = BaseTestCaseUtility.initDepartment();
			dupDept.setName(dept.getName());
			dept = (Department) appService.createObject(dept); 
			dupDept = (Department) appService.createObject(dupDept); 
			assertFalse("Test Failed. Duplicate dept name should throw exception", true);
		}
		 catch(Exception e){
			e.printStackTrace();
			assertTrue("Submission failed since a Department with the same NAME already exists" , true);
			 
		 }
	}
	
//	public void testNullDomainObjectInInsert_Department()
//	{
//		domainObject = new Department(); 
//		testNullDomainObjectInInsert(domainObject);
//	}
//	
//	public void testNullSessionDataBeanInInsert_Department()
//	{
//		domainObject = new Department();
//		testNullSessionDataBeanInInsert(domainObject);
//	}
//		
//	public void testWrongDaoTypeInInsert_Department()
//	{
//		domainObject = new Department();
//		testWrongDaoTypeInInsert(domainObject);
//	}
//	public void testNullSessionDataBeanInUpdate_Department()
//	{
//		domainObject = new Department();
//		testNullSessionDataBeanInUpdate(domainObject);
//	}
//	
//	public void testNullOldDomainObjectInUpdate_Department()
//	{
//		domainObject = new Department();
//		testNullOldDomainObjectInUpdate(domainObject);
//	}
//	
//		
//	public void testNullCurrentDomainObjectInUpdate_Department()
//	{
//		domainObject = new Department();
//		testNullCurrentDomainObjectInUpdate(domainObject);
//	}
//	
//	public void testEmptyCurrentDomainObjectInUpdate_Department()
//	{
//		domainObject = new Department();
//		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initDepartment();
//		testEmptyCurrentDomainObjectInUpdate(domainObject, initialisedDomainObject);
//	}
//	
//	public void testEmptyOldDomainObjectInUpdate_Department()
//	{
//		domainObject = new Department();
//		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initDepartment();
//		testEmptyOldDomainObjectInUpdate( domainObject , initialisedDomainObject);
//		
//	}
//	
//	public void testNullDomainObjectInRetrieve_Department()
//	{
//		domainObject = new Department();
//		testNullCurrentDomainObjectInRetrieve(domainObject);
//	}
//	
//	
//	
}
