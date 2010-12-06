package edu.wustl.catissuecore.smoketest.login;

import edu.wustl.catissuecore.actionForm.LoginForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.catissuecore.testcase.util.DataObject;
import edu.wustl.catissuecore.testcase.util.TestCaseDataUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

public class FirstTimeLoginTestCase extends CaTissueSuiteSmokeBaseTest
{
	/**
	 * Test First Time Login.
	 */

	public void testFirstTimeLogin()
	{
		setRequestPathInfo("/Login");
		DataObject dataObject = TestCaseDataUtil.getDataObject("testFirstTimeLogin");
		String[] arr = dataObject.getValues();
		System.out.println("Parameters : "+ arr);
		addRequestParameter("loginName", arr[0]);
		addRequestParameter("password", arr[1]);
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
		addRequestParameter("newPassword", arr[2]);
		addRequestParameter("confirmNewPassword", arr[2]);
		//addRequestParameter("access","denied");
		actionPerform();
		verifyForward("success");

		System.out.println("----"+getActualForward());
	}
}
