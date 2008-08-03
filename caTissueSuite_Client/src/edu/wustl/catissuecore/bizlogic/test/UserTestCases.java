package edu.wustl.catissuecore.bizlogic.test;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class UserTestCases extends CaTissueBaseTestCase {
	AbstractDomainObject domainObject = null;
	
		
	public void testAddUser()
	 {
		 try{
			User user = BaseTestCaseUtility.initUser();
			user = (User)appService.createObject(user);
			TestCaseUtility.setObjectMap(user, User.class);
			Logger.out.info("Object created successfully");
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 System.out.println("UserTestCases.testAddUser()"+ e.getMessage());
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	 }
	
	/*
	 public void testAddSupervisor()
	 {
		 try{
			User userObj = new User();
			userObj.setEmailAddress("supervisor@admin.com");
			userObj.setLoginName(userObj.getEmailAddress());
			userObj.setLastName("last" + UniqueKeyGeneratorUtil.getUniqueKey());
			userObj.setFirstName("name" + UniqueKeyGeneratorUtil.getUniqueKey());
			
			Address address = new Address();
			address.setStreet("Main street");
			address.setCity("New hampshier");
			address.setState("Alabama");
			address.setZipCode("12345");
			address.setCountry("United States");
			address.setPhoneNumber("212-223-2424");
			address.setFaxNumber("212-223-2424");	 
			
			
			userObj.setAddress(address);
			
			Institution inst = new Institution();
			inst.setId(new Long(1));
			userObj.setInstitution(inst);
			
			Department department = new Department();
			department.setId(new Long(1));
			userObj.setDepartment(department);
			
			CancerResearchGroup cancerResearchGroup =  new CancerResearchGroup();
			cancerResearchGroup.setId(new Long(1));
			userObj.setCancerResearchGroup(cancerResearchGroup);
			
			
			userObj.setRoleId("2");
			userObj.setActivityStatus("Active");
			userObj.setPageOf(Constants.PAGEOF_USER_ADMIN);		
			userObj = (User)appService.createObject(userObj);	
			

			userObj.setNewPassword("Test123");
			
			userObj.setRoleId("2");
			userObj.setActivityStatus("Active");
			userObj.setPageOf(Constants.PAGEOF_USER_ADMIN);	
			userObj = (User)appService.updateObject(userObj);	
			
		}
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }		 
	 }
	
	public void testAddTechnician()
	 {
		try{
			User userObj = new User();
			userObj.setEmailAddress("technician@admin.com");
			userObj.setLoginName(userObj.getEmailAddress());
			userObj.setLastName("last" + UniqueKeyGeneratorUtil.getUniqueKey());
			userObj.setFirstName("name" + UniqueKeyGeneratorUtil.getUniqueKey());
			
			Address address = new Address();
			address.setStreet("Main street");
			address.setCity("New hampshier");
			address.setState("Alabama");
			address.setZipCode("12345");
			address.setCountry("United States");
			address.setPhoneNumber("212-223-2424");
			address.setFaxNumber("212-223-2424");	 
			
			
			userObj.setAddress(address);
			
			Institution inst = new Institution();
			inst.setId(new Long(1));
			userObj.setInstitution(inst);
			
			Department department = new Department();
			department.setId(new Long(1));
			userObj.setDepartment(department);
			
			CancerResearchGroup cancerResearchGroup =  new CancerResearchGroup();
			cancerResearchGroup.setId(new Long(1));
			userObj.setCancerResearchGroup(cancerResearchGroup);
			
			userObj.setRoleId("3");
			userObj.setActivityStatus("Active");
			userObj.setPageOf(Constants.PAGEOF_USER_ADMIN);
		
			userObj = (User)appService.createObject(userObj);			
			Logger.out.info("Object created successfully");
			System.out.println("Object created successfully");
			
			userObj.setNewPassword("Test123");
			
			userObj.setRoleId("3");
			userObj.setActivityStatus("Active");
			userObj.setPageOf(Constants.PAGEOF_USER_ADMIN);	
			userObj = (User)appService.updateObject(userObj);	
			
		}catch(Exception e){
				 e.printStackTrace();
				 assertFalse("could not add object", true);
		 }			
	 } 
	
	public void testAddScientist()
	 {
		try{
			User userObj = new User();
			userObj.setEmailAddress("scientist@admin.com");
			userObj.setLoginName(userObj.getEmailAddress());
			userObj.setLastName("last" + UniqueKeyGeneratorUtil.getUniqueKey());
			userObj.setFirstName("name" + UniqueKeyGeneratorUtil.getUniqueKey());
			
			Address address = new Address();
			address.setStreet("Main street");
			address.setCity("New hampshier");
			address.setState("Alabama");
			address.setZipCode("12345");
			address.setCountry("United States");
			address.setPhoneNumber("212-223-2424");
			address.setFaxNumber("212-223-2424");	 
			
			
			userObj.setAddress(address);
			
			Institution inst = new Institution();
			inst.setId(new Long(1));
			userObj.setInstitution(inst);
			
			Department department = new Department();
			department.setId(new Long(1));
			userObj.setDepartment(department);
			
			CancerResearchGroup cancerResearchGroup =  new CancerResearchGroup();
			cancerResearchGroup.setId(new Long(1));
			userObj.setCancerResearchGroup(cancerResearchGroup);
			
			userObj.setRoleId("7");
			userObj.setActivityStatus("Active");
			userObj.setPageOf(Constants.PAGEOF_USER_ADMIN);

		
			userObj = (User)appService.createObject(userObj);
			
			userObj.setNewPassword("Test123");
			
			userObj.setRoleId("7");
			userObj.setActivityStatus("Active");
			userObj.setPageOf(Constants.PAGEOF_USER_ADMIN);	
			userObj = (User)appService.updateObject(userObj);	
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	 } 
	*/	
	
	public void testSearchUser()
    {
    	 User user = (User) new User();
    	 User cachedUser = (User) TestCaseUtility.getObjectMap(User.class);
     	 user.setId((Long)cachedUser.getId());
     	 Logger.out.info(" searching domain object");
    	     	 
         try {
        	 List resultList = appService.search(User.class,user);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 User returneduser = (User) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returneduser.getEmailAddress());
        		 assertTrue("Domain Object is successfully Found", true);
             }
          } 
          catch (Exception e) {
        	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		fail("could not find object");
          }
    }
	
	public void testAddUserWithEmptyEmailAddress()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();		
			user.setEmailAddress("");
			user = (User) appService.createObject(user); 
			Logger.out.info("User with empty email addres");
			fail("For empty email addres, it should throw exception");
			
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("For empty email address, it throws exception", true);
			 
		 }
	 }
	
	public void testAddUserWithEmptyFirstName()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();		
			user.setFirstName("");
			user = (User) appService.createObject(user); 
			Logger.out.info("User with empty first name");
			fail("For empty first name, it should throw exception");
			
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("For empty first name, it throws exception", true);
			 
		 }
	 }
	
	public void testAddUserWithEmptyLastName()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();		
			user.setLastName("");
			user = (User) appService.createObject(user); 
			Logger.out.info("User with empty last name");
			fail("For empty last name, it should throw exception");
			
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("For empty last name, it throws exception", true);
			 
		 }
	 }
	
	public void testAddUserWithEmptyCityName()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();		
			Address address = new Address();
			address.setStreet("Main street");
			address.setCity("");
			address.setState("Alabama");
			address.setZipCode("12345");
			address.setCountry("United States");
			address.setPhoneNumber("21222324");
			address.setFaxNumber("21222324");
			user.setAddress(address);
			user = (User) appService.createObject(user);
			Logger.out.info("User with empty last name");
			fail("For empty last name, it should throw exception");
			
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("For empty last name, it throws exception", true);
			 
		 }
	 }
	
	public void testAddUserWithEmptyStateName()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();		
			Address address = new Address();
			address.setStreet("Main street");
			address.setCity("New hampshier");
			address.setState("");
			address.setZipCode("12345");
			address.setCountry("United States");
			address.setPhoneNumber("21222324");
			address.setFaxNumber("21222324");
			user.setAddress(address);
			System.out.println(user);
			user = (User) appService.createObject(user);
			Logger.out.info("For empty last name, it should throw exception");
			fail("For empty last name, it should throw exception");
			
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("For empty last name, it throws exception", true);
			 
		 }
	 }
	
	public void testAddUserWithEmptyZipCode()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();		
			Address address = new Address();
			address.setStreet("Main street");
			address.setCity("New hampshier");
			address.setState("Alabama");
			address.setZipCode("");
			address.setCountry("United States");
			address.setPhoneNumber("21222324");
			address.setFaxNumber("21222324");
			user.setAddress(address);
			System.out.println(user);
			user = (User) appService.createObject(user);
			Logger.out.info("For empty last name, it should throw exception");
			fail("For empty last name, it should throw exception");
			
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("For empty last name, it throws exception", true);
			 
		 }
	 }
	
	public void testAddUserWithNullInstitution()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();		
			user.setCancerResearchGroup(null);
			System.out.println(user);
			user = (User) appService.createObject(user);
			Logger.out.info("For null Institute name, it should throw exception");
			fail("For null Institute name, it should throw exception");
			
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("For empty last name, it throws exception", true);
			 
		 }
	 }
	
	public void testAddUserWithNullDepartment()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();		
			user.setDepartment(null);
			System.out.println(user);
			user = (User) appService.createObject(user);
			Logger.out.info("For null department, it should throw exception");
			fail("For null department, it should throw exception");
			
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("For null Department, it throws exception", true);
			 
		 }
	 }
	
	public void testAddUserWithNullCRG()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();		
			user.setDepartment(null);
			System.out.println(user);
			user = (User) appService.createObject(user);
			Logger.out.info("For null CRG, it should throw exception");
			fail("For null CRG, it should throw exception");	
					
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("For empty last name, it throws exception", true);			 
			 
		 }
	 }

	/*
	public void testAddUserWithNullRoleId()
	 {
		 try
		 {
			User user = BaseTestCaseUtility.initUser();		
			user.setRoleId("");
			System.out.println(user);
			user = (User) appService.createObject(user);
			Logger.out.info("For invalid role id, it should throw exception");
			fail("For invalid role id, it should throw exception");
						
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("For invalid role id, it throws exception", true);			 
			 
		 }
	 }  
	*/
	
	public void testAddUserWithInvalidPhoneNumber()
	{
		try
		 {
			User user = (User) BaseTestCaseUtility.initUser();		
			Address address = new Address();
			address.setStreet("Main street");
			address.setCity("New hampshier");
			address.setState("Alabama");
			address.setZipCode("12345");
			address.setCountry("United States");
			address.setPhoneNumber("21222324");
			address.setFaxNumber("212-223-2224");
			user.setAddress(address);
			System.out.println(user);
			user = (User) appService.createObject(user);
			Logger.out.info("Invalid phone number entered, it should throw exception");
			fail("Invalid phone number entered, it should throw exception");
			
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Invalid phone number entered, correct format is xxx-xxx-xxxx", true);			 
		 }		
	}	
	
	public void testAddUserWithInvalidFaxNumber()
	{
		try
		 {
			User user = (User) BaseTestCaseUtility.initUser();		
			Address address = new Address();
			address.setStreet("Main street");
			address.setCity("New hampshier");
			address.setState("Alabama");
			address.setZipCode("12345");
			address.setCountry("United States");
			address.setPhoneNumber("212-223-2224");
			address.setFaxNumber("2122232224");
			user.setAddress(address);
			System.out.println(user);
			user = (User) appService.createObject(user);
			Logger.out.info("For invalid fax number, it should throw exception");
			fail("Invalid fax number entered, it should throw exception");			
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Invalid fax number entered, correct format is xxx-xxx-xxxx", true);			 
		 }		
	}
	
	public void testAddUserWithEmptyPhoneNumber()
	{
		try
		 {
			User user = (User) BaseTestCaseUtility.initUser();		
			Address address = new Address();
			address.setStreet("Main street");
			address.setCity("New hampshier");
			address.setState("Alabama");
			address.setZipCode("12345");
			address.setCountry("United States");
			address.setPhoneNumber("");
			address.setFaxNumber("2122232224");
			user.setAddress(address);
			System.out.println(user);
			user = (User) appService.createObject(user);
			Logger.out.info("Phone number not entered, it should throw exception");
			fail("Phone number not entered, it should throw exception");
			
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Phone number is Empty", true);			 
		 }		
	}	
}
