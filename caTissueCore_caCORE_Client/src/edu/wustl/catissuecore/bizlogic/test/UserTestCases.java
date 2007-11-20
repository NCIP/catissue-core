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
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	 }
	
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
	
	public void testAddUserWithNullInstitutuion()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();		
			user.setInstitution(null);
			System.out.println(user);
			user = (User) appService.createObject(user);
			Logger.out.info("For empty null Institute name, it should throw exception");
			fail("For empty null Institute name, it should throw exception");
			
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("For empty last name, it throws exception", true);
			 
		 }
	 }
	
	public void testAddUserWithNullDeptepartment()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();		
			user.setDepartment(null);
			System.out.println(user);
			user = (User) appService.createObject(user);
			Logger.out.info("For empty last name, it should throw exception");
			fail("For empty last name, it should throw exception");
			
		 }
		 catch(Exception e)
		 {
		//	 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("For empty last name, it throws exception", true);
			 
		 }
	 }
	
}
