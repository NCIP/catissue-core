package edu.wustl.catissuecore.smoketest.login;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;


public class UserSignUpTestCase extends CaTissueSuiteBaseTest
{
	public void testUserSignUp()
	{
		setRequestPathInfo("/SignUp");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfSignUp");
		actionPerform();
		verifyForward("pageOfSignUp");
		verifyNoActionErrors();

		Institution institution = (Institution)TestCaseUtility.getNameObjectMap("Institution");
		Department department= (Department)TestCaseUtility.getNameObjectMap("Department");
		CancerResearchGroup researchGroup= (CancerResearchGroup)TestCaseUtility.getNameObjectMap("CancerResearchGroup");

		UserForm userForm = (UserForm)getActionForm();
		userForm.setInstitutionId(institution.getId());
		userForm.setDepartmentId(department.getId());
		userForm.setCancerResearchGroupId(researchGroup.getId());
		String emailAddress = "signUpUser"+UniqueKeyGeneratorUtil.getUniqueKey()+"@test.com";
		userForm.setEmailAddress(emailAddress);
		userForm.setConfirmEmailAddress(emailAddress);
		userForm.setLastName(emailAddress);
		userForm.setFirstName(emailAddress);
		userForm.setStreet(emailAddress);
		userForm.setCity(emailAddress);
		userForm.setZipCode("111111");
		userForm.setCountry("United States");
		userForm.setPhoneNumber("444-556-8889");
		userForm.setState("Alabama");
		setRequestPathInfo("/SignUpUserAdd");
		setActionForm(userForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		User user = new User();
		user.setEmailAddress(userForm.getEmailAddress());

		TestCaseUtility.setNameObjectMap("SignedUpUser", user);
	}

	public void testSignUpuserNegative()
	{
		setRequestPathInfo("/SignUp");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfSignUp");
		actionPerform();
		verifyForward("pageOfSignUp");
		verifyNoActionErrors();

		UserForm userForm = (UserForm)getActionForm();
//		userForm.setInstitutionId(institution.getId());
//		userForm.setDepartmentId(department.getId());
//		userForm.setCancerResearchGroupId(researchGroup.getId());
		String emailAddress = "signUpUser"+UniqueKeyGeneratorUtil.getUniqueKey()+"@test.com";
		userForm.setEmailAddress("");
		userForm.setConfirmEmailAddress("");
		userForm.setLastName("");
		userForm.setFirstName("");
		userForm.setStreet("");
		userForm.setCity("");
		userForm.setZipCode("");
		userForm.setCountry("");
		userForm.setPhoneNumber("");
		userForm.setState("");
		setRequestPathInfo("/SignUpUserAdd");
		setActionForm(userForm);
		actionPerform();
		String[] errors={"errors.item.required","errors.item.required","errors.item.required","errors.item.required","errors.item.required","errors.item.required","errors.item.required"};
		verifyActionErrors(errors);
	}

	public void testUserSignUpwithDuplicateEmailId()
	{
		setRequestPathInfo("/SignUp");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfSignUp");
		actionPerform();
		verifyForward("pageOfSignUp");
		verifyNoActionErrors();

		Institution institution = (Institution)TestCaseUtility.getNameObjectMap("Institution");
		Department department= (Department)TestCaseUtility.getNameObjectMap("Department");
		CancerResearchGroup researchGroup= (CancerResearchGroup)TestCaseUtility.getNameObjectMap("CancerResearchGroup");
		User user = (User)TestCaseUtility.getNameObjectMap("SignedUpUser");

		UserForm userForm = (UserForm)getActionForm();
		userForm.setInstitutionId(institution.getId());
		userForm.setDepartmentId(department.getId());
		userForm.setCancerResearchGroupId(researchGroup.getId());
		String emailAddress = user.getEmailAddress();
		userForm.setEmailAddress(emailAddress);
		userForm.setConfirmEmailAddress(emailAddress);
		userForm.setLastName(emailAddress);
		userForm.setFirstName(emailAddress);
		userForm.setStreet(emailAddress);
		userForm.setCity(emailAddress);
		userForm.setZipCode("111111");
		userForm.setCountry("United States");
		userForm.setPhoneNumber("444-556-8889");
		userForm.setState("Alabama");
		setRequestPathInfo("/SignUpUserAdd");
		setActionForm(userForm);
		actionPerform();
		verifyActionErrors(new String[]{"errors.item"});

	}

	public void testUserSignUpWithEmptyInstitute()
	{
		setRequestPathInfo("/SignUp");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfSignUp");
		actionPerform();
		verifyForward("pageOfSignUp");
		verifyNoActionErrors();


		Department department= (Department)TestCaseUtility.getNameObjectMap("Department");
		CancerResearchGroup researchGroup= (CancerResearchGroup)TestCaseUtility.getNameObjectMap("CancerResearchGroup");

		UserForm userForm = (UserForm)getActionForm();
		userForm.setDepartmentId(department.getId());
		userForm.setCancerResearchGroupId(researchGroup.getId());
		String emailAddress = "signUpUser"+UniqueKeyGeneratorUtil.getUniqueKey()+"@test.com";
		userForm.setEmailAddress(emailAddress);
		userForm.setConfirmEmailAddress(emailAddress);
		userForm.setLastName(emailAddress);
		userForm.setFirstName(emailAddress);
		userForm.setStreet(emailAddress);
		userForm.setCity(emailAddress);
		userForm.setZipCode("111111");
		userForm.setCountry("United States");
		userForm.setPhoneNumber("444-556-8889");
		userForm.setState("Alabama");
		setRequestPathInfo("/SignUpUserAdd");
		setActionForm(userForm);
		actionPerform();
		verifyActionErrors(new String[]{"errors.item"});

	}

	public void testUserSignUpWithEmptyDept()
	{
		setRequestPathInfo("/SignUp");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfSignUp");
		actionPerform();
		verifyForward("pageOfSignUp");
		verifyNoActionErrors();

		Institution institution = (Institution)TestCaseUtility.getNameObjectMap("Institution");
		CancerResearchGroup researchGroup= (CancerResearchGroup)TestCaseUtility.getNameObjectMap("CancerResearchGroup");

		UserForm userForm = (UserForm)getActionForm();
		userForm.setInstitutionId(institution.getId());
		userForm.setCancerResearchGroupId(researchGroup.getId());
		String emailAddress = "signUpUser"+UniqueKeyGeneratorUtil.getUniqueKey()+"@test.com";
		userForm.setEmailAddress(emailAddress);
		userForm.setConfirmEmailAddress(emailAddress);
		userForm.setLastName(emailAddress);
		userForm.setFirstName(emailAddress);
		userForm.setStreet(emailAddress);
		userForm.setCity(emailAddress);
		userForm.setZipCode("111111");
		userForm.setCountry("United States");
		userForm.setPhoneNumber("444-556-8889");
		userForm.setState("Alabama");
		setRequestPathInfo("/SignUpUserAdd");
		setActionForm(userForm);
		actionPerform();
		verifyActionErrors(new String[]{"errors.item"});

	}

	public void testUserSignUpWithEmptyCRG()
	{
		setRequestPathInfo("/SignUp");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfSignUp");
		actionPerform();
		verifyForward("pageOfSignUp");
		verifyNoActionErrors();

		Institution institution = (Institution)TestCaseUtility.getNameObjectMap("Institution");
		Department department= (Department)TestCaseUtility.getNameObjectMap("Department");

		UserForm userForm = (UserForm)getActionForm();
		userForm.setInstitutionId(institution.getId());
		userForm.setDepartmentId(department.getId());
		String emailAddress = "signUpUser"+UniqueKeyGeneratorUtil.getUniqueKey()+"@test.com";
		userForm.setEmailAddress(emailAddress);
		userForm.setConfirmEmailAddress(emailAddress);
		userForm.setLastName(emailAddress);
		userForm.setFirstName(emailAddress);
		userForm.setStreet(emailAddress);
		userForm.setCity(emailAddress);
		userForm.setZipCode("111111");
		userForm.setCountry("United States");
		userForm.setPhoneNumber("444-556-8889");
		userForm.setState("Alabama");
		setRequestPathInfo("/SignUpUserAdd");
		setActionForm(userForm);
		actionPerform();
		verifyActionErrors(new String[]{"errors.item"});

	}

	public void testUserSignUpWithInvalidDeptId()
	{
		setRequestPathInfo("/SignUp");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfSignUp");
		actionPerform();
		verifyForward("pageOfSignUp");
		verifyNoActionErrors();

		UserForm userForm = (UserForm)getActionForm();
		userForm.setInstitutionId(0l);
		userForm.setDepartmentId(0l);
		userForm.setCancerResearchGroupId(0l);
		String emailAddress = "signUpUser"+UniqueKeyGeneratorUtil.getUniqueKey()+"@test.com";
		userForm.setEmailAddress(emailAddress);
		userForm.setConfirmEmailAddress(emailAddress);
		userForm.setLastName(emailAddress);
		userForm.setFirstName(emailAddress);
		userForm.setStreet(emailAddress);
		userForm.setCity(emailAddress);
		userForm.setZipCode("111111");
		userForm.setCountry("United States");
		userForm.setPhoneNumber("444-556-8889");
		userForm.setState("Alabama");
		setRequestPathInfo("/SignUpUserAdd");
		setActionForm(userForm);
		actionPerform();
		verifyActionErrors(new String[]{"errors.item"});

	}

	public void testApproveUser()
	{
		setRequestPathInfo("/ApproveUserShow");
		addRequestParameter("pageNum", "1");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		User signedUpUser = (User)TestCaseUtility.getNameObjectMap("SignedUpUser");
		List userList = new ArrayList();
		userList=(List)getSession().getAttribute("originalDomainObjectList");
		for(int i=0;i<userList.size();i++)
		{
			User user = (User)userList.get(i);
			if(user.getEmailAddress().equals(signedUpUser.getEmailAddress()))
			{
				signedUpUser.setId(user.getId());
				break;
			}
		}

		setRequestPathInfo("/UserDetailsShow");
		addRequestParameter("id", signedUpUser.getId()+"");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		setRequestPathInfo("/User");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfApproveUser");
		actionPerform();
		verifyForward("pageOfApproveUser");
		verifyNoActionErrors();

		UserForm userForm = (UserForm)getActionForm();
		userForm.setStatus("Approve");

		setRequestPathInfo("/ApproveUserEdit");
//		addRequestParameter("operation", "edit");
		//addRequestParameter("pageOf", "pageOfApproveUser");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}


}
