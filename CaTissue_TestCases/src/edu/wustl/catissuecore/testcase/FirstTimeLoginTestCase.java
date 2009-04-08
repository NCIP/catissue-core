package edu.wustl.catissuecore.testcase;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.LoginForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

public class FirstTimeLoginTestCase extends CaTissueSuiteBaseTest 
{
	/**
	 * Test Department Add.
	 */
	@Test
	public void testFirstTimeLogin()
	{
		setRequestPathInfo("/Login");
		addRequestParameter("loginName", "new@user.com");
		addRequestParameter("password", "Login123");
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
		addRequestParameter("oldPassword", "Login123");
		addRequestParameter("newPassword", "Test123");
		addRequestParameter("confirmNewPassword", "Test123");
		//addRequestParameter("access","denied");
		actionPerform();
		
		
		System.out.println("----"+getActualForward());
	}
}
