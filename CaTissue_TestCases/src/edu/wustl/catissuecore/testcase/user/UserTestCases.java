package edu.wustl.catissuecore.testcase.user;

import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.bizlogic.BaseTestCaseUtility;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;

public class UserTestCases extends CaTissueSuiteBaseTest
{
	
	
	public void testSupervisorAdd()
	{
		addRequestParameter("pageOf", "pageOfUserAdmin");		
		UserForm form = new UserForm();
        form.setFirstName("user_first_name_" + UniqueKeyGeneratorUtil.getUniqueKey());
        form.setLastName("user_last_name_" + UniqueKeyGeneratorUtil.getUniqueKey());
        form.setCity("Pune");
        form.setState("Alaska");
        form.setCountry("India");
        form.setZipCode("335001");
        form.setPhoneNumber("9011083118");
        form.setInstitutionId(((Institution)TestCaseUtility.getNameObjectMap("Institution")).getId());
        form.setDepartmentId(((Department)TestCaseUtility.getNameObjectMap("Department")).getId());
        form.setCancerResearchGroupId(((CancerResearchGroup)TestCaseUtility.getNameObjectMap("CancerResearchGroup")).getId());
        form.setOperation("add");
        form.setRole("2");
        form.setEmailAddress("super"+UniqueKeyGeneratorUtil.getUniqueKey()+"@super.com");
        form.setConfirmEmailAddress(form.getEmailAddress());
        Site site = (Site)TestCaseUtility.getNameObjectMap("Site");
        String siteIds[] = new String[1];
        siteIds[0] = site.getId().toString();
        form.setSiteIds(siteIds);
        form.setWustlKey("");
		addRequestParameter("operation", "add");
		addRequestParameter("dirtyVar", "true");
		addRequestParameter("menuSelected", "1");
		setRequestPathInfo("/UserAdd");
        setActionForm(form);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();				
		
		User supervisor = new User();		
		supervisor.setId(form.getId());
		supervisor.setLastName(form.getLastName());
		supervisor.setFirstName(form.getFirstName());
		supervisor.setEmailAddress(form.getEmailAddress());
		
		//add site object to objectMap
		TestCaseUtility.setNameObjectMap("Supervisor",supervisor);
		
	}
	public void testAddScientist()
	 {
		 try
		 {
			User user = BaseTestCaseUtility.initUser();
			user.setRoleId( "7" );
			user.setEmailAddress("Scientist"+ UniqueKeyGeneratorUtil.getUniqueKey()+"@scientist.com");
			user.setLoginName(user.getEmailAddress());
			user.setPageOf(Constants.PAGE_OF_USER_ADMIN);
			user = (User)appService.createObject(user);
			TestCaseUtility.setNameObjectMap( "Scientist", user );
			Logger.out.info("Scientist User added successfully");
			System.out.println("Scientist User added successfully");
			assertTrue("Scientist User added successfully", true);
		    user.setNewPassword("Login123");
		   	user = (User)appService.updateObject(user);		
			
		 }
		 catch(Exception e)
		 {
			 System.out.println("UserTestCases.testAddScientist()"+ e.getMessage());
			 e.printStackTrace();
			 assertFalse("Could not add a Scientist into System", true);
		 }
	 }
	/**
	 * Test First Time Login.
	 */
	
	public void testFirstTimeScientistLogin()
	{
		setRequestPathInfo("/Login");
		User user = (User)TestCaseUtility.getNameObjectMap( "Scientist" );
		addRequestParameter("loginName", user.getEmailAddress());
		addRequestParameter("password", "Login123");
		actionPerform();
		String pageOf = (String) request.getAttribute(Constants.PAGE_OF);
		SessionDataBean sessionData = null;
		if(getSession().getAttribute(Constants.TEMP_SESSION_DATA) != null) 
		{
			sessionData = (SessionDataBean)getSession().getAttribute(Constants.TEMP_SESSION_DATA);
		}
		else 
		{
			sessionData = (SessionDataBean)getSession().getAttribute(Constants.SESSION_DATA);
		}
		String userId = sessionData.getUserId().toString();
		setRequestPathInfo("/UpdatePassword");		
		addRequestParameter("id",userId);
		addRequestParameter("operation", "");
		addRequestParameter("pageOf",pageOf);
		addRequestParameter("oldPassword", "Login123");
		addRequestParameter("newPassword", "Test123");
		addRequestParameter("confirmNewPassword", "Test123");
		actionPerform();
		verifyForward("success");		
		System.out.println("----"+getActualForward());
	}
	/**
	 * Test Login with Valid Login name and Password.
	 */	

	public void testScientistLogin()
	{
		setRequestPathInfo("/Login") ;
		User user = (User)TestCaseUtility.getNameObjectMap( "Scientist" );
		addRequestParameter("loginName", user.getEmailAddress());
		addRequestParameter("password", "Test123");
		logger.info("start in login");
		actionPerform();
		logger.info("Login: "+getActualForward());
		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		assertEquals("user name should be equal to logged in username",user.getEmailAddress(),bean.getUserName());
		CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN=bean;
		verifyNoActionErrors();
		logger.info("end in login");
	} 
}
