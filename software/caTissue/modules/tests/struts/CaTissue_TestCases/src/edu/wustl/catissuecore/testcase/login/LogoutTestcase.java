package edu.wustl.catissuecore.testcase.login;

import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;


public class LogoutTestcase extends CaTissueSuiteBaseTest
{
	/**
	 * Negative Test Case.
	 * Test logout.
	 */

	public void testLogout()
	{
		setRequestPathInfo("/Logout") ;
		actionPerform();
//		verifyForward("success");k
		CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN = null;
	}


}
