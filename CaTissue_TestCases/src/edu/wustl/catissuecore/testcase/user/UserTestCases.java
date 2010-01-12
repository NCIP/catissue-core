package edu.wustl.catissuecore.testcase.user;

import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;

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
}
