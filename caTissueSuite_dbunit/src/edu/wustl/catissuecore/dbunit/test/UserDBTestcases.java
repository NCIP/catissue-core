/**
 * @Class UserDBTestcases.java
 * @Author abhijit_naik
 * @Created on Aug 12, 2008
 */
package edu.wustl.catissuecore.dbunit.test;

import java.io.FileInputStream;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.global.Constants;


/**
 * @author abhijit_naik
 *
 */
public class UserDBTestcases extends DefaultCatissueDBUnitTestCase
{

	public UserDBTestcases() throws Exception
	{
		super();
		
	}

	public void testUserCreate()
	{
		try
		{
			insertObjectsOf(User.class);
			assertTrue("UserCreated",true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert user");
			
		}
	}
	public void testUserUpdate()
	{
		try
		{
	
			UpdateObjects(User.class);
			assertTrue("UserUpdated", true);
		}catch(Exception e)
		{
			e.printStackTrace();
			fail("failed to insert user");
		}
	}

	public static User initUser()
	{
		User userObj = new User();
		userObj.setEmailAddress("ddd"+ UniqueKeyGeneratorUtil.getUniqueKey()+"@admin.com");
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
		
		userObj.setRoleId("1");
		userObj.setActivityStatus("Active");
		userObj.setPageOf(Constants.PAGEOF_SIGNUP);

		return userObj;
	}

}
