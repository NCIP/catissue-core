package edu.wustl.catissuecore.bizlogic.test;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.domain.AbstractDomainObject;


public class UserTestCases extends CaTissueBaseTestCase {
	AbstractDomainObject domainObject = null;
	
		
	public void testAddUser()
	 {
		 try{
			User user = BaseTestCaseUtility.initUser();			
			System.out.println(user);
			user = (User) appService.createObject(user); 
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	 }
	
	
	
	
	
	/*public void testEmptyObjectInInsert_User(){
		domainObject = new User();
		super.testEmptyDomainObjectInInsert(domainObject);
	}
	
	public void testNullObjectInInsert_User(){
		domainObject = new User(); 
		super.testNullDomainObjectInInsert(domainObject);
	}
	
	public void testNullSessionDatBeanInInsert_User(){
		domainObject = new User();
		super.testNullSessionDataBeanInInsert(domainObject);
	}
	
	public void testNullSessionDataBeanInUpdate_User(){
		domainObject = new User();
		super.testNullSessionDataBeanInUpdate(domainObject);
	}
	
	public void testNullOldDomainObjectInUpdate_User(){
		domainObject = new User();
		super.testNullOldDomainObjectInUpdate(domainObject);
	}
	
	public void testNullCurrentDomainObjectInUpdate_User(){
		domainObject = new User();
		super.testNullCurrentDomainObjectInUpdate(domainObject);
	}
	
	public void testWrongDaoTypeInUpdate_User(){
		domainObject = new User();
		super.testNullCurrentDomainObjectInUpdate(domainObject);
	}
	
	public void testEmptyCurrentDomainObjectInUpdate_User(){
		domainObject = new User();
		super.testEmptyCurrentDomainObjectInUpdate(domainObject);
	}
	
	public void testEmptyOldDomainObjectInUpdate_User(){
		domainObject = new User();
		super.testEmptyOldDomainObjectInUpdate(domainObject);
	}
*/}
