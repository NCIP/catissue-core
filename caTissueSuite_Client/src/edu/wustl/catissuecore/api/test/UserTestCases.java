package edu.wustl.catissuecore.api.test;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


/**
 * A person who interacts with the caTISSUE Core data system
 * and/or participates in the process of biospecimen collection,
 * processing, or utilization.
 * @hibernate.class table="CATISSUE_USER"
 * @author sagar_baldwa
 */
public class UserTestCases extends CaTissueBaseTestCase {
	AbstractDomainObject domainObject = null;


	/**
	 * Add a new User in the caTISSUE core system
	 * @return void
	 *
	 */public void testAddUser()
	 {
		 try{
			User user = BaseTestCaseUtility.initUser();
			user = (User)appService.createObject(user);
			TestCaseUtility.setObjectMap(user, User.class);
			Logger.out.info("User added successfully");
			System.out.println("User added successfully");
			assertTrue("User added successfully", true);
		 }
		 catch(Exception e){
			 System.out.println("UserTestCases.testAddUser()"+ e.getMessage());
			 e.printStackTrace();
			 assertFalse("Could not add a User into System", true);
		 }
	 }


	 /**
	  * Add a User with Role as Supervisor
	  * @return void
	  */
	 /*public void testAddSupervisor()
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
			userObj.setPageOf(Constants.PAGE_OF_USER_ADMIN);
			userObj = (User)appService.createObject(userObj);


			userObj.setNewPassword("Login123");

			userObj.setRoleId("2");
			userObj.setActivityStatus("Active");
			userObj.setPageOf(Constants.PAGE_OF_USER_ADMIN);
			userObj = (User)appService.updateObject(userObj);

		}
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("Could not add User with a Scientist Role", true);
		 }
	 }
	*/

	 /**
	  * Add a User with its Role as Technician
	  * @return void
	  */
	 /*public void testAddTechnician()
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
			userObj.setPageOf(Constants.PAGE_OF_USER_ADMIN);

			userObj = (User)appService.createObject(userObj);
			Logger.out.info("User successfully added with Role of Technician");
			System.out.println("User successfully added with Role of Technician");

			userObj.setNewPassword("Login123");

			userObj.setRoleId("3");
			userObj.setActivityStatus("Active");
			userObj.setPageOf(Constants.PAGE_OF_USER_ADMIN);
			userObj = (User)appService.updateObject(userObj);

		}catch(Exception e){
				 e.printStackTrace();
				 assertFalse("User not added with a Role of Technician", true);
		 }
	 }
	*/

	/**
	 * Add a User with its Role as Scientist
	 * @return void
	 */
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
			userObj.setPageOf(Constants.PAGE_OF_USER_ADMIN);


			userObj = (User)appService.createObject(userObj);

			userObj.setNewPassword("Login123");

			userObj.setRoleId("7");
			userObj.setActivityStatus("Active");
			userObj.setPageOf(Constants.PAGE_OF_USER_ADMIN);
			userObj = (User)appService.updateObject(userObj);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse("Could not add a User with a Scientist Role "+e.getMessage(), true);
		 }
	 }
	/**
	 * Search a User in caTISSUE core system
	 * @return void
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
        		 Logger.out.info(" User found successfully  :: " + returneduser.getEmailAddress());
        		 assertTrue("User found successfully", true);
             }
          }
          catch (Exception e) {
        	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		fail("User Not Found in the System");
          }
    }

	/**
	 * Add a User without email address
	 * @return void
	 */
	public void testAddUserWithEmptyEmailAddress()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();
			user.setEmailAddress("");
			user = (User) appService.createObject(user);
			Logger.out.info("User with an empty Email Address");
			fail("Cannot enter a User without an Email Address");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cannot enter User without an Email Address", true);

		 }
	 }


	/**
	 * Add a User without First Name
	 * @return void
	 */
	public void testAddUserWithEmptyFirstName()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();
			user.setFirstName("");
			user = (User) appService.createObject(user);
			Logger.out.info("User with empty First Name");
			fail("Cannot enter a User without a First Name");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cannot enter a User without a First Name", true);

		 }
	 }

	/**
	 * Add a User without Last Name
	 * @return void
	 */
	public void testAddUserWithEmptyLastName()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();
			user.setLastName("");
			user = (User) appService.createObject(user);
			Logger.out.info("User with an empty Last Name");
			fail("Cannot enter a User without a Last Name");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cannot enter a User without a Last Name", true);

		 }
	 }

	/**
	 * Add a User without the City name
	 * @return void
	 */
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
			Logger.out.info("User with an empty City Name");
			fail("Cannot enter a User without a its City Name");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cannot enter a User without its City Name", true);

		 }
	 }

	/**
	 * Add a User without State Name
	 * @return void
	 */
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
			Logger.out.info("User with an empty State Name");
			fail("Cannot enter a User without its State Name");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cannot enter a User without its State Name", true);

		 }
	 }

	/**
	 * Add a User without ZIPCODE
	 * @return void
	 */
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
			Logger.out.info("User with an empty Zip Code");
			assertTrue("Cannot enter a User without a ZipCode", true);

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 fail("Cannot enter a User without a ZipCode");

		 }
	 }

	/**
	 * Add a User without Institution
	 * @return void
	 */
	public void testAddUserWithNullInstitution()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();
			user.setCancerResearchGroup(null);
			System.out.println(user);
			user = (User) appService.createObject(user);
			Logger.out.info("User with an empty Institution Name");
			fail("Cannot enter a User without an Insitution Name");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cannot enter a User without an Insitution Name", true);

		 }
	 }

	/**
	 * It will check for adding a User without its Department Name
	 * @return void
	 */
	public void testAddUserWithNullDepartment()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();
			user.setDepartment(null);
			System.out.println(user);
			user = (User) appService.createObject(user);
			Logger.out.info("User with an empty Department Name");
			fail("Cannot enter a User without a Department Name");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cannot enter a User without a Department Name", true);

		 }
	 }

	/**
	 * It will check for adding a User without specifying its Cancer Research
	 * Group he belongs to.
	 * @return void
	 */
	public void testAddUserWithNullCRG()
	 {
		 try
		 {
			User user = (User) BaseTestCaseUtility.initUser();
			user.setDepartment(null);
			System.out.println(user);
			user = (User) appService.createObject(user);
			Logger.out.info("User with an empty CRG Name");
			fail("Cannot enter a User without a CRG Name");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cannot enter a User without a CRG Name", true);

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

	/**
	 * It will check for the invalid phone number format of the User.
	 * @return void
	 */
	/*public void testAddUserWithInvalidPhoneNumber()
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
			Logger.out.info("User with an Invalid Phone Number  Format");
			fail("Cannot enter a User without Invalid Phone Number Format");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cannot enter a User without Invalid Phone Number Format," +
			 		" correct format is XXX-XXX-XXXX", true);
		 }
	}	*/

	/**
	 * It will check for the Invalid Fax Number Format of the User.
	 * @return void
	 */
	/*public void testAddUserWithInvalidFaxNumber()
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
			Logger.out.info("User with an Invalid fax Number  Format");
			fail("Cannot enter a User without Invalid Fax Number Format");
		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cannot enter a User without Invalid Phone Number Format," +
			 		" correct format is XXX-XXX-XXXX", true);
		 }
	}*/

	/**
	 * Add a user without its Phone Number
	 * @return void
	 */
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
			Logger.out.info("User without a Phone Number");
			assertTrue("Cannot enter a User without a Phone Number", true);

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 fail("Cannot enter a User without a Phone Number");
		 }
	}

	/**
	 * It will check wheather the First Name of User is Vulnerable or not
	 * @return void
	 */
	public void testAddUserWithXssVulnerableFirstName()
	{
		try
		 {
			User user = (User) BaseTestCaseUtility.initUser();
			user.setFirstName("Test>");
			user = (User) appService.createObject(user);
			Logger.out.info("User with XSS vulnerable First Name");
			fail("Cannot enter a User with an XSS Vulnerable First Name");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cannot enter a User with an XSS Vulnerable First Name", true);

		 }
	}

	/**
	 * It will check wheather the Last Name of User is Vulnerable or not
	 * @return void
	 */
	public void testAddUserWithXssVulnerableLastName()
	{
		try
		 {
			User user = (User) BaseTestCaseUtility.initUser();
			user.setLastName("Test)");
			user = (User) appService.createObject(user);
			Logger.out.info("User with XSS vulnerable Last Name");
			fail("Cannot enter a User with an XSS Vulnerable Last Name");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cannot enter a User with an XSS Vulnerable Last Name", true);

		 }
	}

	/**
	 * It will check wheather the First Name of User can be updated which could
	 * be of Vulnerable type
	 * @return void
	 */
	public void testUpdateUserWithXssVulnerableFirstName()
	{
		try
		 {
			User user = BaseTestCaseUtility.initUser();
			user = (User)appService.createObject(user);
			TestCaseUtility.setObjectMap(user, User.class);
			Logger.out.info("User Added successfully");

			user.setFirstName("Test<");
			user = (User)appService.updateObject(user);

			Logger.out.info("Update User with XSS vulnerable First Name");
			fail("Cannot Update a User with an XSS Vulnerable First Name");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cannot Update a User with an XSS Vulnerable First Name", true);

		 }
	}

	/**
	 * It will check wheather the Last Name of User can be updated which could
	 * be of Vulnerable type
	 * @return void
	 */
	public void testUpdateUserWithXssVulnerableLastName()
	{
		try
		 {
			User user = BaseTestCaseUtility.initUser();
			user = (User)appService.createObject(user);
			TestCaseUtility.setObjectMap(user, User.class);
			Logger.out.info("User Added successfully");

			user.setLastName("Test(");
			user = (User)appService.updateObject(user);

			Logger.out.info("User with XSS vulnerable Last Name");
			fail("Cannot Update a User with an XSS Vulnerable Last Name");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Cannot Update a User with an XSS Vulnerable Last Name", true);

		 }
	}
	/**
	 * Add a User with an Null Password
	 * @return void
	 */
	/*public void testAddUserWithNullPassword()
	{
		try
		 {
			User user = BaseTestCaseUtility.initUser();
			user.setNewPassword(null);
			user = (User)appService.createObject(user);
			TestCaseUtility.setObjectMap(user, User.class);
			Logger.out.info("Object created successfully with null password");

			fail("For null password, it should throw exception");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("For null password, it should throw exception", true);

		 }
	}*/

	/**
	 * Add a USer with Empty Password
	 * @return void
	 */
	/*
	public void testAddUserWithEmptyPassword()
	{
		try
		 {
			User user = BaseTestCaseUtility.initUser();
			user.setNewPassword("");
			user = (User)appService.createObject(user);
			TestCaseUtility.setObjectMap(user, User.class);
			Logger.out.info("Object created successfully with empty password");

			fail("For empty password, it should throw exception");

		 }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("For empty password, it should throw exception",true);

		 }
	}*/
}
