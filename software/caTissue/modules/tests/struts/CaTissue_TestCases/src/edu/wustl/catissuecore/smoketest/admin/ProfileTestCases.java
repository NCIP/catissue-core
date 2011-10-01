package edu.wustl.catissuecore.smoketest.admin;

import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.testframework.util.DataObject;

public class ProfileTestCases extends CaTissueSuiteSmokeBaseTest {
	public ProfileTestCases(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public ProfileTestCases(String name)
	{
		super(name);
	}
	public ProfileTestCases()
	{
		super();
	}
	public void testUserProfileEdit()
	{
		String arr[] = getDataObject().getValues();
		UserForm userForm = new UserForm();

		setRequestPathInfo("/UserProfileEdit");
		addRequestParameter("pageOf","pageOfUserProfile");
		setActionForm(userForm);
		actionPerform();
		verifyForward("pageOfUserProfile");

		userForm = (UserForm) getActionForm();
		setRequestPathInfo("/User");
		addRequestParameter("pageOf","pageOfUserProfile");
		addRequestParameter("operation","edit");
		addRequestParameter("menuSelected","1");
		setActionForm(userForm);
		actionPerform();
		verifyForward("pageOfUserProfile");


		userForm.setCity(arr[0]);
		userForm.setState(arr[1]);
		userForm.setCountry(arr[2]);
		userForm.setZipCode(arr[3]);
		userForm.setPhoneNumber(arr[4]);
		userForm.setFirstName(arr[5]);
		userForm.setLastName(arr[6]);

		setRequestPathInfo("/UserEditProfile");
		//addRequestParameter("pageOf","pageOfUserProfile");
		setActionForm(userForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.edit.successOnly"});

	}
	public void testPasswordChange()
	{
		String arr[] = getDataObject().getValues();
		UserForm userForm = new UserForm();

		setRequestPathInfo("/ChangePassword");
		addRequestParameter("operation","edit");
		addRequestParameter("pageOf","pageOfChangePassword");
		setActionForm(userForm);
		actionPerform();
		verifyForward("pageOfChangePassword");

		userForm = (UserForm) getActionForm();
		userForm.setOldPassword(arr[0]);
		userForm.setNewPassword(arr[1]);
		userForm.setConfirmNewPassword(arr[1]);
		userForm.setId(Long.parseLong(arr[2]));

		setRequestPathInfo("/UpdatePassword");
		addRequestParameter("access","denied");
		setActionForm(userForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();
		
	}
}
