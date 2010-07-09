package edu.wustl.catissuecore.testcase.login;

import edu.wustl.catissuecore.actionForm.LoginForm;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

public class FirstTimeLoginTestCase extends CaTissueSuiteBaseTest
{
	/**
	 * Test First Time Login.
	 */

	public void testFirstTimeLogin()
	{
		setRequestPathInfo("/Login");
		addRequestParameter("loginName", "admin@admin.com");
		addRequestParameter("password", "Test123");
		//addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("access_denied");
		LoginForm loginForm = (LoginForm)getActionForm();


		String operation = (String) request.getAttribute(Constants.OPERATION);
		String pageOf = (String) request.getAttribute(Constants.PAGE_OF);
		SessionDataBean sessionData = null;
		if(getSession().getAttribute(Constants.TEMP_SESSION_DATA) != null)
		{
		sessionData = (SessionDataBean)getSession().getAttribute(Constants.TEMP_SESSION_DATA);
		} else
		{
		sessionData = (SessionDataBean)getSession().getAttribute(Constants.SESSION_DATA);
		}
		String userId = sessionData.getUserId().toString();

		setRequestPathInfo("/UpdatePassword");

		addRequestParameter("id",userId);
		addRequestParameter("operation", "");
		addRequestParameter("pageOf",pageOf);
		addRequestParameter("oldPassword", "Test123");
		addRequestParameter("newPassword", "Login123");
		addRequestParameter("confirmNewPassword", "Login123");
		//addRequestParameter("access","denied");
		actionPerform();
		verifyForward("success");

		System.out.println("----"+getActualForward());
	}
}
